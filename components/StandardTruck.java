package components;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Arc2D;
import java.awt.geom.Rectangle2D;

/**
 * 
 * @author Nadav Ishai, ID: 206119893
 * class StandardTruck take small and standard packages from sender post office to hub and from hub to destination post office
 */
public class StandardTruck extends Truck{

	private int maxWeight;
	private double currentWeight;
	private Branch destination;
	private Branch exitbranch;
	
	
	public StandardTruck() {
		super();
		maxWeight = (int)(Math.random() * 21 +20);
		currentWeight = 0;
		destination = null;
		exitbranch = null;
		System.out.println("Creating " + this);
	}
	
	public StandardTruck(String licensePlate,String truckModel,int maxWeight) {
		super(licensePlate, truckModel);
		this.maxWeight = maxWeight;
		currentWeight = 0;
		destination = null;
		exitbranch = null;
	}
	
	public StandardTruck(Truck t) {
		super(t);
		maxWeight = ((StandardTruck)t).maxWeight;
		currentWeight = ((StandardTruck)t).currentWeight;
		destination = ((StandardTruck)t).GetDestination();
		exitbranch = ((StandardTruck)t).GetExitBranch();
	}
	
	/**
	 * StandardTruck toString function
	 */
	public String toString() {
		return "StandardTruck " + super.toString() + ", maxWeight = "+maxWeight+"]";
	}
	
	/**
	 * Function GetMaxWeight return standard truck max weight
	 * @return maxWeight
	 */
	public int GetMaxWeight() {
		return maxWeight;
	}
	
	/**
	 * Function GetTruckType return truck type
	 */
	public String GetTruckType() {
		return "StandardTruck";
	}
	
	/**
	 * Function UpdateCurrentWeight update standard truck current weight 
	 * @param weight = weight to add to the current weight
	 */
	public void UpdateCurrentWeight(int weight) {
		currentWeight += weight;
	}
	
	/**
	 * Function MinusPackageWeight reduce the current weight
	 * @param w = reduce current weight by w
	 */
	public void MinusPackageWeight(double w) {
		currentWeight -= w;
	}
	
	/**
	 * Function GetCurrentWeight return current weight
	 * @return currentWeight
	 */
	public double GetCurrentWeight() {
		return currentWeight;
	}
	
	/**
	 * Function SetDestination set standard truck new destination 
	 * @param b = Branch to take the new destination
	 */
	public void SetDestination(Branch b) {
		destination = Hub.GetBranch(b.GetBranchId());
	}
	
	/**
	 * Function GetDestination return standard truck destination
	 * @return destination
	 */
	public Branch GetDestination() {
		return destination;
	}
	
	public Branch GetExitBranch() {
		return exitbranch;
	}
	
	/**
	 * Function GetTruckName return truck name
	 */
	public String GetTruckName() {
		return "StandardTruck " + GetTruckId();
	}
	
	public void SetExitBranch(Branch branch) {
		exitbranch = branch;
	}
	
	/**
	 * Function deliverPackage deliver packages from the the hub to the destination post office - 
	 * take small and standard packages from sender post office to hub and from hub to destination post office
	 */
	public void deliverPackage(Package p) {
		for(int i = 0; i < GetPackagesList().size(); i++)
		{
			if(GetPackagesList().get(i).equals(p))
			{
				GetPackagesList().get(i).ChangeStatus(Status.DELIVERY);
				GetPackagesList().get(i).addTracking(Hub.GetBranch(p.GetDestinationAddress().Get_Zip()), Status.DELIVERY);
				Hub.GetBranch(p.GetDestinationAddress().Get_Zip()).GetPackagesList().add(GetPackagesList().get(i));
			}	
		}
	}
	
	/**
	 * Function work decrease StandardTruck driving time and perform one work unit
	 */
	public synchronized void work() {
		
		if(!(GetAavilableStatus()))
		{
			if(this.GetTimeLeft() == 0)
			{
				System.out.printf("StandardTruck %d arrived to %s\n", this.GetTruckId(), destination.GetBranchName());
				if(destination instanceof Branch && !(destination instanceof Hub))	//unloading and loading packages at branch
				{	
					for (int i = 0; i < GetPackagesList().size(); i++) 
					{
						if(GetPackagesList().get(i).equals("BRANCH_TRANSPORT"))
						{
							deliverPackage(GetPackagesList().get(i));
							if(GetPackagesList().get(i) instanceof StandardPackage)
								MinusPackageWeight(((StandardPackage)GetPackagesList().get(i)).GetPackageWeight());
							else
								MinusPackageWeight(1);
							GetPackagesList().remove(i);
						}
					}
					System.out.printf("StandardTruck %d unloaded packages at %s \n", GetTruckId(), destination.GetBranchName());
					
					for (int i = 0; i < destination.GetPackagesList().size(); i++) {
						if(destination.GetPackagesList().get(i).equals("BRANCH_STORAGE"))
						{
							destination.GetPackagesList().get(i).ChangeStatus(Status.HUB_TRANSPORT);
							destination.GetPackagesList().get(i).addTracking(this, Status.HUB_TRANSPORT);
							GetPackagesList().add(destination.GetPackagesList().get(i));
							destination.GetPackagesList().remove(i);
						}
					}
					System.out.printf("StandardTruck %d loaded packages at %s\n", GetTruckId(), destination.GetBranchName());
					int time = (int)(Math.random () * 6 +1) * 10;
					SetTimeLeft(time);
					SetSaveTime(time);
					System.out.printf("StandardTruck %d is on it's way to the HUB, time to arrive: %d\n", GetTruckId(), time);
					destination = MainOffice.GetHub();
				}
				else	//unloading packages at hub
				{
					for (int i = 0; i < GetPackagesList().size(); i++) 
					{
						GetPackagesList().get(i).ChangeStatus(Status.HUB_STORAGE);
						GetPackagesList().get(i).addTracking(MainOffice.GetHub(), Status.HUB_STORAGE);
						MainOffice.GetHub().GetPackagesList().add(GetPackagesList().get(i));
						GetPackagesList().remove(i);
					}
					System.out.printf("StandardTruck %d unloaded packages at HUB\n", GetTruckId());
					SetToAvailable();
				}
			}
			this.DecreaseTimeLeft();
		}
	}
	
	public void collectPackage(Package p) {}
	
	/**
	 * Function run starts the standardtruck work
	 */
	@Override
	public void run() {
		while(true) {
				
			try {
				Thread.sleep(500);
				synchronized(this) {
	                while (GetThreadSuspend())
	                	wait();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (GetStop()) return;
			work();
		}
	}
	
	/**
	 * Function DrawTruck draws all the standardtrucks in the mainoffice
	 */
	public void DrawTruck(Graphics g, Hub hub) {
//		synchronized(this) {
		Graphics2D g2d = (Graphics2D) g;
		
		double trucxstart = 0, trucystart = 0;
		double totalx = 0, totaly = 0;
		double newx = 0, newy = 0;
		
		Branch branch = null; 
		
		if(destination instanceof Hub)
			branch = exitbranch;
		else
			branch = destination;
		
		double branchxmiddle = branch.GetXMiddle();
		double branchymiddle = branch.GetYMiddle();
		
		if(destination instanceof Hub)
		{
			trucxstart = branchxmiddle - 5;
			trucystart = branchymiddle - 9;
		}
		else if(destination instanceof Branch)
		{
			trucxstart = branch.GetHubX() - 7;
			trucystart = branch.GetHubY() - 8;
		}
			
		totalx = (branch.GetHubX() - 5) - (branchxmiddle+20);
		totaly = Math.abs(branch.GetYMiddle() - branch.GetHubY());

		double mul = GetSaveTime() - GetTimeLeft();
		
		if(destination instanceof Hub)
		{
			newx = trucxstart + ((totalx / GetSaveTime()) * mul);
			
			if(branchymiddle > branch.GetHubY())
				newy = trucystart - ((totaly / GetSaveTime()) * mul);
			else
				newy = trucystart + ((totaly / GetSaveTime()) * mul);
		}
			
		else
		{
			newx = trucxstart - ((totalx / GetSaveTime()) * mul);
			
			if(branchymiddle > branch.GetHubY())
				newy = trucystart + ((totaly / GetSaveTime()) * mul);
			else
				newy = trucystart - ((totaly / GetSaveTime()) * mul);
		}
			
		if(GetPackagesList().size() == 0)
		{
			g2d.setColor(Color.green);
			g2d.fill(new Rectangle2D.Double(newx, newy, 16, 16));
		}
		else
		{
			g2d.setColor(new Color(0, 150, 0));
			g2d.fill(new Rectangle2D.Double(newx, newy, 16, 16));
			g2d.setColor(Color.black);
			g2d.drawString(Integer.toString(GetPackagesList().size()), (float)newx+4, (float)newy-5);
		}
			
		g2d.setColor(Color.black);
		double ovalxstart = newx - 5;	//top left
		double ovalystart = newy - 5;
		g2d.fill(new Arc2D.Double(ovalxstart, ovalystart, 10.0, 10.0, 0.0, 360.0, 1));
		
		ovalxstart = newx + 11;	//top right
		ovalystart = newy - 5;
		g2d.fill(new Arc2D.Double(ovalxstart, ovalystart, 10.0, 10.0, 0.0, 360.0, 1));
		
		ovalxstart = newx + 11;	//bottom right
		ovalystart = newy + 11;
		g2d.fill(new Arc2D.Double(ovalxstart, ovalystart, 10.0, 10.0, 0.0, 360.0, 1));
		
		ovalxstart = newx - 5;	//bottom left
		ovalystart = newy + 11;
		g2d.fill(new Arc2D.Double(ovalxstart, ovalystart, 10.0, 10.0, 0.0, 360.0, 1));
	}
//	}
}





