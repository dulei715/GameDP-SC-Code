package edu.ecnu.dll.basic_struct.agent;

public abstract class Task {
    public double[] location;

    public double[] getLocation() {
        return location;
    }

    public void setLocation(double[] location) {
        this.location = location;
    }

    public Task() {
    }

    public Task(double[] location) {
        this.location = location;
    }
}
