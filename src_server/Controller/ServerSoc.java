package Controller;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerSoc implements Runnable{
	private ServerSocket server;
	private ControllerEventListener controller;
	
	public ServerSoc(int port, ControllerEventListener controller) {
		this.controller = controller;
		try {
			server = new ServerSocket(port);
		} catch (IOException e) {
			System.out.println("Server không mở cổng được " +e.getMessage() );
		}
	}
	
	@Override
	public void run() {
		int count = 1;
		while(true) {
			try {
				Socket soc = server.accept();
				System.out.println("Client accepted" + count++);
				ControlConnectionClientHandle ccch = new ControlConnectionClientHandle(soc, controller);
				ccch.send("220-Welcome to my simple FTP server");
				ccch.send("220 Ready");
				new Thread(ccch).start();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
