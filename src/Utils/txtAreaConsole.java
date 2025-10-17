package Utils;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

import View.ClientGUI;

public class txtAreaConsole {
	private JTextArea txtConsole;
	private int lastPromptPos = 0;
	
	public interface ConsoleListener{
		void onCommandEntered(String command);
	}
	
	private ConsoleListener listener;
	
	public void setConsoleListener(ConsoleListener listener) {
		this.listener = listener;
	}
	
	public txtAreaConsole() {
		this.txtConsole = new JTextArea();
		setConsole();
	}
	
	public JTextArea getConsole() {
		return txtConsole;
	}
	
	public void setConsole() {
		txtConsole.getInputMap().put(KeyStroke.getKeyStroke("ENTER"), "handleEnter");
		txtConsole.getActionMap().put("handleEnter", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String fullText = txtConsole.getText();
				String command = fullText.substring(lastPromptPos).trim();
				
				//Gửi dòng vừa nhập đi, sẽ viết sau.
				if(listener != null) {
					try {
						listener.onCommandEntered(command);
					}catch(Exception E) {
						
					}
				}
				
				//xuống dòng mới
				txtConsole.append("\n\n>");
				lastPromptPos = txtConsole.getCaretPosition();
			}
		});
		txtConsole.addCaretListener(e -> {
			int caret = txtConsole.getCaretPosition();
			int limit = lastPromptPos;
			if(caret <= limit) {
				txtConsole.setCaretPosition(txtConsole.getDocument().getLength());
			}
		});
		((AbstractDocument) txtConsole.getDocument()).setDocumentFilter(new DocumentFilter() {
			@Override
			public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
				// TODO Auto-generated method stub
				if(offset < lastPromptPos) {
					return;
				}
				super.remove(fb, offset, length);
			}
			@Override
			public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
					throws BadLocationException {
				// TODO Auto-generated method stub
				if(offset < lastPromptPos) {
					return;
				}
				super.insertString(fb, offset, string, attr);
			}
			@Override
			public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
					throws BadLocationException {
				// TODO Auto-generated method stub
				if(offset < lastPromptPos) {
					return;
				}
				super.replace(fb, offset, length, text, attrs);
			}
		});
		txtConsole.append(">");
		lastPromptPos = txtConsole.getDocument().getLength();
	}
}
