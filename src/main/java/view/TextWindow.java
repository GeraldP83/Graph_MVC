package view;

import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

@SuppressWarnings("serial")
public class TextWindow extends JInternalFrame {

	private JTextArea textArea;
	private JScrollPane scroll;

	public TextWindow() {
		textArea = new JTextArea();
		scroll = new JScrollPane(textArea);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		this.add(scroll);
	}

	public void setText(String text) {
		textArea.setText(text);
	}
}
