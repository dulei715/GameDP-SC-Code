package edu.ecnu.dll.scheme.solution._3_multiple_task;

import edu.ecnu.dll.basic_struct.comparator.TargetInfoTargetFirstComparator;
import edu.ecnu.dll.basic_struct.pack.DistanceBudgetPair;
import edu.ecnu.dll.basic_struct.pack.TargetInfo;
import edu.ecnu.dll.basic_struct.pack.Winner;
import edu.ecnu.dll.basic_struct.pack.WorkerIDDistanceBudgetPair;
import edu.ecnu.dll.scheme.solution._3_multiple_task.struct.EnhanceConflictElimination;
import edu.ecnu.dll.scheme_compared.solution.ConflictElimination;
import tools.basic.BasicArray;
import tools.differential_privacy.compare.impl.LaplaceProbabilityDensityFunction;
import tools.differential_privacy.noise.LaplaceUtils;

import java.util.*;

public class MultiTaskMultiCompetitionSolution extends MultiTaskSingleCompetitionSolution {


    public static final int proposalSize = 5;
    public static TargetInfoTargetFirstComparator targetInfoTargetFirstComparator = new TargetInfoTargetFirstComparator();

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

    public Winner complete() {

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
        Integer[] taskCurrentWinnerIDArray = new Integer[this.tasks.length];
        BasicArray.setIntArrayTo(taskCurrentWinnerIDArray, -1);
        // 针对每个task，记录当前竞争成功的worker的budget以及扰动距离
        Double[][] taskCurrentWinnerInfoArray = new Double[this.tasks.length][2];

        // todo: 封装到了initializeAllocations

        // 针对每个task，本轮提出竞争的worker的ID（每轮需要清空）
        List<Integer>[] newCandidateWorkerIDList, oldCandidateWorkerIDList;
        newCandidateWorkerIDList = new ArrayList[this.tasks.length];
//        BasicArray.setListArrayToEmptyList(newCandidateWorkerIDList);
        initializeAllocationByFirstTaskAndNullAllocation(newCandidateWorkerIDList, taskCurrentWinnerInfoArray, competingTimes, completedWorkerIDSet);


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
//                winnerInfoArray = chooseArrayByTaskEntropy(tempCandidateTaskList, tempWorkerID, competingTimes, taskCurrentWinnerIDArray, taskCurrentWinnerInfoArray, completedWorkerIDSet, proposalSize);
                winnerInfoArray = chooseArrayByProposingValue(tempCandidateTaskList, tempWorkerID, taskCurrentWinnerIDArray, taskCurrentWinnerInfoArray);
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
            chosenByServer(taskCurrentWinnerIDArray, taskCurrentWinnerInfoArray, newCandidateWorkerIDList);
        }
        return new Winner(taskCurrentWinnerIDArray, taskCurrentWinnerInfoArray);
    }

    private TargetInfo[] chooseArrayByProposingValue(List<Integer> tempCandidateTaskList, Integer tempWorkerID, Integer[] taskCurrentWinnerIDArray, Double[][] taskCurrentWinnerInfoArray) {
        
    }

    protected TargetInfo[] chooseArrayByTaskEntropy(List<Integer> taskIDList, Integer workerID, Integer[] totalCompetingTimesList, Integer[] lastTermTaskWinnerIDArray, Double[][] lastTermTaskWinnerInfoArray, HashSet<Integer>[] competingWorkerIDSetArray, int topK) {
        // 遍历taskIDList中的所有task，找出能使当前worker的utility增加最大的task，返回（该task的ID, 申请即将泄露的平均distance[此处可返回扰动], 申请泄露的budget总和[此处可返回当前budget]）
        Integer taskID = null;
        Double noiseEfficientDistance = null;
        Double effectivePrivacyBudget = null;
//        Double efficientPrivacyBudget = null;
        TreeSet<TargetInfo> candidateTargetInfoSet = new TreeSet<>(targetInfoTargetFirstComparator);

        Double newCostPrivacyBudget = null, newPrivacyBudget = null, newNoiseDistance = null, newUtilityValue = null;
        for (Integer i : taskIDList) {
            if (lastTermTaskWinnerIDArray[i].equals(workerID)) {
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
            Double tempNewUtilityValue = this.getUtilityValue(this.tasks[i].valuation, tempEffectivePrivacyBudget, this.workers[workerID].toTaskDistance[i], tempNewCostPrivacyBudget);
            if (tempNewUtilityValue <= this.workers[workerID].completeUtilityFunctionValue[i]) {
                continue;
            }

            // PCF 判断
            double pcfValue = LaplaceProbabilityDensityFunction.probabilityDensityFunction(tempCompeteDistance, lastTermTaskWinnerInfoArray[i][DISTANCE_TAG], tempEffectivePrivacyBudget, lastTermTaskWinnerInfoArray[i][BUDGET_TAG]);
            if (pcfValue <= 0.5) {
                continue;
            }

            // 根据 taskEntropy 挑选
            double tempTaskEntropy = getTaskEntropy(i, totalCompetingTimesList[i], competingWorkerIDSetArray[i]);
            TargetInfo targetInfo;

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
        if (taskID == null) {
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
     * @param taskCurrentWinnerIDArray  记录当前每个task被竞争获胜的worker的ID
     * @param taskCurrentWinnerInfoArray 记录当前每个task被竞争获胜的worker的信息
     * @param newCandidateWorkerIDList 记录每个task候选的worker id列表
     */
    @Override
    public void chosenByServer(Integer[] taskCurrentWinnerIDArray, Double[][] taskCurrentWinnerInfoArray, List<Integer>[] newCandidateWorkerIDList) {
        double competeTemp;
        Integer[] taskBeforeWinnerIDArray = new Integer[newCandidateWorkerIDList.length];
        // 1. 设置task的访问量
        // 2. 获取获胜者
        // 3. 设置获胜的worker的有效噪声距离和有效隐私预算以及当前效用函数值
        // 4. 设置被竞争下去的worker的当前效用函数值
        // 5. 更新竞争列表
        List<WorkerIDDistanceBudgetPair>[] unSortedTable = createTableDataOfPreferenceTableByID(newCandidateWorkerIDList);
        Integer[] assignmentResult = this.conflictElimination.assignment(unSortedTable);
    }


}
