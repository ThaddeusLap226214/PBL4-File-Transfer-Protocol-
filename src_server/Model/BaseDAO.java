package Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class BaseDAO {
	
	//SELECT trả về danh sách đối tượng
	public <T> List<T> executeQuery(
			String sql,
			Function<ResultSet, T> mapper,
			Object... params){
		List<T> resultList = new ArrayList<>();
		
		try (Connection conn = DBConnection.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql)){
			setParameters(pstmt, params);
			
			try (ResultSet rs = pstmt.executeQuery()){
				while (rs.next()) {
					resultList.add(mapper.apply(rs));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultList;
	}
	
	// INSERT /UPDATE /DELETE
	public boolean executeUpdate(String sql, Object... params) {
		try (Connection conn = DBConnection.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql)){
			setParameters(pstmt, params);
			return pstmt.executeUpdate() > 0;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	//Gán tham số
	private void setParameters(PreparedStatement pstmt, Object... params) throws SQLException {
		if(params != null) {
			for (int i = 0; i < params.length; i++) {
				pstmt.setObject(i + 1, params[i]);
			}
		}
	}
}
