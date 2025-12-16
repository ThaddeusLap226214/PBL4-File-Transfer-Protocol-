package Model.Account;

import java.util.List;

public class AccountBO {

    private AccountDAO accountDAO = new AccountDAO();

    public AccountDAO getAccountDAO() {
        return accountDAO;
    }

    public boolean findUser(String username) {
        if (username == null)
            return false;
        username = username.trim().toLowerCase();

        for (String u : accountDAO.getAllUsernames()) {
            if (u != null && u.trim().toLowerCase().equals(username)) {
                return true;
            }
        }
        return false;
    }

    public boolean insertUser(String username, String password) {
        if (findUser(username))
            return false;
        return accountDAO.insertUser(username, password);
    }

    public boolean deleteUser(String username) {
        return accountDAO.deleteUser(username);
    }

    public boolean updateUsername(String oldName, String newName) {
        if (findUser(newName))
            return false;
        return accountDAO.updateUsername(oldName, newName);
    }

    public int checkLogin(String username, String password) {
        return accountDAO.checkLogin(username, password);
    }

    public boolean updateUserPassword(String username, String password) {
        return accountDAO.updateUserPassword(username, password);
    }

    public List<String> getAllUsernames() {
        return accountDAO.getAllUsernames();
    }

    public int getUsidByUsername(String username) {
        if (username == null || username.trim().isEmpty())
            return -1;
        return accountDAO.getUsidByUsername(username.trim());
    }

}
