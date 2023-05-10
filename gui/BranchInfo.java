package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import components.Branch;
import components.Hub;
import components.MainOffice;

/**
 * 
 * @author Nadav Ishai, ID: 206119893
 * class BranchInfo represents the new frame for the package info according to the selected branch
 */
class BranchInfo extends JDialog implements ActionListener{
	private JComboBox<String> branchcombobox;
	private String[] branchnames = null;
	private JButton bt_ok;
	private JButton bt_cnl;
	private JPanel pn;
	private Panels panels;
	private int brunchnum;
	private boolean visible = false;
	private boolean framevisible = false;
	private MainOffice mainoffice;
	private JScrollPane scrollpanel;
	
	//Constructor
	public BranchInfo(Main fr, Panels pnls, String str, MainOffice mo) {
		super((Main)fr,str,true);
		if(mo == null)return;
		
		panels = pnls;
		mainoffice = mo;
		brunchnum = mainoffice.GetHub().GetBranchesList().size();
		if(framevisible == false)
		{
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension dimension  = toolkit.getScreenSize();
		setBounds(dimension.width/2-150, dimension.height/2-75, 300, 150);
		
		BorderLayout myBorderLayout = new BorderLayout();
		setLayout(myBorderLayout);
		
		pn = new JPanel();
		pn.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		bt_ok = new JButton("OK");
		bt_ok.addActionListener(this);
		bt_cnl = new JButton("Cancel");
		bt_cnl.addActionListener(this);
	
		branchnames = new String[brunchnum + 1];
		branchnames[0] = "Sorting center";
		int i = 1;
		for(Branch branch : mainoffice.GetHub().GetBranchesList())
		{
			branchnames[i] = branch.GetBranchName();
			i++;
		}
		branchcombobox = new JComboBox<String>(branchnames);
		branchcombobox.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		add(branchcombobox, BorderLayout.NORTH);
		pn.add(bt_ok);
		pn.add(bt_cnl);
		add(pn, BorderLayout.SOUTH);
		framevisible = true;
		}
	}	

	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == bt_ok)
		{
			setVisible(false);
			panels.allpackageinfo(true, branchcombobox.getSelectedIndex() - 1);
		}
		else
			setVisible(false);
	}
}
