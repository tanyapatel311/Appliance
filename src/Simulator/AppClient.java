package Simulator;
/* This is a stub code. You can modify it as you wish. */
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class AppClient {
    private static ArrayList<Appliance> list = new ArrayList<>();
    private static ArrayList <Location> locs = new ArrayList<Location>();
	
    //READ THE FILE INPUTTED AND PUT INTO ARRAYLIST
    public static void readAppFile(String file) {
        try {
        	File file1 = new File(file);
            Scanner scnr = new Scanner(file1);
            int locationID;
            String appName;
            double onPower;
            double probOn;
            boolean appType;
            double lowPower;
            while (scnr.hasNextLine()) {
                String line = scnr.nextLine();
                String[] fields = line.split(",");
             
	            locationID = Integer.parseInt(fields[0]);
	            appName = fields[1];
	            onPower = Double.parseDouble(fields[2]);
	            probOn = Double.parseDouble(fields[3]);
	            appType = Boolean.parseBoolean(fields[4]);
	            lowPower = Double.parseDouble(fields[5]);
	
	            if (appType) {
	                SmartAppliance app = new SmartAppliance(locationID, appName, onPower, probOn, appType, false, lowPower);
	                list.add(app);
	            } 
	            else {
	                Appliance app1 = new Appliance(locationID, appName, onPower, probOn, appType, false);
	                list.add(app1);
	            }
            }
            System.out.println("File Read");
            scnr.close();
        } catch (IOException ioe) {
            System.out.println("The file cannot be read");
        }
    }
	
	public static void main(String []args) throws IOException{
		//User interactive part
		String option1;
		Scanner scan = new Scanner(System.in);
		while(true){// Application menu to be displayed to the user.
			System.out.println("\nSelect an option:");
			System.out.println("Type \"A\" Add an appliance");
			System.out.println("Type \"D\" Delete an appliance");	
			System.out.println("Type \"L\" List the appliances");
			System.out.println("Type \"F\" Read Appliances from a file");
			System.out.println("Type \"S\" To Start the simulation");
			System.out.println("Type \"Q\" Quit the program");
			option1= scan.next();
			if (option1.equals("A"))
			{
				System.out.println("Enter location ID: ");
				int loc = scan.nextInt();
				System.out.println("Enter appliance name: ");
				String name = scan.next();
				System.out.println("Enter watts when on: ");
				int onWatts = scan.nextInt();
				System.out.println("Enter on probability: ");
				double prob = scan.nextDouble();
				System.out.println("Enter App Type: ");
				boolean type = Boolean.parseBoolean(scan.next());
				if(!type)
				{
					Appliance app2 = new Appliance(loc, name, onWatts, prob, type, false);
					list.add(app2);
				}
				else
				{
					System.out.println("Enter watts when on low power: ");
					int lowPow = scan.nextInt();
					SmartAppliance app3 = new SmartAppliance(loc, name, onWatts, prob, type, false, lowPow);
					list.add(app3);
					
				}
			}
			if (option1.equals("D"))
			{
		        System.out.println("Enter an Appliance ID to delete:");
		        int N = scan.nextInt();
		        boolean found = false;

		        while (!found) {
		            found = false;
		            for (int i = 0; i < list.size(); i++) {
		                if (list.get(i).getApplianceID() == N) {
		                    list.remove(i);
		                    found = true;
		                    break;
		                }
		            }
		            if (!found) {
		                System.out.println("Appliance ID not found. Please enter the correct Appliance ID:");
		                N = scan.nextInt();
		            }
		        }
				
			}
			if (option1.equals("L"))
			{
				
				for(Appliance appliance : list)
					{System.out.println(appliance.toString());}
			}
			if (option1.equals("F"))
			{
				System.out.println("Enter a file name: ");
				String fileName = scan.next();
				readAppFile(fileName);
			}
			if (option1.equals("S"))
			{
				System.out.println("Enter the max voltage: ");
				int maxWatts = scan.nextInt();
				System.out.println("Enter number of time steps:");
				int timesteps = scan.nextInt();
				FileWriter writer = new FileWriter("summaryFile.txt");
				runTimeSteps(maxWatts, timesteps, writer);
			}
				
			if (option1.equals("Q"))
				{break;}
				
		}
		scan.close();
	}

	//RUN THE TIMESTEPS WHEN ASKED TO SIMULATE
	public static void runTimeSteps (int maxWatts, int steps, FileWriter writer)
	{	int size = 0;
		for (int i = 0; i < steps; i++)
		{
			System.out.println("***** TIMESTEP " + (i+1)+ " *****");
			int brownedOut = 0;
			int changedSmartApps = 0;
			double totalWatts = 0;
			ArrayList<Appliance> finalList = new ArrayList<>();
			ArrayList <Location> finalLocs = new ArrayList<>();
			
			// Sets all the apps to on based off probability
			ArrayList<Appliance> onList = setAppsOn();
			//Sort smartApps
			ArrayList <Appliance> smartApps = smartAppsSort(onList);
			
			// CALCULATES TOTAL WATTS IN ONLIST
			for (int j =0; j < onList.size(); j++)
				{totalWatts += onList.get(j).getOnPower();}
			
			//sets all smart appliances to low Power
			for (int k = 0; k < smartApps.size(); k++)
			{
					if (totalWatts <= maxWatts)
						{break;}
					double onPower = smartApps.get(k).getOnPower();
					double lowPower =  (1 - ((SmartAppliance) smartApps.get(k)).getLowPower()) * onPower;
					smartApps.get(k).setOnPower(lowPower);
					
					totalWatts -= (onPower-lowPower);
					finalList.add(smartApps.get(k));
					changedSmartApps++;
					
			}
			System.out.println("Number of Appliances Changed: " + changedSmartApps);
			
//*********BROWNOUT********
				// Creates ArrayList of locations that have appliances that are on
			if (totalWatts > maxWatts)
			{	for (int w = 0; w < onList.size(); w++)
				{
					Location l = new Location(onList.get(w).getLocationID());
					l.addAppliance(onList.get(w));
					locs.add(l);
				}
				deleteDuplicate();
		
				//SORT LOCATIONS
				sortLocations(locs);
				for (int l =0; l < locs.size(); l++)
					{
						locs.get(l).brownout();
						finalLocs.add(locs.get(l));
						size++;
						brownedOut++;
						totalWatts -= (locs.get(l).getTotalWatts());
						// If watts go to negative after browning out

						if (totalWatts <= maxWatts)
						{break;}
					}
			
			System.out.println("Number of Locations Browned out: " + brownedOut);
			
			}
			
			readIntoFile(i, finalList,finalLocs, writer);
			
		}
		System.out.println("\nNumber of Locations Affected: " + size);
		System.out.println("Max Affected Location: " + locs.get(3).getLocationID());
	}

	
	// Sets all apps on based on probability
	public static ArrayList<Appliance> setAppsOn()
	{
		
			// List of appliances that are On
			ArrayList<Appliance> onList = new ArrayList<>();
			//Randomly sets appliances to On based off of on probability
			for (int j = 0; j < list.size(); j++)
			{
				if (Math.random() < list.get(j).getProbOn())
				{onList.add(list.get(j));}
			}
			for (int k = 0; k < onList.size(); k++)
				{onList.get(k).setIsOn(true);}
			return onList;
	}

	//SORT ALL SMART APLLIANCES BY WATTS WHEN POWERED ON
	public static ArrayList<Appliance> smartAppsSort(ArrayList<Appliance> appList)
	{
		ArrayList<Appliance> smartApps = new ArrayList<>();
		//Smart Appliance ArrayList
		for (int i = 0; i < appList.size(); i++)
		{
			if(appList.get(i).getAppType() == true)
				{smartApps.add(appList.get(i));}
		}
		//SORT smart Appliances from smallest to largest
		for (int i = 0; i < smartApps.size(); i++) 
		{
            for (int j = i + 1; j < smartApps.size(); j++) 
            {
                Appliance temp = new Appliance();
                if (smartApps.get(i).getOnPower() > smartApps.get(j).getOnPower()) 
                {
                    temp = smartApps.get(i);
                    smartApps.set(i,smartApps.get(j)) ;
                    smartApps.set(j,temp);
                }
            }
		}
		return smartApps;
	}
	
	public static void sortLocations(ArrayList<Location> loc1)
	{
		for (int i = 0; i < loc1.size()-1; i++) 
		{
            for (int j = i + 1; j < loc1.size(); j++) 
            {
                Location temp1 = new Location();
                // sort from least to greatest
                if (loc1.get(i).getTotalWatts() > loc1.get(j).getTotalWatts()) 
                {
                    temp1 = loc1.get(i);
                    loc1.set(i,loc1.get(j));
                    loc1.set(j,temp1);
                }
            }
		}
	}
	// Delete Duplicate Locations 
 	public static void deleteDuplicate()
	{
		for (int i = 0; i < locs.size()-1; i++) 
		{
            for (int j = i + 1; j < locs.size(); j++) 
            {
                // take out repeated locations
                if (locs.get(i).getLocationID() == locs.get(j).getLocationID()) 
                	{
                		locs.get(i).merge(locs.get(j).getAppliance());
                		locs.remove(locs.get(j));
                		j--;
                	}
            }
		}
	}
 	public static void displayLocation(ArrayList <Location> loc)

 	{
 		for (int i = 0; i < loc.size(); i++)
 			{
 				System.out.println("Location " + (i+1));
 				loc.get(i).printLocations();
 				System.out.println("");
 			}
 	}
 	public static void displayAppList(ArrayList<Appliance> appList)
 	{
		for(Appliance a : appList)
			{System.out.println(a.toString());}
 	}

 	public static void readIntoFile(int step, ArrayList <Appliance> appFinal, ArrayList <Location> locFinal, FileWriter writer ) {
 		try
        {
 			writer.write("\n********** TIMESTEP " + (step+1) + " **********");
 			writer.write("\nSMART APPLIANCES CHANGED TO LOW\n");
	        for (int j = 0; j < appFinal.size(); j++) 
	        	{writer.write(appFinal.get(j).toString() + "\n");}	
	        
	        writer.write("\nLOCATIONS BROWNED OUT\n");
	        for (int k = 0; k < locFinal.size(); k++)
	        {
	        	writer.write("Location " + (k+1) + "\n");
	        	for (int m = 0; m < locFinal.get(k).getAppliance().size(); m++)
	        	{
	        		writer.write(locFinal.get(k).getAppliance().get(m).toString());
	        		writer.write("\n");
	        	}
	        	writer.write("\n");
	        	
	        }
	        	
        } 
        catch (IOException e) 
            {e.printStackTrace();}
 	}

}