package edu.ecnu.dll.scheme.run.main_run;

import edu.ecnu.dll.basic_struct.pack.experiment_result_info.BasicExperimentResult;
import edu.ecnu.dll.basic_struct.pack.experiment_result_info.ExtendedExperimentResult;
import edu.ecnu.dll.basic_struct.pack.experiment_result_info.NormalExperimentResult;
import edu.ecnu.dll.basic_struct.pack.experiment_result_info.PackNormalExperimentResult;
import edu.ecnu.dll.basic_struct.pack.single_agent_info.sub_class.WorkerIDNoiseDistanceBudgetPair;
import edu.ecnu.dll.scheme.run.common.CommonFunction;
import edu.ecnu.dll.scheme.run.target_tools.TargetTool;
import edu.ecnu.dll.scheme.solution._3_multiple_task.ConflictEliminationBasedSolution;
import tools.io.read.DoubleRead;
import tools.io.read.PointRead;
import tools.io.read.TwoDimensionDoubleRead;
import tools.io.write.WriteExperimentResult;
import tools.struct.Point;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MultiTaskMultiCompleteRun extends AbstractRun {
    public static NormalExperimentResult runningOnSingleDataset(String parentPath, String dataType, boolean ppcfState, double[] fixedTaskValueAndWorkerRange, Integer proposalSize) {
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
        ConflictEliminationBasedSolution competitionSolution = new ConflictEliminationBasedSolution();

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
//        multiTaskMultiCompetitionSolution.initializeAgents();
        Integer dataTypeValue = Integer.valueOf(dataType);
        if (AbstractRun.COORDINATE.equals(dataTypeValue)) {
            competitionSolution.initializeAgents(workerPrivacyBudgetList, workerNoiseDistanceList);
        } else if (AbstractRun.LONGITUDE_LATITUDE.equals(dataTypeValue)) {
            competitionSolution.initializeAgentsWithLatitudeLongitude(workerPrivacyBudgetList, workerNoiseDistanceList);
        } else {
            throw new RuntimeException("The type input is not right!");
        }


        /**
         * 自变量：
         * 1. task 数量变化 (数据集)
         * 2. worker 数量变化 (数据集)
         * 3. privacy group size 变化(数据集)
         *
         * 4. task value 变化范围
         * 5. worker range 变化范围
         * 6. α 变化范围
         * 7. β 变化范围
         *
         *
         * 对比方案
         * 1. NonPrivacyBestSolution
         * 2. ConflictBasedSolution
         * 3. GameTheoryBasedSolution
         *
         * 因变量：
         * 1. competing time
         * 2. utility value
         * 3. real travel distance
         *
         */

        // 执行竞争过程
        long startCompetingTime = System.currentTimeMillis();
        WorkerIDNoiseDistanceBudgetPair[] winner = competitionSolution.compete(ppcfState);
        long endCompetingTime = System.currentTimeMillis();
        Long runningTime = TargetTool.getRunningTime(startCompetingTime, endCompetingTime);

        BasicExperimentResult basicExperimentResult = CommonFunction.getResultData(winner, competitionSolution.workers);

        NormalExperimentResult normalExperimentResult = new NormalExperimentResult(basicExperimentResult, runningTime);
        ExtendedExperimentResult extendedExperimentResult = new ExtendedExperimentResult(normalExperimentResult, proposalSize, taskValue, workerRange);

        return extendedExperimentResult;
    }


    public static void main(String[] args) {
        // for dataset
//        String parentPath = args[0];
//        String parentPath = "E:\\1.学习\\4.数据集\\1.FourSquare-NYCandTokyoCheck-ins\\output\\SYN";

//        String parentParentPath = args[0];
        String parentParentPath = "E:\\1.学习\\4.数据集\\1.FourSquare-NYCandTokyoCheck-ins\\output";

        File parentParentFile = new File(parentParentPath);
        File[] parentFilesArray = parentParentFile.listFiles();

        PackNormalExperimentResult tempResult;

        String dataTile = ExtendedExperimentResult.getTitleNameString(",");
        NormalExperimentResult tempNEResult;

        String newTitle = PackNormalExperimentResult.getSelfTitle() + "," + dataTile;



        // for dataset type
//        String dataType = args[1];
        String dataType = String.valueOf(AbstractRun.LONGITUDE_LATITUDE);
//        String dataType = String.valueOf(AbstractRun.LONGITUDE_LATITUDE);

        // for
        Boolean[] ppcfStateArray = new Boolean[]{false, true};
//        String ppcfState = "false";
//        String ppcfState = "true";



        WriteExperimentResult writeExperimentResult = new WriteExperimentResult();

        Integer[] proposalValues = new Integer[] {1, 4, 7, 10, 13, 16, 19, 22, Integer.MAX_VALUE};
//        double[] fixedTaskValueAndWorkerRange = new double[]{15, 1.1};
        double[] fixedTaskValueAndWorkerRange = new double[]{20, 2};


        for (int i = 0; i < parentFilesArray.length; i++) {
            File parentFile = parentFilesArray[i];
            List<String> stringExperimentResultList = new ArrayList<>();
            for (int a = 0; a < proposalValues.length; a++) {
                for (int j = 0; j < ppcfStateArray.length; j++) {
                    tempNEResult = runningOnSingleDataset(parentFile.getAbsolutePath(), dataType, ppcfStateArray[j],fixedTaskValueAndWorkerRange, proposalValues[a]);
                    tempResult = new PackNormalExperimentResult(parentFile.getName(), Integer.valueOf(dataType), ppcfStateArray[j], tempNEResult);
                    // String datasetName, Integer dataType, Boolean ppcfState, Boolean eceaState, Integer workerChosenState
//                        String newTitle = concat(",", parentFile.getName(), dataType, ppcfStateArray[j], eceaStateArray[k], workerChosenStateArray[l]);
                    stringExperimentResultList.add(tempResult.toString());
                }
            }
            String outputPath = parentFile.getAbsolutePath() + File.separator + parentFile.getName() + "_result.csv";
            writeExperimentResult.writeResultList(outputPath, newTitle, stringExperimentResultList);

        }

//        NormalExperimentResult normalExperimentResult = runningOnSingleDataset(parentPath, dataType, Boolean.valueOf(ppcfState), Integer.valueOf(workerChosenState), Boolean.valueOf(eceaStateArray));
//        System.out.println(normalExperimentResult);
    }



}
