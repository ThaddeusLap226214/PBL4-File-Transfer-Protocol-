package Model;

import Model.Account.AccountBO;
import Model.Bean.User;
import Model.File.FileBO;
import Model.Bean.FolderPath;

import java.util.List;

public class ModelBO {

    private final AccountBO accBO = new AccountBO();
    private final FileBO fileBO = new FileBO();

    // ==================== USER ====================
    public boolean addUser(User u) {
        return accBO.addUser(u);
    }

    public boolean updateUser(User u) {
        return accBO.updateUser(u);
    }

    public boolean removeUser(int usid) {
        return accBO.removeUser(usid);
    }

    public List<User> getAllUsers() {
        return accBO.getAllUsers();
    }

    public User findUser(String username) {
        return accBO.findUser(username);
    }

}
