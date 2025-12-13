package FTPSystemData;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.*;
import java.util.Set;

import FTPBean.FileInfo;

public class SystemData {

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
}
