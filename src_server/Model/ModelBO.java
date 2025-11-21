package Model;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class ModelBO {
	private ResponseListener rl;
	private DataConnectionHandle dataConnect = new DataConnectionHandle();
	
	public ModelBO(ResponseListener rl) {
		this.rl = rl;
	}
	
	public void handleAUTH(String argument) {
		rl.onRespose("502 Command not implemented");
	}
	
	public void handleUSER(String argument) {
		rl.onRespose("331 Password required for " + argument);
	}
	
	public void handlePASS(String argument) {
		rl.onRespose("230 User logged in, proceed.");
	}
	
	public void handleFEAT() {
		rl.onRespose("211-Features:");
		rl.onRespose(" UTF8");
		rl.onRespose(" PASV");
		rl.onRespose(" PORT");
		rl.onRespose("211 End.");
	}
	
	public void handleTYPE(String argument) {
		if(argument.equalsIgnoreCase("I")) {
			rl.onRespose("200 Type set to I.");
		}else if(argument.equalsIgnoreCase("A")) {
			rl.onRespose("200 Type set to A.");
		}else {
			rl.onRespose("504 Command not implemented for that parameter.");
		}
	}
	
	public void handlePWD() {
		//gọi AccountDAO kiểm tra đăng nhập, lấy id, 
		//dùng id để vào PermissionDAO lấy fid, từ fid vào PathDAO để lấy đường dẫn
		
		rl.onRespose("257 \"" + "/" + "\" is current directory.");
	}
	
	public void handlePASV() {
		try {
			//Kiểm tra đăng nhập
			
			//Mở cổng để client kết nối
			int port = dataConnect.enterPassiveMode();
			String host = InetAddress.getLocalHost().getHostAddress();
			//Tạo mã phản hồi
			int p1 = port / 256;
			int p2 = port % 256;
			String[] parts = host.split("\\.");
			String reply = String.format(
					"227 Entering Passive Mode (%s,%s,%s,%s,%d,%d)\r\n",
				    parts[0], parts[1], parts[2], parts[3], p1, p2
					);
			//String reply = "227 Entering Passive Mode (127,0,0,1,p1,p2)";
			rl.onRespose(reply);
		} catch (IOException e) {
			rl.onRespose("425 Can't open passive connection.\r\n");
		}
	}
	
	public void handleLIST(String argument) {
		try {
//			if (!model.isLoggedIn()) {
//	            ccch.send("530 Not logged in.\r\n");
//	            return;
//	        }
			if (!dataConnect.isModeSelected()) {
				rl.onRespose("425 Use PASV or PORT first.\r\n");
	            return;
	        }
			rl.onRespose("150 Opening data connection.\r\n");
			Socket dataSocket = dataConnect.openDataSocket();
			OutputStream dataOut = dataSocket.getOutputStream();
			
			//kiểm tra đường dẫn
			String path;
			if (argument == null || argument.isEmpty()) {
//	            path = model.getCurrentDirectory(""); // dùng thư mục hiện tại
				path = "/";
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
	        rl.onRespose("226 Transfer complete.\r\n");
		} catch (IOException e) {
			rl.onRespose("425 Can't open data connection.\r\n");
	        e.printStackTrace();
		}
	}
	
	public void handleQUIT() {
		
	}
	
	public void handleDEFAULT() {
		rl.onRespose("502 Command not implemented");
	}
	
}
