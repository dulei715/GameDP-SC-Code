package edu.ecnu.dll.struct.worker;

public abstract class Worker {
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
