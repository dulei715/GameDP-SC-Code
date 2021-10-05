package edu.ecnu.dll.run;

import edu.ecnu.dll.struct.pack.TaskIDDistanceBugetPair;
import edu.ecnu.dll.struct.task.BasicTask;
import edu.ecnu.dll.struct.task.Task;
import edu.ecnu.dll.struct.worker.NormalWorker;
import tools.basic.BasicArray;
import tools.basic.BasicCalculation;
import tools.differential_privacy.compare.impl.LaplaceProbabilityDensityFunction;
import tools.io.MyPrint;

import java.util.ArrayList;
import java.util.List;

public class MultiTaskSolution {

    public Task[] tasks = null;
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
        this.tasks = new Task[]{new BasicTask(new double[]{0.0, 0.0}), new BasicTask(new double[]{1.0, 1.0})};
        // todo: 初始化 workers 针对 task 的 privacy budget
        this.workers = new NormalWorker[4];
        this.workers[0].location = new double[]{2.0, 2.0};
        this.workers[0].maxRange = 4.0;
        this.workers[0].privacyBudgetArray = new Double[][]{new Double[]{0.2, 0.3, 0.5}, new Double[]{0.3, 0.4, 0.6}};

        this.workers[1].location = new double[]{-1.5, -1.5};
        this.workers[1].maxRange = 3.0;
        this.workers[1].privacyBudgetArray = new Double[][]{new Double[]{0.3, 0.4, 0.3}, new Double[]{0.2, 0.3, 0.4}};

        this.workers[2].location = new double[]{0.5, 0.5};
        this.workers[2].maxRange = 2.0;
        this.workers[2].privacyBudgetArray = new Double[][]{new Double[]{0.4, 0.6, 0.2}, new Double[]{0.5, 0.2, 0.3}};

        this.workers[3].location = new double[]{2.5, 2.5};
        this.workers[3].maxRange = 3.5;
        this.workers[3].privacyBudgetArray = new Double[][]{new Double[]{0.3, 0.4, 0.3}, new Double[]{0.2, 0.5, 0.3}};


    }

    public void initializeAgents() {
        for (int j = 0; j < this.workers.length; j++) {
            for (int i = 0; i < tasks.length; i++) {
                this.workers[j].toTaskDistance[i] = BasicCalculation.get2Norm(this.tasks[i].location, this.workers[i].location);
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
        int[] taskTempWinnerID = new int[this.tasks.length];
        BasicArray.setIntArrayTo(taskTempWinnerID, -1);

        // 针对每个task，记录当前竞争成功的worker的budget以及扰动距离
        double[][] taskTempWinnerInfo = new double[this.tasks.length][2];

        // 针对每个task，初始化距离为最大距离值
        // 针对每个task，初始化对应距离的隐私预算为最大隐私预算
        for (int i = 0; i < this.tasks.length; i++) {
            taskTempWinnerInfo[i][0] = Double.MAX_VALUE;
            taskTempWinnerInfo[i][1] = Double.MAX_VALUE;
        }

        // 针对每个task，本轮提出竞争的worker的ID（每轮需要清空）
        List<Integer>[] candidateWorkerIDList;
        candidateWorkerIDList = new ArrayList[this.tasks.length];
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
                    // 进行是否竞争判断3： 遍历所有的可选task, 选出最大使得自身Utility增加最大的task, 如果为空，则不作为。
                    TaskIDDistanceBugetPair maxIncrementUtilityInfo;
                    maxIncrementUtilityInfo = getMaxIncrementUtilityTask(tempCandidateTaskList, this.workers[i], candidateWorkerIDListArray, taskTempWinnerID, taskTempWinnerInfo);
                    if (maxIncrementUtilityInfo == null) {
                        continue;
                    }

                    // 否则（竞争成功），发布当前扰动距离长度和隐私预算(这里只添加进候选列表供server进一步选择)，并将隐私自己的预算索引值加1
                    candidateWorkerIDList[maxIncrementUtilityInfo.taskID].add(i);
                    this.workers[i].alreadyPublishedEverageNoiseDistance[maxIncrementUtilityInfo.taskID] = maxIncrementUtilityInfo.noiseEverageDistance;
                    this.workers[i].alreadyPublishedTotalPrivacyBudget[maxIncrementUtilityInfo.taskID] = maxIncrementUtilityInfo.totalPrivacyBudget;
                    this.workers[i].budgetIndex[maxIncrementUtilityInfo.taskID] ++;
                    totalCompleteWorkerNumber ++;
                }
            }


            // 服务器遍历每个worker，找出竞争成功的worker，修改他的competeState为false，将其他竞争状态为true的workerBudgetIndex值加1
            // todo: 设置 taskTempWinnerID 和 taskTempWinnerInfo
            //继续一轮竞争，直到除了胜利者其他的竞争者的competeState都为false.
            for (int k = 0; k < candidateWorkerIDListArray.length; k++) {
                for (Integer i : tempCandidateTaskList) {
                    competeTemp = LaplaceProbabilityDensityFunction.probabilityDensityFunction(this.workers[i].alreadyPublishedEverageNoiseDistance[k], taskTempWinnerInfo[k][0], this.workers[i].alreadyPublishedTotalPrivacyBudget[k], taskTempWinnerInfo[k][1]);
                    if (competeTemp > 0.5) {
                        taskTempWinnerID[k] = i;
                        taskTempWinnerInfo[k][0] = this.workers[i].alreadyPublishedEverageNoiseDistance[k];
                        taskTempWinnerInfo[k][1] = this.workers[i].alreadyPublishedTotalPrivacyBudget[k];
                    }
                }
            }
        }
//        System.out.println("The winner worker's id is " + taskTempWinnerID);
//        System.out.println("The winner worker's noise distance is " + taskTempWinnerInfo[0]);
//        System.out.println("The winner worker's budget is " + taskTempWinnerInfo[1]);
        MyPrint.showIntegerArray(taskTempWinnerID);
        MyPrint.showDoubleArray(taskTempWinnerInfo[0]);
        MyPrint.showDoubleArray(taskTempWinnerInfo[1]);
    }

    /**
     *
     * @param taskIDList 候选的要去竞争的所有task(已经排除budget不足的task)
     * @param worker 要竞争的worker
     * @param candidateWorkerIDListArray 记录上轮竞争中竞争每个task的worker的集合
     * @param lastTermTaskWinnerID 记录上轮竞争中成功竞争每个task的workerID
     * @param lastTermTaskWinnerInfo 记录上轮竞争中成功竞争每个task的worker其他信息
     * @return
     */
    private TaskIDDistanceBugetPair getMaxIncrementUtilityTask(List<Integer> taskIDList, NormalWorker worker, List<Integer>[] candidateWorkerIDListArray, int[] lastTermTaskWinnerID, double[][] lastTermTaskWinnerInfo) {
        // 遍历taskIDList中的所有task，找出能使当前worker的utility增加最大的task，返回（该task的ID, 申请即将泄露的平均distance[此处可返回扰动], 申请泄露的budget总和[此处可返回当前budget]）
        return null;
    }
}
