package Model.Permission;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import Model.BaseDAO;
import Model.Bean.UserPermission;

public class PermissionDAO extends BaseDAO {

    // map Result cho select *
    private UserPermission mapFull(ResultSet rs) {
        UserPermission p = new UserPermission();
        try {
            p.setUsid(rs.getInt("usid"));
            p.setFid(rs.getInt("fid"));
            p.setAccessMode(rs.getString("accessmode"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return p;
    }

    // map Result cơ bản: usid và fid
    private UserPermission mapBasic(ResultSet rs) {
        UserPermission p = new UserPermission();
        try {
            p.setUsid(rs.getInt("usid"));
            p.setFid(rs.getInt("fid"));
            p.setAccessMode("RWD");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return p;
    }
    
    // map Result list fid
    private int mapListFid(ResultSet rs) {
    		Integer fid = null;
        try {
        		fid = rs.getInt("fid");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return fid;
    }

    // lấy theo usid và fid
    public UserPermission getPermission(int usid, int fid) {
        String sql = "SELECT * FROM userpermission WHERE usid = ? AND fid = ?";
        List<UserPermission> list = executeQuery(sql, this::mapFull, usid, fid);
        return list.isEmpty() ? null : list.get(0);
    }

    // cập nhật quyền
    public boolean updatePermission(int usid, int fid, String accessMode) {
        String sql = "UPDATE userpermission SET accessMode = ? WHERE usid = ? AND fid = ?";
        return executeUpdate(sql, accessMode, usid, fid);
    }
    
    // lấy danh sách fid theo usid
	public List<Integer> getListFidByUsid(int usid) {
		String sql = "SELECT fid FROM userpermission WHERE usid = ?";
		return executeQuery(sql, this::mapListFid, usid);
	}

	public boolean deleteUserPermission(int usid, Integer fid) {
		String sql = "DELETE FROM userpermission WHERE usid = ? AND fid = ?";
        boolean deleted = executeUpdate(sql, usid, fid);
//        System.out.println("Deleted existing permission: usid=" + usid + ", fid=" + fid + " -> " + deleted);
        return deleted;
	}

	public boolean insertUserPermission(int usid, int fid, String accessMode) {
		String sql = "INSERT INTO userpermission (usid, fid, accessmode) VALUES (?, ?, ?)";
        boolean inserted = executeUpdate(sql, usid, fid, accessMode);
//        if (inserted) {
//            System.out.println("Inserted permission: usid=" + usid + ", fid=" + fid + ", access=" + accessMode);
//        } else {
//            System.out.println("Failed to insert permission: usid=" + usid + ", fid=" + fid);
//        }
        return inserted;
	}

	public String getAccessMode(int usid, int fid) {
		String sql = "SELECT accessmode FROM userpermission WHERE usid = ? AND fid = ?";

    List<String> list = executeQuery(sql, rs -> {
        try {
            return rs.getString("accessmode");
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }, usid, fid);

    return list.isEmpty() ? null : list.get(0);
	}
}
