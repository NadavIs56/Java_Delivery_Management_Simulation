package components;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Arc2D;
import java.awt.geom.Rectangle2D;

/**
 * 
 * @author Nadav Ishai, ID: 206119893
 * class NonStandardTruck deliver NonStandardackages from a customer to the recipient 
 */
public class NonStandardTruck extends Truck{
	
	private int width;
	private int length;
	private int height;
	
	public NonStandardTruck () {
		super();
		height = (int)(Math.random() * 200 + 301);
		width = (int)(Math.random() * 200 + 401);
		length = (int)(Math.random() * 200 + 901);
		System.out.println("Creating " + this);
	}
	
	public NonStandardTruck(String licensePlate, String truckModel, int length, int width, int height) {
		super(licensePlate, truckModel);
		this.width = width;
		this.length = length;
		this.height = height;
	}
	
	public NonStandardTruck(Truck t) {
		super(t);
		width = ((NonStandardTruck)t).width;
		length = ((NonStandardTruck)t).length;
		height = ((NonStandardTruck)t).height;	
	}
	
	/**
	 * NonStandardTruck toString function
	 */
	public String toString() {
		return "NonStandardTruck " + super.toString() + ", length="+length+", width="+width+", height="+height+"]";
	}
	
	/**
	 * Function GetTruckType return truck type
	 */
	public String GetTruckType() {
		return "NonStandardTruck";
	}
	
	/**
	 * Function GetWidth return nonstandard truck width
	 * @return width
	 */
	public int GetWidth() {
		return width;
	}
	
	/**
	 * Function GetHeight return nonstandard truck height
	 * @return width
	 */
	public int GetHeight() {
		return height;
	}
	
	/**
	 * Function GetLength return nonstandard truck length
	 * @return width
	 */
	public int GetLength() {
		return length;
	}
	
	/**
	 * Function GetTruckName return truck name
	 */
	public String GetTruckName() {
		return "NonStandardTruck " + GetTruckId();
	}
	
	/*
	 * Function CheckIfGoodSize check if a package is fitting to the the NonStandardTruck sizes
	 */
	public boolean CheckIfGoodSize(int w, int l, int h) {
		return (w <= width && l <= length && h <= height);
	}
	
	/**
	 * Function collectPackage takes nonstandard package from the customer
	 */
	public void collectPackage(Package p) {
		this.GetPackagesList().get(0).ChangeStatus(Status.DISTRIBUTION);
		this.GetPackagesList().get(0).addTracking(this, Status.DISTRIBUTION);
		int timetoarrive = ((Math.abs(GetPackagesList().get(0).GetSenderAddress().Get_Zip() - GetPackagesList().get(0).GetDestinationAddress().Get_Zip()) % 10) + 1) * 10;
		SetTimeLeft(timetoarrive);
		SetSaveTime(timetoarrive);
		System.out.printf("NonStandardTruck %d has collected package %d\n", GetTruckId(), GetPackagesList().get(0).GetPackageId());
		System.out.printf("NonStandardTruck %d is delivering package %d, time left: %d\n", GetTruckId(), GetPackagesList().get(0).GetPackageId(), GetTimeLeft());
	}
	
	/**
	 * Function collectPackage deliver nonstandard package to the destination
	 */
	public void deliverPackage(Package p) {
		this.GetPackagesList().get(0).ChangeStatus(Status.DELIVERED);
		this.GetPackagesList().get(0).addTracking(null, Status.DELIVERED);
		System.out.printf("NonStandartTruck %d has delivered package %d to the destination\n", GetTruckId(), GetPackagesList().get(0).GetPackageId());
		GetPackagesList().remove(0);
		SetToAvailable();
	}
	
	/**
	 * Function work decrease the nonstandard driving time and perform one work unit
	 */
	public synchronized void work() {
		
		if(!(this.GetAavilableStatus()))
		{
			if(this.GetTimeLeft() == 0)
			{
				if(GetPackagesList().get(0).GetStatus().equals("COLLECTION"))
					collectPackage(GetPackagesList().get(0));
	
				else if(GetPackagesList().get(0).GetStatus().equals("DISTRIBUTION"))
					deliverPackage(GetPackagesList().get(0));
			}
			this.DecreaseTimeLeft();
		}
	}
	
	/**
	 * Function run starts the nonstandardtrucks work
	 */
	@Override
	public void run() {
		
		while(true) {
			try {
				Thread.sleep(500);
				synchronized(this) {
	                while (GetThreadSuspend())
	                	wait();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (GetStop()) return;
			work();
		}
	}
	
	/**
	 * Function DrawTruck draws the nonstandardtrucks
	 */
	public void DrawTruck(Graphics g, Hub hub) {
//		synchronized(this) {
		Graphics2D g2d = (Graphics2D) g;
		
		double trucxstart = 0, trucystart = 0;
		double totalx = 0, totaly = 0;
		double newx = 0, newy = 0;
		double mul = Math.abs(GetSaveTime() - GetTimeLeft());
		boolean flag = false;
		
		if(GetPackagesList().get(0).GetStatus().equals("COLLECTION"))
		{
			trucxstart = 1160 - 8;
			trucystart = 300 - 108;
			totalx = Math.abs(1160 - GetPackagesList().get(0).GetSenderXLocation());
			totaly = Math.abs(200 - GetPackagesList().get(0).GetSenderYLocation() - 15);
			newx = trucxstart - ((totalx/GetSaveTime()) * mul);
			newy = trucystart - ((totaly/GetSaveTime()) * mul);
			g2d.setColor(new Color(255, 204, 203));
			g2d.fill(new Rectangle2D.Double(newx, newy, 16, 16));
			flag = true;
		}
		else if(GetPackagesList().get(0).GetStatus().equals("DISTRIBUTION"))
		{
			trucxstart = GetPackagesList().get(0).GetSenderXLocation() - 8;
			trucystart = GetPackagesList().get(0).GetSenderYLocation() + 7;
			totalx = 0;
			totaly = Math.abs(GetPackagesList().get(0).GetDestinationYLocation() - GetPackagesList().get(0).GetSenderYLocation());
			newx = trucxstart;
			newy = trucystart + ((totaly/GetSaveTime()) * mul);
			g2d.setColor(new Color(139, 0, 0));
			g2d.fill(new Rectangle2D.Double(newx, newy, 16, 16));
			flag = true;
		}

		if(flag)
		{
			g2d.setColor(Color.black);
			double ovalxstart = newx - 5;	//top left
			double ovalystart = newy - 5;
			g2d.fill(new Arc2D.Double(ovalxstart, ovalystart, 10.0, 10.0, 0.0, 360.0, 1));
			
			ovalxstart = newx + 11;	//top right
			ovalystart = newy - 5;
			g2d.fill(new Arc2D.Double(ovalxstart, ovalystart, 10.0, 10.0, 0.0, 360.0, 1));
			
			ovalxstart = newx + 11;	//bottom right
			ovalystart = newy + 11;
			g2d.fill(new Arc2D.Double(ovalxstart, ovalystart, 10.0, 10.0, 0.0, 360.0, 1));
			
			ovalxstart = newx - 5;	//bottom left
			ovalystart = newy + 11;
			g2d.fill(new Arc2D.Double(ovalxstart, ovalystart, 10.0, 10.0, 0.0, 360.0, 1));
		}
//		}
	}
}






