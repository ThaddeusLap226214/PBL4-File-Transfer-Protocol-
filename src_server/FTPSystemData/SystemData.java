package FTPSystemData;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import FTPBean.FileInfo;

public class SystemData {
	
	//lấy thêm thông tin thư mục hoặc file từ đường dẫn thực
    public FileInfo getMoreInfoFolder(String nativePath) {
        try {
            Path path = Paths.get(nativePath);

            boolean isDirectory = Files.isDirectory(path);
            long size = isDirectory ? 0 : Files.size(path);

            // Owner
            UserPrincipal owner = Files.getOwner(path);
            String ownerName = owner != null ? owner.getName() : "owner";

            // Group (Windows không có group chuẩn, dùng giả lập)
            String groupName = "group";

            // Permissions giả lập
            Set<PosixFilePermission> perms = null;
            String permissionStr = isDirectory ? "drwxr-xr-x" : "-rw-r--r--";

            // Last modified
            FileTime lastModified = Files.getLastModifiedTime(path);

            // Name
            String name = path.getFileName().toString();

            return new FileInfo(name, isDirectory, size, ownerName, groupName, permissionStr, lastModified);

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public String[] listChildRealPaths(String nativePath) {

        // Danh sách tạm để lưu các đường dẫn thật
        List<String> realPaths = new ArrayList<>();

        // Chuyển chuỗi đường dẫn thành đối tượng Path
        Path dir = Paths.get(nativePath);

        // Kiểm tra: đường dẫn có tồn tại không, đường dẫn đó có phải là thư mục không
        // Nếu không phải → trả về mảng rỗng
        //* Files.isDirectory(path); Files.isRegularFile(path)
        if (!Files.exists(dir) || !Files.isDirectory(dir)) {
            return new String[0];
        }
        
        // DirectoryStream dùng để duyệt các file / thư mục con trực tiếp
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {

            // Duyệt từng file / thư mục bên trong
            for (Path p : stream) {

                // Lấy đường dẫn đầy đủ (absolute / native path)
                // Ví dụ: D:\FTP_ROOT\docs\a.txt
                String realPath = p.toString();

                // Thêm vào danh sách kết quả
                realPaths.add(realPath);
            }

        } catch (IOException e) {
            // Lỗi IO: không đủ quyền, thư mục bị khóa, ...
            e.printStackTrace();
        }
        
        // Chuyển List<String> sang mảng String[]
        return realPaths.toArray(new String[0]);
    }
}
