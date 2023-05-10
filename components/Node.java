package components;

/**
 * 
 * @author Nadav Ishai, ID: 206119893
 *
 *interface Node represent a package location
 */

public interface Node {

	public void collectPackage(Package p);
	public void deliverPackage(Package p);
	public void work();
	public void setSuspend();
	public void setResume();
	public void setStop();
}
