package Model;

import java.util.ArrayList;

import Model.Account.AccountBO;
import Model.File.FileBO;
import Model.Permission.PermissionBO;
import Utils.FolderInfo;

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

	public ArrayList<FolderInfo> getListFolderPermission(int userId) {
		//dùng id truy xuất trong bảng userPermission lấy danh sách fid
		int[] liFolderID = permsBO.getListIdByUsid();
		return null;
	}

}
