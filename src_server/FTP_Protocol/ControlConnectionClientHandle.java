package FTP_Protocol;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import FTPController.FTPControllerEventListener;

public class ControlConnectionClientHandle extends Thread{
	private FTPControllerEventListener cel;
	private Socket soc;
	private BufferedReader reader;
	private BufferedWriter writer;
	private CommandHandle ch;
	
	//thêm tạm thời session
	private Session session;
	
	//hàm dựng với session
	public ControlConnectionClientHandle(Socket soc, CommandHandle ch, FTPControllerEventListener cel, int session) {
		this.cel = cel;
		this.soc = soc;
		this.ch = ch;
		DataConnectionHandle dataConnect = new DataConnectionHandle();
		this.session = new Session(session, dataConnect);
		setDataIOStream();
	}
	
	
	private void setDataIOStream() {
		if(soc.isConnected()) {
			try {
				reader = new BufferedReader(new InputStreamReader(soc.getInputStream(), StandardCharsets.UTF_8));
				writer = new BufferedWriter(new OutputStreamWriter(soc.getOutputStream(),StandardCharsets.UTF_8));
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}
	
//	public ControlConnectionClientHandle(Socket soc, ControllerEventListener cel) {
//		this.cel = cel;
//		this.soc = soc;
//		setDataIOStream();
//	}
	
	private String receive() {
		String command = null;
		try {
			command = reader.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return command;
	}
	
	public void send(String request) {
		try {
			writer.write(request + "\r\n");
			writer.flush();
			cel.onServerRequest(1,"user1" , "127.0.0.1",request);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
//		ch.setControlConnectionHandle(this);
		while(true) {
			String command = receive();
			cel.onClientCommand(session.getSessionID(), session.getUsername() , "127.0.0.1",command);
			ch.handle(command, this, this.session);
		}
	}
}
