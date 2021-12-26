package edu.ecnu.dll.scheme.scheme_compared.solution._1_non_privacy;

import edu.ecnu.dll.basic.basic_solution.NonPrivacySolution;
import edu.ecnu.dll.basic.basic_solution.PrivacySolution;
import edu.ecnu.dll.basic.basic_struct.comparator.TargetInfoComparator;
import edu.ecnu.dll.basic.basic_struct.pack.TaskWorkerIDSuccessfulValuationPair;
import edu.ecnu.dll.basic.basic_struct.pack.experiment_result_info.BasicExperimentResult;
import edu.ecnu.dll.basic.basic_struct.pack.experiment_result_info.ExtendedExperimentResult;
import edu.ecnu.dll.basic.basic_struct.pack.experiment_result_info.NormalExperimentResult;
import edu.ecnu.dll.basic.basic_struct.pack.multi_agent_info.ResponseNonPrivacyWorkerTaskInfo;
import edu.ecnu.dll.basic.basic_struct.pack.single_agent_info.sub_class.*;
import edu.ecnu.dll.run.result_tools.CommonFunction;
import edu.ecnu.dll.run.result_tools.TargetTool;
import edu.ecnu.dll.run.run_main.AbstractRun;
import tools.io.read.DoubleRead;
import tools.io.read.PointRead;
import tools.struct.Point;

import java.util.List;

public class GameTheoryNonPrivacySolution extends NonPrivacySolution {


    public static TargetInfoComparator targetInfoComparator = new TargetInfoComparator(TargetInfoComparator.DESCENDING);
    public static TargetInfoComparator targetInfoForProposingValueComparator = new TargetInfoComparator(TargetInfoComparator.DESCENDING);
    public static TargetInfoComparator targetInfoForUtilityValueComparator = new TargetInfoComparator(TargetInfoComparator.DESCENDING);
    public static TargetInfoComparator targetInfoForUtilityAndCompositionValueComparator = new TargetInfoComparator(TargetInfoComparator.DESCENDING);



//    public static double getValueAndDistancePartOfUtilityValue(double taskValue, double realDistance) {
//        return taskValue - transformDistanceToValue(realDistance);
//    }

    public void initializeAllocation(WorkerIDDistancePair[] winnerPackedArray) {
        for (int i = 0; i < this.tasks.length; i++) {
            winnerPackedArray[i] = NonPrivacySolution.DEFAULT_WORKER_ID_DISTANCE_PAIR;
        }
    }

    /**
     * 返回game theory迭代中的utility函数值
     * 假设 worker w_j 原来是 task t_i_1 的获胜者(没有则相应的距离值和隐私值为0)，现在要竞争 task t_i_2, 原来 task t_i_2 的获胜者是 worker w_j_w (没有则相应的距离值和隐私值为0). 当前轮数是 k.
     * @param proposalValue t_i_2 的价值
     * @param nowDistance 第 k 轮 w_j 到 t_i_2 的有效距离
     * @param beforeWinnerTaskValue t_i_2 的值 (正常情况下回合 proposalValue 相等)
     * @param beforeWinnerDistance 第 k-1 轮 t_i_2 到 w_j_w 的有效距离
     * @param abandonedBeforeValue t_i_1 的价值
     * @param abandonedBeforeDistance 第 k-1 轮 w_j 到 t_i_1 的有效距离
     * @return 效用函数值
     */
    // TODO: ALTER
    public Double getGTUtilityValue(Double proposalValue, Double nowDistance, Double beforeWinnerTaskValue , Double beforeWinnerDistance, Double abandonedBeforeValue, Double abandonedBeforeDistance) {
        return proposalValue - transformDistanceToValue(nowDistance)
                - beforeWinnerTaskValue + transformDistanceToValue(beforeWinnerDistance)
                - abandonedBeforeValue + transformDistanceToValue(abandonedBeforeDistance);
    }

    public ResponseNonPrivacyWorkerTaskInfo getBestResponseTaskPackedInfo(Integer workerID, WorkerIDDistancePair[] winnerPackedArray) {
        Integer budgetIndex;
        Double newPrivacyBudget, newNoiseDistance;
        Double tempDistance, chosenDistance = Double.MAX_VALUE;
        Integer chosenTaskID = DEFAULT_WORKER_WINNING_STATE, abandonedTaskID;
        Double chosenUtilityValue = Double.MIN_VALUE, tempUtilityValue;
        Double beforeWinnerDistance, beforeWinnerTaskValue, abandonedBeforeValue, abandonedBeforeDistance;
        Double abandonedSuccessfulValue, defeatSuccessfulValue, winSuccessfulValue;

        // 记录即将放弃的task相关信息
        abandonedTaskID = this.workers[workerID].getCurrentWinningState();
        if (abandonedTaskID < 0) {
            // 判断当前worker是否未占用某个task(不是某个task的获胜者)
            abandonedBeforeValue = 0.0;
            abandonedBeforeDistance = 0.0;
            abandonedSuccessfulValue = 0.0;
        } else {
            abandonedBeforeValue = this.tasks[abandonedTaskID].valuation;
            // todo: 记得更新 effective noise distance
            abandonedBeforeDistance = this.workers[workerID].getToTaskDistance(abandonedTaskID);
//            Double decreaseValuation = GameTheoryNonPrivacyBasedSolution.getValueAndDistancePartOfUtilityValue(abandonedBeforeValue, abandonedBeforeDistance);
            Double decreaseValuation = NonPrivacySolution.getUtilityValue(abandonedBeforeValue, abandonedBeforeDistance);
            // 非隐私下该值一定为0
            abandonedSuccessfulValue = this.workers[workerID].getSuccessfullyUtilityFunctionValue(abandonedTaskID) - decreaseValuation;

        }

        for (Integer tempTaskID : this.workers[workerID].reverseIndex) {
            if (tempTaskID.equals(abandonedTaskID)) {
                // 不能响应已经获胜的task
                continue;
            }

            //for test
//            if (tempTaskID == 57) {
//                System.out.println("tempTaskID: " + tempTaskID);
//            }
//            if (tempTaskID == 78) {
//                System.out.println("tempTaskID: " + tempTaskID);
//            }

//            budgetIndex = this.workers[workerID].getBudgetIndex(tempTaskID);
//            if (budgetIndex >= this.workers[workerID].getPrivacyBudgetArray(tempTaskID).length) {
//                continue;
//            }

//            newPrivacyBudget =  this.workers[workerID].getPrivacyBudgetArray(tempTaskID)[budgetIndex];
            // 默认隐私花费是线性的，每次就是多了一个新的privacyBudget
//            newNoiseDistance = this.workers[workerID].getNoiseDistanceArray(tempTaskID)[budgetIndex];
//            newNoiseDistance = this.workers[workerID].getToTaskDistance(tempTaskID);

            if (winnerPackedArray[tempTaskID] == null || winnerPackedArray[tempTaskID].getWorkerID().equals(NonPrivacySolution.DEFAULT_WORKER_ID_DISTANCE_PAIR.getWorkerID())) {
                // 判断要竞争的 task 是否未被占有
                beforeWinnerTaskValue = 0.0;
                beforeWinnerDistance = 0.0;
            } else {
                beforeWinnerTaskValue = this.tasks[tempTaskID].valuation;
                beforeWinnerDistance = winnerPackedArray[tempTaskID].getDistance();
            }


            // todo: 记得更新worker的Already published noise distance
//            tempDistance = getNewEffectiveNoiseDistanceAndPrivacyBudget(workerID, tempTaskID, newNoiseDistance, newPrivacyBudget);
            tempDistance = this.workers[workerID].getToTaskDistance(tempTaskID);
//            tempUtilityValue = getGTUtilityValue(this.tasks[tempTaskID].valuation, tempDistance.distance, tempDistance.budget, beforeWinnerTaskValue, beforeWinnerDistance, abandonedBeforeValue, abandonedBeforeDistance);
            tempUtilityValue = getGTUtilityValue(this.tasks[tempTaskID].valuation, tempDistance, beforeWinnerTaskValue, beforeWinnerDistance, abandonedBeforeValue, abandonedBeforeDistance);
            if (tempUtilityValue > chosenUtilityValue) {
                // todo: 测试当有两个相同的task点时的无限循环
                chosenTaskID = tempTaskID;
                chosenUtilityValue = tempUtilityValue;
                chosenDistance = tempDistance;
            }
        }

        // 获取选定者相应的修改数据以及对应的放弃值和打败值
        // 1. 竞争者的workerID
        // 2. 被竞争的taskID
        // 3. budget index increment
        // 4. new noise distance
        // 5. new privacy budget
        // 6. new privacy cost
        // 7. effective noise distance
        // 8. effective privacy budget
        // 9. successful utility value

        // 1. 要放弃的workerID（和竞争者的workerID一致）
        // 2. 要放弃的taskID
        // 3. 要放弃的该workerID对应的taskID对应的SuccessfulUtilityValue

        // 1. 被打败的workerID
        // 2. 被打败的taskID（和被竞争的taskID一致）
        // 3. 被打败的该workerID对应的taskID对应的SuccessfulUtilityValue

        if (chosenTaskID.equals(-1)) {
            return null;
        }

//        Integer competedTaskBudgetIndex = this.workers[workerID].getBudgetIndex(chosenTaskID);

//        WorkerTaskUpdateInfo[] result = new WorkerTaskUpdateInfo[3];


        Double chosenTaskValue = this.tasks[chosenTaskID].valuation;
        // 包含了新的 noise distance, new privacy budget, new total privacy cost
//        Double[] newBasicInfo = getNewNoiseDistancePrivacyBudgetTotalPrivacyBudgetCost(workerID, chosenTaskID);

        chosenDistance = this.workers[workerID].getToTaskDistance(chosenTaskID);
        // 默认隐私花费是线性的，每次就是多了一个新的privacyBudget
        winSuccessfulValue = getUtilityValue(chosenTaskValue, chosenDistance);

        WorkerTaskNonPrivacyUpdateInfo winnerInfo = new WorkerTaskNonPrivacyUpdateInfo(chosenTaskID, chosenDistance, workerID, winSuccessfulValue);



//        Double abandonedBeforeValue = this.tasks[abandonedTaskID].valuation;
//        Double abandonedTaskEffectiveDistance = this.workers[workerID].getToTaskEffectiveNoiseDistance(abandonedTaskID);
        // todo: 这里由于考虑到utility对于各个worker的一致性，所以统一用effective noise distance 而不用真实距离
        Double decreaseValuation = 0.0;
//        if (ba)
//        Double decreaseValuation = GameTheoryBasedSolution.getValueAndDistancePartOfUtilityValue(abandonedBeforeValue, abandonedBeforeEffectiveNoiseDistance);
//        abandonedSuccessfulValue = this.workers[workerID].getSuccessfullyUtilityFunctionValue(abandonedTaskID) - decreaseValuation;

        TaskWorkerIDSuccessfulValuationPair abandonedInfo = new TaskWorkerIDSuccessfulValuationPair(abandonedTaskID, workerID, abandonedSuccessfulValue);


        Integer defeatedWorkerID = NonPrivacySolution.DEFAULT_WORKER_ID_DISTANCE_PAIR.getWorkerID();
        if (winnerPackedArray[chosenTaskID] == null || (defeatedWorkerID = winnerPackedArray[chosenTaskID].getWorkerID()).equals(PrivacySolution.DEFAULT_WORKER_ID_DISTANCE_BUDGET_PAIR.getWorkerID())) {
            // 判断要竞争的 task 是否未被占有
            defeatSuccessfulValue = 0.0;
        } else {
            beforeWinnerTaskValue = chosenTaskValue;
            beforeWinnerDistance = winnerPackedArray[chosenTaskID].getDistance();
            defeatSuccessfulValue = this.workers[defeatedWorkerID].getSuccessfullyUtilityFunctionValue(chosenTaskID) - GameTheoryNonPrivacySolution.getUtilityValue(beforeWinnerTaskValue, beforeWinnerDistance);
        }

        TaskWorkerIDSuccessfulValuationPair defeatedInfo = new TaskWorkerIDSuccessfulValuationPair(chosenTaskID, defeatedWorkerID, defeatSuccessfulValue);

        return new ResponseNonPrivacyWorkerTaskInfo(chosenUtilityValue, winnerInfo, abandonedInfo, defeatedInfo);

    }

    protected void updateSuccessfulWorkerInfo(WorkerTaskNonPrivacyUpdateInfo workerTaskNonPrivacyUpdateInfo) {
        Integer workerID = workerTaskNonPrivacyUpdateInfo.getWorkerID();
        Integer taskID = workerTaskNonPrivacyUpdateInfo.getTaskID();

        if (NonPrivacySolution.DEFAULT_WORKER_ID_DISTANCE_PAIR.getWorkerID().equals(workerID) || taskID.equals(-1)) {
            return;
        }

        Double successfulUtilityFunctionValue = workerTaskNonPrivacyUpdateInfo.getNewSuccessfulUtilityFunctionValue();

        this.workers[workerID].setSuccessfullyUtilityFunctionValue(taskID, successfulUtilityFunctionValue);
        this.workers[workerID].setCurrentWinningState(taskID);
    }

    protected void updateDefeatedWorkerInfo(TaskWorkerIDSuccessfulValuationPair workerTaskUpdateInfo) {
        Integer workerID = workerTaskUpdateInfo.getWorkerID();
        Integer taskID = workerTaskUpdateInfo.getTaskID();
        if (NonPrivacySolution.DEFAULT_WORKER_ID_DISTANCE_PAIR.getWorkerID().equals(workerID) || taskID.equals(-1)) {
            return;
        }
        Double successfulUtilityFunctionValue = workerTaskUpdateInfo.getSuccessfulUtilityValue();
        this.workers[workerID].setSuccessfullyUtilityFunctionValue(taskID, successfulUtilityFunctionValue);
        // 无论是放弃还是被挤下去，worker都会变成没胜利状态
        this.workers[workerID].setCurrentWinningState(DEFAULT_WORKER_WINNING_STATE);
    }

    /**
     * 数组的第一个是胜利worker，会调用updateSuccessfulWorkerInfo
     * 数组剩余两个是失败和遗弃(同时是胜利)worker，会调用updateDefeatedWorkerInfo
     */
    public void updateWorkerInfo(ResponseNonPrivacyWorkerTaskInfo responseNonPrivacyWorkerTaskInfo) {
        this.updateDefeatedWorkerInfo(responseNonPrivacyWorkerTaskInfo.getAbandonedInfo());
        this.updateDefeatedWorkerInfo(responseNonPrivacyWorkerTaskInfo.getDefeatedInfo());
        // 考虑到winning状态更新，必须将successful这个放在其他两个之后
        this.updateSuccessfulWorkerInfo(responseNonPrivacyWorkerTaskInfo.getWinnerInfo());
    }

    private void updateWinnerList(ResponseNonPrivacyWorkerTaskInfo responseNonPrivacyWorkerTaskInfo, WorkerIDDistancePair[] winnerPackedArray) {
        WorkerTaskNonPrivacyUpdateInfo winnerInfo = responseNonPrivacyWorkerTaskInfo.getWinnerInfo();
        TaskWorkerIDSuccessfulValuationPair abandonedInfo = responseNonPrivacyWorkerTaskInfo.getAbandonedInfo();

        Integer abandonedTaskID = abandonedInfo.getTaskID();
        if (abandonedTaskID >= 0) {
            winnerPackedArray[abandonedTaskID] = NonPrivacySolution.DEFAULT_WORKER_ID_DISTANCE_PAIR;
        }
        Integer proposalTaskID = winnerInfo.getTaskID();

        winnerPackedArray[proposalTaskID] = winnerInfo.getWorkerIDDistancePair();

    }


    public WorkerIDDistancePair[] compete() {
//        int k = 0;
        WorkerIDDistancePair[] winnerPackedArray = new WorkerIDDistancePair[this.tasks.length];
        initializeAllocation(winnerPackedArray);
        boolean strategyChangeState = true;
        ResponseNonPrivacyWorkerTaskInfo tempResponseInfo;
        Double tempGTValue;
        while (strategyChangeState) {
            strategyChangeState = false;
            // 重复迭代所有worker
//            ++k;
            for (int workerID = 0; workerID < workers.length; workerID++) {

//                if (workerID == 590) {
//                    System.out.println(workerID);
//                }

                /**
                 *  针对每个worker，选出使其GTUtility最大的task
                 */
               tempResponseInfo = getBestResponseTaskPackedInfo(workerID, winnerPackedArray);





                if (tempResponseInfo == null) {
                    continue;
                }


//                if (!tempResponseInfo.getDefeatedInfo().getWorkerID().equals(-1)){
//                    System.out.println("Oh Hou!");
//                }


                tempGTValue = tempResponseInfo.getGtUtilityValue();
               if (tempGTValue <= 1e-6) {
                   continue;
               }

               updateWorkerInfo(tempResponseInfo);

               // 更新结果列表
                updateWinnerList(tempResponseInfo, winnerPackedArray);

               strategyChangeState = true;

            }
        }

//        System.out.println(k);
        return winnerPackedArray;

    }

    public static void main(String[] args) {
//        String basicDatasetPath = "E:\\1.学习\\4.数据集\\1.FourSquare-NYCandTokyoCheck-ins\\output\\SYN";
//        String basicDatasetPath = "E:\\1.学习\\4.数据集\\1.FourSquare-NYCandTokyoCheck-ins\\output\\TKY";
//        String basicDatasetPath = "E:\\1.学习\\4.数据集\\1.FourSquare-NYCandTokyoCheck-ins\\output\\test\\test1";
//        String basicDatasetPath = "E:\\1.学习\\4.数据集\\dataset\\original\\chengdu";
        String basicDatasetPath = "E:\\1.学习\\4.数据集\\dataset\\original\\chengdu_default";
//        double[] fixedTaskValueAndWorkerRange = new double[]{20.0, 2};
        double[] fixedTaskValueAndWorkerRange = new double[]{400.0, 400};
//        Integer dataType = AbstractRun.LONGITUDE_LATITUDE;
        Integer dataType = AbstractRun.COORDINATE;


        String workerPointPath = basicDatasetPath + "\\worker_point.txt";
        String taskPointPath = basicDatasetPath + "\\task_point.txt";
        String taskValuePath = basicDatasetPath + "\\task_value.txt";
        String workerRangePath = basicDatasetPath + "\\worker_range.txt";
        String workerPrivacyBudgetPath = basicDatasetPath + "\\worker_budget.txt";
        String workerNoiseDistancePath = basicDatasetPath + "\\worker_noise_distance.txt";

        List<Point> taskPointList = PointRead.readPointWithFirstLineCount(taskPointPath);
        List<Double> taskValueList;

        List<Point> workerPointList = PointRead.readPointWithFirstLineCount(workerPointPath);
        List<Double> workerRangeList;
//        List<Double[]>[] workerPrivacyBudgetList = TwoDimensionDoubleRead.readDouble(workerPrivacyBudgetPath, 1);
//        List<Double[]>[] workerNoiseDistanceList = TwoDimensionDoubleRead.readDouble(workerNoiseDistancePath, 0.001);


        // 初始化 task 和 workers
        GameTheoryNonPrivacySolution gameTheoryNonPrivacySolution = new GameTheoryNonPrivacySolution();

        Double taskValue = null, workerRange = null;

        if (fixedTaskValueAndWorkerRange == null) {
            taskValueList = DoubleRead.readDoubleWithFirstSizeLineToList(taskValuePath);
            workerRangeList = DoubleRead.readDoubleWithFirstSizeLineToList(workerRangePath);
            gameTheoryNonPrivacySolution.initializeBasicInformation(taskPointList, taskValueList, workerPointList, workerRangeList);
        } else {
            taskValue = fixedTaskValueAndWorkerRange[0];
            workerRange = fixedTaskValueAndWorkerRange[1];
            gameTheoryNonPrivacySolution.initializeBasicInformation(taskPointList, taskValue, workerPointList, workerRange);
        }

        //todo: 根据不同的数据集选用不同的初始化
//        multiTaskMultiCompetitionSolution.initializeAgents();
        Integer dataTypeValue = Integer.valueOf(dataType);
        if (AbstractRun.COORDINATE.equals(dataTypeValue)) {
//            gameTheoryNonPrivacyBasedSolution.initializeAgents(workerPrivacyBudgetList, workerNoiseDistanceList);
            gameTheoryNonPrivacySolution.initializeAgents();
        } else if (AbstractRun.LONGITUDE_LATITUDE.equals(dataTypeValue)) {
//            gameTheoryNonPrivacyBasedSolution.initializeAgentsWithLatitudeLongitude(workerPrivacyBudgetList, workerNoiseDistanceList);
            gameTheoryNonPrivacySolution.initializeAgentsWithLatitudeLongitude();
        } else {
            throw new RuntimeException("The type input is not right!");
        }


        // 执行竞争过程
        long startCompetingTime = System.currentTimeMillis();
        WorkerIDDistancePair[] winner = gameTheoryNonPrivacySolution.compete();
        long endCompetingTime = System.currentTimeMillis();
        Long runningTime = TargetTool.getRunningTime(startCompetingTime, endCompetingTime);

//        showResultA(winner);
        BasicExperimentResult basicExperimentResult = CommonFunction.getResultData(winner, gameTheoryNonPrivacySolution.workers);

        CommonFunction.showResultB(winner);

        NormalExperimentResult normalExperimentResult = new NormalExperimentResult(basicExperimentResult, runningTime);
        ExtendedExperimentResult extendedExperimentResult = new ExtendedExperimentResult(normalExperimentResult, null, taskValue, workerRange);

//        System.out.println(normalExperimentResult);
        System.out.println(extendedExperimentResult);
    }






}
