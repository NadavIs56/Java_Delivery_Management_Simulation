package components;

import java.awt.Color;
import java.awt.Graphics;

/**
 * 
 * @author Nadav Ishai, ID: 206119893
 * class SmallPackage represents small size package
 */
public class SmallPackage extends Package {

	private boolean acknowledge;

	public SmallPackage(Priority priority, Address senderAddress, Address destinationAdress, boolean acknowledge, int customer) {
		super(priority, senderAddress, destinationAdress, customer);
		this.acknowledge = acknowledge;
		System.out.println("Creating " + this);
	}
	
	public SmallPackage(Package p) {
		super(p);
		acknowledge = ((SmallPackage)p).acknowledge;
	}

	/**
	 * SmallPackage toString function
	 */
	public String toString() {
		return "SmallPackage " + super.toString() + "acknowledge="+acknowledge+"]";
	}

	/**
	 * Function GetAcknowledge return small package acknowledge
	 * @return acknowledge
	 */
	public boolean GetAcknowledge() {
		return acknowledge;
	}
	
	/**
	 * Function GetPackageType returns package size type
	 * 
	 */
	public String GetPackageType() {
		return "SmallPackage";
	}
	
}











