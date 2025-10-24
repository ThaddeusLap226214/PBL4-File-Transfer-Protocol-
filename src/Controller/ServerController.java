package Controller;

import Model.ServerModel;
import View.ServerGUI;

public class ServerController {
	private ServerModel model;
	private ServerGUI view;

	public ServerController(ServerModel model, ServerGUI view) {
		this.model = model;
		this.view = view;
	}

	public ServerController(ServerGUI view) {
		this.view = view;
	}

	public void startServer() {
		new Thread(model).start();
		view.appendLog("Server started!");
	}

	public void stopServer() {
		model.stop();
		view.appendLog("Server stopped!");
	}

	public void appendLog(String msg) {
		view.appendLog(msg);
	}

	public void addClientSession(String date, int id, String host, String user, String status) {
		view.addClientSession(date, id, host, user, status);
	}

	public void updateClientStatus(int id, String status) {
		view.updateClientStatus(id, status);
	}

	public void updateClientUser(int sessionId, String username) {
		view.updateClientUser(sessionId, username);
	}

}
