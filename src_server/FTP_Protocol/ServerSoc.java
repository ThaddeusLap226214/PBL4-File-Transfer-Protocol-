package FTP_Protocol;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import FTPController.FTPControllerEventListener;

public class ServerSoc implements Runnable{
	private CommandHandle ch = new CommandHandle();
	private ServerSocket server;
	private FTPControllerEventListener controller;
	
	public ServerSoc(int port, FTPControllerEventListener controller) {
		this.controller = controller;
		try {
			server = new ServerSocket(port);
		} catch (IOException e) {
			System.out.println("Server không mở cổng được " +e.getMessage() );
		}
	}
	
	@Override
	public void run() {
		int count = 0;
		while(true) {
			try {
				Socket soc = server.accept();
				System.out.println("Client accepted, session " + ++count);
				
				//hàm dựng với session
				ControlConnectionClientHandle ccch = new ControlConnectionClientHandle(soc, ch, controller, count);
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
