package Model;

import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;
import Controller.ServerController;

public class ServerModel implements Runnable {
	private ServerSocket serverSocket;
	private boolean running;
	private int port;
	private ServerController controller;
	private int sessionCounter = 1;
	private final List<ClientHandler> clients = new ArrayList<>();

	public ServerModel(int port) {
		this.port = port;
	}

	public void setController(ServerController controller) {
		this.controller = controller;
	}

	@Override
	public void run() {
		try {
			serverSocket = new ServerSocket(port);
			running = true;

			while (running) {
				Socket clientSocket = serverSocket.accept();

				// ✅ Di chuyển log này xuống đây để chỉ hiện khi có kết nối
				controller.appendLog("Server đang lắng nghe tại cổng " + port + "\n");

				int sessionId = sessionCounter++;
				controller.appendLog("Client kết nối: " + clientSocket.getInetAddress().getHostAddress());

				ClientHandler handler = new ClientHandler(sessionId, clientSocket, controller);
				clients.add(handler);
				new Thread(handler).start();
			}
		} catch (IOException e) {
			controller.appendLog("Lỗi server: " + e.getMessage());
		}
	}

	public void stop() {
		try {
			running = false;
			if (serverSocket != null && !serverSocket.isClosed()) {
				serverSocket.close();
			}
			controller.appendLog("Server đã dừng.");
		} catch (IOException e) {
			controller.appendLog("Lỗi khi dừng server: " + e.getMessage());
		}
	}

	// Lớp xử lý từng client
	private static class ClientHandler implements Runnable {
		private int sessionId;
		private Socket socket;
		private ServerController controller;
		private BufferedReader in;
		private BufferedWriter out;

		public ClientHandler(int sessionId, Socket socket, ServerController controller) {
			this.sessionId = sessionId;
			this.socket = socket;
			this.controller = controller;
		}

		@Override
		public void run() {
			try {
				in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

				controller.addClientSession(
						new SimpleDateFormat("HH:mm:ss").format(new Date()),
						sessionId,
						socket.getInetAddress().getHostAddress(),
						"Unknown",
						"Connected");

				String line;
				while ((line = in.readLine()) != null) {
					controller.appendLog("[Client " + sessionId + "] " + line);
					// có thể gửi ngược lại cho client
					out.write("Server received: " + line + "\n");
					out.flush();
				}

			} catch (IOException e) {
				controller.appendLog("Client " + sessionId + " đã ngắt kết nối.");
				controller.updateClientStatus(sessionId, "Disconnected");
			} finally {
				try {
					socket.close();
				} catch (IOException e) {
				}
			}
		}
	}
}
