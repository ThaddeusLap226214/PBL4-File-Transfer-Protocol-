package View;

import javax.swing.*;
import java.awt.*;
import Controller.ClientController;
import Utils.FileTreePanel;
import Utils.txtAreaConsole;

public class ClientGUI extends JFrame {
	private ClientController controller;

	private JTextField txtFHost;
	private JTextField txtFUser;
	private JTextField txtFPass;
	private JTextField txtFPort;
	private JTextField txtFLocalPath;
	private JTextField txtFRemotePath;

	// ===== Gán controller từ MainClient =====
	public void setController(ClientController controller) {
		this.controller = controller;
	}

	public ClientGUI() {
		// --- Layout chính ---
		getContentPane().setLayout(new GridLayout(1, 0, 0, 0));

		// Chia giao diện chính thành 2 phần: trên (Login + CMD) và dưới (Folder)
		JSplitPane splitPaneMain = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		splitPaneMain.setDividerSize(3);
		splitPaneMain.setResizeWeight(0.5);
		getContentPane().add(splitPaneMain);

		// ========== PHẦN TRÊN: LOGIN + CMD ==========
		JPanel panelTop = new JPanel(new GridLayout(1, 0, 0, 0));
		splitPaneMain.setTopComponent(panelTop);

		JSplitPane splitTop = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		splitTop.setResizeWeight(0.2);
		panelTop.add(splitTop);

		// ===== LOGIN PANEL =====
		JPanel panelLogin = new JPanel(null);
		splitTop.setTopComponent(panelLogin);

		JLabel lbHost = new JLabel("Host:");
		lbHost.setFont(new Font("Times New Roman", Font.PLAIN, 13));
		lbHost.setBounds(10, 25, 40, 13);
		panelLogin.add(lbHost);

		txtFHost = new JTextField("127.0.0.1");
		txtFHost.setFont(new Font("Times New Roman", Font.PLAIN, 13));
		txtFHost.setBounds(44, 21, 96, 21);
		panelLogin.add(txtFHost);

		JLabel lbUser = new JLabel("User:");
		lbUser.setFont(new Font("Times New Roman", Font.PLAIN, 13));
		lbUser.setBounds(160, 25, 40, 13);
		panelLogin.add(lbUser);

		txtFUser = new JTextField("anonymous");
		txtFUser.setBounds(200, 21, 96, 21);
		panelLogin.add(txtFUser);

		JLabel lbPass = new JLabel("Pass:");
		lbPass.setFont(new Font("Times New Roman", Font.PLAIN, 13));
		lbPass.setBounds(310, 25, 40, 13);
		panelLogin.add(lbPass);

		txtFPass = new JTextField();
		txtFPass.setBounds(350, 21, 96, 21);
		panelLogin.add(txtFPass);

		JLabel lbPort = new JLabel("Port:");
		lbPort.setFont(new Font("Times New Roman", Font.PLAIN, 13));
		lbPort.setBounds(470, 25, 40, 13);
		panelLogin.add(lbPort);

		txtFPort = new JTextField("5000");
		txtFPort.setBounds(510, 21, 80, 21);
		panelLogin.add(txtFPort);

		JButton btnConnect = new JButton("Connect");
		btnConnect.setBounds(620, 21, 100, 23);
		panelLogin.add(btnConnect);

		// Khi nhấn Connect
		btnConnect.addActionListener(e -> {
			String host = txtFHost.getText().trim();
			String portStr = txtFPort.getText().trim();
			String user = txtFUser.getText().trim();
			String pass = txtFPass.getText().trim();

			if (controller != null) {
				try {
					int port = Integer.parseInt(portStr);
					controller.connectToServer(host, port, user, pass);
				} catch (NumberFormatException ex) {
					JOptionPane.showMessageDialog(this, "Port phải là số!", "Lỗi", JOptionPane.ERROR_MESSAGE);
				}
			} else {
				JOptionPane.showMessageDialog(this, "Controller chưa được gán!", "Lỗi", JOptionPane.ERROR_MESSAGE);
			}
		});

		// ===== CMD PANEL =====
		JPanel panelCMD = new JPanel(new BorderLayout());
		splitTop.setBottomComponent(panelCMD);

		txtAreaConsole tac = new txtAreaConsole();
		tac.setConsoleListener(command -> controller.checkCMD(command));

		JScrollPane scrollConsole = new JScrollPane(tac.getConsole());
		panelCMD.add(scrollConsole, BorderLayout.CENTER);

		splitTop.setDividerLocation(0.25);

		// ========== PHẦN DƯỚI: LOCAL + REMOTE ==========
		JPanel panelBottom = new JPanel(new GridLayout(1, 0, 0, 0));
		splitPaneMain.setBottomComponent(panelBottom);

		JSplitPane splitBottom = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		splitBottom.setDividerSize(3);
		splitBottom.setResizeWeight(0.5);
		panelBottom.add(splitBottom);

		// ===== LOCAL PANEL =====
		JPanel panelLocal = new JPanel(new BorderLayout());
		splitBottom.setLeftComponent(panelLocal);

		// Thanh đường dẫn
		JPanel panelLocalPath = new JPanel(new BorderLayout());
		panelLocalPath.add(new JLabel("  Local site:"), BorderLayout.WEST);
		txtFLocalPath = new JTextField("D:\\");
		panelLocalPath.add(txtFLocalPath, BorderLayout.CENTER);
		panelLocal.add(panelLocalPath, BorderLayout.NORTH);

		// Cây thư mục local
		JScrollPane scrollLocalTree = new JScrollPane();
		FileTreePanel localTreePanel = new FileTreePanel("D:\\");
		scrollLocalTree.setViewportView(localTreePanel.tree());
		panelLocal.add(scrollLocalTree, BorderLayout.CENTER);

		// ===== REMOTE PANEL =====
		JPanel panelRemote = new JPanel(new BorderLayout());
		splitBottom.setRightComponent(panelRemote);

		JPanel panelRemotePath = new JPanel(new BorderLayout());
		panelRemotePath.add(new JLabel("  Remote site:"), BorderLayout.WEST);
		txtFRemotePath = new JTextField("/");
		panelRemotePath.add(txtFRemotePath, BorderLayout.CENTER);
		panelRemote.add(panelRemotePath, BorderLayout.NORTH);

		// Cây thư mục remote (tạm thời dùng JTree trống)
		JScrollPane scrollRemoteTree = new JScrollPane(new JTree());
		panelRemote.add(scrollRemoteTree, BorderLayout.CENTER);

		// ===== CẤU HÌNH CHUNG =====
		splitBottom.setDividerLocation(0.5);
		splitPaneMain.setDividerLocation(0.45);

		setTitle("FTP Client");
		setSize(new Dimension(1600, 900));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
	}

	// Hàm append log ra console (nếu cần controller gọi ngược)
	public void appendConsole(String msg) {
		System.out.println(msg); // hoặc gọi tac.appendConsole(msg) nếu bạn sửa txtAreaConsole hỗ trợ
	}

	// Test GUI riêng lẻ
	public static void main(String[] args) {
		new ClientGUI().setVisible(true);
	}
}
