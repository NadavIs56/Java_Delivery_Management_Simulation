package components;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.locks.Lock;

/**
 * class Customer represents a customer that creates packages
 * @author Nadav Ishai, ID: 206119893
 *
 */
public class Customer implements Runnable{
	
	private static int num = 1;
	private int id;
	private Address address;
	private ArrayList <Package> customerpackageslist; 
	private boolean threadSuspend = false;
	private boolean stop = false;
	private static FileReader fr = null;
	private Lock readLock = components.MainOffice.GetReadLock();
	
	public Customer() {
		id = num;
		num += 1;
		address = components.MainOffice.GenerateAddress(components.Hub.GetBranchesList().size());
		customerpackageslist = new ArrayList<Package>(5);
		System.out.println("customer id = " + id);
	}
	
	public Customer(Customer cts)
	{
		id = cts.GetId();
		address = cts.GetAddress();
		threadSuspend = cts.threadSuspend;
		stop = cts.stop;
		fr = cts.fr;
		readLock = cts.readLock;
		
		customerpackageslist = new ArrayList<Package>();
		
		for(Package pck : MainOffice.GetMainOfficePackageList())
			if(pck.GetSenderAddress().equals(address))
				customerpackageslist.add(pck);
		
			
				
	}

	/**
	 * Function GetCustomerId returns customer's id by package
	 *  according to the packages list of the customer
	 * @param p is a package
	 * @return id if the customer sent this package(p) else -1
	 */
	public int GetCustomerId(Package p) {
		for(Package pck : customerpackageslist)
			if (pck.equals(p))
				return id;
		return -1; 
	}
	
	/**
	 * Function run starts the Customer work
	 */
	public void run() {
		int time = 0;
		for(int i = 0; i < 5; i++)
		{
			while(true) {
		   		try {
		   			time = (int)(Math.random() * 3000) + 2000;
					Thread.sleep(time);
					synchronized(this) {
		                while (threadSuspend)
		                	wait();
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
		   		if (stop) return;
		   		
	   			if(components.MainOffice.GetMainOfficePackageList().size() < 50)
		   			synchronized(MainOffice.class) {
		   				if(components.MainOffice.GetMainOfficePackageList().size() < 50)
		   				{
		   					customerpackageslist.add(components.MainOffice.addPackage(address, id));
		   				}				
		   			}
	   			if(customerpackageslist.size() == 5)
	   			{
	   				try {
						Thread.sleep(10000);
						synchronized(this) {
			                while (threadSuspend)
			                	wait();
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
			   		if (stop) return;
			   		
			   		synchronized(this){
			   		if(Read())
			   		{
			   			System.out.printf("---------------------Customer %d has stopd---------------------\n", id);
			   			stop = true;
			   		}
			   		}
	   			}
			}
   		}
	}
	
	/**
	 * Function sesSuspend suspends the Customer thred
	 */
	public synchronized void setSuspend() {
	   	threadSuspend = true;
	}
	
	/**
	 * Function setResume resumes the Customer thred
	 */
	public synchronized void setResume() {
		threadSuspend = false;
	   	notify();
	}
	
	/**
	 * Function setStop stops the Customer thred
	 */
	public void setStop() {
	   	stop = true;
	}
	
	/**
	 * Function Read reads the tracking.txt and check if all the customer's packages has delivered
	 */
	public boolean Read() {
		synchronized(this) {
		readLock.lock();
		int count = 0;
		int temp = 0;
		String line = "";
		synchronized(this) {
		try {
			fr = new FileReader("tracking.txt");
			int content = 0;
            while (content != -1)
            {
            	while((char)content != '\n')
            	{
            		line += (char)content;
            		content = fr.read();
            	}
            	content = fr.read();
            	
            	if(line.contains("DELIVERED") && line.contains("Customer = " + String.valueOf(id)))
            			count++;
            	
            	line = "";
            }
        	if (count == 5)
        	{
        		fr.close();
        		return true;
            } 
			
		}catch(IOException e) {
			e.printStackTrace();
		}
		finally {
			readLock.unlock();
		}
		return false; 
		}
		}
	}
	
	/**
	 * function to get stop
	 * @return stop
	 */
	public boolean GetStop() {
		return stop;
	}
	
	/**
	 * function to get id
	 * @return id
	 */
	public int GetId() {
		return id;
	}
	
	/**
	 * function to get address
	 * @return address
	 */
	public Address GetAddress() {
		return address;
	}

}







