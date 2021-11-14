package edu.ecnu.dll.scheme.solution._3_multiple_task;

import edu.ecnu.dll.basic_struct.agent.Task;
import edu.ecnu.dll.basic_struct.comparator.TargetInfoForTaskEntropyComparator;
import edu.ecnu.dll.basic_struct.pack.single_agent_info.sub_class.DistanceBudgetPair;
import edu.ecnu.dll.basic_struct.pack.single_agent_info.sub_class.sub_class.TaskTargetInfo;
import edu.ecnu.dll.basic_struct.pack.single_agent_info.sub_class.WorkerIDDistanceBudgetPair;
import edu.ecnu.dll.scheme.solution.Solution;
import edu.ecnu.dll.scheme.solution._3_multiple_task.struct.EnhanceConflictElimination;
import edu.ecnu.dll.scheme.struct.task.BasicTask;
import edu.ecnu.dll.scheme.struct.worker.MultiTaskBasicWorker;
import edu.ecnu.dll.scheme_compared.solution.ConflictElimination;
import tools.basic.BasicArray;
import tools.basic.BasicCalculation;
import tools.differential_privacy.compare.impl.LaplaceProbabilityDensityFunction;
import tools.differential_privacy.noise.LaplaceUtils;
import tools.struct.Point;
import tools.struct.table.PrivacyPreferenceTable;

import java.util.*;

public class MultiTaskMultiCompetitionSolution extends Solution {


    public static final Integer ONLY_UTILITY = 0;
    public static final Integer UTILITY_WITH_TASK_ENTROPY = 1;
    public static final Integer UTILITY_WITH_PROPOSING_VALUE = 2;

    public Task[] tasks = null;
    public MultiTaskBasicWorker[] workers = null;
    public ConflictElimination conflictElimination = null;


    public static TargetInfoForTaskEntropyComparator targetInfoForTaskEntropyComparator = new TargetInfoForTaskEntropyComparator(TargetInfoForTaskEntropyComparator.DESCENDING);
    public static TargetInfoForTaskEntropyComparator targetInfoForProposingValueComparator = new TargetInfoForTaskEntropyComparator(TargetInfoForTaskEntropyComparator.DESCENDING);
    public static TargetInfoForTaskEntropyComparator targetInfoForUtilityValueComparator = new TargetInfoForTaskEntropyComparator(TargetInfoForTaskEntropyComparator.DESCENDING);
    public static TargetInfoForTaskEntropyComparator targetInfoForUtilityAndCompositionValueComparator = new TargetInfoForTaskEntropyComparator(TargetInfoForTaskEntropyComparator.DESCENDING);


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
                tempPair = new WorkerIDDistanceBudgetPair(workerID, this.workers[workerID].getEffectiveNoiseDistance(i), this.workers[workerID].getEffectivePrivacyBudget(i));
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
                tempPair = new WorkerIDDistanceBudgetPair(workerID, this.workers[workerID].getEffectiveNoiseDistance(i), this.workers[workerID].getEffectivePrivacyBudget(i));
                table[i].add(tempPair);
            }
            if (!originalWinnerInfo[i].getWorkerID().equals(ConflictElimination.DEFAULT_WORKER_ID_DISTANCE_BUDGET_PAIR.getWorkerID()) || workerIDList[i].isEmpty()) {
                table[i].add(originalWinnerInfo[i]);
            }
            PrivacyPreferenceTable.sortedPreferenceTable(table[i], this.conflictElimination.workerIDDistanceBudgetPairComparator);
        }
        return table;
    }


    /**
     *  3个 get 函数
     */
    protected double getUtilityValue(double taskValue, double effectivePrivacyBudget, double realDistance, double privacyBudgetCost) {
        return taskValue + taskValue * toNormalValue(effectivePrivacyBudget) - alpha * realDistance - beta * privacyBudgetCost;
    }

    protected DistanceBudgetPair getNewEffectiveNoiseDistanceAndPrivacyBudget(Integer workerID, Integer taskID, double newNoiseDistance, double newPrivacyBudget) {
        //todo: test maximumLikelihood
        TreeSet<DistanceBudgetPair> tempTreeSet = new TreeSet<>();
//        tempTreeSet.addAll(this.workers[workerID].alreadyPublishedNoiseDistanceAndBudgetTreeSetArray[taskID]);
        tempTreeSet.addAll(this.workers[workerID].getAlreadyPublishedNoiseDistanceAndBudgetTreeSet(taskID));
        tempTreeSet.add(new DistanceBudgetPair(newNoiseDistance, newPrivacyBudget));
        double[] distanceBudget = LaplaceUtils.getMaximumLikelihoodEstimationInGivenPoint(tempTreeSet);
        return new DistanceBudgetPair(distanceBudget[0], distanceBudget[1]);
    }
    protected Double getNewCostPrivacyBudget(Integer workerID, Integer taskID) {
        Double result;
//        int index = this.workers[workerID].budgetIndex[taskID];
        int index = this.workers[workerID].getBudgetIndex(taskID);
//        result = this.workers[workerID].privacyBudgetCost[taskID] + this.workers[workerID].privacyBudgetArray[taskID][index];
        result = this.workers[workerID].getPrivacyBudgetCost(taskID) + this.workers[workerID].getPrivacyBudgetArray(taskID)[index];
        return result;
    }


    /**
     *  5个初始化函数
     */
    public void initializeBasicInformation(List<Point> taskPositionList, Double[] taskValueArray, List<Point> workerPositionList, List<Double> workerRangeList) {
        Point taskPosition, workerPosition;
        // 同时初始化父类
        super.tasks = this.tasks = new BasicTask[taskPositionList.size()];
        for (int i = 0; i < taskPositionList.size(); i++) {
            taskPosition = taskPositionList.get(i);
            this.tasks[i] = new BasicTask(taskPosition.getIndex());
            this.tasks[i].valuation = taskValueArray[i];
        }
        // 同时初始化父类
        super.workers = this.workers = new MultiTaskBasicWorker[workerPositionList.size()];
        for (int j = 0; j < workers.length; j++) {
            workerPosition = workerPositionList.get(j);
            this.workers[j] = new MultiTaskBasicWorker(workerPosition.getIndex());
            this.workers[j].setMaxRange(workerRangeList.get(j));
            this.workers[j].taskIndex = BasicArray.getInitializedArray(-1, this.tasks.length);
        }

    }

    public void initializeAgents(List<Double[]>[] privacyBudgetListArray, List<Double[]>[] noiseDistanceListArray) {
        for (int j = 0; j < this.workers.length; j++) {
            this.workers[j].reverseIndex = new ArrayList<>();
            this.workers[j].toTaskDistance = new ArrayList<>();
            // 此处没有初始化privacyBudgetArray，因为会在initialize basic function 中初始化
            this.workers[j].privacyBudgetArrayList = new ArrayList<>();
            this.workers[j].noiseDistanceArrayList = new ArrayList<>();
            this.workers[j].budgetIndex = new ArrayList<>();
            this.workers[j].alreadyPublishedNoiseDistanceAndBudgetTreeSetArray = new ArrayList<>();
            this.workers[j].effectiveNoiseDistance = new ArrayList<>();
            this.workers[j].effectivePrivacyBudget = new ArrayList<>();
            this.workers[j].privacyBudgetCost = new ArrayList<>();
            this.workers[j].taskCompetingTimes = new ArrayList<>();
            this.workers[j].currentUtilityFunctionValue = new ArrayList<>();
            this.workers[j].successfullyUtilityFunctionValue = new ArrayList<>();
            this.workers[j].currentWinningState = -1;
            double tempDistance;
            int k = 0;
            for (int i = 0; i < tasks.length; i++) {
                // todo: 修改为经纬度的
                tempDistance = BasicCalculation.get2Norm(this.tasks[i].location, this.workers[j].location);
                if (tempDistance <= this.workers[j].maxRange) {
                    this.workers[j].taskIndex[i] = k++;
                    this.workers[j].reverseIndex.add(i);
                    this.workers[j].toTaskDistance.add(tempDistance);
                    this.workers[j].budgetIndex.add(0);
                    this.workers[j].privacyBudgetArrayList.add(privacyBudgetListArray[j].get(i));
//                    this.workers[j].noiseDistanceArrayList.add(LaplaceUtils.getLaplaceNoiseWithOriginalValue(tempDistance, privacyBudgetListArray[j].get(i)));
                    this.workers[j].noiseDistanceArrayList.add(noiseDistanceListArray[j].get(i));
                    this.workers[j].alreadyPublishedNoiseDistanceAndBudgetTreeSetArray.add(new TreeSet<>());
                    this.workers[j].effectiveNoiseDistance.add(0.0);
                    this.workers[j].effectivePrivacyBudget.add(0.0);
                    this.workers[j].privacyBudgetCost.add(0.0);
                    this.workers[j].taskCompetingTimes.add(0);
                    this.workers[j].currentUtilityFunctionValue.add(0.0);
                    this.workers[j].successfullyUtilityFunctionValue.add(0.0);
                }
            }
        }
    }

    public void initializeAgentsWithLatitudeLongitude(List<Double[]>[] privacyBudgetListArray, List<Double[]>[] noiseDistanceListArray) {
        for (int j = 0; j < this.workers.length; j++) {
            this.workers[j].reverseIndex = new ArrayList<>();
            this.workers[j].toTaskDistance = new ArrayList<>();
            // 此处没有初始化privacyBudgetArray，因为会在initialize basic function 中初始化
            this.workers[j].privacyBudgetArrayList = new ArrayList<>();
            this.workers[j].noiseDistanceArrayList = new ArrayList<>();
            this.workers[j].budgetIndex = new ArrayList<>();
            this.workers[j].alreadyPublishedNoiseDistanceAndBudgetTreeSetArray = new ArrayList<>();
            this.workers[j].effectiveNoiseDistance = new ArrayList<>();
            this.workers[j].effectivePrivacyBudget = new ArrayList<>();
            this.workers[j].privacyBudgetCost = new ArrayList<>();
            this.workers[j].taskCompetingTimes = new ArrayList<>();
            this.workers[j].currentUtilityFunctionValue = new ArrayList<>();
            this.workers[j].successfullyUtilityFunctionValue = new ArrayList<>();
            this.workers[j].currentWinningState = -1;
            double tempDistance;
            int k = 0;
            for (int i = 0; i < tasks.length; i++) {
                // todo: 修改为经纬度的
                tempDistance = BasicCalculation.getDistanceFrom2LngLat(this.tasks[i].location[1], this.tasks[i].location[0], this.workers[j].location[1],this.workers[j].location[0]);
                if (tempDistance <= this.workers[j].maxRange) {
                    this.workers[j].taskIndex[i] = k++;
                    this.workers[j].reverseIndex.add(i);
                    this.workers[j].toTaskDistance.add(tempDistance);
                    this.workers[j].budgetIndex.add(0);
                    this.workers[j].privacyBudgetArrayList.add(privacyBudgetListArray[j].get(i));
//                    this.workers[j].noiseDistanceArrayList.add(LaplaceUtils.getLaplaceNoiseWithOriginalValue(tempDistance, privacyBudgetListArray[j].get(i)));
                    this.workers[j].noiseDistanceArrayList.add(noiseDistanceListArray[j].get(i));
                    this.workers[j].alreadyPublishedNoiseDistanceAndBudgetTreeSetArray.add(new TreeSet<>());
                    this.workers[j].effectiveNoiseDistance.add(0.0);
                    this.workers[j].effectivePrivacyBudget.add(0.0);
                    this.workers[j].privacyBudgetCost.add(0.0);
                    this.workers[j].taskCompetingTimes.add(0);
                    this.workers[j].currentUtilityFunctionValue.add(0.0);
                    this.workers[j].successfullyUtilityFunctionValue.add(0.0);
                }
            }
        }
    }

    protected void setCandidateTaskByBudget(List<Integer> tempCandidateTaskList, MultiTaskBasicWorker worker) {
        // 只遍历privacybudget列表，即限制遍历的task为range范围内
        for (int i = 0; i < worker.privacyBudgetArrayList.size(); i++) {
            if (worker.budgetIndex.get(i) < worker.privacyBudgetArrayList.get(i).length) {
                tempCandidateTaskList.add(worker.reverseIndex.get(i));
            }
        }
    }

    protected void initializeAllocationByFirstTaskAndNullAllocation(WorkerIDDistanceBudgetPair[] taskCurrentWinnerPackedArray, Integer[] competingTimes, HashSet<Integer>[] competedWorkerIDSet) {
        // 针对每个task，初始化距离为最大距离值
        // 针对每个task，初始化对应距离的隐私预算为最大隐私预算
        // 针对每个task，初始化总的被竞争次数为0
        // 针对每个task，初始化访问过被访问worker集合为空集合
        for (int i = 0; i < this.tasks.length; i++) {
            taskCurrentWinnerPackedArray[i] = ConflictElimination.DEFAULT_WORKER_ID_DISTANCE_BUDGET_PAIR;
            competingTimes[i] = 0;
            competedWorkerIDSet[i] = new HashSet<>();
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
    protected TaskTargetInfo[] chooseArrayByUtilityFunction(List<Integer> taskIDList, Integer workerID, WorkerIDDistanceBudgetPair[] lastTermTaskWinnerPackedArray, int topK, boolean ppcfState){
        TreeSet<TaskTargetInfo> candidateTaskTargetInfoSet = new TreeSet<>(targetInfoForUtilityValueComparator);
        for (Integer i : taskIDList) {
            if (lastTermTaskWinnerPackedArray[i].getWorkerID().equals(workerID)) {
                continue;
            }
            // PPCF 判断
            if (ppcfState) {
                if (this.workers[workerID].getToTaskDistance(i) >= lastTermTaskWinnerPackedArray[i].getNoiseEffectiveDistance()) {
                    // 如果PPCF不占优，则通过设置privacybuget状态为用尽状态来防止接下来被再次选择
                    this.workers[workerID].setBudgetIndex(i, Integer.MAX_VALUE);
                    continue;
                }
            }

            Double tempNewCostPrivacyBudget = getNewCostPrivacyBudget(workerID, i);
            Double tempNewPrivacyBudget =  this.workers[workerID].getPrivacyBudgetArray(i)[this.workers[workerID].getBudgetIndex(i)];
            Double tempNewNoiseDistance = this.workers[workerID].getNoiseDistanceArray(i)[this.workers[workerID].getBudgetIndex(i)];
            this.workers[workerID].increaseBudgetIndex(i);

            DistanceBudgetPair newEffectiveDistanceBudgetPair = getNewEffectiveNoiseDistanceAndPrivacyBudget(workerID, i, tempNewNoiseDistance, tempNewPrivacyBudget);
            double tempCompeteDistance = newEffectiveDistanceBudgetPair.distance;
            double tempEffectivePrivacyBudget = newEffectiveDistanceBudgetPair.budget;

            // PCF 判断
            double pcfValue = LaplaceProbabilityDensityFunction.probabilityDensityFunction(tempCompeteDistance, lastTermTaskWinnerPackedArray[i].getNoiseEffectiveDistance(), tempEffectivePrivacyBudget, lastTermTaskWinnerPackedArray[i].getEffectivePrivacyBudget());
            if (pcfValue <= 0.5) {
                continue;
            }


            // Utility 函数判断
            Double tempNewUtilityValue = this.getUtilityValue(this.tasks[i].valuation, tempEffectivePrivacyBudget, this.workers[workerID].getToTaskDistance(i), tempNewCostPrivacyBudget);
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

    protected TaskTargetInfo[] chooseArrayByUtilityFunctionInfluencedByTaskEntropy(List<Integer> taskIDList, Integer workerID, Integer[] totalCompetingTimeArray, WorkerIDDistanceBudgetPair[] lastTermTaskWinnerPackedArray, HashSet<Integer>[] competingWorkerIDSetArray, int topK, boolean ppcfState) {
        TreeSet<TaskTargetInfo> candidateTaskTargetInfoSet = new TreeSet<>(targetInfoForUtilityAndCompositionValueComparator);
        for (Integer i : taskIDList) {
            if (lastTermTaskWinnerPackedArray[i].getWorkerID().equals(workerID)) {
                continue;
            }
            // PPCF 判断
            if (ppcfState) {
                if (this.workers[workerID].getToTaskDistance(i) >= lastTermTaskWinnerPackedArray[i].getNoiseEffectiveDistance()) {
                    // 如果PPCF不占优，则通过设置privacybuget状态为用尽状态来防止接下来被再次选择
                    this.workers[workerID].setBudgetIndex(i, Integer.MAX_VALUE);
                    continue;
                }
            }

            Double tempNewCostPrivacyBudget = getNewCostPrivacyBudget(workerID, i);
            Double tempNewPrivacyBudget =  this.workers[workerID].getPrivacyBudgetArray(i)[this.workers[workerID].getBudgetIndex(i)];
//            Double tempNewNoiseDistance = this.workers[workerID].getToTaskDistance(i) + LaplaceUtils.getLaplaceNoise(1, tempNewPrivacyBudget);
            Double tempNewNoiseDistance = this.workers[workerID].getNoiseDistanceArray(i)[this.workers[workerID].getBudgetIndex(i)];
            this.workers[workerID].increaseBudgetIndex(i);

            DistanceBudgetPair newEffectiveDistanceBudgetPair = getNewEffectiveNoiseDistanceAndPrivacyBudget(workerID, i, tempNewNoiseDistance, tempNewPrivacyBudget);
            double tempCompeteDistance = newEffectiveDistanceBudgetPair.distance;
            double tempEffectivePrivacyBudget = newEffectiveDistanceBudgetPair.budget;

            // PCF 判断
            double pcfValue = LaplaceProbabilityDensityFunction.probabilityDensityFunction(tempCompeteDistance, lastTermTaskWinnerPackedArray[i].getNoiseEffectiveDistance(), tempEffectivePrivacyBudget, lastTermTaskWinnerPackedArray[i].getEffectivePrivacyBudget());
            if (pcfValue <= 0.5) {
                continue;
            }


            // Utility 函数判断
            Double tempNewUtilityValue = this.getUtilityValue(this.tasks[i].valuation, tempEffectivePrivacyBudget, this.workers[workerID].getToTaskDistance(i), tempNewCostPrivacyBudget);
//            if (tempNewUtilityValue <= 0  || tempNewUtilityValue <= this.workers[workerID].successfullyUtilityFunctionValue[i]) {
//                continue;
//            }
            if (tempNewUtilityValue <= 0) {
                continue;
            }

            double tempTaskEntropy = super.getTaskEntropy(i, totalCompetingTimeArray[i], competingWorkerIDSetArray[i]);
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

    protected TaskTargetInfo[] chooseArrayByUtilityFunctionInfluencedByProposingValue(List<Integer> taskIDList, Integer workerID, WorkerIDDistanceBudgetPair[] lastTermTaskWinnerPackedArray, int topK, boolean ppcfState) {
        Integer taskID = null;
        TreeSet<TaskTargetInfo> candidateTaskTargetInfoSet = new TreeSet<>(targetInfoForUtilityAndCompositionValueComparator);
        for (Integer i : taskIDList) {
            if (lastTermTaskWinnerPackedArray[i].getWorkerID().equals(workerID)) {
                continue;
            }
            // PPCF 判断
            if (ppcfState) {
                if (this.workers[workerID].getToTaskDistance(i) >= lastTermTaskWinnerPackedArray[i].getNoiseEffectiveDistance()) {
                    // 如果PPCF不占优，则通过设置privacybuget状态为用尽状态来防止接下来被再次选择
                    this.workers[workerID].setBudgetIndex(i, Integer.MAX_VALUE);
                    continue;
                }
            }

            Double tempNewCostPrivacyBudget = getNewCostPrivacyBudget(workerID, i);
            Double tempNewPrivacyBudget =  this.workers[workerID].getPrivacyBudgetArray(i)[this.workers[workerID].getBudgetIndex(i)];
            Double tempNewNoiseDistance = this.workers[workerID].getNoiseDistanceArray(i)[this.workers[workerID].getBudgetIndex(i)];
            this.workers[workerID].increaseBudgetIndex(i);

            DistanceBudgetPair newEffectiveDistanceBudgetPair = getNewEffectiveNoiseDistanceAndPrivacyBudget(workerID, i, tempNewNoiseDistance, tempNewPrivacyBudget);
            double tempCompeteDistance = newEffectiveDistanceBudgetPair.distance;
            double tempEffectivePrivacyBudget = newEffectiveDistanceBudgetPair.budget;

            // Utility 函数判断

            // PCF 判断
            double pcfValue = LaplaceProbabilityDensityFunction.probabilityDensityFunction(tempCompeteDistance, lastTermTaskWinnerPackedArray[i].getNoiseEffectiveDistance(), tempEffectivePrivacyBudget, lastTermTaskWinnerPackedArray[i].getEffectivePrivacyBudget());
            if (pcfValue <= 0.5) {
                continue;
            }
            Double tempNewUtilityValue = this.getUtilityValue(this.tasks[i].valuation, tempEffectivePrivacyBudget, this.workers[workerID].getToTaskDistance(i), tempNewCostPrivacyBudget);
//            if (tempNewUtilityValue <= this.workers[workerID].successfullyUtilityFunctionValue[i]) {
            if (tempNewUtilityValue <= 0) {
                continue;
            }

            /**
             * 获取Proposing Value
             */
            double tempProposingValue = super.getProposingValue(pcfValue, this.workers[workerID].getToTaskDistance(i));
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

    protected TaskTargetInfo[] chooseArrayByTaskEntropy(List<Integer> taskIDList, Integer workerID, Integer[] totalCompetingTimesList, WorkerIDDistanceBudgetPair[] lastTermTaskWinnerPackedArray, HashSet<Integer>[] competingWorkerIDSetArray, int topK, boolean ppcfState) {
        // 遍历taskIDList中的所有task，找出能使当前worker的utility增加最大的task，返回（该task的ID, 申请即将泄露的平均distance[此处可返回扰动], 申请泄露的budget总和[此处可返回当前budget]）
        TreeSet<TaskTargetInfo> candidateTaskTargetInfoSet = new TreeSet<>(targetInfoForTaskEntropyComparator);

        Double newCostPrivacyBudget = null, newPrivacyBudget = null, newNoiseDistance = null, newUtilityValue = null;
        for (Integer i : taskIDList) {

            if (lastTermTaskWinnerPackedArray[i].equals(workerID)) {
                continue;
            }
            // PPCF 判断
            if (ppcfState) {
                if (this.workers[workerID].getToTaskDistance(i) >= lastTermTaskWinnerPackedArray[i].getNoiseEffectiveDistance()) {
                    // 如果PPCF不占优，则通过设置privacybuget状态为用尽状态来防止接下来被再次选择
                    this.workers[workerID].setBudgetIndex(i, Integer.MAX_VALUE);
                    continue;
                }
            }

            Double tempNewCostPrivacyBudget = getNewCostPrivacyBudget(workerID, i);
            Double tempNewPrivacyBudget =  this.workers[workerID].getPrivacyBudgetArray(i)[this.workers[workerID].getBudgetIndex(i)];
//            Double tempNewNoiseDistance = this.workers[workerID].toTaskDistance[i] + LaplaceUtils.getLaplaceNoise(1, tempNewPrivacyBudget);
            Double tempNewNoiseDistance = this.workers[workerID].getNoiseDistanceArray(i)[this.workers[workerID].getBudgetIndex(i)];
//            this.workers[workerID].budgetIndex[i] ++;
            this.workers[workerID].increaseBudgetIndex(i);

            DistanceBudgetPair newEffectiveDistanceBudgetPair = getNewEffectiveNoiseDistanceAndPrivacyBudget(workerID, i, tempNewNoiseDistance, tempNewPrivacyBudget);
            double tempCompeteDistance = newEffectiveDistanceBudgetPair.distance;
            double tempEffectivePrivacyBudget = newEffectiveDistanceBudgetPair.budget;

            // Utility 函数值判断
            Double tempNewUtilityValue = this.getUtilityValue(this.tasks[i].valuation, tempEffectivePrivacyBudget, this.workers[workerID].getToTaskDistance(i), tempNewCostPrivacyBudget);
            if (tempNewUtilityValue <= this.workers[workerID].getSuccessfullyUtilityFunctionValue(i)) {
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
            if (this.workers[workerID].getToTaskDistance(i) >= lastTermTaskWinnerPackedArray[i].getNoiseEffectiveDistance()) {
                continue;
            }

            Double tempNewCostPrivacyBudget = getNewCostPrivacyBudget(workerID, i);
            Double tempNewPrivacyBudget =  this.workers[workerID].getPrivacyBudgetArray(i)[this.workers[workerID].getBudgetIndex(i)];
            Double tempNewNoiseDistance = this.workers[workerID].getNoiseDistanceArray(i)[this.workers[workerID].getBudgetIndex(i)];
            this.workers[workerID].increaseBudgetIndex(i);

            DistanceBudgetPair newEffectiveDistanceBudgetPair = getNewEffectiveNoiseDistanceAndPrivacyBudget(workerID, i, tempNewNoiseDistance, tempNewPrivacyBudget);
            double tempCompeteDistance = newEffectiveDistanceBudgetPair.distance;
            double tempEffectivePrivacyBudget = newEffectiveDistanceBudgetPair.budget;

            // Utility 函数判断
            Double tempNewUtilityValue = this.getUtilityValue(this.tasks[i].valuation, tempEffectivePrivacyBudget, this.workers[workerID].getToTaskDistance(i), tempNewCostPrivacyBudget);
            if (tempNewUtilityValue <= this.workers[workerID].getSuccessfullyUtilityFunctionValue(i)) {
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
            double tempProposingValue = super.getProposingValue(pcfValue, this.workers[workerID].getToTaskDistance(i));

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


    /**
     * 1种server的选择函数
     */
    /**
     *
     * @param taskCurrentWinnerPackedArray 记录当前每个task被竞争获胜的worker的信息
     * @param newCandidateWorkerIDList 记录每个task候选的worker id列表
     */
    public WorkerIDDistanceBudgetPair[] serverExecute(WorkerIDDistanceBudgetPair[] taskCurrentWinnerPackedArray, List<Integer>[] newCandidateWorkerIDList, Set<Integer> newTotalCompetingWorkerIDSet) {
        if (newTotalCompetingWorkerIDSet.isEmpty()) {
            return taskCurrentWinnerPackedArray;
        }
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
            this.workers[currentWinnerWorkerID].setSuccessfullyUtilityFunctionValue(i, this.workers[currentWinnerWorkerID].getCurrentUtilityFunctionValue(i));
            this.workers[currentWinnerWorkerID].currentWinningState = i; // 设置成胜利者，之后在没被竞争掉的情况下不会再竞争其他task
            newTotalCompetingWorkerIDSet.remove(currentWinnerWorkerID);    // 将竞争胜利者从竞争列表中去掉
            // 3. 设置被竞争下去的worker的当前效用函数值、胜利状态，并将其加入竞争列表
            if (!beforeWinnerWorkerID.equals(-1)) {
                this.workers[beforeWinnerWorkerID].increaseSuccessfullyUtilityFunctionValue(i, -this.tasks[i].valuation) ;
                this.workers[beforeWinnerWorkerID].currentWinningState = -1;
                // 将被竞争下去的worker加入可竞争集合，以便获取考察资格
                newTotalCompetingWorkerIDSet.add(beforeWinnerWorkerID);
            }

        }

        return taskCurrentWinnerPackedArray;

    }


    public WorkerIDDistanceBudgetPair[] complete(boolean ppcfState, Integer workerChosenState, boolean eceaState) {

        if (eceaState) {
            conflictElimination = new EnhanceConflictElimination(this.tasks.length, this.workers.length);
        } else {
            conflictElimination = new ConflictElimination(this.tasks.length, this.workers.length);
        }
        // 记录每个task的被竞争的总次数
        Integer[] competingTimes = new Integer[this.tasks.length];
        // 记录每个task被竞争过的worker id的集合
        HashSet<Integer>[] competedWorkerIDSet = new HashSet[this.tasks.length];

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
        initializeAllocationByFirstTaskAndNullAllocation(taskCurrentWinnerPackedArray, competingTimes, competedWorkerIDSet);


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
                if (this.workers[tempWorkerID].currentWinningState > -1) {
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

                TaskTargetInfo[] winnerInfoArray = null;
                if (ONLY_UTILITY.equals(workerChosenState)) {
                    winnerInfoArray = chooseArrayByUtilityFunction(tempCandidateTaskList, tempWorkerID, taskCurrentWinnerPackedArray, proposalSize, ppcfState);
                } else if (UTILITY_WITH_TASK_ENTROPY.equals(workerChosenState)) {
                    winnerInfoArray = chooseArrayByUtilityFunctionInfluencedByTaskEntropy(tempCandidateTaskList, tempWorkerID, competingTimes, taskCurrentWinnerPackedArray, competedWorkerIDSet, proposalSize, ppcfState);
                } else if (UTILITY_WITH_PROPOSING_VALUE.equals(workerChosenState)) {
                    winnerInfoArray = chooseArrayByUtilityFunctionInfluencedByProposingValue(tempCandidateTaskList, tempWorkerID, taskCurrentWinnerPackedArray, proposalSize, ppcfState);
                } else {
                    throw new RuntimeException("The worker chosen state is error!");
                }
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
                    competedWorkerIDSet[tempTaskID].add(tempWorkerID);
//                    this.workers[tempWorkerID].alreadyPublishedNoiseDistanceAndBudgetTreeSetArray[tempTaskID].add(new DistanceBudgetPair(winnerInfoArray[i].getNewNoiseDistance(), winnerInfoArray[i].getNewPrivacyBudget()));
                    this.workers[tempWorkerID].addElementToAlreadyPublishedNoiseDistanceAndBudgetTreeSet(tempTaskID, new DistanceBudgetPair(winnerInfoArray[i].getNewNoiseDistance(), winnerInfoArray[i].getNewPrivacyBudget()));
//                    this.workers[tempWorkerID].effectiveNoiseDistance[tempTaskID] = winnerInfoArray[i].getNoiseEffectiveDistance();
                    this.workers[tempWorkerID].setEffectiveNoiseDistance(tempTaskID, winnerInfoArray[i].getNoiseEffectiveDistance());
                    this.workers[tempWorkerID].setEffectivePrivacyBudget(tempTaskID, winnerInfoArray[i].getEffectivePrivacyBudget());
                    this.workers[tempWorkerID].setCurrentUtilityFunctionValue(tempTaskID, winnerInfoArray[i].getNewUtilityValue());
                    this.workers[tempWorkerID].increaseTaskCompetingTimes(tempTaskID);
                    competingTimes[tempTaskID] ++;

                }

            }
            taskCurrentWinnerPackedArray = serverExecute(taskCurrentWinnerPackedArray, newCandidateWorkerIDList, newTotalCompetingWorkerIDSet);
        }
        return taskCurrentWinnerPackedArray;
    }






}
