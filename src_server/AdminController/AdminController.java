package AdminController;

import Model.ModelBO;
import Model.Bean.FolderPath;
import View.ViewAdminConfigure;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.util.List;

public class AdminController {

    private final ModelBO modelBO;
    private final ViewAdminConfigure view;

    public AdminController(ViewAdminConfigure view) {
        this.view = view;
        this.modelBO = new ModelBO();
        loadUserListToView();
        initUserClick();
        initMountTableClick();

    }

    // Load danh sách user lên JList
    public void loadUserListToView() {
        List<String> usernames = modelBO.getAllUsernames();
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
        int usid = modelBO.getAccountBO().getUsidByUsername(username);
        if (usid <= 0) {
            JOptionPane.showMessageDialog(view, "Cannot find user in database!");
            return;
        }

        // 1. Xóa tất cả userpermission liên quan đến usid
        List<Integer> fids = modelBO.getPermsBO().getListFidByUsid(usid);
        if (fids != null) {
            for (Integer fid : fids) {
                modelBO.getPermsBO().deleteUserPermission(usid, fid);
            }
        }

        // 2. Xóa user khỏi bảng user
        if (modelBO.getAccountBO().deleteUser(username)) {
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

        if (modelBO.updateUsername(oldName, newName.trim())) {
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
        boolean ok = modelBO.insertUser(username, password);
        if (!ok) {
            ok = modelBO.updateUserPassword(username, password);
        }
        if (!ok) {
            JOptionPane.showMessageDialog(view, "Save user failed");
            return false;
        }

        int usid = modelBO.getAccountBO().getUsidByUsername(username);
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
            FolderPath fp = modelBO.getFileBO().insertIfNotExists(virtualPath, nativePath);

            if (fp == null) {
                System.err.println("Failed to insert folder: " + virtualPath + " | " + nativePath);
                JOptionPane.showMessageDialog(view, "Cannot save folder: " + virtualPath);
                return false;
            }

            int fid = fp.getFid();

            boolean permOk = modelBO.getPermsBO().saveUserPermission(usid, fid, accessMode);
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
            int usid = modelBO.getAccountBO().getUsidByUsername(username);

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

            int usid = modelBO.getAccountBO().getUsidByUsername(username);
            if (usid <= 0)
                return;

            // Lấy folder từ dòng table
            String vpath = String.valueOf(table.getValueAt(row, 0));
            String npath = String.valueOf(table.getValueAt(row, 1));

            FolderPath fp = modelBO.getFileBO()
                    .getByVirtualAndNativePath(vpath, npath);

            if (fp == null)
                return;

            // Lấy access mode từ DB
            String mode = modelBO.getPermsBO()
                    .getAccessMode(usid, fp.getFid());

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
