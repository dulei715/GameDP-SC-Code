package edu.ecnu.dll.scheme.run.main_run;

import edu.ecnu.dll.basic_struct.comparator.TaskWorkerIDComparator;
import edu.ecnu.dll.basic_struct.pack.TaskWorkerIDPair;
import edu.ecnu.dll.basic_struct.pack.Winner;
import edu.ecnu.dll.basic_struct.pack.WorkerIDDistanceBudgetPair;
import edu.ecnu.dll.scheme.solution._2_single_task.SingleTaskSolution;
import edu.ecnu.dll.scheme.solution._3_multiple_task.MultiTaskSingleCompetitionSolution;
import tools.io.print.MyPrint;
import tools.io.read.DoubleRead;
import tools.io.read.PointRead;
import tools.io.read.TwoDimensionDoubleRead;
import tools.struct.Point;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MultiTaskSingleCompleteRun {
    public static void main(String[] args) {
        // 从数据库读数据
        String basicPath = System.getProperty("user.dir") + "\\dataset\\test_dataset\\_2_multiple_task_dataset\\";
        String testPath = "test2\\";
        String taskPointPath = basicPath + testPath + "task_point.txt";
        String taskValuePath = basicPath + testPath + "task_value.txt";
        String workerPointPath = basicPath + testPath + "worker_point.txt";
        String workerPrivacyBudgetPath = basicPath + testPath + "worker_privacy_budget.txt";

        List<Point> taskPointList = PointRead.readPoint(taskPointPath);
        Double[] taskValueArray = DoubleRead.readDouble(taskValuePath);

        List<Point> workerPointList = PointRead.readPoint(workerPointPath);
        List<Double[]>[] workerPrivacyBudgetList = TwoDimensionDoubleRead.readDouble(workerPrivacyBudgetPath);

//        MyPrint.showList(taskPointList);
//        System.out.println("******************************************************************************");
//        MyPrint.showDoubleArray(taskValueArray);
//        System.out.println("******************************************************************************");
//        MyPrint.showList(workerPointList);
//        System.out.println("******************************************************************************");
//        MyPrint.showListDoubleArray(workerPrivacyBudgetList);
//        System.out.println("******************************************************************************");

        // 初始化 task 和 workers
        MultiTaskSingleCompetitionSolution multiTaskSingleCompetitionSolution = new MultiTaskSingleCompetitionSolution();
        multiTaskSingleCompetitionSolution.initializeBasicInformation(taskPointList, taskValueArray, workerPointList, workerPrivacyBudgetList);
        multiTaskSingleCompetitionSolution.initializeAgents();


        // 执行竞争过程
        WorkerIDDistanceBudgetPair[] winner = multiTaskSingleCompetitionSolution.complete();

//        showResultA(winner);
        showResultB(winner);

    }

    private static void showResultA(WorkerIDDistanceBudgetPair[] winnerTaskWorkerPackedArray) {
        int serveredTaskSize = 0;
        for (int i = 0; i < winnerTaskWorkerPackedArray.length; i++) {
//        for (int i = 0; i < 10; i++) {
            if (winnerTaskWorkerPackedArray[i].workerID == -1) {
                continue;
            }
            ++ serveredTaskSize;
            System.out.println("Task ID: " + i + "; Winner worker ID: " + winnerTaskWorkerPackedArray[i].workerID + "; Effective noise distance: "
                    + winnerTaskWorkerPackedArray[i].noiseEffectiveDistance + "; Effective privacy budget: "
                    + winnerTaskWorkerPackedArray[i].effectivePrivacyBudget);
        }

        System.out.println(serveredTaskSize);
    }

    private static void showResultB(WorkerIDDistanceBudgetPair[] winnerTaskWorkerPackedArray) {
        int serveredTaskSize = 0;
        TaskWorkerIDPair[] taskWorkerIDPairArray;
        List<TaskWorkerIDPair> taskWorkerIDPairList = new ArrayList<>();

        for (int i = 0; i < winnerTaskWorkerPackedArray.length; i++) {
            if (winnerTaskWorkerPackedArray[i].workerID == -1) {
                continue;
            }
            taskWorkerIDPairList.add(new TaskWorkerIDPair(i, winnerTaskWorkerPackedArray[i].workerID));
        }
        serveredTaskSize = taskWorkerIDPairList.size();
        taskWorkerIDPairArray = taskWorkerIDPairList.toArray(new TaskWorkerIDPair[0]);
        Arrays.sort(taskWorkerIDPairArray, new TaskWorkerIDComparator(TaskWorkerIDComparator.WORKER_FIRST, TaskWorkerIDComparator.TASK_ASCENDING, TaskWorkerIDComparator.WORKER_ASCENDING));
        int tempTaskID, tempWorkerID;
        for (int i = 0; i < serveredTaskSize; i++) {
            tempTaskID = taskWorkerIDPairArray[i].taskID;
            tempWorkerID = taskWorkerIDPairArray[i].workerID;
            System.out.println("Winner worker ID: " + tempWorkerID + "; Task ID: " + tempTaskID + "; Effective noise distance: "
                    + winnerTaskWorkerPackedArray[tempTaskID].noiseEffectiveDistance + "; Effective privacy budget: "
                    + winnerTaskWorkerPackedArray[tempTaskID].effectivePrivacyBudget);
        }

        System.out.println(serveredTaskSize);
    }
}