package Model.Account;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import Model.BaseDAO;
import Model.Bean.User;

public class AccountDAO extends BaseDAO {

    // Map ResultSet đầy đủ
    private User mapFull(ResultSet rs) {
        User u = new User();
        try {
            u.setUsid(rs.getInt("usid"));
            u.setUsername(rs.getString("username"));
            u.setPassword(rs.getString("password"));
            u.setGrid(rs.getInt("grid"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return u;
    }

    // Map username + usid
    private User mapBasic(ResultSet rs) {
        User u = new User();
        try {
            u.setUsid(rs.getInt("usid"));
            u.setUsername(rs.getString("username"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return u;
    }

    /** Lấy user theo username */
    public User getByUsername(String username) {
        String sql = "SELECT * FROM `user` WHERE username = ?";
        List<User> list = executeQuery(sql, this::mapFull, username);
        return list.isEmpty() ? null : list.get(0);
    }

    /** Thêm user mới */
    public boolean insertUser(String username, String password) {
        if (username == null || username.trim().isEmpty())
            return false;

        String sql = "INSERT INTO `user` (username, password, grid) VALUES (?, ?, ?)";

        return executeUpdate(sql,
                username.trim(),
                password == null ? "" : password,
                1 // 👈 grid mặc định
        );
    }

    /** Xóa user */
    public boolean deleteUser(String username) {
        String sql = "DELETE FROM `user` WHERE username = ?";
        return executeUpdate(sql, username);
    }

    /** Đổi tên user */
    public boolean updateUsername(String oldName, String newName) {
        String sql = "UPDATE `user` SET username = ? WHERE username = ?";
        return executeUpdate(sql, newName, oldName);
    }

    /** Lấy danh sách username */
    public List<String> getAllUsernames() {
        String sql = "SELECT username FROM `user`";
        return executeQuery(sql, rs -> {
            try {
                return rs.getString("username");
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }
        });
    }

    public boolean updateUserPassword(String username, String password) {
        String sql = "UPDATE `user` SET password = ? WHERE username = ?";
        return executeUpdate(sql, password, username);
    }

    public int getUsidByUsername(String username) {
        String sql = "SELECT usid FROM user WHERE username = ?";
        List<Integer> list = executeQuery(sql, rs -> {
            try {
                return rs.getInt("usid");
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }, username);

        return list.isEmpty() ? -1 : list.get(0);
    }

    /** Kiểm tra login */
    public int checkLogin(String username, String password) {
        String sql = "SELECT usid FROM `user` WHERE username = ? AND password = ?";
        List<Integer> list = executeQuery(sql, rs -> {
            try {
                return rs.getInt("usid");
            } catch (SQLException e) {
                e.printStackTrace();
                return -1;
            }
        }, username, password);

        return list.isEmpty() ? -1 : list.get(0);
    }
}
