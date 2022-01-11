package edu.ecnu.dll.run.batch_run_total.test;

import edu.ecnu.dll.basic.basic_struct.pack.experiment_result_info.ExtendedExperimentResult;
import edu.ecnu.dll.basic.basic_struct.pack.experiment_result_info.PackExtendedExperimentResult;
import edu.ecnu.dll.config.Constant;
import edu.ecnu.dll.run.run_compared.ConflictEliminationNonPrivacyCompleteRun;
import edu.ecnu.dll.run.run_compared.GameIterationNonPrivacyCompleteRun;
import edu.ecnu.dll.run.run_compared.GreedyNonPrivacyCompleteRun;
import edu.ecnu.dll.run.run_main.ConflictEliminationCompleteRun;
import edu.ecnu.dll.run.run_main.GameIterationCompleteRun;
import tools.io.read.PointRead;
import tools.io.read.TwoDimensionDoubleRead;
import tools.io.write.WriteExperimentResult;
import tools.struct.Point;

import java.util.ArrayList;
import java.util.List;

public class BatchNonPrivacyRunA_WorkerRatio {
    public static void main(String[] args) {

        // todo: 修改数据集路径
//        String basicPath = "E:\\1.学习\\4.数据集\\dataset\\original\\chengdu_total_dataset_km";
        String basicPath = args[0];
        // todo: 修改数据集路径2
        String[] parentPartPathArray = Constant.parentPathArray;
        // todo: 修改数据集类型
//        String dataType = AbstractRun.COORDINATE.toString();
        String dataType = args[1];
        // todo: 修改数据集名称
//        String datasetName = "ChengduDiDi";
        String datasetName = args[2];

        String batchNumberString = args[3];

        String taskPointFileName = Constant.FILE_PATH_SPLIT + "batch_" + batchNumberString + "_task_point.txt";
        String workerPointFileName = Constant.FILE_PATH_SPLIT + "batch_" + batchNumberString + "_worker_point.txt";
        String workerBudgetFileName = Constant.FILE_PATH_SPLIT + "batch_" + batchNumberString + "_worker_budget.txt";
        String workerNoiseDistanceFileName = Constant.FILE_PATH_SPLIT + "batch_" + batchNumberString + "_worker_noise_distance.txt";

        String basicOutputPath = basicPath + Constant.FILE_PATH_SPLIT + "outputPath" + Constant.FILE_PATH_SPLIT + "_" + batchNumberString + "_";
        String outputPath = basicOutputPath + Constant.FILE_PATH_SPLIT + "output_worker_ratio_change.csv";

        /**
         * 基本设置
         *      1. f_1 参数 α 设置
         *      2. f_2 参数 β 设置
         *      3. worker申请task数量的最大值
         *      4. data type
         *      5. 数据集名称
         */
        // todo: 修改效用函数参数: 在Constant类里
//        Solution.alpha = 0.1;
//        Solution.alpha = 1;
//        Solution.beta = 0.2;
//        Solution.beta = 1;


//        int proposalSize = Integer.MAX_VALUE;
        int proposalSize = Constant.defaultProposalSize;






        /**
         * 一. 自变量
         *
         *      1. worker 占比（和数据库相关 todo:暂不比较1）
         *      2. task value 值
         *      3. worker range 值
         *      4. privacy budget 组数（todo: 暂不比较2）
         *      5. privacy budget 大小（todo: 暂不比较2）
         */

        Double workerTaskRatioDefault = 2.5;




        Double taskValueDefault = Constant.taskValueDefault;


//        Double workerRangeDefault = 1.1;
//        Double workerRangeDefault = 2.0;
//        Double workerRangeDefault = 50000.0;
        Double workerRangeDefault = Constant.workerRangeDefault;


        /**
         * 二. 因变量
         *
         *      1. Running time
         *      2. Utility value
         *      3. Real travel distance
         *
         */


        /**
         * 三. 涉及的solution
         *
         *      1. 本文方案
         *          (1) Utility conflict without PPCF
         *              Utility conflict with PPCF
         *              Distance conflict without PPCF
         *              Distance conflict with PPCF
         *          (2) Game theory
         *
         *      2. 对比方案
         *          (1) Non privacy utility conflict
         *          (2) Non privacy distance conflict
         *          (3) Non privacy game theory
         *          (4) Non privacy Greedy
         *
         */


        // 读入基本数据
        List<Point> taskPointList = null;
        List<Point> workerPointList = null;

        // 读入隐私方案所需基本数据
        List<Double[]>[] workerPrivacyBudgetList = null;
        List<Double[]>[] workerNoiseDistanceList = null;



        String parentPartPath;
        Boolean ppfState;
//        ExtendedExperimentResult experimentResult;
        String solutionName;
        List<String> resultList = new ArrayList<>();
        System.out.println();
        String title = PackExtendedExperimentResult.getSelfTitle() + "," + ExtendedExperimentResult.getTitleNameString(",");
//        System.out.println(title);
        for (int i = 0; i < parentPartPathArray.length; i++) {
            parentPartPath = parentPartPathArray[i];

            taskPointList = PointRead.readPointWithFirstLineCount(basicPath + parentPartPath + taskPointFileName);
            workerPointList = PointRead.readPointWithFirstLineCount(basicPath + parentPartPath + workerPointFileName);


            // 4. 执行 non-privacy utility conflicts
            solutionName = "UtilityConflictNonPrivacySolution";
            ExtendedExperimentResult uConflictNonPrivacy = ConflictEliminationNonPrivacyCompleteRun.runningOnSingleDatasetWithUtilityConflictElimination(taskPointList, workerPointList, taskValueDefault, workerRangeDefault, proposalSize, dataType);
            PackExtendedExperimentResult uConflictNonPrivacyResult = new PackExtendedExperimentResult(datasetName, Integer.valueOf(dataType), false, solutionName, uConflictNonPrivacy);
            resultList.add(uConflictNonPrivacyResult.toString());
//            System.out.println(uConflictNonPrivacyResult);

            // 5. 执行 non-privacy distance conflicts
            solutionName = "DistanceConflictNonPrivacySolution";
            ExtendedExperimentResult dConflictNonPrivacy = ConflictEliminationNonPrivacyCompleteRun.runningOnSingleDatasetWithDistanceConflictElimination(taskPointList, workerPointList, taskValueDefault, workerRangeDefault, proposalSize, dataType);
            PackExtendedExperimentResult dConflictNonPrivacyResult = new PackExtendedExperimentResult(datasetName, Integer.valueOf(dataType), false, solutionName, dConflictNonPrivacy);
            resultList.add(dConflictNonPrivacyResult.toString());
//            System.out.println(dConflictNonPrivacyResult);


            // 6. 执行 non-privacy game iterator
            solutionName = "GameNonPrivacySolution";
            ExtendedExperimentResult gNonPrivacy = GameIterationNonPrivacyCompleteRun.runningOnSingleDataset(taskPointList, workerPointList, taskValueDefault, workerRangeDefault, dataType);
            PackExtendedExperimentResult gNonPrivacyResult = new PackExtendedExperimentResult(datasetName, Integer.valueOf(dataType), false, solutionName, gNonPrivacy);
            resultList.add(gNonPrivacyResult.toString());
//            System.out.println(gNonPrivacyResult);

            // 7. 执行 non-privacy greedy
            solutionName = "GreedyNonPrivacySolution";
            ExtendedExperimentResult greedyNonPrivacy = GreedyNonPrivacyCompleteRun.runningOnSingleDataset(taskPointList, workerPointList, taskValueDefault, workerRangeDefault, dataType);
            PackExtendedExperimentResult greedyNonPrivacyResult = new PackExtendedExperimentResult(datasetName, Integer.valueOf(dataType), false, solutionName, greedyNonPrivacy);
            resultList.add(greedyNonPrivacyResult.toString());
//            System.out.println(greedyNonPrivacyResult);


            System.out.println("Finish param worker ratio " + parentPartPath);


        }

        WriteExperimentResult writeExperimentResult = new WriteExperimentResult();
        writeExperimentResult.writeResultList(outputPath, title, resultList);
//        System.out.println(title);
//        MyPrint.showList(resultList, "\r\n");

    }
}
