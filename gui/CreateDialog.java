package gui;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

/**
 * 
 * @author Nadav Ishai, ID: 206119893
 *	class CreateDialog responsible to create the panel to the main frame
 */
public class CreateDialog extends JDialog implements ActionListener {
	private static final long serialVersionUID = 1L;
	private JButton bt_ok, bt_cancel;
	private JPanel pn1, pn2;
	private JLabel numbrc, numtrckbrc, numpck;
	private JSlider sl_numbrc, sl_numtrckbrc , sl_numpck;
	private Panels panel;
	
	//Constructor
	public CreateDialog(Main fr, Panels pn, String str) {
		super((Main)fr,str,true);
		
		panel = pn;
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension dimension = toolkit.getScreenSize();
		setBounds(dimension.width/2-300, dimension.height/2-200, 600, 400);
		
		pn1 = new JPanel();
		pn2 = new JPanel();
		
		pn1.setLayout(new GridLayout(6,1,10,5));;
		numbrc = new JLabel("Number of branches", JLabel.CENTER);
		pn1.add(numbrc);
		
		sl_numbrc = new JSlider(1, 10);
		sl_numbrc.setMajorTickSpacing(1);
		sl_numbrc.setMinorTickSpacing(1);
		sl_numbrc.setPaintTicks(true);
		sl_numbrc.setPaintLabels(true);
		pn1.add(sl_numbrc);
		
		numtrckbrc = new JLabel("Number of trucks per branch", JLabel.CENTER);
		pn1.add(numtrckbrc);
		
		sl_numtrckbrc = new JSlider(1, 10);
		sl_numtrckbrc.setMajorTickSpacing(1);
		sl_numtrckbrc.setMinorTickSpacing(1);
		sl_numtrckbrc.setPaintTicks(true);
		sl_numtrckbrc.setPaintLabels(true);
		pn1.add(sl_numtrckbrc);
		
		pn2.setLayout(new GridLayout(1,2,5,5));
		bt_ok = new JButton("OK");
		bt_ok.addActionListener(this);
		bt_ok.setBackground(Color.lightGray);
		pn2.add(bt_ok);		
		
		bt_cancel=new JButton("Cancel");
		bt_cancel.addActionListener(this);
		bt_cancel.setBackground(Color.lightGray);
		pn2.add(bt_cancel);
		
		setLayout(new BorderLayout());
		add("North" , pn1);
		add("South" , pn2);
	}
	
	/**
	 * Function actiomperformed of the dialog window
	 */
	public void actionPerformed(ActionEvent e){
		if(e.getSource() == bt_ok){
		    panel.createNewOfficeSystem(sl_numbrc.getValue(), sl_numtrckbrc.getValue(), 50);
		    setVisible(false);
		}
		else 
		    setVisible(false);
	}
}








