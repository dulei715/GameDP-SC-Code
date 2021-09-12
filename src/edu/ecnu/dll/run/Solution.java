package edu.ecnu.dll.run;

import edu.ecnu.dll.struct.task.Task;
import edu.ecnu.dll.struct.worker.Worker;
import tools.basic.BasicCalculation;

import java.util.ArrayList;
import java.util.List;

public class Solution {
    public static final int budgetSize = 3;

    private double getUlitityValue(List<Task> taskList, Double workerMaxRange, double[][][] workerBudgetMatrix, int taskIndex, int workerIndex, int bugetIndex) {
        return 0;
    }

    public void complete(Task task, Worker[] workerArray) {
//        double distanceA = BasicCalculation.get2Norm(task.getLocation(), workerA.getLocation());
//        double distanceB = BasicCalculation.get2Norm(task.getLocation(), workerB.getLocation());

        double[] distanceArray = new double[workerArray.length];
        // 记录worker当前的效用函数值
        double[] utilityArray = new double[workerArray.length];
        // 记录worker竞争时临时效用函数值
        double[] tempUtilityArray = new double[workerArray.length];
        int[] workerBudgetIndex = new int[workerArray.length];
        double[][][] budgetMatrix = new double[1][workerArray.length][budgetSize];
        boolean[] competeState = new boolean[workerArray.length];

        for (int i = 0; i < workerArray.length; i++) {
            distanceArray[i] = BasicCalculation.get2Norm(task.getLocation(), workerArray[i].getLocation());
            utilityArray[i] = 0.0;
            workerBudgetIndex[i] = 0;
            competeState[i] = true;
        }

//        double utilityA = 0.0;
//        double utilityB = 0.0;

//        double[][] budgetMatrixA = new double[1][budgetSize];
        budgetMatrix[0][0] = new double[]{0.2, 0.3, 0.5};
//        double[][] budgetMatrixB = new double[1][budgetSize];
        budgetMatrix[0][1] = new double[]{0.3, 0.4, 0.3};


        List<Task> taskList = new ArrayList<>();

        // 记录当前竞争成功的worker的budget以及扰动距离
        double[][] taskTempWinner = new double[1][2];
        // 初始化距离为最大距离值
        taskTempWinner[0][0] = Double.MAX_VALUE;
        // 初始化对应距离的隐私预算为最大隐私预算
        taskTempWinner[0][1] = Double.MAX_VALUE;

        List<Integer>[] candidateWorkerID = new ArrayList[1];
        List<double[]>[] candidateWorkerDistanceAndBudget = new ArrayList[1];
        candidateWorkerID[0] = new ArrayList<>();
        candidateWorkerDistanceAndBudget[0] = new ArrayList<>();

        boolean stateValue = true;
        int taskIndex = 0;
        while (stateValue) {
//            double tempUtilityA = getUlitityValue(taskListA, workerA.getMaxRange(), budgetMatrixA);
//            double tempUtilityB = getUlitityValue(taskListA, workerA.getMaxRange(), budgetMatrixA);
            for (int i = 0; i < workerArray.length; i++) {
                //记性是否竞争判断1：如果当前 worker 不需要竞争(是上轮的胜利者)，就不作为
                if (competeState[i] == false) {
                    continue;
                }

                // 进行是否竞争判断2：如果发布当前隐私预算以及扰动距离长度是否会造成效用函数值下降，修改compteState为false，不作为
                tempUtilityArray[i] = getUlitityValue(taskList, workerArray[i].getMaxRange(), budgetMatrix, taskIndex, i, workerBudgetIndex[i]);

                // 进行是否竞争判断3：如果PPCF函数计算出来的距离大于之前胜利者的距离，修改compteState为false，不作为

                // 进行是否竞争判断4：根据将要竞争的预算，计算扰动的距离值。如果PCF函数计算出来的距离大于之前胜利者的距离，不作为

                // 否则（竞争成功），发布当前隐私预算和扰动距离长度
                

            }

            // 服务器遍历每个worker，找出竞争成功的worker，修改他的competeState为false，将其他竞争状态为true的workerBudgetIndex值加1
            //继续一轮竞争，直到除了胜利者其他的竞争者的competeState都为false.

        }
    }
}
