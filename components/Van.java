package components;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Arc2D;
import java.awt.geom.Rectangle2D;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

/**
 * 
 * @author Nadav Ishai, ID: 206119893
 * class Van takes small and standard packages from customer to post office and from post office to recipient 
 */
public class Van extends Truck {


	public Van() {
		super();
		System.out.println("Creating " + this);
	}

	public Van(String licensePlate, String truckModel) {
		super(licensePlate, truckModel);
	}
	
	public Van(Truck t) {
		super(t);
	}
	
	/**
	 * Van toString function 
	 */
	public String toString() {
		return "Van " + super.toString() + "]";
	}
	
	/**
	 * Function GetTruckName return Van name
	 */
	public String GetTruckName() {
		return "Van " + GetTruckId();
	}
	
	/**
	 * Function GetTruckType return Truck type
	 */
	public String GetTruckType() {
		return "Van";
	}

	/**
	 * Function work decrease the driving time of the van and perform one work unit of it
	 */
	public synchronized void work() {
		if (!(GetAavilableStatus())) {
			if (GetTimeLeft() == 0) {
					if (((GetPackagesList()).get(0).GetStatus()).compareTo("COLLECTION") == 0)// pick up from sender
						collectPackage(GetPackagesList().get(0));
					 else 
						if (((GetPackagesList()).get(0).GetStatus()).compareTo("DISTRIBUTION") == 0)
							deliverPackage(GetPackagesList().get(0));
			}
			DecreaseTimeLeft();
		}
	}

	/**
	 * Function collectPackage collect a package from a customer and bring it to the post office
	 */
	public void collectPackage(Package p) {
				int branchid = p.GetSenderAddress().Get_Zip();
				GetPackagesList().get(0).ChangeStatus(Status.BRANCH_STORAGE);
				GetPackagesList().get(0).addTracking(Hub.GetBranch(branchid), Status.BRANCH_STORAGE);
				System.out.printf("Van %d has collected package %d and arrived back to branch %d\n",GetTruckId(), (GetPackagesList()).get(0).GetPackageId(),(GetPackagesList()).get(0).GetSenderAddress().Get_Zip());
				synchronized(this) {
				Hub.AddPackageToBranch(GetPackagesList().remove(0));
				}
				SetToAvailable();
	}
	
	/**
	 * Function deliverPackage collect a package from a post office and deliver it to the customer
	 */
	public void deliverPackage(Package p) {
			GetPackagesList().get(0).ChangeStatus(Status.DELIVERED);
			GetPackagesList().get(0).addTracking(null, Status.DELIVERED);
			System.out.printf("Van %d has delivered package %d to the destination\n",GetTruckId(), p.GetPackageId());
			if(p.getClass().getSimpleName().compareTo("SmallPackage") == 0)
				if(((SmallPackage)p).GetAcknowledge())
					System.out.printf("acknowledge = true: the package delivered successfully to customer\n");
			synchronized(this){
			Hub.RemovePackageFromBranch(GetPackagesList().remove(0));
			}
			
	}
	
	/**
	 * Function run starts the van thread
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
	 * Function DrawTruck draws all the vans in the mainoffice
	 */
	public void DrawTruck(Graphics g, Hub hub) {
//		synchronized(this) {
		Graphics2D g2d = (Graphics2D) g;
		
		Branch branch = null;
		
		if(GetPackagesList().get(0).GetStatus().equals("COLLECTION"))
			branch = hub.GetBranch(GetPackagesList().get(0).GetSenderAddress().Get_Zip());
		else if(GetPackagesList().get(0).GetStatus().equals("DISTRIBUTION"))
			branch = hub.GetBranch(GetPackagesList().get(0).GetDestinationAddress().Get_Zip());
		
		if (branch != null)
		{
			double vanxstart = branch.GetXMiddle() - 8;
			double vanystart = 0;
			
			if(GetPackagesList().get(0).GetStatus().equals("COLLECTION"))
				vanystart = branch.GetYMiddle() - 23;
			else if(GetPackagesList().get(0).GetStatus().equals("DISTRIBUTION"))
				vanystart = branch.GetYMiddle() + 7;
			
			double mul = GetSaveTime() - GetTimeLeft();
			
			double totalx = GetPackagesList().get(0).GetSenderXLocation() - branch.GetXMiddle();
			double newx = vanxstart + ((totalx / GetSaveTime()) * mul);
			
			double totaly = 0;
			double newy = 0;
			
			if(GetPackagesList().get(0).GetStatus().equals("COLLECTION"))
			{
				totaly = (branch.GetYMiddle() - 15) - (GetPackagesList().get(0).GetSenderYLocation() + 10);
				newy = vanystart - ((totaly / GetSaveTime()) * mul);
			}
			else if(GetPackagesList().get(0).GetStatus().equals("DISTRIBUTION"))
			{
				totaly = (GetPackagesList().get(0).GetDestinationYLocation()) - (branch.GetYMiddle() + 7);
				newy = vanystart + ((totaly / GetSaveTime()) * mul);
			}
			
			g2d.setColor(Color.blue);
			g2d.fill(new Rectangle2D.Double(newx, newy, 16, 16));
			
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
//		}
	}	
}
