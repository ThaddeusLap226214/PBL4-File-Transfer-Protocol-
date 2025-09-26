package Block1;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerFTP {

	public static void main(String[] args) {
		try {
			ServerSocket server = new ServerSocket(5000);
			while(true) {
				Socket soc = server.accept();
				System.out.println("Client connected.");
				Xuly x = new Xuly(soc);
				x.start();
			}
		} catch(IOException e) {
			System.out.println("Lỗi kết nối " + e.getMessage());
		}
	}

}

class Xuly extends Thread{
	Socket soc;
	public Xuly(Socket soc) {
		this.soc = soc;
	}
	@Override
	public void run() {
		try {
			while(true) {
				DataInputStream dis = new DataInputStream(soc.getInputStream());
				DataOutputStream dos = new DataOutputStream(soc.getOutputStream());
				dos.writeUTF("Hi Client, please send name file");
				String nameFile = dis.readUTF();
				dos.writeUTF("I will send to you file " + nameFile + "\n");
				
				String filePath = "filePath\\" + nameFile;
				File file = new File(filePath);
				FileInputStream fileIn = new FileInputStream(file);
				
				dos.writeLong(file.length());
				
				byte[] buffer = new byte[1024*4];
				int bytesRead;
				
				while((bytesRead = fileIn.read(buffer)) != -1) {
					dos.write(buffer, 0, bytesRead);;
				}
				
				fileIn.close();
				dos.flush();
				System.out.println("file is send to client");				
			}
		} catch (IOException e) {
			System.out.println("Không lấy được luồng dữ liệu từ socket " + e.getMessage());;
		}
	}
}

//tách lớp send và receivd 