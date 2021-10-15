package edu.ecnu.dll.scheme.solution;

import edu.ecnu.dll.basic_struct.pack.Candidate;
import edu.ecnu.dll.basic_struct.agent.Task;
import edu.ecnu.dll.basic_struct.agent.Worker;
import tools.basic.BasicCalculation;
import tools.differential_privacy.compare.impl.LaplaceProbabilityDensityFunction;
import tools.differential_privacy.noise.LaplaceUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class SolutionBefore {
    public static final int budgetSize = 3;

    private double getUlitityValue(List<Integer> taskList, Double workerMaxRange, double[][] workerBudgetMatrix, int taskIndex, int workerIndex, int bugetIndex) {
        return 0;
    }

    private double getIncrementUtility(double[] distanceArray, double maximumRange, double[] addingPrivacyBudget) {
        return distanceArray.length + BasicCalculation.getSum(distanceArray) - BasicCalculation.getSum(addingPrivacyBudget);
    }

    public void complete(Task task, Worker[] workerArray) {

        TreeSet<Candidate> candidatesSet = new TreeSet<>();

        // 针对该task，记录所有worker到其距离
        double[] toTaskDistanceArray = new double[workerArray.length];
        // 针对该task，记录之前发布的噪声平均距离和隐私预算
        double[][] lastTimeNoiseDistanceAndBudget = new double[workerArray.length][2];
        // 记录worker当前的效用函数值
        double[] utilityArray = new double[workerArray.length];
        // 记录worker竞争时临时效用函数值
        double[] tempUtilityArray = new double[workerArray.length];
        // 记录竞争中worker对于该task用到第几个budget了(从0开始)
        int[] workerBudgetIndex = new int[workerArray.length];
        // 记录worker对于该task的一组budget值
        double[][] budgetMatrix = new double[workerArray.length][budgetSize];
        // 针对该task，记录当前worker是否要竞争
        boolean[] competeState = new boolean[workerArray.length];

        for (int i = 0; i < workerArray.length; i++) {
            toTaskDistanceArray[i] = BasicCalculation.get2Norm(task.getLocation(), workerArray[i].getLocation());
            lastTimeNoiseDistanceAndBudget[i][0] = lastTimeNoiseDistanceAndBudget[i][1] = 0.0;
            utilityArray[i] = 0.0;
            workerBudgetIndex[i] = 0;
            competeState[i] = true;
        }

//        double utilityA = 0.0;
//        double utilityB = 0.0;

        budgetMatrix[0] = new double[]{0.2, 0.3, 0.5};
        budgetMatrix[1] = new double[]{0.3, 0.4, 0.3};


        // 记录每个worker的申请到的task列表
        List<Integer>[] allocatedTaskIDListArray = new ArrayList[workerArray.length];

        // 针对该task，记录当前竞争成功的worker的ID
        int taskTempWinnerID = -1;
        // 针对该task，记录当前竞争成功的worker的budget以及扰动距离
        double[] taskTempWinnerInfo = new double[2];
        // 针对该task，初始化距离为最大距离值
        taskTempWinnerInfo[0] = Double.MAX_VALUE;
        // 针对该task，初始化对应距离的隐私预算为最大隐私预算
        taskTempWinnerInfo[1] = Double.MAX_VALUE;

        // 针对该task，本轮提出竞争的worker的ID（每轮需要清空）
        List<Integer> candidateWorkerID;
        // 针对该task，本轮提出竞争的worker的距离和隐私预算（每轮需要清空）
        List<double[]> candidateWorkerDistanceAndBudget;

        candidateWorkerID = new ArrayList<>();
        candidateWorkerDistanceAndBudget = new ArrayList<>();
//        initializeCandidate()

        boolean stateValue = true;
        int taskIndex = 0;
        while (stateValue) {
//            double tempUtilityA = getUlitityValue(taskListA, workerA.getMaxRange(), budgetMatrixA);
//            double tempUtilityB = getUlitityValue(taskListA, workerA.getMaxRange(), budgetMatrixA);
            candidateWorkerID.clear();
            candidateWorkerDistanceAndBudget.clear();
            for (int i = 0; i < workerArray.length; i++) {
                //记性是否竞争判断1：如果当前 worker 不需要竞争(是上轮的胜利者)，就不作为
                if (competeState[i] == false) {
                    continue;
                }

                // 进行是否竞争判断2：如果发布当前隐私预算以及扰动距离长度是否会造成效用函数值下降，修改compteState为false，不作为
//                tempUtilityArray[i] = getUlitityValue(allocatedTaskIDListArray[i], , workerArray[i].getMaxRange(), budgetMatrix, taskIndex, i, workerBudgetIndex[i]);
                double incrementUtility = getIncrementUtility(new double[]{toTaskDistanceArray[i]}, workerArray[i].getMaxRange(), new double[]{budgetMatrix[i][workerBudgetIndex[i]]});
                if (incrementUtility <= 0) {
                    competeState[i] = false;
                    continue;
                }

                // 进行是否竞争判断3：如果PPCF函数计算出来的距离大于之前胜利者的距离，修改compteState为false，不作为
                //todo: 假设扰动距离的均值精度更高

                if (toTaskDistanceArray[i] >= taskTempWinnerInfo[0]) {
                    continue;
                }


                // 进行是否竞争判断4：根据将要竞争的预算，计算扰动的距离值。如果PCF函数计算出来的距离大于之前胜利者的距离，不作为
                double newNoiseDistance = toTaskDistanceArray[i] + LaplaceUtils.getLaplaceNoise(1, budgetMatrix[i][workerBudgetIndex[i]]);
                double competeDistance = BasicCalculation.getAverage(lastTimeNoiseDistanceAndBudget[i][0], newNoiseDistance, workerBudgetIndex[i] + 1);
                double totalBudget = lastTimeNoiseDistanceAndBudget[i][1] + budgetMatrix[i][workerBudgetIndex[i]];
                double competeValue = LaplaceProbabilityDensityFunction.probabilityDensityFunction(competeDistance, taskTempWinnerInfo[0], totalBudget, taskTempWinnerInfo[1]);
                if (competeValue <= 0.5) {
                    continue;
                }


                // 否则（竞争成功），发布当前隐私预算和扰动距离长度
                candidateWorkerID.add(i);
                candidateWorkerDistanceAndBudget.add(new double[]{competeDistance, totalBudget});

            }

            // 服务器遍历每个worker，找出竞争成功的worker，修改他的competeState为false，将其他竞争状态为true的workerBudgetIndex值加1
            //继续一轮竞争，直到除了胜利者其他的竞争者的competeState都为false.

        }
    }
}
