package FTP_Protocol;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

import Model.ModelBO;
import Model.Bean.User;

public class CommandHandle {
	private ModelBO modelBO = new ModelBO();

	public void handle(String cmd, ControlConnectionClientHandle ccch, Session session) {
		String[] Cmd = cmd.trim().split("\\s+", 2);
		String command = Cmd[0].toUpperCase();
		String argument = (Cmd.length > 1) ? Cmd[1] : "";

		switch (command) {
			case "AUTH":
				handleAUTH(argument, ccch, session);
				return;
			case "USER":
				handleUSER(argument, ccch, session);
				return;
			case "PASS":
				handlePASS(argument, ccch, session);
				return;
			case "FEAT":
				handleFEAT(argument, ccch, session);
				return;
			case "TYPE":
				handleTYPE(argument, ccch, session);
				return;
			case "PWD":
				handlePWD(argument, ccch, session);
				return;
			case "PASV":
				handlePASV(argument, ccch, session);
				return;
			case "LIST":
				handleLIST(argument, ccch, session);
				return;
			case "QUIT":
				handleQUIT(argument, ccch, session);
				return;
			default:
				handleCommandNotImplm(argument, ccch, session);
		}
	}

	private void handleAUTH(String argument, ControlConnectionClientHandle ccch, Session session) {
		// modelBO.handleAUTH(argument);
		ccch.send("502 Command not implemented");
	}

	private void handleUSER(String username, ControlConnectionClientHandle ccch, Session session) {
		// modelBO.handleUSER(username);
		// phản hồi mặc định away true
		// ccch.send("331 Password required for " + username);

		// Kiểm tra user
		User user = modelBO.findUser(username); // nhận về User thay vì boolean
		if (user != null) {
			ccch.send("331 Password required for " + username);
		} else {
			ccch.send("400 Cannot find " + username);
		}
	}

	private void handlePASS(String argument, ControlConnectionClientHandle ccch, Session session) {
		// modelBO.handlePASS(argument);
		ccch.send("230 User logged in, proceed.");
	}

	private void handleFEAT(String argument, ControlConnectionClientHandle ccch, Session session) {
		// modelBO.handleFEAT();
		ccch.send("211-Features:");
		ccch.send(" UTF8");
		ccch.send(" PASV");
		ccch.send(" PORT");
		ccch.send("211 End.");
	}

	private void handleTYPE(String argument, ControlConnectionClientHandle ccch, Session session) {
		// modelBO.handleTYPE(argument);
		if (argument.equalsIgnoreCase("I")) {
			ccch.send("200 Type set to I.");
		} else if (argument.equalsIgnoreCase("A")) {
			ccch.send("200 Type set to A.");
		} else {
			ccch.send("504 Command not implemented for that parameter.");
		}
	}

	private void handlePWD(String argument, ControlConnectionClientHandle ccch, Session session) {
		// modelBO.handlePWD();
		// String path = modelBO.getCurrentDirectory(session.getUsername())
		// session.setCurrentDirectory(path);
		ccch.send("257 \"" + "/" + "\" is current directory.");
	}

	private void handlePASV(String argument, ControlConnectionClientHandle ccch, Session session) {
		// modelBO.handlePASV();
		try {
			DataConnectionHandle dataConnect = session.getDataConnect();

			// Kiểm tra đăng nhập

			// Mở cổng để client kết nối
			int port = dataConnect.enterPassiveMode();
			String host = InetAddress.getLocalHost().getHostAddress();
			// Tạo mã phản hồi
			int p1 = port / 256;
			int p2 = port % 256;
			String[] parts = host.split("\\.");
			String reply = String.format(
					"227 Entering Passive Mode (%s,%s,%s,%s,%d,%d)\r\n",
					parts[0], parts[1], parts[2], parts[3], p1, p2);
			// String reply = "227 Entering Passive Mode (127,0,0,1,p1,p2)";
			ccch.send(reply);
		} catch (IOException e) {
			ccch.send("425 Can't open passive connection.\r\n");
		}
	}

	private void handleLIST(String argument, ControlConnectionClientHandle ccch, Session session) {
		// modelBO.handleLIST(argument);
		try {
			// if (!model.isLoggedIn()) {
			// ccch.send("530 Not logged in.\r\n");
			// return;
			// }
			DataConnectionHandle dataConnect = session.getDataConnect();

			if (!dataConnect.isModeSelected()) {
				ccch.send("425 Use PASV or PORT first.\r\n");
				return;
			}
			ccch.send("150 Opening data connection.\r\n");
			Socket dataSocket = dataConnect.openDataSocket();
			OutputStream dataOut = dataSocket.getOutputStream();

			// kiểm tra đường dẫn
			String path;
			if (argument == null || argument.isEmpty()) {
				// path = model.getCurrentDirectory(""); // dùng thư mục hiện tại
				path = "/";
			} else {
				// path = model.resolvePath(argument); // tự viết thêm hàm để ghép tương
				// đối/tuyệt đối
			}

			// gọi sang ModelBO để lấy danh sách
			// String listing = model.getDirectoryListing(path);

			// Gửi danh sách
			// dataOut.write(listing.getBytes());
			dataOut.flush();

			dataOut.close();
			dataSocket.close();
			dataConnect.close();
			dataConnect.resetMode();
			ccch.send("226 Transfer complete.\r\n");
		} catch (IOException e) {
			ccch.send("425 Can't open data connection.\r\n");
			e.printStackTrace();
		}
	}

	private void handleQUIT(String argument, ControlConnectionClientHandle ccch, Session session) {
		// modelBO.handleQUIT();
	}

	private void handleCommandNotImplm(String argument, ControlConnectionClientHandle ccch, Session session) {
		ccch.send("502 Command not implemented");
	}
}
