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
}
