package AdminController;

import Model.ModelBO;
import Model.Bean.User;
import View.ViewAdminConfigure;

import javax.swing.*;
import java.util.List;

public class AdminController {

    private final ModelBO modelBO = new ModelBO();
    private final ViewAdminConfigure view;

    public AdminController(ViewAdminConfigure viewConfig) {
        this.view = viewConfig;
        initController();
        loadUserList();
    }

    // ================= INIT CONTROLLER =================
    private void initController() {
        // Add user
        view.getBtnAddUser().addActionListener(e -> addUser());

        // Remove user
        view.getBtnRemoveUser().addActionListener(e -> removeUser());

        // Duplicate user
        view.getBtnDuplicateUser().addActionListener(e -> duplicateUser());

        // Rename user
        view.getBtnRenameUser().addActionListener(e -> renameUser());

        // Apply / OK
        view.getBtnApply().addActionListener(e -> {
            saveSelectedUser(); // lưu vào database
            view.dispose(); // đóng cửa sổ
        });

        view.getBtnOk().addActionListener(e -> {
            saveSelectedUser(); // lưu vào database
            // KHÔNG đóng cửa sổ
        });

        // Load selected user to tab when selected in JList
        view.getUserList().addListSelectionListener(e -> loadSelectedUserToTab());
    }

    // ================= LOAD USER LIST =================
    private void loadUserList() {
        DefaultListModel<String> model = new DefaultListModel<>();
        for (User u : modelBO.getAllUsers()) {
            model.addElement(u.getUsername());
        }
        view.getUserList().setModel(model);
    }

    // ================= HELPER: GET SELECTED USER =================
    private User getSelectedUser() {
        String selected = view.getUserList().getSelectedValue();
        if (selected == null)
            return null;
        return modelBO.getAllUsers().stream()
                .filter(u -> u.getUsername().equals(selected))
                .findFirst()
                .orElse(null);
    }

    // ================= ADD USER =================
    private void addUser() {
        List<User> users = modelBO.getAllUsers();
        int i = 1;
        String newName;

        // Tạo tên user không trùng
        while (true) {
            newName = "<new_user" + i + ">";
            i++;
            final String checkName = newName;
            boolean exists = users.stream().anyMatch(u -> u.getUsername().equals(checkName));
            if (!exists)
                break;
        }

        // ✅ Sửa lỗi: grid phải là 1 hoặc 2 (tồn tại trong bảng groups)
        // ✅ Sửa lỗi: password không được rỗng
        User newUser = new User(0, newName, " ", 1); // 1 dấu cách

        if (modelBO.addUser(newUser)) {
            loadUserList();
            view.getUserList().setSelectedValue(newName, true);
            // ✅ Reset password field
            view.getTxtPassword().setText("");
        } else {
            JOptionPane.showMessageDialog(view, "Error adding user!");
        }
    }

    // ================= REMOVE USER =================
    private void removeUser() {
        User selected = getSelectedUser();
        if (selected != null && modelBO.removeUser(selected.getUsid())) {
            loadUserList();
        } else {
            JOptionPane.showMessageDialog(view, "Error removing user!");
        }
    }

    // ================= DUPLICATE USER =================
    private void duplicateUser() {
        User selected = getSelectedUser();
        if (selected == null)
            return;

        List<User> users = modelBO.getAllUsers();
        int i = 1;
        String copyName;

        while (true) {
            copyName = "<copy_user" + i + ">";
            i++;
            final String checkName = copyName;
            boolean exists = users.stream().anyMatch(u -> u.getUsername().equals(checkName));
            if (!exists)
                break;
        }

        // ✅ Copy user nhưng đảm bảo grid hợp lệ
        int validGrid = selected.getGrid() == 1 || selected.getGrid() == 2 ? selected.getGrid() : 1;

        User copy = new User(0, copyName, selected.getPassword(), validGrid);

        if (modelBO.addUser(copy)) {
            loadUserList();
            view.getUserList().setSelectedValue(copyName, true);
        } else {
            JOptionPane.showMessageDialog(view, "Error duplicating user!");
        }
    }

    // ================= RENAME USER =================
    private void renameUser() {
        User selected = getSelectedUser();
        if (selected == null)
            return;

        final String newName = JOptionPane.showInputDialog(view, "Enter new username:", selected.getUsername());
        if (newName != null && !newName.isEmpty()) {
            selected.setUsername(newName);
            if (modelBO.updateUser(selected)) {
                loadUserList();
                view.getUserList().setSelectedValue(newName, true);
            } else {
                JOptionPane.showMessageDialog(view, "Error renaming user!");
            }
        }
    }

    // ================= LOAD SELECTED USER INTO TAB =================
    private void loadSelectedUserToTab() {
        User selected = getSelectedUser();
        if (selected == null)
            return;

        // LẤY txtPassword từ View
        JTextField txtPassword = view.getTxtPassword();

        // GÁN mật khẩu của user vào ô nhập
        txtPassword.setText(selected.getPassword());
    }

    // ================= SAVE USER =================
    private void saveSelectedUser() {
        User selected = getSelectedUser();
        if (selected == null)
            return;

        try {
            java.lang.reflect.Field field = view.getClass().getDeclaredField("txtPassword");
            field.setAccessible(true);
            JTextField txtPass = (JTextField) field.get(view);
            selected.setPassword(txtPass.getText());
        } catch (Exception e) {
            e.printStackTrace();
        }

        modelBO.updateUser(selected);
        loadUserList();
        User u = getSelectedUser();
        if (u != null)
            view.getUserList().setSelectedValue(u.getUsername(), true);
    }

}
