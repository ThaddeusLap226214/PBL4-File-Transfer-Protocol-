package FTP_Protocol;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import FTPBean.FileInfo;
import FTPSystemData.SystemData;
import Model.Bean.FolderPath;

public class DataHandle {
	private SystemData SD = new SystemData();
	public boolean HandleSendFolderData(List<FolderPath> listFp, OutputStream dataOut) {
		//dựa vào nativePath lấy thêm thông tin cho các Folder
		List<FileInfo> listInfoFolder = new ArrayList<FileInfo>(); //danh sách thông tin thư mục
		for (FolderPath fp : listFp) {
			FileInfo info = SD.getMoreInfoFolder(fp.getNativePath());
			if (info != null) {
				listInfoFolder.add(info);
			}
		}
		//Chuyển đổi thông tin các folder thành định dạng UNIX để chuẩn bị gửi qua Client FTP
		String listFolderUnix = ListInfoFolderTransferToUNIX(listInfoFolder);
		
		//gửi qua socket
		return sendData(listFolderUnix, dataOut);
	}
	public String ListInfoFolderTransferToUNIX(List<FileInfo> listInfoFolder) {
		StringBuilder sb = new StringBuilder();
		//Chuyển đổi từ định dạng Window sang UNIX và lưu vào listFolderUnix ngăn cách bởi CRLF
		for (FileInfo f : listInfoFolder) {
			// Permissions
            String perms = f.getPermissions();
            
            // Hard link count (giả lập 1)
            int links = 1;
            
            // Owner & group
            String owner = f.getOwner();
            String group = f.getGroup();
            
            // Size
            long size = f.getSize();

            // Last modified
            Date lmDate = new Date(f.getLastModified().toMillis());

            // Chuyển ngày tháng sang format UNIX (MMM dd HH:mm hoặc MMM dd yyyy)
            String dateStr = formatUnixDate(lmDate);
            
            // Tên file
            String name = f.getName();

            // Build dòng UNIX
            sb.append(String.format("%s %d %s %s %10d %s %s\r\n",
                    perms, links, owner, group, size, dateStr, name));
		}
		
		return sb.toString();
	}
	
	private String formatUnixDate(Date date) {
        long SIX_MONTHS = 1000L * 60 * 60 * 24 * 30 * 6; // approx 6 months
        Date now = new Date();

        SimpleDateFormat sdf;
        if (Math.abs(now.getTime() - date.getTime()) > SIX_MONTHS) {
            sdf = new SimpleDateFormat("MMM dd yyyy");
        } else {
            sdf = new SimpleDateFormat("MMM dd HH:mm");
        }
        return sdf.format(date);
    }
	
	public boolean sendData(String listFolderUnix, OutputStream dataOut) {
		//gửi dữ liệu qua cho client
		try {
            byte[] bytes = listFolderUnix.getBytes(StandardCharsets.UTF_8);
            dataOut.write(bytes);
            dataOut.flush();
            // Không đóng stream ở đây, FTP server sẽ quản lý data socket
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
	}
}
