package Main;

import java.net.InetAddress;
import java.net.UnknownHostException;

import Controller.Controller;
import Model.ModelBO;
import View.*;

public class FTPrun {
	private ViewAdminFTPServer viewConnect = new ViewAdminFTPServer();
	private ViewConfigure viewConfig = new ViewConfigure();
	private Controller controller;
	private ModelBO model;
	
	public FTPrun() {
		controller = new Controller(viewConnect, viewConfig);
	}

	public static void main(String[] args) {
		
		try {
			System.out.println(InetAddress.getLocalHost().getHostAddress());
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		new FTPrun();
	}
}
