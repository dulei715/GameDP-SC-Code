package edu.ecnu.dll.basic.basic_struct.pack.single_agent_info.sub_class;

import edu.ecnu.dll.basic.basic_struct.pack.single_agent_info.SingleInfoPack;

public class WorkerIDDistancePair extends SingleInfoPack {
    protected Integer workerID = null;
    protected Double distance = null;

    public WorkerIDDistancePair() {
    }

    public WorkerIDDistancePair(Integer workerID, Double distance) {
        this.workerID = workerID;
        this.distance = distance;
    }

    public Integer getWorkerID() {
        return workerID;
    }

    public void setWorkerID(Integer workerID) {
        this.workerID = workerID;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }


    @Override
    public String toString() {
        return "WorkerIDDistancePair{" +
                "workerID=" + workerID +
                ", distance=" + distance +
                '}';
    }
}
