package Main;

import java.net.InetAddress;
import java.net.UnknownHostException;

import Controller.ClientController;
import Model.ClientModel;
import View.ClientGUI;

public class MainClient {
	public static void main(String[] args) {
			try {
				ClientModel model = new ClientModel(InetAddress.getLocalHost(), 5000);
				ClientGUI view = new ClientGUI();
				ClientController controller = new ClientController(view, model);
				
				view.setController(controller);
				model.setController(controller);
				new Thread(model).start();
				
				view.setVisible(true);
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	}

}
