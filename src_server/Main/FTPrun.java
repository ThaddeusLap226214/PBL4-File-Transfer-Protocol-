package Main;

import java.net.InetAddress;
import java.net.UnknownHostException;

import AdminController.AdminController;
import FTPController.FTPController;
import Model.ModelBO;
import View.*;

public class FTPrun {
	private ViewAdminConfigure viewConfig = new ViewAdminConfigure();
	private ViewAdminFTPServer viewConnect = new ViewAdminFTPServer(viewConfig);
	private FTPController controllerFTP;
	private AdminController controllerAdmin;

	public FTPrun() {
		controllerFTP = new FTPController(viewConnect);
		controllerAdmin = new AdminController(viewConfig);
	}

	public static void main(String[] args) {

		try {
			System.out.println("Server running at:");
			System.out.println("LAN address: " + InetAddress.getLocalHost().getHostAddress());
			System.out.println("Port: 5000");
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		new FTPrun();
	}
}
