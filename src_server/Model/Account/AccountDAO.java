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
	public User getByUsername(String username) {
	    String sql = "SELECT * FROM user WHERE username = ?";
	    List<User> list = executeQuery(sql, this::mapFull, username);
	    return list.isEmpty() ? null : list.get(0);
	}
	public boolean insertUser(String username, String password) {
	    String sql = "INSERT INTO user(username, password) VALUES(?,?)";
	    return executeUpdate(sql, username, password);
	}
}
