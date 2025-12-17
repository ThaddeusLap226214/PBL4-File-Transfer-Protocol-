package Model.Permission;

import java.util.List;

public class PermissionBO {
	private PermissionDAO permsDAO = new PermissionDAO();

	public List<Integer> getListFidByUsid(int usid) {
		//truy vấn lên CSDL để lấy danh sách fid từ usid 
		if(usid <= 0) return null;
		List<Integer> listFid = permsDAO.getListFidByUsid(usid);
		return listFid;
	}

	public boolean deleteUserPermission(int usid, Integer fid) {
		if (usid <= 0 || fid <= 0)
            return false;
        return permsDAO.deleteUserPermission(usid, fid);
	}

	public boolean saveUserPermission(int usid, int fid, String accessMode) {
		if (accessMode == null)
            accessMode = "";
        if (usid <= 0 || fid <= 0 || accessMode == null || accessMode.isEmpty())
            return false;

        // Xóa quyền cũ nếu đã tồn tại
        permsDAO.deleteUserPermission(usid, fid);

        // Thêm quyền mới
        return permsDAO.insertUserPermission(usid, fid, accessMode);
	}

	public String getAccessMode(int usid, int fid) {
		if (usid <= 0 || fid <= 0)
            return null;
        return permsDAO.getAccessMode(usid, fid);
	}
}
