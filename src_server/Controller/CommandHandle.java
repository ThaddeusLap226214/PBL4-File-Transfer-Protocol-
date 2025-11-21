package Controller;

import Model.ModelBO;
import Model.ResponseListener;

public class CommandHandle implements ResponseListener{
	private ModelBO modelBO = new ModelBO(this);
	private ControlConnectionClientHandle ccch;
	
	public void setControlConnectionHandle(ControlConnectionClientHandle cc) {
		ccch = cc;
	}
	
	@Override
	public void onRespose(String response) {
		ccch.send(response);
	}
	
//	public void handle(String cmd, ControlConnectionClientHandle ccch) {
	public void handle(String cmd) {
		String[] Cmd = cmd.trim().split("\\s+", 2);
		String command = Cmd[0].toUpperCase();
		String argument = (Cmd.length > 1) ? Cmd[1] : "";
		
		switch(command) {
			case "AUTH":
				handleAUTH(argument);
				return;
			case "USER":
				handleUSER(argument);
				return;
			case "PASS":
				handlePASS(argument);
				return;
			case "FEAT":
				handleFEAT();
			    return;
			case "TYPE":
				handleTYPE(argument);
				return;
			case "PWD":
				handlePWD();
				return;
			case "PASV":
				handlePASV();
				return;
			case "LIST":
				handleLIST(argument);
				return;
			case "QUIT":
				handleQUIT();
				return;
			default:
				String reply = "502 Command not implemented";
				ccch.send(reply);
		}
	}
	
	private void handleAUTH(String argument) {
		modelBO.handleAUTH(argument);
	}
	
	private void handleUSER(String argument) {
		modelBO.handleUSER(argument);
	}
	
	private void handlePASS(String argument) {
		modelBO.handlePASS(argument);
	}
	
	private void handleFEAT() {
		modelBO.handleFEAT();
	}
	
	private void handleTYPE(String argument) {
		modelBO.handleTYPE(argument);
	}
	
	private void handlePWD() {
		modelBO.handlePWD();
	}	
	
	private void handlePASV() {
		modelBO.handlePASV();
	}
	
	private void handleLIST(String argument) {
		modelBO.handleLIST(argument);
	}
	
	private void handleQUIT() {
		modelBO.handleQUIT();
	}

}
