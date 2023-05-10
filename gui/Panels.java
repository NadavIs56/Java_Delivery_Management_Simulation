package gui;
import javax.swing.*;

import components.Branch;
import components.Hub;
import components.MainOffice;
import components.MementoMainOffice;
import components.NonStandardTruck;
import components.StandardTruck;
import components.Truck;
import components.Van;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;


/**
 * 
 * @author Nadav Ishai, ID: 206119893
 *	class panels responsible to the main panel of the gui
 */
public class Panels extends JPanel implements ActionListener{
	private static final long serialVersionUID = 1L;
	final int MAIN_BUTTONS_NUMBER = 9;
	private JButton[] bt_mainbuttonsarr;
	final static String[] str_mainbuttons = {"Create system", "Start", "Stop", "Resume", "All Packages Info", "Branch info", "Add New Branch", "Restore", "Report"};
	private JPanel pn_mainscreen;
	private Main frame;
	private MainOffice mainoffice = null;
	private boolean isrunning = false;
	private boolean visibletable;
	private JScrollPane allpackagesinfopanel;
	private int countmemento = 0;
	private ArrayList<MementoMainOffice> mementomainoffice = new ArrayList<MementoMainOffice>();
	private Hub hub;
	private boolean flag = false;
	
	//Constructor
	public Panels(Main fr) {
		frame = fr;
		visibletable = false;
		pn_mainscreen = new JPanel();
		pn_mainscreen.setLayout(new GridLayout(1, 6));
		bt_mainbuttonsarr = new JButton[MAIN_BUTTONS_NUMBER];
		for(int i = 0; i < MAIN_BUTTONS_NUMBER; i++)
		{
			bt_mainbuttonsarr[i] = new JButton(str_mainbuttons[i]);
			bt_mainbuttonsarr[i].addActionListener(this);
			bt_mainbuttonsarr[i].setBackground(Color.lightGray);
			pn_mainscreen.add(bt_mainbuttonsarr[i]);
		}

		setLayout(new BorderLayout());
		add("South", pn_mainscreen);
		
	}	//End of Panels constructor
	
	/**
	 * Function createNewOfficeSystem creats new system
	 * @param num_branches number of branches
	 * @param num_trucks number of truck per branch
	 * @param num_packs nummber of package per one run of the system
	 */
	public void createNewOfficeSystem(int num_branches, int num_trucks, int num_packs) {
	   if (mainoffice !=null)
	    {
	    	mainoffice.setSuspend();
	    	
	    }
	   mainoffice = components.MainOffice.getMainOffice(num_branches, num_trucks, num_packs);
	   hub = mainoffice.GetHub();
	   mainoffice.SetMainOfficePanel(this); 
	   isrunning = false;
	   repaint();
	}
	

	/**
	 * Function PaintComponent is responsible to call to all the functions that draws something in the mainoffice system
	 */
	public void paintComponent(Graphics g) {
	   	super.paintComponent(g);
	   	
	   	if(mainoffice == null) return;

	   	
	   	hub.DrawHub(g, 40, 200);
	   	
	   	for(Branch branch : hub.GetBranchesList())
	   	{
	   		branch.DrawBranch(g, 40, 30);
	   		branch.DrawPath(g, 40, 200);
	   	}
	   	
	   	mainoffice.DrawPackages(g, 30);
	   	
	   	for(Branch branch : hub.GetBranchesList())
	   		for(Truck van : branch.GetTruckList())
	   			if(van instanceof Van && van.GetPackagesList().size() > 0)
	   				((Van)van).DrawTruck(g, hub);
	   		
	   	for(Truck truck : hub.GetTruckList())
	   	{
	   		if(truck instanceof NonStandardTruck && !truck.GetAavilableStatus())
	   			((NonStandardTruck)truck).DrawTruck(g, hub);
	   		else if(truck instanceof StandardTruck && ((StandardTruck)truck).GetDestination() != null && !((StandardTruck)truck).GetAavilableStatus())
	   			((StandardTruck)truck).DrawTruck(g, hub);
	   	}  
	}
	
	/**
	 * Function add create new system
	 */
	public void add() {
		CreateDialog dial = new CreateDialog(frame,this,"Create post system");
		dial.setVisible(true);
	}
	
	/**
	 * Function start run the system
	 */
	public void start() {
		if (mainoffice == null || isrunning) return;
	   isrunning= true;
	   Thread t = new Thread(mainoffice);
	   t.start();
	}
	   
	/**
	 * Function stop stops the system
	 */
 	public void stop() {
		if (mainoffice == null) return;
		
			mainoffice.setSuspend();
 	}
	 	
 	/**
 	 * Function resume resumes the system
 	 */
	public void resume() {
		if (mainoffice == null) return;
			mainoffice.setResume();
	}
		
	/**
	 * Function allpackageinfo shows all the packages info in a table
	 * @param flag boolean voisible or no
	 * @param branchid int id of the branch
	 */
	public void allpackageinfo(boolean flag, int branchid) { 
	   if (mainoffice == null) return;
	   
	   if(visibletable == false) {
		  int i=0;
		  String[] columnNames = {"Package ID", "Sender", "Destination", "Priority", "Status"};
		  ArrayList<components.Package> allpackages = null;
		  String [][] data = null;
		  if(!flag)
		  {
			  allpackages = mainoffice.GetMainOfficePackageList();
			  data = new String[mainoffice.GetMainOfficePackageList().size()][columnNames.length];
			  for(components.Package pck : mainoffice.GetMainOfficePackageList())
			  {
				  data[i][0] = ""+pck.GetPackageId();
				  data[i][1] = ""+pck.GetSenderAddress().toString();
				  data[i][2] = ""+pck.GetDestinationAddress().toString();
				  data[i][3] = ""+pck.GetPriority();
				  data[i][4] = ""+pck.GetStatus();
				  i++;
			  }
		  }
		  else if(flag && branchid != -1)
		  {
			  allpackages = new ArrayList<components.Package>();
			  for(components.Package pck : mainoffice.GetMainOfficePackageList())
				  if(pck.GetSenderAddress().Get_Zip() == branchid)
					  allpackages.add(pck);
			  data = new String[allpackages.size()][columnNames.length];
			  for(components.Package pck : allpackages)
			  {
				  data[i][0] = ""+pck.GetPackageId();
				  data[i][1] = ""+pck.GetSenderAddress().toString();
				  data[i][2] = ""+pck.GetDestinationAddress().toString();
				  data[i][3] = ""+pck.GetPriority();
				  data[i][4] = ""+pck.GetStatus();
				  i++;
			  }
		  }
		  else
		  {
			  allpackages = new ArrayList<components.Package>();
			  for(components.Package pck : mainoffice.GetMainOfficePackageList())
				  if(pck.GetPackageType().equals("NonStandardPackage"))
					  allpackages.add(pck);
			  data = new String[allpackages.size()][columnNames.length];
			  for(components.Package pck : allpackages)
			  {
				  data[i][0] = ""+pck.GetPackageId();
				  data[i][1] = ""+pck.GetSenderAddress().toString();
				  data[i][2] = ""+pck.GetDestinationAddress().toString();
				  data[i][3] = ""+pck.GetPriority();
				  data[i][4] = ""+pck.GetStatus();
				  i++;
			  }
		  }
		  JTable table = new JTable(data, columnNames);
		  allpackagesinfopanel = new JScrollPane(table);
		  allpackagesinfopanel.setSize(460,table.getRowHeight()*(allpackages.size())+25);
	      add(allpackagesinfopanel, BorderLayout.CENTER );
	      visibletable = true;
	   }
	   else
		   visibletable = false;
	   
	   allpackagesinfopanel.setVisible(visibletable);
	   repaint();
   }
	   
	/**
	 * Function branchinfotable shows a specific branch packages details
	 */
	public void branchinfotable() {
		if(visibletable)
		{
			visibletable = false;
			allpackagesinfopanel.setVisible(visibletable);
		}
		BranchInfo bi = new BranchInfo(frame,this,"Create post system", mainoffice);
		bi.setVisible(true);
	}
	
	/**
	 * function to add new cloned branch
	 */
	public void AddNewBranch() {	
		synchronized(this) { 
			mementomainoffice.add(new MementoMainOffice(mainoffice));
			mainoffice.GetHub().AddClonedBranch(mainoffice.GetHub().GetBranch(0).clone());
			repaint();
			countmemento++;
		}
		
	}
	
	/**
	 * function to set memnto
	 */
	public void SetMemento() {
		if(countmemento > 0)
		{
			synchronized(this) {
				mainoffice.setStop();
				mainoffice.Restore(mementomainoffice.get(mementomainoffice.size() - 1));
				mementomainoffice.remove(mementomainoffice.size() - 1);
				hub = mainoffice.GetHub();
			    mainoffice.SetMainOfficePanel(this); 
			    isrunning = false;
			    for(int i = 0; i < countmemento; i++)
			    	Branch.DecreaseBranchId();
			    start();
				repaint();
				countmemento--; 
				
			}
		}
	}
	
	/**
	 * function to open trucking file
	 */
	public void OpenTrackingFile(){
		if (Desktop.isDesktopSupported()) 
		{
			Desktop desktop = Desktop.getDesktop(); 
			File file = new File("tracking.txt");    
			try {
				desktop.open(file);
				
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
		

	/**
	 * Function actionPerformed is the listener of the main frame buttons
	 */
	public void actionPerformed(ActionEvent e){
		if(e.getSource() == bt_mainbuttonsarr[0])
			add();
		else if(e.getSource() == bt_mainbuttonsarr[1]) 
			start();
		else if(e.getSource() == bt_mainbuttonsarr[2])  
			stop();
		else if(e.getSource() == bt_mainbuttonsarr[3])  
			resume(); 
		else if(e.getSource() == bt_mainbuttonsarr[4])  
			allpackageinfo(false, 0);
		else if(e.getSource() == bt_mainbuttonsarr[5]) 
			branchinfotable();
		else if (e.getSource() == bt_mainbuttonsarr[6]) 
			AddNewBranch();
		else if (e.getSource() == bt_mainbuttonsarr[7]) 
			SetMemento();
		else if (e.getSource() == bt_mainbuttonsarr[8]) 
			OpenTrackingFile();
	}

	/**
	 * Function GetMainOffice returns the mainoffice
	 * @return
	 */
	public MainOffice GetMainOffice() {
		return mainoffice;
	}
	
}




