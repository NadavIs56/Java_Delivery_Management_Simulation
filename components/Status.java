package components;

/**
 * 
 * @author Nadav Ishai, ID: 206119893
 * enum Status contains all the packages's statuses
 */

public enum Status{
	CREATION,
	COLLECTION,
	BRANCH_STORAGE,
	HUB_TRANSPORT,
	HUB_STORAGE,
	BRANCH_TRANSPORT,
	DELIVERY,
	DISTRIBUTION,
	DELIVERED;
	
	/**
	 * Function GetStatus return the current Status as String
	 * @return String(current Status)
	 */
	public String GetStatus() {
		switch(this) {
			case CREATION: 
				return "CREATION";
			case COLLECTION: 
				return "COLLECTION";
			case BRANCH_STORAGE: 
				return "BRANCH_STORAGE";
			case HUB_TRANSPORT: 
				return "HUB_TRANSPORT";
			case HUB_STORAGE: 
				return "HUB_STORAGE";
			case BRANCH_TRANSPORT: 
				return "BRANCH_TRANSPORT";
			case DELIVERY: 
				return "DELIVERY";
			case DISTRIBUTION: 
				return "DISTRIBUTION";
			case DELIVERED: 
				return "DELIVERED";
		}
		return "";
	}
}
