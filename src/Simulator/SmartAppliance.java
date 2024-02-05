package Simulator;

public class SmartAppliance extends Appliance{
    private double lowPower;

    public SmartAppliance(int loc, String name, double myOnPower, double onProbability, boolean type, boolean on, double mylowPow)
    {
        super(loc, name, myOnPower, onProbability, type, on);
        lowPower = mylowPow;
    }

    public SmartAppliance() {
        locationID = 0; 
        appName = ""; 
        onPower = 0; 
        probOn = 0; 
        appType = false;
        isOn = false;
        lowPower = 0.0;
    }

    public double getLowPower() {
        return lowPower;
    }

    public void setLowPower(double lowPower) {
        this.lowPower = lowPower;
    }

    public String toString() {
        String retString = super.toString();
        return retString + ", lowPower=" + lowPower + "]";
    }
}