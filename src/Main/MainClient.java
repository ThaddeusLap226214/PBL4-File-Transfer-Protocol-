package Main;

import Controller.ClientController;
import Model.ClientModel;
import View.ClientGUI;

public class MainClient {
	public static void main(String[] args) {
		ClientGUI view = new ClientGUI();
		ClientModel model = new ClientModel();
		ClientController controller = new ClientController(view, model);

		model.setController(controller);
		view.setController(controller);

		view.setVisible(true);
	}
}
