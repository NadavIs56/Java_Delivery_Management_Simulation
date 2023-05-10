package gui;
import javax.swing.*;

import components.Branch;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * 
 * @author Nadav Ishai, ID: 206119893
 *class Main represent the main frmae
 */
public class Main extends JFrame implements ActionListener{
	private static final long serialVersionUID = 1L;
	private Panels panel;
	   
	public static void main(String[] args) {
		Main main_frame = new Main();
		main_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension dimension = toolkit.getScreenSize();
		main_frame.setBounds(dimension.width/2-600, dimension.height/2-350, 1200, 700);
		main_frame.setVisible(true);

	}
	
	public Main() {
		super("Post tracking system");
		panel = new Panels(this);
	    add(panel);
	    panel.setVisible(true);
	}
	
	public Panels GetPanels() {
		return panel;
	}
	

	
	public void actionPerformed(ActionEvent e){}	
	
}
