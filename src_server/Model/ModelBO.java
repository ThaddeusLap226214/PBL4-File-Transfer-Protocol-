package Model;

import java.util.ArrayList;
import java.util.List;

import Model.Account.AccountBO;
import Model.Bean.FolderPath;
import Model.File.FileBO;
import Model.Permission.PermissionBO;

public class ModelBO {
	private AccountBO accBO = new AccountBO();
	private FileBO fileBO = new FileBO();
	private PermissionBO permsBO = new PermissionBO();
	
	public boolean findUser(String username) {		
		return accBO.findUser(username);
	}

	public int checkLogin(String username, String password) {
		return accBO.checkLogin(username, password);
	}

	public List<FolderPath> getListFolderPermission(int usid) {
		//dùng id truy xuất trong bảng userPermission lấy danh sách fid
		List<Integer> listFid = permsBO.getListFidByUsid(usid);
		
		//dùng list fid để lấy danh sách thư mục đầy đủ
		List<FolderPath> listFp = new ArrayList<FolderPath>();
		for(Integer i : listFid) {
			FolderPath fp = fileBO.getFolderPathByFid(i);
			listFp.add(fp);
		}
		return listFp;
	}

}
