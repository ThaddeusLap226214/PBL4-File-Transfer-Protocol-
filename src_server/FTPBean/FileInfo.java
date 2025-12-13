package FTPBean;

import java.nio.file.attribute.FileTime;

public class FileInfo {
    private String name;        // Tên file/folder
    private boolean isDirectory;
    private long size;
    private String owner;
    private String group;
    private String permissions; // drwxr-xr-x
    private FileTime lastModified;

    // Constructor
    public FileInfo(String name, boolean isDirectory, long size, String owner, String group, String permissions, FileTime lastModified) {
        this.name = name;
        this.isDirectory = isDirectory;
        this.size = size;
        this.owner = owner;
        this.group = group;
        this.permissions = permissions;
        this.lastModified = lastModified;
    }

    // Getters
    public String getName() { return name; }
    public boolean isDirectory() { return isDirectory; }
    public long getSize() { return size; }
    public String getOwner() { return owner; }
    public String getGroup() { return group; }
    public String getPermissions() { return permissions; }
    public FileTime getLastModified() { return lastModified; }
}
