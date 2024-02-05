package Simulator;
import java.util.ArrayList;

public class Location {
    private ArrayList<Appliance> appliances = new ArrayList<Appliance>();
    private int totalWatts;
    private int locationID;

    public Location() {
        // initialize
        totalWatts = 0;
        locationID = 0;
    }
    public Location(int locID) {
        setLocationID(locID);
    }
    public void brownout() {
        for (Appliance a : appliances)
        	{a.setIsOn(false);}
    }
    
    public void addAppliance(Appliance a) {
        appliances.add(a);
    }
    public void addWatts(Appliance a) {
    	
        totalWatts += a.getOnPower();
    }
    public ArrayList<Appliance> getAppliance() {
        return appliances;
    }
    public int getTotalWatts() {
    	for (Appliance a: appliances)
    		{totalWatts += a.getOnPower();}
        return totalWatts;
    }
    public int getLocationID() {
        return locationID;
    }
    public void setLocationID(int locationID) {
        this.locationID = locationID;
    }
    
    public void printLocations()
    {
    	for(Appliance a: appliances)
    		{ System.out.println(a.toString());}
    }
    public void merge(ArrayList<Appliance> appList)
    {
    	for (Appliance a: appList)
    		{appliances.add(a);}
    }

}