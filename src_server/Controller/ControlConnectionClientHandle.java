package Controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ControlConnectionClientHandle extends Thread{
	private ControllerEventListener cel;
	private Socket soc;
	private BufferedReader reader;
	private BufferedWriter writer;
	private CommandHandle ch = new CommandHandle();
	
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
	
	public ControlConnectionClientHandle(Socket soc, ControllerEventListener cel) {
		this.cel = cel;
		this.soc = soc;
		setDataIOStream();
	}
	
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
		ch.setControlConnectionHandle(this);
		while(true) {
			String command = receive();
			cel.onClientCommand(1,"user1" , "127.0.0.1",command);
			ch.handle(command);
		}
	}
}
