package Model.Account;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import Model.BaseDAO;
import Model.Bean.User;

public class AccountDAO extends BaseDAO{
	
	//map Result cho select *
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
	
	//map Result cho username và usid
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
	
	private User mapUsid(ResultSet rs) {
	    User u = new User();
	    try {
	        u.setUsid(rs.getInt("usid"));
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return u;
	}
	
	public User getByUsername(String username) {
	    String sql = "SELECT * FROM user WHERE username = ?";
	    List<User> list = executeQuery(sql, this::mapFull, username);
	    return list.isEmpty() ? null : list.get(0);
	}
	public boolean insertUser(String username, String password) {
	    String sql = "INSERT INTO user(username, password, grid) VALUES(?,?,?)";
	    return executeUpdate(sql, username, password, 1);
	}

	public boolean findUser(String username) {
		String sql = "SELECT 1 FROM user WHERE username = ?";
	    List<User> list = executeQuery(sql, rs -> {
	        // Chỉ cần map một giá trị bất kỳ, ví dụ `1`, vì chúng ta chỉ cần biết có dòng dữ liệu nào không.
	        return null; // Chúng ta không cần trả về đối tượng User ở đây.
	    }, username);
	    return !list.isEmpty(); // Nếu list có phần tử, có nghĩa là đã tồn tại user

	}

	public int checkLogin(String username, String password) {
		String sql = "SELECT usid FROM user WHERE username = ? AND password = ?";
	    List<User> list = executeQuery(sql, this::mapUsid, username, password);
	    
	    if (!list.isEmpty()) {
	        return list.get(0).getUsid(); // Trả về usid nếu tìm thấy người dùng
	    }
	    
	    return -1; // Nếu không tìm thấy, trả về -1
	}

	public List<String> getAllUsernames() {
		 String sql = "SELECT username FROM user";
	        return executeQuery(sql, rs -> {
	            try {
	                return rs.getString("username");
	            } catch (SQLException e) {
	                e.printStackTrace();
	                return null;
	            }
	        });
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

	public boolean deleteUser(String username) {
		String sql = "DELETE FROM user WHERE username = ?";
        return executeUpdate(sql, username);
	}

	public boolean updateUsername(String oldName, String newName) {
		String sql = "UPDATE user SET username = ? WHERE username = ?";
        return executeUpdate(sql, newName, oldName);
	}

	public boolean updateUserPassword(String username, String password) {
		String sql = "UPDATE user SET password = ? WHERE username = ?";
        return executeUpdate(sql, password, username);
	}
}
