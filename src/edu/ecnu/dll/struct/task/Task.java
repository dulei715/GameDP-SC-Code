package edu.ecnu.dll.struct.task;

public abstract class Task {
    protected double[] location;

    public double[] getLocation() {
        return location;
    }

    public void setLocation(double[] location) {
        this.location = location;
    }
}
