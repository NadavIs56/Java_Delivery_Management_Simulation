package components;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;


/**
 * class Branch represents a post office
 * @author Nadav Ishai, ID: 206119893
 *
 */
public class Branch implements Node, Runnable, Cloneable{ 

	private static int branchNum = -1;
	private int branchId;
	private String branchName;
	protected ArrayList <Truck> listTrucks = new ArrayList <Truck>();
	protected ArrayList <Package> listPackages = new ArrayList <Package>();
	private static ArrayList <Branch> BranchesList = new ArrayList <Branch>();
	private ArrayList <Branch> mementoBranches = new ArrayList <Branch>();
	protected double xmiddle;
	protected double ymiddle;
	protected boolean threadSuspend = false;
	protected boolean stop = false;
	protected double hubx;
	protected double huby;
//	private ArrayList<Thread> tvan = new ArrayList<Thread>();
//	private ArrayList<Thread> mementotvantvan = new ArrayList<Thread>();
	
	
	public Branch(){
		branchId = branchNum;
		if (branchId == -1)
			branchName = "HUB";
		else
			branchName = "Branch " + branchId;
		branchNum += 1;
		listTrucks = new ArrayList <Truck>();
		listPackages = new ArrayList <Package>();
		BranchesList.add(this);
		System.out.printf("Creating Branch %d, branch name:%s, packages: %d, trucks: %d\n", branchId, branchName, listPackages.size(), listTrucks.size());
	}

	Branch(String branchName){
		if (branchId == -1)
		{
			branchId= - 1;
			this.branchName = "HUB";
		}
		else
		{
			branchId = (int)(branchName.charAt(branchName.length() - 1) - '0');
			this.branchName = branchName;
		}
		branchNum += 1;
		listTrucks = new ArrayList <Truck>();
		listPackages = new ArrayList <Package>();
		BranchesList.add(this);
	}
	
	
	Branch(Branch b){
		
		branchId = b.GetBranchId();
		branchName = b.GetBranchName();
		
		listTrucks = new ArrayList <Truck>();
		for(Truck t : b.listTrucks)
			if(t instanceof Van)
				listTrucks.add(new Van(t));

		listPackages = new ArrayList <Package>();
		
		for(Package p : b.listPackages)
		{
			if(p instanceof SmallPackage)
				listPackages.add(new SmallPackage(p));
			else if(p instanceof StandardPackage)
				listPackages.add(new StandardPackage(p));
		}
		
		xmiddle = b.GetXMiddle();
		ymiddle = b.GetYMiddle();
		threadSuspend = b.GetThreadSuspend();
		stop = b.GetStop();
		hubx = b.GetHubX();
		huby = b.GetHubY();
	}
	
	
	Branch(Hub b, boolean flag){
		branchId = -1;
		branchName = "HUB";
	}
	
	
	/**
	 * Function AddStandardTruck adding a new StandardTruck to the Hub truck's list
	 */
	public void AddStandardTruck() {
		listTrucks.add(new StandardTruck());
	}
	
	/**
	 * Function AddNonStandardTruck adding a new NonStandardTruck to the Hub truck's list
	 */
	public void AddNonStandardTruck() {
		listTrucks.add(new NonStandardTruck());
	}
	
	/**
	 * Function AddNonStandardTruck adding a new Van to the Branch truck's list
	 */
	public void AddVan(int size) {
		
		for(int i = 0; i < size; i++)
			listTrucks.add(new Van());
	}
	
	/**
	 * Function GetBranchName return the branch name
	 * @return branchName
	 */
	public String GetBranchName() {
		return branchName;
	}
	
	/**
	 * Function GetBranchId return the branch id
	 * @return branchId
	 */
	public int GetBranchId() {
		return branchId;
	}
	
	/**
	 * Function GetTruckList return the branch truck's list
	 * @return listTrucks
	 */
	public ArrayList<Truck> GetTruckList(){
		return listTrucks;
	}
	
	/**
	 * Function GetPackagesList return the branch packages's list
	 * @return listPackages
	 */
	public ArrayList<Package> GetPackagesList(){
		return listPackages;
	}
	
	/**
	 * Function collectPackage is searching for an available Van and link him to package that waits to collection and determine the Van driving time 
	 */
	public void collectPackage(Package p) {
		for (int j = 0; j < GetTruckList().size(); j++) 
		{
			if(GetTruckList().get(j).GetAavilableStatus())
			{
				int time = ((p.GetSenderAddress().Get_Street() % 10) + 1) * 10;
				GetTruckList().get(j).SetTimeLeft(time);
				GetTruckList().get(j).SetSaveTime(time);
				GetTruckList().get(j).SetToNoAvailable();
				p.addTracking(GetTruckList().get(j), Status.COLLECTION);
				GetTruckList().get(j).GetPackagesList().add(p);		
				System.out.printf("Van %d is collecting package %d, time to arrive: %d\n", GetTruckList().get(j).GetTruckId(), p.GetPackageId(), time);
				break;
			}
		}
	}
	
	/**
	 * Function deliverPackage is searching for an available Van and link him to package that waits to distribution and determine the Van driving time 
	 */
	public void deliverPackage(Package p) {
		for (int j = 0; j < GetTruckList().size(); j++) {
			if(GetTruckList().get(j).GetAavilableStatus())
			{
				int time = ((p.GetDestinationAddress().Get_Street() % 10) + 1) * 10;
				GetTruckList().get(j).SetTimeLeft(time);
				GetTruckList().get(j).SetSaveTime(time);
				GetTruckList().get(j).SetToNoAvailable();
				p.addTracking(GetTruckList().get(j), Status.DISTRIBUTION);
				GetTruckList().get(j).GetPackagesList().add(p);	
				break;
			}
		}
	}
	
	/**
	 * Function work perform one work unit of all the vans and run the branch's vans work function 
	 */
	public synchronized void work() {
		for (int i = 0; i < GetPackagesList().size(); i++) 
		{			
			if(GetPackagesList().get(i).equals("CREATION"))
			{
				GetPackagesList().get(i).ChangeStatus(Status.COLLECTION);
				synchronized(this) {
				collectPackage(GetPackagesList().remove(i));
				}
			}
			else if(GetPackagesList().get(i).equals("DELIVERY"))
			{
				GetPackagesList().get(i).ChangeStatus(Status.DISTRIBUTION);
				synchronized(this) {
				deliverPackage(GetPackagesList().remove(i));
				}
			}
		}
		
		for (int i = 0; i < GetTruckList().size(); i++)
			GetTruckList().get(i).work();
	}
	
	/**
	 * Function GetHubX returns hub's x middle position
	 * @return int hubx
	 */
	public double GetHubX() {
		return hubx;
	}
	
	/**
	 * Function GetHubY returns hub's y middle position
	 * @return int huby
	 */
	public double GetHubY() {
		return huby;
	}
	
	/**
	 * Function DrawBranch draws all the branches of the office
	 * @param g Graphics parameter
	 * @param width the branch width
	 * @param height the branch height
	 */
	public void DrawBranch(Graphics g, int width, int height) {
//		synchronized(this) {
		Graphics2D g2d = (Graphics2D) g;
		
		int branch_number = Hub.branches.size() - 1;
		int id = Hub.branches.indexOf(this) + 1;
		double middle;
		double xstart = 20, ystart;
		double space = (int)(450 / branch_number);
		
		middle = (branch_number / 2) + 1;
		ystart = (int)(280 +((id - middle) * space));	
		
		
		xmiddle = width / 2 + xstart;
		ymiddle = height / 2 + ystart;
		
		if(listPackages.size() == 0)
			g2d.setColor(Color.cyan);
		else
			g2d.setColor(Color.blue);
		
		g2d.fill(new Rectangle2D.Double(xstart, ystart, width, height));
	}
//	}
	
	public void DrawPath(Graphics g, int xhub, int yhub) {
		
		Graphics2D g2d = (Graphics2D) g;
		
		int branch_number = BranchesList.size() - 1;
		double xstart = xmiddle + 20, ystart = ymiddle, xend = 1200 - 60, yend;
		
		yend = 200 + ((180 / branch_number) * (branchId + 1));
		hubx = xend;
		huby = yend;
		g2d.setColor(new Color(0, 150, 0));
		g2d.draw(new Line2D.Double(xstart, ystart, xend, yend));
	}
	
	/**
	 * Function GeXMiddle returns current brunch x middle position
	 * @return int xmiddle
	 */
	public double GetXMiddle() {
		return xmiddle;
	}
	
	/**
	 * Function GeYMiddle returns current brunch y middle position
	 * @return int ymiddle
	 */
	public double GetYMiddle() {
		return ymiddle;
	}

	/**
	 * Function run starts the branches work
	 */
	@Override
	public void run() {
		
		
		for(Truck van : GetTruckList())
		{
			Thread t = new Thread((Runnable)van);
			t.start();
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
				work();
		}
	}
	
	/**
	 * Function SetSuspend suspends the branch thread
	 */
	@Override
	public synchronized void setSuspend(){
		for(Truck truck : GetTruckList())
			truck.setSuspend();
		threadSuspend = true;
	}
	
	/**
	 * Function SetResume resumes the branch thread
	 */
	@Override
	public synchronized void setResume() {
		for(Truck truck : GetTruckList())
			truck.setResume();
		threadSuspend = false;
		notifyAll();
	}
	
	/**
	 * Function SetStop stops the branch thread
	 */
	public void setStop() {
		for(Truck truck : GetTruckList())
			truck.setStop();
		stop = true;
	}
	
	/**
	 * function to clone a branch
	 */
	public Branch clone() {
		Branch b = new Branch();
		for(Truck v : GetTruckList())
			b.listTrucks.add(new Van());
		
		return b;
	}
	
	/**
	 * function to set branch list
	 * @param b
	 */
	public static void SetStaticBranchesList(ArrayList<Branch> b) {
		BranchesList = b;
	}
	
	/**
	 * function to branch list
	 * 
	 */
	public ArrayList <Branch> GetStaticBranchesList() {
		 return BranchesList;
	}
	
	/**
	 * function to return thread suspend
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
	 * function to deacrese branch id
	 * 
	 */
	public static void DecreaseBranchId() {
		branchNum -= 1;
	}
	
}








