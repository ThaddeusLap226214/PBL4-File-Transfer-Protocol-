package Model.Permission;

import java.sql.SQLException;
import java.util.List;

import Model.BaseDAO;
import Model.Bean.UserPermission;

public class PermissionDAO extends BaseDAO {

    /** Lấy danh sách fid theo usid */
    public List<Integer> getListFidByUsid(int usid) {
        String sql = "SELECT fid FROM userpermission WHERE usid = ?";

        return executeQuery(sql, rs -> {
            try {
                return rs.getInt("fid");
            } catch (SQLException e) {
                e.printStackTrace();
                return null; // trả về null nếu có lỗi với 1 row
            }
        }, usid);
    }

    /** Xóa permission nếu đã tồn tại */
    public boolean deleteUserPermission(int usid, int fid) {
        String sql = "DELETE FROM userpermission WHERE usid = ? AND fid = ?";
        boolean deleted = executeUpdate(sql, usid, fid);
        System.out.println("Deleted existing permission: usid=" + usid + ", fid=" + fid + " -> " + deleted);
        return deleted;
    }

    /** Thêm permission mới */
    public boolean insertUserPermission(int usid, int fid, String accessMode) {
        String sql = "INSERT INTO userpermission (usid, fid, accessmode) VALUES (?, ?, ?)";
        boolean inserted = executeUpdate(sql, usid, fid, accessMode);
        if (inserted) {
            System.out.println("Inserted permission: usid=" + usid + ", fid=" + fid + ", access=" + accessMode);
        } else {
            System.out.println("Failed to insert permission: usid=" + usid + ", fid=" + fid);
        }
        return inserted;
    }

    public boolean saveUserPermission(int usid, int fid, String accessMode) {
        String sql = """
                    INSERT INTO userpermission (usid, fid, accessmode)
                    VALUES (?, ?, ?)
                    ON DUPLICATE KEY UPDATE accessmode = VALUES(accessmode)
                """;

        boolean ok = executeUpdate(sql, usid, fid, accessMode);
        System.out.println(
                "Save permission (UPSERT): usid=" + usid + ", fid=" + fid + ", access=" + accessMode
                        + " -> " + ok);
        return ok;
    }

    /** Lấy danh sách permission chi tiết theo usid */
    public List<UserPermission> getUserPermissionsByUsid(int usid) {
        String sql = "SELECT * FROM userpermission WHERE usid = ?";
        return executeQuery(sql, rs -> {
            try {
                UserPermission up = new UserPermission();
                up.setUsid(rs.getInt("usid"));
                up.setFid(rs.getInt("fid"));
                up.setAccessMode(rs.getString("accessmode"));
                return up;
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }
        }, usid);
    }

    public String getAccessMode(int usid, int fid) {
        String sql = """
                    SELECT accessmode
                    FROM userpermission
                    WHERE usid = ? AND fid = ?
                """;

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
