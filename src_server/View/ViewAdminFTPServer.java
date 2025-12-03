package View;

import FTPController.FTPController;

public class ViewAdminFTPServer {
	private FTPController controller;
	
	public void setController(FTPController controller) {
		this.controller = controller;
	}

	public void appendLog(int sessionId, String username, String clientAddress, String msg) {
		// TODO Auto-generated method stub
		System.out.println("id: "+ sessionId + " user: "+ username + " msg: " + msg);
	}
}
