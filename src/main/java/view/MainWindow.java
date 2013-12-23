package view;

import interfaces.IView;

import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.util.Arrays;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import model.Model;

@SuppressWarnings("serial")
public class MainWindow extends JFrame implements IView{
	
	private Internal adjazenzMatrix;
	private Internal distanceMatrix;
	private Internal wayMatrix;
	private TextWindow textWindow;
	private JDesktopPane desktopPane;
	private int size;

	public MainWindow(int size) {
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.size = size;
		SwingUtilities.updateComponentTreeUI(this);
		initComponents(size);
		addComponents();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(Toolkit.getDefaultToolkit().getScreenSize());

	}
	@Override
	public void setButtonSelected(int row, int column, boolean isSelected) {
		adjazenzMatrix.setButtonSelected(row, column, isSelected);
	}
	@Override
	public void setAdjaButtonText(int row, int column, String text) {
		adjazenzMatrix.setButtonText(row, column, text);
	}
	@Override
	public void setDistanceButtonText(int row, int column, String text) {
		distanceMatrix.setButtonText(row, column, text);
	}
	@Override
	public void setWayButtonText(int row, int column, String text) {
		wayMatrix.setButtonText(row, column, text);
	}
	
	@Override
	public void setListener(ActionListener listener) {
		adjazenzMatrix.addListenerToAllButtons(listener);
		distanceMatrix.addListenerToAllButtons(listener);
		wayMatrix.addListenerToAllButtons(listener);
	}
	
	public void setViewVisible(boolean b) {
		this.setVisible(b);
	}


	
	private void addComponents() {
		desktopPane = new JDesktopPane();
		desktopPane.setBackground(Color.gray);
		desktopPane.add(adjazenzMatrix);
		desktopPane.add(distanceMatrix);
		desktopPane.add(wayMatrix);
		desktopPane.add(textWindow);
		setSizeToInternals();
		adjazenzMatrix.setLocation(10, 10);
		textWindow.setSize(650,300);
		distanceMatrix.setLocation(970, 10);
		wayMatrix.setLocation(500, 10);
		textWindow.setLocation(10, 500);
		textWindow.show();
		adjazenzMatrix.show();
		distanceMatrix.show();
		wayMatrix.show();
		add(desktopPane);

	}

	private void setSizeToInternals() {
		if (size > 8 && size < 16) {
			adjazenzMatrix.setSize(30*size+3, 30*size+3);
			distanceMatrix.setSize(30*size+3, 30*size+3);
			wayMatrix.setSize(30*size+3, 30*size+3);
		}
		else if (size < 9){
			adjazenzMatrix.setSize(50*size+3, 50*size+3);
			distanceMatrix.setSize(50*size+3, 50*size+3);
			wayMatrix.setSize(50*size+3, 50*size+3);
		}
		else if (size > 15){
			adjazenzMatrix.setSize(20*size+3, 20*size+3);
			distanceMatrix.setSize(20*size+3, 20*size+3);
			wayMatrix.setSize(20*size+3, 20*size+3);
		}
	}
	
	public void setSize(int size) {
		this.size = size;
	}

	private void initComponents(int size) {
		adjazenzMatrix = new Internal("Adjazenzmatrix", size, true, "0", "0", true);
		distanceMatrix = new Internal("Distancematrix", size, true, "0", "∞", false);
		wayMatrix = new Internal("Waymatrix", size, true, "1", "0", false);
		textWindow = new TextWindow();
	}
	
	@Override
	public void update(Model model) {
		StringBuilder sb = new StringBuilder();
		sb.append("Der Graph ist zusammenhaengend: " + model.getCoherently()).append("\n");
		if (model.getCoherently()) { sb.append("Das Zentrum sind: " + model.getCenterVertices().toString()).append("\n");	} 
		else sb.append("Das Zentrum ist: -1 \n");
		sb.append("Der Radius ist: " + model.getRadius()).append("\n");
		sb.append("Der Durchmesser ist: " + model.getDiameter()).append("\n");
		sb.append("Exzentrizitäten sind: " + Arrays.toString(model.getEccentricity())).append("\n");
		sb.append(model.getComponents()).append("\n");
		sb.append("Artikulationen: ").append(model.getArtikulationen().toString()).append("\n");
		sb.append("Bruecken: ").append(model.getBruecken().toString());
		textWindow.setText(sb.toString());
		
	}
}
