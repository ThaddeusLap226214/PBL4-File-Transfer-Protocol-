package FTPController;

import FTP_Protocol.ServerSoc;
import View.*;

public class FTPController implements FTPControllerEventListener{
	private ViewAdminFTPServer viewConnect;
	
	public void setViewConnect(ViewAdminFTPServer view) {
		this.viewConnect = view;
	}
	
	public FTPController(ViewAdminFTPServer viewConnect) {
		setViewConnect(viewConnect);
		//cổng 21
		ServerSoc server = new ServerSoc(21, this);
		new Thread(server).start();
	}
		
	@Override
	public void onClientCommand(int session, String username, String clientAddress, String command) {
		// TODO Auto-generated method stub
		viewConnect.appendLog(session, username, clientAddress, command);
	}

	@Override
	public void onServerRequest(int session, String username, String clientAddress, String request) {
		// TODO Auto-generated method stub
		viewConnect.appendLog(session, username, clientAddress, request);
	}
}
