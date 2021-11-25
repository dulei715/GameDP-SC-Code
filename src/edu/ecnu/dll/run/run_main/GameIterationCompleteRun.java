package edu.ecnu.dll.run.run_main;

import edu.ecnu.dll.basic.basic_struct.pack.experiment_result_info.*;
import edu.ecnu.dll.basic.basic_struct.pack.single_agent_info.sub_class.WorkerIDNoiseDistanceBudgetPair;
import edu.ecnu.dll.run.result_tools.CommonFunction;
import edu.ecnu.dll.run.result_tools.TargetTool;
import edu.ecnu.dll.scheme.scheme_main.solution._2_multiple_task.GameTheorySolution;
import tools.io.read.PointRead;
import tools.io.read.TwoDimensionDoubleRead;
import tools.io.write.WriteExperimentResult;
import tools.struct.Point;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GameIterationCompleteRun extends AbstractRun {


    public static void main(String[] args) {
        // for dataset
//        String parentPath = args[0];
//        String parentPath = "E:\\1.学习\\4.数据集\\1.FourSquare-NYCandTokyoCheck-ins\\output\\SYN";

//        String parentParentPath = args[0];
//        String parentParentPath = "E:\\1.学习\\4.数据集\\1.FourSquare-NYCandTokyoCheck-ins\\output";
//        String parentParentPath = "E:\\1.学习\\4.数据集\\1.FourSquare-NYCandTokyoCheck-ins\\output\\test";
        String parentParentPath = "E:\\1.学习\\4.数据集\\1.FourSquare-NYCandTokyoCheck-ins\\output";

        File parentParentFile = new File(parentParentPath);
        File[] parentFilesArray = parentParentFile.listFiles();

        PackExtendedExperimentResult tempResult;

        String dataTile = ExtendedExperimentResult.getTitleNameString(",");
        ExtendedExperimentResult tempEEResult;

        String newTitle = PackExtendedExperimentResult.getSelfTitle() + "," + dataTile;



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



        //临时添加
        // 读入基本数据
        List<Point> taskPointList = PointRead.readPointWithFirstLineCount(parentParentPath + "\\SYN\\" + "task_point.txt");
        List<Point> workerPointList = PointRead.readPointWithFirstLineCount(parentParentPath + "\\SYN\\" + "worker_point.txt");

        // 读入隐私方案所需基本数据
        List<Double[]>[] workerPrivacyBudgetList = TwoDimensionDoubleRead.readDouble(parentParentPath + "\\SYN\\" + "worker_budget.txt", 1);
        List<Double[]>[] workerNoiseDistanceList = TwoDimensionDoubleRead.readDouble(parentParentPath + "\\SYN\\" + "worker_noise_distance.txt", 1);


        List<String> stringExperimentResultList = new ArrayList<>();
        tempEEResult = runningOnSingleDataset(taskPointList, workerPointList, workerPrivacyBudgetList, workerNoiseDistanceList, fixedTaskValueAndWorkerRange[0], fixedTaskValueAndWorkerRange[1],dataType);
        tempResult = new PackExtendedExperimentResult("SYN", Integer.valueOf(dataType), false, "GI", tempEEResult);
        // String datasetName, Integer dataType, Boolean ppcfState, Boolean eceaState, Integer workerChosenState
//                        String newTitle = concat(",", parentFile.getName(), dataType, ppcfStateArray[j], eceaStateArray[k], workerChosenStateArray[l]);
        stringExperimentResultList.add(tempResult.toString());

//        String outputPath = parentParentPath + "\\SYN" + File.separator + "SYN_result_test.csv";
//        writeExperimentResult.writeResultList(outputPath, newTitle, stringExperimentResultList);
        System.out.println(newTitle);
        System.out.println(stringExperimentResultList);


//        NormalExperimentResult normalExperimentResult = runningOnSingleDataset(parentPath, dataType, Boolean.valueOf(ppcfState), Integer.valueOf(workerChosenState), Boolean.valueOf(eceaStateArray));
//        System.out.println(normalExperimentResult);
    }


    public static ExtendedExperimentResult runningOnSingleDataset(List<Point> taskPointList, List<Point> workerPointList, List<Double[]>[] workerPrivacyBudgetList, List<Double[]>[] workerNoiseDistanceList,
                                                                                               double taskValue, double workerRange, String dataType) {

        // 初始化 task 和 workers
        GameTheorySolution gGameSolution = new GameTheorySolution();



        gGameSolution.initializeBasicInformation(taskPointList, taskValue, workerPointList, workerRange);

        //todo: 根据不同的数据集选用不同的初始化
//        multiTaskMultiCompetitionSolution.initializeAgents();
        Integer dataTypeValue = Integer.valueOf(dataType);
        if (AbstractRun.COORDINATE.equals(dataTypeValue)) {
            gGameSolution.initializeAgents(workerPrivacyBudgetList, workerNoiseDistanceList);
        } else if (AbstractRun.LONGITUDE_LATITUDE.equals(dataTypeValue)) {
            gGameSolution.initializeAgentsWithLatitudeLongitude(workerPrivacyBudgetList, workerNoiseDistanceList);
        } else {
            throw new RuntimeException("The type input is not right!");
        }


        // 执行竞争过程
        long startCompetingTime = System.currentTimeMillis();
//        WorkerIDNoDistanceUtilityNoiseDistanceBudgetPair[] winner = gGameSolution.compete();
        WorkerIDNoiseDistanceBudgetPair[] winner = gGameSolution.compete();
        long endCompetingTime = System.currentTimeMillis();
        Long runningTime = TargetTool.getRunningTime(startCompetingTime, endCompetingTime);

        BasicExperimentResult basicExperimentResult = CommonFunction.getResultData(winner, gGameSolution.workers);

        NormalExperimentResult normalExperimentResult = new NormalExperimentResult(basicExperimentResult, runningTime);
        ExtendedExperimentResult extendedExperimentResult = new ExtendedExperimentResult(normalExperimentResult, 0, taskValue, workerRange);

        return extendedExperimentResult;
    }

}
