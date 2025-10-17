package Controller;

import Model.ClientModel;
import View.ClientGUI;

public class ClientController {
	private ClientGUI view;
	private ClientModel model;
	
	public ClientController(ClientGUI view, ClientModel model) {
		this.model = model;
		this.view = view;
	}
	
	public void checkCMD(String cmd) {
		String[] Cmd = cmd.trim().split("\\s+", 2);
		String command = Cmd[0].toUpperCase();
		String argument = (Cmd.length > 1) ? Cmd[1] : "";
		switch(command) {
			case "USER":
				//
				break;
			case "PASS":
				//
				break;
			case "PWD":
				//
				break;
			case "LIST":
				//
				break;
			case "QUIT":
				//
				break;
			default:
				//gửi lại view
				break;
		}
	}
	public static void main(String[] args) {
		
	}
}
