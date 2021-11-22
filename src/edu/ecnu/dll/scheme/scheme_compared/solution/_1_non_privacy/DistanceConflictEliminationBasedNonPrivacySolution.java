package edu.ecnu.dll.scheme.scheme_compared.solution._1_non_privacy;

import edu.ecnu.dll.basic.basic_struct.comparator.TargetInfoComparator;
import edu.ecnu.dll.basic.basic_struct.data_structure.PreferenceTable;
import edu.ecnu.dll.basic.basic_struct.pack.single_agent_info.sub_class.WorkerIDDistancePair;
import edu.ecnu.dll.basic.basic_struct.pack.single_agent_info.sub_class.sub_class.TaskTargetInfo;
import edu.ecnu.dll.run.result_tools.CommonFunction;
import edu.ecnu.dll.run.result_tools.TargetTool;
import edu.ecnu.dll.basic.basic_solution.NonPrivacySolution;
import edu.ecnu.dll.basic.basic_solution.PrivacySolution;
import edu.ecnu.dll.basic.basic_struct.agent.Task;
import edu.ecnu.dll.scheme.scheme_compared.struct.agent.worker.MultiTaskNonPrivacyWorker;
import tools.basic.BasicArray;
import tools.io.read.DoubleRead;
import tools.io.read.PointRead;
import edu.ecnu.dll.scheme.scheme_compared.struct.function.NonPrivacyDistanceConflictElimination;
import tools.struct.Point;

import java.util.*;

public class DistanceConflictEliminationBasedNonPrivacySolution extends NonPrivacySolution {

    public static final Integer ONLY_UTILITY = 0;
    public static final Integer UTILITY_WITH_TASK_ENTROPY = 1;

    public List<Integer> tempCandidateTaskList = null;

    public Task[] tasks = null;
    public MultiTaskNonPrivacyWorker[] workers = null;

    public NonPrivacyDistanceConflictElimination nonPrivacyDistanceConflictElimination = null;
    public static TargetInfoComparator targetInfoComparator = new TargetInfoComparator(TargetInfoComparator.DESCENDING);
    public static TargetInfoComparator targetInfoForUtilityValueComparator = new TargetInfoComparator(TargetInfoComparator.DESCENDING);
    public static TargetInfoComparator targetInfoForUtilityAndCompositionValueComparator = new TargetInfoComparator(TargetInfoComparator.DESCENDING);

    @Override
    public void initializeBasicInformation(List<Point> taskPositionList, Double[] taskValueArray, List<Point> workerPositionList, List<Double> workerRangeList) {
        super.initializeBasicInformation(taskPositionList, taskValueArray, workerPositionList, workerRangeList);
        this.tasks = super.tasks;
        this.workers = super.workers;
        for (int j = 0; j < this.workers.length; j++) {
            this.workers[j].taskCompetingState = new ArrayList<>();
        }
    }

    @Override
    public void initializeBasicInformation(List<Point> taskPositionList, Double taskValue, List<Point> workerPositionList, Double workerRange) {
        super.initializeBasicInformation(taskPositionList, taskValue, workerPositionList, workerRange);
        this.tasks = super.tasks;
        this.workers = super.workers;
        for (int j = 0; j < this.workers.length; j++) {
            this.workers[j].taskCompetingState = new ArrayList<>();
        }
    }

    @Override
    public void initializeAgents() {
        super.initializeAgents();
        for (int j = 0; j < this.workers.length; j++) {
            for (int i = 0; i < this.workers[j].reverseIndex.size(); i++) {
                this.workers[j].taskCompetingState.add(true);
            }
        }
    }

    @Override
    public void initializeAgentsWithLatitudeLongitude() {
        super.initializeAgentsWithLatitudeLongitude();
        for (int j = 0; j < this.workers.length; j++) {
            for (int i = 0; i < this.workers[j].reverseIndex.size(); i++) {
                this.workers[j].taskCompetingState.add(true);
            }
        }
    }

    public List<WorkerIDDistancePair>[] createTableDataOfPreferenceTableByID(WorkerIDDistancePair[] originalWinnerInfo, List<Integer>[] workerIDList) {
        List<WorkerIDDistancePair>[] table = new ArrayList[workerIDList.length];
        WorkerIDDistancePair tempPair;
        Integer tempWorkerID;
        for (int i = 0; i < workerIDList.length; i++) {
            table[i] = new ArrayList<>();
            for (Integer workerID : workerIDList[i]) {
                if (workerID.equals(NonPrivacyDistanceConflictElimination.DEFAULT_WORKER_ID_DISTANCE_PAIR.getWorkerID()) && workerIDList[i].size() > 1) {
                    continue;
                }
                tempPair = new WorkerIDDistancePair(workerID, this.workers[workerID].getToTaskDistance(i));
                table[i].add(tempPair);
            }
            if (!originalWinnerInfo[i].getWorkerID().equals(PrivacySolution.DEFAULT_WORKER_ID_DISTANCE_BUDGET_PAIR.getWorkerID()) || workerIDList[i].isEmpty()) {
                table[i].add(originalWinnerInfo[i]);
            }
            PreferenceTable.sortedPreferenceTable(table[i], this.nonPrivacyDistanceConflictElimination.workerIDDistancePairComparator);
        }
        return table;
    }


//    public void initializeBasicInformation(List<Point> taskPositionList, Double[] taskValueArray, List<Point> workerPositionList, List<Double> workerRangeList) {
//        Point taskPosition, workerPosition;
//        super.tasks = this.tasks = new BasicTask[taskPositionList.size()];
//        for (int i = 0; i < taskPositionList.size(); i++) {
//            taskPosition = taskPositionList.get(i);
//            this.tasks[i] = new BasicTask(taskPosition.getIndex());
//            this.tasks[i].valuation = taskValueArray[i];
//        }
//        super.workers = this.workers = new MultiTaskNonPrivacyWorker[workerPositionList.size()];
//        for (int j = 0; j < workers.length; j++) {
//            workerPosition = workerPositionList.get(j);
//            this.workers[j] = new MultiTaskNonPrivacyWorker(workerPosition.getIndex());
////            this.workers[j].privacyBudgetArray = privacyBudgetListArray[j].toArray(new Double[0][0]);
////            this.workers[j].setPrivacyBudgetArray(privacyBudgetListArray[j].toArray(new Double[0][0]));
//            this.workers[j].setMaxRange(workerRangeList.get(j));
//            this.workers[j].taskIndex = BasicArray.getInitializedArray(-1, this.tasks.length);
//        }
////        BasicArray.setIntegerListToContinuousNaturalNumber(this.tempCandidateTaskList, this.tasks.length - 1);
//
//    }


//    protected void initializeAllocationByFirstTaskAndNullAllocation(WorkerIDDistancePair[] taskCurrentWinnerPackedArray, Integer[] competingTimes, HashSet<Integer>[] competedWorkerIDSet) {
    protected void initializeAllocationByFirstTaskAndNullAllocation(WorkerIDDistancePair[] taskCurrentWinnerPackedArray, HashSet<Integer>[] competedWorkerIDSet) {
        // 针对每个task，初始化距离为最大距离值
        // 针对每个task，初始化总的被竞争次数为0
        // 针对每个task，初始化访问过被访问worker集合为空集合
        for (int i = 0; i < this.tasks.length; i++) {
            taskCurrentWinnerPackedArray[i] = new WorkerIDDistancePair();
            taskCurrentWinnerPackedArray[i].setWorkerID(-1);
            taskCurrentWinnerPackedArray[i].setDistance(Double.MAX_VALUE);
//            competingTimes[i] = 0;
            competedWorkerIDSet[i] = new HashSet<>();
        }
    }

    protected TaskTargetInfo[] chooseArrayByUtilityFunction(List<Integer> taskIDList, Integer workerID, WorkerIDDistancePair[] lastTermTaskWinnerPackedArray, int topK){
        TreeSet<TaskTargetInfo> candidateTaskTargetInfoSet = new TreeSet<>(targetInfoForUtilityValueComparator);
        for (Integer i : taskIDList) {
            if (lastTermTaskWinnerPackedArray[i].getWorkerID().equals(workerID)) {
                continue;
            }

            double tempCompeteDistance = this.workers[workerID].getToTaskDistance(i);

            if (tempCompeteDistance >= lastTermTaskWinnerPackedArray[i].getDistance()) {
                this.workers[workerID].setTaskCompetingState(i, false);
                continue;
            }



            // Utility 函数判断
            Double tempNewUtilityValue = this.getUtilityValue(this.tasks[i].valuation, this.workers[workerID].getToTaskDistance(i));
            if (tempNewUtilityValue <= 0) {
                continue;
            }



            TaskTargetInfo taskTargetInfo = null;

            if (candidateTaskTargetInfoSet.size() < topK) {
                candidateTaskTargetInfoSet.add(new TaskTargetInfo(i, tempCompeteDistance, null, tempNewUtilityValue, null, null, null, tempNewUtilityValue));
            } else {
                taskTargetInfo = candidateTaskTargetInfoSet.last();
                if (tempNewUtilityValue > taskTargetInfo.getTarget()) {
                    candidateTaskTargetInfoSet.remove(taskTargetInfo); //todo: 测试是否能够真的删除
                    candidateTaskTargetInfoSet.add(new TaskTargetInfo(i, tempCompeteDistance, null, tempNewUtilityValue, null, null, null, tempNewUtilityValue));
                }
            }

        }
        if (candidateTaskTargetInfoSet.isEmpty()) {
            return null;
        }
        return candidateTaskTargetInfoSet.toArray(new TaskTargetInfo[0]);
    }

//    protected TaskTargetInfo[] chooseArrayByUtilityFunctionInfluencedByTaskEntropy(List<Integer> taskIDList, Integer workerID, Integer[] totalCompetingTimeArray, WorkerIDDistancePair[] lastTermTaskWinnerPackedArray, HashSet<Integer>[] competingWorkerIDSetArray, int topK) {
//        TreeSet<TaskTargetInfo> candidateTaskTargetInfoSet = new TreeSet<>(targetInfoForUtilityAndCompositionValueComparator);
//        for (Integer i : taskIDList) {
//            if (lastTermTaskWinnerPackedArray[i].getWorkerID().equals(workerID)) {
//                continue;
//            }
//            double tempCompeteDistance = this.workers[workerID].getToTaskDistance(i);
//
//            if (tempCompeteDistance >= lastTermTaskWinnerPackedArray[i].getDistance()) {
//                this.workers[workerID].setTaskCompetingState(i, false);
//                continue;
//            }
//
//
//            // Utility 函数判断
//            Double tempNewUtilityValue = this.getUtilityValue(this.tasks[i].valuation, this.workers[workerID].getToTaskDistance(i));
////            if (tempNewUtilityValue <= 0  || tempNewUtilityValue <= this.workers[workerID].successfullyUtilityFunctionValue[i]) {
////                continue;
////            }
//            if (tempNewUtilityValue <= 0) {
//                continue;
//            }
//
//            double tempTaskEntropy = super.getTaskEntropy(i, totalCompetingTimeArray[i], competingWorkerIDSetArray[i]);
//            Double compositionQuantity = tempNewUtilityValue * tempTaskEntropy;
//
//
//            TaskTargetInfo taskTargetInfo = null;
//
//            if (candidateTaskTargetInfoSet.size() < topK) {
//                candidateTaskTargetInfoSet.add(new TaskTargetInfo(i, tempCompeteDistance, null, compositionQuantity, null, null, null, tempNewUtilityValue));
//            } else {
//                taskTargetInfo = candidateTaskTargetInfoSet.last();
//                if (tempNewUtilityValue > taskTargetInfo.getTarget()) {
//                    candidateTaskTargetInfoSet.remove(taskTargetInfo); //todo: 测试是否能够真的删除
//                    candidateTaskTargetInfoSet.add(new TaskTargetInfo(i, tempCompeteDistance, null, compositionQuantity, null, null, null, tempNewUtilityValue));
//                }
//            }
//
//        }
//        if (candidateTaskTargetInfoSet.isEmpty()) {
//            return null;
//        }
//        return candidateTaskTargetInfoSet.toArray(new TaskTargetInfo[0]);
//    }



    protected void setAbleToCompetingCandidateTaskInRange(List<Integer> tempCandidateTaskList, MultiTaskNonPrivacyWorker worker) {
        // 只遍历privacybudget列表，即限制遍历的task为range范围内
        for (int i = 0; i < worker.taskCompetingState.size(); i++) {
            if (worker.taskCompetingState.get(i).equals(true)) {
                tempCandidateTaskList.add(worker.reverseIndex.get(i));
            }
        }
    }


    public WorkerIDDistancePair[] complete() {


        this.nonPrivacyDistanceConflictElimination = new NonPrivacyDistanceConflictElimination(this.tasks.length, this.workers.length);

        // 记录每个task的被竞争的总次数
//        Integer[] competingTimes = new Integer[this.tasks.length];
        // 记录每个task被竞争过的worker id的集合
        HashSet<Integer>[] competedWorkerIDSet = new HashSet[this.tasks.length];

        // 记录worker竞争时临时效用函数值
        double[] tempUtilityArray = new double[this.workers.length];

        // 记录每个worker的申请到的task列表
        // List<Integer>[] allocatedTaskIDListArray = new ArrayList[this.workers.length];


        // 针对每个task，记录当前竞争成功的worker的信息
        WorkerIDDistancePair[] taskCurrentWinnerPackedArray = new WorkerIDDistancePair[this.tasks.length];

        // todo: 封装到了initializeAllocations

        // 针对每个task，本轮提出竞争的worker的ID（每轮需要清空）
        List<Integer>[] newCandidateWorkerIDList, oldCandidateWorkerIDList;
        newCandidateWorkerIDList = new ArrayList[this.tasks.length];
//        BasicArray.setListArrayToEmptyList(newCandidateWorkerIDList);
//        initializeAllocationByFirstTaskAndNullAllocation(taskCurrentWinnerPackedArray, competingTimes, competedWorkerIDSet);
        initializeAllocationByFirstTaskAndNullAllocation(taskCurrentWinnerPackedArray, competedWorkerIDSet);


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
                setAbleToCompetingCandidateTaskInRange(tempCandidateTaskList, this.workers[tempWorkerID]);
                if (tempCandidateTaskList.isEmpty()) {
                    continue;
                }

                // 更新下一轮可以去竞争的worker的集合
                newTotalCompetingWorkerIDSet.add(tempWorkerID);

                // 进行是否竞争判断3： 考察 utility函数、PPCF函数、PCF函数、任务熵

                TaskTargetInfo[] winnerInfoArray = null;
                winnerInfoArray = chooseArrayByUtilityFunction(tempCandidateTaskList, tempWorkerID, taskCurrentWinnerPackedArray, proposalSize);
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
                    this.workers[tempWorkerID].setCurrentUtilityFunctionValue(tempTaskID, winnerInfoArray[i].getNewUtilityValue());
//                    this.workers[tempWorkerID].increaseTaskCompetingTimes(tempTaskID);
//                    competingTimes[tempTaskID] ++;

                }

            }
            taskCurrentWinnerPackedArray = serverExecute(taskCurrentWinnerPackedArray, newCandidateWorkerIDList, newTotalCompetingWorkerIDSet);
        }
        return taskCurrentWinnerPackedArray;
    }

//    protected void chosenByServer(WorkerIDDistancePair[] taskCurrentWinnerPackedArray, List<Integer>[] newCandidateWorkerIDList) {
//        WorkerIDDistancePair[] taskBeforeWinnerPackedArray = new WorkerIDDistancePair[newCandidateWorkerIDList.length];
//        for (int i = 0; i < newCandidateWorkerIDList.length; i++) {
//            if (newCandidateWorkerIDList[i].size() == 0) {
//                continue;
//            }
//            taskBeforeWinnerPackedArray[i] = taskCurrentWinnerPackedArray[i];
//            for (Integer j : newCandidateWorkerIDList[i]) {
//                if (this.workers[j].getToTaskDistance(i) < taskCurrentWinnerPackedArray[i].getDistance()) {
//                    taskCurrentWinnerPackedArray[i].setWorkerID(j);
//                    taskCurrentWinnerPackedArray[i].setDistance(this.workers[j].getToTaskDistance(i));
//                }
//            }
//            if (!taskCurrentWinnerPackedArray[i].getWorkerID().equals(taskBeforeWinnerPackedArray[i].getWorkerID())) {
//                if (!taskBeforeWinnerPackedArray[i].getWorkerID().equals(-1)) {
//                    // todo: 封装
////                    this.workers[taskBeforeWinnerPackedArray[i].getWorkerID()].currentUtilityFunctionValue[i] -= this.tasks[i].valuation;
//                    this.workers[taskBeforeWinnerPackedArray[i].getWorkerID()].increaseSuccessfullyUtilityFunctionValue(i, -this.tasks[i].valuation);
//                    // 将被竞争下去的worker加入竞争集合，以便下轮判断是否可以竞争
//                    newCandidateWorkerIDList[i].add(taskBeforeWinnerPackedArray[i].getWorkerID());
//                }
//                this.workers[taskCurrentWinnerPackedArray[i].getWorkerID()].setCurrentUtilityFunctionValue(i, this.workers[taskCurrentWinnerPackedArray[i].getWorkerID()].getSuccessfullyUtilityFunctionValue(i));
//            }
//        }
//    }

    public WorkerIDDistancePair[] serverExecute(WorkerIDDistancePair[] taskCurrentWinnerPackedArray, List<Integer>[] newCandidateWorkerIDList, Set<Integer> newTotalCompetingWorkerIDSet) {
        if (newTotalCompetingWorkerIDSet.isEmpty()) {
            return taskCurrentWinnerPackedArray;
        }
        Integer currentWinnerWorkerID, beforeWinnerWorkerID;
        WorkerIDDistancePair[] taskBeforeWinnerPackedArray = taskCurrentWinnerPackedArray;
        // 1. 获取获胜者，并更新胜利列表
        List<WorkerIDDistancePair>[] sortedTable = createTableDataOfPreferenceTableByID(taskBeforeWinnerPackedArray, newCandidateWorkerIDList);
        taskCurrentWinnerPackedArray = this.nonPrivacyDistanceConflictElimination.assignment(sortedTable);

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

    public static void main(String[] args) {
        String basicDatasetPath = "E:\\1.学习\\4.数据集\\1.FourSquare-NYCandTokyoCheck-ins\\output\\SYN";

        String workerPointPath = basicDatasetPath + "\\worker_point.txt";
        String taskPointPath = basicDatasetPath + "\\task_point.txt";
        String taskValuePath = basicDatasetPath + "\\task_value.txt";
        String workerRangePath = basicDatasetPath + "\\worker_range.txt";
//        String workerPrivacyBudgetPath = basicDatasetPath + "\\worker_budget.txt";
//        String workerNoiseDistancePath = basicDatasetPath + "\\worker_noise_distance.txt";

        List<Point> taskPointList = PointRead.readPointWithFirstLineCount(taskPointPath);
        Double[] taskValueArray = DoubleRead.readDouble(taskValuePath);

        List<Point> workerPointList = PointRead.readPointWithFirstLineCount(workerPointPath);
        List<Double> workerRangeList = DoubleRead.readDoubleToList(workerRangePath);
//        List<Double[]>[] workerPrivacyBudgetList = TwoDimensionDoubleRead.readDouble(workerPrivacyBudgetPath);
//        List<Double[]>[] workerNoiseDistanceList = TwoDimensionDoubleRead.readDouble(workerNoiseDistancePath);


        // 初始化 task 和 workers
        Double taskValue = 20.0, workerRange = 2.0;
        DistanceConflictEliminationBasedNonPrivacySolution competitionSolution = new DistanceConflictEliminationBasedNonPrivacySolution();
        competitionSolution.initializeBasicInformation(taskPointList, taskValueArray, workerPointList, workerRangeList);
        competitionSolution.proposalSize = 20;


        //todo: 根据不同的数据集选用不同的初始化
//        multiTaskMultiCompetitionSolution.initializeAgents();
//        Integer dataTypeValue = Integer.valueOf(dataType);
        competitionSolution.initializeAgentsWithLatitudeLongitude();

        long startCompetingTime = System.currentTimeMillis();
        WorkerIDDistancePair[] winner = competitionSolution.complete();
        long endCompetingTime = System.currentTimeMillis();
        Long runningTime = TargetTool.getRunningTime(startCompetingTime, endCompetingTime);

        System.out.println(runningTime);

        CommonFunction.showResultB(winner);
    }


}
