package View;

import javax.swing.JDialog;
import javax.swing.JFrame;
import java.awt.Dimension;

public class ServerConfigureGUI extends JDialog{
	public ServerConfigureGUI() {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setTitle("Configure Server");
		setSize(new Dimension(850, 550));
		setModal(true);
		
		
		setVisible(true);
	}

	public static void main(String[] args) {
		//new ServerConfigureGUI();
	}

}
