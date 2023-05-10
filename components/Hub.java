package components;

import java.awt.Color;
import java.awt.Graphics;
import java.io.IOException;
import java.util.ArrayList;

/**
 * 
 * @author Nadav Ishai, ID: 206119893
 * class Hub operate the branches, packages and trucks
 */
public class Hub extends Branch{
	
	protected static ArrayList <Branch> branches;
	private ArrayList <Branch> mementobranches = new ArrayList<Branch>();
	private static int count = 0;
	
	Hub(){
		super();
		branches = new ArrayList <Branch>();
	}
	
	
	public Hub(Hub hub, boolean flag) {
		super(hub, flag);
//		synchronized(this) {
		
		listTrucks = new ArrayList<Truck>();
		for(Truck t : hub.GetTruckList())
		{
			if (t instanceof StandardTruck)
				listTrucks.add(new StandardTruck(t));
			
			else if (t instanceof NonStandardTruck)
				listTrucks.add(new NonStandardTruck(t));
		}
		
		
		if(!flag)
		{	
			mementobranches = new ArrayList<Branch>();
			for(Branch b : hub.GetBranchesList())
				mementobranches.add(new Branch(b));
		}
		else
		{
			System.out.printf("branches size before = %d --------------------------\n", branches.size());
			branches = new ArrayList<Branch>();
			for(Branch b : hub.mementobranches)
				branches.add(new Branch(b));
			
			System.out.printf("branches size after = %d --------------------------\n", branches.size());

	
		}
		
		listPackages = new ArrayList <Package>();
		for(Package p : hub.listPackages)
		{
			if(p instanceof SmallPackage)
				listPackages.add(new SmallPackage(p));
			
			if(p instanceof StandardPackage)
				listPackages.add(new StandardPackage(p));
			
			else if(p instanceof NonStandardPackage)
				listPackages.add(new NonStandardPackage(p));
		}
		
		count = hub.count;
		
		threadSuspend = hub.GetThreadSuspend();
		stop = hub.GetStop();
	} 
	
	/**
	 * Function AddHubTrucks adding standard truck and one nonstandard truck to hub
	 * @param size
	 */
	public void AddHubTrucks(int size) {
		for(int i = 0; i < size; i++)
			this.AddStandardTruck();
		this.AddNonStandardTruck();
	}
	
	/**
	 * Function AddBranchVans adding vans to all the branches 
	 * @param size = number of branches to create
	 * @param vansize = number of vans to add for each branch  
	 */
	public void AddBranchVans(int size, int vansize) {
		for(int i = 1; i <= size; i++)
		{
			Branch b = new Branch();
			b.AddVan(vansize);
			branches.add(b);
			mementobranches.add(b);
			System.out.println();
		}
	}
	
	/**
	 * GetBranchesList GetBranchesList return the branches list
	 * @return branches
	 */
	public static ArrayList<Branch> GetBranchesList(){
		return branches;
	}
	
	/**
	 * Function RemovePackageFromBranch remove a package from branch
	 * @param p = package to remove
	 */
	public static void RemovePackageFromBranch(Package p) {
		int branchnum = 0;
		if(p.equals("DELIVERED"))
			branchnum = p.GetDestinationAddress().Get_Zip();
			
		for(int i = 0; i < branches.get(branchnum).GetPackagesList().size(); i++)
			if(branches.get(branchnum).GetPackagesList().get(i).equals(p))
				branches.get(branchnum).GetPackagesList().remove(i);
	}
	
	/**
	 * Function IncreaseCount increasing the count to send all the truck to the branches
	 */
	public static void IncreaseCount() {
		count++;
	}
	
	/**
	 * Function AddPackageToBranch add a package to branch
	 * @param p = package to add
	 */
	public static void AddPackageToBranch(Package p) {
		int branchnum = 0;
		
		if(p.equals("BRANCH_STORAGE"))
			branchnum = p.GetSenderAddress().Get_Zip();
			
		branches.get(branchnum).GetPackagesList().add(p);
	}
	
	/**
	 * Function GetBranch return a branch
	 * @param id = branch id to return 
	 * @return branch(id)
	 */
	public static Branch GetBranch(int id){
		return branches.get(id);
	}
	
	/**
	 * Function GetBranchName return the branch name
	 */
	public String GetBranchName() {
		return "HUB";
	}
	
	/**
	 * Function LoadStansardTruckAtHub loading to an available standard truck packages to a branch destination
	 * @param st = the truck to load the packages to
	 * @param count = branch id
	 */
	public void LoadStansardTruckAtHub(StandardTruck st, int count) {
		double weight = 0;
		for(int i = 0; i < GetTruckList().size(); i++)
		{
			if(GetTruckList().get(i).equals(st))
			{
				for(int j = 0; j < GetPackagesList().size(); j++)
				{	
					if(GetPackagesList().get(j).GetSenderAddress().Get_Zip() == count)
					{
						if(GetPackagesList().get(j) instanceof StandardPackage || GetPackagesList().get(j) instanceof SmallPackage)
						{		//standard package
							if(GetPackagesList().get(j) instanceof StandardPackage)
									weight = ((StandardPackage)GetPackagesList().get(j)).GetPackageWeight();
							else	//small package
								weight = 1;
								//check the maximum weight
							if(((StandardTruck)GetTruckList().get(i)).GetCurrentWeight() + weight <= ((StandardTruck)GetTruckList().get(i)).GetMaxWeight())
							{
								GetPackagesList().get(j).ChangeStatus(Status.BRANCH_TRANSPORT);
								GetPackagesList().get(j).addTracking(GetTruckList().get(i), Status.BRANCH_TRANSPORT);
								synchronized(this) {
									GetTruckList().get(i).GetPackagesList().add(GetPackagesList().get(j));
									GetPackagesList().remove(j);
								}
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * Function work perform one unit work and run all the standard trucks, nonstandard trucks and all the branches work function
	 */
	public synchronized void work() {
			//all the trucks
		for(int i = 0; i < GetTruckList().size(); i++)
		{		//available and standard truck
			
			if(GetTruckList().get(i).GetAavilableStatus() && GetTruckList().get(i) instanceof StandardTruck)
			{					
					LoadStansardTruckAtHub((StandardTruck)GetTruckList().get(i), count % Hub.branches.size()); 
					System.out.printf("StandardTruck %d loaded packages at HUB\n", GetTruckList().get(i).GetTruckId());
					((StandardTruck)GetTruckList().get(i)).SetDestination(Hub.GetBranch(count % Hub.branches.size()));
					((StandardTruck)GetTruckList().get(i)).SetExitBranch(Hub.GetBranch(count % Hub.branches.size()));
					GetTruckList().get(i).SetToNoAvailable();
					int time = ((int)(Math.random() * 10 + 1)) * 10;
					GetTruckList().get(i).SetTimeLeft(time);
					GetTruckList().get(i).SetSaveTime(time);
					System.out.printf("StandardTruck %d is on it's way to Branch %d, time to arrive: %d\n", GetTruckList().get(i).GetTruckId(), count % Hub.branches.size(), time);
					IncreaseCount();
			}
			if(GetTruckList().get(i).GetAavilableStatus() && GetTruckList().get(i) instanceof NonStandardTruck)
			{
				for(int j = 0; j < GetPackagesList().size(); j++)
				{
					if(GetPackagesList().get(j) instanceof NonStandardPackage)
					{
						int timetoarrive = 0;
						GetPackagesList().get(j).ChangeStatus(Status.COLLECTION);
						GetPackagesList().get(j).addTracking(GetTruckList().get(i), Status.COLLECTION); 
						synchronized(this) {
						GetTruckList().get(i).GetPackagesList().add(GetPackagesList().remove(j));
						}
						timetoarrive = ((Math.abs(GetTruckList().get(i).GetPackagesList().get(0).GetSenderAddress().Get_Zip() - GetTruckList().get(i).GetPackagesList().get(0).GetDestinationAddress().Get_Zip()) % 10) + 1) * 10;
						GetTruckList().get(i).SetToNoAvailable();
						GetTruckList().get(i).SetTimeLeft(timetoarrive);
						GetTruckList().get(i).SetSaveTime(timetoarrive);
						System.out.printf("NonStandardTruck %d is collecting package %d, time to arrive: %d\n", GetTruckList().get(i).GetTruckId(), GetTruckList().get(i).GetPackagesList().get(0).GetPackageId(), timetoarrive);
						break;
					}	
				}
			}
		}
		for(int i = 0; i < GetBranchesList().size(); i++)
			GetBranchesList().get(i).work();
		for(int i = 0; i < GetTruckList().size(); i++)
			GetTruckList().get(i).work();
	}
	
	public void DrawHub(Graphics g, int width, int height) {
//		synchronized(this) {
		int xstart = 1140, ystart = 300 - (height / 2);
		
		int [] xpoints = {xstart, xstart + width, xstart + width, xstart};
		int [] ypoints = {ystart, ystart, ystart + height, ystart + height};
	
		Color clr = new Color(0, 150, 0);
		g.setColor(clr);
		g.fillPolygon(xpoints, ypoints, 4);
//		}
	}
	
	/**
	 * Function run starts the hub work
	 */
	@Override
	public void run() {
		
		for(Branch b: GetBranchesList())
		{
			Thread t = new Thread((Runnable)b);
			t.start();
		}
		
		for(Truck tr : GetTruckList())
		{
			Thread t = new Thread((Runnable)tr);
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
	 * Function SetSuspend suspends the hub thread
	 */
	@Override
	public synchronized void setSuspend() {
		for(Branch branch : GetBranchesList())
			branch.setSuspend();
		for(Truck truck : GetTruckList())
			truck.setSuspend();
		threadSuspend = true;
	}
	
	/**
	 * Function SetResume resumes the hub thread
	 */
	@Override
	public synchronized void setResume() {
		for(Branch branch : GetBranchesList())
			branch.setResume();
		for(Truck truck : GetTruckList())
			truck.setResume();
		threadSuspend = false;
		notifyAll();
	}
	
	/**
	 * Function SetStop stops the hub thread
	 */
	public void setStop() {
		for(Branch branch : GetBranchesList())
			branch.setStop();
		for(Truck truck : GetTruckList())
			truck.setStop();
		stop = true;
	}
	
	
	/**
	 * function to clone a branch
	 * @param b
	 */
	public void AddClonedBranch(Branch b) {
		branches.add(b);
//		mementobranches.add(b);
	}

	
	/**
	 * function to return mementobranches
	 * @return mementobranches
	 */
	public ArrayList<Branch> GetMementobranches(){
		return mementobranches;
	}

	public void collectPackage(Package p) {}
	public void deliverPackage(Package p) {}
}





	
	
	
