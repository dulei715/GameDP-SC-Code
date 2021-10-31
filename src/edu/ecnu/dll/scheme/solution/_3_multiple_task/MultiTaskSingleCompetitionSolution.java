package edu.ecnu.dll.scheme.solution._3_multiple_task;

import edu.ecnu.dll.basic_struct.pack.DistanceBudgetPair;
import edu.ecnu.dll.basic_struct.pack.TargetInfo;
import edu.ecnu.dll.basic_struct.pack.Winner;
import edu.ecnu.dll.basic_struct.pack.WorkerIDDistanceBudgetPair;
import edu.ecnu.dll.scheme.struct.task.BasicTask;
import edu.ecnu.dll.basic_struct.agent.Task;
import edu.ecnu.dll.scheme.struct.worker.MultiTaskBasicWorker;
import tools.basic.BasicArray;
import tools.basic.BasicCalculation;
import tools.differential_privacy.compare.impl.LaplaceProbabilityDensityFunction;
import tools.differential_privacy.noise.LaplaceUtils;
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

    protected double getUtilityValue(double taskValue, double effectivePrivacyBudget, double realDistance, double privacyBudgetCost) {
        return taskValue + taskValue * effectivePrivacyBudget - alpha * realDistance - beta * privacyBudgetCost;
    }

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
            this.workers[j].taskCompetingTimes = new Integer[this.tasks.length];
            this.workers[j].currentUtilityFunctionValue = new Double[this.tasks.length];
            for (int i = 0; i < tasks.length; i++) {
                this.workers[j].toTaskDistance[i] = BasicCalculation.get2Norm(this.tasks[i].location, this.workers[j].location);
                this.workers[j].budgetIndex[i] = 0;
                this.workers[j].alreadyPublishedNoiseDistanceAndBudgetTreeSetArray[i] = new TreeSet<>();
                this.workers[j].completeUtilityFunctionValue = BasicArray.getInitializedArray(0.0, this.tasks.length);
                this.workers[j].effectiveNoiseDistance[i] = 0.0;
                this.workers[j].effectivePrivacyBudget[i] = 0.0;
                this.workers[j].privacyBudgetCost[i] = 0.0;
                this.workers[j].taskCompetingTimes[i] = 0;
                this.workers[j].currentUtilityFunctionValue[i] = 0.0;
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

    protected void initializeAllocationByFirstTaskAndNullAllocation(List<Integer>[] newCandidateWorkerIDList, WorkerIDDistanceBudgetPair[] taskCurrentWinnerPackedArray, Integer[] competingTimes, HashSet<Integer>[] completedWorkerIDSet) {
        // 针对每个task，初始化距离为最大距离值
        // 针对每个task，初始化对应距离的隐私预算为最大隐私预算
        // 针对每个task，初始化总的被竞争次数为0
        // 针对每个task，初始化访问过被访问worker集合为空集合
        for (int i = 0; i < this.tasks.length; i++) {
            newCandidateWorkerIDList[i] = new ArrayList<>();
            taskCurrentWinnerPackedArray[i] = new WorkerIDDistanceBudgetPair();
            taskCurrentWinnerPackedArray[i].workerID = -1;
            taskCurrentWinnerPackedArray[i].noiseEffectiveDistance = Double.MAX_VALUE;
            taskCurrentWinnerPackedArray[i].effectivePrivacyBudget = Double.MAX_VALUE;
            competingTimes[i] = 0;
            completedWorkerIDSet[i] = new HashSet<>();
        }
        for (int i = 0; i < this.workers.length; i++) {
            newCandidateWorkerIDList[0].add(i);
        }
    }
//    private void initializeAllocationByFirstTaskAndNullAllocation(List<Integer>[] newCandidateWorkerIDList, Integer[] taskCurrentWinnerIDArray, Double[][] taskCurrentWinnerInfoArray, Integer[] competingTimes, HashSet<Integer>[] completedWorkerIDSet) {
//
//    }

    public WorkerIDDistanceBudgetPair[] complete() {

        // 记录每个task的被竞争的总次数
        Integer[] competingTimes = new Integer[this.tasks.length];
        // 记录每个task被竞争过的worker id的集合
        HashSet<Integer>[] completedWorkerIDSet = new HashSet[this.tasks.length];

        // 记录worker竞争时临时效用函数值
        double[] tempUtilityArray = new double[this.workers.length];


        // 针对每个task，记录当前竞争成功的worker的ID和扰动距离以及隐私预算
        WorkerIDDistanceBudgetPair[] taskCurrentWinnerPackedArray = new WorkerIDDistanceBudgetPair[this.tasks.length];

        // todo: 封装到了initializeAllocations

        // 针对每个task，本轮提出竞争的worker的ID（每轮需要清空）
        List<Integer>[] newCandidateWorkerIDList, oldCandidateWorkerIDList;
        newCandidateWorkerIDList = new ArrayList[this.tasks.length];
//        BasicArray.setListArrayToEmptyList(newCandidateWorkerIDList);
        initializeAllocationByFirstTaskAndNullAllocation(newCandidateWorkerIDList, taskCurrentWinnerPackedArray, competingTimes, completedWorkerIDSet);


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
                    if (j.equals(taskCurrentWinnerPackedArray[i].workerID)) {
                        continue;
                    }

                    // 进行是否竞争判断2：计算出所有预算充足的 task ID 的集合。如果该集合为空，则不作为
                    tempCandidateTaskList.clear();
                    setCandidateTaskByBudget(tempCandidateTaskList, this.workers[j]);
                    if (tempCandidateTaskList.isEmpty()) {
                        continue;
                    }


                    // 进行是否竞争判断3： 考察 utility函数、PPCF函数、PCF函数、任务熵

                    TargetInfo winnerInfo;
                    // todo: 暂时用一个函数测试task entropy 和 proposing value。 由于task entropy需要统计竞争次数，因此影响时间，需要和proposing value 分开对比
                    winnerInfo = chooseByTaskEntropy(tempCandidateTaskList, j, competingTimes, taskCurrentWinnerPackedArray, completedWorkerIDSet);
//                    winnerInfo = chooseByProposingValue(tempCandidateTaskList, j, taskCurrentWinnerPackedArray);
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

                    this.workers[j].taskCompetingTimes[winnerInfo.taskID] ++;
                    totalCompleteWorkerNumber ++;

                    competingTimes[winnerInfo.taskID] ++;

                }
            }


            chosenByServer(taskCurrentWinnerPackedArray, newCandidateWorkerIDList);
        }
        return taskCurrentWinnerPackedArray;
    }

    protected void chosenByServer(WorkerIDDistanceBudgetPair[] taskCurrentWinnerPackedArray, List<Integer>[] newCandidateWorkerIDList) {
        double competeTemp;
        // todo: 设置 taskTempWinnerID 和 taskTempWinnerInfo
//        Integer[] taskBeforeWinnerIDArray = new Integer[newCandidateWorkerIDList.length];
        WorkerIDDistanceBudgetPair[] taskBeforeWinnerPackedArray = new WorkerIDDistanceBudgetPair[newCandidateWorkerIDList.length];
        for (int i = 0; i < newCandidateWorkerIDList.length; i++) {
            if (newCandidateWorkerIDList[i].size() == 0) {
                continue;
            }
            taskBeforeWinnerPackedArray[i] = taskCurrentWinnerPackedArray[i];
            for (Integer j : newCandidateWorkerIDList[i]) {
                competeTemp = LaplaceProbabilityDensityFunction.probabilityDensityFunction(this.workers[j].effectiveNoiseDistance[i], taskCurrentWinnerPackedArray[i].noiseEffectiveDistance, this.workers[j].effectivePrivacyBudget[i], taskCurrentWinnerPackedArray[i].effectivePrivacyBudget);
                if (competeTemp > 0.5) {
                    taskCurrentWinnerPackedArray[i].workerID = j;
                    taskCurrentWinnerPackedArray[i].noiseEffectiveDistance = this.workers[j].effectiveNoiseDistance[i];
                    taskCurrentWinnerPackedArray[i].effectivePrivacyBudget = this.workers[j].effectivePrivacyBudget[i];
                }
            }
            if (!taskCurrentWinnerPackedArray[i].workerID.equals(taskBeforeWinnerPackedArray[i].workerID)) {
                if (!taskBeforeWinnerPackedArray[i].workerID.equals(-1)) {
                    // todo: 封装
                    this.workers[taskBeforeWinnerPackedArray[i].workerID].currentUtilityFunctionValue[i] -= this.tasks[i].valuation;
                    // 将被竞争下去的worker加入竞争集合，以便下轮判断是否可以竞争
                    newCandidateWorkerIDList[i].add(taskBeforeWinnerPackedArray[i].workerID);
                }
                this.workers[taskCurrentWinnerPackedArray[i].workerID].currentUtilityFunctionValue[i] = this.workers[taskCurrentWinnerPackedArray[i].workerID].completeUtilityFunctionValue[i];
            }
        }
    }


    protected Double getNewCostPrivacyBudget(Integer workerID, Integer taskID) {
        Double result;
        result = this.workers[workerID].privacyBudgetCost[taskID] + this.workers[workerID].privacyBudgetArray[taskID][this.workers[workerID].budgetIndex[taskID]];
        return result;
    }

    protected DistanceBudgetPair getNewEffectiveNoiseDistanceAndPrivacyBudget(Integer workerID, Integer taskID, double newNoiseDistance, double newPrivacyBudget) {
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
     * @param lastTermTaskWinnerPackedArray 记录上轮竞争中成功竞争每个task的worker其他信息
     * @param competingWorkerIDSetArray 竞争过某个 task 的worker ID 的集合的列标
     * @return 经过考虑utility函数，PPCF，PCF，task熵之后选择的taskID以及对应要的worker要修改的有效噪声距离，有效隐私预算和task熵值
     */
    protected TargetInfo chooseByTaskEntropy(List<Integer> taskIDList, Integer workerID, Integer[] totalCompetingTimesList, WorkerIDDistanceBudgetPair[] lastTermTaskWinnerPackedArray, HashSet<Integer>[] competingWorkerIDSetArray) {
        // 遍历taskIDList中的所有task，找出能使当前worker的utility增加最大的task，返回（该task的ID, 申请即将泄露的平均distance[此处可返回扰动], 申请泄露的budget总和[此处可返回当前budget]）
        Integer taskID = null;
        Double noiseEfficientDistance = null;
        Double effectivePrivacyBudget = null;
//        Double efficientPrivacyBudget = null;
//        double candidateTaskEntropy = Double.MAX_VALUE;
        double candidateTaskEntropy = -1;
        Double newCostPrivacyBudget = null, newPrivacyBudget = null, newNoiseDistance = null, newUtilityValue = null;
        for (Integer i : taskIDList) {
            if (lastTermTaskWinnerPackedArray[i].workerID.equals(workerID)) {
                continue;
            }
            // PPCF 判断
            if (this.workers[workerID].toTaskDistance[i] >= lastTermTaskWinnerPackedArray[i].noiseEffectiveDistance) {
                continue;
            }
            Double tempNewCostPrivacyBudget = getNewCostPrivacyBudget(workerID, i);
            Double tempNewPrivacyBudget =  this.workers[workerID].privacyBudgetArray[i][this.workers[workerID].budgetIndex[i]];
            Double tempNewNoiseDistance = this.workers[workerID].toTaskDistance[i] + LaplaceUtils.getLaplaceNoise(1, tempNewPrivacyBudget);
            DistanceBudgetPair newEffectiveDistanceBudgetPair = getNewEffectiveNoiseDistanceAndPrivacyBudget(workerID, i, tempNewNoiseDistance, tempNewPrivacyBudget);
            double tempCompeteDistance = newEffectiveDistanceBudgetPair.distance;
            double tempEffectivePrivacyBudget = newEffectiveDistanceBudgetPair.budget;

            // Utility 函数值判断
            Double tempNewUtilityValue = this.getUtilityValue(this.tasks[i].valuation, tempEffectivePrivacyBudget, this.workers[workerID].toTaskDistance[i], tempNewCostPrivacyBudget);
            if (tempNewUtilityValue <= this.workers[workerID].completeUtilityFunctionValue[i]) {
                continue;
            }

            // PCF 判断
            double pcfValue = LaplaceProbabilityDensityFunction.probabilityDensityFunction(tempCompeteDistance, lastTermTaskWinnerPackedArray[i].noiseEffectiveDistance, tempEffectivePrivacyBudget, lastTermTaskWinnerPackedArray[i].effectivePrivacyBudget);
            if (pcfValue <= 0.5) {
                continue;
            }

            // 根据 taskEntropy 挑选
            double taskEntropy = getTaskEntropy(i, totalCompetingTimesList[i], competingWorkerIDSetArray[i]);
            if (taskEntropy > candidateTaskEntropy) {
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
        return new TargetInfo(taskID, noiseEfficientDistance, effectivePrivacyBudget, candidateTaskEntropy, newCostPrivacyBudget, newPrivacyBudget, newNoiseDistance, newUtilityValue);
    }

    protected double getTaskEntropy(Integer taskID, Integer totalCompetingTime, HashSet<Integer> competingWorkerIDSet) {
        if (totalCompetingTime <= 0) {
//            throw new RuntimeException("The total competing time is not positive value!");
            return 0;
        }
        double taskEntropy = 0;
        double tempRatio;
        for (Integer j : competingWorkerIDSet) {
            tempRatio = this.workers[j].taskCompetingTimes[taskID] / totalCompetingTime;
            taskEntropy -= tempRatio * Math.log(tempRatio);
        }
        return taskEntropy;
    }

    protected TargetInfo chooseByProposingValue(List<Integer> taskIDList, Integer workerID, WorkerIDDistanceBudgetPair[] lastTermTaskWinnerPackedArray) {
        Integer taskID = null;
        Double noiseEfficientDistance = null;
        Double effectivePrivacyBudget = null;
        double candidateProposingValue = Double.MIN_VALUE;
        Double newCostPrivacyBudget = null, newPrivacyBudget = null, newNoiseDistance = null, newUtilityValue = null;
        for (Integer i : taskIDList) {
            if (lastTermTaskWinnerPackedArray[i].workerID.equals(workerID)) {
                continue;
            }
            // PPCF 判断
            if (this.workers[workerID].toTaskDistance[i] >= lastTermTaskWinnerPackedArray[i].noiseEffectiveDistance) {
                continue;
            }

            Double tempNewCostPrivacyBudget = getNewCostPrivacyBudget(workerID, i);
            Double tempNewPrivacyBudget =  this.workers[workerID].privacyBudgetArray[i][this.workers[workerID].budgetIndex[i]];
            Double tempNewNoiseDistance = this.workers[workerID].toTaskDistance[i] + LaplaceUtils.getLaplaceNoise(1, tempNewPrivacyBudget);
            DistanceBudgetPair newEffectiveDistanceBudgetPair = getNewEffectiveNoiseDistanceAndPrivacyBudget(workerID, i, tempNewNoiseDistance, tempNewPrivacyBudget);
            double tempCompeteDistance = newEffectiveDistanceBudgetPair.distance;
            double tempEffectivePrivacyBudget = newEffectiveDistanceBudgetPair.budget;

            // Utility 函数判断
            Double tempNewUtilityValue = this.getUtilityValue(this.tasks[i].valuation, tempEffectivePrivacyBudget, this.workers[workerID].toTaskDistance[i], tempNewCostPrivacyBudget);
            if (tempNewUtilityValue <= this.workers[workerID].completeUtilityFunctionValue[i]) {
                continue;
            }

            // PCF 判断
            double pcfValue = LaplaceProbabilityDensityFunction.probabilityDensityFunction(tempCompeteDistance, lastTermTaskWinnerPackedArray[i].noiseEffectiveDistance, tempEffectivePrivacyBudget, lastTermTaskWinnerPackedArray[i].effectivePrivacyBudget);
            if (pcfValue <= 0.5) {
                continue;
            }

            /**
             * 获取Proposing Value
             */
            double proposingValue = pcfValue / this.workers[workerID].toTaskDistance[i];


            if (proposingValue > candidateProposingValue) {
                candidateProposingValue = proposingValue;
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
        return new TargetInfo(taskID, noiseEfficientDistance, effectivePrivacyBudget, candidateProposingValue, newCostPrivacyBudget, newPrivacyBudget, newNoiseDistance, newUtilityValue);
    }




}
