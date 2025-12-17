//package View;
//
//import javax.swing.JFrame;
//
//public class ViewAdminConfigure extends JFrame{
//
//}
package View;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

import AdminController.AdminController;
import Model.Bean.FolderPath;

import java.awt.*;
import java.util.List;

public class ViewAdminConfigure extends JFrame {

    private JPanel rightPanel; //vùng nhập liệu bên phải
    private CardLayout cardLayout;

    // USERS PANEL COMPONENTS
    private JPanel usersPanel;
    private JList<String> userList; //Danh sách người dùng
    private JTextField txtPassword;
    private DefaultTableModel mountTableModel;
    
    public static void main(String[] args) {
		new ViewAdminConfigure();
	}

    public ViewAdminConfigure() {
    	
//    		setVisible(true);

        setTitle("FTP Server Configuration");
        setMinimumSize(new Dimension(900, 450));
        setSize(1350, 770);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setLayout(new BorderLayout());

        // TREE
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Root");
        DefaultMutableTreeNode rights = new DefaultMutableTreeNode("Rights management");
        rights.add(new DefaultMutableTreeNode("Groups"));
        rights.add(new DefaultMutableTreeNode("Users"));
        root.add(rights);

        JTree tree = new JTree(root);
        tree.setMinimumSize(new Dimension(260, 300));
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        JScrollPane treeScroll = new JScrollPane(tree);
        treeScroll.setMinimumSize(new Dimension(260, 300));

        // RIGHT PANEL
        cardLayout = new CardLayout();
        rightPanel = new JPanel(cardLayout);
        rightPanel.add(emptyPanel("Groups settings panel"), "Groups");
        usersPanel = createUsersPanel();
        rightPanel.add(usersPanel, "Users");

        // SPLIT PANE
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, treeScroll, rightPanel);
        split.setDividerLocation(260);
        split.setResizeWeight(0);
        add(split, BorderLayout.CENTER);

        // TREE SELECTION
        tree.addTreeSelectionListener(e -> {
            DefaultMutableTreeNode selected = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
            if (selected == null)
                return;
            String key = selected.toString();
            if (key.equals("Groups") || key.equals("Users")) {
                cardLayout.show(rightPanel, key);
            }
        });
    }

    // USERS PANEL
    private JPanel createUsersPanel() {

        JPanel main = new JPanel(new BorderLayout());
        main.setMinimumSize(new Dimension(580, 400));

        JLabel title = new JLabel("Rights management / Users");
        title.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        main.add(title, BorderLayout.NORTH);

        DefaultListModel<String> userListModel = new DefaultListModel<>();
        userList = new JList<>(userListModel);
        JScrollPane leftScroll = new JScrollPane(userList);
        leftScroll.setMinimumSize(new Dimension(230, 350));

        // BUTTON PANEL LEFT
        JPanel leftButtons = new JPanel(new GridLayout(2, 2, 10, 5));
        leftButtons.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        leftButtons.setMinimumSize(new Dimension(230, 80));
        leftButtons.setPreferredSize(new Dimension(230, 80));

        JButton btnAdd = new JButton("Add");
        JButton btnRemove = new JButton("Remove");
        JButton btnRename = new JButton("Rename");

        leftButtons.add(btnAdd);
        leftButtons.add(btnRemove);
        leftButtons.add(btnRename);

        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(leftScroll, BorderLayout.CENTER);
        leftPanel.add(leftButtons, BorderLayout.SOUTH);
        leftPanel.setMinimumSize(new Dimension(260, 500));

        // TABS
        JTabbedPane tabs = new JTabbedPane();
        tabs.setMinimumSize(new Dimension(600, 450));

        JPanel generalTab = createGeneralTab();
        tabs.add("General", generalTab);

        JSplitPane userSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, tabs);
        userSplit.setDividerLocation(260);
        userSplit.setResizeWeight(0);

        main.add(userSplit, BorderLayout.CENTER);

        // BOTTOM BUTTONS
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnOk = new JButton("OK");
        JButton btnCancel = new JButton("Cancel");
        JButton btnApply = new JButton("Apply");

        bottom.add(btnOk);
        bottom.add(btnCancel);
        bottom.add(btnApply);

        main.add(bottom, BorderLayout.SOUTH);

        // STORE COMPONENTS
        main.putClientProperty("userList", userList);
        main.putClientProperty("btnAdd", btnAdd);
        main.putClientProperty("btnRemove", btnRemove);
        main.putClientProperty("btnRename", btnRename);
        main.putClientProperty("btnApply", btnApply);
        main.putClientProperty("btnOk", btnOk);
        main.putClientProperty("generalTab", generalTab);
        main.putClientProperty("tabs", tabs);

        return main;
    }

    // GENERAL TAB
    private JPanel createGeneralTab() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setMinimumSize(new Dimension(650, 450));
        int y = 0;

        addG(panel, new JCheckBox("User is enabled"), 0, y++, 2);
        addG(panel, new JLabel("Authentication:"), 0, y, 1);
        addG(panel, new JComboBox<>(new String[] {
                "Require a password to log in", "No password required"
        }), 1, y++, 1);

        txtPassword = new JTextField();
        addG(panel, txtPassword, 1, y++, 1);

        addG(panel, new JLabel("Member of groups:"), 0, y, 1);
        JComboBox<String> comboGroups = new JComboBox<>(new String[] { "" });
        addG(panel, comboGroups, 1, y++, 1);

        addG(panel, new JLabel("Mount points:"), 0, y++, 2);

        mountTableModel = new DefaultTableModel(
                new Object[] { "Virtual path", "Native path" },
                0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return true;
            }
        };

        JTable table = new JTable(mountTableModel);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setMinimumSize(new Dimension(380, 250));
        panel.putClientProperty("mountTable", table);
        panel.putClientProperty("mountTableModel", mountTableModel);

        JPanel opt = new JPanel(new GridBagLayout());
        opt.setMinimumSize(new Dimension(280, 250));
        int oy = 0;

        addO(opt, new JLabel("Mount options"), 0, oy++, 2);
        addO(opt, new JLabel("Access mode:"), 0, oy, 1);

        JComboBox<String> combo = new JComboBox<>(new String[] {
                "Read + Write + Delete",
                "Read Only",
                "Disabled"
        });
        combo.setSelectedIndex(0); // không chọn gì khi mở form

        panel.putClientProperty("comboPermission", combo);
        JCheckBox chkApplySub = new JCheckBox("Apply permissions to subdirectories");
        JCheckBox chkWritable = new JCheckBox("Writable directory structure");
        JCheckBox chkCreateNative = new JCheckBox("Create native directory if it does not exist");

        addO(opt, combo, 1, oy++, 1);
        addO(opt, chkApplySub, 0, oy++, 2);
        addO(opt, chkWritable, 0, oy++, 2);
        addO(opt, chkCreateNative, 0, oy++, 2);
        
        // Mặc định RWD
        chkApplySub.setEnabled(false);
        chkWritable.setEnabled(false);
        chkCreateNative.setEnabled(false);
        chkApplySub.setSelected(true);
        chkWritable.setSelected(true);
        chkCreateNative.setSelected(true);

        combo.addActionListener(e -> {
            String selected = (String) combo.getSelectedItem();

            switch (selected) {
                case "Read + Write + Delete":
                    chkApplySub.setSelected(true);
                    chkWritable.setSelected(true);
                    chkCreateNative.setSelected(true);
                    break;
                case "Read Only":
                    chkApplySub.setSelected(true);
                    chkWritable.setSelected(false);
                    chkCreateNative.setSelected(true);
                    break;
                case "Disabled":
                    chkApplySub.setSelected(true);
                    chkWritable.setSelected(false);
                    chkCreateNative.setSelected(false);
                    break;
            }
        });
        JSplitPane mtSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scroll, opt);
        mtSplit.setDividerLocation(380);
        mtSplit.setResizeWeight(0.7);
        addGFull(panel, mtSplit, 0, y++, 2);

        JPanel btnPanel = new JPanel();
        JButton addMount = new JButton("Add");
        JButton removeMount = new JButton("Remove");
        btnPanel.add(addMount);
        btnPanel.add(removeMount);
        addGFull(panel, btnPanel, 0, y, 2);

        addMount.addActionListener(e -> {
            int row = mountTableModel.getRowCount();
            mountTableModel.addRow(new Object[] { "", "" });
            table.setRowSelectionInterval(row, row);
            table.editCellAt(row, 0);
            table.requestFocus();
        });

        removeMount.addActionListener(e -> {
            int sel = table.getSelectedRow();
            if (sel != -1)
                mountTableModel.removeRow(sel);
        });

        panel.putClientProperty("txtPassword", txtPassword);

        return panel;
    }

    // LAYOUT HELPERS
    private void addG(JPanel p, Component comp, int x, int y, int w) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = x;
        c.gridy = y;
        c.gridwidth = w;
        c.insets = new Insets(5, 5, 5, 5);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        p.add(comp, c);
    }

    private void addGFull(JPanel p, Component comp, int x, int y, int w) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = x;
        c.gridy = y;
        c.gridwidth = w;
        c.insets = new Insets(5, 5, 5, 5);
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.weighty = 1;
        p.add(comp, c);
    }

    private void addO(JPanel p, Component comp, int x, int y, int w) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = x;
        c.gridy = y;
        c.gridwidth = w;
        c.insets = new Insets(5, 5, 5, 5);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        p.add(comp, c);
    }

    private JPanel emptyPanel(String text) {
        JPanel p = new JPanel();
        p.add(new JLabel(text));
        return p;
    }

    // EVENTS
    public void initUserButtonEvents(AdminController controller) {

        JButton btnAdd = getBtnAddUser();
        JButton btnRemove = getBtnRemoveUser();
        JButton btnRename = getBtnRenameUser();
        JButton btnApply = getBtnApply();
        JButton btnOk = getBtnOk();

        // ADD USER
        btnAdd.addActionListener(e -> {
            String username = JOptionPane.showInputDialog(
            		this, 
            		"Enter new username", 
            		"Add User",
            		JOptionPane.PLAIN_MESSAGE);
            if (username != null && !username.trim().isEmpty()) {
                controller.addUser(username.trim());
            }
        });

        // REMOVE USER
        btnRemove.addActionListener(e -> {
            String selected = userList.getSelectedValue();
            if (selected == null) {
                JOptionPane.showMessageDialog(this, "Please select a user to remove", "Warning",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(this, "Delete user: " + selected + "?", "Confirm",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION)
                controller.removeUser(selected);
        });

        // RENAME USER
        btnRename.addActionListener(e -> {
            String selected = userList.getSelectedValue();
            if (selected == null) {
                JOptionPane.showMessageDialog(this, "Please select a user to rename", "Warning",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            String newName = JOptionPane.showInputDialog(this, "Enter new username", selected);
            if (newName != null && !newName.trim().isEmpty()) {
                controller.renameUser(selected, newName.trim());
            }
        });

        btnApply.addActionListener(e -> controller.saveUserAll());
        btnOk.addActionListener(e -> {
            controller.saveUserAll();
            this.dispose();
        });
    }

    // GETTERS
    public JList<String> getUserList() {
        return userList;
    }

    public JButton getBtnAddUser() {
        return (JButton) usersPanel.getClientProperty("btnAdd");
    }

    public JButton getBtnRemoveUser() {
        return (JButton) usersPanel.getClientProperty("btnRemove");
    }

    public JButton getBtnRenameUser() {
        return (JButton) usersPanel.getClientProperty("btnRename");
    }

    public JButton getBtnApply() {
        return (JButton) usersPanel.getClientProperty("btnApply");
    }

    public JButton getBtnOk() {
        return (JButton) usersPanel.getClientProperty("btnOk");
    }

    public JTextField getTxtPassword() {
        JPanel generalTab = (JPanel) usersPanel.getClientProperty("generalTab");
        return (JTextField) generalTab.getClientProperty("txtPassword");
    }

    public JTable getMountTable() {
        JPanel generalTab = (JPanel) usersPanel.getClientProperty("generalTab");
        return (JTable) generalTab.getClientProperty("mountTable");
    }

    public DefaultTableModel getMountTableModel() {
        JPanel generalTab = (JPanel) usersPanel.getClientProperty("generalTab");
        return (DefaultTableModel) generalTab.getClientProperty("mountTableModel");
    }

    public JComboBox<String> getPermissionComboBox() {
        JPanel generalTab = (JPanel) usersPanel.getClientProperty("generalTab");
        return (JComboBox<String>) generalTab.getClientProperty("comboPermission");
    }

    // LOAD DATA
    public void loadUserList(List<String> usernames) {
        DefaultListModel<String> model = (DefaultListModel<String>) userList.getModel();
        model.clear();
        for (String username : usernames)
            model.addElement(username);
    }

    public void loadFolderTable(List<FolderPath> folders) {
        DefaultTableModel model = getMountTableModel();
        model.setRowCount(0);
        if (folders != null) {
            for (FolderPath fp : folders) {
                model.addRow(new Object[] { fp.getVirtualPath(), fp.getNativePath() });
            }
        }
    }
}