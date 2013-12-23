package interfaces;

import java.awt.event.ActionListener;

import model.Model;

public interface IView {
	
	void update(Model model);
	
	void setViewVisible(boolean b);
	
	void setListener(ActionListener listener);
	
	void setButtonSelected(int row, int column, boolean isSelected); 
	void setAdjaButtonText(int row, int column, String text);
	void setDistanceButtonText(int row, int column, String text);
	void setWayButtonText(int row, int column, String text);
}
