package edu.ecnu.dll.basic_struct.agent;

public abstract class Worker extends Agent {
    public double[] location = null;
    public Double maxRange = null;

    public double[] getLocation() {
        return location;
    }

    public void setLocation(double[] location) {
        this.location = location;
    }

    public Double getMaxRange() {
        return maxRange;
    }

    public void setMaxRange(Double maxRange) {
        this.maxRange = maxRange;
    }
}
