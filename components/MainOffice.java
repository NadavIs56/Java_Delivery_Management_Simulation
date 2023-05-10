package components;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Arc2D;
import java.awt.geom.Line2D;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.swing.JPanel;

import gui.Panels;

/**
 * 
 * @author Nadav Ishai, ID: 206119893
 *
 *class MainOffice is responsible to operate all the trucks and branches
 */

public class MainOffice implements Runnable{

		private static int clock;
		private int mementoclock;
		private static Hub hub;
		private Hub mementohub;
		private static ArrayList < Package> packages;
		private ArrayList<Package> mementopackages;
		private int totalpackagesnumber;
		private JPanel mainofficepanel;
		private boolean threadSuspend = false;
		private boolean stop = false;
		private static volatile MainOffice instance = null;
		private static ArrayList <Customer> customers;
		private ArrayList <Customer> mementocustomers;
		private static FileWriter fw = null;
		private static PrintWriter pw = null;
		private static ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
		private static Lock readLock = rwl.readLock();
	    private static Lock writeLock = rwl.writeLock();
	    private static FileReader fr = null;
	    ArrayList<Thread> tr = new ArrayList<Thread>(10);
	    Thread thub = new Thread();

	    
		
		protected MainOffice(int branches, int trucksForBranch, int num_packs){
			clock = 0;
			hub = new Hub();
			hub.AddHubTrucks(trucksForBranch);
			System.out.println();
			hub.AddBranchVans(branches, trucksForBranch);
			packages = new ArrayList < Package>();
			totalpackagesnumber = num_packs;
			
			customers = new ArrayList<Customer>(10);
			for (int i = 0; i < 10; i++)
				customers.add(new Customer());
			
			try {
				fw = new FileWriter("tracking.txt");
				pw = new PrintWriter(fw);
			} catch (IOException e) {
				e.printStackTrace();
			}
			finally {
				pw.print("");
				pw.close();
			}
		}
		
		public static MainOffice getMainOffice(int branches, int trucksForBranch, int num_packs) {
			if (instance == null)
				synchronized(MainOffice.class) {
					if (instance == null)
						instance = new MainOffice(branches, trucksForBranch, num_packs);
				}
			return instance; 
		}

		
		public void Restore(MementoMainOffice main) {
			synchronized(this) {
			
				Package.DecreaseId(packages.size() - main.GetMnenentoPackages().size());
				
				hub = new Hub(main.GetMnenentoHub(), true);
				
				
				
				packages = new ArrayList<Package>();
				
				
				for(Package pck : hub.GetPackagesList())
					packages.add(pck);
				
				for(Truck t : hub.GetTruckList())
					for(Package pck : t.GetPackagesList())
						packages.add(pck);
				
				for(Branch b :hub.GetMementobranches())
				{
					for(Truck t : b.GetTruckList())
						for(Package pck : t.GetPackagesList())
							packages.add(pck);
					
					for(Package pck : b.GetPackagesList())
						packages.add(pck);		
				}
				
				customers = new ArrayList<Customer>();
				for(Customer cts : main.GetMementoCustomers())
					customers.add(new Customer(cts));
				
				clock = main.GetMementoClock();
				instance = this;
				totalpackagesnumber = main.GetMementoTotalPackagesNumber();
				mainofficepanel = main.GetMementoMainOfficePanel();
				threadSuspend = main.GetMementoThreadSuspend();
				stop = main.GetMementoStop();
				fw = main.GetMementoFW();
				pw = main.GetMementoPW();
				rwl = main.GetMementoRRWL();
				readLock = main.GetMementoReadLock();
				writeLock = main.GetMementoWriteLock();
				fr = main.GetMementoFileReader();
				
				writeLock.lock();
				try {
					fw = new FileWriter("tracking.txt");
					pw = new PrintWriter(fw);
				} catch (IOException e) {
					e.printStackTrace();
				}
				finally {
					pw.print(main.GetMementoTrackingText());
					pw.close();
					writeLock.unlock();
				}
			}
		}
		
		
		/**
		 * Function play run the game "playTime" times
		 * @param playTime is determine the number of time units
		 */
		public void play(int playTime) {
			if(playTime > 0)
				for(int i = 0; i < playTime; i++) {
					if(i == 0)
						System.out.println("\n========================== START ==========================");
					tick();
				}
			System.out.println("========================== STOP ==========================\n\n");
			printReport();
		}
		
		
		
		/**
		 * Function printReport is responsible to print each package with it's tracking list
		 */
		public void printReport() {
			for(int i = 0; i < packages.size(); i++)
			{
				System.out.println("TRACKING " + packages.get(i));
				packages.get(i).PrintTracking();
				System.out.println();
			}
		}
		
		/**
		 * Function clockString printing the running time of the system "MM:SS"
		 */
		public void clockString() {
			if(clock / 60 < 10)
				System.out.printf("0%d:", clock / 60);
			else
				
				System.out.printf("%d:", clock / 60);
			if(clock % 60 < 10)
				System.out.printf("0%d\n", clock % 60);
			else
				System.out.printf("%d\n", clock % 60);
		}
		
		/**
		 * Function tick run the system for one time 
		 */
		public synchronized void tick() {
			clockString();
			clock += 1;
		}
		
		/**
		 * Function addPackage is responsible to create a new package and to add it to the packages lists
		 */
		public static Package addPackage(Address sender, int customer) {
				int size = hub.GetBranchesList().size();
//				Address sender = GenerateAddress(size);
				Address destination = GenerateAddress(size);
				int zip = sender.Get_Zip();
				int prioritynum = (int) (Math.random() * 3);
				Priority [] pr = Priority.values();
				Priority priority = pr[prioritynum];
				
				Random bl = new Random();
				
				int packtype = (int) (Math.random() * 3);
	
				switch (packtype) {
					case 0:
						boolean acknowledge = bl.nextBoolean();
						SmallPackage pck0 = new SmallPackage(priority, sender, destination, acknowledge, customer);
						hub.GetBranchesList().get(zip).GetPackagesList().add(pck0);
						GetMainOfficePackageList().add(pck0);
						
						return pck0;
					case 1:
						double weight = (Math.random() * 9) + 1;
						StandardPackage pck1 = new StandardPackage(priority, sender, destination, weight, customer);
						hub.GetBranchesList().get(zip).GetPackagesList().add(pck1);
						GetMainOfficePackageList().add(pck1);
						return pck1;
					case 2:
						int width = (int)(Math.random() * 500);
						int length = (int)(Math.random() * 1000);
						int height =(int)(Math.random() * 400);
						NonStandardPackage pck2 = new NonStandardPackage(priority, sender, destination, width, length, height, customer);
						hub.GetPackagesList().add(pck2);
						GetMainOfficePackageList().add(pck2);
						return pck2;
				}
				Package p = new SmallPackage(priority, sender, destination, true, 0);
				return p;
			
		}
		
		/**
		 * Function GenerateAddress is creating an address for customer
		 * @param size = number of the office branches
		 * @return a new valid address
		 */
		public static Address GenerateAddress(int size) {
			int zip = (int)(Math.random() * size);
			int street = (int) (Math.random() * 900000) + 100000;
			return new Address(zip, street);
		}
		
		/**
		 * Function GetHub returns the hub
		 * @return hub
		 */
		public static Hub GetHub() {
			return hub;
		}
		
		/**
		 * Function GetClock returns the system running time clock
		 * @return static clock
		 */
		public static int  GetClock() {
			return clock;
		}
		
		/**
		 * Function GetMainOfficePackageList return MainOffice packages list
		 * @return packages
		 */
		public static ArrayList <Package> GetMainOfficePackageList(){
			return packages;
		}
		
		/**
		 * Function SetMainOfficePanel sets the mainoffice panel
		 * @param pn the panel to set
		 */
		public void SetMainOfficePanel(JPanel pn) {
			mainofficepanel = pn;
		}
		
		/**
		 * Function run starts the mainoffice work
		 */
		@Override
		public void run() {	
			
			Thread t = new Thread((Runnable)hub);
			t.start();
			
			for(Customer c : customers)
			{
				Thread tr = new Thread((Runnable)c);
				tr.start();
			}
			
			while(true) {
		   		try {
					Thread.sleep(500);
					synchronized(this) {
		                while (threadSuspend)
		                	wait();
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
		   		if (stop) return;
		   		tick();
		   		mainofficepanel.repaint();
		   		if(CheckIfDone())
		   		{
		   			setStop();
		   			System.out.println("----------------------------All Done----------------------------");
		   			printReport();
		   		}
	   		}
			
		}
		
		/**
		 * Function DrawPackages draws all the packages that exist in the mainoffice senders & destinations
		 * @param g the graphics parameter
		 * @param rad the radius for each package
		 */
		public void DrawPackages(Graphics g, int rad) {
			synchronized(this) {
			double xstart = 115, ystart = 20;
			double pcknum = totalpackagesnumber;
			double space = 1000 / (pcknum);
			Color clr1, clr2;
			double x = 0, y1 = 0, y2 = 0;
			double brcx = 0, brcy1 = 0, brcy2 = 0;
			
			if(packages.size() > 0 && packages != null)
				for(Package pck : packages)
				{
					 Graphics2D g2d1 = (Graphics2D) g;
					 Graphics2D g2d2 = (Graphics2D) g;
					 
					 if(pck.GetStatus().equals("CREATION") || (pck instanceof NonStandardPackage && pck.GetStatus().equals("COLLECTION")))
						 clr1  = new Color(139, 0, 0);
					 else
						 clr1  = new Color(255, 204, 203);
					 
					 if(pck.GetStatus().equals("DELIVERED"))
						 clr2  = new Color(139, 0, 0);
					 else
						 clr2  = new Color(255, 204, 203);
					 
					 x = xstart + (space * (packages.indexOf(pck)));
					 y1 = ystart;
					 y2 = ystart + 565;
	
					 pck.SetSenderLocation(x + 15, y1 + 15);
					 pck.SetDestinationYLocation(y2 - 15);
					 
					 g2d1.setColor(clr1);
					 g2d1.fill(new Arc2D.Double(x, ystart, 30.0, 30.0, 0.0, 360.0, 1));
				     
				     g2d2.setColor(clr2);
				     g2d2.fill(new Arc2D.Double(x, y2, 30.0, 30.0, 0.0, 360.0, 1));
				     
				     brcx = hub.GetBranch(pck.GetSenderAddress().Get_Zip()).GetXMiddle();
				     brcy1 = hub.GetBranch(pck.GetSenderAddress().Get_Zip()).GetYMiddle();
				     brcy2 = hub.GetBranch(pck.GetDestinationAddress().Get_Zip()).GetYMiddle();
				     
				     if(pck instanceof StandardPackage || pck instanceof SmallPackage)
				    	 DrawStandardPackagePath(g, x+15, y1+30, brcx, brcy1, brcy2, y2, pck.GetStatus().equals("CREATION") || pck.GetStatus().equals("COLLECTION"), !pck.GetStatus().equals("DELIVERED"));
				     else
				    	 DrawNonStandardPackagePath(g, x+15, y1+30, y2, pck.GetStatus().equals("CREATION") || pck.GetStatus().equals("COLLECTION"), !pck.GetStatus().equals("DELIVERED"));
				}
			}
		}
		
		/**
		 * Function DrawStandardPackagePath draws the paths for the standardtrucks
		 * @param g the graphics parameter
		 * @param pckx smallpackage & standardpackage x  sender position
		 * @param pcky1 smallpackage & standardpackage y  sender position
		 * @param brcx branch middle x position
		 * @param brcy1 branch middle y position for senders
		 * @param brcy2 branch middle y position for destinations
		 * @param pcky2 smallpackage & standardpackage y  destination position
		 */
		public void DrawStandardPackagePath(Graphics g, double pckx, double pcky1, double brcx, double brcy1, double brcy2, double pcky2, boolean flag1, boolean flag2) {
			synchronized(this) {
			Color clr = Color.blue;
			Graphics2D g2d = (Graphics2D) g;
			
			g2d.setColor(clr);
			
			if(flag1)
				g2d.draw(new Line2D.Double(pckx, pcky1, brcx, brcy1 - 15));
//				g.drawLine(pckx, pcky1, brcx, brcy1 - 15);
			if(flag2)
				g2d.draw(new Line2D.Double(brcx, brcy2 + 15, pckx, pcky2));
//				g.drawLine(brcx, brcy2 + 15, pckx, pcky2);
		}
		}
		
		/**
		 * Function DrawNonStandardPackagePath draws the paths for the nonstandardtrucks
		 * @param g the graphics parameter
		 * @param pckx nonstandardpackage x position
		 * @param pcky1 nonstandardpackage y position
		 * @param pcky2 hub y position for the nonstandardtruck
		 */
		public void DrawNonStandardPackagePath(Graphics g, double pckx, double pcky1, double pcky2, boolean flag1, boolean flag2) {
			synchronized(this) {
			Color clr = new Color(255, 204, 203);
			Graphics2D g2d = (Graphics2D) g;
			g.setColor(clr);
			if(flag2)
				g2d.draw(new Line2D.Double(pckx, pcky1, pckx, pcky2));
//				g.drawLine(pckx, pcky1, pckx, pcky2);
			if(flag1)
				g2d.draw(new Line2D.Double(pckx, pcky1, 1160, 200));
//				g.drawLine(pckx, pcky1, 1160, 200);
		}
		}
		
		/**
		 * Function sesSuspend suspends the mainoffice thred
		 */
		public synchronized void setSuspend() {
		   	hub.setSuspend();
		   	for(Customer cs : customers)
		   		cs.setSuspend();
		   	threadSuspend = true;
		}
		
		/**
		 * Function setResume resumes the mainoffice thred
		 */
		public synchronized void setResume() {
			hub.setResume();
			for(Customer cs : customers)
		   		cs.setResume();
			threadSuspend = false;
		   	notifyAll();
		}
		
		/**
		 * Function setStop stops the mainoffice thred
		 */
		public void setStop() {
			setResume();
			for(Customer cs : customers)
		   		cs.setStop();
			hub.setStop();
		   	stop = true;
		}
		
		/**
		 * Function CheckIfDone checks if all the packages delivered to destination 
		 * @return
		 */
		public boolean CheckIfDone() {
			for(Customer cs : customers)
				if(!cs.GetStop())
					return false;
			
			return true;
		}
		
		/**
		 * return all customers array
		 * @return customers
		 */
		public static ArrayList <Customer> GetCustomers(){
			return customers;
		}
		
		/**
		 * return readLock of the ReadWriteLock
		 * @return readLock
		 */
		protected static Lock GetReadLock() {
			return readLock;
		}

		/**
		 * Function Write is responsible to write the new statuses of the packages to "tracking.txt"
		 * using Read/Write lock
		 * @param str is the new status
		 */
		public static void Write(String str) {
			synchronized(MainOffice.class){
			writeLock.lock();
			try {
				fw = new FileWriter("tracking.txt", true);
				pw = new PrintWriter(fw);
				pw.print(str);
			} catch (IOException e) {
				e.printStackTrace();
			}
			finally {
				pw.close();
				writeLock.unlock();
			}
			}
		}
		
		/**
		 * function to return total packages number
		 * 
		 */
		public int GetTotalPackagesNumber() {
			return totalpackagesnumber;
		}
		
		/**
		 * function to return main office panel
		 * 
		 */
		public JPanel GetMainOfficePanel() {
			return mainofficepanel; 
		}
		
		/**
		 * function to return thread Suspend
		 * 
		 */
		public boolean GetThreadSuspend() {
			return threadSuspend;
		}
		
		/**
		 * function to return stop
		 * 
		 */
		public boolean GetStop() {
			return stop;
		}
		
		/**
		 * function to return rwl
		 * 
		 */
		public ReentrantReadWriteLock GetReentrantReadWriteLock() {
			return rwl;
		}
		
		/**
		 * function to return writeLock
		 * 
		 */
		public Lock GetWriteLock() {
			return writeLock;
		}
		
		/**
		 * function to return fw
		 * 
		 */
		public FileWriter GetFileWriter() {
			return fw;
		}
		
		/**
		 * function to return pw
		 * 
		 */
		public PrintWriter GetPrintWriter() {
			return pw;
		}
		
		/**
		 * function to return fr
		 * 
		 */
		public FileReader GetFileReader() {
			return fr;
		}
		
		/**
		 * function to return tr
		 * 
		 */
		public ArrayList<Thread> GetCustomersThread() {
			return tr;
		}
		
		/**
		 * function to return thub
		 * 
		 */
		public Thread GetTHub() {
			return thub;
		}
}















