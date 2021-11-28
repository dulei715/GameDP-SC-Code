package edu.ecnu.dll.run.run_compared;

import edu.ecnu.dll.basic.basic_struct.pack.experiment_result_info.BasicExperimentResult;
import edu.ecnu.dll.basic.basic_struct.pack.experiment_result_info.ExtendedExperimentResult;
import edu.ecnu.dll.basic.basic_struct.pack.experiment_result_info.NormalExperimentResult;
import edu.ecnu.dll.basic.basic_struct.pack.experiment_result_info.PackExtendedExperimentResult;
import edu.ecnu.dll.basic.basic_struct.pack.single_agent_info.sub_class.WorkerIDDistancePair;
import edu.ecnu.dll.basic.basic_struct.pack.single_agent_info.sub_class.WorkerIDNoDistanceUtilityDistancePair;
import edu.ecnu.dll.run.result_tools.CommonFunction;
import edu.ecnu.dll.run.result_tools.TargetTool;
import edu.ecnu.dll.run.run_main.AbstractRun;
import edu.ecnu.dll.scheme.scheme_compared.solution._1_non_privacy.DistanceConflictEliminationNonPrivacySolution;
import edu.ecnu.dll.scheme.scheme_compared.solution._1_non_privacy.UtilityConflictEliminationNonPrivacySolution;
import tools.io.read.DoubleRead;
import tools.io.read.PointRead;
import tools.io.write.WriteExperimentResult;
import tools.struct.Point;

import java.io.File;
import java.util.List;

public class ConflictEliminationNonPrivacyCompleteRun extends AbstractRun {
    public static NormalExperimentResult runningOnSingleDataset(String parentPath, String dataType, double[] fixedTaskValueAndWorkerRange, Integer proposalSize) {
        // 从数据库读数据
//        String basicDatasetPath = "E:\\1.学习\\4.数据集\\1.FourSquare-NYCandTokyoCheck-ins\\output\\SYN";
        String basicDatasetPath = parentPath;

        String workerPointPath = basicDatasetPath + "\\worker_point.txt";
        String taskPointPath = basicDatasetPath + "\\task_point.txt";
        String taskValuePath = basicDatasetPath + "\\task_value.txt";
        String workerRangePath = basicDatasetPath + "\\worker_range.txt";

        List<Point> taskPointList = PointRead.readPointWithFirstLineCount(taskPointPath);
        List<Double> taskValueList = DoubleRead.readDoubleWithFirstSizeLineToList(taskValuePath);

        List<Point> workerPointList = PointRead.readPointWithFirstLineCount(workerPointPath);
        List<Double> workerRangeList = DoubleRead.readDoubleWithFirstSizeLineToList(workerRangePath);


        // 初始化 task 和 workers
        UtilityConflictEliminationNonPrivacySolution competitionSolution = new UtilityConflictEliminationNonPrivacySolution();
//        competitionSolution.initializeBasicInformation(taskPointList, taskValueArray, workerPointList, workerRangeList);
        competitionSolution.proposalSize = proposalSize;
        Double taskValue = null, workerRange = null;

        if (fixedTaskValueAndWorkerRange == null) {
            competitionSolution.initializeBasicInformation(taskPointList, taskValueList, workerPointList, workerRangeList);
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
        WorkerIDDistancePair[] winner = competitionSolution.compete();
        long endCompetingTime = System.currentTimeMillis();
        Long runningTime = TargetTool.getRunningTime(startCompetingTime, endCompetingTime);

        BasicExperimentResult basicExperimentResult = CommonFunction.getResultData(winner, competitionSolution.workers);
        NormalExperimentResult normalExperimentResult = new NormalExperimentResult(basicExperimentResult, runningTime);
        return normalExperimentResult;
    }


    public static ExtendedExperimentResult runningOnSingleDatasetWithUtilityConflictElimination(List<Point> taskPointList, List<Point> workerPointList,
                                                                                              double taskValue, double workerRange, Integer proposalSize, String dataType) {
        // 初始化 task 和 workers
        UtilityConflictEliminationNonPrivacySolution uConflictNPSolution = new UtilityConflictEliminationNonPrivacySolution();


        uConflictNPSolution.proposalSize = proposalSize;

        uConflictNPSolution.initializeBasicInformation(taskPointList, taskValue, workerPointList, workerRange);

        //todo: 根据不同的数据集选用不同的初始化
//        multiTaskMultiCompetitionSolution.initializeAgents();
        Integer dataTypeValue = Integer.valueOf(dataType);
        if (AbstractRun.COORDINATE.equals(dataTypeValue)) {
            uConflictNPSolution.initializeAgents();
        } else if (AbstractRun.LONGITUDE_LATITUDE.equals(dataTypeValue)) {
            uConflictNPSolution.initializeAgentsWithLatitudeLongitude();
        } else {
            throw new RuntimeException("The type input is not right!");
        }


        // 执行竞争过程
        long startCompetingTime = System.currentTimeMillis();
        WorkerIDNoDistanceUtilityDistancePair[] winner = uConflictNPSolution.compete();
        long endCompetingTime = System.currentTimeMillis();
        Long runningTime = TargetTool.getRunningTime(startCompetingTime, endCompetingTime);

        BasicExperimentResult basicExperimentResult = CommonFunction.getResultData(winner, uConflictNPSolution.workers);

        NormalExperimentResult normalExperimentResult = new NormalExperimentResult(basicExperimentResult, runningTime);
        ExtendedExperimentResult extendedExperimentResult = new ExtendedExperimentResult(normalExperimentResult, proposalSize, taskValue, workerRange);

        return extendedExperimentResult;
    }

    public static ExtendedExperimentResult runningOnSingleDatasetWithDistanceConflictElimination(List<Point> taskPointList, List<Point> workerPointList,
                                                                                                double taskValue, double workerRange, Integer proposalSize, String dataType) {
        // 初始化 task 和 workers
        DistanceConflictEliminationNonPrivacySolution dConflictNPSolution = new DistanceConflictEliminationNonPrivacySolution();


        dConflictNPSolution.proposalSize = proposalSize;

        dConflictNPSolution.initializeBasicInformation(taskPointList, taskValue, workerPointList, workerRange);

        //todo: 根据不同的数据集选用不同的初始化
//        multiTaskMultiCompetitionSolution.initializeAgents();
        Integer dataTypeValue = Integer.valueOf(dataType);
        if (AbstractRun.COORDINATE.equals(dataTypeValue)) {
            dConflictNPSolution.initializeAgents();
        } else if (AbstractRun.LONGITUDE_LATITUDE.equals(dataTypeValue)) {
            dConflictNPSolution.initializeAgentsWithLatitudeLongitude();
        } else {
            throw new RuntimeException("The type input is not right!");
        }


        // 执行竞争过程
        long startCompetingTime = System.currentTimeMillis();
        WorkerIDDistancePair[] winner = dConflictNPSolution.compete();
        long endCompetingTime = System.currentTimeMillis();
        Long runningTime = TargetTool.getRunningTime(startCompetingTime, endCompetingTime);

        BasicExperimentResult basicExperimentResult = CommonFunction.getResultData(winner, dConflictNPSolution.workers);

        NormalExperimentResult normalExperimentResult = new NormalExperimentResult(basicExperimentResult, runningTime);
        ExtendedExperimentResult extendedExperimentResult = new ExtendedExperimentResult(normalExperimentResult, proposalSize, taskValue, workerRange);

        return extendedExperimentResult;
    }
    public static ExtendedExperimentResult runningOnSingleDatasetWithUtilityConflictElimination(List<Point> taskPointList, List<Point> workerPointList,
                                                                                              List<Double> taskValueList, List<Double> workerRangeList, Integer proposalSize, String dataType) {
        // 初始化 task 和 workers
        UtilityConflictEliminationNonPrivacySolution uConflictNPSolution = new UtilityConflictEliminationNonPrivacySolution();


        uConflictNPSolution.proposalSize = proposalSize;

        uConflictNPSolution.initializeBasicInformation(taskPointList, taskValueList, workerPointList, workerRangeList);

        //todo: 根据不同的数据集选用不同的初始化
//        multiTaskMultiCompetitionSolution.initializeAgents();
        Integer dataTypeValue = Integer.valueOf(dataType);
        if (AbstractRun.COORDINATE.equals(dataTypeValue)) {
            uConflictNPSolution.initializeAgents();
        } else if (AbstractRun.LONGITUDE_LATITUDE.equals(dataTypeValue)) {
            uConflictNPSolution.initializeAgentsWithLatitudeLongitude();
        } else {
            throw new RuntimeException("The type input is not right!");
        }


        // 执行竞争过程
        long startCompetingTime = System.currentTimeMillis();
        WorkerIDNoDistanceUtilityDistancePair[] winner = uConflictNPSolution.compete();
        long endCompetingTime = System.currentTimeMillis();
        Long runningTime = TargetTool.getRunningTime(startCompetingTime, endCompetingTime);

        BasicExperimentResult basicExperimentResult = CommonFunction.getResultData(winner, uConflictNPSolution.workers);

        NormalExperimentResult normalExperimentResult = new NormalExperimentResult(basicExperimentResult, runningTime);
        ExtendedExperimentResult extendedExperimentResult = new ExtendedExperimentResult(normalExperimentResult, proposalSize, null, null);

        return extendedExperimentResult;
    }

    public static ExtendedExperimentResult runningOnSingleDatasetWithDistanceConflictElimination(List<Point> taskPointList, List<Point> workerPointList,
                                                                                                List<Double> taskValueList, List<Double> workerRangeList, Integer proposalSize, String dataType) {
        // 初始化 task 和 workers
        DistanceConflictEliminationNonPrivacySolution dConflictNPSolution = new DistanceConflictEliminationNonPrivacySolution();


        dConflictNPSolution.proposalSize = proposalSize;

        dConflictNPSolution.initializeBasicInformation(taskPointList, taskValueList, workerPointList, workerRangeList);

        //todo: 根据不同的数据集选用不同的初始化
//        multiTaskMultiCompetitionSolution.initializeAgents();
        Integer dataTypeValue = Integer.valueOf(dataType);
        if (AbstractRun.COORDINATE.equals(dataTypeValue)) {
            dConflictNPSolution.initializeAgents();
        } else if (AbstractRun.LONGITUDE_LATITUDE.equals(dataTypeValue)) {
            dConflictNPSolution.initializeAgentsWithLatitudeLongitude();
        } else {
            throw new RuntimeException("The type input is not right!");
        }


        // 执行竞争过程
        long startCompetingTime = System.currentTimeMillis();
        WorkerIDDistancePair[] winner = dConflictNPSolution.compete();
        long endCompetingTime = System.currentTimeMillis();
        Long runningTime = TargetTool.getRunningTime(startCompetingTime, endCompetingTime);

        BasicExperimentResult basicExperimentResult = CommonFunction.getResultData(winner, dConflictNPSolution.workers);

        NormalExperimentResult normalExperimentResult = new NormalExperimentResult(basicExperimentResult, runningTime);
        ExtendedExperimentResult extendedExperimentResult = new ExtendedExperimentResult(normalExperimentResult, proposalSize, null, null);

        return extendedExperimentResult;
    }

    public static void main(String[] args) {
        // for dataset
        String parentParentPath = "E:\\1.学习\\4.数据集\\1.FourSquare-NYCandTokyoCheck-ins\\output";

        File parentParentFile = new File(parentParentPath);
        File[] parentFilesArray = parentParentFile.listFiles();

        PackExtendedExperimentResult tempResult;

        String dataTile = ExtendedExperimentResult.getTitleNameString(",");
        ExtendedExperimentResult tempEEResult;

        String newTitle = PackExtendedExperimentResult.getSelfTitle() + "," + dataTile;



        String dataType = String.valueOf(AbstractRun.LONGITUDE_LATITUDE);


        WriteExperimentResult writeExperimentResult = new WriteExperimentResult();

        Integer[] proposalValues = new Integer[] {1, 4, 7, 10, 13, 16, 19, 22, Integer.MAX_VALUE};
//        double[] fixedTaskValueAndWorkerRange = new double[]{15, 1.1};
        double[] fixedTaskValueAndWorkerRange = new double[]{20, 2};



        //临时添加
        // 读入基本数据
        List<Point> taskPointList = PointRead.readPointWithFirstLineCount(parentParentPath + "\\SYN\\" + "task_point.txt");
        List<Point> workerPointList = PointRead.readPointWithFirstLineCount(parentParentPath + "\\SYN\\" + "worker_point.txt");

        // 读入隐私方案所需基本数据
//        List<Double[]>[] workerPrivacyBudgetList = TwoDimensionDoubleRead.readDouble(parentParentPath + "\\SYN\\" + "worker_budget.txt", 1);
//        List<Double[]>[] workerNoiseDistanceList = TwoDimensionDoubleRead.readDouble(parentParentPath + "\\SYN\\" + "worker_noise_distance.txt", 1);


//        List<String> stringExperimentResultList = new ArrayList<>();
        System.out.println(newTitle);
        for (int a = 0; a < proposalValues.length; a++) {
//                    tempNEResult = runningOnSingleDataset(parentFile.getAbsolutePath(), dataType, ppcfStateArray[j],fixedTaskValueAndWorkerRange, proposalValues[a]);
            tempEEResult = runningOnSingleDatasetWithUtilityConflictElimination(taskPointList, workerPointList, fixedTaskValueAndWorkerRange[0], fixedTaskValueAndWorkerRange[1],proposalValues[a], dataType);
            tempResult = new PackExtendedExperimentResult("SYN", Integer.valueOf(dataType), false, "UCENP", tempEEResult);
            System.out.println(tempResult.toString());
        }

    }


    public static void main2(String[] args) {
        // for dataset
        String parentParentPath = "E:\\1.学习\\4.数据集\\1.FourSquare-NYCandTokyoCheck-ins\\output";

        File parentParentFile = new File(parentParentPath);
        File[] parentFilesArray = parentParentFile.listFiles();

        PackExtendedExperimentResult tempResult;

        String dataTile = ExtendedExperimentResult.getTitleNameString(",");
        ExtendedExperimentResult tempEEResult;

        String newTitle = PackExtendedExperimentResult.getSelfTitle() + "," + dataTile;



        String dataType = String.valueOf(AbstractRun.LONGITUDE_LATITUDE);


        WriteExperimentResult writeExperimentResult = new WriteExperimentResult();

        Integer[] proposalValues = new Integer[] {1, 4, 7, 10, 13, 16, 19, 22, Integer.MAX_VALUE};
//        double[] fixedTaskValueAndWorkerRange = new double[]{15, 1.1};
        double[] fixedTaskValueAndWorkerRange = new double[]{20, 2};



        //临时添加
        // 读入基本数据
        List<Point> taskPointList = PointRead.readPointWithFirstLineCount(parentParentPath + "\\SYN\\" + "task_point.txt");
        List<Point> workerPointList = PointRead.readPointWithFirstLineCount(parentParentPath + "\\SYN\\" + "worker_point.txt");

        // 读入隐私方案所需基本数据
//        List<Double[]>[] workerPrivacyBudgetList = TwoDimensionDoubleRead.readDouble(parentParentPath + "\\SYN\\" + "worker_budget.txt", 1);
//        List<Double[]>[] workerNoiseDistanceList = TwoDimensionDoubleRead.readDouble(parentParentPath + "\\SYN\\" + "worker_noise_distance.txt", 1);


//        List<String> stringExperimentResultList = new ArrayList<>();
        System.out.println(newTitle);
        for (int a = 0; a < proposalValues.length; a++) {
//                    tempNEResult = runningOnSingleDataset(parentFile.getAbsolutePath(), dataType, ppcfStateArray[j],fixedTaskValueAndWorkerRange, proposalValues[a]);
            tempEEResult = runningOnSingleDatasetWithDistanceConflictElimination(taskPointList, workerPointList, fixedTaskValueAndWorkerRange[0], fixedTaskValueAndWorkerRange[1],proposalValues[a], dataType);
            tempResult = new PackExtendedExperimentResult("SYN", Integer.valueOf(dataType), false, "UCENP", tempEEResult);
            System.out.println(tempResult.toString());
        }

    }



}
