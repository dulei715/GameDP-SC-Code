package edu.ecnu.dll.scheme.run.compare_run;

import edu.ecnu.dll.basic_struct.agent.Worker;
import edu.ecnu.dll.basic_struct.comparator.TaskWorkerIDComparator;
import edu.ecnu.dll.basic_struct.pack.TaskWorkerIDPair;
import edu.ecnu.dll.basic_struct.pack.experiment_result_info.BasicExperimentResult;
import edu.ecnu.dll.basic_struct.pack.experiment_result_info.NormalExperimentResult;
import edu.ecnu.dll.basic_struct.pack.single_agent_info.sub_class.WorkerIDDistanceBudgetPair;
import edu.ecnu.dll.basic_struct.pack.single_agent_info.sub_class.WorkerIDDistancePair;
import edu.ecnu.dll.scheme.run.common.CommonFunction;
import edu.ecnu.dll.scheme.run.main_run.AbstractRun;
import edu.ecnu.dll.scheme.run.target_tools.TargetTool;
import edu.ecnu.dll.scheme.solution._1_non_privacy.MultiTaskMultiCompetitionNonPrivacySolution;
import edu.ecnu.dll.scheme.solution._3_multiple_task.MultiTaskMultiCompetitionSolution;
import tools.io.print.MyPrint;
import tools.io.read.DoubleRead;
import tools.io.read.PointRead;
import tools.io.read.TwoDimensionDoubleRead;
import tools.struct.Point;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MultiTaskMultiCompleteNonPrivacyRun extends AbstractRun {
    public static NormalExperimentResult runningOnSingleDataset(String parentPath, String dataType) {
        // 从数据库读数据
//        String basicDatasetPath = "E:\\1.学习\\4.数据集\\1.FourSquare-NYCandTokyoCheck-ins\\output\\SYN";
        String basicDatasetPath = parentPath;

        String workerPointPath = basicDatasetPath + "\\worker_point.txt";
        String taskPointPath = basicDatasetPath + "\\task_point.txt";
        String taskValuePath = basicDatasetPath + "\\task_value.txt";
        String workerRangePath = basicDatasetPath + "\\worker_range.txt";

        List<Point> taskPointList = PointRead.readPointWithFirstLineCount(taskPointPath);
        Double[] taskValueArray = DoubleRead.readDouble(taskValuePath);

        List<Point> workerPointList = PointRead.readPointWithFirstLineCount(workerPointPath);
        List<Double> workerRangeList = DoubleRead.readDoubleToList(workerRangePath);


        // 初始化 task 和 workers
        MultiTaskMultiCompetitionNonPrivacySolution competitionSolution = new MultiTaskMultiCompetitionNonPrivacySolution();
        competitionSolution.initializeBasicInformation(taskPointList, taskValueArray, workerPointList, workerRangeList);

        //todo: 根据不同的数据集选用不同的初始化
        Integer dataTypeValue = Integer.valueOf(dataType);
        if (AbstractRun.COORDINATE.equals(dataTypeValue)) {
            competitionSolution.initializeAgents();
        } else if (AbstractRun.LONGITUDE_LATITUDE.equals(dataTypeValue)) {
            competitionSolution.initializeAgentsWithLatitudeLongitude();
        } else {
            throw new RuntimeException("The type input is not right!");
        }


        // 执行竞争过程
        long startCompetingTime = System.currentTimeMillis();
        WorkerIDDistancePair[] winner = competitionSolution.complete();
        long endCompetingTime = System.currentTimeMillis();
        Long runningTime = TargetTool.getRunningTime(startCompetingTime, endCompetingTime);

        BasicExperimentResult basicExperimentResult = CommonFunction.getResultData(winner, competitionSolution.workers);
        NormalExperimentResult normalExperimentResult = new NormalExperimentResult(basicExperimentResult, runningTime);
        return normalExperimentResult;
    }

    public static void main(String[] args) {
        String parentPath = "E:\\\\1.学习\\\\4.数据集\\\\1.FourSquare-NYCandTokyoCheck-ins\\\\output\\\\SYN";
        String dataType = String.valueOf(AbstractRun.LONGITUDE_LATITUDE);
        NormalExperimentResult normalExperimentResult = runningOnSingleDataset(parentPath, dataType);
        System.out.println(normalExperimentResult);
    }




}
