package FTP_Protocol;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class DataConnectionHandle {
	private boolean modeSelected = false;
	private boolean passiveMode;
	private ServerSocket passiveServer;
	private Socket dataSocket;
	private String activeHost;
	private int activePort;
	
	public boolean isModeSelected() {
	    return modeSelected;
	}
	
	public void resetMode() {
	    modeSelected = false;
	}
	
	public int enterPassiveMode() throws IOException{
		close();
		Random rd = new Random();
		passiveServer = new ServerSocket(50000 + rd.nextInt(1000));
		passiveMode = true;
		modeSelected = true;
		return passiveServer.getLocalPort();
	}
	
	public void enterActiveMode(String host, int port) throws IOException{
		close();
		this.activeHost = host;
	    this.activePort = port;
	    passiveMode = false;
	    modeSelected = true;
	}
	
	public Socket openDataSocket() throws IOException{
		if (passiveMode) {
	        dataSocket = passiveServer.accept();
	    } else {
	        dataSocket = new Socket(activeHost, activePort);
	    }
	    return dataSocket;
	}
	
	public void close(){
		try {
	        if (dataSocket != null && !dataSocket.isClosed()) {
	            dataSocket.close();
	        }
	        if (passiveServer != null && !passiveServer.isClosed()) {
	            passiveServer.close();
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
}
