package Model;

import Model.Account.AccountBO;
import Model.File.FileBO;
import Model.Permission.PermissionBO;

public class ModelBO {
	private AccountBO accBO = new AccountBO();
	private FileBO fileBO = new FileBO();
	private PermissionBO permsB0 = new PermissionBO();
	
	public boolean findUser(String username) {		
		return accBO.findUser(username);
	}
	
}
