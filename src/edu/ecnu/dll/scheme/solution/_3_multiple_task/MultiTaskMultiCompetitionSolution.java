package edu.ecnu.dll.scheme.solution._3_multiple_task;

import edu.ecnu.dll.basic_struct.comparator.TargetInfoForTaskEntropyComparator;
import edu.ecnu.dll.basic_struct.pack.TaskWorkerIDPair;
import edu.ecnu.dll.basic_struct.pack.single_agent_info.sub_class.DistanceBudgetPair;
import edu.ecnu.dll.basic_struct.pack.single_agent_info.sub_class.sub_class.TaskTargetInfo;
import edu.ecnu.dll.basic_struct.pack.single_agent_info.sub_class.WorkerIDDistanceBudgetPair;
import edu.ecnu.dll.scheme.solution._3_multiple_task.struct.EnhanceConflictElimination;
import edu.ecnu.dll.scheme_compared.solution.ConflictElimination;
import tools.basic.BasicArray;
import tools.differential_privacy.compare.impl.LaplaceProbabilityDensityFunction;
import tools.differential_privacy.noise.LaplaceUtils;
import tools.struct.PreferenceTable;

import java.util.*;

public class MultiTaskMultiCompetitionSolution extends MultiTaskSingleCompetitionSolution {


    public static final int proposalSize = 50;
    public static TargetInfoForTaskEntropyComparator targetInfoForTaskEntropyComparator = new TargetInfoForTaskEntropyComparator(TargetInfoForTaskEntropyComparator.DESCENDING);
    public static TargetInfoForTaskEntropyComparator targetInfoForProposingValueComparator = new TargetInfoForTaskEntropyComparator(TargetInfoForTaskEntropyComparator.DESCENDING);
    public static TargetInfoForTaskEntropyComparator targetInfoForUtilityValueComparator = new TargetInfoForTaskEntropyComparator(TargetInfoForTaskEntropyComparator.DESCENDING);
    public static TargetInfoForTaskEntropyComparator targetInfoForUtilityAndCompositionValueComparator = new TargetInfoForTaskEntropyComparator(TargetInfoForTaskEntropyComparator.DESCENDING);

    public ConflictElimination conflictElimination = null;

    public List<WorkerIDDistanceBudgetPair>[] createTableDataOfPreferenceTableByID(List<Integer>[] workerIDList) {
        List<WorkerIDDistanceBudgetPair>[] table = new ArrayList[workerIDList.length];
        WorkerIDDistanceBudgetPair tempPair;
        Integer tempWorkerID;
        for (int i = 0; i < workerIDList.length; i++) {
            table[i] = new ArrayList<>();
            for (Integer workerID : workerIDList[i]) {
                if (workerID.equals(ConflictElimination.DEFAULT_WORKER_ID_DISTANCE_BUDGET_PAIR.getWorkerID()) && workerIDList[i].size() > 1) {
                    continue;
                }
                tempPair = new WorkerIDDistanceBudgetPair(workerID, this.workers[workerID].effectiveNoiseDistance[i], this.workers[workerID].effectivePrivacyBudget[i]);
                table[i].add(tempPair);
            }
        }

        return table;
    }

    public List<WorkerIDDistanceBudgetPair>[] createTableDataOfPreferenceTableByID(WorkerIDDistanceBudgetPair[] originalWinnerInfo, List<Integer>[] workerIDList) {
        List<WorkerIDDistanceBudgetPair>[] table = new ArrayList[workerIDList.length];
        WorkerIDDistanceBudgetPair tempPair;
        Integer tempWorkerID;
        for (int i = 0; i < workerIDList.length; i++) {
            table[i] = new ArrayList<>();
            for (Integer workerID : workerIDList[i]) {
                if (workerID.equals(ConflictElimination.DEFAULT_WORKER_ID_DISTANCE_BUDGET_PAIR.getWorkerID()) && workerIDList[i].size() > 1) {
                    continue;
                }
                tempPair = new WorkerIDDistanceBudgetPair(workerID, this.workers[workerID].effectiveNoiseDistance[i], this.workers[workerID].effectivePrivacyBudget[i]);
                table[i].add(tempPair);
            }
            if (!originalWinnerInfo[i].getWorkerID().equals(ConflictElimination.DEFAULT_WORKER_ID_DISTANCE_BUDGET_PAIR.getWorkerID()) || workerIDList[i].isEmpty()) {
                table[i].add(originalWinnerInfo[i]);
            }
            PreferenceTable.sortedPreferenceTable(table[i], this.conflictElimination.workerIDDistanceBudgetPairComparator);
        }
        return table;
    }


    /**
     * 初始化函数
     */
    @Override
    public void initializeAgents() {
        super.initializeAgents();
        for (int i = 0; i < this.workers.length; i++) {
            this.workers[i].currentWinningState = false;
        }
    }
    protected void initializeAllocationByFirstTaskAndNullAllocation(WorkerIDDistanceBudgetPair[] taskCurrentWinnerPackedArray, Integer[] competingTimes, HashSet<Integer>[] completedWorkerIDSet) {
        // 针对每个task，初始化距离为最大距离值
        // 针对每个task，初始化对应距离的隐私预算为最大隐私预算
        // 针对每个task，初始化总的被竞争次数为0
        // 针对每个task，初始化访问过被访问worker集合为空集合
        for (int i = 0; i < this.tasks.length; i++) {
            taskCurrentWinnerPackedArray[i] = ConflictElimination.DEFAULT_WORKER_ID_DISTANCE_BUDGET_PAIR;
            competingTimes[i] = 0;
            completedWorkerIDSet[i] = new HashSet<>();
        }
    }
    private void addAllWorkerIDToSet(Set<Integer> set) {
        for (int i = 0; i < this.workers.length; i++) {
            set.add(i);
        }
    }

    /**
     * 2种worker的选择函数
     */
    protected TaskTargetInfo[] chooseArrayByUtilityFunction(List<Integer> taskIDList, Integer workerID, WorkerIDDistanceBudgetPair[] lastTermTaskWinnerPackedArray, int topK){
        TreeSet<TaskTargetInfo> candidateTaskTargetInfoSet = new TreeSet<>(targetInfoForUtilityValueComparator);
        for (Integer i : taskIDList) {
            if (lastTermTaskWinnerPackedArray[i].getWorkerID().equals(workerID)) {
                continue;
            }
            // PPCF 判断
            if (this.workers[workerID].toTaskDistance[i] >= lastTermTaskWinnerPackedArray[i].getNoiseEffectiveDistance()) {
                continue;
            }

            Double tempNewCostPrivacyBudget = getNewCostPrivacyBudget(workerID, i);
            Double tempNewPrivacyBudget =  this.workers[workerID].privacyBudgetArray[i][this.workers[workerID].budgetIndex[i]];
            Double tempNewNoiseDistance = this.workers[workerID].toTaskDistance[i] + LaplaceUtils.getLaplaceNoise(1, tempNewPrivacyBudget);
            DistanceBudgetPair newEffectiveDistanceBudgetPair = getNewEffectiveNoiseDistanceAndPrivacyBudget(workerID, i, tempNewNoiseDistance, tempNewPrivacyBudget);
            double tempCompeteDistance = newEffectiveDistanceBudgetPair.distance;
            double tempEffectivePrivacyBudget = newEffectiveDistanceBudgetPair.budget;

            // PCF 判断
            double pcfValue = LaplaceProbabilityDensityFunction.probabilityDensityFunction(tempCompeteDistance, lastTermTaskWinnerPackedArray[i].getNoiseEffectiveDistance(), tempEffectivePrivacyBudget, lastTermTaskWinnerPackedArray[i].getEffectivePrivacyBudget());
            if (pcfValue <= 0.5) {
                continue;
            }


            // Utility 函数判断
            Double tempNewUtilityValue = this.getUtilityValue(this.tasks[i].valuation, tempEffectivePrivacyBudget, this.workers[workerID].toTaskDistance[i], tempNewCostPrivacyBudget);
//            if (tempNewUtilityValue <= 0  || tempNewUtilityValue <= this.workers[workerID].successfullyUtilityFunctionValue[i]) {
//                continue;
//            }
            if (tempNewUtilityValue <= 0) {
                continue;
            }



            TaskTargetInfo taskTargetInfo = null;

            if (candidateTaskTargetInfoSet.size() < topK) {
                candidateTaskTargetInfoSet.add(new TaskTargetInfo(i, tempCompeteDistance, tempEffectivePrivacyBudget, tempNewUtilityValue, tempNewCostPrivacyBudget, tempNewPrivacyBudget, tempNewNoiseDistance, tempNewUtilityValue));
            } else {
                taskTargetInfo = candidateTaskTargetInfoSet.last();
                if (tempNewUtilityValue > taskTargetInfo.getTarget()) {
                    candidateTaskTargetInfoSet.remove(taskTargetInfo); //todo: 测试是否能够真的删除
                    candidateTaskTargetInfoSet.add(new TaskTargetInfo(i, tempCompeteDistance, tempEffectivePrivacyBudget, tempNewUtilityValue, tempNewCostPrivacyBudget, tempNewPrivacyBudget, tempNewNoiseDistance, tempNewUtilityValue));
                }
            }

        }
        if (candidateTaskTargetInfoSet.isEmpty()) {
            return null;
        }
        return candidateTaskTargetInfoSet.toArray(new TaskTargetInfo[0]);
    }

    protected TaskTargetInfo[] chooseArrayByUtilityFunctionInfluencedByTaskEntropy(List<Integer> taskIDList, Integer workerID, Integer[] totalCompetingTimesList, WorkerIDDistanceBudgetPair[] lastTermTaskWinnerPackedArray, HashSet<Integer>[] competingWorkerIDSetArray, int topK) {
        TreeSet<TaskTargetInfo> candidateTaskTargetInfoSet = new TreeSet<>(targetInfoForUtilityAndCompositionValueComparator);
        for (Integer i : taskIDList) {
            if (lastTermTaskWinnerPackedArray[i].getWorkerID().equals(workerID)) {
                continue;
            }
            // PPCF 判断
            if (this.workers[workerID].toTaskDistance[i] >= lastTermTaskWinnerPackedArray[i].getNoiseEffectiveDistance()) {
                continue;
            }

            Double tempNewCostPrivacyBudget = getNewCostPrivacyBudget(workerID, i);
            Double tempNewPrivacyBudget =  this.workers[workerID].privacyBudgetArray[i][this.workers[workerID].budgetIndex[i]];
            Double tempNewNoiseDistance = this.workers[workerID].toTaskDistance[i] + LaplaceUtils.getLaplaceNoise(1, tempNewPrivacyBudget);
            DistanceBudgetPair newEffectiveDistanceBudgetPair = getNewEffectiveNoiseDistanceAndPrivacyBudget(workerID, i, tempNewNoiseDistance, tempNewPrivacyBudget);
            double tempCompeteDistance = newEffectiveDistanceBudgetPair.distance;
            double tempEffectivePrivacyBudget = newEffectiveDistanceBudgetPair.budget;

            // PCF 判断
            double pcfValue = LaplaceProbabilityDensityFunction.probabilityDensityFunction(tempCompeteDistance, lastTermTaskWinnerPackedArray[i].getNoiseEffectiveDistance(), tempEffectivePrivacyBudget, lastTermTaskWinnerPackedArray[i].getEffectivePrivacyBudget());
            if (pcfValue <= 0.5) {
                continue;
            }


            // Utility 函数判断
            Double tempNewUtilityValue = this.getUtilityValue(this.tasks[i].valuation, tempEffectivePrivacyBudget, this.workers[workerID].toTaskDistance[i], tempNewCostPrivacyBudget);
//            if (tempNewUtilityValue <= 0  || tempNewUtilityValue <= this.workers[workerID].successfullyUtilityFunctionValue[i]) {
//                continue;
//            }
            if (tempNewUtilityValue <= 0) {
                continue;
            }

            double tempTaskEntropy = super.getTaskEntropy(i, totalCompetingTimesList[i], competingWorkerIDSetArray[i]);
            Double compositionQuantity = tempNewUtilityValue * tempTaskEntropy;


            TaskTargetInfo taskTargetInfo = null;

            if (candidateTaskTargetInfoSet.size() < topK) {
                candidateTaskTargetInfoSet.add(new TaskTargetInfo(i, tempCompeteDistance, tempEffectivePrivacyBudget, compositionQuantity, tempNewCostPrivacyBudget, tempNewPrivacyBudget, tempNewNoiseDistance, tempNewUtilityValue));
            } else {
                taskTargetInfo = candidateTaskTargetInfoSet.last();
                if (tempNewUtilityValue > taskTargetInfo.getTarget()) {
                    candidateTaskTargetInfoSet.remove(taskTargetInfo); //todo: 测试是否能够真的删除
                    candidateTaskTargetInfoSet.add(new TaskTargetInfo(i, tempCompeteDistance, tempEffectivePrivacyBudget, compositionQuantity, tempNewCostPrivacyBudget, tempNewPrivacyBudget, tempNewNoiseDistance, tempNewUtilityValue));
                }
            }

        }
        if (candidateTaskTargetInfoSet.isEmpty()) {
            return null;
        }
        return candidateTaskTargetInfoSet.toArray(new TaskTargetInfo[0]);
    }

    protected TaskTargetInfo[] chooseArrayByTaskEntropy(List<Integer> taskIDList, Integer workerID, Integer[] totalCompetingTimesList, WorkerIDDistanceBudgetPair[] lastTermTaskWinnerPackedArray, HashSet<Integer>[] competingWorkerIDSetArray, int topK) {
        // 遍历taskIDList中的所有task，找出能使当前worker的utility增加最大的task，返回（该task的ID, 申请即将泄露的平均distance[此处可返回扰动], 申请泄露的budget总和[此处可返回当前budget]）
        TreeSet<TaskTargetInfo> candidateTaskTargetInfoSet = new TreeSet<>(targetInfoForTaskEntropyComparator);

        Double newCostPrivacyBudget = null, newPrivacyBudget = null, newNoiseDistance = null, newUtilityValue = null;
        for (Integer i : taskIDList) {

            if (lastTermTaskWinnerPackedArray[i].equals(workerID)) {
                continue;
            }
            // PPCF 判断
            if (this.workers[workerID].toTaskDistance[i] >= lastTermTaskWinnerPackedArray[i].getNoiseEffectiveDistance()) {
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
            if (tempNewUtilityValue <= this.workers[workerID].successfullyUtilityFunctionValue[i]) {
                continue;
            }

            // PCF 判断
            double pcfValue = LaplaceProbabilityDensityFunction.probabilityDensityFunction(tempCompeteDistance, lastTermTaskWinnerPackedArray[i].getNoiseEffectiveDistance(), tempEffectivePrivacyBudget, lastTermTaskWinnerPackedArray[i].getEffectivePrivacyBudget());
            if (pcfValue <= 0.5) {
                continue;
            }

            // 根据 taskEntropy 挑选
            double tempTaskEntropy = super.getTaskEntropy(i, totalCompetingTimesList[i], competingWorkerIDSetArray[i]);
            TaskTargetInfo taskTargetInfo = null;

            if (candidateTaskTargetInfoSet.size() < topK) {
                candidateTaskTargetInfoSet.add(new TaskTargetInfo(i, tempCompeteDistance, tempEffectivePrivacyBudget, tempTaskEntropy, tempNewCostPrivacyBudget, tempNewPrivacyBudget, tempNewNoiseDistance, tempNewUtilityValue));
            } else {
                // 应该挑选task entropy 最大的!!!
                taskTargetInfo = candidateTaskTargetInfoSet.last();
                if (tempTaskEntropy > taskTargetInfo.getTarget()) {
                    candidateTaskTargetInfoSet.remove(taskTargetInfo); //todo: 测试是否能够真的删除
                    candidateTaskTargetInfoSet.add(new TaskTargetInfo(i, tempCompeteDistance, tempEffectivePrivacyBudget, tempTaskEntropy, tempNewCostPrivacyBudget, tempNewPrivacyBudget, tempNewNoiseDistance, tempNewUtilityValue));
                }
            }

        }
        if (candidateTaskTargetInfoSet.isEmpty()) {
            return null;
        }
        return candidateTaskTargetInfoSet.toArray(new TaskTargetInfo[0]);
    }
    protected TaskTargetInfo[] chooseArrayByProposingValue(List<Integer> taskIDList, Integer workerID, WorkerIDDistanceBudgetPair[] lastTermTaskWinnerPackedArray, int topK) {
        Integer taskID = null;
        TreeSet<TaskTargetInfo> candidateTaskTargetInfoSet = new TreeSet<>(targetInfoForProposingValueComparator);
        for (Integer i : taskIDList) {
            if (lastTermTaskWinnerPackedArray[i].getWorkerID().equals(workerID)) {
                continue;
            }
            // PPCF 判断
            if (this.workers[workerID].toTaskDistance[i] >= lastTermTaskWinnerPackedArray[i].getNoiseEffectiveDistance()) {
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
            if (tempNewUtilityValue <= this.workers[workerID].successfullyUtilityFunctionValue[i]) {
                continue;
            }

            // PCF 判断
            double pcfValue = LaplaceProbabilityDensityFunction.probabilityDensityFunction(tempCompeteDistance, lastTermTaskWinnerPackedArray[i].getNoiseEffectiveDistance(), tempEffectivePrivacyBudget, lastTermTaskWinnerPackedArray[i].getEffectivePrivacyBudget());
            if (pcfValue <= 0.5) {
                continue;
            }

            /**
             * 获取Proposing Value
             */
            double tempProposingValue = super.getProposingValue(pcfValue, this.workers[workerID].toTaskDistance[i]);

            TaskTargetInfo taskTargetInfo = null;

            if (candidateTaskTargetInfoSet.size() < topK) {
                candidateTaskTargetInfoSet.add(new TaskTargetInfo(i, tempCompeteDistance, tempEffectivePrivacyBudget, tempProposingValue, tempNewCostPrivacyBudget, tempNewPrivacyBudget, tempNewNoiseDistance, tempNewUtilityValue));
            } else {
                taskTargetInfo = candidateTaskTargetInfoSet.last();
                if (tempProposingValue > taskTargetInfo.getTarget()) {
                    candidateTaskTargetInfoSet.remove(taskTargetInfo); //todo: 测试是否能够真的删除
                    candidateTaskTargetInfoSet.add(new TaskTargetInfo(i, tempCompeteDistance, tempEffectivePrivacyBudget, tempProposingValue, tempNewCostPrivacyBudget, tempNewPrivacyBudget, tempNewNoiseDistance, tempNewUtilityValue));
                }
            }

        }
        if (candidateTaskTargetInfoSet.isEmpty()) {
            return null;
        }
        return candidateTaskTargetInfoSet.toArray(new TaskTargetInfo[0]);
    }

    protected TaskTargetInfo[] chooseArrayByUtilityFunctionInfluencedByProposingValue(List<Integer> taskIDList, Integer workerID, WorkerIDDistanceBudgetPair[] lastTermTaskWinnerPackedArray, int topK) {
        Integer taskID = null;
        TreeSet<TaskTargetInfo> candidateTaskTargetInfoSet = new TreeSet<>(targetInfoForUtilityAndCompositionValueComparator);
        for (Integer i : taskIDList) {
            if (lastTermTaskWinnerPackedArray[i].getWorkerID().equals(workerID)) {
                continue;
            }
            // PPCF 判断
            if (this.workers[workerID].toTaskDistance[i] >= lastTermTaskWinnerPackedArray[i].getNoiseEffectiveDistance()) {
                continue;
            }

            Double tempNewCostPrivacyBudget = getNewCostPrivacyBudget(workerID, i);
            Double tempNewPrivacyBudget =  this.workers[workerID].privacyBudgetArray[i][this.workers[workerID].budgetIndex[i]];
            Double tempNewNoiseDistance = this.workers[workerID].toTaskDistance[i] + LaplaceUtils.getLaplaceNoise(1, tempNewPrivacyBudget);
            DistanceBudgetPair newEffectiveDistanceBudgetPair = getNewEffectiveNoiseDistanceAndPrivacyBudget(workerID, i, tempNewNoiseDistance, tempNewPrivacyBudget);
            double tempCompeteDistance = newEffectiveDistanceBudgetPair.distance;
            double tempEffectivePrivacyBudget = newEffectiveDistanceBudgetPair.budget;

            // Utility 函数判断

            // PCF 判断
            double pcfValue = LaplaceProbabilityDensityFunction.probabilityDensityFunction(tempCompeteDistance, lastTermTaskWinnerPackedArray[i].getNoiseEffectiveDistance(), tempEffectivePrivacyBudget, lastTermTaskWinnerPackedArray[i].getEffectivePrivacyBudget());
            if (pcfValue <= 0.5) {
                continue;
            }
            Double tempNewUtilityValue = this.getUtilityValue(this.tasks[i].valuation, tempEffectivePrivacyBudget, this.workers[workerID].toTaskDistance[i], tempNewCostPrivacyBudget);
//            if (tempNewUtilityValue <= this.workers[workerID].successfullyUtilityFunctionValue[i]) {
            if (tempNewUtilityValue <= 0) {
                continue;
            }

            /**
             * 获取Proposing Value
             */
            double tempProposingValue = super.getProposingValue(pcfValue, this.workers[workerID].toTaskDistance[i]);
            Double compositionQuantity = tempNewUtilityValue * tempProposingValue;

            TaskTargetInfo taskTargetInfo = null;

            if (candidateTaskTargetInfoSet.size() < topK) {
                candidateTaskTargetInfoSet.add(new TaskTargetInfo(i, tempCompeteDistance, tempEffectivePrivacyBudget, compositionQuantity, tempNewCostPrivacyBudget, tempNewPrivacyBudget, tempNewNoiseDistance, tempNewUtilityValue));
            } else {
                taskTargetInfo = candidateTaskTargetInfoSet.last();
                if (tempProposingValue > taskTargetInfo.getTarget()) {
                    candidateTaskTargetInfoSet.remove(taskTargetInfo); //todo: 测试是否能够真的删除
                    candidateTaskTargetInfoSet.add(new TaskTargetInfo(i, tempCompeteDistance, tempEffectivePrivacyBudget, compositionQuantity, tempNewCostPrivacyBudget, tempNewPrivacyBudget, tempNewNoiseDistance, tempNewUtilityValue));
                }
            }

        }
        if (candidateTaskTargetInfoSet.isEmpty()) {
            return null;
        }
        return candidateTaskTargetInfoSet.toArray(new TaskTargetInfo[0]);
    }


    /**
     * 1种server的选择函数
     */
    /**
     *
     * @param taskCurrentWinnerPackedArray 记录当前每个task被竞争获胜的worker的信息
     * @param newCandidateWorkerIDList 记录每个task候选的worker id列表
     */
    public WorkerIDDistanceBudgetPair[] serverExecute(WorkerIDDistanceBudgetPair[] taskCurrentWinnerPackedArray, List<Integer>[] newCandidateWorkerIDList, Set<Integer> newTotalCompetingWorkerIDSet) {
        Integer currentWinnerWorkerID, beforeWinnerWorkerID;
        WorkerIDDistanceBudgetPair[] taskBeforeWinnerPackedArray = taskCurrentWinnerPackedArray;
        // 1. 获取获胜者，并更新胜利列表
        List<WorkerIDDistanceBudgetPair>[] sortedTable = createTableDataOfPreferenceTableByID(taskBeforeWinnerPackedArray, newCandidateWorkerIDList);
        taskCurrentWinnerPackedArray = this.conflictElimination.assignment(sortedTable);

        for (int i = 0; i < taskCurrentWinnerPackedArray.length; i++) {
            if (taskCurrentWinnerPackedArray[i] == null ||taskCurrentWinnerPackedArray[i].getWorkerID().equals(taskBeforeWinnerPackedArray[i].getWorkerID())) {
                continue;
            }
            currentWinnerWorkerID = taskCurrentWinnerPackedArray[i].getWorkerID();
            beforeWinnerWorkerID = taskBeforeWinnerPackedArray[i].getWorkerID();
            // 2. 设置获胜的worker的[有效噪声距离、有效隐私预算](已经包含在packedArray中，无需设置)、当前效用函数值、胜利状态
//            if (currentWinnerWorkerID.equals(-1)) {
//                System.out.println("wocao!");
//            }
            this.workers[currentWinnerWorkerID].successfullyUtilityFunctionValue[i] = this.workers[currentWinnerWorkerID].currentUtilityFunctionValue[i];
            this.workers[currentWinnerWorkerID].currentWinningState = true; // 设置成胜利者，之后在没被竞争掉的情况下不会再竞争其他task
            // 3. 设置被竞争下去的worker的当前效用函数值、胜利状态，并将其加入竞争列表
            if (!beforeWinnerWorkerID.equals(-1)) {
                this.workers[beforeWinnerWorkerID].successfullyUtilityFunctionValue[i] -= this.tasks[i].valuation;
                this.workers[beforeWinnerWorkerID].currentWinningState = false;
                // 将被竞争下去的worker加入可竞争集合，以便获取考察资格
                newTotalCompetingWorkerIDSet.add(beforeWinnerWorkerID);
            }

        }

        return taskCurrentWinnerPackedArray;

    }


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


        // 针对每个task，记录当前竞争成功的worker的信息
        WorkerIDDistanceBudgetPair[] taskCurrentWinnerPackedArray = new WorkerIDDistanceBudgetPair[this.tasks.length];

        // todo: 封装到了initializeAllocations

        // 针对每个task，本轮提出竞争的worker的ID（每轮需要清空）
        List<Integer>[] newCandidateWorkerIDList, oldCandidateWorkerIDList;
        newCandidateWorkerIDList = new ArrayList[this.tasks.length];
//        BasicArray.setListArrayToEmptyList(newCandidateWorkerIDList);
        initializeAllocationByFirstTaskAndNullAllocation(taskCurrentWinnerPackedArray, competingTimes, completedWorkerIDSet);


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
//            newCandidateWorkerIDList
            BasicArray.setListArrayToEmptyList(newCandidateWorkerIDList);

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

                // 更新下一轮可以去竞争的worker的集合
                newTotalCompetingWorkerIDSet.add(tempWorkerID);

                // 进行是否竞争判断3： 考察 utility函数、PPCF函数、PCF函数、任务熵

                TaskTargetInfo[] winnerInfoArray;
                winnerInfoArray = chooseArrayByUtilityFunction(tempCandidateTaskList, tempWorkerID, taskCurrentWinnerPackedArray, proposalSize);
//                winnerInfoArray = chooseArrayByTaskEntropy(tempCandidateTaskList, tempWorkerID, competingTimes, taskCurrentWinnerPackedArray, completedWorkerIDSet, proposalSize);
//                winnerInfoArray = chooseArrayByProposingValue(tempCandidateTaskList, tempWorkerID, taskCurrentWinnerPackedArray, proposalSize);
                if (winnerInfoArray == null) {
                    continue;
                }

                // 否则（竞争成功），发布当前扰动距离长度和隐私预算(这里只添加进候选列表供server进一步选择)，并将隐私自己的预算索引值加1



                /**
                 * for test
                System.out.println(tempWorkerID);
                Integer[] tempChosenTaskIDArray = new Integer[winnerInfoArray.length];
                for (int i = 0; i < winnerInfoArray.length; i++) {
                    tempChosenTaskIDArray[i] = winnerInfoArray[i].taskID;
                }
                MyPrint.showIntegerArray(tempChosenTaskIDArray, ", ", 2);
                */

                Integer tempTaskID;
                for (int i = 0; i < winnerInfoArray.length; i++) {
                    tempTaskID = winnerInfoArray[i].getTaskID();
                    newCandidateWorkerIDList[tempTaskID].add(tempWorkerID);
                    completedWorkerIDSet[tempTaskID].add(tempWorkerID);
                    this.workers[tempWorkerID].alreadyPublishedNoiseDistanceAndBudgetTreeSetArray[tempTaskID].add(new DistanceBudgetPair(winnerInfoArray[i].getNewNoiseDistance(), winnerInfoArray[i].getNewPrivacyBudget()));
                    this.workers[tempWorkerID].effectiveNoiseDistance[tempTaskID] = winnerInfoArray[i].getNoiseEffectiveDistance();
                    this.workers[tempWorkerID].effectivePrivacyBudget[tempTaskID] = winnerInfoArray[i].getEffectivePrivacyBudget();
                    this.workers[tempWorkerID].currentUtilityFunctionValue[tempTaskID] = winnerInfoArray[i].getNewUtilityValue();
                    this.workers[tempWorkerID].budgetIndex[tempTaskID] ++;
                    this.workers[tempWorkerID].taskCompetingTimes[tempTaskID] ++;
                    competingTimes[tempTaskID] ++;

                }

            }
            taskCurrentWinnerPackedArray = serverExecute(taskCurrentWinnerPackedArray, newCandidateWorkerIDList, newTotalCompetingWorkerIDSet);
        }
        return taskCurrentWinnerPackedArray;
    }






}
