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
	
	public void reiceveFile(DataInputStream dis, DataOutputStream dos, String nameFile, String reiceve) {
		//Client gửi tên file sau đó đợi nhận độ lớn file
		try {
			dos.writeUTF(nameFile);
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
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			while(true) {
				System.out.print("Enter '1' to receive file and other to send file: ");
				String seOrRe = br.readLine();
				if(seOrRe.equals("1")) { // 1 là client yêu cầu file
					dos.writeUTF(seOrRe);
					System.out.print("Name file: ");
					String nameFile = br.readLine();
					if(nameFile.equalsIgnoreCase("delete")) {
						System.out.println("Thank.");
						break;
					}
					System.out.print("Receive: ");
					String reiceve = br.readLine();
					
					reiceveFile(dis, dos, nameFile, reiceve);
					
					System.out.println("Done");
				}else { // client gửi file cho server
					dos.writeUTF("-1");
					System.out.print("Name file: ");
					String nameFile = br.readLine();
					if(nameFile.equalsIgnoreCase("delete")) {
						System.out.println("Thank.");
						break;
					}
					System.out.print("send with name: ");
					String reiceve = br.readLine();
					
					//server sẽ cần tên file để lưu ra ngoài
					dos.writeUTF(reiceve);
					
					sendFile(dis, dos, nameFile);
					
					System.out.println("file is send to server");
				}

				
			}
		} catch (IOException e) {
			System.out.println("Không lấy được luồng dữ liệu từ socket " + e.getMessage());
		}
	}
}