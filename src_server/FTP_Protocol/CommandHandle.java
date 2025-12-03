package FTP_Protocol;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

import Model.ModelBO;
import Utils.FolderInfo;

public class CommandHandle {
	private ModelBO modelBO = new ModelBO();

	public void handle(String cmd, ControlConnectionClientHandle ccch, Session session) {
		String[] Cmd = cmd.trim().split("\\s+", 2);
		String command = Cmd[0].toUpperCase();
		String argument = (Cmd.length > 1) ? Cmd[1] : "";
		
		switch(command) {
			case "AUTH":
				handleAUTH(argument, ccch, session);
				return;
			case "OPTS":
				handleOPTS(argument, ccch, session);
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
	
	private void handleOPTS(String argument, ControlConnectionClientHandle ccch, Session session) {
		ccch.send("200 Utf8 mode enabled");
	}

	private void handleAUTH(String argument, ControlConnectionClientHandle ccch, Session session) {
//		modelBO.handleAUTH(argument);
		ccch.send("502 Command not implemented");
	}
	
	private void handleUSER(String username, ControlConnectionClientHandle ccch, Session session) {
		//kiểm tra user
		if(modelBO.findUser(username)) {
			session.setUsername(username);
			ccch.send("331 Password required for " + username);
		}
		else {
			session.setUsername(null);
			ccch.send("430 Can not find " + username);
		}
	}
	
	private void handlePASS(String argument, ControlConnectionClientHandle ccch, Session session) {
		//Kiểm tra username đã hợp lệ chưa trước
		if(session.getUsername() == null || session.getUsername().isEmpty()) {
			ccch.send("503 use USER first.");
			return;
		}
		
		//Nếu đã có username, kiểm tra mật khẩu
		int userId = modelBO.checkLogin(session.getUsername(), argument);
		//nếu đăng xác thực thành công
		if(userId != -1) {
			session.setLoggedIn(true);	//xác nhận session đã login
			session.setUserId(userId);	//set userId dùng cho các lần truy vấn sau
			session.setCurrentDirectory("/");	//đặt thư mục hiện tại là "/" làm gốc
			//khởi tạo gốc trong cacheFolder
			FolderInfo root = new FolderInfo(0, "/");
			session.getCacheFolder().put("/", root);
			ccch.send("230 User logged in, proceed.");
		} else {
			session.setUsername(null);
			ccch.send("504 Wrong password, log in again from USER command.");
		}
	}
	
	private void handleFEAT(String argument, ControlConnectionClientHandle ccch, Session session) {
//		modelBO.handleFEAT();
		ccch.send("211-Features:");
		ccch.send(" UTF8");
		ccch.send(" PASV");
		ccch.send(" PORT");
		ccch.send("211 End.");
	}
	
	private void handleTYPE(String argument, ControlConnectionClientHandle ccch, Session session) {
		//Kiểm tra loggin
		if(!session.isLoggedIn()) {
			ccch.send("530 Not logged in yet.");
			return;
		}
		//Chỉ hỗ trợ kiểu nhị phân, không hỗ trợ Ascci TYPE A
		if(argument.equalsIgnoreCase("I")) {
			ccch.send("200 Type set to I.");
		}else {
			ccch.send("504 Command not implemented for that parameter.");
		}
	}
	
	private void handlePWD(String argument, ControlConnectionClientHandle ccch, Session session) {
		//Kiểm tra login trước
		if(!session.isLoggedIn()) {
			ccch.send("530 Not logged in yet.");
			return;
		}
		
		//Lấy thư mục được cấp cho user
		String path = session.getCurrentDirectory();
		
		//Nếu lấy được thư mục
		if(path != null && !path.isEmpty()) {
			ccch.send("257 \"" + path + "\" is current directory.");
		} else {
			ccch.send("550 Can't get current directory.");
		}
	}	
	
	private void handlePASV(String argument, ControlConnectionClientHandle ccch, Session session) {
		//Kiểm tra login trước
		if(!session.isLoggedIn()) {
			ccch.send("530 Not logged in yet.");
			return;
		}
		try {
			DataConnectionHandle dataConnect = session.getDataConnect();
			
			//Mở cổng để client kết nối
			int port = dataConnect.enterPassiveMode();
			String host = InetAddress.getLocalHost().getHostAddress();
			//Tạo mã phản hồi
			int p1 = port / 256;
			int p2 = port % 256;
			String[] parts = host.split("\\.");
			String reply = String.format(
					"227 Entering Passive Mode (%s,%s,%s,%s,%d,%d)",
				    parts[0], parts[1], parts[2], parts[3], p1, p2
					);
			//String reply = "227 Entering Passive Mode (127,0,0,1,p1,p2)";
			ccch.send(reply);
		} catch (IOException e) {
			ccch.send("425 Can't open passive connection.");
		}
	}
	
	private void handleLIST(String argument, ControlConnectionClientHandle ccch, Session session) {
		//Kiểm tra login trước
		if(!session.isLoggedIn()) {
			ccch.send("530 Not logged in yet.");
			return;
		}
		try {
			DataConnectionHandle dataConnect = session.getDataConnect();
			
			if (!dataConnect.isModeSelected()) {
				ccch.send("425 Use PASV or PORT first.");
	            return;
	        }
			Socket dataSocket = dataConnect.openDataSocket();
			ccch.send("150 Opening data connection.");
			OutputStream dataOut = dataSocket.getOutputStream();
			
			//lấy thư mục cần LIST
			String path;
			if (argument == null || argument.isEmpty()) {
				//nếu lệnh LIST không tham số thì dùng thư mục hiện tại
				path = session.getCurrentDirectory();
				//mặc định viết cho trường hợp một lần đầu lấy LIST
				ArrayList<FolderInfo> listFd = modelBO.getListFolderPermission(session.getUserId());
	        } else {
//	            path = model.resolvePath(argument); // tự viết thêm hàm để ghép tương đối/tuyệt đối
	        }
			
			//gọi sang ModelBO để lấy danh sách
//			String listing = model.getDirectoryListing(path);
			
			//Gửi danh sách
//			dataOut.write(listing.getBytes());
			dataOut.flush();
			
	        dataOut.close();
	        dataSocket.close();
	        dataConnect.close();
	        dataConnect.resetMode();
	        ccch.send("226 Transfer complete.");
		} catch (IOException e) {
			ccch.send("425 Can't open data connection.");
	        e.printStackTrace();
		}
	}
	
	private void handleQUIT(String argument, ControlConnectionClientHandle ccch, Session session) {
//		modelBO.handleQUIT();
		ccch.close();
	}

	private void handleCommandNotImplm(String argument, ControlConnectionClientHandle ccch, Session session) {
		ccch.send("502 Command not implemented.");
	}
}
