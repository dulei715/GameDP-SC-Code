package edu.ecnu.dll.run.batch_run_total;

import edu.ecnu.dll.basic.basic_struct.pack.experiment_result_info.BatchAllKindsResult;
import edu.ecnu.dll.basic.basic_struct.pack.experiment_result_info.ExtendedExperimentResult;
import edu.ecnu.dll.basic.basic_struct.pack.experiment_result_info.Pack2ExtendedExperimentResult;
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

@Deprecated
public class BatchTotalRun {

    public static final Integer WORKER_RATIO = 1;
    public static final Integer TASK_VALUE = 2;
    public static final Integer WORKER_RANGE = 3;
    public static final Integer PRIVACY_BUDGET = 4;


    public static BatchAllKindsResult batchTotalRunByWorkerRatio(String basicPath, String dataType, String datasetName, String batchNumberString) {

        // todo: 修改数据集路径
//        String basicPath = "E:\\1.学习\\4.数据集\\dataset\\original\\chengdu_total_dataset_km";

        // todo: 修改数据集路径2
        String[] parentPartPathArray = Constant.parentPathArray;
        // todo: 修改数据集类型
//        String dataType = AbstractRun.COORDINATE.toString();
        // todo: 修改数据集名称
//        String datasetName = "ChengduDiDi";


        String taskPointFileName = Constant.FILE_PATH_SPLIT + "batch_" + batchNumberString + "_task_point.txt";
        String workerPointFileName = Constant.FILE_PATH_SPLIT + "batch_" + batchNumberString + "_worker_point.txt";
        String workerBudgetFileName = Constant.FILE_PATH_SPLIT + "batch_" + batchNumberString + "_worker_budget.txt";
        String workerNoiseDistanceFileName = Constant.FILE_PATH_SPLIT + "batch_" + batchNumberString + "_worker_noise_distance.txt";

//        String outputPath = basicPath + Constant.FILE_PATH_SPLIT + "outputPath" + Constant.FILE_PATH_SPLIT + "output_worker_ratio_change.csv";

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
        List<PackExtendedExperimentResult> resultList = new ArrayList<>();
//        System.out.println();
        String title = PackExtendedExperimentResult.getSelfTitle() + "," + ExtendedExperimentResult.getTitleNameString(",");
//        System.out.println(title);
        for (int i = 0; i < parentPartPathArray.length; i++) {
            parentPartPath = parentPartPathArray[i];

            taskPointList = PointRead.readPointWithFirstLineCount(basicPath + parentPartPath + taskPointFileName);
            workerPointList = PointRead.readPointWithFirstLineCount(basicPath + parentPartPath + workerPointFileName);
            workerPrivacyBudgetList = TwoDimensionDoubleRead.readDouble(basicPath + parentPartPath + workerBudgetFileName, 1);
            workerNoiseDistanceList = TwoDimensionDoubleRead.readDouble(basicPath + parentPartPath + workerNoiseDistanceFileName, 1);

            // 1. 执行 privacy utility conflicts(两种)
            solutionName = "UtilityConflictPrivacySolution";
            ppfState = false;
            ExtendedExperimentResult uConflictPrivacyNoPPCF = ConflictEliminationCompleteRun.runningOnSingleDatasetWithUtilityConflictElimination(taskPointList, workerPointList, workerPrivacyBudgetList, workerNoiseDistanceList, ppfState, taskValueDefault, workerRangeDefault, proposalSize, dataType);
            PackExtendedExperimentResult uConflictPrivacyNoPPCFResult = new PackExtendedExperimentResult(datasetName, Integer.valueOf(dataType), ppfState, solutionName, uConflictPrivacyNoPPCF);
            resultList.add(uConflictPrivacyNoPPCFResult);
//            System.out.println(uConflictPrivacyNoPPCFResult.toString());

            ppfState = true;
            ExtendedExperimentResult uConflictPrivacyPPCF = ConflictEliminationCompleteRun.runningOnSingleDatasetWithUtilityConflictElimination(taskPointList, workerPointList, workerPrivacyBudgetList, workerNoiseDistanceList, ppfState, taskValueDefault, workerRangeDefault, proposalSize, dataType);
            PackExtendedExperimentResult uConflictPrivacyPPCFResult = new PackExtendedExperimentResult(datasetName, Integer.valueOf(dataType), ppfState, solutionName, uConflictPrivacyPPCF);
            resultList.add(uConflictPrivacyPPCFResult);
//            System.out.println(uConflictPrivacyPPCFResult);



            // 2. 执行 privacy distance conflicts(两种)

            solutionName = "DistanceConflictPrivacySolution";
            ppfState = false;
            ExtendedExperimentResult dConflictPrivacyNoPPCF = ConflictEliminationCompleteRun.runningOnSingleDatasetWithDistanceConflictElimination(taskPointList, workerPointList, workerPrivacyBudgetList, workerNoiseDistanceList, ppfState, taskValueDefault, workerRangeDefault, proposalSize, dataType);
            PackExtendedExperimentResult dConflictPrivacyNoPPCResult = new PackExtendedExperimentResult(datasetName, Integer.valueOf(dataType), ppfState, solutionName, dConflictPrivacyNoPPCF);
            resultList.add(dConflictPrivacyNoPPCResult);
//            System.out.println(dConflictPrivacyNoPPCResult);


            ppfState = true;
            ExtendedExperimentResult dConflictPrivacyPPCF = ConflictEliminationCompleteRun.runningOnSingleDatasetWithDistanceConflictElimination(taskPointList, workerPointList, workerPrivacyBudgetList, workerNoiseDistanceList, ppfState, taskValueDefault, workerRangeDefault, proposalSize, dataType);
            PackExtendedExperimentResult dConflictPrivacyPPCResult = new PackExtendedExperimentResult(datasetName, Integer.valueOf(dataType), ppfState, solutionName, dConflictPrivacyPPCF);
            resultList.add(dConflictPrivacyPPCResult);
//            System.out.println(dConflictPrivacyPPCResult);



            // 3. 执行 privacy game iterate
            solutionName = "GamePrivacySolution";
            ExtendedExperimentResult gPrivacy = GameIterationCompleteRun.runningOnSingleDataset(taskPointList, workerPointList, workerPrivacyBudgetList, workerNoiseDistanceList, taskValueDefault, workerRangeDefault, dataType);
            PackExtendedExperimentResult gPrivacyResult = new PackExtendedExperimentResult(datasetName, Integer.valueOf(dataType), false, solutionName, gPrivacy);
            resultList.add(gPrivacyResult);
//            System.out.println(gPrivacyResult);



            // 4. 执行 non-privacy utility conflicts
            solutionName = "UtilityConflictNonPrivacySolution";
            ExtendedExperimentResult uConflictNonPrivacy = ConflictEliminationNonPrivacyCompleteRun.runningOnSingleDatasetWithUtilityConflictElimination(taskPointList, workerPointList, taskValueDefault, workerRangeDefault, proposalSize, dataType);
            PackExtendedExperimentResult uConflictNonPrivacyResult = new PackExtendedExperimentResult(datasetName, Integer.valueOf(dataType), false, solutionName, uConflictNonPrivacy);
            resultList.add(uConflictNonPrivacyResult);
//            System.out.println(uConflictNonPrivacyResult);

            // 5. 执行 non-privacy distance conflicts
            solutionName = "DistanceConflictNonPrivacySolution";
            ExtendedExperimentResult dConflictNonPrivacy = ConflictEliminationNonPrivacyCompleteRun.runningOnSingleDatasetWithDistanceConflictElimination(taskPointList, workerPointList, taskValueDefault, workerRangeDefault, proposalSize, dataType);
            PackExtendedExperimentResult dConflictNonPrivacyResult = new PackExtendedExperimentResult(datasetName, Integer.valueOf(dataType), false, solutionName, dConflictNonPrivacy);
            resultList.add(dConflictNonPrivacyResult);
//            System.out.println(dConflictNonPrivacyResult);


            // 6. 执行 non-privacy game iterator
            solutionName = "GameNonPrivacySolution";
            ExtendedExperimentResult gNonPrivacy = GameIterationNonPrivacyCompleteRun.runningOnSingleDataset(taskPointList, workerPointList, taskValueDefault, workerRangeDefault, dataType);
            PackExtendedExperimentResult gNonPrivacyResult = new PackExtendedExperimentResult(datasetName, Integer.valueOf(dataType), false, solutionName, gNonPrivacy);
            resultList.add(gNonPrivacyResult);
//            System.out.println(gNonPrivacyResult);

            // 7. 执行 non-privacy greedy
            solutionName = "GreedyNonPrivacySolution";
            ExtendedExperimentResult greedyNonPrivacy = GreedyNonPrivacyCompleteRun.runningOnSingleDataset(taskPointList, workerPointList, taskValueDefault, workerRangeDefault, dataType);
            PackExtendedExperimentResult greedyNonPrivacyResult = new PackExtendedExperimentResult(datasetName, Integer.valueOf(dataType), false, solutionName, greedyNonPrivacy);
            resultList.add(greedyNonPrivacyResult);
//            System.out.println(greedyNonPrivacyResult);


            System.out.println("Finish param worker ratio " + parentPartPath);


        }

//        WriteExperimentResult writeExperimentResult = new WriteExperimentResult();
//        writeExperimentResult.writeResultList(outputPath, title, resultList);
        return new BatchAllKindsResult(title, resultList);
    }

    public static BatchAllKindsResult batchTotalRunByTaskValue(String basicPath, String dataType, String datasetName, String batchNumberString) {

        // todo: 修改数据集路径
//        String basicPath = "E:\\1.学习\\4.数据集\\dataset\\original\\chengdu_total_dataset_km";
//        String basicPath = "E:\\1.学习\\4.数据集\\1.FourSquare-NYCandTokyoCheck-ins\\output\\SYN";
        // todo: 修改数据集路径2
        String parentPartPath = Constant.parentPathDefault;
        // todo: 修改数据集类型
//        String dataType = AbstractRun.COORDINATE.toString();
//        String dataType = AbstractRun.LONGITUDE_LATITUDE.toString();
        // todo: 修改数据集名称
//        String datasetName = "ChengduDiDi";
//        String datasetName = "SYN";


        String taskPointFileName = Constant.FILE_PATH_SPLIT + "batch_" + batchNumberString + "_task_point.txt";
        String workerPointFileName = Constant.FILE_PATH_SPLIT + "batch_" + batchNumberString + "_worker_point.txt";
        String workerBudgetFileName = Constant.FILE_PATH_SPLIT + "batch_" + batchNumberString + "_worker_budget.txt";
        String workerNoiseDistanceFileName = Constant.FILE_PATH_SPLIT + "batch_" + batchNumberString + "_worker_noise_distance.txt";

//        String outputPath = basicPath + Constant.FILE_PATH_SPLIT + "outputPath" + Constant.FILE_PATH_SPLIT + "output_task_value_change.csv";
//        String outputParentPath = basicPath + Constant.FILE_PATH_SPLIT + "outputPath" + Constant.FILE_PATH_SPLIT + "_" + batchNumberString + "_";

        /**
         * 基本设置
         *      1. f_1 参数 α 设置
         *      2. f_2 参数 β 设置
         *      3. worker申请task数量的最大值
         *      4. data type
         *      5. 数据集名称
         */
        // todo: 修改效用函数参数: 在Constant类里


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




        Double[] taskValueArray = Constant.taskValueArray;


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
        List<Point> taskPointList = PointRead.readPointWithFirstLineCount(basicPath + parentPartPath + taskPointFileName);
        List<Point> workerPointList = PointRead.readPointWithFirstLineCount(basicPath + parentPartPath + workerPointFileName);

        // 读入隐私方案所需基本数据
        List<Double[]>[] workerPrivacyBudgetList = TwoDimensionDoubleRead.readDouble(basicPath + parentPartPath + workerBudgetFileName, 1);
        List<Double[]>[] workerNoiseDistanceList = TwoDimensionDoubleRead.readDouble(basicPath + parentPartPath + workerNoiseDistanceFileName, 1);



        Double taskValue;
        Boolean ppfState;
//        ExtendedExperimentResult experimentResult;
        String solutionName;
        List<PackExtendedExperimentResult> resultList = new ArrayList<>();

        String title = PackExtendedExperimentResult.getSelfTitle() + "," + ExtendedExperimentResult.getTitleNameString(",");
//        System.out.println(title);
        for (int i = 0; i < taskValueArray.length; i++) {
            taskValue = taskValueArray[i];

            // 1. 执行 privacy utility conflicts(两种)
            solutionName = "UtilityConflictPrivacySolution";
            ppfState = false;
            ExtendedExperimentResult uConflictPrivacyNoPPCF = ConflictEliminationCompleteRun.runningOnSingleDatasetWithUtilityConflictElimination(taskPointList, workerPointList, workerPrivacyBudgetList, workerNoiseDistanceList, ppfState, taskValue, workerRangeDefault, proposalSize, dataType);
            PackExtendedExperimentResult uConflictPrivacyNoPPCFResult = new PackExtendedExperimentResult(datasetName, Integer.valueOf(dataType), ppfState, solutionName, uConflictPrivacyNoPPCF);
            resultList.add(uConflictPrivacyNoPPCFResult);
//            System.out.println(uConflictPrivacyNoPPCFResult);

            ppfState = true;
            ExtendedExperimentResult uConflictPrivacyPPCF = ConflictEliminationCompleteRun.runningOnSingleDatasetWithUtilityConflictElimination(taskPointList, workerPointList, workerPrivacyBudgetList, workerNoiseDistanceList, ppfState, taskValue, workerRangeDefault, proposalSize, dataType);
            PackExtendedExperimentResult uConflictPrivacyPPCFResult = new PackExtendedExperimentResult(datasetName, Integer.valueOf(dataType), ppfState, solutionName, uConflictPrivacyPPCF);
            resultList.add(uConflictPrivacyPPCFResult);
//            System.out.println(uConflictPrivacyPPCFResult);



            // 2. 执行 privacy distance conflicts(两种)

            solutionName = "DistanceConflictPrivacySolution";
            ppfState = false;
            ExtendedExperimentResult dConflictPrivacyNoPPCF = ConflictEliminationCompleteRun.runningOnSingleDatasetWithDistanceConflictElimination(taskPointList, workerPointList, workerPrivacyBudgetList, workerNoiseDistanceList, ppfState, taskValue, workerRangeDefault, proposalSize, dataType);
            PackExtendedExperimentResult dConflictPrivacyNoPPCResult = new PackExtendedExperimentResult(datasetName, Integer.valueOf(dataType), ppfState, solutionName, dConflictPrivacyNoPPCF);
            resultList.add(dConflictPrivacyNoPPCResult);
//            System.out.println(dConflictPrivacyNoPPCResult);


            ppfState = true;
            ExtendedExperimentResult dConflictPrivacyPPCF = ConflictEliminationCompleteRun.runningOnSingleDatasetWithDistanceConflictElimination(taskPointList, workerPointList, workerPrivacyBudgetList, workerNoiseDistanceList, ppfState, taskValue, workerRangeDefault, proposalSize, dataType);
            PackExtendedExperimentResult dConflictPrivacyPPCResult = new PackExtendedExperimentResult(datasetName, Integer.valueOf(dataType), ppfState, solutionName, dConflictPrivacyPPCF);
            resultList.add(dConflictPrivacyPPCResult);
//            System.out.println(dConflictPrivacyPPCResult);



            // 3. 执行 privacy game iterate
            solutionName = "GamePrivacySolution";
            ExtendedExperimentResult gPrivacy = GameIterationCompleteRun.runningOnSingleDataset(taskPointList, workerPointList, workerPrivacyBudgetList, workerNoiseDistanceList, taskValue, workerRangeDefault, dataType);
            PackExtendedExperimentResult gPrivacyResult = new PackExtendedExperimentResult(datasetName, Integer.valueOf(dataType), false, solutionName, gPrivacy);
            resultList.add(gPrivacyResult);
//            System.out.println(gPrivacyResult);



            // 4. 执行 non-privacy utility conflicts
            solutionName = "UtilityConflictNonPrivacySolution";
            ExtendedExperimentResult uConflictNonPrivacy = ConflictEliminationNonPrivacyCompleteRun.runningOnSingleDatasetWithUtilityConflictElimination(taskPointList, workerPointList, taskValue, workerRangeDefault, proposalSize, dataType);
            PackExtendedExperimentResult uConflictNonPrivacyResult = new PackExtendedExperimentResult(datasetName, Integer.valueOf(dataType), false, solutionName, uConflictNonPrivacy);
            resultList.add(uConflictNonPrivacyResult);
//            System.out.println(uConflictNonPrivacyResult);

            // 5. 执行 non-privacy distance conflicts
            solutionName = "DistanceConflictNonPrivacySolution";
            ExtendedExperimentResult dConflictNonPrivacy = ConflictEliminationNonPrivacyCompleteRun.runningOnSingleDatasetWithDistanceConflictElimination(taskPointList, workerPointList, taskValue, workerRangeDefault, proposalSize, dataType);
            PackExtendedExperimentResult dConflictNonPrivacyResult = new PackExtendedExperimentResult(datasetName, Integer.valueOf(dataType), false, solutionName, dConflictNonPrivacy);
            resultList.add(dConflictNonPrivacyResult);
//            System.out.println(dConflictNonPrivacyResult);


            // 6. 执行 non-privacy game iterator
            solutionName = "GameNonPrivacySolution";
            ExtendedExperimentResult gNonPrivacy = GameIterationNonPrivacyCompleteRun.runningOnSingleDataset(taskPointList, workerPointList, taskValue, workerRangeDefault, dataType);
            PackExtendedExperimentResult gNonPrivacyResult = new PackExtendedExperimentResult(datasetName, Integer.valueOf(dataType), false, solutionName, gNonPrivacy);
            resultList.add(gNonPrivacyResult);
//            System.out.println(gNonPrivacyResult);

            // 7. 执行 non-privacy greedy
            solutionName = "GreedyNonPrivacySolution";
            ExtendedExperimentResult greedyNonPrivacy = GreedyNonPrivacyCompleteRun.runningOnSingleDataset(taskPointList, workerPointList, taskValue, workerRangeDefault, dataType);
            PackExtendedExperimentResult greedyNonPrivacyResult = new PackExtendedExperimentResult(datasetName, Integer.valueOf(dataType), false, solutionName, greedyNonPrivacy);
            resultList.add(greedyNonPrivacyResult);
//            System.out.println(greedyNonPrivacyResult);


            System.out.println("Finish param task value " + taskValue);


        }

//        WriteExperimentResult writeExperimentResult = new WriteExperimentResult();
//        writeExperimentResult.writeResultList(outputPath, title, resultList);
        return new BatchAllKindsResult(title, resultList);
    }

    public static BatchAllKindsResult batchTotalRunByWorkerRange(String basicPath, String dataType, String datasetName, String batchNumberString) {

        // todo: 修改数据集路径
//        String basicPath = "E:\\1.学习\\4.数据集\\dataset\\original\\chengdu_total_dataset_km";
        // todo: 修改数据集路径2
        String parentPartPath = Constant.parentPathDefault;
        // todo: 修改数据集类型
//        String dataType = AbstractRun.COORDINATE.toString();
        // todo: 修改数据集名称
//        String datasetName = "ChengduDiDi";




        String taskPointFileName = Constant.FILE_PATH_SPLIT + "batch_" + batchNumberString + "_task_point.txt";
        String workerPointFileName = Constant.FILE_PATH_SPLIT + "batch_" + batchNumberString + "_worker_point.txt";
        String workerBudgetFileName = Constant.FILE_PATH_SPLIT + "batch_" + batchNumberString + "_worker_budget.txt";
        String workerNoiseDistanceFileName = Constant.FILE_PATH_SPLIT + "batch_" + batchNumberString + "_worker_noise_distance.txt";

        String outputPath = basicPath + Constant.FILE_PATH_SPLIT + "outputPath" + Constant.FILE_PATH_SPLIT + "output_worker_range_change.csv";

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


        Double[] workerRangeArray = Constant.workerRangeArray;


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
        List<Point> taskPointList = PointRead.readPointWithFirstLineCount(basicPath + parentPartPath + taskPointFileName);
        List<Point> workerPointList = PointRead.readPointWithFirstLineCount(basicPath + parentPartPath + workerPointFileName);

        // 读入隐私方案所需基本数据
        List<Double[]>[] workerPrivacyBudgetList = TwoDimensionDoubleRead.readDouble(basicPath + parentPartPath + workerBudgetFileName, 1);
        List<Double[]>[] workerNoiseDistanceList = TwoDimensionDoubleRead.readDouble(basicPath + parentPartPath + workerNoiseDistanceFileName, 1);



        Double workerRange;
        Boolean ppfState;
        ExtendedExperimentResult experimentResult;
        String solutionName;
        List<PackExtendedExperimentResult> resultList = new ArrayList<>();

        String title = PackExtendedExperimentResult.getSelfTitle() + "," + ExtendedExperimentResult.getTitleNameString(",");
//        System.out.println(title);
        for (int i = 0; i < workerRangeArray.length; i++) {
//            taskValue = taskValueArray[i];
            workerRange = workerRangeArray[i];

            // 1. 执行 privacy utility conflicts(两种)
            solutionName = "UtilityConflictPrivacySolution";
            ppfState = false;
            ExtendedExperimentResult uConflictPrivacyNoPPCF = ConflictEliminationCompleteRun.runningOnSingleDatasetWithUtilityConflictElimination(taskPointList, workerPointList, workerPrivacyBudgetList, workerNoiseDistanceList, ppfState, taskValueDefault, workerRange, proposalSize, dataType);
            PackExtendedExperimentResult uConflictPrivacyNoPPCFResult = new PackExtendedExperimentResult(datasetName, Integer.valueOf(dataType), ppfState, solutionName, uConflictPrivacyNoPPCF);
            resultList.add(uConflictPrivacyNoPPCFResult);
//            System.out.println(uConflictPrivacyNoPPCFResult);

            ppfState = true;
            ExtendedExperimentResult uConflictPrivacyPPCF = ConflictEliminationCompleteRun.runningOnSingleDatasetWithUtilityConflictElimination(taskPointList, workerPointList, workerPrivacyBudgetList, workerNoiseDistanceList, ppfState, taskValueDefault, workerRange, proposalSize, dataType);
            PackExtendedExperimentResult uConflictPrivacyPPCFResult = new PackExtendedExperimentResult(datasetName, Integer.valueOf(dataType), ppfState, solutionName, uConflictPrivacyPPCF);
            resultList.add(uConflictPrivacyPPCFResult);
//            System.out.println(uConflictPrivacyPPCFResult);



            // 2. 执行 privacy distance conflicts(两种)

            solutionName = "DistanceConflictPrivacySolution";
            ppfState = false;
            ExtendedExperimentResult dConflictPrivacyNoPPCF = ConflictEliminationCompleteRun.runningOnSingleDatasetWithDistanceConflictElimination(taskPointList, workerPointList, workerPrivacyBudgetList, workerNoiseDistanceList, ppfState, taskValueDefault, workerRange, proposalSize, dataType);
            PackExtendedExperimentResult dConflictPrivacyNoPPCResult = new PackExtendedExperimentResult(datasetName, Integer.valueOf(dataType), ppfState, solutionName, dConflictPrivacyNoPPCF);
            resultList.add(dConflictPrivacyNoPPCResult);
//            System.out.println(dConflictPrivacyNoPPCResult);


            ppfState = true;
            ExtendedExperimentResult dConflictPrivacyPPCF = ConflictEliminationCompleteRun.runningOnSingleDatasetWithDistanceConflictElimination(taskPointList, workerPointList, workerPrivacyBudgetList, workerNoiseDistanceList, ppfState, taskValueDefault, workerRange, proposalSize, dataType);
            PackExtendedExperimentResult dConflictPrivacyPPCResult = new PackExtendedExperimentResult(datasetName, Integer.valueOf(dataType), ppfState, solutionName, dConflictPrivacyPPCF);
            resultList.add(dConflictPrivacyPPCResult);
//            System.out.println(dConflictPrivacyPPCResult);



            // 3. 执行 privacy game iterate
            solutionName = "GamePrivacySolution";
            ExtendedExperimentResult gPrivacy = GameIterationCompleteRun.runningOnSingleDataset(taskPointList, workerPointList, workerPrivacyBudgetList, workerNoiseDistanceList, taskValueDefault, workerRange, dataType);
            PackExtendedExperimentResult gPrivacyResult = new PackExtendedExperimentResult(datasetName, Integer.valueOf(dataType), false, solutionName, gPrivacy);
            resultList.add(gPrivacyResult);
//            System.out.println(gPrivacyResult);



            // 4. 执行 non-privacy utility conflicts
            solutionName = "UtilityConflictNonPrivacySolution";
            ExtendedExperimentResult uConflictNonPrivacy = ConflictEliminationNonPrivacyCompleteRun.runningOnSingleDatasetWithUtilityConflictElimination(taskPointList, workerPointList, taskValueDefault, workerRange, proposalSize, dataType);
            PackExtendedExperimentResult uConflictNonPrivacyResult = new PackExtendedExperimentResult(datasetName, Integer.valueOf(dataType), false, solutionName, uConflictNonPrivacy);
            resultList.add(uConflictNonPrivacyResult);
//            System.out.println(uConflictNonPrivacyResult);

            // 5. 执行 non-privacy distance conflicts
            solutionName = "DistanceConflictNonPrivacySolution";
            ExtendedExperimentResult dConflictNonPrivacy = ConflictEliminationNonPrivacyCompleteRun.runningOnSingleDatasetWithDistanceConflictElimination(taskPointList, workerPointList, taskValueDefault, workerRange, proposalSize, dataType);
            PackExtendedExperimentResult dConflictNonPrivacyResult = new PackExtendedExperimentResult(datasetName, Integer.valueOf(dataType), false, solutionName, dConflictNonPrivacy);
            resultList.add(dConflictNonPrivacyResult);
//            System.out.println(dConflictNonPrivacyResult);


            // 6. 执行 non-privacy game iterator
            solutionName = "GameNonPrivacySolution";
            ExtendedExperimentResult gNonPrivacy = GameIterationNonPrivacyCompleteRun.runningOnSingleDataset(taskPointList, workerPointList, taskValueDefault, workerRange, dataType);
            PackExtendedExperimentResult gNonPrivacyResult = new PackExtendedExperimentResult(datasetName, Integer.valueOf(dataType), false, solutionName, gNonPrivacy);
            resultList.add(gNonPrivacyResult);
//            System.out.println(gNonPrivacyResult);

            // 7. 执行 non-privacy greedy
            solutionName = "GreedyNonPrivacySolution";
            ExtendedExperimentResult greedyNonPrivacy = GreedyNonPrivacyCompleteRun.runningOnSingleDataset(taskPointList, workerPointList, taskValueDefault, workerRange, dataType);
            PackExtendedExperimentResult greedyNonPrivacyResult = new PackExtendedExperimentResult(datasetName, Integer.valueOf(dataType), false, solutionName, greedyNonPrivacy);
            resultList.add(greedyNonPrivacyResult);
//            System.out.println(greedyNonPrivacyResult);


//            MyPrint.showSplitLine("*", 100);
            System.out.println("Finish param worker range " + workerRange);


        }

//        WriteExperimentResult writeExperimentResult = new WriteExperimentResult();
//        writeExperimentResult.writeResultList(outputPath, title, resultList);
        return new BatchAllKindsResult(title, resultList);
    }

    public static BatchAllKindsResult batchTotalRunByPrivacyBudget(String basicPath, String dataType, String datasetName, String batchNumberString) {

        // todo: 修改数据集路径
//        String basicPath = "E:\\1.学习\\4.数据集\\dataset\\original\\chengdu_total_dataset_km";
        // todo: 修改数据集路径2
        String[] parentPartPathArray = Constant.parentBudgetPathArray;
        double[][] parentPartBudgetArray = Constant.parentBudgetRange;
        // todo: 修改数据集类型
//        String dataType = AbstractRun.COORDINATE.toString();
        // todo: 修改数据集名称
//        String datasetName = "ChengduDiDi";


        String taskPointFileName = Constant.FILE_PATH_SPLIT + "batch_" + batchNumberString + "_task_point.txt";
        String workerPointFileName = Constant.FILE_PATH_SPLIT + "batch_" + batchNumberString + "_worker_point.txt";
        String workerBudgetFileName = Constant.FILE_PATH_SPLIT + "batch_" + batchNumberString + "_worker_budget.txt";
        String workerNoiseDistanceFileName = Constant.FILE_PATH_SPLIT + "batch_" + batchNumberString + "_worker_noise_distance.txt";


//        String outputPath = basicPath + Constant.FILE_PATH_SPLIT + "outputPath" + Constant.FILE_PATH_SPLIT + "output_worker_budget_change.csv";

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




        Double taskValueDefault = Constant.taskValueDefault;


        Double workerRangeDefault = Constant.workerRangeDefault;

        Double workerRatioDefault = 2.0;


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
        List<PackExtendedExperimentResult> resultList = new ArrayList<>();

        String title = PackExtendedExperimentResult.getSelfTitle() + "," + ExtendedExperimentResult.getTitleNameString(",") + "," + Pack2ExtendedExperimentResult.getAddingTitle();
//        System.out.println(title);
        for (int i = 0; i < parentPartPathArray.length; i++) {
            parentPartPath = parentPartPathArray[i];

            taskPointList = PointRead.readPointWithFirstLineCount(basicPath + parentPartPath + taskPointFileName);
            workerPointList = PointRead.readPointWithFirstLineCount(basicPath + parentPartPath + workerPointFileName);
            workerPrivacyBudgetList = TwoDimensionDoubleRead.readDouble(basicPath + parentPartPath + workerBudgetFileName, 1);
            workerNoiseDistanceList = TwoDimensionDoubleRead.readDouble(basicPath + parentPartPath + workerNoiseDistanceFileName, 1);

            // 1. 执行 privacy utility conflicts(两种)
            solutionName = "UtilityConflictPrivacySolution";
            ppfState = false;
            ExtendedExperimentResult uConflictPrivacyNoPPCF = ConflictEliminationCompleteRun.runningOnSingleDatasetWithUtilityConflictElimination(taskPointList, workerPointList, workerPrivacyBudgetList, workerNoiseDistanceList, ppfState, taskValueDefault, workerRangeDefault, proposalSize, dataType);
            PackExtendedExperimentResult uConflictPrivacyNoPPCFResult = new PackExtendedExperimentResult(datasetName, Integer.valueOf(dataType), ppfState, solutionName, uConflictPrivacyNoPPCF);
            Pack2ExtendedExperimentResult uConflictPrivacyNoPPCFResult2 = new Pack2ExtendedExperimentResult(uConflictPrivacyNoPPCFResult, parentPartBudgetArray[i][0], parentPartBudgetArray[i][1]);
            resultList.add(uConflictPrivacyNoPPCFResult2);
//            resultList.add(uConflictPrivacyNoPPCFResult.toString() + "," + parentPartBudgetArray[i][0] + "," + parentPartBudgetArray[i][1]);
//            System.out.println(uConflictPrivacyNoPPCFResult.toString());

            ppfState = true;
            ExtendedExperimentResult uConflictPrivacyPPCF = ConflictEliminationCompleteRun.runningOnSingleDatasetWithUtilityConflictElimination(taskPointList, workerPointList, workerPrivacyBudgetList, workerNoiseDistanceList, ppfState, taskValueDefault, workerRangeDefault, proposalSize, dataType);
            PackExtendedExperimentResult uConflictPrivacyPPCFResult = new PackExtendedExperimentResult(datasetName, Integer.valueOf(dataType), ppfState, solutionName, uConflictPrivacyPPCF);
            Pack2ExtendedExperimentResult uConflictPrivacyPPCFResult2 = new Pack2ExtendedExperimentResult(uConflictPrivacyPPCFResult, parentPartBudgetArray[i][0], parentPartBudgetArray[i][1]);
            resultList.add(uConflictPrivacyPPCFResult2);
//            resultList.add(uConflictPrivacyPPCFResult.toString() + "," + parentPartBudgetArray[i][0] + "," + parentPartBudgetArray[i][1]);
//            System.out.println(uConflictPrivacyPPCFResult);



            // 2. 执行 privacy distance conflicts(两种)

            solutionName = "DistanceConflictPrivacySolution";
            ppfState = false;
            ExtendedExperimentResult dConflictPrivacyNoPPCF = ConflictEliminationCompleteRun.runningOnSingleDatasetWithDistanceConflictElimination(taskPointList, workerPointList, workerPrivacyBudgetList, workerNoiseDistanceList, ppfState, taskValueDefault, workerRangeDefault, proposalSize, dataType);
            PackExtendedExperimentResult dConflictPrivacyNoPPCResult = new PackExtendedExperimentResult(datasetName, Integer.valueOf(dataType), ppfState, solutionName, dConflictPrivacyNoPPCF);
            Pack2ExtendedExperimentResult dConflictPrivacyNoPPCResult2 = new Pack2ExtendedExperimentResult(dConflictPrivacyNoPPCResult, parentPartBudgetArray[i][0], parentPartBudgetArray[i][1]);
            resultList.add(dConflictPrivacyNoPPCResult2);
//            System.out.println(dConflictPrivacyNoPPCResult);


            ppfState = true;
            ExtendedExperimentResult dConflictPrivacyPPCF = ConflictEliminationCompleteRun.runningOnSingleDatasetWithDistanceConflictElimination(taskPointList, workerPointList, workerPrivacyBudgetList, workerNoiseDistanceList, ppfState, taskValueDefault, workerRangeDefault, proposalSize, dataType);
            PackExtendedExperimentResult dConflictPrivacyPPCResult = new PackExtendedExperimentResult(datasetName, Integer.valueOf(dataType), ppfState, solutionName, dConflictPrivacyPPCF);
            Pack2ExtendedExperimentResult dConflictPrivacyPPCResult2 = new Pack2ExtendedExperimentResult(dConflictPrivacyPPCResult, parentPartBudgetArray[i][0], parentPartBudgetArray[i][1]);
            resultList.add(dConflictPrivacyPPCResult2);
//            System.out.println(dConflictPrivacyPPCResult);



            // 3. 执行 privacy game iterate
            solutionName = "GamePrivacySolution";
            ExtendedExperimentResult gPrivacy = GameIterationCompleteRun.runningOnSingleDataset(taskPointList, workerPointList, workerPrivacyBudgetList, workerNoiseDistanceList, taskValueDefault, workerRangeDefault, dataType);
            PackExtendedExperimentResult gPrivacyResult = new PackExtendedExperimentResult(datasetName, Integer.valueOf(dataType), false, solutionName, gPrivacy);
            Pack2ExtendedExperimentResult gPrivacyResult2 = new Pack2ExtendedExperimentResult(gPrivacyResult, parentPartBudgetArray[i][0], parentPartBudgetArray[i][1]);
            resultList.add(gPrivacyResult2);
//            System.out.println(gPrivacyResult);



            // 4. 执行 non-privacy utility conflicts
            solutionName = "UtilityConflictNonPrivacySolution";
            ExtendedExperimentResult uConflictNonPrivacy = ConflictEliminationNonPrivacyCompleteRun.runningOnSingleDatasetWithUtilityConflictElimination(taskPointList, workerPointList, taskValueDefault, workerRangeDefault, proposalSize, dataType);
            PackExtendedExperimentResult uConflictNonPrivacyResult = new PackExtendedExperimentResult(datasetName, Integer.valueOf(dataType), false, solutionName, uConflictNonPrivacy);
            Pack2ExtendedExperimentResult uConflictNonPrivacyResult2 = new Pack2ExtendedExperimentResult(uConflictNonPrivacyResult, parentPartBudgetArray[i][0], parentPartBudgetArray[i][1]);
            resultList.add(uConflictNonPrivacyResult2);
//            System.out.println(uConflictNonPrivacyResult);

            // 5. 执行 non-privacy distance conflicts
            solutionName = "DistanceConflictNonPrivacySolution";
            ExtendedExperimentResult dConflictNonPrivacy = ConflictEliminationNonPrivacyCompleteRun.runningOnSingleDatasetWithDistanceConflictElimination(taskPointList, workerPointList, taskValueDefault, workerRangeDefault, proposalSize, dataType);
            PackExtendedExperimentResult dConflictNonPrivacyResult = new PackExtendedExperimentResult(datasetName, Integer.valueOf(dataType), false, solutionName, dConflictNonPrivacy);
            Pack2ExtendedExperimentResult dConflictNonPrivacyResult2 = new Pack2ExtendedExperimentResult(dConflictNonPrivacyResult, parentPartBudgetArray[i][0], parentPartBudgetArray[i][1]);
            resultList.add(dConflictNonPrivacyResult2);
//            System.out.println(dConflictNonPrivacyResult);


            // 6. 执行 non-privacy game iterator
            solutionName = "GameNonPrivacySolution";
            ExtendedExperimentResult gNonPrivacy = GameIterationNonPrivacyCompleteRun.runningOnSingleDataset(taskPointList, workerPointList, taskValueDefault, workerRangeDefault, dataType);
            PackExtendedExperimentResult gNonPrivacyResult = new PackExtendedExperimentResult(datasetName, Integer.valueOf(dataType), false, solutionName, gNonPrivacy);
            Pack2ExtendedExperimentResult gNonPrivacyResult2 = new Pack2ExtendedExperimentResult(gNonPrivacyResult, parentPartBudgetArray[i][0], parentPartBudgetArray[i][1]);
            resultList.add(gNonPrivacyResult2);
//            System.out.println(gNonPrivacyResult);

            // 7. 执行 non-privacy greedy
            solutionName = "GreedyNonPrivacySolution";
            ExtendedExperimentResult greedyNonPrivacy = GreedyNonPrivacyCompleteRun.runningOnSingleDataset(taskPointList, workerPointList, taskValueDefault, workerRangeDefault, dataType);
            PackExtendedExperimentResult greedyNonPrivacyResult = new PackExtendedExperimentResult(datasetName, Integer.valueOf(dataType), false, solutionName, greedyNonPrivacy);
            Pack2ExtendedExperimentResult greedyNonPrivacyResult2 = new Pack2ExtendedExperimentResult(greedyNonPrivacyResult, parentPartBudgetArray[i][0], parentPartBudgetArray[i][1]);
            resultList.add(greedyNonPrivacyResult2);
//            System.out.println(greedyNonPrivacyResult);


            System.out.println("Finish param privacy budget " + parentPartPath);


        }

//        WriteExperimentResult writeExperimentResult = new WriteExperimentResult();
//        writeExperimentResult.writeResultList(outputPath, title, resultList);
        return new BatchAllKindsResult(title, resultList);

    }


}
