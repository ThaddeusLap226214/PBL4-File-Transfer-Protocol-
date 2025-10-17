package Main;

import Controller.ServerController;
import Model.ServerModel;
import View.ServerGUI;

public class MainServer {

	public static void main(String[] args) {
		ServerModel model = new ServerModel(5000);
		ServerGUI view = new ServerGUI();
		ServerController controller = new ServerController(model, view);
		
		model.setController(controller);
		view.setController(controller);
		new Thread(model).start();
		
		view.setVisible(true);
	}

}
