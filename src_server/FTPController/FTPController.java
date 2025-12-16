package FTPController;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;

import FTP_Protocol.ServerSoc;
import View.*;

public class FTPController implements FTPControllerEventListener{
	private ViewAdminFTPServer viewConnect;
//	private ServerSoc server;
	
	public void setViewConnect(ViewAdminFTPServer view) {
		this.viewConnect = view;
	}
//	public void setServerFTP(ServerSoc server) {
//		this.server = server;
//	}
	
	public FTPController(ViewAdminFTPServer viewConnect) {
		setViewConnect(viewConnect);
//		setServerFTP(server);
		//cổng 21
		ServerSoc server = new ServerSoc(21, this);
		new Thread(server).start();
	}
		
	@Override
	public void onClientCommand(LocalDateTime time, int sessionID, InetAddress host, String command) {
		// TODO Auto-generated method stub
		viewConnect.appendLog(time, "FTP Session " +sessionID+ " "+ host.getHostAddress(), "command", command);
	}

	@Override
	public void onServerResponse(LocalDateTime time, int sessionID, InetAddress host, String response) {
		// TODO Auto-generated method stub
		viewConnect.appendLog(time, "FTP Session " +sessionID+ " "+ host.getHostAddress(), "response", response);
	}

	@Override
	public void onClientLoginSuccess(LocalDateTime time, int id, InetAddress IP, String Username) {
		viewConnect.appendSession(time, id, IP, Username);
	}

//	public void StartServerFTP() {
//		new Thread(this.server).start();
//		try {
//			viewConnect.appendLog(LocalDateTime.now(), "Server", null, "Server running at: "+InetAddress.getLocalHost().getHostAddress()+":21");
//		} catch (UnknownHostException e) {
//		}
//	}
}
