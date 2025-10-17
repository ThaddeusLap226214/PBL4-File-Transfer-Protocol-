package View;

import javax.swing.JFrame;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JPanel;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;

import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.KeyStroke;

import Controller.ClientController;
import Utils.FileTreePanel;
import Utils.txtAreaConsole;

import javax.swing.JScrollPane;

public class ClientGUI extends JFrame{
	private ClientController controller;
	
	private JTextField txtFHost;
	private JTextField txtFUser;
	private JTextField txtFPass;
	private JTextField txtFPort;
	private JTextField txtFLocalPath;
	private JTextField textField_1;
	
	public void setController(ClientController controller) {
		this.controller = controller;
	}
	
	public ClientGUI() {
		//những dòng setLayout GridLayout(1,0) là sẽ đặt 1 loại panel (JSplitPane) phủ toàn bộ frame đang setLayout
		getContentPane().setLayout(new GridLayout(1, 0, 0, 0));
		
		//JSplitPane Chia Frame thành 2 phần theo tỉ lệ ban đầu và có thể thay đổi tùy ý
		//Phần Login và CMD ở trên, Thư mục của máy chủ và khách ở dưới
		//DividerSize là độ rộng viền, ResizeWeight là sự thay đổi 2 phần khi phóng thu cửa sổ
		JSplitPane splitPaneChia2GiaoDien = new JSplitPane();
		splitPaneChia2GiaoDien.setDividerSize(3);
		splitPaneChia2GiaoDien.setResizeWeight(0.5);
		splitPaneChia2GiaoDien.setOrientation(JSplitPane.VERTICAL_SPLIT);
		getContentPane().add(splitPaneChia2GiaoDien);
		
		JPanel panelLoginAndCMD = new JPanel();
		splitPaneChia2GiaoDien.setLeftComponent(panelLoginAndCMD);
		panelLoginAndCMD.setLayout(new GridLayout(1, 0, 0, 0));
		
		//Phần Login và CMD chia làm 2 phần tiếp Login và CMD (2 panel)
		JSplitPane splitPaneChiaLoginVaCMD = new JSplitPane();
		splitPaneChiaLoginVaCMD.setResizeWeight(0.2);
		splitPaneChiaLoginVaCMD.setOrientation(JSplitPane.VERTICAL_SPLIT);
		panelLoginAndCMD.add(splitPaneChiaLoginVaCMD);
		
		//Panel Login và các thành phần
		JPanel panelLogin = new JPanel();
		splitPaneChiaLoginVaCMD.setLeftComponent(panelLogin);
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
		
		//Panel CMD, Bọc bằng Jscroll Pane để cuộn và JTextArea
		JPanel panelCMD = new JPanel();
		splitPaneChiaLoginVaCMD.setRightComponent(panelCMD);
		panelCMD.setLayout(new GridLayout(1, 0, 0, 0));
		
		JScrollPane scrollPaneCMD = new JScrollPane();
		panelCMD.add(scrollPaneCMD);
		
		JPanel panelCMDdirect = new JPanel();
		scrollPaneCMD.setViewportView(panelCMDdirect);
		panelCMDdirect.setLayout(new GridLayout(1, 0, 0, 0));
		
		//Khởi tạo txtConsole kiểu JTextArea từ lớp txtAreaConsole trong Utils
		txtAreaConsole tac = new txtAreaConsole();
		//tac.setConsoleListener(cmd -> controller.checkCMD(cmd));
		tac.setConsoleListener(new txtAreaConsole.ConsoleListener() {		
			@Override
			public void onCommandEntered(String command) {
				// TODO Auto-generated method stub
				controller.checkCMD(command);
			}
		});
		JTextArea txtConsole = tac.getConsole();
		panelCMDdirect.add(txtConsole);
		splitPaneChiaLoginVaCMD.setDividerLocation(0.2);
		
		//Quay lại với toàn Frame lúc đầu Login và CMD ở trên còn 
		//PHẦN DƯỚI LÀ HIỂN THỊ THƯ MỤC MÁY CHỦ VÀ MÁY KHÁCH
		JPanel panelFolderTreeLocalAndRemote = new JPanel();
		splitPaneChia2GiaoDien.setRightComponent(panelFolderTreeLocalAndRemote);
		panelFolderTreeLocalAndRemote.setLayout(new GridLayout(1, 0, 0, 0));
		
		//Chia phần dưới làm trái cho local và phải cho Remote
		JSplitPane splitPaneChiaLocalVaRemoteFolder = new JSplitPane();
		splitPaneChiaLocalVaRemoteFolder.setDividerSize(2);
		splitPaneChiaLocalVaRemoteFolder.setResizeWeight(0.5);
		panelFolderTreeLocalAndRemote.add(splitPaneChiaLocalVaRemoteFolder);
		
		//Phần trái: Local Site (Path and Tree Folder)
		JPanel panelLocalFolder = new JPanel();
		splitPaneChiaLocalVaRemoteFolder.setLeftComponent(panelLocalFolder);
		panelLocalFolder.setLayout(new GridLayout(1, 0, 0, 0));
		
		//Chia phần trái thành trên để hiện thư mục còn phần dưới để sau phát triển thêm
		JSplitPane splitPaneChiaLocalFolderTrenVaDuoi = new JSplitPane();
		splitPaneChiaLocalFolderTrenVaDuoi.setDividerSize(2);
		splitPaneChiaLocalFolderTrenVaDuoi.setResizeWeight(0.5);
		splitPaneChiaLocalFolderTrenVaDuoi.setOrientation(JSplitPane.VERTICAL_SPLIT);
		panelLocalFolder.add(splitPaneChiaLocalFolderTrenVaDuoi);
		
		JPanel panelLocalFolderTop = new JPanel();
		splitPaneChiaLocalFolderTrenVaDuoi.setLeftComponent(panelLocalFolderTop);
		panelLocalFolderTop.setLayout(new GridLayout(1, 0, 0, 0));
		
		//Gồm 2 phần một để hiện đường dẫn và một để hiện cây thư mục
		JSplitPane splitPaneChiaLocalFolderTren = new JSplitPane();
		splitPaneChiaLocalFolderTren.setDividerSize(2);
		splitPaneChiaLocalFolderTren.setOrientation(JSplitPane.VERTICAL_SPLIT);
		panelLocalFolderTop.add(splitPaneChiaLocalFolderTren);
		
		//Phần để hiện đường dẫn
		JSplitPane splitPaneChiaLocalSiteVaPath = new JSplitPane();
		splitPaneChiaLocalSiteVaPath.setDividerSize(2);
		splitPaneChiaLocalFolderTren.setLeftComponent(splitPaneChiaLocalSiteVaPath);
		
		JPanel panelLocalSite = new JPanel();
		splitPaneChiaLocalSiteVaPath.setLeftComponent(panelLocalSite);
		panelLocalSite.setLayout(new GridLayout(1, 0, 0, 0));
		
		JLabel lblNewLabel_2 = new JLabel("  Local site:");
		lblNewLabel_2.setFont(new Font("Times New Roman", Font.PLAIN, 13));
		panelLocalSite.add(lblNewLabel_2);
		
		JPanel panelLocalPath = new JPanel();
		splitPaneChiaLocalSiteVaPath.setRightComponent(panelLocalPath);
		panelLocalPath.setLayout(new GridLayout(1, 0, 0, 0));
		
		txtFLocalPath = new JTextField();
		panelLocalPath.add(txtFLocalPath);
		txtFLocalPath.setColumns(10);
		splitPaneChiaLocalSiteVaPath.setDividerLocation(72);
		
		//Phần để hiện cây thư mục
		JScrollPane scrollPane = new JScrollPane();
		splitPaneChiaLocalFolderTren.setRightComponent(scrollPane);
		
		//Khởi tạo và set Cây thư mục lấy từ lớp panelLocalTree tạo bên Utils
		JPanel panelLocalTree = new JPanel();
		scrollPane.setViewportView(panelLocalTree);
		panelLocalTree.setLayout(new GridLayout(1, 0, 0, 0));
		
		FileTreePanel UTree = new FileTreePanel("D:\\");
		JTree tree = UTree.tree();
		panelLocalTree.add(tree);
		splitPaneChiaLocalFolderTren.setDividerLocation(25);
				
		JPanel panelLocalFolderBottom = new JPanel();
		splitPaneChiaLocalFolderTrenVaDuoi.setRightComponent(panelLocalFolderBottom);
		splitPaneChiaLocalFolderTrenVaDuoi.setDividerLocation(0.5);
		
		//Phần phải: Remote Site (Path và FolderTree)
		JPanel panelRemoteFolder = new JPanel();
		splitPaneChiaLocalVaRemoteFolder.setRightComponent(panelRemoteFolder);
		panelRemoteFolder.setLayout(new GridLayout(1, 0, 0, 0));
		
		//Chia thành 2 phần, phần trên để hiện đường dẫn và cây thư mục, phần dưới để phát triển thêm
		JSplitPane splitPaneChỉaemoteFolderTrenVaDuoi = new JSplitPane();
		splitPaneChỉaemoteFolderTrenVaDuoi.setDividerSize(2);
		splitPaneChỉaemoteFolderTrenVaDuoi.setResizeWeight(0.5);
		splitPaneChỉaemoteFolderTrenVaDuoi.setOrientation(JSplitPane.VERTICAL_SPLIT);
		panelRemoteFolder.add(splitPaneChỉaemoteFolderTrenVaDuoi);
		
		//Phần đường dẫn và cây thư mục
		JPanel panelRemoteFolderTop = new JPanel();
		splitPaneChỉaemoteFolderTrenVaDuoi.setLeftComponent(panelRemoteFolderTop);
		panelRemoteFolderTop.setLayout(new GridLayout(1, 0, 0, 0));
		
		//Chia thành 2 phần
		JSplitPane splitPaneChiaRemoteFolderTren = new JSplitPane();
		splitPaneChiaRemoteFolderTren.setDividerSize(2);
		splitPaneChiaRemoteFolderTren.setOrientation(JSplitPane.VERTICAL_SPLIT);
		panelRemoteFolderTop.add(splitPaneChiaRemoteFolderTren);
		
		//Phần đường dẫn
		JSplitPane splitPaneChiaRemoteSiteVaPath = new JSplitPane();
		splitPaneChiaRemoteSiteVaPath.setDividerSize(2);
		splitPaneChiaRemoteFolderTren.setLeftComponent(splitPaneChiaRemoteSiteVaPath);
		
		JPanel panelRemoteSite = new JPanel();
		splitPaneChiaRemoteSiteVaPath.setLeftComponent(panelRemoteSite);
		panelRemoteSite.setLayout(new GridLayout(1, 0, 0, 0));
		
		JLabel lblNewLabel_3 = new JLabel("  Remote Site:");
		lblNewLabel_3.setFont(new Font("Times New Roman", Font.PLAIN, 13));
		panelRemoteSite.add(lblNewLabel_3);
		
		JPanel panelRemotePath = new JPanel();
		splitPaneChiaRemoteSiteVaPath.setRightComponent(panelRemotePath);
		panelRemotePath.setLayout(new GridLayout(1, 0, 0, 0));
		
		textField_1 = new JTextField();
		panelRemotePath.add(textField_1);
		textField_1.setColumns(10);
		splitPaneChiaRemoteSiteVaPath.setDividerLocation(72);
		
		//Phần cây thư mục
		JScrollPane scrollPane_1 = new JScrollPane();
		splitPaneChiaRemoteFolderTren.setRightComponent(scrollPane_1);
		
		//Khởi tạo và set cây thư mục lấy từ Server sẽ viết sau, tạm thời để JTree của swing
		JPanel panelRemoteTree = new JPanel();
		scrollPane_1.setViewportView(panelRemoteTree);
		panelRemoteTree.setLayout(new GridLayout(1, 0, 0, 0));
		
		JTree tree_1 = new JTree();
		panelRemoteTree.add(tree_1);
		splitPaneChiaRemoteFolderTren.setDividerLocation(25);
		
		JPanel panelRemoteFolderBottom = new JPanel();
		splitPaneChỉaemoteFolderTrenVaDuoi.setRightComponent(panelRemoteFolderBottom);
		splitPaneChỉaemoteFolderTrenVaDuoi.setDividerLocation(0.5);
		splitPaneChiaLocalVaRemoteFolder.setDividerLocation(0.5);
		
		setTitle("CLientFTP");
		setSize(new Dimension(1800, 1440));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
	

	public static void main(String[] args) {
		ClientGUI fr = new ClientGUI();
	}
}
