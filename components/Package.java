package components;

import java.awt.Graphics;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;

/**
 * 
 * @author Nadav Ishai, ID: 206119893
 * abstract class Package 
 */
public abstract class Package{
	private static int packNum = 1000;
	private int packageID;
	private Priority priority;
	private Status status;
	private Address senderAddress;
	private Address destinationAddress;
	private ArrayList<Tracking> tracking;
	private double senderxlocation = 0;
	private double senderylocation = 0;	
	private double destinationylocation = 0;
	private static int statusid = 1;
	private int customerid;

	Package(Priority priority, Address senderAddress, Address destinationAdress, int customerid) {
		packageID = packNum;
		packNum += 1;
		this.priority = priority;
		status = Status.CREATION;
		this.senderAddress = new Address(senderAddress.Get_Zip(), senderAddress.Get_Street());
		this.destinationAddress = new Address(destinationAdress.Get_Zip(), destinationAdress.Get_Street());
		tracking = new ArrayList<Tracking>();
		tracking.add(new Tracking(MainOffice.GetClock(), null, Status.CREATION));
		this.customerid = customerid;
	}

	public Package(Package p) {
		this.packageID = p.GetPackageId();
		this.priority = p.GetPriority();
		this.status = p.GetRealStatus();
		this.senderAddress = p.GetSenderAddress();
		this.destinationAddress = p.GetDestinationAddress();
		senderxlocation = p.GetSenderXLocation();
		senderylocation = p.GetSenderYLocation();	
		this.destinationylocation = GetDestinationYLocation();
		this.tracking = new ArrayList<Tracking>();
		for (int i = 0; i < tracking.size(); i++)
			this.tracking.set(i, new Tracking(p.tracking.get(i)));
		customerid = p.customerid;
	}
	
	/**
	 * abstract class Package toString
	 * 
	 */
	public String toString() {
		return "[packageID="+packageID+", priority="+priority+", status="+status+", startTime=, senderAddress="+senderAddress.Get_Address()+", destinationAddress="+destinationAddress.Get_Address()+", ";
	}
	
	/**
	 * Function equals check the equality between 2 packages statuses
	 * @param s = other package
	 * @return true or false 
	 */
	public boolean equals(String s) {
		if(this.GetStatus().compareTo(s) == 0)
			return true;
		return false;
	}
	
	/**
	 * Function equals check the equality between 2 packages by the ID
	 * @param s = other package
	 * @return true or false 
	 */
	public boolean equals(Package s) {
		if(this.GetPackageId() == s.packageID)
			return true;
		return false;
	}

	/**
	 * Function addTracking add tracking to a package 
	 * @param node = location of the package
	 * @param status = status of the package
	 */
	void addTracking(Node node, Status status) {
		Tracking t = new Tracking(MainOffice.GetClock(), node, status); 
		tracking.add(t);
	}

	/**
	 * Function GetTrackingList return package tracking list
	 * @return tracking
	 */
	public ArrayList<Tracking> GetTrackingList() {
		return tracking;
	}

	/**
	 * Function GetStatus return package status as String 
	 * @return
	 */
	public String GetStatus() {
		return status.GetStatus();
	}
	
	/**
	 * Function PrintTracking prints all the tracking of a package
	 */
	public void PrintTracking() {
		for (int i = 0; i < tracking.size(); i++)
			System.out.print(tracking.get(i));
	}

	/**
	 * Function ChangeStatus changes package status
	 * @param s = change to s status
	 */
	public void ChangeStatus(Status s) {
		status = s;
		String str;
				
		str = statusid + ". " + "Package number: " + packageID + ", status = " + s + ", Customer = " + customerid + "\n";
		statusid += 1;
		components.MainOffice.Write(str);
	}
	
	/**
	 * Function GetPackageId return package id 
	 * @return packageId
	 */
	public int GetPackageId() {
		return packageID;
	}

	/**
	 * Function GetSenderAddress return sender address
	 * @return senderAddress
	 */
	public Address GetSenderAddress() {
		return senderAddress;
	}

	/**
	 * Function GetDestinationAddress return destination address
	 * @return destinationAddress
	 */
	public Address GetDestinationAddress() {
		return destinationAddress;
	}

	/**
	 * Function GetPriority return package priority
	 * @return priority
	 */
	public Priority GetPriority() {
		return priority;
	}
	
	public abstract String GetPackageType();
	
	/**
	 * Function SetSenderLocation sets the x & y coordinates of a sender
	 * @param x the x position of the sender
	 * @param y the y position of the sender
	 */
	public void SetSenderLocation(double x, double y) {
		senderxlocation = x;
		senderylocation = y;
	}
	
	/**
	 * Function GetSenderXLocation return the x position of sender
	 * @return int senderxlocation
	 */
	public double GetSenderXLocation() {
		return senderxlocation;
	}
	
	/**
	 * Function GetSenderYLocation returns the sender y position
	 * @return int senderylocation
	 */
	public double GetSenderYLocation() {
		return senderylocation;
	}
	
	/**
	 * Function SetDestinationYLocation sets the destination y position
	 * @param y the y position of the destination
	 */
	public void SetDestinationYLocation(double y) {
		destinationylocation = y;
	}
	
	/**
	 * Function GetDestinationYLocation return the destination y position
	 * @return int destinationylocation
	 */
	public double GetDestinationYLocation() {
		return destinationylocation;
	}	
	
	/**
	 * function to decrease id
	 * @param num
	 */
	public static void DecreaseId(int num) {
		packNum -= num;
	}
	
	/**
	 * function to return status;
	 */
	public Status GetRealStatus() {
		return status;
	}
	
	/**
	 * function to set id 
	 * @param id
	 */
	public void SetCustomerId(int id) {
		customerid = id;
	}
}






