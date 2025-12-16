package Model.File;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import Model.BaseDAO;
import Model.Bean.FolderPath;

public class FileDAO extends BaseDAO {

    private FolderPath mapFolder(ResultSet rs) {
        FolderPath fp = new FolderPath();
        try {
            fp.setFid(rs.getInt("fid"));
            fp.setVirtualPath(rs.getString("virtualpath"));
            fp.setNativePath(rs.getString("nativepath"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return fp;
    }

    public List<FolderPath> getAllFolders() {
        String sql = "SELECT * FROM folderpath";
        return executeQuery(sql, this::mapFolder);
    }

    public FolderPath getByFid(int fid) {
        String sql = "SELECT * FROM folderpath WHERE fid = ?";
        List<FolderPath> list = executeQuery(sql, this::mapFolder, fid);
        return list.isEmpty() ? null : list.get(0);
    }

    /** Lấy folder theo virtual + native path */
    public FolderPath getByVirtualAndNativePath(String virtualPath, String nativePath) {
        String sql = """
                    SELECT * FROM folderpath
                    WHERE virtualpath = ? AND nativepath = ?
                """;
        List<FolderPath> list = executeQuery(sql, this::mapFolder,
                virtualPath.trim(), nativePath.trim());
        return list.isEmpty() ? null : list.get(0);
    }

    /** INSERT nếu chưa tồn tại */
    public FolderPath insertIfNotExists(String virtualPath, String nativePath) {
        if (virtualPath == null || nativePath == null)
            return null;

        virtualPath = virtualPath.trim();
        nativePath = nativePath.trim();

        // 1. Kiểm tra tồn tại
        FolderPath fp = getByVirtualAndNativePath(virtualPath, nativePath);
        if (fp != null) {
            System.out.println("[FileDAO] Folder already exists: fid=" + fp.getFid());
            return fp;
        }

        // 2. Insert
        String sql = "INSERT INTO folderpath (virtualpath, nativepath) VALUES (?, ?)";
        int fid = executeInsertGetId(sql, virtualPath, nativePath);

        if (fid <= 0) {
            System.err.println("[FileDAO] INSERT folder FAILED: " + virtualPath + " | " + nativePath);
            return null;
        }

        System.out.println("[FileDAO] INSERT folder OK, fid=" + fid);
        return getByFid(fid);
    }

    // Xóa folder
    public boolean deleteFolder(int fid) {
        String sql = "DELETE FROM folderpath WHERE fid = ?";
        boolean result = executeUpdate(sql, fid);
        System.out.println("Deleted folder fid=" + fid + " | success=" + result);
        return result;
    }
}
