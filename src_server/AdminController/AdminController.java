//package AdminController;
//
//import Model.ModelBO;
//import View.ViewAdminConfigure;
//
//public class AdminController {
//	private ModelBO BO = new ModelBO();
//	private ViewAdminConfigure view;
//	
//	public AdminController(ViewAdminConfigure viewConfig) {
//		this.view = viewConfig;
//	}
//
//}
package AdminController;

import Model.LogicCoordinator;
import Model.Account.AccountBO;
import Model.Bean.FolderPath;
import Model.File.FileBO;
import Model.Permission.PermissionBO;
import View.ViewAdminConfigure;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.util.List;

public class AdminController {

    private final LogicCoordinator modelBO = new LogicCoordinator();
    private final AccountBO accBO = new AccountBO();
    private final FileBO fileBO = new FileBO();
    private final PermissionBO permsBO = new PermissionBO();
    private final ViewAdminConfigure view;

    public AdminController(ViewAdminConfigure view) {
        this.view = view;
        view.initUserButtonEvents(this);
        loadUserListToView();
        initUserClick();
        initMountTableClick();
    }

    // Load danh sách user lên JList
    public void loadUserListToView() {
        List<String> usernames = accBO.getAllUsernames();
        view.loadUserList(usernames);
    }

    public void addUser(String username) {

        DefaultListModel<String> model = (DefaultListModel<String>) view.getUserList().getModel();

        if (model.contains(username)) {
            JOptionPane.showMessageDialog(view, "User already exists in list");
            return;
        }

        model.addElement(username);
        view.getUserList().setSelectedValue(username, true);

        // cho phép nhập password
        view.getTxtPassword().setText("");
    }

    public void removeUser(String username) {
        if (username == null) {
            JOptionPane.showMessageDialog(view, "Please select a user first!");
            return;
        }
        // Lấy usid từ username
        int usid = accBO.getUsidByUsername(username);
        if (usid <= 0) {
            JOptionPane.showMessageDialog(view, "Cannot find user in database!");
            return;
        }

        // 1. Xóa tất cả userpermission liên quan đến usid
        List<Integer> fids = permsBO.getListFidByUsid(usid);
        if (fids != null) {
            for (Integer fid : fids) {
                permsBO.deleteUserPermission(usid, fid);
            }
        }

        // 2. Xóa user khỏi bảng user
        if (accBO.deleteUser(username)) {
            DefaultListModel<String> model = (DefaultListModel<String>) view.getUserList().getModel();
            model.removeElement(username);
            view.loadFolderTable(null); // Xóa bảng mount của user trên GUI
            JOptionPane.showMessageDialog(view, "User deleted successfully!");
        } else {
            JOptionPane.showMessageDialog(view, "Failed to delete user.");
        }
    }

    public void renameUser(String oldName, String newName) {
        if (oldName == null) {
            JOptionPane.showMessageDialog(view, "Please select a user first!");
            return;
        }

        if (newName == null || newName.trim().isEmpty()) {
            JOptionPane.showMessageDialog(view, "New username cannot be empty!");
            return;
        }

        if (accBO.updateUsername(oldName, newName.trim())) {
            DefaultListModel<String> model = (DefaultListModel<String>) view.getUserList().getModel();
            int index = view.getUserList().getSelectedIndex();
            if (index != -1) {
                model.set(index, newName.trim());
            }
            JOptionPane.showMessageDialog(view, "User renamed successfully!");
        } else {
            JOptionPane.showMessageDialog(view, "Failed to rename user.");
        }
    }

    public boolean saveUserAll() {

        // ===============================
        // 1. KIỂM TRA USER ĐƯỢC CHỌN
        // ===============================
        String username = view.getUserList().getSelectedValue();
        if (username == null || username.trim().isEmpty()) {
            JOptionPane.showMessageDialog(view, "Please select a user");
            return false;
        }

        String password = view.getTxtPassword().getText();
        if (password == null || password.trim().isEmpty()) {
            JOptionPane.showMessageDialog(view, "Password cannot be empty");
            return false;
        }

        // ===============================
        // 2. INSERT / UPDATE USER
        // ===============================
        boolean ok = accBO.insertUser(username, password);
        if (!ok) {
            ok = accBO.updateUserPassword(username, password);
        }
        if (!ok) {
            JOptionPane.showMessageDialog(view, "Save user failed");
            return false;
        }

        int usid = accBO.getUsidByUsername(username);
        if (usid <= 0) {
            JOptionPane.showMessageDialog(view, "Cannot get usid for user");
            return false;
        }

        // ===============================
        // 3. LẤY ACCESS MODE TỪ COMBO
        // ===============================
        JComboBox<String> combo = view.getPermissionComboBox();
        String sel = (String) combo.getSelectedItem();

        String accessMode = "RWD";
        if ("Read Only".equals(sel))
            accessMode = "RO";
        else if ("Disabled".equals(sel))
            accessMode = "DIS";

        // ===============================
        // 4. LƯU FOLDER + PERMISSION
        // ===============================
        JTable table = view.getMountTable();
        // Nếu user đang edit cell, stop edit để lấy giá trị mới
        if (table.isEditing()) {
            table.getCellEditor().stopCellEditing();
        }

        DefaultTableModel tableModel = view.getMountTableModel();
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String virtualPath = String.valueOf(tableModel.getValueAt(i, 0)).trim();
            String nativePath = String.valueOf(tableModel.getValueAt(i, 1)).trim();

            if (virtualPath.isEmpty() || nativePath.isEmpty()) {
                System.out.println("[DEBUG] Skipping empty row: " + virtualPath + " | " + nativePath);
                continue;
            }

            System.out.println("[DEBUG] Insert folder: " + virtualPath + " | " + nativePath);
            FolderPath fp = fileBO.insertIfNotExists(virtualPath, nativePath);

            if (fp == null) {
                System.err.println("Failed to insert folder: " + virtualPath + " | " + nativePath);
                JOptionPane.showMessageDialog(view, "Cannot save folder: " + virtualPath);
                return false;
            }

            int fid = fp.getFid();

            boolean permOk = permsBO.saveUserPermission(usid, fid, accessMode);
            if (!permOk) {
                System.err.println("Failed to save permission: usid=" + usid + ", fid=" + fid);
                JOptionPane.showMessageDialog(view, "Save permission failed (usid=" + usid + ", fid=" + fid + ")");
                return false;
            }
        }

        // ===============================
        // 5. RELOAD DATA LÊN VIEW
        // ===============================
        List<FolderPath> folders = modelBO.getListFolderPermission(usid);
        view.loadFolderTable(folders);

        loadUserListToView();
        view.getUserList().setSelectedValue(username, true);

        JOptionPane.showMessageDialog(view, "Save user, folders, permissions successfully!");
        return true;
    }

    private void initUserClick() {
        view.getUserList().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting())
                return;

            String username = view.getUserList().getSelectedValue();
            if (username == null)
                return;

            // Lấy usid từ database
            int usid = accBO.getUsidByUsername(username);

            // Nếu user chưa có trong DB (usid <= 0) → bảng mount trống
            if (usid <= 0) {
                view.loadFolderTable(null); // bảng rỗng
                return;
            }

            // Nếu user đã có trong DB → load folder permissions
            List<FolderPath> folders = modelBO.getListFolderPermission(usid);
            view.loadFolderTable(folders);
        });
    }

    private void initMountTableClick() {

        JTable table = view.getMountTable();

        table.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting())
                return;

            int row = table.getSelectedRow();
            if (row < 0)
                return;

            // Lấy user đang chọn
            String username = view.getUserList().getSelectedValue();
            if (username == null)
                return;

            int usid = accBO.getUsidByUsername(username);
            if (usid <= 0)
                return;

            // Lấy folder từ dòng table
            String vpath = String.valueOf(table.getValueAt(row, 0));
            String npath = String.valueOf(table.getValueAt(row, 1));

            FolderPath fp = fileBO.getByVirtualAndNativePath(vpath, npath);

            if (fp == null)
                return;

            // Lấy access mode từ DB
            String mode = permsBO.getAccessMode(usid, fp.getFid());

            JComboBox<String> combo = view.getPermissionComboBox();

            if ("RO".equals(mode))
                combo.setSelectedItem("Read Only");
            else if ("DIS".equals(mode))
                combo.setSelectedItem("Disabled");
            else
                combo.setSelectedItem("Read + Write + Delete");
        });
    }

}