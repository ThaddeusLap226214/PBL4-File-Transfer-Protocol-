package Model.Account;

import Model.Bean.User;
import java.util.List;

public interface AccountDAO {

    boolean addUser(User u);

    boolean updateUser(User u);

    boolean removeUser(int usid);

    List<User> getAllUsers();

    // Lấy user theo username
    User findUserByName(String username);
}
