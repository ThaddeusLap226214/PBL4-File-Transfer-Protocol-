package Model.Permission;

import java.util.List;

public class PermissionBO {
	private PermissionDAO permsDAO = new PermissionDAO();

	public List<Integer> getListFidByUsid(int usid) {
		//truy vấn lên CSDL để lấy danh sách fid từ usid 
		List<Integer> listFid = permsDAO.getListFidByUsid(usid);
		return listFid;
	}
}
