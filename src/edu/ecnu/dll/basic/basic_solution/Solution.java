package edu.ecnu.dll.basic.basic_solution;

import edu.ecnu.dll.basic.basic_struct.agent.Task;
import edu.ecnu.dll.basic.basic_struct.agent.Worker;

import java.util.Set;

public abstract class Solution {
    public static final int DISTANCE_TAG = 0;
    public static final int BUDGET_TAG = 1;

    // parameter for distance
    public static final double alpha = 1;
    // parameter for privacy budget
    public static final double beta = 1;

//    public static int proposalSize = Integer.MAX_VALUE;
    public Integer proposalSize = null;

    public Task[] tasks = null;
    public Worker[] workers = null;

    public static double transformDistanceToValue(double distance) {
        return alpha * distance;
    }

    public static double transformPrivacyBudgetToValue(double privacyBudget) {
        // 要求该函数必须是线性函数，即满足 f(x+y) = f(x) + f(y)
        return beta * privacyBudget;
    }


    public double toNormalValue(double privacyBudget) {
        double expValue = Math.exp(-privacyBudget);
        return (1 - expValue) / (1 + expValue);
    }

    public void addAllWorkerIDToSet(Set<Integer> set) {
        for (int i = 0; i < this.workers.length; i++) {
            set.add(i);
        }
    }

//    protected double getTaskEntropy(Integer taskID, Integer totalCompetingTime, HashSet<Integer> competingWorkerIDSet) {
//        if (totalCompetingTime <= 0) {
////            throw new RuntimeException("The total competing time is not positive value!");
//            return 0;
//        }
//        double taskEntropy = 0;
//        double tempRatio;
//        Integer competingTimes = null;
//        for (Integer j : competingWorkerIDSet) {
//            competingTimes = this.workers[j].getTaskCompetingTimes(taskID);
//            if (competingTimes == null) {
//                continue;
//            }
//            tempRatio = competingTimes / totalCompetingTime;
//            taskEntropy -= tempRatio * Math.log(tempRatio);
//        }
//        return taskEntropy;
//    }

    protected  double getProposingValue(Double pcfValue, Double distance) {
        return pcfValue / distance;
    }


}
