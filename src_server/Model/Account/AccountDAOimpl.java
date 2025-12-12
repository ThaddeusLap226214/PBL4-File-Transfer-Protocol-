package Model.Account;

import Model.Bean.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountDAOimpl implements AccountDAO {

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/ftpdb",
                "root",
                "");
    }

    @Override
    public boolean addUser(User u) {
        String sql = "INSERT INTO user(username, password, grid) VALUES (?, ?, ?)";
        try (Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, u.getUsername());
            ps.setString(2, u.getPassword());
            ps.setInt(3, u.getGrid());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace(); // xem chi tiết lỗi nếu insert fail
            return false;
        }
    }

    @Override
    public boolean updateUser(User u) {
        String sql = "UPDATE user SET username=?, password=?, grid=? WHERE usid=?";
        try (Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, u.getUsername());
            ps.setString(2, u.getPassword());
            ps.setInt(3, u.getGrid());
            ps.setInt(4, u.getUsid());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean removeUser(int usid) {
        String sql = "DELETE FROM user WHERE usid=?";
        try (Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, usid);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> list = new ArrayList<>();
        String sql = "SELECT * FROM user";
        try (Connection conn = getConnection();
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new User(
                        rs.getInt("usid"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getInt("grid")));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public User findUserByName(String username) {
        String sql = "SELECT * FROM user WHERE username=?";
        try (Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new User(
                            rs.getInt("usid"),
                            rs.getString("username"),
                            rs.getString("password"),
                            rs.getInt("grid"));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
