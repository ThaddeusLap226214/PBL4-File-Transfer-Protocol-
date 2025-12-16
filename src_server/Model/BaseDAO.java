package Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Statement;
import java.util.function.Function;

public class BaseDAO {

	// SELECT trả về danh sách đối tượng
	public <T> List<T> executeQuery(
			String sql,
			Function<ResultSet, T> mapper,
			Object... params) {
		List<T> resultList = new ArrayList<>();

		try (Connection conn = DBConnection.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {
			setParameters(pstmt, params);

			try (ResultSet rs = pstmt.executeQuery()) {
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
				PreparedStatement pstmt = conn.prepareStatement(sql)) {
			setParameters(pstmt, params);
			return pstmt.executeUpdate() > 0;
		} catch (Exception e) {
			System.err.println("SQL ERROR: " + sql);
			e.printStackTrace();
			return false;
		}
	}

	// INSERT + lấy ID tự sinh
	public int executeInsertGetId(String sql, Object... params) {
		try (Connection conn = DBConnection.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

			setParameters(pstmt, params);
			pstmt.executeUpdate();

			try (ResultSet rs = pstmt.getGeneratedKeys()) {
				if (rs.next())
					return rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	// Gán tham số
	private void setParameters(PreparedStatement pstmt, Object... params) throws SQLException {
		if (params != null) {
			for (int i = 0; i < params.length; i++) {
				pstmt.setObject(i + 1, params[i]);
			}
		}
	}
}
