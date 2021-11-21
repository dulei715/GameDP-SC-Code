package edu.ecnu.dll._deprecated;

import edu.ecnu.dll.basic.basic_struct.agent.PrivacyWorker;

import java.util.List;
@Deprecated
public class PPPWorker extends PrivacyWorker {
    public List<Double> privacyBudget = null;


    public PPPWorker(double[] location, List<Double> privacyBudget) {
        this.location = location;
        this.privacyBudget = privacyBudget;
    }

    public PPPWorker(double[] location) {
        this.location = location;
    }

    @Override
    public Double getFinalUtility(Integer taskID) {
        return null;
    }


    @Override
    public Double getToTaskEffectiveNoiseDistance(Integer taskID) {
//        int index = taskIndex[taskID];
//        if (index == -1) {
//            return null;
//        }
//        return toTaskNoiseDistance.get(index);
        return null;
    }

    @Override
    public Double getTotalPrivacyBudgetCost(Integer taskID) {
        return null;
    }

    public PPPWorker() {
    }

    @Override
    public Double getPrivacyBudget(Integer taskID) {
        int index = taskIndex[taskID];
        if (index == -1) {
            return null;
        }
        return this.privacyBudget.get(index);
    }
}
