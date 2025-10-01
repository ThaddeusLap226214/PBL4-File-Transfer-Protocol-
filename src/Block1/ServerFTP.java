package Block1;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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
	public void sendFile(DataInputStream dis, DataOutputStream dos, String nameFile) {
		try {
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
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	public void reiceveFile(DataInputStream dis, DataOutputStream dos, String reiceve) {
		//Client gửi tên file sau đó đợi nhận độ lớn file
		try {
			long fileSize = dis.readLong();
			
			FileOutputStream fileOut = new FileOutputStream("filePath\\" + reiceve);
			byte[] buffer = new byte[1024*4];
			long totalRead = 0;
			int bytesRead;
			
			while(totalRead < fileSize && (bytesRead = dis.read(buffer)) != -1) {
				fileOut.write(buffer, 0, bytesRead);
				totalRead += bytesRead;
			}
			fileOut.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	@Override
	public void run() {
		try {
			DataInputStream dis = new DataInputStream(soc.getInputStream());
			DataOutputStream dos = new DataOutputStream(soc.getOutputStream());
			while(true) {
				String seOrRe = dis.readUTF();
				if(seOrRe.equals("1")) { // 1 là client yêu cầu file
					String nameFile = dis.readUTF();
					
					sendFile(dis, dos, nameFile);
					
					System.out.println("file is send to client");	
				}
				else { // còn lại là client gửi file cho server
					
					String reiceve = dis.readUTF();
					
					reiceveFile(dis, dos, reiceve);
					
				}
			}
		}catch(IOException e) {
			System.out.println("Không lấy được luồng dữ liệu" + e.getMessage());
		}
	}
}

//tách lớp send và receivd 