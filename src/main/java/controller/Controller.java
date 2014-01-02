package controller;

import interfaces.IView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;
import javax.swing.JToggleButton;

import model.Model;
import view.MainWindow;

public class Controller {
	
	
	private IView view;
	private Model model;
	
	public Controller() {
		int size = Integer.valueOf(JOptionPane.showInputDialog("Knotenanzahl eingeben: "));
		view = new MainWindow(size);
		model = new Model(size);
		addListener();
		view.setViewVisible(true);
		update();
	}

	private void addListener() {
		view.setListener(new ButtonListener());
	}
	
	//TODO pass the model reference to the view
	void update() {
	view.update(model);
	int[][] distance = model.getCalculatedDistAndWay().get(0);
	int[][] way = model.getCalculatedDistAndWay().get(1);
	for (int bRow = 0; bRow < distance.length; bRow++) {
		for (int bColumn = 0; bColumn < distance.length; bColumn++) {
			view.setDistanceButtonText(bRow, bColumn, String.valueOf(distance[bRow][bColumn]).equals("0") && bRow != bColumn ? "∞" 
														: String.valueOf(distance[bRow][bColumn]));
			view.setWayButtonText(bRow, bColumn, String.valueOf(way[bRow][bColumn]));
		}
	}
		
	}
	//TODO refactor this silly Listener and implement an own one with coordinates...
	private class ButtonListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			JToggleButton buttonClicked = null;
			int row;
			int column;
			String[] justForConverting = new String[2];
			
			if (!(e.getSource() instanceof JToggleButton)) { return; }
			
			
			buttonClicked = (JToggleButton) e.getSource();
			justForConverting = buttonClicked.getName().split("/");
			row = Integer.valueOf(justForConverting[0]);
			column = Integer.valueOf(justForConverting[1]);
			if(row == column) { return; }
			if (buttonClicked.getText().equals("0")) { 
				buttonClicked.setText("1");
				view.setButtonSelected(row, column, true);
				view.setAdjaButtonText(row, column, "1");
				model.calculate(row, column, 1);
				
			}
			
			else if (buttonClicked.getText().equals("1")) {
				buttonClicked.setText("0");
				view.setButtonSelected(row, column, false);
				view.setAdjaButtonText(row, column, "0");
				model.calculate(row, column, 0);
			}
			
			update();
		}

		
	}
	

}
