package View;

import javax.swing.JFrame;
import java.awt.GridLayout;
import javax.swing.JSplitPane;
import java.awt.Dimension;
import javax.swing.JScrollPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JMenuBar;
import javax.swing.JTextArea;
import javax.swing.JTable;
import java.awt.Font;
import javax.swing.JMenu;
import javax.swing.table.DefaultTableModel;

import Controller.ServerController;

import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ServerGUI extends JFrame {
	private JTable table;
	private ServerController controller;
	
	public void setController(ServerController controller) {
		this.controller = controller;
	}
	
	public ServerGUI() {
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(new Dimension(700, 1400));
		setTitle("ServerFTP");
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu menuServer = new JMenu("Server");
		menuBar.add(menuServer);
		
		JMenuItem ItemConfigure = new JMenuItem("Configure...");
		ItemConfigure.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new ServerConfigureGUI();
			}
		});
		menuServer.add(ItemConfigure);
		
		JMenuItem ItemStart = new JMenuItem("Start");
		menuServer.add(ItemStart);
		
		JMenuItem ItemStop = new JMenuItem("Stop");
		menuServer.add(ItemStop);
		
		JMenuItem ItemQuit = new JMenuItem("Quit");
		menuServer.add(ItemQuit);
		
		JMenu menuView = new JMenu("View");
		menuBar.add(menuView);
		
		JMenuItem ItemShowUserList = new JMenuItem("Show User List");
		menuView.add(ItemShowUserList);
		
		JMenuItem ItemShowLogWindow = new JMenuItem("Show Log Window");
		menuView.add(ItemShowLogWindow);
		
		JMenuItem ItemClearLog = new JMenuItem("Clear Log");
		menuView.add(ItemClearLog);
		
		JMenu menuHelp = new JMenu("Help");
		menuBar.add(menuHelp);
		
		JMenuItem ItemDocumentation = new JMenuItem("Documentation");
		menuHelp.add(ItemDocumentation);
		
		JMenuItem ItemAbout = new JMenuItem("About");
		menuHelp.add(ItemAbout);
		getContentPane().setLayout(new GridLayout(1, 0, 0, 0));
		
		JSplitPane splitPaneChiaTextAreaVaTable = new JSplitPane();
		splitPaneChiaTextAreaVaTable.setResizeWeight(0.65);
		splitPaneChiaTextAreaVaTable.setOrientation(JSplitPane.VERTICAL_SPLIT);
		getContentPane().add(splitPaneChiaTextAreaVaTable);
		
		JScrollPane scrollPaneChuaTxtArea = new JScrollPane();
		splitPaneChiaTextAreaVaTable.setLeftComponent(scrollPaneChuaTxtArea);
		
		JPanel panelChuaTxtArea = new JPanel();
		scrollPaneChuaTxtArea.setViewportView(panelChuaTxtArea);
		panelChuaTxtArea.setLayout(new BorderLayout(0, 0));
		
		JTextArea textArea = new JTextArea();
		panelChuaTxtArea.add(textArea, BorderLayout.CENTER);
		
		JScrollPane scrollPaneChuaTable = new JScrollPane();
		scrollPaneChuaTable.setPreferredSize(new Dimension(0, 65));
		scrollPaneChuaTable.setFont(new Font("Times New Roman", Font.PLAIN, 13));
		splitPaneChiaTextAreaVaTable.setRightComponent(scrollPaneChuaTable);
		
		JPanel panelChuaTable = new JPanel();
		panelChuaTable.setPreferredSize(new Dimension(0, 0));
		scrollPaneChuaTable.setViewportView(panelChuaTable);
		panelChuaTable.setLayout(new BorderLayout(0, 0));
		
		table = new JTable();
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		table.setModel(new DefaultTableModel(
			new Object[][] {
				{null, null, null, null, null, null},
				{null, null, null, null, null, null},
			},
			new String[] {
				"Date/Time", "Session ID", "Protocols", "Host", "User", "Transfer"
			}
		) {
			Class[] columnTypes = new Class[] {
				String.class, Integer.class, String.class, String.class, String.class, String.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
		});
		table.getColumnModel().getColumn(0).setPreferredWidth(107);
		table.getColumnModel().getColumn(0).setMinWidth(85);
		table.getColumnModel().getColumn(1).setPreferredWidth(65);
		table.getColumnModel().getColumn(1).setMinWidth(65);
		table.getColumnModel().getColumn(2).setMinWidth(65);
		table.getColumnModel().getColumn(3).setPreferredWidth(82);
		table.getColumnModel().getColumn(3).setMinWidth(65);
		table.getColumnModel().getColumn(4).setPreferredWidth(65);
		table.getColumnModel().getColumn(4).setMinWidth(65);
		table.getColumnModel().getColumn(5).setPreferredWidth(5000);
		table.setFont(new Font("Times New Roman", Font.PLAIN, 13));
		JScrollPane ScrollPaneChuaTableHienTenCot = new JScrollPane(table);
		panelChuaTable.add(ScrollPaneChuaTableHienTenCot, BorderLayout.CENTER);
		splitPaneChiaTextAreaVaTable.setDividerLocation(0.65);
		setVisible(true);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//ServerGUI sg = new ServerGUI();
	}
}
