package FTP_Protocol;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import FTPControl.FTPControlEventListener;
import FTPEntity.Session;

public class ControlConnectionClientHandle extends Thread{
	private FTPControlEventListener cel;
	private Socket soc;
	private BufferedReader reader;
	private BufferedWriter writer;
	private CommandHandle ch;
	private Session session;
	
	//hàm dựng với session
	public ControlConnectionClientHandle(Socket soc, CommandHandle ch, FTPControlEventListener cel, int session) {
		this.cel = cel;
		this.soc = soc;
		this.ch = ch;
		DataConnectionHandle dataConnect = new DataConnectionHandle();
		this.session = new Session(session, dataConnect);
		setDataIOStream();
	}
	
	public void close() {
//		try {
//			if (soc != null && !soc.isClosed()) {
//	            soc.close();
//			}
//		} catch (IOException e) {
//			
//		}
		try {
	        if (reader != null) reader.close();
	        if (writer != null) writer.close();
	        if (soc != null && !soc.isClosed()) soc.close();
	    } catch (IOException e) {
	        // log nếu cần
	    }
	}
	
	public void shutdown() {
	    interrupt();
	    close();
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
		if (Thread.currentThread().isInterrupted()) {
		    return null;
		}
		try {
			if(soc.isClosed() || !soc.isConnected()) {
				return null;
			}
			command = reader.readLine();
			if (command == null) {
				return null;
			}
			//test
			System.out.println(command);
		} catch (IOException e) {
			return null;
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
		while(!isInterrupted()) {
			String command = receive();
			if(command != null) {
				cel.onClientCommand(LocalDateTime.now(), session.getSessionID(), this.soc.getInetAddress(), command);
				ch.handle(command, this, this.session);
			}
			else {
				break;
			}
		}
		close();
	}
	
}
