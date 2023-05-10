package components;

/**
 * 
 * @author Nadav Ishai, ID: 206119893
 *
 *class Address represent addresses of both sender and recipient
 */

public class Address {

	private int zip;
	private int street;

	/**
	 * Address constructor
	 * @param zip = zip number of an address
	 * @param street = street number of an address
	 * 
	 */
	public Address(int zip, int street) { 
		this.Set_Zip(zip);
		this.Set_Street(street);
	}
	
	/**
	 * Address copy constructor
	 * @param a = other Address object
	 */
	public Address(Address a) {
		this.zip = a.zip;
		this.street = a.street;
	} 
	
	/**
	 * Function Get_Zip return address's zip number
	 * @return zip
	 */
	public int Get_Zip() {
		return zip;
	}

	/**
	 * Function Get_Street return address's street number
	 * @return street
	 */
	public int Get_Street() {
		return street;
	}

	/**
	 * Function Get_Address return address as String 
	 * @return String(address)
	 */
	public String Get_Address() {
		return zip + "-" + street;
	}
	
	/**
	 * Function Set_Zip is setting an address zip number
	 * @param z = a zip number to set
	 */
	public void Set_Zip(int z) {
		zip = z;
	}

	/**
	 * Function Set_Street is setting an address street number
	 * @param s = a street number to set
	 */
	public void Set_Street(int s) {
		this.street = s;
	}
	
	/**
	 * Function Set_Address is setting an address like the parameter address
	 * @param a = address to set
	 */
	public void Set_Address(Address a) {
		this.Set_Zip(a.Get_Zip());
		this.Set_Street(a.Get_Street());
	}
	
	public String toString() {
		return zip+"-"+street;
	}
}
