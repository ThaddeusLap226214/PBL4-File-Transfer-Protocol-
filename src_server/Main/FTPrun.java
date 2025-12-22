package Main;

import java.net.InetAddress;
import java.net.UnknownHostException;

import AdminController.AdminController;
import FTPControl.FTPControl;
import FTP_Protocol.ServerSoc;
import Model.LogicCoordinator;
import View.*;

public class FTPrun {
	//private ViewAdminFTPServer viewConnect = new ViewAdminFTPServer();
	private ViewAdminConfigure viewConfig = new ViewAdminConfigure();
	private ViewFTPServer viewConnect = new ViewFTPServer(viewConfig);
	private FTPControl controllerFTP;
	private AdminController controllerAdmin;
	
	public FTPrun() {
		controllerFTP = new FTPControl(viewConnect);
		controllerAdmin = new AdminController(viewConfig);
	}

	public static void main(String[] args) {
		
		try {
			System.out.println("Server running at:");
			System.out.println("LAN address: " + InetAddress.getLocalHost().getHostAddress());
				   System.out.println("Port: 21");
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		new FTPrun();
	}
}
