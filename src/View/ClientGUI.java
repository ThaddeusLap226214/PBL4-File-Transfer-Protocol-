package View;

import javax.swing.JFrame;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JMenuBar;
import java.awt.BorderLayout;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollBar;

import java.awt.GridLayout;
import javax.swing.JSplitPane;
import java.awt.FlowLayout;
import javax.swing.JTextArea;
import javax.swing.JTree;

import Utils.FileTreePanel;
import javax.swing.JScrollPane;

public class ClientGUI extends JFrame{
	private JTextField txtFHost;
	private JTextField txtFUser;
	private JTextField txtFPass;
	private JTextField txtFPort;
	private JTextField textField;
	private JTextField textField_1;
	public ClientGUI() {
		getContentPane().setLayout(new GridLayout(1, 0, 0, 0));
		
		JSplitPane splitPane = new JSplitPane();
		splitPane.setDividerSize(3);
		splitPane.setResizeWeight(0.5);
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		getContentPane().add(splitPane);
		
		JPanel panelLoginAndCMD = new JPanel();
		splitPane.setLeftComponent(panelLoginAndCMD);
		panelLoginAndCMD.setLayout(new GridLayout(1, 0, 0, 0));
		
		JSplitPane splitPane_1 = new JSplitPane();
		splitPane_1.setResizeWeight(0.2);
		splitPane_1.setOrientation(JSplitPane.VERTICAL_SPLIT);
		panelLoginAndCMD.add(splitPane_1);
		
		JPanel panelLogin = new JPanel();
		splitPane_1.setLeftComponent(panelLogin);
		panelLogin.setLayout(null);
		
		JLabel lbHost = new JLabel("Host:");
		lbHost.setFont(new Font("Times New Roman", Font.PLAIN, 13));
		lbHost.setName("");
		lbHost.setBounds(10, 25, 45, 13);
		panelLogin.add(lbHost);
		
		txtFHost = new JTextField();
		txtFHost.setFont(new Font("Times New Roman", Font.PLAIN, 13));
		txtFHost.setBounds(44, 21, 96, 19);
		panelLogin.add(txtFHost);
		txtFHost.setColumns(10);
		
		JLabel lbUserName = new JLabel("User:");
		lbUserName.setFont(new Font("Times New Roman", Font.PLAIN, 13));
		lbUserName.setBounds(167, 25, 45, 13);
		panelLogin.add(lbUserName);
		
		txtFUser = new JTextField();
		txtFUser.setFont(new Font("Times New Roman", Font.PLAIN, 13));
		txtFUser.setBounds(205, 21, 96, 19);
		panelLogin.add(txtFUser);
		txtFUser.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("Pass:");
		lblNewLabel.setFont(new Font("Times New Roman", Font.PLAIN, 13));
		lblNewLabel.setBounds(330, 25, 45, 13);
		panelLogin.add(lblNewLabel);
		
		txtFPass = new JTextField();
		txtFPass.setBounds(365, 22, 96, 19);
		panelLogin.add(txtFPass);
		txtFPass.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("Port:");
		lblNewLabel_1.setFont(new Font("Times New Roman", Font.PLAIN, 13));
		lblNewLabel_1.setBounds(497, 25, 45, 13);
		panelLogin.add(lblNewLabel_1);
		
		txtFPort = new JTextField();
		txtFPort.setBounds(529, 22, 96, 19);
		panelLogin.add(txtFPort);
		txtFPort.setColumns(10);
		
		JButton butConnect = new JButton("Connect");
		butConnect.setBounds(670, 21, 96, 21);
		panelLogin.add(butConnect);
		
		JPanel panelCMD = new JPanel();
		splitPane_1.setRightComponent(panelCMD);
		panelCMD.setLayout(new GridLayout(1, 0, 0, 0));
		
		JScrollPane scrollPaneCMD = new JScrollPane();
		panelCMD.add(scrollPaneCMD);
		
		JPanel panelCMDdirect = new JPanel();
		scrollPaneCMD.setViewportView(panelCMDdirect);
		panelCMDdirect.setLayout(new GridLayout(1, 0, 0, 0));
		
		JTextArea textArea = new JTextArea();
		textArea.setFont(new Font("Times New Roman", Font.PLAIN, 13));
		panelCMDdirect.add(textArea);
		splitPane_1.setDividerLocation(0.2);
		
		JPanel panelFolderTree = new JPanel();
		splitPane.setRightComponent(panelFolderTree);
		panelFolderTree.setLayout(new GridLayout(1, 0, 0, 0));
		
		JSplitPane splitPane_2 = new JSplitPane();
		splitPane_2.setDividerSize(2);
		splitPane_2.setResizeWeight(0.5);
		panelFolderTree.add(splitPane_2);
		
		JPanel panelLocalFolder = new JPanel();
		splitPane_2.setLeftComponent(panelLocalFolder);
		panelLocalFolder.setLayout(new GridLayout(1, 0, 0, 0));
		
		JSplitPane splitPane_3 = new JSplitPane();
		splitPane_3.setDividerSize(2);
		splitPane_3.setResizeWeight(0.5);
		splitPane_3.setOrientation(JSplitPane.VERTICAL_SPLIT);
		panelLocalFolder.add(splitPane_3);
		
		JPanel panelLocalFolderTop = new JPanel();
		splitPane_3.setLeftComponent(panelLocalFolderTop);
		panelLocalFolderTop.setLayout(new GridLayout(1, 0, 0, 0));
		
		JSplitPane splitPane_5 = new JSplitPane();
		splitPane_5.setDividerSize(2);
		splitPane_5.setOrientation(JSplitPane.VERTICAL_SPLIT);
		panelLocalFolderTop.add(splitPane_5);
		
		JSplitPane splitPane_6 = new JSplitPane();
		splitPane_6.setDividerSize(2);
		splitPane_5.setLeftComponent(splitPane_6);
		
		JPanel panel = new JPanel();
		splitPane_6.setLeftComponent(panel);
		panel.setLayout(new GridLayout(1, 0, 0, 0));
		
		JLabel lblNewLabel_2 = new JLabel("  Local site:");
		lblNewLabel_2.setFont(new Font("Times New Roman", Font.PLAIN, 13));
		panel.add(lblNewLabel_2);
		
		JPanel panel_1 = new JPanel();
		splitPane_6.setRightComponent(panel_1);
		panel_1.setLayout(new GridLayout(1, 0, 0, 0));
		
		textField = new JTextField();
		panel_1.add(textField);
		textField.setColumns(10);
		splitPane_6.setDividerLocation(72);
		
		JScrollPane scrollPane = new JScrollPane();
		splitPane_5.setRightComponent(scrollPane);
		
		JPanel panel_2 = new JPanel();
		scrollPane.setViewportView(panel_2);
		panel_2.setLayout(new GridLayout(1, 0, 0, 0));
		
		FileTreePanel UTree = new FileTreePanel("D:\\");
		JTree tree = UTree.tree();
		panel_2.add(tree);
		splitPane_5.setDividerLocation(25);
		
		//FileTreePanel localTree = new FileTreePanel("D:\\");
		
		JPanel localSite2 = new JPanel();
		splitPane_3.setRightComponent(localSite2);
		splitPane_3.setDividerLocation(0.5);
		
		JPanel panelRemoteFolder = new JPanel();
		splitPane_2.setRightComponent(panelRemoteFolder);
		panelRemoteFolder.setLayout(new GridLayout(1, 0, 0, 0));
		
		JSplitPane splitPane_4 = new JSplitPane();
		splitPane_4.setDividerSize(2);
		splitPane_4.setResizeWeight(0.5);
		splitPane_4.setOrientation(JSplitPane.VERTICAL_SPLIT);
		panelRemoteFolder.add(splitPane_4);
		
		JPanel panel_8 = new JPanel();
		splitPane_4.setLeftComponent(panel_8);
		panel_8.setLayout(new GridLayout(1, 0, 0, 0));
		
		JSplitPane splitPane_7 = new JSplitPane();
		splitPane_7.setDividerSize(2);
		splitPane_7.setOrientation(JSplitPane.VERTICAL_SPLIT);
		panel_8.add(splitPane_7);
		
		JSplitPane splitPane_8 = new JSplitPane();
		splitPane_8.setDividerSize(2);
		splitPane_7.setLeftComponent(splitPane_8);
		
		JPanel panel_3 = new JPanel();
		splitPane_8.setLeftComponent(panel_3);
		panel_3.setLayout(new GridLayout(1, 0, 0, 0));
		
		JLabel lblNewLabel_3 = new JLabel("  Remote Site:");
		lblNewLabel_3.setFont(new Font("Times New Roman", Font.PLAIN, 13));
		panel_3.add(lblNewLabel_3);
		
		JPanel panel_4 = new JPanel();
		splitPane_8.setRightComponent(panel_4);
		panel_4.setLayout(new GridLayout(1, 0, 0, 0));
		
		textField_1 = new JTextField();
		panel_4.add(textField_1);
		textField_1.setColumns(10);
		splitPane_8.setDividerLocation(72);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		splitPane_7.setRightComponent(scrollPane_1);
		
		JPanel panel_5 = new JPanel();
		scrollPane_1.setViewportView(panel_5);
		panel_5.setLayout(new GridLayout(1, 0, 0, 0));
		
		JTree tree_1 = new JTree();
		panel_5.add(tree_1);
		splitPane_7.setDividerLocation(25);
		
		JPanel panel_9 = new JPanel();
		splitPane_4.setRightComponent(panel_9);
		splitPane_4.setDividerLocation(0.5);
		splitPane_2.setDividerLocation(0.5);
		
		setTitle("CLientFTP");
		setSize(new Dimension(1800, 1440));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ClientGUI fr = new ClientGUI();
	}
}
