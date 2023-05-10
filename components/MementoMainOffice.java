package components;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.swing.JPanel;

/**
 * 
 * @author Nadav Ishai, ID: 206119893
 * class MementoMainOffice is used to imagine a copy of main office for the memento
 */
public class MementoMainOffice{
	private int mementoclock;
	private Hub mementohub;
	protected ArrayList<Package> mementopackages;
	private int mementototalpackagesnumber;
	private JPanel mementomainofficepanel;
	private boolean mementothreadSuspend = false;
	private boolean mementostop = false;
	private volatile MainOffice mementoinstance = null;
	protected ArrayList <Customer> mementocustomers;
	private FileWriter mementofw = null;
	private PrintWriter mementopw = null;
	private ReentrantReadWriteLock mementorwl = null;
	private Lock mementoreadLock;
    private Lock mementowriteLock;
    private FileReader mementofr = null;
    ArrayList<Thread> mementotc = new ArrayList<Thread>();
    Thread mementothub = new Thread();
    private String mementotrackingtext = "";
    
    
	public MementoMainOffice(MainOffice main) {
//		synchronized(this) {
			
			mementoclock = (int)main.GetClock();
			
			mementohub = new Hub(main.GetHub(), false);
			
			mementopackages = new ArrayList<Package>();
			
			for(Package pck : mementohub.GetPackagesList())
				mementopackages.add(pck);
			
			for(Truck t : mementohub.GetTruckList())
				for(Package pck : t.GetPackagesList())
					mementopackages.add(pck);
			
			for(Branch b : mementohub.GetMementobranches())
			{
				for(Truck t : b.GetTruckList())
					for(Package pck : t.GetPackagesList())
						mementopackages.add(pck);
				
				for(Package pck : b.GetPackagesList())
					mementopackages.add(pck);		
			}
				
			mementocustomers = new ArrayList<Customer>();
			for (Customer cstm : main.GetCustomers())
				mementocustomers.add(new Customer(cstm));
			
			mementoinstance = main;
			mementototalpackagesnumber = main.GetTotalPackagesNumber();
			mementomainofficepanel = main.GetMainOfficePanel();
			mementothreadSuspend = main.GetThreadSuspend();
			mementostop = main.GetStop();
			mementofw = main.GetFileWriter();
			mementopw = main.GetPrintWriter();
			mementorwl = main.GetReentrantReadWriteLock();
			mementoreadLock = main.GetReadLock();
			mementowriteLock = main.GetWriteLock();
			mementofr = main.GetFileReader();
			
			mementotrackingtext = "";
			mementoreadLock.lock();
			try {
				mementofr = new FileReader("tracking.txt");
				int content = 0;
	            while (content != -1)
	            {
	            	mementotrackingtext += (char)content;
	            	content = mementofr.read();
	            }
			}catch(IOException e) {
				e.printStackTrace();
			}
	
			finally {
				mementoreadLock.unlock();
			}
			
			
//		}
	}
	
	/**
	 * function to return mementoclock
	 * @return mementoclock
	 */
	public int GetMementoClock() {
		return mementoclock;
	}
	
	/**
	 * function to return mementohub
	 * @return mementohub
	 */
	public Hub GetMnenentoHub() {
		return mementohub;
	}
	
	/**
	 * function to return mementopackages
	 * @return mementopackages
	 */
	public ArrayList<Package> GetMnenentoPackages() { 
		return mementopackages;
	}
	
	/**
	 * function to return mementocustomers
	 * @return mementocustomers
	 */
	public ArrayList<Customer> GetMementoCustomers(){
		return mementocustomers;
	}
	
	/**
	 * function to return mementototalpackagesnumber
	 * @return mementototalpackagesnumber
	 */
	public int GetMementoTotalPackagesNumber(){
		return mementototalpackagesnumber;
	}
	
	/**
	 * function to return mementomainofficepanel
	 * @return mementomainofficepanel
	 */
	public JPanel GetMementoMainOfficePanel(){
		return mementomainofficepanel;
	}
	
	/**
	 * function to return mementothreadSuspend
	 * @return mementothreadSuspend
	 */
	public boolean GetMementoThreadSuspend(){
		return mementothreadSuspend;
	}
	
	/**
	 * function to return mementostop
	 * @return mementostop
	 */
	public boolean GetMementoStop(){
		return mementostop;
	}
	
	/**
	 * function to return mementofw
	 * @return mementofw
	 */
	public FileWriter GetMementoFW(){
		return mementofw;
	}
	
	/**
	 * function to return mementopw
	 * @return mementopw
	 */
	public PrintWriter GetMementoPW(){
		return mementopw;
	}
	
	/**
	 * function to return mementorwl
	 * @return mementorwl
	 */
	public ReentrantReadWriteLock GetMementoRRWL(){
		return mementorwl;
	}
	
	/**
	 * function to return mementoreadLock
	 * @return mementoreadLock
	 */
	public Lock GetMementoReadLock(){
		return mementoreadLock;
	}
	
	/**
	 * function to return mementowriteLock
	 * @return mementowriteLock
	 */
	public Lock GetMementoWriteLock(){
		return mementowriteLock;
	}
	
	/**
	 * function to return mementofr
	 * @return mementofr
	 */
	public FileReader GetMementoFileReader(){
		return mementofr;
	}
	
	/**
	 * function to return mementotc
	 * @return mementotc
	 */
	public ArrayList<Thread> GetMementoThread() {
		return mementotc;
	}
	
	/**
	 * function to return mementothub
	 * @return mementothub
	 */
	public Thread GetMementoTHub() {
		return mementothub;
	}
	
	/**
	 * function to return mementotrackingtext
	 * @return mementotrackingtext
	 */
	public String GetMementoTrackingText() {
		return mementotrackingtext;
	}
}











