package view;

import java.awt.Font;
import java.awt.Insets;

import javax.swing.JToggleButton;

@SuppressWarnings("serial")
public class MyToggleButton extends JToggleButton{
	
	public MyToggleButton(int size) {
		setFont(new Font("sansserif", Font.PLAIN, size > 15 ? 8 : 10));
		setMargin(new Insets(2, 2, 2, 2));
	}

}
