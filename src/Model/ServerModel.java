package Model;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import Controller.ServerController;

public class ServerModel implements Runnable{
	private ServerSocket server;
	private ServerController controller;
	
	public void setController(ServerController controller) {
		this.controller = controller;
	}
	
	public ServerModel(int port) {
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
				new Thread(new ClientHandler(soc)).start();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}

class ClientHandler extends Thread{
	private Socket soc;
	
	public ClientHandler(Socket soc) {
		this.soc = soc;
	}
	
	@Override
	public void run() {
		while(true) {
			
		}
	}
}

