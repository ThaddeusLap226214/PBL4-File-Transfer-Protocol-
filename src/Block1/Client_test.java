package Block1;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client_test implements Runnable{
	Socket soc;
	
	static public void main(String[] args) {
		new Client_test();
	}
	
	public Client_test() {
		try {
			soc = new Socket("localhost", 5000);
			Thread x = new Thread(this);
			x.start();
		}catch(IOException e) {
			System.out.println("Lỗi kết nối " + e.getMessage());
		}
	}
	
	@Override
	public void run() {
		try {
			while(true) {
				DataInputStream dis = new DataInputStream(soc.getInputStream());
				DataOutputStream dos = new DataOutputStream(soc.getOutputStream());
				String mes = dis.readUTF();
				System.out.println(mes);
				BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
				String nameFile = br.readLine();
				if(nameFile.equalsIgnoreCase("delete")) {
					System.out.println("Thank you.");
					break;
				}
				System.out.print("you want the file send to:");
				String receive = br.readLine();
				dos.writeUTF(nameFile);
				System.out.println(dis.readUTF());
				
				long fileSize = dis.readLong();
				
				FileOutputStream fileOut = new FileOutputStream("filePath\\" + receive);
				byte[] buffer = new byte[1024*4];
				long totalRead = 0;
				int bytesRead;
				
				while(totalRead < fileSize && (bytesRead = dis.read(buffer)) != -1) {
					fileOut.write(buffer, 0, bytesRead);
					totalRead += bytesRead;
				}
				fileOut.close();
				System.out.println("Done");
				
			}			
		} catch (IOException e) {
			System.out.println("Không lấy được luồng dữ liệu từ socket " + e.getMessage());
		}
	}
}