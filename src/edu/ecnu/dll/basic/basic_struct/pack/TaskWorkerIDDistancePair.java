package edu.ecnu.dll.basic.basic_struct.pack;

public class TaskWorkerIDDistancePair extends TaskWorkerIDPair {
    private Double distance = null;

    public TaskWorkerIDDistancePair() {
    }

    public TaskWorkerIDDistancePair(Integer taskID, Integer workerID, Double distance) {
        super(taskID, workerID);
        this.distance = distance;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    @Override
    public String toString() {
        return super.toString() + "; TaskWorkerIDDistancePair{" +
                "distance=" + distance +
                '}';
    }
}
