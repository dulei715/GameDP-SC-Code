package edu.ecnu.dll.scheme.solution._3_multiple_task;

import edu.ecnu.dll.basic_struct.pack.DistanceBudgetPair;
import edu.ecnu.dll.basic_struct.pack.TaskIDDistanceBudgetPairProposingValue;
import edu.ecnu.dll.basic_struct.pack.EntropyInfo;
import edu.ecnu.dll.scheme.struct.task.BasicTask;
import edu.ecnu.dll.basic_struct.agent.Task;
import edu.ecnu.dll.scheme.struct.worker.MultiTaskBasicWorker;
import tools.basic.BasicArray;
import tools.basic.BasicCalculation;
import tools.differential_privacy.compare.impl.LaplaceProbabilityDensityFunction;
import tools.differential_privacy.noise.LaplaceUtils;
import tools.io.print.MyPrint;
import tools.struct.Point;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.TreeSet;

public class MultiTaskSingleCompetitionSolution {

    public static final int DISTANCE_TAG = 0;
    public static final int BUDGET_TAG = 1;

    public static final double alpha = 1;
    public static final double beta = 1;

    public Task[] tasks = null;
    public MultiTaskBasicWorker[] workers = null;

    public static final int budgetSize = 3;

    private double getUtilityValue(double taskValue, double effectivePrivacyBudget, double realDistance, double privacyBudgetCost) {
        return taskValue + taskValue * effectivePrivacyBudget - alpha * realDistance - beta * privacyBudgetCost;
    }

//    protected double getIncrementUtility(double distance, double maximumRange, double addingPrivacyBudget) {
//        return 1 + distance - addingPrivacyBudget;
//    }

//    public void initializeBasicInformation() {
//        // todo: 初始化 task 位置，以及 workers 的位置
//        this.tasks = new Task[]{new BasicTask(new double[]{0.0, 0.0}), new BasicTask(new double[]{1.0, 1.0})};
//        // todo: 初始化 workers 针对 task 的 privacy budget
//        this.workers = new MultiTaskBasicWorker[4];
//        this.workers[0].location = new double[]{2.0, 2.0};
//        this.workers[0].maxRange = 4.0;
//        this.workers[0].privacyBudgetArray = new Double[][]{new Double[]{0.2, 0.3, 0.5}, new Double[]{0.3, 0.4, 0.6}};
//
//        this.workers[1].location = new double[]{-1.5, -1.5};
//        this.workers[1].maxRange = 3.0;
//        this.workers[1].privacyBudgetArray = new Double[][]{new Double[]{0.3, 0.4, 0.3}, new Double[]{0.2, 0.3, 0.4}};
//
//        this.workers[2].location = new double[]{0.5, 0.5};
//        this.workers[2].maxRange = 2.0;
//        this.workers[2].privacyBudgetArray = new Double[][]{new Double[]{0.4, 0.6, 0.2}, new Double[]{0.5, 0.2, 0.3}};
//
//        this.workers[3].location = new double[]{2.5, 2.5};
//        this.workers[3].maxRange = 3.5;
//        this.workers[3].privacyBudgetArray = new Double[][]{new Double[]{0.3, 0.4, 0.3}, new Double[]{0.2, 0.5, 0.3}};
//
//
//    }

    public void initializeBasicInformation(List<Point> taskPositionList, Double[] taskValueArray, List<Point> workerPositionList, List<Double[]>[] privacyBudgetListArray) {
        Point taskPosition, workerPosition;
        this.tasks = new BasicTask[taskPositionList.size()];
        for (int i = 0; i < taskPositionList.size(); i++) {
            taskPosition = taskPositionList.get(i);
            this.tasks[i] = new BasicTask(taskPosition.getIndex());
            this.tasks[i].valuation = taskValueArray[i];
        }
        this.workers = new MultiTaskBasicWorker[workerPositionList.size()];
        for (int j = 0; j < workers.length; j++) {
            workerPosition = workerPositionList.get(j);
            this.workers[j] = new MultiTaskBasicWorker(workerPosition.getIndex());
            this.workers[j].privacyBudgetArray = privacyBudgetListArray[j].toArray(new Double[0][0]);
//            for (int i = 0; i < this.tasks.length; i++) {
//                this.workers[j].privacyBudgetArray[i] = privacyBudgetListArray
//            }
        }

    }

    public void initializeAgents() {
        for (int j = 0; j < this.workers.length; j++) {
            this.workers[j].toTaskDistance = new Double[this.tasks.length];
            // 此处没有初始化privacyBudgetArray，因为会在initialize basic function 中初始化
            this.workers[j].budgetIndex = new int[this.tasks.length];
            this.workers[j].alreadyPublishedNoiseDistanceAndBudgetTreeSetArray = new TreeSet[this.tasks.length];
            this.workers[j].effectiveNoiseDistance = new Double[this.tasks.length];
            this.workers[j].effectivePrivacyBudget = new Double[this.tasks.length];
            this.workers[j].privacyBudgetCost = new Double[this.tasks.length];
            this.workers[j].taskCompletingTimes = new Integer[this.tasks.length];
            for (int i = 0; i < tasks.length; i++) {
                this.workers[j].toTaskDistance[i] = BasicCalculation.get2Norm(this.tasks[i].location, this.workers[j].location);
                this.workers[j].budgetIndex[i] = 0;
                this.workers[j].alreadyPublishedNoiseDistanceAndBudgetTreeSetArray[i] = new TreeSet<>();
                this.workers[j].completeUtilityFunctionValue = BasicArray.getInitializedArray(0.0, this.tasks.length);
                this.workers[j].effectiveNoiseDistance[i] = 0.0;
                this.workers[j].effectivePrivacyBudget[i] = 0.0;
                this.workers[j].privacyBudgetCost[i] = 0.0;
                this.workers[j].taskCompletingTimes[i] = 0;
                
            }
        }
    }

    protected void setCandidateTaskByBudget(List<Integer> tempCandidateTaskList, MultiTaskBasicWorker worker) {
        for (int i = 0; i < worker.privacyBudgetArray.length; i++) {
            if (worker.budgetIndex[i] < worker.privacyBudgetArray[i].length) {
                tempCandidateTaskList.add(i);
            }
        }
    }

    private void initializeAllocationByOrderedAllocation(List<Integer>[] newCandidateWorkerIDList, Integer[] taskCurrentWinnerIDArray, Double[][] taskCurrentWinnerInfoArray, Integer[] competingTimes, HashSet<Integer>[] completedWorkerIDSet) {
        // 针对每个task，初始化距离为最大距离值
        // 针对每个task，初始化对应距离的隐私预算为最大隐私预算
        // 针对每个task，初始化总的被竞争次数为0
        // 针对每个task，初始化访问过被访问worker集合为空集合
        for (int i = 0; i < this.tasks.length; i++) {
            newCandidateWorkerIDList[i] = new ArrayList<>();
            taskCurrentWinnerInfoArray[i][DISTANCE_TAG] = Double.MAX_VALUE;
            taskCurrentWinnerInfoArray[i][BUDGET_TAG] = Double.MAX_VALUE;
            competingTimes[i] = 0;
            completedWorkerIDSet[i] = new HashSet<>();
        }
    }

    public void complete() {

        // 记录每个task的被竞争的总次数
        Integer[] competingTimes = new Integer[this.tasks.length];
        // 记录每个task被竞争过的worker id的集合
        HashSet<Integer>[] completedWorkerIDSet = new HashSet[this.tasks.length];

        // 记录worker竞争时临时效用函数值
        double[] tempUtilityArray = new double[this.workers.length];

        // 记录每个worker的申请到的task列表
        // List<Integer>[] allocatedTaskIDListArray = new ArrayList[this.workers.length];


        // 针对每个task，记录当前竞争成功的worker的ID
        Integer[] taskCurrentWinnerIDArray = new Integer[this.tasks.length];
        BasicArray.setIntArrayTo(taskCurrentWinnerIDArray, -1);
        // 针对每个task，记录当前竞争成功的worker的budget以及扰动距离
        Double[][] taskCurrentWinnerInfoArray = new Double[this.tasks.length][2];

        // todo: 封装到了initializeAllocations

        // 针对每个task，本轮提出竞争的worker的ID（每轮需要清空）
        List<Integer>[] newCandidateWorkerIDList, oldCandidateWorkerIDList;
        newCandidateWorkerIDList = new ArrayList[this.tasks.length];
//        BasicArray.setListArrayToEmptyList(newCandidateWorkerIDList);
        initializeAllocationByOrderedAllocation(newCandidateWorkerIDList, taskCurrentWinnerIDArray, taskCurrentWinnerInfoArray, competingTimes, completedWorkerIDSet);


        double competeTemp;
        // 用于记录当前竞争的总的worker数量
        int totalCompleteWorkerNumber = this.workers.length;

        // 用来临时记录每个worker经过对budget使用情况考察后，能够去进行竞争的tasks
        List<Integer> tempCandidateTaskList = new ArrayList<>();

        while (totalCompleteWorkerNumber > 0) {
//        while (!candidateWorkerIDList.isEmpty()) {
            totalCompleteWorkerNumber = 0;

            oldCandidateWorkerIDList = newCandidateWorkerIDList;
            newCandidateWorkerIDList = new ArrayList[oldCandidateWorkerIDList.length];
            BasicArray.setListArrayToEmptyList(newCandidateWorkerIDList);

            /*
             * 遍历每个 task 对应的候选集合中的worker。
             * 每个 worker 对所有的 tasks 进行竞争。但只能挑出其中 1 个 task 作为最终竞争对象。
             * 每轮结束后统计剩余的总的将要竞争的 workers 的数量。
             */
            for (int i = 0; i < oldCandidateWorkerIDList.length; i++) {

                for (Integer j : oldCandidateWorkerIDList[i]) {
                    //进行是否竞争判断1：如果当前 worker 不需要竞争(是上轮的胜利者)，就不作为
                    if (j.equals(taskCurrentWinnerIDArray[i])) {
                        continue;
                    }

                    // 进行是否竞争判断2：计算出所有预算充足的 task ID 的集合。如果该集合为空，则不作为
                    tempCandidateTaskList.clear();
                    setCandidateTaskByBudget(tempCandidateTaskList, this.workers[j]);
                    if (tempCandidateTaskList.isEmpty()) {
                        continue;
                    }


                    //todo: 好好设计一下：
                    // 进行是否竞争判断3： 考察 utility函数、PPCF函数、PCF函数、任务熵

                    EntropyInfo winnerInfo;
                    winnerInfo = chooseByTaskEntropy(tempCandidateTaskList, j, competingTimes, taskCurrentWinnerIDArray, taskCurrentWinnerInfoArray, completedWorkerIDSet);
//                    winnerInfo = chooseByTaskEntropy();
                    if (winnerInfo == null) {
                        continue;
                    }

                    // 否则（竞争成功），发布当前扰动距离长度和隐私预算(这里只添加进候选列表供server进一步选择)，并将隐私自己的预算索引值加1
                    newCandidateWorkerIDList[winnerInfo.taskID].add(j);
                    completedWorkerIDSet[winnerInfo.taskID].add(j);
                    this.workers[j].alreadyPublishedNoiseDistanceAndBudgetTreeSetArray[winnerInfo.taskID].add(new DistanceBudgetPair(winnerInfo.newNoiseDistance, winnerInfo.newPrivacyBudget));
                    this.workers[j].effectiveNoiseDistance[winnerInfo.taskID] = winnerInfo.noiseEffectiveDistance;
                    this.workers[j].effectivePrivacyBudget[winnerInfo.taskID] = winnerInfo.effectivePrivacyBudget;
                    this.workers[j].completeUtilityFunctionValue[winnerInfo.taskID] = winnerInfo.newUtilityValue;
                    this.workers[j].budgetIndex[winnerInfo.taskID] ++;

                    this.workers[j].taskCompletingTimes[i] ++;
                    totalCompleteWorkerNumber ++;
                }
            }


            // 服务器遍历每个worker，找出竞争成功的worker，修改他的competeState为false
            // todo: 设置 taskTempWinnerID 和 taskTempWinnerInfo
            Integer[] taskBeforeWinnerIDArray = new Integer[newCandidateWorkerIDList.length];
            for (int i = 0; i < newCandidateWorkerIDList.length; i++) {
                if (newCandidateWorkerIDList[i].size() == 0) {
                    continue;
                }
                competingTimes[i] += newCandidateWorkerIDList[i].size();
                taskBeforeWinnerIDArray[i] = taskCurrentWinnerIDArray[i];
                for (Integer j : newCandidateWorkerIDList[i]) {
                    competeTemp = LaplaceProbabilityDensityFunction.probabilityDensityFunction(this.workers[j].effectiveNoiseDistance[i], taskCurrentWinnerInfoArray[i][DISTANCE_TAG], this.workers[j].effectivePrivacyBudget[i], taskCurrentWinnerInfoArray[i][BUDGET_TAG]);
                    if (competeTemp > 0.5) {
                        taskCurrentWinnerIDArray[i] = j;
                        taskCurrentWinnerInfoArray[i][DISTANCE_TAG] = this.workers[j].effectiveNoiseDistance[i];
                        taskCurrentWinnerInfoArray[i][BUDGET_TAG] = this.workers[j].effectivePrivacyBudget[i];
                    }
                }
                if (!taskCurrentWinnerIDArray[i].equals(taskBeforeWinnerIDArray[i])) {
                    if (!taskBeforeWinnerIDArray[i].equals(-1)) {
                        // todo: 封装
                        this.workers[taskBeforeWinnerIDArray[i]].currentUtilityFunctionValue[i] -= this.tasks[i].valuation;
                    }
                    this.workers[taskCurrentWinnerIDArray[i]].currentUtilityFunctionValue[i] = this.workers[taskCurrentWinnerIDArray[i]].completeUtilityFunctionValue[i];
                }
            }
        }
//        System.out.println("The winner worker's id is " + taskTempWinnerID);
//        System.out.println("The winner worker's noise distance is " + taskTempWinnerInfo[0]);
//        System.out.println("The winner worker's budget is " + taskTempWinnerInfo[1]);
        MyPrint.showIntegerArray(taskCurrentWinnerIDArray);
        MyPrint.showDoubleArray(taskCurrentWinnerInfoArray[DISTANCE_TAG]);
        MyPrint.showDoubleArray(taskCurrentWinnerInfoArray[BUDGET_TAG]);
    }




    private Double getNewCostPrivacyBudget(Integer workerID, Integer taskID) {
        Double result;
        result = this.workers[workerID].privacyBudgetCost[taskID] + this.workers[workerID].privacyBudgetArray[taskID][this.workers[workerID].budgetIndex[taskID]];
        return result;
    }

    private DistanceBudgetPair getNewEffectiveNoiseDistanceAndPrivacyBudget(Integer workerID, Integer taskID, double newNoiseDistance, double newPrivacyBudget) {
        TreeSet<DistanceBudgetPair> tempTreeSet = new TreeSet<>();
        tempTreeSet.addAll(this.workers[workerID].alreadyPublishedNoiseDistanceAndBudgetTreeSetArray[taskID]);
        tempTreeSet.add(new DistanceBudgetPair(newNoiseDistance, newPrivacyBudget));
        double[] distanceBudget = LaplaceUtils.getMaximumLikelihoodEstimationInGivenPoint(tempTreeSet);
        return new DistanceBudgetPair(distanceBudget[0], distanceBudget[1]);
    }


    /**
     *
     * @param taskIDList 候选的要去竞争的所有task(已经排除budget不足的task)
     * @param workerID 要竞争的worker的ID
     * @param totalCompetingTimesList 记录当前总的轮竞争每个task的次数
     * @param lastTermTaskWinnerIDArray 记录上轮竞争中成功竞争每个task的workerID
     * @param lastTermTaskWinnerInfoArray 记录上轮竞争中成功竞争每个task的worker其他信息
     * @param competingWorkerIDSetArray 竞争过某个 task 的worker ID 的集合的列标
     * @return 经过考虑utility函数，PPCF，PCF，task熵之后选择的taskID以及对应要的worker要修改的有效噪声距离，有效隐私预算和task熵值
     */
    protected EntropyInfo chooseByTaskEntropy(List<Integer> taskIDList, Integer workerID, Integer[] totalCompetingTimesList, Integer[] lastTermTaskWinnerIDArray, Double[][] lastTermTaskWinnerInfoArray, HashSet<Integer>[] competingWorkerIDSetArray) {
        // 遍历taskIDList中的所有task，找出能使当前worker的utility增加最大的task，返回（该task的ID, 申请即将泄露的平均distance[此处可返回扰动], 申请泄露的budget总和[此处可返回当前budget]）
        Integer taskID = null;
        Double noiseEfficientDistance = null;
        Double effectivePrivacyBudget = null;
//        Double efficientPrivacyBudget = null;
        double candidateTaskEntropy = Double.MAX_VALUE;
        Double newCostPrivacyBudget = null, newPrivacyBudget = null, newNoiseDistance = null, newUtilityValue = null;
        for (Integer i : taskIDList) {
            if (lastTermTaskWinnerIDArray[i] == workerID) {
                continue;
            }
            // PPCF 判断
            if (this.workers[workerID].toTaskDistance[i] >= lastTermTaskWinnerInfoArray[i][DISTANCE_TAG]) {
                continue;
            }
            Double tempNewCostPrivacyBudget = getNewCostPrivacyBudget(workerID, i);
            Double tempNewPrivacyBudget =  this.workers[workerID].privacyBudgetArray[i][this.workers[workerID].budgetIndex[i]];
            Double tempNewNoiseDistance = this.workers[workerID].toTaskDistance[i] + LaplaceUtils.getLaplaceNoise(1, tempNewPrivacyBudget);
            DistanceBudgetPair newEffectiveDistanceBudgetPair = getNewEffectiveNoiseDistanceAndPrivacyBudget(workerID, i, tempNewNoiseDistance, tempNewPrivacyBudget);
            double tempCompeteDistance = newEffectiveDistanceBudgetPair.distance;
            double tempEffectivePrivacyBudget = newEffectiveDistanceBudgetPair.budget;

            // Utility 函数值判断
            Double tempNewUtilityValue = this.getUtilityValue(this.tasks[i].valuation, tempEffectivePrivacyBudget, this.workers[workerID].toTaskDistance[i], newCostPrivacyBudget);
            if (tempNewUtilityValue <= this.workers[workerID].completeUtilityFunctionValue[i]) {
                continue;
            }

            // PCF 判断
            double pcfValue = LaplaceProbabilityDensityFunction.probabilityDensityFunction(tempCompeteDistance, lastTermTaskWinnerInfoArray[i][DISTANCE_TAG], tempEffectivePrivacyBudget, lastTermTaskWinnerInfoArray[i][BUDGET_TAG]);
            if (pcfValue <= 0.5) {
                continue;
            }

            // 根据 taskEntropy 挑选
            double taskEntropy = getTaskEntropy(i, totalCompetingTimesList[i], competingWorkerIDSetArray[i]);
            if (taskEntropy < candidateTaskEntropy) {
                candidateTaskEntropy = taskEntropy;
                taskID = i;
                noiseEfficientDistance = tempCompeteDistance;
                effectivePrivacyBudget = tempEffectivePrivacyBudget;
                newCostPrivacyBudget = tempNewCostPrivacyBudget;
                newPrivacyBudget = tempNewPrivacyBudget;
                newNoiseDistance = tempNewNoiseDistance;
                newUtilityValue = tempNewUtilityValue;
            }

        }
        if (taskID == null) {
            return null;
        }
        return new EntropyInfo(taskID, noiseEfficientDistance, effectivePrivacyBudget, candidateTaskEntropy, newCostPrivacyBudget, newPrivacyBudget, newNoiseDistance, newUtilityValue);
    }

    protected double getTaskEntropy(Integer taskID, Integer totalCompetingTime, HashSet<Integer> competingWorkerIDSet) {
        if (totalCompetingTime <= 0) {
            throw new RuntimeException("The total competing time is not positive value!");
        }
        double taskEntropy = 0;
        double tempRatio;
        for (Integer j : competingWorkerIDSet) {
            tempRatio = this.workers[j].taskCompletingTimes[taskID] / totalCompetingTime;
            taskEntropy -= tempRatio * Math.log(tempRatio);
        }
        return taskEntropy;
    }

    protected TaskIDDistanceBudgetPairProposingValue chooseByProposingValue(List<Integer> taskIDList, MultiTaskBasicWorker worker, Integer[] totalCompetingTimesList, int[] lastTermTaskWinnerIDArray, double[][] lastTermTaskWinnerInfoArray, HashSet<Integer>[] competingWorkerIDSet) {
        Integer taskID = null;
        Double noiseAverageDistance = null;
        Double totalPrivacyBudget = null;
        double candidateProposingValue = Double.MIN_VALUE;
        for (Integer i : taskIDList) {
            if (worker.toTaskDistance[i] >= lastTermTaskWinnerInfoArray[i][DISTANCE_TAG]) {
                continue;
            }
            double newNoiseDistance = worker.toTaskDistance[i] + LaplaceUtils.getLaplaceNoise(1, worker.privacyBudgetArray[i][worker.budgetIndex[i]]);
            double competeDistance = BasicCalculation.getAverage(worker.effectiveNoiseDistance[i], newNoiseDistance, worker.budgetIndex[i] + 1);
            double completeTotalBudget = worker.effectivePrivacyBudget[i] + worker.privacyBudgetArray[i][worker.budgetIndex[i]];
            double pcfValue = LaplaceProbabilityDensityFunction.probabilityDensityFunction(competeDistance, lastTermTaskWinnerInfoArray[i][DISTANCE_TAG], completeTotalBudget, lastTermTaskWinnerInfoArray[i][BUDGET_TAG]);
            if (pcfValue <= 0.5) {
                continue;
            }
            /**
             * 获取Proposing Value
             */
            double proposingValue = pcfValue / worker.toTaskDistance[i];


            if (proposingValue > candidateProposingValue) {
                candidateProposingValue = proposingValue;
                taskID = i;
                noiseAverageDistance = competeDistance;
                totalPrivacyBudget = completeTotalBudget;
            }

        }
        if (taskID == null) {
            return null;
        }
        return new TaskIDDistanceBudgetPairProposingValue(taskID, noiseAverageDistance, totalPrivacyBudget, candidateProposingValue);
    }


}
