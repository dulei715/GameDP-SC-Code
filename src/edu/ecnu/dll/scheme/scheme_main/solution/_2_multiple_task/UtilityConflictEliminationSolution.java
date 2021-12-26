package edu.ecnu.dll.scheme.scheme_main.solution._2_multiple_task;

import edu.ecnu.dll.basic.basic_solution.PrivacySolution;
import edu.ecnu.dll.basic.basic_solution.Solution;
import edu.ecnu.dll.basic.basic_struct.comparator.TargetInfoComparator;
import edu.ecnu.dll.basic.basic_struct.comparator.WorkerIDNoDistanceUtilityNoiseDistanceBudgetPairComparator;
import edu.ecnu.dll.basic.basic_struct.data_structure.PreferenceTable;
import edu.ecnu.dll.basic.basic_struct.pack.experiment_result_info.BasicExperimentResult;
import edu.ecnu.dll.basic.basic_struct.pack.experiment_result_info.ExtendedExperimentResult;
import edu.ecnu.dll.basic.basic_struct.pack.experiment_result_info.NormalExperimentResult;
import edu.ecnu.dll.basic.basic_struct.pack.single_agent_info.sub_class.DistanceBudgetPair;
import edu.ecnu.dll.basic.basic_struct.pack.single_agent_info.sub_class.WorkerIDNoDistanceUtilityNoiseDistanceBudgetPair;
import edu.ecnu.dll.basic.basic_struct.pack.single_agent_info.sub_class.sub_class.TaskTargetInfo;
import edu.ecnu.dll.run.result_tools.CommonFunction;
import edu.ecnu.dll.run.result_tools.TargetTool;
import edu.ecnu.dll.run.run_main.AbstractRun;
import edu.ecnu.dll.scheme.scheme_main.struct.function.PrivacyUtilityConflictElimination;
import tools.basic.BasicArray;
import tools.differential_privacy.compare.impl.LaplaceProbabilityDensityFunction;
import tools.io.read.DoubleRead;
import tools.io.read.PointRead;
import tools.io.read.TwoDimensionDoubleRead;
import tools.struct.Point;

import java.util.*;

public class UtilityConflictEliminationSolution extends PrivacySolution {



    public PrivacyUtilityConflictElimination conflictElimination = null;

    public static WorkerIDNoDistanceUtilityNoiseDistanceBudgetPairComparator comparator = new WorkerIDNoDistanceUtilityNoiseDistanceBudgetPairComparator();
    public static TargetInfoComparator targetInfoForUtilityValueComparator = new TargetInfoComparator(TargetInfoComparator.DESCENDING);
    public static TargetInfoComparator targetInfoForDistanceComparator = new TargetInfoComparator(TargetInfoComparator.ASCENDING);


    public void initializeAllocation(WorkerIDNoDistanceUtilityNoiseDistanceBudgetPair[] taskCurrentWinnerPackedArray, HashSet<Integer>[] competedWorkerIDSet) {
        // 针对每个task，初始化距离为最大距离值
        // 针对每个task，初始化对应距离的隐私预算为最大隐私预算
        // 针对每个task，初始化总的被竞争次数为0
        // 针对每个task，初始化访问过被访问worker集合为空集合
        for (int i = 0; i < this.tasks.length; i++) {
            taskCurrentWinnerPackedArray[i] = PrivacySolution.DEFAULT_WORKER_ID_NO_DISTANCE_DISTANCE_BUDGET_PAIR;
//            competingTimes[i] = 0;
            competedWorkerIDSet[i] = new HashSet<>();
        }
    }


    public List<WorkerIDNoDistanceUtilityNoiseDistanceBudgetPair>[] createTableDataOfPreferenceTableByID(WorkerIDNoDistanceUtilityNoiseDistanceBudgetPair[] originalWinnerInfo, List<Integer>[] workerIDList) {
        List<WorkerIDNoDistanceUtilityNoiseDistanceBudgetPair>[] table = new ArrayList[workerIDList.length];
        WorkerIDNoDistanceUtilityNoiseDistanceBudgetPair tempPair;
        Integer tempWorkerID;
        for (int i = 0; i < workerIDList.length; i++) {
            table[i] = new ArrayList<>();
            for (Integer workerID : workerIDList[i]) {
                if (workerID.equals(PrivacySolution.DEFAULT_WORKER_ID_NO_DISTANCE_DISTANCE_BUDGET_PAIR.getWorkerID()) && workerIDList[i].size() > 1) {
                    continue;
                }
                tempPair = new WorkerIDNoDistanceUtilityNoiseDistanceBudgetPair(workerID, this.workers[workerID].getEffectiveNoiseDistance(i), this.workers[workerID].getEffectivePrivacyBudget(i), getUtilityWithoutDistance(new Integer(i), workerID));
                table[i].add(tempPair);
            }
            if (!originalWinnerInfo[i].getWorkerID().equals(PrivacySolution.DEFAULT_WORKER_ID_NO_DISTANCE_DISTANCE_BUDGET_PAIR.getWorkerID()) || workerIDList[i].isEmpty()) {
                table[i].add(originalWinnerInfo[i]);
            }
            PreferenceTable.sortedPreferenceTable(table[i], this.conflictElimination.workerIDNoDistanceUtilityNoiseDistanceBudgetPairComparator);
        }
        return table;
    }


    /**
     * 5种worker的选择函数
     */
    @Deprecated
    public TaskTargetInfo[] chooseArrayByUtilityFilteredByDistanceFunction(List<Integer> taskIDList, Integer workerID, WorkerIDNoDistanceUtilityNoiseDistanceBudgetPair[] lastTermTaskWinnerPackedArray, int topK, boolean ppcfState){
        TreeSet<TaskTargetInfo> candidateTaskTargetInfoSet = new TreeSet<>(targetInfoForUtilityValueComparator);
        for (Integer i : taskIDList) {
            if (lastTermTaskWinnerPackedArray[i].getWorkerID().equals(workerID)) {
                continue;
            }
            // PPCF 判断
            if (ppcfState) {
                if (this.workers[workerID].getToTaskDistance(i) >= lastTermTaskWinnerPackedArray[i].getEffectiveNoiseDistance()) {
                    // 如果PPCF不占优，则通过设置privacybuget状态为用尽状态来防止接下来被再次选择
                    this.workers[workerID].setBudgetIndex(i, Integer.MAX_VALUE);
                    continue;
                }
            }

            // 上层的通过budget的剩余选择函数已经保证了这个不会返回空值
            Double tempNewTotalCostPrivacyBudget = getNewTotalCostPrivacyBudget(workerID, i);
            Double tempNewPrivacyBudget =  this.workers[workerID].getPrivacyBudgetArray(i)[this.workers[workerID].getBudgetIndex(i)];
            Double tempNewNoiseDistance = this.workers[workerID].getNoiseDistanceArray(i)[this.workers[workerID].getBudgetIndex(i)];
            this.workers[workerID].increaseBudgetIndex(i);

            DistanceBudgetPair newEffectiveDistanceBudgetPair = getNewEffectiveNoiseDistanceAndPrivacyBudget(workerID, i, tempNewNoiseDistance, tempNewPrivacyBudget);
            double tempCompeteDistance = newEffectiveDistanceBudgetPair.distance;
            double tempEffectivePrivacyBudget = newEffectiveDistanceBudgetPair.budget;

            // Utility 函数判断
            Double tempNewUtilityValue = this.getUtilityValue(this.tasks[i].valuation, this.workers[workerID].getToTaskDistance(i), tempNewTotalCostPrivacyBudget);
            if (tempNewUtilityValue <= 1e-6) {
                this.workers[workerID].setBudgetIndex(i, Integer.MAX_VALUE);
                continue;
            }

            // PCF 判断
            double pcfValue = LaplaceProbabilityDensityFunction.probabilityDensityFunction(tempCompeteDistance, lastTermTaskWinnerPackedArray[i].getEffectiveNoiseDistance(), tempEffectivePrivacyBudget, lastTermTaskWinnerPackedArray[i].getEffectivePrivacyBudget());
            if (pcfValue <= 0.5) {
                continue;
            }


            TaskTargetInfo taskTargetInfo = null;

            if (candidateTaskTargetInfoSet.size() < topK) {
                candidateTaskTargetInfoSet.add(new TaskTargetInfo(i, tempCompeteDistance, tempEffectivePrivacyBudget, tempNewUtilityValue, tempNewTotalCostPrivacyBudget, tempNewPrivacyBudget, tempNewNoiseDistance, tempNewUtilityValue));
            } else {
                taskTargetInfo = candidateTaskTargetInfoSet.last();
                if (tempNewUtilityValue > taskTargetInfo.getTarget()) {
                    candidateTaskTargetInfoSet.remove(taskTargetInfo); //todo: 测试是否能够真的删除
                    candidateTaskTargetInfoSet.add(new TaskTargetInfo(i, tempCompeteDistance, tempEffectivePrivacyBudget, tempNewUtilityValue, tempNewTotalCostPrivacyBudget, tempNewPrivacyBudget, tempNewNoiseDistance, tempNewUtilityValue));
                }
            }

        }
        if (candidateTaskTargetInfoSet.isEmpty()) {
            return null;
        }
        return candidateTaskTargetInfoSet.toArray(new TaskTargetInfo[0]);
    }
    public TaskTargetInfo[] chooseArrayByUtilityFilteredByUtilityFunction(List<Integer> taskIDList, Integer workerID, WorkerIDNoDistanceUtilityNoiseDistanceBudgetPair[] lastTermTaskWinnerPackedArray, int topK, boolean ppcfState){
//        TreeSet<TaskTargetInfo> candidateTaskTargetInfoSet = new TreeSet<>(targetInfoForDistanceComparator);
        TreeSet<TaskTargetInfo> candidateTaskTargetInfoSet = new TreeSet<>(targetInfoForUtilityValueComparator);
        for (Integer i : taskIDList) {
            if (lastTermTaskWinnerPackedArray[i].getWorkerID().equals(workerID)) {
                continue;
            }

            Double tempNewTotalCostPrivacyBudget = getNewTotalCostPrivacyBudget(workerID, i);
            Double tempNewPrivacyBudget =  this.workers[workerID].getPrivacyBudgetArray(i)[this.workers[workerID].getBudgetIndex(i)];
            Double tempNewNoiseDistance = this.workers[workerID].getNoiseDistanceArray(i)[this.workers[workerID].getBudgetIndex(i)];

            DistanceBudgetPair newEffectiveDistanceBudgetPair = getNewEffectiveNoiseDistanceAndPrivacyBudget(workerID, i, tempNewNoiseDistance, tempNewPrivacyBudget);
            // Utility 函数判断
            Double tempNewUtilityValue = this.getUtilityValue(this.tasks[i].valuation, this.workers[workerID].getToTaskDistance(i), tempNewTotalCostPrivacyBudget);


            // PPCF 判断utility是否占优
            if (ppcfState) {
                if (tempNewUtilityValue <= lastTermTaskWinnerPackedArray[i].getEffectiveUtility()) {
//                    if (tempNewUtilityValue > 0 ){
//                        System.out.println("haha");
//                    }
                    // 如果PPCF不占优，则通过设置privacybuget状态为用尽状态来防止接下来被再次选择
                    this.workers[workerID].setBudgetIndex(i, Integer.MAX_VALUE);
                    continue;
                }
            }


            double tempCompeteDistance = newEffectiveDistanceBudgetPair.distance;
            double tempEffectivePrivacyBudget = newEffectiveDistanceBudgetPair.budget;
            double tempNonDistanceUtility = getNewNonDistanceUtility(i, workerID);

            this.workers[workerID].increaseBudgetIndex(i);

            if (tempNewUtilityValue <= 1e-6) {
//                this.workers[workerID].setBudgetIndex(i, Integer.MAX_VALUE);
                continue;
            }



            // PCF 判断utility是否占优
//            double pcfValue = LaplaceProbabilityDensityFunction.probabilityDensityFunction(tempCompeteDistance, lastTermTaskWinnerPackedArray[i].getEffectiveNoiseDistance(), tempEffectivePrivacyBudget, lastTermTaskWinnerPackedArray[i].getEffectivePrivacyBudget());
            double pcfValue = LaplaceProbabilityDensityFunction.probabilityDensityFunction(tempCompeteDistance, lastTermTaskWinnerPackedArray[i].getEffectiveNoiseDistance() + transformValueToDistance(tempNonDistanceUtility) - transformValueToDistance(lastTermTaskWinnerPackedArray[i].getNoDistanceUtility()), tempEffectivePrivacyBudget, lastTermTaskWinnerPackedArray[i].getEffectivePrivacyBudget());
            if (pcfValue <= 0.5) {
                continue;
            }

//            if(ppcfState==false && tempNewUtilityValue <= lastTermTaskWinnerPackedArray[i].getEffectiveUtility() && pcfValue > 0.5){
//                System.out.println("haha");
//            }


//            double tempRealDistance = this.workers[workerID].getToTaskDistance(i);

            TaskTargetInfo taskTargetInfo = null;
            // todo: 设计成取前k个最小距离的task
            // todo: 设计成取前k个最大utility

            if (candidateTaskTargetInfoSet.size() < topK) {
//                candidateTaskTargetInfoSet.add(new TaskTargetInfo(i, tempCompeteDistance, tempEffectivePrivacyBudget, tempRealDistance, tempNewTotalCostPrivacyBudget, tempNewPrivacyBudget, tempNewNoiseDistance, tempNewUtilityValue));
                candidateTaskTargetInfoSet.add(new TaskTargetInfo(i, tempCompeteDistance, tempEffectivePrivacyBudget, tempNewUtilityValue, tempNewTotalCostPrivacyBudget, tempNewPrivacyBudget, tempNewNoiseDistance, tempNewUtilityValue));
            } else {
                taskTargetInfo = candidateTaskTargetInfoSet.last();
//                if (tempRealDistance < taskTargetInfo.getTarget()) {
                if (tempNewUtilityValue > taskTargetInfo.getTarget()) {
                    candidateTaskTargetInfoSet.remove(taskTargetInfo); //todo: 测试是否能够真的删除
//                    candidateTaskTargetInfoSet.add(new TaskTargetInfo(i, tempCompeteDistance, tempEffectivePrivacyBudget, tempRealDistance, tempNewTotalCostPrivacyBudget, tempNewPrivacyBudget, tempNewNoiseDistance, tempNewUtilityValue));
                    candidateTaskTargetInfoSet.add(new TaskTargetInfo(i, tempCompeteDistance, tempEffectivePrivacyBudget, tempNewUtilityValue, tempNewTotalCostPrivacyBudget, tempNewPrivacyBudget, tempNewNoiseDistance, tempNewUtilityValue));
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
    public WorkerIDNoDistanceUtilityNoiseDistanceBudgetPair[] serverExecute(WorkerIDNoDistanceUtilityNoiseDistanceBudgetPair[] taskCurrentWinnerPackedArray, List<Integer>[] newCandidateWorkerIDList, Set<Integer> newTotalCompetingWorkerIDSet) {
        if (newTotalCompetingWorkerIDSet.isEmpty()) {
            return taskCurrentWinnerPackedArray;
        }
        Integer currentWinnerWorkerID, beforeWinnerWorkerID;
        WorkerIDNoDistanceUtilityNoiseDistanceBudgetPair[] taskBeforeWinnerPackedArray = taskCurrentWinnerPackedArray;
        // 1. 获取获胜者，并更新胜利列表
        List<WorkerIDNoDistanceUtilityNoiseDistanceBudgetPair>[] sortedTable = createTableDataOfPreferenceTableByID(taskBeforeWinnerPackedArray, newCandidateWorkerIDList);
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
                double decreaseUtilityPart = PrivacySolution.getValueAndDistancePartOfUtilityValue(this.tasks[i].valuation, this.workers[beforeWinnerWorkerID].getToTaskDistance(i));
                this.workers[beforeWinnerWorkerID].increaseSuccessfullyUtilityFunctionValue(i, -decreaseUtilityPart) ;
                this.workers[beforeWinnerWorkerID].currentWinningState = -1;
                // 将被竞争下去的worker加入可竞争集合，以便获取考察资格
                newTotalCompetingWorkerIDSet.add(beforeWinnerWorkerID);
            }

        }

        return taskCurrentWinnerPackedArray;

    }


    /**
     * 竞争
     * @param ppcfState
     * @return
     */
    public WorkerIDNoDistanceUtilityNoiseDistanceBudgetPair[] competeBefore(boolean ppcfState) {

        conflictElimination = new PrivacyUtilityConflictElimination(this.tasks.length, this.workers.length, comparator);
        // 记录每个task的被竞争的总次数
//        Integer[] competingTimes = new Integer[this.tasks.length];
        // 记录每个task被竞争过的worker id的集合
        HashSet<Integer>[] competedWorkerIDSet = new HashSet[this.tasks.length];

        // 记录worker竞争时临时效用函数值
        double[] tempUtilityArray = new double[this.workers.length];

        // 记录每个worker的申请到的task列表
        // List<Integer>[] allocatedTaskIDListArray = new ArrayList[this.workers.length];


        // 针对每个task，记录当前竞争成功的worker的信息
        WorkerIDNoDistanceUtilityNoiseDistanceBudgetPair[] taskCurrentWinnerPackedArray = new WorkerIDNoDistanceUtilityNoiseDistanceBudgetPair[this.tasks.length];

        // todo: 封装到了initializeAllocations

        // 针对每个task，本轮提出竞争的worker的ID（每轮需要清空）
        List<Integer>[] newCandidateWorkerIDList, oldCandidateWorkerIDList;
        newCandidateWorkerIDList = new ArrayList[this.tasks.length];
//        BasicArray.setListArrayToEmptyList(newCandidateWorkerIDList);
        initializeAllocation(taskCurrentWinnerPackedArray, competedWorkerIDSet);


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
                winnerInfoArray = chooseArrayByUtilityFilteredByDistanceFunction(tempCandidateTaskList, tempWorkerID, taskCurrentWinnerPackedArray, proposalSize, ppcfState);
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
                    this.workers[tempWorkerID].setEffectiveNoiseDistance(tempTaskID, winnerInfoArray[i].getEffectiveNoiseDistance());
                    this.workers[tempWorkerID].setEffectivePrivacyBudget(tempTaskID, winnerInfoArray[i].getEffectivePrivacyBudget());
                    this.workers[tempWorkerID].setCurrentUtilityFunctionValue(tempTaskID, winnerInfoArray[i].getNewUtilityValue());
                    this.workers[tempWorkerID].setTotalPrivacyBudgetCost(tempTaskID, winnerInfoArray[i].getNewTotalCostPrivacyBudget());
//                    this.workers[tempWorkerID].increaseTaskCompetingTimes(tempTaskID);
//                    competingTimes[tempTaskID] ++;

                }

            }
            taskCurrentWinnerPackedArray = serverExecute(taskCurrentWinnerPackedArray, newCandidateWorkerIDList, newTotalCompetingWorkerIDSet);
        }
        return taskCurrentWinnerPackedArray;
    }

    public WorkerIDNoDistanceUtilityNoiseDistanceBudgetPair[] compete(boolean ppcfState) {

        conflictElimination = new PrivacyUtilityConflictElimination(this.tasks.length, this.workers.length, comparator);
        // 记录每个task的被竞争的总次数
//        Integer[] competingTimes = new Integer[this.tasks.length];
        // 记录每个task被竞争过的worker id的集合
        HashSet<Integer>[] competedWorkerIDSet = new HashSet[this.tasks.length];

        // 记录worker竞争时临时效用函数值
//        double[] tempUtilityArray = new double[this.workers.length];

        // 记录每个worker的申请到的task列表
        // List<Integer>[] allocatedTaskIDListArray = new ArrayList[this.workers.length];


        // 针对每个task，记录当前竞争成功的worker的信息
        WorkerIDNoDistanceUtilityNoiseDistanceBudgetPair[] taskCurrentWinnerPackedArray = new WorkerIDNoDistanceUtilityNoiseDistanceBudgetPair[this.tasks.length];

        // todo: 封装到了initializeAllocations

        // 针对每个task，本轮提出竞争的worker的ID（每轮需要清空）
        List<Integer>[] newCandidateWorkerIDList, oldCandidateWorkerIDList;
        newCandidateWorkerIDList = new ArrayList[this.tasks.length];
//        BasicArray.setListArrayToEmptyList(newCandidateWorkerIDList);
        initializeAllocation(taskCurrentWinnerPackedArray, competedWorkerIDSet);


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
                winnerInfoArray = chooseArrayByUtilityFilteredByUtilityFunction(tempCandidateTaskList, tempWorkerID, taskCurrentWinnerPackedArray, proposalSize, ppcfState);
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
                    this.workers[tempWorkerID].setEffectiveNoiseDistance(tempTaskID, winnerInfoArray[i].getEffectiveNoiseDistance());
                    this.workers[tempWorkerID].setEffectivePrivacyBudget(tempTaskID, winnerInfoArray[i].getEffectivePrivacyBudget());
                    this.workers[tempWorkerID].setCurrentUtilityFunctionValue(tempTaskID, winnerInfoArray[i].getNewUtilityValue());
                    this.workers[tempWorkerID].setTotalPrivacyBudgetCost(tempTaskID, winnerInfoArray[i].getNewTotalCostPrivacyBudget());
//                    this.workers[tempWorkerID].increaseTaskCompetingTimes(tempTaskID);
//                    competingTimes[tempTaskID] ++;

                }

            }
            taskCurrentWinnerPackedArray = serverExecute(taskCurrentWinnerPackedArray, newCandidateWorkerIDList, newTotalCompetingWorkerIDSet);
        }
        return taskCurrentWinnerPackedArray;
    }

    public static void main(String[] args) {
//        String basicDatasetPath = "E:\\1.学习\\4.数据集\\1.FourSquare-NYCandTokyoCheck-ins\\output\\SYN";
//        String basicDatasetPath = "E:\\1.学习\\4.数据集\\1.FourSquare-NYCandTokyoCheck-ins\\output\\TKY";
//        String basicDatasetPath = "E:\\1.学习\\4.数据集\\1.FourSquare-NYCandTokyoCheck-ins\\output\\test\\test1";
        String basicDatasetPath = "E:\\1.学习\\4.数据集\\dataset\\original\\chengdu_default";
//        double[] fixedTaskValueAndWorkerRange = new double[]{20.0, 2};
        Solution.alpha = 0.001;
        Solution.beta = 1;

        double[] fixedTaskValueAndWorkerRange = new double[]{40.0, 4000};
//        Integer dataType = AbstractRun.LONGITUDE_LATITUDE;
        Integer dataType = AbstractRun.COORDINATE;


        String workerPointPath = basicDatasetPath + "\\worker_point.txt";
        String taskPointPath = basicDatasetPath + "\\task_point.txt";
        String taskValuePath = basicDatasetPath + "\\task_value.txt";
        String workerRangePath = basicDatasetPath + "\\worker_range.txt";
        String workerPrivacyBudgetPath = basicDatasetPath + "\\worker_budget.txt";
        String workerNoiseDistancePath = basicDatasetPath + "\\worker_noise_distance.txt";

        List<Point> taskPointList = PointRead.readPointWithFirstLineCount(taskPointPath);
        List<Double> taskValueList = DoubleRead.readDoubleWithFirstSizeLineToList(taskValuePath);

        List<Point> workerPointList = PointRead.readPointWithFirstLineCount(workerPointPath);
        List<Double> workerRangeList = DoubleRead.readDoubleWithFirstSizeLineToList(workerRangePath);
        List<Double[]>[] workerPrivacyBudgetList = TwoDimensionDoubleRead.readDouble(workerPrivacyBudgetPath, 1);
        List<Double[]>[] workerNoiseDistanceList = TwoDimensionDoubleRead.readDouble(workerNoiseDistancePath, 1);


        // 初始化 task 和 workers
        UtilityConflictEliminationSolution utilityConflictEliminationSolution = new UtilityConflictEliminationSolution();
        utilityConflictEliminationSolution.proposalSize = Integer.MAX_VALUE;

        Double taskValue = null, workerRange = null;

        if (fixedTaskValueAndWorkerRange == null) {
            utilityConflictEliminationSolution.initializeBasicInformation(taskPointList, taskValueList, workerPointList, workerRangeList);
        } else {
            taskValue = fixedTaskValueAndWorkerRange[0];
            workerRange = fixedTaskValueAndWorkerRange[1];
            utilityConflictEliminationSolution.initializeBasicInformation(taskPointList, taskValue, workerPointList, workerRange);
        }

        //todo: 根据不同的数据集选用不同的初始化
//        multiTaskMultiCompetitionSolution.initializeAgents();
        Integer dataTypeValue = Integer.valueOf(dataType);
        if (AbstractRun.COORDINATE.equals(dataTypeValue)) {
            utilityConflictEliminationSolution.initializeAgents(workerPrivacyBudgetList, workerNoiseDistanceList);
        } else if (AbstractRun.LONGITUDE_LATITUDE.equals(dataTypeValue)) {
            utilityConflictEliminationSolution.initializeAgentsWithLatitudeLongitude(workerPrivacyBudgetList, workerNoiseDistanceList);
        } else {
            throw new RuntimeException("The type input is not right!");
        }


        // 执行竞争过程
        long startCompetingTime = System.currentTimeMillis();
        WorkerIDNoDistanceUtilityNoiseDistanceBudgetPair[] winner = utilityConflictEliminationSolution.compete(false);
//        WorkerIDNoDistanceUtilityNoiseDistanceBudgetPair[] winner = utilityConflictEliminationSolution.competeBefore(false);
        long endCompetingTime = System.currentTimeMillis();
        Long runningTime = TargetTool.getRunningTime(startCompetingTime, endCompetingTime);

//        showResultA(winner);
        BasicExperimentResult basicExperimentResult = CommonFunction.getResultData(winner, utilityConflictEliminationSolution.workers);

        CommonFunction.showResultB(winner);

        NormalExperimentResult normalExperimentResult = new NormalExperimentResult(basicExperimentResult, runningTime);
        ExtendedExperimentResult extendedExperimentResult = new ExtendedExperimentResult(normalExperimentResult, null, taskValue, workerRange);

//        System.out.println(normalExperimentResult);
        System.out.println(extendedExperimentResult);
    }





}
