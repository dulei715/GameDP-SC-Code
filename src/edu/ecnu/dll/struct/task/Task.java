package edu.ecnu.dll.struct.task;

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
