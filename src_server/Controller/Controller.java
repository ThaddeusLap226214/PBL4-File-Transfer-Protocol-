package Controller;

import Model.ModelBO;
import View.*;

public class Controller implements ControllerEventListener{
	private ModelBO modelBO;
	private ViewAdminFTPServer viewConnect;
	private ViewConfigure viewConfig;
	
	public void setModelBO(ModelBO modelBO) {
		this.modelBO = modelBO;
	}
	
	public void setViewConnect(ViewAdminFTPServer view) {
		this.viewConnect = view;
	}
	
	public void setViewConfig(ViewConfigure view) {
		this.viewConfig = view;
	}
	
	public Controller(ViewAdminFTPServer viewConnect, ViewConfigure viewConfig) {
		setViewConnect(viewConnect);
		setViewConfig(viewConfig);
		//tạm thời
		ServerSoc server = new ServerSoc(5000, this);
		new Thread(server).start();
	}
		
	@Override
	public void onClientCommand(int session, String username, String clientAddress, String command) {
		// TODO Auto-generated method stub
		viewConnect.appendLog(session, username, clientAddress, command);
	}

	@Override
	public void onServerRequest(int session, String username, String clientAddress, String request) {
		// TODO Auto-generated method stub
		viewConnect.appendLog(session, username, clientAddress, request);
	}
}
