package Model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import Controller.ClientController;

public class ClientModel implements Runnable {
	private Socket soc;
	private ClientController controller;
	
	private BufferedReader reader;
	private BufferedWriter writer;
	
	private void setDataIOStream() {
		if(soc.isConnected()) {
			try {
				reader = new BufferedReader(new InputStreamReader(soc.getInputStream(), StandardCharsets.UTF_8));
				writer = new BufferedWriter(new OutputStreamWriter(soc.getOutputStream(),StandardCharsets.UTF_8));
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	public void setController(ClientController controller) {
		this.controller = controller;
	}
	
	public ClientModel(InetAddress address, int port) {
		try {
			soc = new Socket(address.getHostName(), port);
			setDataIOStream();
			Thread x = new Thread(this);
			x.start();
		}catch(IOException e) {
			System.out.println("Lỗi kết nối "+e.getMessage());
		}
	}
	//luồng để nhận phản hồi
	@Override
	public void run() {
		while(true) {
	
		}
	}

}
