package edu.ecnu.dll.scheme.run.main_run;

import edu.ecnu.dll.basic_struct.comparator.TaskWorkerIDComparator;
import edu.ecnu.dll.basic_struct.pack.TaskWorkerIDPair;
import edu.ecnu.dll.basic_struct.pack.single_agent_info.sub_class.WorkerIDDistanceBudgetPair;
import edu.ecnu.dll.scheme.run.target_tools.TargetTool;
import edu.ecnu.dll.scheme.solution._3_multiple_task.MultiTaskMultiCompetitionSolution;
import tools.io.print.MyPrint;
import tools.io.read.DoubleRead;
import tools.io.read.PointRead;
import tools.io.read.TwoDimensionDoubleRead;
import tools.struct.Point;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MultiTaskMultiCompleteRun {
    public static void main(String[] args) {
        // 从数据库读数据
//        String basicPath = System.getProperty("user.dir") + "\\dataset\\test_dataset\\_2_multiple_task_dataset\\";
//        String taskPointPath = basicPath + "test1\\task_point.txt";
//        String taskValuePath = basicPath + "test1\\task_value.txt";
//        String workerPointPath = basicPath + "test1\\worker_point.txt";
//        String workerPrivacyBudgetPath = basicPath + "test1\\worker_privacy_budget.txt";
        String basicDatasetPath = "E:\\1.学习\\4.数据集\\dataset\\original\\chengdu\\";
        String workerPointPath = basicDatasetPath + "worker_point.txt";
        String taskPointPath = basicDatasetPath + "task_point.txt";
        String taskValuePath = basicDatasetPath + "task_value.txt";
        String workerPrivacyBudgetPath = basicDatasetPath + "worker_privacy_budget.txt";

        List<Point> taskPointList = PointRead.readPointWithFirstLineCount(taskPointPath);
        Double[] taskValueArray = DoubleRead.readDouble(taskValuePath);

        List<Point> workerPointList = PointRead.readPointWithFirstLineCount(workerPointPath);
        List<Double[]>[] workerPrivacyBudgetList = TwoDimensionDoubleRead.readDouble(workerPrivacyBudgetPath);


        // 初始化 task 和 workers
        MultiTaskMultiCompetitionSolution multiTaskMultiCompetitionSolution = new MultiTaskMultiCompetitionSolution();
        multiTaskMultiCompetitionSolution.initializeBasicInformation(taskPointList, taskValueArray, workerPointList, workerPrivacyBudgetList);
        multiTaskMultiCompetitionSolution.initializeAgents();


        // 执行竞争过程
        long startCompetingTime = System.currentTimeMillis();
        WorkerIDDistanceBudgetPair[] winner = multiTaskMultiCompetitionSolution.complete();
        long endCompetingTime = System.currentTimeMillis();
        Long runningTime = TargetTool.getRunningTime(startCompetingTime, endCompetingTime);

//        showResultA(winner);
        showResultB(winner);

        System.out.println("Running time is: " + runningTime);
    }

    private static void showResultA(WorkerIDDistanceBudgetPair[] winnerTaskWorkerPackedArray) {
        int serveredTaskSize = 0;
        for (int i = 0; i < winnerTaskWorkerPackedArray.length; i++) {
//        for (int i = 0; i < 10; i++) {
            if (winnerTaskWorkerPackedArray[i].getWorkerID().equals(-1)) {
                continue;
            }
            ++ serveredTaskSize;
            System.out.println("Task ID: " + i + "; Winner worker ID: " + winnerTaskWorkerPackedArray[i].getWorkerID() + "; Effective noise distance: "
                    + winnerTaskWorkerPackedArray[i].getNoiseEffectiveDistance() + "; Effective privacy budget: "
                    + winnerTaskWorkerPackedArray[i].getEffectivePrivacyBudget());
        }

        System.out.println(serveredTaskSize);
    }

    private static void showResultB(WorkerIDDistanceBudgetPair[] winnerTaskWorkerPackedArray) {
        int serveredTaskSize = 0;
        TaskWorkerIDPair[] taskWorkerIDPairArray;
        List<TaskWorkerIDPair> taskWorkerIDPairList = new ArrayList<>();

        List<Integer> nonCompeteTaskIDList = new ArrayList<>();
        List<Integer> competeFailureTaskIDList = new ArrayList<>();

        for (int i = 0; i < winnerTaskWorkerPackedArray.length; i++) {

            if (winnerTaskWorkerPackedArray[i] == null) {
                competeFailureTaskIDList.add(i);
                continue;
            }
            if (winnerTaskWorkerPackedArray[i].getWorkerID().equals(-1)) {
                nonCompeteTaskIDList.add(i);
                continue;
            }
            taskWorkerIDPairList.add(new TaskWorkerIDPair(i, winnerTaskWorkerPackedArray[i].getWorkerID()));
        }
        serveredTaskSize = taskWorkerIDPairList.size();
        taskWorkerIDPairArray = taskWorkerIDPairList.toArray(new TaskWorkerIDPair[0]);
        Arrays.sort(taskWorkerIDPairArray, new TaskWorkerIDComparator(TaskWorkerIDComparator.WORKER_FIRST, TaskWorkerIDComparator.TASK_ASCENDING, TaskWorkerIDComparator.WORKER_ASCENDING));
        int tempTaskID, tempWorkerID;
        for (int i = 0; i < serveredTaskSize; i++) {
            tempTaskID = taskWorkerIDPairArray[i].getTaskID();
            tempWorkerID = taskWorkerIDPairArray[i].getWorkerID();
            System.out.println("Winner worker ID: " + tempWorkerID + "; Task ID: " + tempTaskID + "; Effective noise distance: "
                    + winnerTaskWorkerPackedArray[tempTaskID].getNoiseEffectiveDistance() + "; Effective privacy budget: "
                    + winnerTaskWorkerPackedArray[tempTaskID].getEffectivePrivacyBudget());
        }

        System.out.println(serveredTaskSize);
        System.out.println("Non competing tasks are:");
        MyPrint.showList(nonCompeteTaskIDList);
        System.out.println("Failure competing tasks are:");
        MyPrint.showList(competeFailureTaskIDList);

    }
}
