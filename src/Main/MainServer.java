package Main;

import Controller.ServerController;
import Model.ServerModel;
import View.ServerGUI;

public class MainServer {
	public static void main(String[] args) {
		int port = 5000; // bạn có thể đổi cổng tại đây

		ServerModel model = new ServerModel(port);
		ServerGUI view = new ServerGUI();
		ServerController controller = new ServerController(model, view);

		model.setController(controller);
		view.setController(controller);

		// Chạy server trên luồng riêng
		new Thread(model).start();

		view.setVisible(true);
	}
}
