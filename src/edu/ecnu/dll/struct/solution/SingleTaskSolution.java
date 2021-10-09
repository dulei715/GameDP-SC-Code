package edu.ecnu.dll.struct.solution;

import edu.ecnu.dll.struct.task.BasicTask;
import edu.ecnu.dll.struct.task.Task;
import edu.ecnu.dll.struct.worker.SingleTaskBasicWorker;
import tools.basic.BasicCalculation;
import tools.differential_privacy.compare.impl.LaplaceProbabilityDensityFunction;
import tools.differential_privacy.noise.LaplaceUtils;

import java.util.ArrayList;
import java.util.List;

public class SingleTaskSolution {

    public static final int DISTANCE_TAG = 0;
    public static final int BUDGET_TAG = 1;


    public Task task = null;
    public SingleTaskBasicWorker[] workers = null;

    public static final int budgetSize = 3;

    private double getUlitityValue(List<Integer> taskList, Double workerMaxRange, double[][] workerBudgetMatrix, int taskIndex, int workerIndex, int bugetIndex) {
        return 0;
    }

    private double getIncrementUtility(double distance, double maximumRange, double addingPrivacyBudget) {
        return 1 + distance - addingPrivacyBudget;
    }

    public void initializeBasicInformation() {
        // todo: 初始化 task 位置，以及 workers 的位置
        this.task = new BasicTask(new double[]{0.0, 0.0});
        // todo: 初始化 workers 针对 task 的 privacy budget
        this.workers = new SingleTaskBasicWorker[2];
        this.workers[0].location = new double[]{2.0, 2.0};
        this.workers[0].maxRange = 4.0;
        this.workers[0].privacyBudgetArray = new Double[]{0.2, 0.3, 0.5};

        this.workers[1].location = new double[]{-1.5, -1.5};
        this.workers[1].maxRange = 3.0;
        this.workers[1].privacyBudgetArray = new Double[]{0.3, 0.4, 0.3};


    }

    public void initializeAgents() {
        for (int i = 0; i < this.workers.length; i++) {
            this.workers[i].toTaskDistance = BasicCalculation.get2Norm(this.task.location, this.workers[i].location);
            this.workers[i].alreadyPublishedEverageNoiseDistance = 0.0;
            this.workers[i].alreadyPublishedTotalPrivacyBudget = 0.0;
            this.workers[i].currentUtilityFunctionValue = 0.0;
            this.workers[i].budgetIndex = 0;
        }
    }

    public void complete() {


        // 记录worker竞争时临时效用函数值
        double[] tempUtilityArray = new double[this.workers.length];

        // 记录每个worker的申请到的task列表
//        todo: whether to add:  List<Integer>[] allocatedTaskIDListArray = new ArrayList[this.workers.length];

        // 针对该task，记录当前竞争成功的worker的ID
        int taskTempWinnerID = -1;
        // 针对该task，记录当前竞争成功的worker的budget以及扰动距离
        double[] taskTempWinnerInfo = new double[2];
        // 针对该task，初始化距离为最大距离值
        taskTempWinnerInfo[DISTANCE_TAG] = Double.MAX_VALUE;
        // 针对该task，初始化对应距离的隐私预算为最大隐私预算
        taskTempWinnerInfo[BUDGET_TAG] = Double.MAX_VALUE;

        // 针对该task，本轮提出竞争的worker的ID（每轮需要清空）
        List<Integer> candidateWorkerID;
        candidateWorkerID = new ArrayList<>();

        Integer[] candidateWorkerIDArray;
        double competeTemp;
        while (!candidateWorkerID.isEmpty()) {
            candidateWorkerIDArray = candidateWorkerID.toArray(new Integer[0]);
            candidateWorkerID.clear();
            for (Integer i : candidateWorkerIDArray) {
                //进行是否竞争判断1：如果当前 worker 不需要竞争(是上轮的胜利者)，就不作为
                if (i.equals(taskTempWinnerID)) {
                    continue;
                }

                // 进行是否竞争判断2：如果发布当前隐私预算以及扰动距离长度是否会造成效用函数值下降，修改compteState为false，不作为
//                tempUtilityArray[i] = getUlitityValue(allocatedTaskIDListArray[i], , workerArray[i].getMaxRange(), budgetMatrix, taskIndex, i, workerBudgetIndex[i]);
                double incrementUtility = getIncrementUtility(this.workers[i].toTaskDistance, this.workers[i].getMaxRange(), this.workers[i].privacyBudgetArray[this.workers[i].budgetIndex]);
                if (incrementUtility <= 0) {
//                    competeState[i] = false;
                    continue;
                }

                // 进行是否竞争判断3：如果PPCF函数计算出来的距离大于之前胜利者的距离，修改compteState为false，不作为
                //todo: 假设扰动距离的均值精度更高

                if (this.workers[i].toTaskDistance >= taskTempWinnerInfo[0]) {
//                    competeState[i] = false;
                    continue;
                }

                // 进行是否竞争判断4：如果隐私预算数量不足，不作为
                if (this.workers[i].budgetIndex >= this.workers[i].privacyBudgetArray.length) {
//                    competeState[i] = false;
                    continue;
                }


                // 进行是否竞争判断5：根据将要竞争的预算，计算扰动的距离值。如果PCF函数计算出来的距离大于之前胜利者的距离，不作为
                double newNoiseDistance = this.workers[i].toTaskDistance + LaplaceUtils.getLaplaceNoise(1, this.workers[i].privacyBudgetArray[this.workers[i].budgetIndex]);
                double competeDistance = BasicCalculation.getAverage(this.workers[i].alreadyPublishedEverageNoiseDistance, newNoiseDistance, this.workers[i].budgetIndex + 1);
                double totalBudget = this.workers[i].alreadyPublishedTotalPrivacyBudget + this.workers[i].privacyBudgetArray[this.workers[i].budgetIndex];
                double competeValue = LaplaceProbabilityDensityFunction.probabilityDensityFunction(competeDistance, taskTempWinnerInfo[DISTANCE_TAG], totalBudget, taskTempWinnerInfo[BUDGET_TAG]);
                if (competeValue <= 0.5) {
//                    competeState[i] = false;
                    continue;
                }


                // 否则（竞争成功），发布当前扰动距离长度和隐私预算(这里只添加进候选列表供server进一步选择)，并将隐私自己的预算索引值加1
                candidateWorkerID.add(i);
//                this.workers[i].toCompetePublishEverageNoiseDistance = competeDistance;
//                this.workers[i].toCompetePublishTotalPrivacyBudget = totalBudget;
                this.workers[i].alreadyPublishedEverageNoiseDistance = competeDistance;
                this.workers[i].alreadyPublishedTotalPrivacyBudget = totalBudget;
                this.workers[i].budgetIndex ++;
//                candidateWorkerDistanceAndBudget.add(new double[]{competeDistance, totalBudget});

            }

            // 服务器遍历每个worker，找出竞争成功的worker，修改他的competeState为false，将其他竞争状态为true的workerBudgetIndex值加1
            // todo: 设置 taskTempWinnerID 和 taskTempWinnerInfo
            //继续一轮竞争，直到除了胜利者其他的竞争者的competeState都为false.
            for (Integer i : candidateWorkerIDArray) {
//                competeTemp = LaplaceProbabilityDensityFunction.probabilityDensityFunction(this.workers[i].toCompetePublishEverageNoiseDistance, taskTempWinnerInfo[0], this.workers[i].toCompetePublishTotalPrivacyBudget, taskTempWinnerInfo[1]);
                competeTemp = LaplaceProbabilityDensityFunction.probabilityDensityFunction(this.workers[i].alreadyPublishedEverageNoiseDistance, taskTempWinnerInfo[DISTANCE_TAG], this.workers[i].alreadyPublishedTotalPrivacyBudget, taskTempWinnerInfo[BUDGET_TAG]);

                if (competeTemp > 0.5) {
                    taskTempWinnerID = i;
//                    taskTempWinnerInfo[i] = this.workers[i].toCompetePublishEverageNoiseDistance;
//                    taskTempWinnerInfo[i] = this.workers[i].toCompetePublishTotalPrivacyBudget;
                    taskTempWinnerInfo[DISTANCE_TAG] = this.workers[i].alreadyPublishedEverageNoiseDistance;
                    taskTempWinnerInfo[BUDGET_TAG] = this.workers[i].alreadyPublishedTotalPrivacyBudget;
                }
            }
        }
        System.out.println("The winner worker's id is " + taskTempWinnerID);
        System.out.println("The winner worker's noise distance is " + taskTempWinnerInfo[DISTANCE_TAG]);
        System.out.println("The winner worker's budget is " + taskTempWinnerInfo[BUDGET_TAG]);
    }


}
