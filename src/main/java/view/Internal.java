package view;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

@SuppressWarnings("serial")
public class Internal extends JInternalFrame {

	private JPanel panel = new JPanel();
	private JToggleButton[][] toggleButtons;
	private int size;
	private String diagonalButtonValue;
	private String defaultButtonValue;
	private boolean buttonEnabled;

	public Internal(String name, int size, boolean resizeable,
			String diagonalValue, String defaultValue, boolean buttonEnabled) {
		super(name, resizeable, false);
		if (!(size >= 1)) {
			throw new IllegalArgumentException(
					"Number of Vertices must be greater or equal 1!");
		}
		this.buttonEnabled = buttonEnabled;
		this.size = size;
		this.diagonalButtonValue = diagonalValue;
		this.defaultButtonValue = defaultValue;
		panel.setLayout(new GridLayout(size + 1, size + 1, 3, 3));
		toggleButtons = new JToggleButton[size][size];
		initToggleButtons();
		addComponents();
		setName(name);
		add(panel);
	}
	
	void setButtonSelected(int row, int column, boolean isSelected) {
		toggleButtons[row][column].setSelected(isSelected);
		toggleButtons[column][row].setSelected(isSelected);
	}
	
	void setButtonText(int row, int column, String text) {
		toggleButtons[row][column].setText(text);
		toggleButtons[column][row].setText(text);
	}
	
	
	
	public void addListenerToAllButtons(ActionListener al) {
		for (int row = 0; row < size; row++) {
			for (int column = 0; column < size; column++) {
				if (row == column) { continue; }
				toggleButtons[row][column].addActionListener(al);
			}
		}
	}

	private void initToggleButtons() {
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				toggleButtons[i][j] = new MyToggleButton(size);
				toggleButtons[i][j].setName(String.valueOf(i) + "/"	+ String.valueOf(j));
				if (i == j) {
					toggleButtons[i][j].setText(diagonalButtonValue);
					toggleButtons[i][j].setEnabled(false);
				} else {
					toggleButtons[i][j].setText(defaultButtonValue);
					toggleButtons[i][j].setEnabled(buttonEnabled);
				}

			}
		}
	}
	
	

	/**
	 * inits the description buttons and add all the buttons to the panel
	 */
	private void addComponents() {
		for (int i = 0; i < toggleButtons.length + 1; i++) {
			for (int j = 0; j < toggleButtons.length + 1; j++) {
				if (i == 0 && j == 0) {
					MyToggleButton b = new MyToggleButton(size);
					b.setEnabled(false);
					b.setBackground(Color.LIGHT_GRAY);
					panel.add(b);
					continue;
				}
				if (i == 0 && j != 0) {
					MyToggleButton b = new MyToggleButton(size);
					b.setText(String.valueOf(j));
					b.setEnabled(false);
					b.setBackground(Color.LIGHT_GRAY);
					panel.add(b);
					continue;
				}
				if (j == 0) {
					MyToggleButton b = new MyToggleButton(size);
					b.setText(String.valueOf(i));
					b.setEnabled(false);
					b.setBackground(Color.LIGHT_GRAY);
					panel.add(b);
					continue;
				}
				panel.add(toggleButtons[i - 1][j - 1]);

			}
		}

	}

}
