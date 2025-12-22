package FTP_Protocol;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.file.AccessDeniedException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.NoSuchFileException;
import java.util.ArrayList;
import java.util.List;

import FTPEntity.Session;
import FTPSystemData.SystemData;
import Model.LogicCoordinator;
import Model.Bean.FolderPath;

public class CommandHandle {
	private LogicCoordinator modelBO = new LogicCoordinator();
	private DataHandle dataHandle = new DataHandle();

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
			case "CWD":
				handleCWD(argument, ccch, session);
				return;
			case "CDUP":
				handleCDUP(argument, ccch, session);
				return;
			case "PASV":
				handlePASV(argument, ccch, session);
				return;
			case "LIST":
				handleLIST(argument, ccch, session);
				return;
			case "RETR":
				handleRETR(argument, ccch, session);
				return;
			case "STOR":
				handleSTOR(argument, ccch, session);
				return;
			case "DELE":
				handleDELE(argument, ccch, session);
				return;
			case "MKD":
				handleMKD(argument, ccch, session);
				return;
			case "RMD":
				handleRMD(argument, ccch, session);
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
			//Thiết lập các gốc trong cacheFolder
			List<FolderPath> listFd = modelBO.getListFolderPermission(session.getUserId());
			
				//thêm vào cache
			for(FolderPath fp : listFd) {
				session.getCacheFolder().add(fp.getVirtualPath(), fp);
			}
			ccch.send("230 User logged in, proceed.");
			ccch.sendAppend(argument);
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
	private void handleCWD(String argument, ControlConnectionClientHandle ccch, Session session) {
		//Kiểm tra có tồn tại CWD không
		//để làm sau
		
		if (argument == null || argument.isBlank()) {
	        ccch.send("501 Syntax error in parameters.");
	        return;
	    }
		
		//CDUP
		if(argument.equals("..")) {
			handleCDUP(argument, ccch, session);
			return;
		}
		//Có tham số
		String currentDir = session.getCurrentDirectory();
	    String nextPath;

	    // absolute virtual path
	    if (argument.startsWith("/")) {
	        nextPath = argument;
	    } else {
	        nextPath = currentDir.endsWith("/")
	                ? currentDir + argument
	                : currentDir + "/" + argument;
	    }
	    nextPath = session.getCacheFolder().normalizeVirtualPath(nextPath);
	    
	    //nếu là "/" thì luôn đúng
	    if (nextPath.equals("/")) {
	        session.setCurrentDirectory("/");
	        ccch.send("250 CWD command successful.");
	        return;
	    }

	    // Kiểm tra có từ root
	    if (session.getCacheFolder().mapVirtualToNative(nextPath) == null) {
	        ccch.send("550 Failed to change directory.");
	        return;
	    }

	    session.setCurrentDirectory(nextPath);
	    ccch.send("250 CWD command successful.");
	}
	private void handleCDUP(String argument, ControlConnectionClientHandle ccch, Session session) {
		String currentDir = session.getCurrentDirectory();
		//Lấy thư mục cha trong cache
		String parentDir = session.getCacheFolder().getParentDir(currentDir);
		//Kiểm tra
		if (session.getCacheFolder().mapVirtualToNative(parentDir) == null) {
	        ccch.send("550 Failed to change directory.");
	        return;
	    }
		session.setCurrentDirectory(parentDir);
		ccch.send("250 CDUP command successful.");
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
			//Kiểm tra PASV, PORT
			if (!dataConnect.isModeSelected()) {
				ccch.send("425 Use PASV or PORT first.");
	            return;
	        }
			Socket dataSocket = dataConnect.openDataSocket();
			ccch.send("150 Opening data connection.");
			OutputStream dataOut = dataSocket.getOutputStream();
			boolean transferTrue = false; //cờ để xem truyền thành công không
			//lấy thư mục cần LIST
			String virtualPathNeedList;
			if (argument == null || argument.isEmpty()) {
				//nếu lệnh LIST không tham số thì dùng thư mục hiện tại
				virtualPathNeedList = session.getCurrentDirectory();
			} else {
				virtualPathNeedList = argument.trim();
			}
			//trường hợp lệnh LIST "/"
			if(virtualPathNeedList.equals("/")) {
				//Lấy danh sách folderPath gốc từ cache
				List<FolderPath> listFd = session.getCacheFolder().getListFolderRoot();
				//gọi HandleData để xử lý chuyển đổi dữ liệu và gửi đi
				transferTrue = dataHandle.HandleSendFolderData(listFd, dataOut);
			} else {
				//Kiểm tra đường dẫn ảo phải là con của các root.
				String nativePathNeedList = session.getCacheFolder().mapVirtualToNative(virtualPathNeedList);
				if(nativePathNeedList == null) {
					ccch.send("550 Requested action not taken. Directory not found.");
					return;
				}
				List<FolderPath> listFdChild = dataHandle.getListFolderChild(virtualPathNeedList, nativePathNeedList);
				//gọi HandleData để xử lý chuyển đổi dữ liệu và gửi đi
				transferTrue = dataHandle.HandleSendFolderData(listFdChild, dataOut);
			}
			dataOut.flush();
			
	        dataOut.close();
	        dataSocket.close();
	        dataConnect.close();
	        dataConnect.resetMode();
	        if (transferTrue == true) {
		        ccch.send("226 Transfer complete.");
	        }
	        else {
				ccch.send("426 Connection closed; transfer aborted.");
	        }
		} catch (IOException e) {
			ccch.send("425 Can't open data connection.");
	        e.printStackTrace();
		}
	}
	
	private void handleRETR(String argument, ControlConnectionClientHandle ccch, Session session) {
		//Kiểm tra login trước
		if(!session.isLoggedIn()) {
			ccch.send("530 Not logged in yet.");
			return;
		}
		try {
			DataConnectionHandle dataConnect = session.getDataConnect();
			//Kiểm tra PASV PORT
			if (!dataConnect.isModeSelected()) {
				ccch.send("425 Use PASV or PORT first.");
	            return;
	        }
			Socket dataSocket = dataConnect.openDataSocket();
			ccch.send("150 Opening data connection.");
			OutputStream dataOut = dataSocket.getOutputStream();
			boolean transferTrue = false; //cờ để xem truyền thành công không
			//lấy đường dẫn file cần truyền
			String virtualPath, nativePath;
			//Lấy đường dẫn thực
			if(!argument.startsWith("/")) {
				virtualPath = session.getCurrentDirectory() + "/" + argument;
			} else {
				virtualPath = session.getCacheFolder().normalizeVirtualPath(argument);
			}
			nativePath = session.getCacheFolder().mapVirtualToNative(virtualPath);
			if(nativePath == null) {
				ccch.send("550 Requested action not taken. Directory not found.");
				return;
			}
			
			//gọi lên SystemData để kiểm tra file có tồn tại không
			if(!SystemData.existFile(nativePath)) {
				ccch.send("550 File not found or not a file");
				return;
			}
			
			//gọi lên DataHandle để truyền file
			transferTrue = dataHandle.HandleSendFileData(nativePath, dataOut);
			
			dataOut.flush();
			
	        dataOut.close();
	        dataSocket.close();
	        dataConnect.close();
	        dataConnect.resetMode();
	        if (transferTrue == true) {
		        ccch.send("226 Transfer complete.");
	        }
	        else {
				ccch.send("426 Connection closed; transfer aborted.");
	        }
		} catch (IOException e) {
			ccch.send("425 Can't open data connection.");
	        e.printStackTrace();
		}
	}
	
	private void handleSTOR(String argument, ControlConnectionClientHandle ccch, Session session) {
		//Kiểm tra login trước
		if(!session.isLoggedIn()) {
			ccch.send("530 Not logged in yet.");
			return;
		}
		try {
			DataConnectionHandle dataConnect = session.getDataConnect();
			//Kiểm tra PASV PORT
			if (!dataConnect.isModeSelected()) {
				ccch.send("425 Use PASV or PORT first.");
	            return;
	        }
			Socket dataSocket = dataConnect.openDataSocket();
			ccch.send("150 Opening data connection.");
			InputStream dataIn = dataSocket.getInputStream();
			boolean transferTrue = false; //cờ để xem truyền thành công không
			//tạo đường dẫn file cần truyền
			String virtualPath, nativePath;
			//Tạo đường dẫn thực
			if(!argument.startsWith("/")) {
				virtualPath = session.getCurrentDirectory() + "/" + argument;
			} else {
				virtualPath = session.getCacheFolder().normalizeVirtualPath(argument);
			}
			nativePath = session.getCacheFolder().mapVirtualToNative(virtualPath);
			if(nativePath == null) {
				ccch.send("550 Requested action not taken. Directory not found.");
				return;
			}
			
			//gọi lên SystemData để nhận lại tên file hiệu chỉnh nếu file bị trùng
			nativePath = SystemData.CheckExistFile(nativePath);
			
			//gọi lên DataHandle để nhận file
			transferTrue = dataHandle.HandleReceiveFileData(nativePath, dataIn);			
	        
			dataIn.close();
	        dataSocket.close();
	        dataConnect.close();
	        dataConnect.resetMode();
	        if (transferTrue == true) {
		        ccch.send("226 Transfer complete.");
	        }
	        else {
				ccch.send("426 Connection closed; transfer aborted.");
	        }
		} catch (IOException e) {
			ccch.send("425 Can't open data connection.");
	        e.printStackTrace();
		}
	}
	
	private void handleDELE(String argument, ControlConnectionClientHandle ccch, Session session) {
		//Kiểm tra login trước
		if(!session.isLoggedIn()) {
			ccch.send("530 Not logged in yet.");
			return;
		}
		
		//Tạo đường dẫn thực
		String virtualPath, nativePath;
		if(!argument.startsWith("/")) {
			virtualPath = session.getCurrentDirectory() + "/" + argument;
		} else {
			virtualPath = session.getCacheFolder().normalizeVirtualPath(argument);
		}
		nativePath = session.getCacheFolder().mapVirtualToNative(virtualPath);
		if(nativePath == null) {
			ccch.send("550 Requested action not taken. Directory not found.");
			return;
		}
		
		//Kiểm tra file tồn tại và không phải thư mục rỗng
		if(!SystemData.existFile(nativePath)) {
			ccch.send("550 File not found or not a file");
			return;
		}		
		
		//Xóa file
		boolean DeleSucces = dataHandle.deleteFile(nativePath);
		if(!DeleSucces) {
			ccch.send("450 File busy or Local error deleting file.");
			return;
		}
		ccch.send("250 Requested file action okay, completed.");
	}
	
	private void handleMKD(String argument, ControlConnectionClientHandle ccch, Session session) {
		//Kiểm tra login trước
		if(!session.isLoggedIn()) {
			ccch.send("530 Not logged in yet.");
			return;
		}
		
		//Tạo đường dẫn thực
		String virtualPath, nativePath;
		if(!argument.startsWith("/")) {
			virtualPath = session.getCurrentDirectory() + "/" + argument;
		} else {
			virtualPath = session.getCacheFolder().normalizeVirtualPath(argument);
		}
		nativePath = session.getCacheFolder().mapVirtualToNative(virtualPath);
		if(nativePath == null) {
			ccch.send("550 Requested action not taken. Directory not found.");
			return;
		}
		
//		//Kiểm tra thư mục chưa tồn tại trước khi tạo mới
//		if(SystemData.exists(nativePath)) {
//			ccch.send("550 Directory already exists");
//			return;
//		}		
		
		//Tạo thư mục
		try {
			dataHandle.createDirectory(nativePath);
			ccch.send("257 \"" + virtualPath + "\" directory created.");
		} catch (FileAlreadyExistsException e) {
	        ccch.send("550 Directory already exists.");
	    } catch (AccessDeniedException e) {
	        ccch.send("550 Permission denied.");
	    } catch (IOException e) {
	        ccch.send("550 Failed to create directory.");
	    }
	}
	
	private void handleRMD(String argument, ControlConnectionClientHandle ccch, Session session) {
		//Kiểm tra login trước
		if(!session.isLoggedIn()) {
			ccch.send("530 Not logged in yet.");
			return;
		}
		
		//Tạo đường dẫn thực
		String virtualPath, nativePath;
		if(!argument.startsWith("/")) {
			virtualPath = session.getCurrentDirectory() + "/" + argument;
		} else {
			virtualPath = session.getCacheFolder().normalizeVirtualPath(argument);
		}
		nativePath = session.getCacheFolder().mapVirtualToNative(virtualPath);
		if(nativePath == null) {
			ccch.send("550 Requested action not taken. Directory not found.");
			return;
		}
					
		//Xóa thư mục
		try {
			dataHandle.deleteDirectory(nativePath);
			ccch.send("250 RMD command successful.");
		} catch (DirectoryNotEmptyException e) {
	        ccch.send("550 Directory not empty.");
	    } catch (NoSuchFileException e) {
	        ccch.send("550 Directory not found.");
	    } catch (IOException e) {
	        ccch.send("550 Failed to remove directory.");
	    }
	}
	
	
	private void handleQUIT(String argument, ControlConnectionClientHandle ccch, Session session) {
//		modelBO.handleQUIT();
//		ccch.close();
		ccch.shutdown();
	}

	private void handleCommandNotImplm(String argument, ControlConnectionClientHandle ccch, Session session) {
		ccch.send("502 Command not implemented.");
	}
}
