package View;

import javax.swing.*;
import java.awt.*;
import javax.swing.table.DefaultTableModel;
import Controller.ServerController;

public class ServerGUI extends JFrame {
	private JTable table;
	private JTextArea txtLog;
	private ServerController controller;
	private DefaultTableModel tableModel;

	public void setController(ServerController controller) {
		this.controller = controller;
	}

	public ServerGUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(800, 600);
		setTitle("FTP Server");
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());

		// ===== MENU BAR =====
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu menuServer = new JMenu("Server");
		menuBar.add(menuServer);

		JMenuItem itemStart = new JMenuItem("Start");
		JMenuItem itemStop = new JMenuItem("Stop");
		JMenuItem itemQuit = new JMenuItem("Quit");

		menuServer.add(itemStart);
		menuServer.add(itemStop);
		menuServer.addSeparator();
		menuServer.add(itemQuit);

		JMenu menuView = new JMenu("View");
		menuBar.add(menuView);

		JMenuItem itemClearLog = new JMenuItem("Clear Log");
		menuView.add(itemClearLog);

		JMenu menuHelp = new JMenu("Help");
		menuBar.add(menuHelp);

		JMenuItem itemAbout = new JMenuItem("About");
		menuHelp.add(itemAbout);

		// ===== MAIN SPLIT =====
		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		splitPane.setResizeWeight(0.7);
		add(splitPane, BorderLayout.CENTER);

		// ===== LOG AREA =====
		txtLog = new JTextArea();
		txtLog.setFont(new Font("Consolas", Font.PLAIN, 14));
		txtLog.setEditable(false);
		JScrollPane scrollLog = new JScrollPane(txtLog);
		splitPane.setTopComponent(scrollLog);

		// ===== TABLE =====
		tableModel = new DefaultTableModel(
				new Object[][] {},
				new String[] { "Date/Time", "Session ID", "Host", "User", "Status" });
		table = new JTable(tableModel);
		JScrollPane scrollTable = new JScrollPane(table);
		splitPane.setBottomComponent(scrollTable);

		// ====== ACTIONS ======
		itemStart.addActionListener(e -> controller.startServer());
		itemStop.addActionListener(e -> controller.stopServer());
		itemQuit.addActionListener(e -> System.exit(0));
		itemClearLog.addActionListener(e -> txtLog.setText(""));
		itemAbout.addActionListener(e -> JOptionPane.showMessageDialog(this,
				"Simple FTP Server\nBy: Your Team", "About", JOptionPane.INFORMATION_MESSAGE));

		setVisible(true);
	}

	// ==================== PUBLIC METHODS =====================
	public void appendLog(String msg) {
		SwingUtilities.invokeLater(() -> {
			txtLog.append(msg + "\n");
			txtLog.setCaretPosition(txtLog.getDocument().getLength());
		});
	}

	public void addClientSession(String date, int sessionId, String host, String user, String status) {
		SwingUtilities.invokeLater(() -> {
			tableModel.addRow(new Object[] { date, sessionId, host, user, status });
		});
	}

	public void updateClientStatus(int sessionId, String newStatus) {
		SwingUtilities.invokeLater(() -> {
			for (int i = 0; i < tableModel.getRowCount(); i++) {
				if ((int) tableModel.getValueAt(i, 1) == sessionId) {
					tableModel.setValueAt(newStatus, i, 4);
					break;
				}
			}
		});
	}

	public void updateClientUser(int sessionId, String username) {
		SwingUtilities.invokeLater(() -> {
			for (int i = 0; i < tableModel.getRowCount(); i++) {
				int id = (int) tableModel.getValueAt(i, 1); // Session ID là cột 1
				if (id == sessionId) {
					tableModel.setValueAt(username, i, 3); // ✅ ĐÚNG: cột 3 là "User"
					break;
				}
			}
		});
	}
}
