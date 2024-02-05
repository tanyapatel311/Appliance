package Simulator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Appliance {
    private int locationID;
    private String appName;
    private double onPower;
    private double probOn;
    private boolean appType;
    private static int applianceIDCount = 0; 
    private int applianceID;
    private boolean isOn;

    public Appliance(int loc, String name, double myOnPower, double onProbability, boolean type, boolean on) 
    { 
        locationID = loc; 
        appName = name; 
        onPower = myOnPower; 
        probOn = onProbability; 
        appType = type; 
        applianceID = ++applianceIDCount;
        isOn = on;
    } 

    public Appliance() {
        locationID = 0; 
        appName = ""; 
        onPower = 0; 
        probOn = 0; 
        appType = false;
        isOn = false;
    }


    public int getID()
        {return locationID;}

    public String getAppName()
        {return appName;}

    public double getOnPower()
        {return onPower;}

    public double getProbOn()
        {return probOn;}

    public int getLocationID() {
        return locationID;
    }
    public void setOnPower(double power)
    	{onPower = power;}
    public boolean getAppType()
        {return appType;}

    public int getApplianceID() {
        return applianceID;
    }

    public boolean getIsOn() {
        return isOn;
    }

    public void setIsOn(boolean value)
        {isOn = value;}


    public String toString() {
        String retString = "[ApplianceID=" + applianceID + ", locationID=" + locationID + ", appName=" + appName + ", onPower=" + onPower + ", probOn="
                + probOn + ", appType=" + appType + ", isOn=" + isOn;
        if(!appType) {
            retString += "]";
        }
        return retString;
    }


}