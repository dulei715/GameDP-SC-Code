package edu.ecnu.dll.scheme.run.compare_run;

import edu.ecnu.dll.basic_struct.pack.single_agent_info.sub_class.WorkerIDDistancePair;
import edu.ecnu.dll.scheme.run.target_tools.TargetTool;
import edu.ecnu.dll.scheme.solution._1_non_privacy.SingleTaskNonPrivacySolution;
import edu.ecnu.dll.scheme.solution._2_single_task.SingleTaskSolution;
import tools.io.print.MyPrint;
import tools.io.read.DoubleRead;
import tools.io.read.PointRead;
import tools.struct.Point;

import java.util.List;

public class SingleTaskNonPrivacyRun {
    public static void main(String[] args) {
        // 从数据库读数据
        String basicPath = System.getProperty("user.dir") + "\\dataset\\test_dataset\\_1_single_task_dataset\\";
        String taskPointPath = basicPath + "test1\\task_point.txt";
        String taskValuePath = basicPath + "test1\\task_value.txt";
        String workerPointPath = basicPath + "test1\\worker_point.txt";
//        String workerPrivacyBudgetPath = basicPath + "test1\\worker_privacy_budget.txt";

        List<Point> taskPointList = PointRead.readPoint(taskPointPath);
        Double[] taskValueArray = DoubleRead.readDouble(taskValuePath);

        List<Point> workerPointList = PointRead.readPoint(workerPointPath);
//        List<Double>[] workerPrivacyBudgetList = DoubleRead.readDoubleList(workerPrivacyBudgetPath);

        MyPrint.showList(taskPointList);
        System.out.println("******************************************************************************");
        MyPrint.showDoubleArray(taskValueArray);
        System.out.println("******************************************************************************");
        MyPrint.showList(workerPointList);
        System.out.println("******************************************************************************");
//        MyPrint.showListArray(workerPrivacyBudgetList);
//        System.out.println("******************************************************************************");

        // 初始化 task 和 workers
        SingleTaskNonPrivacySolution singleTaskSolution = new SingleTaskNonPrivacySolution();
        singleTaskSolution.initializeBasicInformation(taskPointList, taskValueArray, workerPointList);
        singleTaskSolution.initializeAgents();


        long startCompetingTime = System.currentTimeMillis();
        // 执行竞争过程
        WorkerIDDistancePair competeResult = singleTaskSolution.compete();
        long endCompetingTime = System.currentTimeMillis();
        System.out.println(competeResult);
        Long runningTime = TargetTool.getRunningTime(startCompetingTime, endCompetingTime);
        Double freeTaskRatio = TargetTool.getFreeTaskRatio(competeResult);
        Double freeWorkerRatio = TargetTool.getFreeWorkerRatio(singleTaskSolution.workers.length, competeResult);

        System.out.println("The competing time is: " + runningTime);
        System.out.println("The free task ratio is: "+ freeTaskRatio);
        System.out.println("The free worker ratio is: "+ freeWorkerRatio);

    }



}
