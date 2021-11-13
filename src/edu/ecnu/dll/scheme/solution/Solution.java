package edu.ecnu.dll.scheme.solution;

import edu.ecnu.dll.basic_struct.agent.Task;
import edu.ecnu.dll.basic_struct.agent.Worker;

import java.util.HashSet;

public abstract class Solution {
    public static final int DISTANCE_TAG = 0;
    public static final int BUDGET_TAG = 1;

    // parameter for distance
    public static final double alpha = 1;
    // parameter for privacy budget
    public static final double beta = 20;

    public Task[] tasks = null;
    public Worker[] workers = null;


    public double toNormalValue(double privacyBudget) {
        double expValue = Math.exp(-privacyBudget);
        return (1 - expValue) / (1 + expValue);
    }

//    protected double getTaskEntropy(Integer taskID, Integer totalCompetingTime, HashSet<Integer> competingWorkerIDSet) {
//        if (totalCompetingTime <= 0) {
//            throw new RuntimeException("The total competing time is not positive value!");
//        }
//        double taskEntropy = 0;
//        double tempRatio = 0;
//        for (Integer j : competingWorkerIDSet) {
//            tempRatio = this.workers[j].getTaskCompetingTimes(taskID) / totalCompetingTime;
//            taskEntropy -= tempRatio*Math.log(tempRatio);
//        }
//        return taskEntropy;
//    }

    protected double getTaskEntropy(Integer taskID, Integer totalCompetingTime, HashSet<Integer> competingWorkerIDSet) {
        if (totalCompetingTime <= 0) {
//            throw new RuntimeException("The total competing time is not positive value!");
            return 0;
        }
        double taskEntropy = 0;
        double tempRatio;
        Integer competingTimes = null;
        for (Integer j : competingWorkerIDSet) {
            competingTimes = this.workers[j].getTaskCompetingTimes(taskID);
            if (competingTimes == null) {
                continue;
            }
            tempRatio = competingTimes / totalCompetingTime;
            taskEntropy -= tempRatio * Math.log(tempRatio);
        }
        return taskEntropy;
    }

    protected  double getProposingValue(Double pcfValue, Double distance) {
        return pcfValue / distance;
    }


}
