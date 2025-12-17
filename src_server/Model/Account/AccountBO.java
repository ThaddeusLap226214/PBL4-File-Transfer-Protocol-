package Model.Account;

import java.util.ArrayList;
import java.util.List;

public class AccountBO {
	private AccountDAO accDAO = new AccountDAO();

	public boolean findUser(String username) {
		return accDAO.findUser(username);
	}

	public int checkLogin(String username, String password) {
		return accDAO.checkLogin(username, password);
	}

	public List<String> getAllUsernames() {
		return accDAO.getAllUsernames();
	}

	public int getUsidByUsername(String username) {
		if (username == null || username.trim().isEmpty())
            return -1;
        return accDAO.getUsidByUsername(username.trim());
	}

	public boolean deleteUser(String username) {
		return accDAO.deleteUser(username);
	}

	public boolean updateUsername(String oldName, String newName) {
		if (findUser(newName))
			return false;
	    return accDAO.updateUsername(oldName, newName);
	}

	public boolean insertUser(String username, String password) {
		if (findUser(username))
            return false;
        return accDAO.insertUser(username, password);
	}

	public boolean updateUserPassword(String username, String password) {
		return accDAO.updateUserPassword(username, password);
	}
	
}
