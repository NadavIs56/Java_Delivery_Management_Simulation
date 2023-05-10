package components;

/**
 * class StandardPackage represents standard size package
 * @author Nadav Ishai, ID: 206119893
 *
 */
public class StandardPackage extends Package {

	private double weight;
	
	public StandardPackage(Priority priority, Address senderAddress, Address destinationAdress, double weight, int customer) {
		super(priority, senderAddress, destinationAdress, customer);
		this.weight = weight;
		System.out.println("Creating " + this);
	}
	 
	
	public StandardPackage(Package p) {
		super(p);
		weight = ((StandardPackage)p).weight;
	}
	
	/**
	 * Standard Package toString function
	 */
	public String toString() {
		return "StandardPackage " + super.toString() + "weight="+weight+"]";
	}
	
	/**
	 * Function GetPackageType returns package size type
	 * 
	 */
	public String GetPackageType() {
		return "StandardPackage";
	}
	
	/**
	 * Function GetPackageWeight returns Standard Package weight
	 * @return weight
	 */
	public double GetPackageWeight() {
		return weight;
	}
}
