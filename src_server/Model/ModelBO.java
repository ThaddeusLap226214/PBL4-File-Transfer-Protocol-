package Model;

import java.util.List;
import java.util.ArrayList;

import Model.Account.AccountBO;
import Model.Bean.FolderPath;
import Model.Bean.UserPermission;
import Model.File.FileBO;
import Model.Permission.PermissionBO;

public class ModelBO {

    private final AccountBO accBO;
    private final FileBO fileBO;
    private final PermissionBO permsBO;

    public ModelBO() {
        this.accBO = new AccountBO();
        this.fileBO = new FileBO();
        this.permsBO = new PermissionBO();
    }

    // Sửa getter cho đúng tên biến
    public AccountBO getAccountBO() {
        return accBO;
    }

    public FileBO getFileBO() {
        return fileBO;
    }

    public PermissionBO getPermsBO() {
        return permsBO;
    }

    // User management
    public boolean findUser(String username) {
        return accBO.findUser(username);
    }

    public int checkLogin(String username, String password) {
        return accBO.checkLogin(username, password);
    }

    public List<String> getAllUsernames() {
        return accBO.getAllUsernames();
    }

    public boolean insertUser(String username, String password) {
        return accBO.insertUser(username, password);
    }

    public boolean deleteUser(String username) {
        return accBO.deleteUser(username);
    }

    public boolean updateUsername(String oldName, String newName) {
        return accBO.updateUsername(oldName, newName);
    }

    public boolean updateUserPassword(String username, String password) {
        return accBO.updateUserPassword(username, password);
    }

    public List<FolderPath> getAllFolders() {
        return fileBO.getAllFolders();
    }

    public List<FolderPath> getListFolderPermission(int usid) {
        List<FolderPath> listFp = new ArrayList<>();
        if (usid <= 0)
            return listFp;

        // Lấy danh sách permission theo user
        List<UserPermission> perms = permsBO.getUserPermissionsByUsid(usid);
        if (perms == null)
            return listFp;

        for (UserPermission up : perms) {
            FolderPath fp = fileBO.getByFid(up.getFid());
            if (fp != null) {
                fp.setAccessMode(up.getAccessMode()); // lưu accessMode vào FolderPath
                listFp.add(fp);
            }
        }
        return listFp;
    }
}
