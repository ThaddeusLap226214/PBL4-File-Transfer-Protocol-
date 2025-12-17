package Model.File;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import Model.BaseDAO;
import Model.Bean.FolderPath;

public class FileDAO extends BaseDAO {

    // map Result cho select *
    private FolderPath mapFull(ResultSet rs) {
        FolderPath f = new FolderPath();
        try {
            f.setFid(rs.getInt("fid"));
            f.setVirtualPath(rs.getString("virtualpath"));
            f.setNativePath(rs.getString("nativepath"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return f;
    }

    // map Result cho fid và virtualPath
    private FolderPath mapBasic(ResultSet rs) {
        FolderPath f = new FolderPath();
        try {
            f.setFid(rs.getInt("fid"));
            f.setVirtualPath(rs.getString("virtualpath"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return f;
    }

    public FolderPath getByFid(Integer fid) {
        String sql = "SELECT * FROM folderpath WHERE fid = ?";
        List<FolderPath> list = executeQuery(sql, this::mapFull, fid);
        return list.isEmpty() ? null : list.get(0);
    }

    public boolean insertFolder(String virtualPath, String nativePath) {
        String sql = "INSERT INTO folderpath(virtualpath, nativepath) VALUES(?, ?)";
        return executeUpdate(sql, virtualPath, nativePath);
    }

	public FolderPath insertIfNotExists(String virtualPath, String nativePath) {
		if (virtualPath == null || nativePath == null)
            return null;

        virtualPath = virtualPath.trim();
        nativePath = nativePath.trim();

        // 1. Kiểm tra tồn tại
        FolderPath fp = getByVirtualAndNativePath(virtualPath, nativePath);
        if (fp != null) {
//            System.out.println("[FileDAO] Folder already exists: fid=" + fp.getFid());
            return fp;
        }

        // 2. Insert
        String sql = "INSERT INTO folderpath (virtualpath, nativepath) VALUES (?, ?)";
        int fid = executeInsertGetId(sql, virtualPath, nativePath);

        if (fid <= 0) {
//            System.err.println("[FileDAO] INSERT folder FAILED: " + virtualPath + " | " + nativePath);
            return null;
        }

//        System.out.println("[FileDAO] INSERT folder OK, fid=" + fid);
        return getByFid(fid);
	}

	public FolderPath getByVirtualAndNativePath(String virtualPath, String nativePath) {
		String sql = "SELECT * FROM folderpath WHERE virtualpath = ? AND nativepath = ?";
    List<FolderPath> list = executeQuery(sql, this::mapFull, virtualPath.trim(), nativePath.trim());
    return list.isEmpty() ? null : list.get(0);
	}
}
