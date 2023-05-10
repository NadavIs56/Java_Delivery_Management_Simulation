package components;

/**
 * 
 * @author Nadav Ishai, ID: 206119893
 * class Tracking represents a package track
 */

public class Tracking {
	
	private int time;
	private Node node;
	private Status status;
	
	
	public Tracking(int time, Node node, Status status) {
		this.time = time;
		this.node = node;
		this.status= status;
	}
	
	/**
	 * copy constructor
	 * @param t = other Tracking object to copy
	 */
	public Tracking(Tracking t) {
		this.time = t.time;
		this.node = t.node;
		this.status= t.status;
	}
	
	/**
	 * Tracking toString function
	 * @return String(Tracking)
	 */
	public String toString() {
		if(node == null)
			return time+": Customer, status="+status+"\n";
		
		else if(node instanceof Branch)
			return time+": "+((Branch)node).GetBranchName()+", status="+status+"\n";
		
		else if(node instanceof Truck)
			return time+": "+((Truck)node).GetTruckName()+", status="+status+"\n";

		return "";
	}
}
