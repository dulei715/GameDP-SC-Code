package edu.ecnu.dll.basic_struct.agent;

public abstract class Task extends Agent {
    public double[] location;

    public double valuation = 0;

    public double[] getLocation() {
        return location;
    }

    public void setLocation(double[] location) {
        this.location = location;
    }

    public Task() {
    }

    public Task(double[] location, double valuation) {
        this.location = location;
        this.valuation = valuation;
    }
}
