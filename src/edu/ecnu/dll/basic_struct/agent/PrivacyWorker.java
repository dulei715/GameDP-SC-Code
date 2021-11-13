package edu.ecnu.dll.basic_struct.agent;

import java.util.List;
import java.util.Map;

public abstract class PrivacyWorker extends Worker {


//    public Double[] toTaskDistance = null;
//    public Double[] toTaskNoiseDistance = null;
    public List<Double> toTaskDistance = null;
    public List<Double> toTaskNoiseDistance = null;

    public abstract Double getPrivacyBudget(Integer taskID);

    public PrivacyWorker() {
    }

    public PrivacyWorker(double[] location) {
        super(location);
    }

    @Override
    public Double getToTaskDistance(Integer taskID) {
        int index = taskIndex[taskID];
        if (index == -1) {
            return null;
        }
        return toTaskDistance.get(index);
    }

    public int setToTaskDistance(Integer taskID, Double toTaskDistance) {
        int index = taskIndex[taskID];
        if (index == -1) {
            return -1;
        }
        this.toTaskDistance.set(index, toTaskDistance);
        return 0;
    }

    public abstract Double getToTaskNoiseDistance(Integer taskID);
//    {
//        int index = taskIndex[taskID];
//        if (index == -1) {
//            return null;
//        }
//        return toTaskNoiseDistance.get(index);
//    }

    public int setToTaskNoiseDistance(Integer taskID, Double toTaskNoiseDistance) {
        int index = taskIndex[taskID];
        if (index == -1) {
            return -1;
        }
        this.toTaskNoiseDistance.set(index, toTaskNoiseDistance);
        return 0;
    }
}
