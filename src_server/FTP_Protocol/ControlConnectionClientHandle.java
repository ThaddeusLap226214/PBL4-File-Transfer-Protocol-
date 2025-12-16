package FTP_Protocol;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import FTPBean.Session;
import FTPController.FTPControllerEventListener;

public class ControlConnectionClientHandle extends Thread{
	private FTPControllerEventListener cel;
	private Socket soc;
	private BufferedReader reader;
	private BufferedWriter writer;
	private CommandHandle ch;
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
	
	public void close() {
		try {
			this.soc.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
	
	private String receive() {
		String command = null;
		try {
			command = reader.readLine();
			//test
			System.out.println(command);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return command;
	}
	
	public void send(String response) {
		try {
			//test
			System.out.println(response);
			writer.write(response + "\r\n");
			writer.flush();
			cel.onServerResponse(LocalDateTime.now(), session.getSessionID(), this.soc.getInetAddress(), response);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void sendAppend(String request) {
		try {
			writer.write(request + "\r\n");
			writer.flush();
			cel.onServerResponse(LocalDateTime.now(), session.getSessionID(), this.soc.getInetAddress(), request);
			cel.onClientLoginSuccess(LocalDateTime.now(), session.getSessionID(), this.soc.getInetAddress(), session.getUsername());
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
			if(command != null) {
				cel.onClientCommand(LocalDateTime.now(), session.getSessionID(), this.soc.getInetAddress(), command);
				ch.handle(command, this, this.session);
			}
			else {
				
			}
		}
	}
}
