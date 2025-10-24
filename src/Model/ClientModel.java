package Model;

import java.io.*;
import java.net.*;
import Controller.ClientController;

public class ClientModel implements Runnable {
	private Socket soc;
	private ClientController controller;
	private BufferedReader in;
	private BufferedWriter out;

	public ClientModel() {
	}

	public void setController(ClientController controller) {
		this.controller = controller;
	}

	public void connect(String host, int port, String user, String pass) {
		try {
			soc = new Socket(host, port);
			controller.onMessageFromServer("Đã kết nối đến " + host + ":" + port);

			in = new BufferedReader(new InputStreamReader(soc.getInputStream()));
			out = new BufferedWriter(new OutputStreamWriter(soc.getOutputStream()));

			// Gửi user/pass
			out.write("USER " + user + "\n");
			out.flush();
			out.write("PASS " + pass + "\n");
			out.flush();

			new Thread(this).start();
		} catch (Exception e) {
			controller.onMessageFromServer("Lỗi kết nối: " + e.getMessage());
		}
	}

	public void sendMessage(String msg) {
		try {
			if (out != null) {
				out.write(msg + "\n");
				out.flush();
			}
		} catch (IOException e) {
			controller.onMessageFromServer("Không gửi được: " + e.getMessage());
		}
	}

	public void disconnect() {
		try {
			if (soc != null && !soc.isClosed()) {
				soc.close();
				controller.onMessageFromServer("Đã ngắt kết nối.");
			}
		} catch (IOException e) {
			controller.onMessageFromServer("Lỗi khi ngắt kết nối: " + e.getMessage());
		}
	}

	@Override
	public void run() {
		try {
			String line;
			while ((line = in.readLine()) != null) {
				controller.onMessageFromServer(line);
			}
		} catch (IOException e) {
			controller.onConnectionClosed();
		}
	}
}
