package components;

import java.awt.Graphics;
import java.util.ArrayList;
 /**
  * 
  * @author Nadav Ishai, ID: 206119893
  * abstract class Truck 
  */
public abstract class Truck implements Node, Runnable {

	private static int truckNum = 2000;
	private int truckID;
	private String licensePlate;
	private String truckModel;
	private boolean available;
	private int timeLeft;
	private int savetime;
	private ArrayList<Package> packages;
	private boolean threadSuspend = false;
	private boolean stop;

	public Truck() {
		truckID = truckNum;
		truckNum += 1;
		int rand = (int) (Math.random() * 900) + 100;
		licensePlate = rand + "-";
		rand = (int) (Math.random() * 90) + 10;
		licensePlate += rand + "-";
		rand = (int) (Math.random() * 900) + 100; 
		licensePlate += rand;
		truckModel = "M" + (int) (Math.random() * 5);
		available = true;
		timeLeft = 0;
		savetime = 0;
		packages = new ArrayList<Package>();
	}

	public Truck(String licensePlate, String truckModel) {
		truckID = truckNum;
		truckNum += 1;
		this.licensePlate = licensePlate;
		this.truckModel = truckModel;
		available = true;
		timeLeft = 0;
		savetime = 0;
		packages = new ArrayList<Package>();
		stop  = false;
	}
	
	public Truck(Truck t) {
		
		truckID = t.truckID;
		licensePlate = t.GetLicensePlate();
		truckModel = t.GetTruckModel();
		available = t.GetAavilableStatus();
		timeLeft = t.GetTimeLeft();
		savetime = t.GetSaveTime(); 
		
		packages = new ArrayList<Package>();
		for(Package p : t.packages)
		{
			if(p instanceof SmallPackage)
				packages.add(new SmallPackage(p));
			
			else if (p instanceof StandardPackage)
				packages.add(new StandardPackage(p));
			
			else if (p instanceof NonStandardPackage)
				packages.add(new NonStandardPackage(p));
		}
		threadSuspend = t.GetThreadSuspend();
		stop = t.GetStop();
	}

	/**
	 * Function GetAvailableStatus return if the truck is available or not
	 * @return true ot false
	 */
	public boolean GetAavilableStatus() {
		return available;
	}
	
	/**
	 * Function DecreaseTimeLeft decrease driving time of a truck by 1 
	 */
	public void DecreaseTimeLeft() {
		timeLeft -= 1;
	}

	/**
	 * Function SetToAvailable set availability of truck to available
	 */
	public void SetToAvailable() {
		available = true;
	}
	
	/**
	 * Function SetToNoAvailable set availability of truck to not available
	 */
	public void SetToNoAvailable() {
		available = false;
	}

	/**
	 * Function GetTimeLeft return truck time left
	 * @return timeLeft
	 */
	public int GetTimeLeft() {
		return timeLeft;
	}

	/**
	 * Function GetPackagesList return truck packages list
	 * @return packages
	 */
	public ArrayList<Package> GetPackagesList() {
		return packages;
	}
	
	/**
	 * Truck toString function
	 */
	public String toString() {
		return "[truckID="+truckID+", licensePlate = "+licensePlate+", truckModel = "+truckModel+", available = "+available;
	}

	/**
	 * Function GetTruckId return truck id number
	 * @return truckID
	 */
	public int GetTruckId() {
		return truckID;
	}
	
	/**
	 * Function SetTimeLeft set the time left of a truck 
	 * @param t = set time of truck to t
	 */
	public void SetTimeLeft(int t) {
		timeLeft = t;
	}
	
	/**
	 * Function GetLicensePlate return truck license plate
	 * @return licensePlate
	 */
	public String GetLicensePlate() {
		return licensePlate;
	}
	
	/**
	 * Function GetTruckModel return truck model 
	 * @return truckModel
	 */
	public String GetTruckModel() {
		return truckModel;
	}
	
	/**
	 * Function GetThreadSuspend returns thread suspend boolean value
	 * @return boolean threadSuspend
	 */
	public boolean GetThreadSuspend() {
		return threadSuspend;
	}
	
	/**
	 * Function GetStop returns thread stop boolean value
	 * @return boolean stop
	 */
	public boolean GetStop() {
		return stop;
	}
	
	/**
	 * Function SetSaveTime saves the truck beginning riding time
	 * @param t the beginning time
	 */
	public void SetSaveTime(int t) {
		savetime = t;
	}
	
	/**
	 * Function GetSaveTime return the truck beginning riding time
	 * @return int savetime
	 */
	public int GetSaveTime() {
		return savetime;
	}
	
	public abstract String GetTruckName();
	public abstract String GetTruckType();
	public abstract void collectPackage(Package p);
	public abstract void deliverPackage(Package p);
	public abstract void work();
	public abstract void run();
	public abstract void DrawTruck(Graphics g, Hub hub);
	
	/**
	 * Function setSuspend suspends the truck thread
	 */
	@Override
	public synchronized void setSuspend(){
		threadSuspend = true;
	}
	
	/**
	 * Function setResume resumes the truck thread
	 */
	@Override
	public synchronized void setResume() {
		threadSuspend = false;
		notifyAll();
	}
	
	/**
	 * Function setStop stops the truck thread
	 */
	public void setStop() {
		stop = true;
	}
}
