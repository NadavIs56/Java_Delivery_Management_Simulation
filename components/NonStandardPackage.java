package components;

/**
 * class NonStandardPackage represents nonstandard size package
 * @author Nadav Ishai, ID: 206119893
 *
 */
public class NonStandardPackage extends Package {
	private int width;
	private int length;
	private int height;

	public NonStandardPackage(Priority priority, Address senderAddress, Address destinationAdress, int width, int length, int height, int customer) {
		super(priority, senderAddress, destinationAdress, customer);
		this.width = width;
		this.length = length;
		this.height = height;
		System.out.println("Creating " + this);
	}
	
	public NonStandardPackage(Package p) {
		super(p);
		width = ((NonStandardPackage)p).width;
		length = ((NonStandardPackage)p).length;
		height = ((NonStandardPackage)p).height; 
	}
	
	/**
	 * NonStandardPackage toString function
	 */
	public String toString() {
		return "NonStandardPackage " + super.toString() + "width="+width+", length="+length+", height="+height+"]";
	}
	
	/**
	 * Function GetPackageType returns package size type
	 * 
	 */
	public String GetPackageType() {
		return "NonStandardPackage";
	}
	
	/**
	 * Function GetPackageWidth returns package width
	 * 
	 */
	public int GetPackageWidth() {
		return width;
	}
	
	/**
	 * Function GetPackageHeight returns package height
	 * 
	 */
	public int GetPackageHeight() {
		return height;
	}
	
	/**
	 * Function GetPackageLength returns package length
	 * 
	 */
	public int GetPackageLength() {
		return length;
	}
}

