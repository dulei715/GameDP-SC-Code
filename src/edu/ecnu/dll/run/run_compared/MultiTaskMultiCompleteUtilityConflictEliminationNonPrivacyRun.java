package edu.ecnu.dll.run.run_compared;

import edu.ecnu.dll.basic.basic_struct.pack.experiment_result_info.BasicExperimentResult;
import edu.ecnu.dll.basic.basic_struct.pack.experiment_result_info.NormalExperimentResult;
import edu.ecnu.dll.basic.basic_struct.pack.single_agent_info.sub_class.WorkerIDDistancePair;
import edu.ecnu.dll.run.result_tools.CommonFunction;
import edu.ecnu.dll.run.result_tools.TargetTool;
import edu.ecnu.dll.run.run_main.AbstractRun;
import edu.ecnu.dll.scheme.scheme_compared.solution._1_non_privacy.UtilityConflictEliminationBasedNonPrivacySolution;
import tools.io.read.DoubleRead;
import tools.io.read.PointRead;
import tools.struct.Point;

import java.util.ArrayList;
import java.util.List;

public class MultiTaskMultiCompleteUtilityConflictEliminationNonPrivacyRun extends AbstractRun {
    public static NormalExperimentResult runningOnSingleDataset(String parentPath, String dataType, double[] fixedTaskValueAndWorkerRange, Integer proposalSize) {
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
        UtilityConflictEliminationBasedNonPrivacySolution competitionSolution = new UtilityConflictEliminationBasedNonPrivacySolution();
//        competitionSolution.initializeBasicInformation(taskPointList, taskValueArray, workerPointList, workerRangeList);
        competitionSolution.proposalSize = proposalSize;
        Double taskValue = null, workerRange = null;

        if (fixedTaskValueAndWorkerRange == null) {
            competitionSolution.initializeBasicInformation(taskPointList, taskValueArray, workerPointList, workerRangeList);
        } else {
            taskValue = fixedTaskValueAndWorkerRange[0];
            workerRange = fixedTaskValueAndWorkerRange[1];
            competitionSolution.initializeBasicInformation(taskPointList, taskValue, workerPointList, workerRange);
        }

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
//        String parentPath = "E:\\\\1.学习\\\\4.数据集\\\\1.FourSquare-NYCandTokyoCheck-ins\\\\output\\\\test";
        String dataType = String.valueOf(AbstractRun.LONGITUDE_LATITUDE);
        int proposalSizeNumber = 60;
        List<Integer> proposalSizeList = new ArrayList<>();
        for (int i = 0; i < proposalSizeNumber; i++) {
            proposalSizeList.add(i*3+1);
        }
        proposalSizeList.add(Integer.MAX_VALUE);
//        Integer[] proposalSize = new Integer[]{1, 4, 7, 10, 13, 16, 19, 22, 25, 28, Integer.MAX_VALUE};
        Integer[] proposalSize = proposalSizeList.toArray(new Integer[0]);
//        String workerChosenState = String.valueOf(MultiTaskMultiCompetitionNonPrivacySolution.UTILITY_WITH_TASK_ENTROPY);
        double[] valueRange = new double[]{20,4};

        for (int i = 0; i < proposalSize.length; i++) {
            NormalExperimentResult normalExperimentResult = runningOnSingleDataset(parentPath, dataType, valueRange, proposalSize[i]);
            System.out.println(normalExperimentResult);
        }

    }




}
