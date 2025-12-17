package View;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import FTPControl.FTPControl;

import java.awt.*;
import java.net.InetAddress;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ViewAdminFTPServer extends JFrame {

    private FTPControl controller;
    private ViewAdminConfigure viewConfig; // <-- lấy từ FTPrun

    private JTable logTable;
    private JTable sessionTable;

    private DefaultTableModel logModel;
    private DefaultTableModel sessionModel;

    private JLabel statusBar;

    // ---------------------------------------------------------
    // CONSTRUCTOR - NHẬN viewConfig TỪ FTPrun
    // ---------------------------------------------------------
    public ViewAdminFTPServer(ViewAdminConfigure viewConfig) {

        this.viewConfig = viewConfig; // <-- dùng đúng cửa sổ config được tạo sẵn

        setTitle("ServerFTP - Administration Interface");
        setMinimumSize(new Dimension(1200, 700));
        setSize(1400, 1000);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // ----------------------------------------
        // MENU BAR
        // ----------------------------------------
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
        
        // StartServer
//        itemStart.addActionListener(e -> controller.StartServerFTP());

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

        // ----------------------------------------
        // LOG TABLE
        // ----------------------------------------        
        String[] logColumns = { "Date/Time", "Info", "Type", "Message" };
        logModel = new DefaultTableModel(logColumns, 0);
        logTable = new JTable(logModel);
        
        logTable.setBackground(Color.WHITE);
        logTable.setGridColor(Color.LIGHT_GRAY);
        logTable.setFillsViewportHeight(true);

        JScrollPane scrollLog = new JScrollPane(logTable);
        scrollLog.getViewport().setBackground(Color.WHITE);

        // ----------------------------------------
        // SESSION TABLE
        // ----------------------------------------
        String[] sessionColumns = { "Date/Time", "Session", "Host", "User"};
        sessionModel = new DefaultTableModel(sessionColumns, 0);
        sessionTable = new JTable(sessionModel);

        sessionTable.setBackground(Color.WHITE);
        sessionTable.setGridColor(Color.LIGHT_GRAY);
        sessionTable.setFillsViewportHeight(true);
        
        
        JScrollPane scrollSession = new JScrollPane(sessionTable);
        scrollSession.getViewport().setBackground(Color.WHITE);

        // ----------------------------------------
        // SPLIT PANE
        // ----------------------------------------
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, scrollLog, scrollSession);
        splitPane.setResizeWeight(0.70);
        splitPane.setDividerLocation(350);

        add(splitPane, BorderLayout.CENTER);

        // ----------------------------------------
        // STATUS BAR
        // ----------------------------------------
        statusBar = new JLabel("Server ready");
        statusBar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        add(statusBar, BorderLayout.SOUTH);

        setVisible(true);
    }

    // ---------------------------------------------------
    // GÁN CONTROLLER
    // ---------------------------------------------------
    public void setController(FTPControl controller) {
        this.controller = controller;
    }

    // ---------------------------------------------------
    // LOG
    // ---------------------------------------------------
    public void appendLog(LocalDateTime time, String Info, String type, String msg) {
        logModel.addRow(new Object[] {
                getTimeformat(time),
                Info,
                type,
                msg
        });
    }

    // ---------------------------------------------------
    // SESSION
    // ---------------------------------------------------
    public void appendSession(LocalDateTime time, int ssid, InetAddress IP, String Username) {
        sessionModel.addRow(new Object[] {
                getTimeformat(time),
                ssid,
                IP.getHostAddress(),
                Username
        });
    }
    
    public String getTimeformat(LocalDateTime time) {
	    	DateTimeFormatter formatter =
	    	        DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
	
	    	String formatted = time.format(formatter);
	    	return formatted;
    }
}