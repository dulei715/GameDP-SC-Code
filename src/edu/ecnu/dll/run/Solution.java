package edu.ecnu.dll.run;

import edu.ecnu.dll.struct.task.Task;
import edu.ecnu.dll.struct.worker.Worker;
import tools.basic.BasicCalculation;

import java.util.ArrayList;
import java.util.List;

public class Solution {
    public static final int budgetSize = 3;

    private double getUlitityValue(List<Task> taskList, Double workerMaxRange, double[][] workerBudgetMatrix) {
        return 0;
    }

    public void complete(Task task, Worker[] workerArray) {
//        double distanceA = BasicCalculation.get2Norm(task.getLocation(), workerA.getLocation());
//        double distanceB = BasicCalculation.get2Norm(task.getLocation(), workerB.getLocation());

        double[] distanceArray = new double[workerArray.length];
        double[] utilityArray = new double[workerArray.length];
        double[][][] budgetMatrix = new double[1][workerArray.length][budgetSize];
        boolean[] competeState = new boolean[workerArray.length];

        for (int i = 0; i < workerArray.length; i++) {
            distanceArray[i] = BasicCalculation.get2Norm(task.getLocation(), workerArray[i].getLocation());
            utilityArray[i] = 0.0;
            competeState[i] = true;
        }

//        double utilityA = 0.0;
//        double utilityB = 0.0;

//        double[][] budgetMatrixA = new double[1][budgetSize];
        budgetMatrix[0][0] = new double[]{0.2, 0.3, 0.5};
//        double[][] budgetMatrixB = new double[1][budgetSize];
        budgetMatrix[0][1] = new double[]{0.3, 0.4, 0.3};


        List<Task> taskListA = new ArrayList<>();
        List<Task> taskListB = new ArrayList<>();
        double[][] taskTempWinner = new double[1][2];
        //初始化距离为最大距离值
        taskTempWinner[0][0] = Double.MAX_VALUE;
        //初始化对应距离的隐私预算为最大隐私预算
        taskTempWinner[0][1] = Double.MAX_VALUE;

        List<Integer>[] candidateWorkerID = new ArrayList[1];
        List<double[]>[] candidateWorkerDistanceAndBudget = new ArrayList[1];
        candidateWorkerID[0] = new ArrayList<>();
        candidateWorkerDistanceAndBudget[0] = new ArrayList<>();

        boolean stateValue = true;
        while (stateValue) {
//            double tempUtilityA = getUlitityValue(taskListA, workerA.getMaxRange(), budgetMatrixA);
//            double tempUtilityB = getUlitityValue(taskListA, workerA.getMaxRange(), budgetMatrixA);
            for (int i = 0; i < workerArray.length; i++) {
                //如果当前 worker 不需要竞争(上轮的胜利者)，就不作为
                if (competeState[i] == false) {
                    continue;
                }

                

            }
        }
    }
}
