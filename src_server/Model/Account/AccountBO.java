package Model.Account;

import Model.Bean.User;

import java.util.List;

public class AccountBO {

    private AccountDAO dao = new AccountDAOimpl();

    public boolean addUser(User u) {
        return dao.addUser(u);
    }

    public boolean updateUser(User u) {
        return dao.updateUser(u);
    }

    public boolean removeUser(int usid) {
        return dao.removeUser(usid);
    }

    public List<User> getAllUsers() {
        return dao.getAllUsers();
    }

    public User findUser(String username) {
        return dao.findUserByName(username);
    }
}
