package View;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;

public class ViewAdminConfigure extends JFrame {

    private JPanel rightPanel;
    private CardLayout cardLayout;

    public ViewAdminConfigure() {

        setTitle("FTP Server Configuration");
        setMinimumSize(new Dimension(1200, 650));
        setSize(1350, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // ---------------- TREE ----------------
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Root");
        root.add(new DefaultMutableTreeNode("Server listeners"));
        root.add(new DefaultMutableTreeNode("Protocols settings"));
        root.add(new DefaultMutableTreeNode("FTP and FTP over TLS (FTPS)"));

        DefaultMutableTreeNode rights = new DefaultMutableTreeNode("Rights management");
        rights.add(new DefaultMutableTreeNode("Groups"));
        rights.add(new DefaultMutableTreeNode("Users"));
        root.add(rights);

        root.add(new DefaultMutableTreeNode("Administration"));
        root.add(new DefaultMutableTreeNode("Logging"));
        root.add(new DefaultMutableTreeNode("Let's Encrypt®"));
        root.add(new DefaultMutableTreeNode("PKCS#11"));
        root.add(new DefaultMutableTreeNode("Updates"));

        JTree tree = new JTree(root);
        tree.setMinimumSize(new Dimension(260, 500));
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

        JScrollPane treeScroll = new JScrollPane(tree);
        treeScroll.setMinimumSize(new Dimension(260, 600));

        // ---------------- RIGHT PANEL ----------------
        cardLayout = new CardLayout();
        rightPanel = new JPanel(cardLayout);

        rightPanel.add(emptyPanel("Server listeners panel"), "Server listeners");
        rightPanel.add(emptyPanel("Protocols settings panel"), "Protocols settings");
        rightPanel.add(emptyPanel("FTPS settings panel"), "FTP and FTP over TLS (FTPS)");
        rightPanel.add(emptyPanel("Groups settings panel"), "Groups");
        rightPanel.add(emptyPanel("Administration panel"), "Administration");
        rightPanel.add(emptyPanel("Logging panel"), "Logging");
        rightPanel.add(emptyPanel("Let's Encrypt panel"), "Let's Encrypt®");
        rightPanel.add(emptyPanel("PKCS#11 panel"), "PKCS#11");
        rightPanel.add(emptyPanel("Updates panel"), "Updates");

        JPanel usersPanel = createUsersPanel();
        rightPanel.add(usersPanel, "Users");

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, treeScroll, rightPanel);
        split.setDividerLocation(260);
        split.setResizeWeight(0);

        add(split, BorderLayout.CENTER);

        // ---------------- TREE SELECTION ----------------
        tree.addTreeSelectionListener(e -> {
            DefaultMutableTreeNode selected = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
            if (selected == null)
                return;

            String key = selected.toString();
            if (!key.equals("Rights management")) {
                cardLayout.show(rightPanel, key);
            }
        });
    }

    // =====================================================================
    // USERS PANEL
    // =====================================================================
    private JPanel createUsersPanel() {

        JPanel main = new JPanel(new BorderLayout());
        main.setMinimumSize(new Dimension(700, 500));

        JLabel title = new JLabel("Rights management / Users");
        title.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        main.add(title, BorderLayout.NORTH);

        DefaultListModel<String> userListModel = new DefaultListModel<>();
        userListModel.addElement("<system user>");
        JList<String> userList = new JList<>(userListModel);
        JScrollPane leftScroll = new JScrollPane(userList);
        leftScroll.setMinimumSize(new Dimension(230, 350));

        // ---------------- BUTTON PANEL LEFT ----------------
        JPanel leftButtons = new JPanel(new GridLayout(2, 2, 10, 5)); // 2 hàng x 2 cột
        leftButtons.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        leftButtons.setMinimumSize(new Dimension(230, 80));
        leftButtons.setPreferredSize(new Dimension(230, 80));

        JButton btnAdd = new JButton("Add");
        JButton btnRemove = new JButton("Remove");
        JButton btnDuplicate = new JButton("Duplicate");
        JButton btnRename = new JButton("Rename");

        leftButtons.add(btnAdd);
        leftButtons.add(btnRemove);
        leftButtons.add(btnDuplicate);
        leftButtons.add(btnRename);

        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(leftScroll, BorderLayout.CENTER);
        leftPanel.add(leftButtons, BorderLayout.SOUTH);
        leftPanel.setMinimumSize(new Dimension(260, 500));

        // ---------------- TABS ----------------
        JTabbedPane tabs = new JTabbedPane();
        tabs.setMinimumSize(new Dimension(600, 450));

        JPanel generalTab = createGeneralTab();
        tabs.add("General", generalTab);
        tabs.add("Filters", new JPanel());
        tabs.add("Limits", new JPanel());
        tabs.add("Protocol policies", new JPanel());

        JSplitPane userSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, tabs);
        userSplit.setDividerLocation(260);
        userSplit.setResizeWeight(0);

        main.add(userSplit, BorderLayout.CENTER);

        // ---------------- BOTTOM BUTTONS ----------------
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnOk = new JButton("OK");
        JButton btnCancel = new JButton("Cancel");
        JButton btnApply = new JButton("Apply");

        bottom.add(btnOk);
        bottom.add(btnCancel);
        bottom.add(btnApply);

        main.add(bottom, BorderLayout.SOUTH);

        // ---------------- STORE COMPONENTS IN CLIENT PROPERTIES ----------------
        main.putClientProperty("userList", userList);
        main.putClientProperty("btnAdd", btnAdd);
        main.putClientProperty("btnRemove", btnRemove);
        main.putClientProperty("btnDuplicate", btnDuplicate);
        main.putClientProperty("btnRename", btnRename);
        main.putClientProperty("btnApply", btnApply);
        main.putClientProperty("btnOk", btnOk);
        main.putClientProperty("generalTab", generalTab);
        main.putClientProperty("tabs", tabs);

        return main;
    }

    // =====================================================================
    // GENERAL TAB
    // =====================================================================
    private JPanel createGeneralTab() {

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setMinimumSize(new Dimension(650, 450));

        int y = 0;

        addG(panel, new JCheckBox("User is enabled"), 0, y++, 2);

        addG(panel, new JLabel("Authentication:"), 0, y, 1);
        addG(panel, new JComboBox<>(new String[] {
                "Require a password to log in", "No password required"
        }), 1, y++, 1);

        JTextField txtPassword = new JTextField();
        addG(panel, txtPassword, 1, y++, 1);

        addG(panel, new JLabel("Mount points:"), 0, y++, 2);

        // Table
        DefaultTableModel model = new DefaultTableModel(
                new Object[] { "Virtual path", "Native path" },
                0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return true; // Bật chế độ editable
            }
        };

        JTable table = new JTable(model);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setMinimumSize(new Dimension(380, 250));

        // Options
        JPanel opt = new JPanel(new GridBagLayout());
        opt.setMinimumSize(new Dimension(280, 250));

        int oy = 0;

        addO(opt, new JLabel("Mount options"), 0, oy++, 2);
        addO(opt, new JLabel("Access mode:"), 0, oy, 1);

        // ====== Tạo combo và checkbox thành biến ======
        JComboBox<String> combo = new JComboBox<>(new String[] {
                "Read + Write",
                "Read Only",
                "Write Only",
                "Disabled"
        });

        JCheckBox chkApplySub = new JCheckBox("Apply permissions to subdirectories");
        JCheckBox chkWritable = new JCheckBox("Writable directory structure");
        JCheckBox chkCreateNative = new JCheckBox("Create native directory if it does not exist");

        // Thêm combobox theo đúng cấu trúc bạn dùng
        addO(opt, combo, 1, oy++, 1);

        // Thêm 3 checkbox theo đúng cấu trúc bạn dùng
        addO(opt, chkApplySub, 0, oy++, 2);
        addO(opt, chkWritable, 0, oy++, 2);
        addO(opt, chkCreateNative, 0, oy++, 2);

        // ====== Logic xử lý Disabled ======
        combo.addActionListener(e -> {
            boolean disabled = "Disabled".equals(combo.getSelectedItem());

            chkApplySub.setEnabled(!disabled);
            chkWritable.setEnabled(!disabled);

            // Checkbox này luôn bật
            chkCreateNative.setEnabled(true);
        });

        // ====== Logic xử lý Disabled ======
        combo.addActionListener(e -> {
            boolean disabled = "Disabled".equals(combo.getSelectedItem());

            chkApplySub.setEnabled(!disabled);
            chkWritable.setEnabled(!disabled);

            // Checkbox này luôn bật
            chkCreateNative.setEnabled(true);
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
        // ----------- Logic Add Row -----------
        addMount.addActionListener(e -> {
            int row = model.getRowCount();
            model.addRow(new Object[] { "", "" }); // thêm dòng trống
            table.setRowSelectionInterval(row, row); // chọn dòng mới
            table.editCellAt(row, 0); // kích hoạt ô đầu tiên
            table.requestFocus(); // focus để nhập ngay
        });

        // ----------- Logic Remove Row -----------
        removeMount.addActionListener(e -> {
            int sel = table.getSelectedRow();
            if (sel != -1) {
                model.removeRow(sel);
            }
        });

        // Store password field for getter
        panel.putClientProperty("txtPassword", txtPassword);

        return panel;
    }

    // =====================================================================
    // SAVE USER SETTINGS
    // =====================================================================
    private void saveUserSettings(DefaultListModel<String> users, JPanel generalTab) {
        System.out.println("=== Users ===");
        for (int i = 0; i < users.size(); i++) {
            System.out.println(users.get(i));
        }

        JTextField txtPassword = (JTextField) generalTab.getClientProperty("txtPassword");
        if (txtPassword != null) {
            System.out.println("Password: " + txtPassword.getText());
        }
    }

    // =====================================================================
    // LAYOUT HELPERS
    // =====================================================================
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

    // ================= GETTERS =================

    public JList<String> getUserList() {
        JPanel usersPanel = (JPanel) rightPanel.getComponent(rightPanel.getComponentCount() - 1);
        return (JList<String>) usersPanel.getClientProperty("userList");
    }

    public JButton getBtnAddUser() {
        JPanel usersPanel = (JPanel) rightPanel.getComponent(rightPanel.getComponentCount() - 1);
        return (JButton) usersPanel.getClientProperty("btnAdd");
    }

    public JButton getBtnRemoveUser() {
        JPanel usersPanel = (JPanel) rightPanel.getComponent(rightPanel.getComponentCount() - 1);
        return (JButton) usersPanel.getClientProperty("btnRemove");
    }

    public JButton getBtnDuplicateUser() {
        JPanel usersPanel = (JPanel) rightPanel.getComponent(rightPanel.getComponentCount() - 1);
        return (JButton) usersPanel.getClientProperty("btnDuplicate");
    }

    public JButton getBtnRenameUser() {
        JPanel usersPanel = (JPanel) rightPanel.getComponent(rightPanel.getComponentCount() - 1);
        return (JButton) usersPanel.getClientProperty("btnRename");
    }

    public JButton getBtnApply() {
        JPanel usersPanel = (JPanel) rightPanel.getComponent(rightPanel.getComponentCount() - 1);
        return (JButton) usersPanel.getClientProperty("btnApply");
    }

    public JButton getBtnOk() {
        JPanel usersPanel = (JPanel) rightPanel.getComponent(rightPanel.getComponentCount() - 1);
        return (JButton) usersPanel.getClientProperty("btnOk");
    }

    public JTextField getTxtPassword() {
        JPanel usersPanel = (JPanel) rightPanel.getComponent(rightPanel.getComponentCount() - 1);
        JPanel generalTab = (JPanel) usersPanel.getClientProperty("generalTab");
        return (JTextField) generalTab.getClientProperty("txtPassword");
    }

}
