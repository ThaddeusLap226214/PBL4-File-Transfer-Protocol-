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
            p.setAccessMode(rs.getString("accessMode"));
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
}
