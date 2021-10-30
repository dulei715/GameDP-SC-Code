package edu.ecnu.dll.scheme.solution._3_multiple_task;

import edu.ecnu.dll.basic_struct.comparator.TargetInfoForTaskEntropyComparator;
import edu.ecnu.dll.basic_struct.pack.DistanceBudgetPair;
import edu.ecnu.dll.basic_struct.pack.TargetInfo;
import edu.ecnu.dll.basic_struct.pack.Winner;
import edu.ecnu.dll.basic_struct.pack.WorkerIDDistanceBudgetPair;
import edu.ecnu.dll.scheme.solution._3_multiple_task.struct.EnhanceConflictElimination;
import edu.ecnu.dll.scheme_compared.solution.ConflictElimination;
import tools.differential_privacy.compare.impl.LaplaceProbabilityDensityFunction;
import tools.differential_privacy.noise.LaplaceUtils;

import java.util.*;

public class MultiTaskMultiCompetitionSolution extends MultiTaskSingleCompetitionSolution {


    public static final int proposalSize = 5;
    public static TargetInfoForTaskEntropyComparator targetInfoForTaskEntropyComparator = new TargetInfoForTaskEntropyComparator(TargetInfoForTaskEntropyComparator.ASCENDING);
    public static TargetInfoForTaskEntropyComparator targetInfoForProposingValueComparator = new TargetInfoForTaskEntropyComparator(TargetInfoForTaskEntropyComparator.DESCENDING);

    public ConflictElimination conflictElimination = null;

    public List<WorkerIDDistanceBudgetPair>[] createTableDataOfPreferenceTableByID(List<Integer>[] workerIDList) {
        List<WorkerIDDistanceBudgetPair>[] table = new ArrayList[workerIDList.length];
        WorkerIDDistanceBudgetPair tempPair;
        Integer tempWorkerID;
        for (int i = 0; i < workerIDList.length; i++) {
            table[i] = new ArrayList<>();
            for (Integer workerID : workerIDList[i]) {
                tempPair = new WorkerIDDistanceBudgetPair(workerID, this.workers[workerID].effectiveNoiseDistance[i], this.workers[workerID].effectivePrivacyBudget[i]);
                table[i].add(tempPair);
            }
        }
        return table;
    }

    @Override
    public void initializeAgents() {
        super.initializeAgents();
        for (int i = 0; i < this.workers.length; i++) {
            this.workers[i].currentWinningState = false;
        }
    }

//    protected void initializeAllocationByFirstTaskAndNullAllocation(List<Integer>[] newCandidateWorkerIDList, WorkerIDDistanceBudgetPair[] taskCurrentWinnerPackedArray, Integer[] competingTimes, HashSet<Integer>[] completedWorkerIDSet) {
//        // 针对每个task，初始化距离为最大距离值
//        // 针对每个task，初始化对应距离的隐私预算为最大隐私预算
//        // 针对每个task，初始化总的被竞争次数为0
//        // 针对每个task，初始化访问过被访问worker集合为空集合
//        for (int i = 0; i < this.tasks.length; i++) {
//            newCandidateWorkerIDList[i] = new ArrayList<>();
//            taskCurrentWinnerPackedArray[i].workerID = -1;
//            taskCurrentWinnerPackedArray[i].noiseEffectiveDistance = Double.MAX_VALUE;
//            taskCurrentWinnerPackedArray[i].effectivePrivacyBudget = Double.MAX_VALUE;
//            competingTimes[i] = 0;
//            completedWorkerIDSet[i] = new HashSet<>();
//        }
//        for (int i = 0; i < this.workers.length; i++) {
//            newCandidateWorkerIDList[0].add(i);
//        }
//    }

    @Override
    public WorkerIDDistanceBudgetPair[] complete() {

        conflictElimination = new EnhanceConflictElimination(this.tasks.length, this.workers.length);
        // 记录每个task的被竞争的总次数
        Integer[] competingTimes = new Integer[this.tasks.length];
        // 记录每个task被竞争过的worker id的集合
        HashSet<Integer>[] completedWorkerIDSet = new HashSet[this.tasks.length];

        // 记录worker竞争时临时效用函数值
        double[] tempUtilityArray = new double[this.workers.length];

        // 记录每个worker的申请到的task列表
        // List<Integer>[] allocatedTaskIDListArray = new ArrayList[this.workers.length];


        // 针对每个task，记录当前竞争成功的worker的ID
//        Integer[] taskCurrentWinnerIDArray = new Integer[this.tasks.length];
//        BasicArray.setIntArrayTo(taskCurrentWinnerIDArray, -1);
        // 针对每个task，记录当前竞争成功的worker的budget以及扰动距离
//        Double[][] taskCurrentWinnerInfoArray = new Double[this.tasks.length][2];
        WorkerIDDistanceBudgetPair[] taskCurrentWinnerPackedArray = new WorkerIDDistanceBudgetPair[this.tasks.length];

        // todo: 封装到了initializeAllocations

        // 针对每个task，本轮提出竞争的worker的ID（每轮需要清空）
        List<Integer>[] newCandidateWorkerIDList, oldCandidateWorkerIDList;
        newCandidateWorkerIDList = new ArrayList[this.tasks.length];
//        BasicArray.setListArrayToEmptyList(newCandidateWorkerIDList);
        initializeAllocationByFirstTaskAndNullAllocation(newCandidateWorkerIDList, taskCurrentWinnerPackedArray, competingTimes, completedWorkerIDSet);


        double competeTemp;
        Iterator<Integer> workerIDIterator;
        Integer tempWorkerID;
        // 用于记录当前竞争的总的worker数量
//        int totalCompetingWorkerNumber = this.workers.length;
        Set<Integer> newTotalCompetingWorkerIDSet = new HashSet<>(), oldTotalCompetingWorkerIDSet;
        addAllWorkerIDToSet(newTotalCompetingWorkerIDSet);

        // 用来临时记录每个worker经过对budget使用情况考察后，能够去进行竞争的tasks
        List<Integer> tempCandidateTaskList = new ArrayList<>();

        while (!newTotalCompetingWorkerIDSet.isEmpty()) {
            oldTotalCompetingWorkerIDSet = newTotalCompetingWorkerIDSet;
            newTotalCompetingWorkerIDSet = new HashSet<>();

            /*
             * 遍历每个竞争集合 oldTotalCompetingWorkerIDSet 对应的候选集合中的worker。
             * 每个 worker 对所有的 tasks 进行竞争。但只能挑出其中 1 个 task 作为最终竞争对象。
             * 每轮结束后统计剩余的总的将要竞争的 workers 的数量。
             */
            workerIDIterator = oldTotalCompetingWorkerIDSet.iterator();
            while (workerIDIterator.hasNext()) {
                tempWorkerID = workerIDIterator.next();

                //进行是否竞争判断1：如果当前 worker 不需要竞争(是某个task的胜利者)，就不作为
                if (this.workers[tempWorkerID].currentWinningState) {
                    continue;
                }

                // 进行是否竞争判断2：计算出所有预算充足的 task ID 的集合。如果该集合为空，则不作为
                tempCandidateTaskList.clear();
                setCandidateTaskByBudget(tempCandidateTaskList, this.workers[tempWorkerID]);
                if (tempCandidateTaskList.isEmpty()) {
                    continue;
                }

                // 进行是否竞争判断3： 考察 utility函数、PPCF函数、PCF函数、任务熵

                TargetInfo[] winnerInfoArray;
                winnerInfoArray = chooseArrayByTaskEntropy(tempCandidateTaskList, tempWorkerID, competingTimes, taskCurrentWinnerPackedArray, completedWorkerIDSet, proposalSize);
//                winnerInfoArray = chooseArrayByProposingValue(tempCandidateTaskList, tempWorkerID, taskCurrentWinnerPackedArray, proposalSize);
                if (winnerInfoArray == null) {
                    continue;
                }

                // 否则（竞争成功），发布当前扰动距离长度和隐私预算(这里只添加进候选列表供server进一步选择)，并将隐私自己的预算索引值加1

                Integer tempTaskID;
                for (int i = 0; i < winnerInfoArray.length; i++) {
                    tempTaskID = winnerInfoArray[i].taskID;
                    newCandidateWorkerIDList[tempTaskID].add(tempWorkerID);
                    completedWorkerIDSet[tempTaskID].add(tempWorkerID);
                    this.workers[tempWorkerID].alreadyPublishedNoiseDistanceAndBudgetTreeSetArray[tempTaskID].add(new DistanceBudgetPair(winnerInfoArray[i].newNoiseDistance, winnerInfoArray[i].newPrivacyBudget));
                    this.workers[tempWorkerID].effectiveNoiseDistance[tempTaskID] = winnerInfoArray[i].noiseEffectiveDistance;
                    this.workers[tempWorkerID].effectivePrivacyBudget[tempTaskID] = winnerInfoArray[i].effectivePrivacyBudget;
                    this.workers[tempWorkerID].completeUtilityFunctionValue[tempTaskID] = winnerInfoArray[i].newUtilityValue;
                    this.workers[tempWorkerID].budgetIndex[tempTaskID] ++;
                    this.workers[tempWorkerID].taskCompetingTimes[tempTaskID] ++;
                    competingTimes[tempTaskID] ++;

                }

            }
            taskCurrentWinnerPackedArray = chosenByServerAndReturnWinnerPackedArray(taskCurrentWinnerPackedArray, newCandidateWorkerIDList);
        }
        return taskCurrentWinnerPackedArray;
    }

    private TargetInfo[] chooseArrayByProposingValue(List<Integer> taskIDList, Integer workerID, WorkerIDDistanceBudgetPair[] lastTermTaskWinnerPackedArray, int topK) {
        Integer taskID = null;
        Double noiseEfficientDistance = null;
        Double effectivePrivacyBudget = null;
        TreeSet<TargetInfo> candidateTargetInfoSet = new TreeSet<>(targetInfoForProposingValueComparator);
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
            double tempProposingValue = pcfValue / this.workers[workerID].toTaskDistance[i];
            TargetInfo targetInfo = null;

            if (candidateTargetInfoSet.size() < topK) {
                candidateTargetInfoSet.add(new TargetInfo(i, tempCompeteDistance, tempEffectivePrivacyBudget, tempProposingValue, tempNewCostPrivacyBudget, tempNewPrivacyBudget, tempNewNoiseDistance, tempNewUtilityValue));
            } else {
                targetInfo = candidateTargetInfoSet.last();
                if (tempProposingValue > targetInfo.target) {
                    candidateTargetInfoSet.remove(targetInfo); //todo: 测试是否能够真的删除
                    candidateTargetInfoSet.add(new TargetInfo(i, tempCompeteDistance, tempEffectivePrivacyBudget, tempProposingValue, tempNewCostPrivacyBudget, tempNewPrivacyBudget, tempNewNoiseDistance, tempNewUtilityValue));
                }
            }

        }
        if (taskID == null) {
            return null;
        }
        return candidateTargetInfoSet.toArray(new TargetInfo[0]);
    }

    protected TargetInfo[] chooseArrayByTaskEntropy(List<Integer> taskIDList, Integer workerID, Integer[] totalCompetingTimesList, WorkerIDDistanceBudgetPair[] lastTermTaskWinnerPackedArray, HashSet<Integer>[] competingWorkerIDSetArray, int topK) {
        // 遍历taskIDList中的所有task，找出能使当前worker的utility增加最大的task，返回（该task的ID, 申请即将泄露的平均distance[此处可返回扰动], 申请泄露的budget总和[此处可返回当前budget]）
        Integer taskID = null;
        Double noiseEfficientDistance = null;
        Double effectivePrivacyBudget = null;
//        Double efficientPrivacyBudget = null;
        TreeSet<TargetInfo> candidateTargetInfoSet = new TreeSet<>(targetInfoForTaskEntropyComparator);

        Double newCostPrivacyBudget = null, newPrivacyBudget = null, newNoiseDistance = null, newUtilityValue = null;
        for (Integer i : taskIDList) {
            if (lastTermTaskWinnerPackedArray[i].equals(workerID)) {
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
            double tempTaskEntropy = getTaskEntropy(i, totalCompetingTimesList[i], competingWorkerIDSetArray[i]);
            TargetInfo targetInfo = null;

            if (candidateTargetInfoSet.size() < topK) {
                candidateTargetInfoSet.add(new TargetInfo(i, tempCompeteDistance, tempEffectivePrivacyBudget, tempTaskEntropy, tempNewCostPrivacyBudget, tempNewPrivacyBudget, tempNewNoiseDistance, tempNewUtilityValue));
            } else {
                targetInfo = candidateTargetInfoSet.last();
                if (tempTaskEntropy < targetInfo.target) {
                    candidateTargetInfoSet.remove(targetInfo); //todo: 测试是否能够真的删除
                    candidateTargetInfoSet.add(new TargetInfo(i, tempCompeteDistance, tempEffectivePrivacyBudget, tempTaskEntropy, tempNewCostPrivacyBudget, tempNewPrivacyBudget, tempNewNoiseDistance, tempNewUtilityValue));
                }
            }

        }
        if (candidateTargetInfoSet.isEmpty()) {
            return null;
        }
        return candidateTargetInfoSet.toArray(new TargetInfo[0]);
    }



    private void addAllWorkerIDToSet(Set<Integer> set) {
        for (int i = 0; i < this.workers.length; i++) {
            set.add(i);
        }
    }

    /**
     *
     * @param taskCurrentWinnerPackedArray 记录当前每个task被竞争获胜的worker的信息
     * @param newCandidateWorkerIDList 记录每个task候选的worker id列表
     */
    public WorkerIDDistanceBudgetPair[] chosenByServerAndReturnWinnerPackedArray(WorkerIDDistanceBudgetPair[] taskCurrentWinnerPackedArray, List<Integer>[] newCandidateWorkerIDList) {
        Integer currentWinnerWorkerID, beforeWinnerWorkerID;
        WorkerIDDistanceBudgetPair[] taskBeforeWinnerPackedArray = taskCurrentWinnerPackedArray;
        // 1. 获取获胜者，并更新胜利列表
        List<WorkerIDDistanceBudgetPair>[] unSortedTable = createTableDataOfPreferenceTableByID(newCandidateWorkerIDList);
        taskCurrentWinnerPackedArray = this.conflictElimination.assignment(unSortedTable);

        for (int i = 0; i < taskCurrentWinnerPackedArray.length; i++) {
            if (taskCurrentWinnerPackedArray[i] == null ||taskCurrentWinnerPackedArray[i].workerID.equals(taskBeforeWinnerPackedArray[i].workerID)) {
                continue;
            }
            currentWinnerWorkerID = taskCurrentWinnerPackedArray[i].workerID;
            beforeWinnerWorkerID = taskBeforeWinnerPackedArray[i].workerID;
            // 2. 设置获胜的worker的[有效噪声距离、有效隐私预算](已经包含在packedArray中，无需设置)、当前效用函数值、胜利状态
            this.workers[currentWinnerWorkerID].currentUtilityFunctionValue[i] = this.workers[currentWinnerWorkerID].completeUtilityFunctionValue[i];
            this.workers[currentWinnerWorkerID].currentWinningState = true; // 设置成胜利者，之后在没被竞争掉的情况下不会再竞争其他task
            // 3. 设置被竞争下去的worker的当前效用函数值、胜利状态，并将其加入竞争列表
            if (!beforeWinnerWorkerID.equals(-1)) {
                this.workers[beforeWinnerWorkerID].currentUtilityFunctionValue[i] -= this.tasks[i].valuation;
                this.workers[beforeWinnerWorkerID].currentWinningState = false;
                // 将被竞争下去的worker加入竞争集合，以便下轮判断是否可以竞争
                newCandidateWorkerIDList[i].add(beforeWinnerWorkerID);
            }

        }

        return taskCurrentWinnerPackedArray;

    }




}
