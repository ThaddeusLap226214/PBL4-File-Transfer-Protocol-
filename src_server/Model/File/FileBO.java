package Model.File;

import java.util.List;

import Model.Bean.FolderPath;

public class FileBO {

    private FileDAO fileDAO = new FileDAO();

    public FileDAO getFileDAO() {
        return fileDAO;
    }

    // =========================
    // FolderPath management
    // =========================

    /** Kiểm tra folder (theo virtual path) có tồn tại chưa */
    public boolean findFolder(String virtualPath) {
        if (virtualPath == null)
            return false;

        virtualPath = virtualPath.trim();

        for (FolderPath f : fileDAO.getAllFolders()) {
            if (f != null && f.getVirtualPath().equalsIgnoreCase(virtualPath)) {
                return true;
            }
        }
        return false;
    }

    public FolderPath getByFid(int fid) {
        return fileDAO.getByFid(fid);
    }

    /** Thêm folder mới */
    /** Thêm folder mới */
    public boolean insertFolder(String virtualPath, String nativePath) {
        if (virtualPath == null || nativePath == null)
            return false;

        FolderPath fp = fileDAO.insertIfNotExists(virtualPath, nativePath);
        if (fp == null) {
            System.err.println("INSERT folder FAILED: " + virtualPath + " | " + nativePath);
        }
        return fp != null;

    }

    /** Xóa folder theo fid */
    public boolean deleteFolder(int fid) {
        return fileDAO.deleteFolder(fid);
    }

    /** Lấy danh sách tất cả folder */
    public List<FolderPath> getAllFolders() {
        return fileDAO.getAllFolders();
    }

    /** Lấy folder theo fid */
    public FolderPath getFolderPathByFid(int fid) {
        return fileDAO.getByFid(fid);
    }

    public FolderPath getByVirtualAndNativePath(String virtualPath, String nativePath) {
        return fileDAO.getByVirtualAndNativePath(virtualPath, nativePath);
    }

    public FolderPath insertIfNotExists(String virtualPath, String nativePath) {
        return fileDAO.insertIfNotExists(virtualPath, nativePath);
    }

}
