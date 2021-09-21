package edu.ecnu.dll.run;

import edu.ecnu.dll.struct.task.BasicTask;
import edu.ecnu.dll.struct.task.Task;
import edu.ecnu.dll.struct.worker.NormalWorker;
import tools.basic.BasicArray;
import tools.basic.BasicCalculation;
import tools.differential_privacy.compare.impl.LaplaceProbabilityDensityFunction;
import tools.differential_privacy.noise.LaplaceUtils;

import java.util.ArrayList;
import java.util.List;

public class MultiTaskSolution {

    public Task[] task = null;
    public NormalWorker[] workers = null;

    public static final int budgetSize = 3;

    private double getUlitityValue(List<Integer> taskList, Double workerMaxRange, double[][] workerBudgetMatrix, int taskIndex, int workerIndex, int bugetIndex) {
        return 0;
    }

    private double getIncrementUtility(double distance, double maximumRange, double addingPrivacyBudget) {
        return 1 + distance - addingPrivacyBudget;
    }

    public void initializeBasicInformation() {
        // todo: 初始化 task 位置，以及 workers 的位置
        this.task = new Task[]{new BasicTask(new double[]{0.0, 0.0}), new BasicTask(new double[]{1.0, 1.0})};
        // todo: 初始化 workers 针对 task 的 privacy budget
        this.workers = new NormalWorker[4];
        this.workers[0].location = new double[]{2.0, 2.0};
        this.workers[0].maxRange = 4.0;
        this.workers[0].privacyBudgetArray = new Double[]{0.2, 0.3, 0.5};

        this.workers[1].location = new double[]{-1.5, -1.5};
        this.workers[1].maxRange = 3.0;
        this.workers[1].privacyBudgetArray = new Double[]{0.3, 0.4, 0.3};

        this.workers[2].location = new double[]{0.5, 0.5};
        this.workers[2].maxRange = 2.0;
        this.workers[2].privacyBudgetArray = new Double[]{0.4, 0.6, 0.2};

        this.workers[3].location = new double[]{2.5, 2.5};
        this.workers[3].maxRange = 3.5;
        this.workers[3].privacyBudgetArray = new Double[]{0.3, 0.4, 0.3};


    }

    public void initializeAgents() {
        for (int j = 0; j < this.workers.length; j++) {
            for (int i = 0; i < task.length; i++) {
                this.workers[j].toTaskDistance[i] = BasicCalculation.get2Norm(this.task[i].location, this.workers[i].location);
                this.workers[j].alreadyPublishedEverageNoiseDistance[i] = 0.0;
                this.workers[j].alreadyPublishedTotalPrivacyBudget[i] = 0.0;
                this.workers[j].currentUtilityFunctionValue = 0.0;
                this.workers[j].budgetIndex[i] = 0;
            }
        }
    }

    private void setCandidateTaskByBudget(List<Integer> tempCandidateTaskList, NormalWorker worker) {
        for (int i = 0; i < worker.privacyBudgetArray.length; i++) {
            if (worker.budgetIndex[i] < worker.privacyBudgetArray[i].length) {
                tempCandidateTaskList.add(i);
            }
        }
    }

    public void complete() {


        // 记录worker竞争时临时效用函数值
        double[] tempUtilityArray = new double[this.workers.length];

        // 记录每个worker的申请到的task列表
        List<Integer>[] allocatedTaskIDListArray = new ArrayList[this.workers.length];


        // 针对每个task，记录当前竞争成功的worker的ID
        int[] taskTempWinnerID = new int[this.task.length];
        BasicArray.setIntArrayTo(taskTempWinnerID, -1);

        // 针对每个task，记录当前竞争成功的worker的budget以及扰动距离
        double[][] taskTempWinnerInfo = new double[this.task.length][2];

        // 针对每个task，初始化距离为最大距离值
        // 针对每个task，初始化对应距离的隐私预算为最大隐私预算
        for (int i = 0; i < this.task.length; i++) {
            taskTempWinnerInfo[i][0] = Double.MAX_VALUE;
            taskTempWinnerInfo[i][1] = Double.MAX_VALUE;
        }

        // 针对每个task，本轮提出竞争的worker的ID（每轮需要清空）
        List<Integer>[] candidateWorkerIDList;
        candidateWorkerIDList = new ArrayList[this.task.length];
        BasicArray.setListArrayToEmptyList(candidateWorkerIDList);


        List<Integer>[] candidateWorkerIDListArray;
        double competeTemp;
        int totalCompleteWorkerNumber = this.workers.length;

        // 用来临时记录每个worker经过对budget使用情况考察后，能够去进行竞争的tasks
        List<Integer> tempCandidateTaskList = new ArrayList<>();

        while (totalCompleteWorkerNumber > 0) {
//        while (!candidateWorkerIDList.isEmpty()) {
            totalCompleteWorkerNumber = 0;

            candidateWorkerIDListArray = candidateWorkerIDList;
            candidateWorkerIDList = new ArrayList[candidateWorkerIDListArray.length];
            BasicArray.setListArrayToEmptyList(candidateWorkerIDList);

            /*
             * 遍历每个 task 对应的候选集合中的worker。
             * 每个 worker 对所有的 tasks 进行竞争。但只能挑出其中 1 个 task 作为最终竞争对象。
             * 每轮结束后统计剩余的总的将要竞争的 workers 的数量。
             */
            for (int k = 0; k < candidateWorkerIDListArray.length; k++) {

                for (Integer i : candidateWorkerIDListArray[k]) {
                    //进行是否竞争判断1：如果当前 worker 不需要竞争(是上轮的胜利者)，就不作为
                    if (i.equals(taskTempWinnerID[k])) {
                        continue;
                    }

                    // 进行是否竞争判断2：计算出所有预算充足的 task ID 的集合。如果该集合为空，则不作为
                    tempCandidateTaskList.clear();
                    setCandidateTaskByBudget(tempCandidateTaskList, this.workers[i]);
                    if (tempCandidateTaskList.isEmpty()) {
                        continue;
                    }

                    //todo: 好好设计一下：
                    // 进行是否竞争判断3：如果发布当前隐私预算以及扰动距离长度是否会造成效用函数值下降，修改compteState为false，不作为
                    double incrementUtility = getIncrementUtility(this.workers[i].toTaskDistance, this.workers[i].getMaxRange(), this.workers[i].privacyBudgetArray[this.workers[i].budgetIndex]);
                    if (incrementUtility <= 0) {
//                    competeState[i] = false;
                        continue;
                    }

                    // 进行是否竞争判断3_(1)：如果PPCF函数计算出来的距离大于之前胜利者的距离，修改compteState为false，不作为
                    //todo: 假设扰动距离的均值精度更高

                    if (this.workers[i].toTaskDistance >= taskTempWinnerInfo[0]) {
//                    competeState[i] = false;
                        continue;
                    }




                    // 进行是否竞争判断3_(2)：根据将要竞争的预算，计算扰动的距离值。如果PCF函数计算出来的距离大于之前胜利者的距离，不作为
                    double newNoiseDistance = this.workers[i].toTaskDistance + LaplaceUtils.getLaplaceNoise(1, this.workers[i].privacyBudgetArray[this.workers[i].budgetIndex]);
                    double competeDistance = BasicCalculation.getAverage(this.workers[i].alreadyPublishedEverageNoiseDistance, newNoiseDistance, this.workers[i].budgetIndex + 1);
                    double totalBudget = this.workers[i].alreadyPublishedTotalPrivacyBudget + this.workers[i].privacyBudgetArray[this.workers[i].budgetIndex];
                    double competeValue = LaplaceProbabilityDensityFunction.probabilityDensityFunction(competeDistance, taskTempWinnerInfo[0], totalBudget, taskTempWinnerInfo[1]);
                    if (competeValue <= 0.5) {
//                    competeState[i] = false;
                        continue;
                    }


                    // 否则（竞争成功），发布当前扰动距离长度和隐私预算(这里只添加进候选列表供server进一步选择)，并将隐私自己的预算索引值加1
                    candidateWorkerIDList.add(i);
//                this.workers[i].toCompetePublishEverageNoiseDistance = competeDistance;
//                this.workers[i].toCompetePublishTotalPrivacyBudget = totalBudget;
                    this.workers[i].alreadyPublishedEverageNoiseDistance = competeDistance;
                    this.workers[i].alreadyPublishedTotalPrivacyBudget = totalBudget;
                    this.workers[i].budgetIndex ++;
//                candidateWorkerDistanceAndBudget.add(new double[]{competeDistance, totalBudget});
                }
            }


            for (Integer i : candidateWorkerIDListArray) {


            }

            // 服务器遍历每个worker，找出竞争成功的worker，修改他的competeState为false，将其他竞争状态为true的workerBudgetIndex值加1
            // todo: 设置 taskTempWinnerID 和 taskTempWinnerInfo
            //继续一轮竞争，直到除了胜利者其他的竞争者的competeState都为false.
            for (Integer i : candidateWorkerIDListArray) {
//                competeTemp = LaplaceProbabilityDensityFunction.probabilityDensityFunction(this.workers[i].toCompetePublishEverageNoiseDistance, taskTempWinnerInfo[0], this.workers[i].toCompetePublishTotalPrivacyBudget, taskTempWinnerInfo[1]);
                competeTemp = LaplaceProbabilityDensityFunction.probabilityDensityFunction(this.workers[i].alreadyPublishedEverageNoiseDistance, taskTempWinnerInfo[0], this.workers[i].alreadyPublishedTotalPrivacyBudget, taskTempWinnerInfo[1]);

                if (competeTemp > 0.5) {
                    taskTempWinnerID = i;
//                    taskTempWinnerInfo[i] = this.workers[i].toCompetePublishEverageNoiseDistance;
//                    taskTempWinnerInfo[i] = this.workers[i].toCompetePublishTotalPrivacyBudget;
                    taskTempWinnerInfo[i] = this.workers[i].alreadyPublishedEverageNoiseDistance;
                    taskTempWinnerInfo[i] = this.workers[i].alreadyPublishedTotalPrivacyBudget;
                }
            }
        }
        System.out.println("The winner worker's id is " + taskTempWinnerID);
        System.out.println("The winner worker's noise distance is " + taskTempWinnerInfo[0]);
        System.out.println("The winner worker's budget is " + taskTempWinnerInfo[1]);
    }
}
