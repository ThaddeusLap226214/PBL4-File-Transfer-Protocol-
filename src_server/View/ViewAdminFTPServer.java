package View;

import Controller.Controller;

public class ViewAdminFTPServer {
	private Controller controller;
	
	public void setController(Controller controller) {
		this.controller = controller;
	}

	public void appendLog(int session, String username, String clientAddress, String msg) {
		// TODO Auto-generated method stub
		System.out.println(msg);
	}
}
