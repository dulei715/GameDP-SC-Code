package edu.ecnu.dll.scheme.run.main_run;

import edu.ecnu.dll.basic_struct.agent.Worker;
import edu.ecnu.dll.basic_struct.pack.experiment_result_info.BasicExperimentResult;
import edu.ecnu.dll.basic_struct.pack.experiment_result_info.NormalExperimentResult;
import edu.ecnu.dll.basic_struct.pack.TaskWorkerIDPair;
import edu.ecnu.dll.basic_struct.pack.single_agent_info.sub_class.WorkerIDDistanceBudgetPair;
import edu.ecnu.dll.scheme.run.common.CommonFunction;
import edu.ecnu.dll.scheme.run.target_tools.TargetTool;
import edu.ecnu.dll.scheme.solution._1_non_privacy.MultiTaskMultiCompetitionNonPrivacySolution;
import edu.ecnu.dll.scheme.solution._3_multiple_task.MultiTaskMultiCompetitionSolution;
import tools.io.read.DoubleRead;
import tools.io.read.PointRead;
import tools.io.read.TwoDimensionDoubleRead;
import tools.struct.Point;

import java.util.ArrayList;
import java.util.List;

public class MultiTaskMultiCompleteRun extends AbstractRun {
    public static NormalExperimentResult runningOnSingleDataset(String parentPath, String dataType, boolean ppcfState, Integer workerChosenState, boolean eceaState) {
        // 从数据库读数据
//        String basicDatasetPath = "E:\\1.学习\\4.数据集\\1.FourSquare-NYCandTokyoCheck-ins\\output\\SYN";
        String basicDatasetPath = parentPath;

        String workerPointPath = basicDatasetPath + "\\worker_point.txt";
        String taskPointPath = basicDatasetPath + "\\task_point.txt";
        String taskValuePath = basicDatasetPath + "\\task_value.txt";
        String workerRangePath = basicDatasetPath + "\\worker_range.txt";
        String workerPrivacyBudgetPath = basicDatasetPath + "\\worker_budget.txt";
        String workerNoiseDistancePath = basicDatasetPath + "\\worker_noise_distance.txt";

        List<Point> taskPointList = PointRead.readPointWithFirstLineCount(taskPointPath);
        Double[] taskValueArray = DoubleRead.readDouble(taskValuePath);

        List<Point> workerPointList = PointRead.readPointWithFirstLineCount(workerPointPath);
        List<Double> workerRangeList = DoubleRead.readDoubleToList(workerRangePath);
        List<Double[]>[] workerPrivacyBudgetList = TwoDimensionDoubleRead.readDouble(workerPrivacyBudgetPath);
        List<Double[]>[] workerNoiseDistanceList = TwoDimensionDoubleRead.readDouble(workerNoiseDistancePath);


        // 初始化 task 和 workers
        MultiTaskMultiCompetitionSolution competitionSolution = new MultiTaskMultiCompetitionSolution();
        competitionSolution.initializeBasicInformation(taskPointList, taskValueArray, workerPointList, workerRangeList);

        //todo: 根据不同的数据集选用不同的初始化
//        multiTaskMultiCompetitionSolution.initializeAgents();
        Integer dataTypeValue = Integer.valueOf(dataType);
        if (AbstractRun.COORDINATE.equals(dataTypeValue)) {
            competitionSolution.initializeAgents(workerPrivacyBudgetList, workerNoiseDistanceList);
        } else if (AbstractRun.LONGITUDE_LATITUDE.equals(dataTypeValue)) {
            competitionSolution.initializeAgentsWithLatitudeLongitude(workerPrivacyBudgetList, workerNoiseDistanceList);
        } else {
            throw new RuntimeException("The type input is not right!");
        }


        // 执行竞争过程
        long startCompetingTime = System.currentTimeMillis();
        WorkerIDDistanceBudgetPair[] winner = competitionSolution.complete(ppcfState, workerChosenState, eceaState);
        long endCompetingTime = System.currentTimeMillis();
        Long runningTime = TargetTool.getRunningTime(startCompetingTime, endCompetingTime);

//        showResultA(winner);
        BasicExperimentResult basicExperimentResult = CommonFunction.getResultData(winner, competitionSolution.workers);

//        showResultB(winner);

        NormalExperimentResult normalExperimentResult = new NormalExperimentResult(basicExperimentResult, runningTime);

//        System.out.println(normalExperimentResult);
        return normalExperimentResult;
    }

    public static void main(String[] args) {
        String parentPath = "E:\\\\1.学习\\\\4.数据集\\\\1.FourSquare-NYCandTokyoCheck-ins\\\\output\\\\SYN";
        String dataType = String.valueOf(AbstractRun.LONGITUDE_LATITUDE);
        String ppcfState = "false";
//        String ppcfState = "true";

//        String workerChosenState = String.valueOf(MultiTaskMultiCompetitionSolution.ONLY_UTILITY);
//        String workerChosenState = String.valueOf(MultiTaskMultiCompetitionSolution.UTILITY_WITH_TASK_ENTROPY);
        String workerChosenState = String.valueOf(MultiTaskMultiCompetitionSolution.UTILITY_WITH_PROPOSING_VALUE);

        String eceaState = "false";
//        String eceaState = "true";
        NormalExperimentResult normalExperimentResult = runningOnSingleDataset(parentPath, dataType, Boolean.valueOf(ppcfState), Integer.valueOf(workerChosenState), Boolean.valueOf(eceaState));
        System.out.println(normalExperimentResult);
    }

}
