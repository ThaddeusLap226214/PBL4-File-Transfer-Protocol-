package Controller;

import Model.ClientModel;
import View.ClientGUI;

public class ClientController {
	private ClientGUI view;
	private ClientModel model;

	public ClientController(ClientGUI view, ClientModel model) {
		this.view = view;
		this.model = model;
	}

	// Dùng cho console (khi người dùng nhập lệnh ở dưới)
	public void checkCMD(String cmd) {
		if (cmd == null || cmd.isEmpty())
			return;
		String[] parts = cmd.trim().split("\\s+", 2);
		String command = parts[0].toUpperCase();
		String argument = (parts.length > 1) ? parts[1] : "";

		switch (command) {
			case "QUIT":
				model.disconnect();
				break;
			default:
				model.sendMessage(cmd);
				break;
		}
	}

	// Dùng khi người dùng nhấn nút Connect
	public void connectToServer(String host, int port, String user, String pass) {
		model.connect(host, port, user, pass);
	}

	// Gửi lệnh từ GUI
	public void sendCommand(String cmd) {
		model.sendMessage(cmd);
	}

	// Khi nhận tin nhắn từ server
	public void onMessageFromServer(String msg) {
		view.appendConsole(msg);
	}

	// Khi kết nối đóng
	public void onConnectionClosed() {
		view.appendConsole("Kết nối đã bị đóng.\n");
	}
}
