package edu.ecnu.dll.scheme.run;

import edu.ecnu.dll.basic_struct.comparator.TaskWorkerIDComparator;
import edu.ecnu.dll.basic_struct.pack.TaskWorkerIDPair;
import edu.ecnu.dll.basic_struct.pack.Winner;
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
        String taskPointPath = basicPath + "test1\\task_point.txt";
        String taskValuePath = basicPath + "test1\\task_value.txt";
        String workerPointPath = basicPath + "test1\\worker_point.txt";
        String workerPrivacyBudgetPath = basicPath + "test1\\worker_privacy_budget.txt";

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
        Winner winner = multiTaskSingleCompetitionSolution.complete();
        Integer[] winTaskWorkerIDArray = winner.winTaskWorkerIDArray;
        Double[][] winnTaskWorkerInfoArray = winner.winnTaskWorkerInfoArray;
//        showResultA(winTaskWorkerIDArray, winnTaskWorkerInfoArray);
        showResultB(winTaskWorkerIDArray, winnTaskWorkerInfoArray);

    }

    private static void showResultA(Integer[] winTaskWorkerIDArray, Double[][] winnTaskWorkerInfoArray) {
        int serveredTaskSize = 0;
        for (int i = 0; i < winTaskWorkerIDArray.length; i++) {
//        for (int i = 0; i < 10; i++) {
            if (winTaskWorkerIDArray[i] == -1) {
                continue;
            }
            ++ serveredTaskSize;
            System.out.println("Task ID: " + i + "; Winner worker ID: " + winTaskWorkerIDArray[i] + "; Effective noise distance: "
                    + winnTaskWorkerInfoArray[i][MultiTaskSingleCompetitionSolution.DISTANCE_TAG] + "; Effective privacy budget: "
                    + winnTaskWorkerInfoArray[i][MultiTaskSingleCompetitionSolution.BUDGET_TAG]);
        }

        System.out.println(serveredTaskSize);
    }

    private static void showResultB(Integer[] winTaskWorkerIDArray, Double[][] winnTaskWorkerInfoArray) {
        int serveredTaskSize = 0;
        TaskWorkerIDPair[] taskWorkerIDPairArray;
        List<TaskWorkerIDPair> taskWorkerIDPairList = new ArrayList<>();

        for (int i = 0; i < winTaskWorkerIDArray.length; i++) {
            if (winTaskWorkerIDArray[i] == -1) {
                continue;
            }
            taskWorkerIDPairList.add(new TaskWorkerIDPair(i, winTaskWorkerIDArray[i]));
        }
        serveredTaskSize = taskWorkerIDPairList.size();
        taskWorkerIDPairArray = taskWorkerIDPairList.toArray(new TaskWorkerIDPair[0]);
        Arrays.sort(taskWorkerIDPairArray, new TaskWorkerIDComparator(TaskWorkerIDComparator.WORKER_FIRST, TaskWorkerIDComparator.TASK_ASCENDING, TaskWorkerIDComparator.WORKER_ASCENDING));
        int tempTaskID, tempWorkerID;
        for (int i = 0; i < serveredTaskSize; i++) {
            tempTaskID = taskWorkerIDPairArray[i].taskID;
            tempWorkerID = taskWorkerIDPairArray[i].workerID;
            System.out.println("Winner worker ID: " + tempWorkerID + "; Task ID: " + tempTaskID + "; Effective noise distance: "
                    + winnTaskWorkerInfoArray[tempTaskID][MultiTaskSingleCompetitionSolution.DISTANCE_TAG] + "; Effective privacy budget: "
                    + winnTaskWorkerInfoArray[tempTaskID][MultiTaskSingleCompetitionSolution.BUDGET_TAG]);
        }

        System.out.println(serveredTaskSize);
    }
}
