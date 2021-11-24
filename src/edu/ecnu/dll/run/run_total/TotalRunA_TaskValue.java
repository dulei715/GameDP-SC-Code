package edu.ecnu.dll.run.run_total;

import edu.ecnu.dll.basic.basic_solution.Solution;
import edu.ecnu.dll.basic.basic_struct.pack.experiment_result_info.ExtendedExperimentResult;
import edu.ecnu.dll.basic.basic_struct.pack.experiment_result_info.NormalExperimentResult;
import edu.ecnu.dll.basic.basic_struct.pack.experiment_result_info.PackExtendedExperimentResult;
import edu.ecnu.dll.basic.basic_struct.pack.experiment_result_info.PackNormalExperimentResult;
import edu.ecnu.dll.basic.basic_struct.pack.single_agent_info.sub_class.WorkerIDNoDistanceUtilityNoiseDistanceBudgetPair;
import edu.ecnu.dll.run.run_main.AbstractRun;
import edu.ecnu.dll.run.run_main.ConflictEliminationCompleteRun;
import edu.ecnu.dll.scheme.scheme_compared.solution._1_non_privacy.DistanceConflictEliminationBasedNonPrivacySolution;
import edu.ecnu.dll.scheme.scheme_compared.solution._1_non_privacy.GameTheoryNonPrivacyBasedSolution;
import edu.ecnu.dll.scheme.scheme_compared.solution._1_non_privacy.GreedyNonePrivacySolution;
import edu.ecnu.dll.scheme.scheme_compared.solution._1_non_privacy.UtilityConflictEliminationBasedNonPrivacySolution;
import edu.ecnu.dll.scheme.scheme_main.solution._2_multiple_task.GameTheoryBasedSolution;
import edu.ecnu.dll.scheme.scheme_main.solution._2_multiple_task.NoiseDistanceConflictEliminationBasedSolution;
import edu.ecnu.dll.scheme.scheme_main.solution._2_multiple_task.UtilityConflictEliminationBasedSolution;
import tools.io.print.MyPrint;
import tools.io.read.PointRead;
import tools.io.read.TwoDimensionDoubleRead;
import tools.struct.Point;

import java.util.ArrayList;
import java.util.List;

public class TotalRunA_TaskValue {
    public static void main(String[] args) {

        String basicPath = "E:\\1.学习\\4.数据集\\dataset\\original\\chengdu_total_dataset";
        String taskPointFileName = "\\task_point.txt";
        String taskValueFileName = "\\task_value.txt";
        String workerPointFileName = "\\worker_point.txt";
        String workerBudgetFileName = "\\worker_budget.txt";
        String workerNoiseDistanceFileName = "\\worker_noise_distance.txt";

        /**
         * 基本设置
         *      1. f_1 参数 α 设置
         *      2. f_2 参数 β 设置
         *      3. worker申请task数量的最大值
         *      4. data type
         *      5. 数据集名称
         */
        Solution.alpha = 0.001;
        Solution.beta = 1;
        int proposalSize = Integer.MAX_VALUE;
        String dataType = AbstractRun.COORDINATE.toString();
        String datasetName = "ChengduDiDi";




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
        String parentPartPath = "\\task_worker_1_2_0";


        Double[] taskValueArray = new Double[] {
                5.0, 10.0, 15.0, 20.0, 25.0
        };


//        Double workerRangeDefault = 1.1;
        Double workerRangeDefault = 1100.0;


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
//        UtilityConflictEliminationBasedSolution uConflictSolution = new UtilityConflictEliminationBasedSolution();
//        NoiseDistanceConflictEliminationBasedSolution dConflictSolution = new NoiseDistanceConflictEliminationBasedSolution();
//        ConflictEliminationCompleteRun conflictRun = new ConflictEliminationCompleteRun();


        GameTheoryBasedSolution gameSolution = new GameTheoryBasedSolution();

        UtilityConflictEliminationBasedNonPrivacySolution uConflictNPSolution = new UtilityConflictEliminationBasedNonPrivacySolution();
        DistanceConflictEliminationBasedNonPrivacySolution dConflictNPSolution = new DistanceConflictEliminationBasedNonPrivacySolution();



        GameTheoryNonPrivacyBasedSolution gameNPSolution = new GameTheoryNonPrivacyBasedSolution();
        GreedyNonePrivacySolution greedyNPSolution = new GreedyNonePrivacySolution();


        // 读入基本数据
        List<Point> taskPointList = PointRead.readPointWithFirstLineCount(basicPath + parentPartPath + taskPointFileName);
        List<Point> workerPointList = PointRead.readPointWithFirstLineCount(basicPath + parentPartPath + workerPointFileName);

        // 读入隐私方案所需基本数据
        List<Double[]>[] workerPrivacyBudgetList = TwoDimensionDoubleRead.readDouble(basicPath + parentPartPath + workerBudgetFileName, 1);
        List<Double[]>[] workerNoiseDistanceList = TwoDimensionDoubleRead.readDouble(basicPath + parentPartPath + workerNoiseDistanceFileName, 1);



        Double taskValue;
        Boolean ppfState;
        ExtendedExperimentResult experimentResult;
        String solutionName;
        List<String> resultList = new ArrayList<>();
        System.out.println();
        String title = PackExtendedExperimentResult.getSelfTitle() + "," + ExtendedExperimentResult.getTitleNameString(",");
        System.out.println(title);
        for (int i = 0; i < taskValueArray.length; i++) {
            taskValue = taskValueArray[i];

            // 1. 执行 privacy utility conflicts(两种)
            solutionName = "UtilityConflictPrivacySolution";
            ppfState = false;
            ExtendedExperimentResult uConflictPrivacyNoPPCF = ConflictEliminationCompleteRun.runningOnSingleDatasetWithUtilityConflictElimination(taskPointList, workerPointList, workerPrivacyBudgetList, workerNoiseDistanceList, ppfState, taskValue, workerRangeDefault, proposalSize, dataType);
            PackExtendedExperimentResult uConflictPrivacyNoPPCFResult = new PackExtendedExperimentResult(datasetName, Integer.valueOf(dataType), ppfState, solutionName, uConflictPrivacyNoPPCF);
//            resultList.add(uConflictPrivacyNoPPCFResult.toString());
//            System.out.println(uConflictPrivacyNoPPCFResult.toString());
//            break;
//            System.out.println(uConflictPrivacyNoPPCF);
//            break;

            // 2. 执行 privacy distance conflicts(两种)


            // 3. 执行 privacy game iterate

            // 4. 执行 non-privacy utility conflicts
            // 5. 执行 non-privacy distance conflicts
            // 6. 执行 non-privacy game iterator
            // 7. 执行 non-privacy greedy





        }



    }
}
