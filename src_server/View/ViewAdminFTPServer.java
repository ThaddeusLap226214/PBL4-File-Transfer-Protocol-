package View;

import FTPController.FTPController;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ViewAdminFTPServer extends JFrame {

    private FTPController controller;
    private ViewAdminConfigure viewConfig;

    private JTable logTable;
    private JTable sessionTable;

    private DefaultTableModel logModel;
    private DefaultTableModel sessionModel;

    private JLabel statusBar;

    // CONSTRUCTOR - NHẬN viewConfig TỪ FTPrun
    public ViewAdminFTPServer(ViewAdminConfigure viewConfig) {

        this.viewConfig = viewConfig;

        setTitle("ServerFTP - Administration Interface");
        setMinimumSize(new Dimension(1200, 700));
        setSize(1400, 1000);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        // MENU BAR
        JMenuBar menuBar = new JMenuBar();

        JMenu menuServer = new JMenu("Server");

        JMenuItem itemConfigure = new JMenuItem("Configure");
        JMenuItem itemStart = new JMenuItem("Start");
        JMenuItem itemStop = new JMenuItem("Stop");
        JMenuItem itemQuit = new JMenuItem("Quit");

        // ---- MỞ CONFIG (KHÔNG TẠO MỚI) ----
        itemConfigure.addActionListener(e -> viewConfig.setVisible(true));

        // ---- THOÁT ----
        itemQuit.addActionListener(e -> System.exit(0));

        menuServer.add(itemConfigure);
        menuServer.add(itemStart);
        menuServer.add(itemStop);
        menuServer.add(itemQuit);
        menuBar.add(menuServer);

        JMenu menuView = new JMenu("View");
        menuView.add(new JMenuItem("Show User List"));
        menuView.add(new JMenuItem("Show Log Window"));
        menuView.add(new JMenuItem("Clear Log"));
        menuBar.add(menuView);

        JMenu menuHelp = new JMenu("Help");
        menuHelp.add(new JMenuItem("Documentation"));
        menuHelp.add(new JMenuItem("About"));
        menuBar.add(menuHelp);

        setJMenuBar(menuBar);
        // LOG TABLE
        String[] logColumns = { "Date/Time", "Info", "Type", "Message" };
        logModel = new DefaultTableModel(logColumns, 0);
        logTable = new JTable(logModel);

        JScrollPane scrollLog = new JScrollPane(logTable);
        // SESSION TABLE
        String[] sessionColumns = { "Date/Time", "Session", "Protocol", "Host", "User", "Transfer" };
        sessionModel = new DefaultTableModel(sessionColumns, 0);
        sessionTable = new JTable(sessionModel);

        JScrollPane scrollSession = new JScrollPane(sessionTable);
        // SPLIT PANE
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, scrollLog, scrollSession);
        splitPane.setResizeWeight(0.70);
        splitPane.setDividerLocation(350);

        add(splitPane, BorderLayout.CENTER);
        // STATUS BAR
        statusBar = new JLabel("Server ready");
        statusBar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        add(statusBar, BorderLayout.SOUTH);

        setVisible(true);
    }

    // GÁN CONTROLLER
    public void setController(FTPController controller) {
        this.controller = controller;
    }

    // LOG
    public void appendLog(int session, String username, String clientAddress, String msg) {
        logModel.addRow(new Object[] {
                java.time.LocalDateTime.now().toString(),
                username,
                "Status",
                msg
        });
    }

    // SESSION
    public void appendSession(int sessionID, String protocol, String host, String user, String transfer) {
        sessionModel.addRow(new Object[] {
                java.time.LocalDateTime.now().toString(),
                sessionID,
                protocol,
                host,
                user,
                transfer
        });
    }
}
