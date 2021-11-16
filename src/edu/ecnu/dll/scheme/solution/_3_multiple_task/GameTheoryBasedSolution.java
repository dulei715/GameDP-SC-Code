package edu.ecnu.dll.scheme.solution._3_multiple_task;

import edu.ecnu.dll.basic_struct.comparator.TargetInfoForTaskEntropyComparator;
import edu.ecnu.dll.basic_struct.pack.single_agent_info.sub_class.DistanceBudgetPair;
import edu.ecnu.dll.basic_struct.pack.single_agent_info.sub_class.TaskUpdateInfo;
import edu.ecnu.dll.basic_struct.pack.single_agent_info.sub_class.WorkerIDNoiseDistanceBudgetPair;
import edu.ecnu.dll.scheme.solution._0_basic.PrivacySolution;

public class GameTheoryBasedSolution extends PrivacySolution {


    public static TargetInfoForTaskEntropyComparator targetInfoForTaskEntropyComparator = new TargetInfoForTaskEntropyComparator(TargetInfoForTaskEntropyComparator.DESCENDING);
    public static TargetInfoForTaskEntropyComparator targetInfoForProposingValueComparator = new TargetInfoForTaskEntropyComparator(TargetInfoForTaskEntropyComparator.DESCENDING);
    public static TargetInfoForTaskEntropyComparator targetInfoForUtilityValueComparator = new TargetInfoForTaskEntropyComparator(TargetInfoForTaskEntropyComparator.DESCENDING);
    public static TargetInfoForTaskEntropyComparator targetInfoForUtilityAndCompositionValueComparator = new TargetInfoForTaskEntropyComparator(TargetInfoForTaskEntropyComparator.DESCENDING);



    public void initializeAllocation(WorkerIDNoiseDistanceBudgetPair[] winnerPackedArray) {
        for (int i = 0; i < this.tasks.length; i++) {
            winnerPackedArray[i] = PrivacySolution.DEFAULT_WORKER_ID_DISTANCE_BUDGET_PAIR;
        }
    }

    /**
     * 返回game theory迭代中的utility函数值
     * 假设 worker w_j 原来是 task t_i_1 的获胜者(没有则相应的距离值和隐私值为0)，现在要竞争 task t_i_2, 原来 task t_i_2 的获胜者是 worker w_j_w (没有则相应的距离值和隐私值为0). 当前轮数是 k.
     * @param proposalValue t_i_2 的价值
     * @param nowEffectiveNoiseDistance 第 k 轮 w_j 到 t_i_2 的有效距离
     * @param nowNewPrivacyBudget 第 k 轮 w_j 新发布的关于t_i_2的因私损失(假设因私损失是线性增长的)
     * @param beforeWinnerTaskValue t_i_2 的值 (正常情况下回合 proposalValue 相等)
     * @param beforeWinnerEffectiveNoiseDistance 第 k-1 轮 t_i_2 到 w_j_w 的有效距离
     * @param abandonedBeforeValue t_i_1 的价值
     * @param abandonedBeforeEffectiveNoiseDistance 第 k-1 轮 w_j 到 t_i_1 的有效距离
     * @return 效用函数值
     */
    public Double getGTUtilityValue(Double proposalValue, Double nowEffectiveNoiseDistance, Double nowNewPrivacyBudget, Double beforeWinnerTaskValue , Double beforeWinnerEffectiveNoiseDistance, Double abandonedBeforeValue, Double abandonedBeforeEffectiveNoiseDistance) {
        return proposalValue - transformDistanceToValue(nowEffectiveNoiseDistance) - transformPrivacyBudgetToValue(nowNewPrivacyBudget)
                - beforeWinnerTaskValue + transformDistanceToValue(beforeWinnerEffectiveNoiseDistance)
                - abandonedBeforeValue + transformDistanceToValue(abandonedBeforeEffectiveNoiseDistance);
    }

    public TaskUpdateInfo getBestResponseTaskPackedInfo(Integer workerID, WorkerIDNoiseDistanceBudgetPair[] winnerPackedArray) {

        Integer budgetIndex;
        Double newPrivacyBudget, newNoiseDistance;
        DistanceBudgetPair newEffectiveDistanceBudgetPair;
        Integer chosenTaskID = -1, abandonedTaskID;
        Double chosenUtilityValue = Double.MIN_VALUE, tempUtilityValue;
        Double beforeWinnerEffectiveNoiseDistance, beforeWinnerTaskValue, abandonedBeforeValue, abandonedBeforeEffectiveNoiseDistance;
        for (Integer tempTaskID : this.workers[workerID].reverseIndex) {
            budgetIndex = this.workers[workerID].getBudgetIndex(tempTaskID);
            newPrivacyBudget =  this.workers[workerID].getPrivacyBudgetArray(tempTaskID)[budgetIndex];
            // 默认隐私花费是线性的，每次就是多了一个新的privacyBudget
            newNoiseDistance = this.workers[workerID].getNoiseDistanceArray(tempTaskID)[budgetIndex];

            if (winnerPackedArray[tempTaskID] == null || winnerPackedArray[tempTaskID].getWorkerID().equals(PrivacySolution.DEFAULT_WORKER_ID_DISTANCE_BUDGET_PAIR.getWorkerID())) {
                // 判断要竞争的 task 是否未被占有
                beforeWinnerTaskValue = 0.0;
                beforeWinnerEffectiveNoiseDistance = 0.0;
            } else {
                beforeWinnerTaskValue = this.tasks[tempTaskID].valuation;
                beforeWinnerEffectiveNoiseDistance = winnerPackedArray[tempTaskID].getEffectiveNoiseDistance();
            }

            abandonedTaskID = this.workers[workerID].getCurrentWinningState();

            if (abandonedTaskID < 0) {
                // 判断当前worker是否未占用某个task(不是某个task的获胜者)
                abandonedBeforeValue = 0.0;
                abandonedBeforeEffectiveNoiseDistance = 0.0;
            } else {
                abandonedBeforeValue = this.tasks[abandonedTaskID].valuation;
                // todo: 记得更新 effective noise distance
                abandonedBeforeEffectiveNoiseDistance = this.workers[workerID].getEffectiveNoiseDistance(tempTaskID);
            }
            // todo: 记得更新worker的Already published noise distance
            newEffectiveDistanceBudgetPair = getNewEffectiveNoiseDistanceAndPrivacyBudget(workerID, tempTaskID, newNoiseDistance, newPrivacyBudget);
            tempUtilityValue = getGTUtilityValue(this.tasks[tempTaskID].valuation, newEffectiveDistanceBudgetPair.distance, newEffectiveDistanceBudgetPair.budget, beforeWinnerTaskValue, beforeWinnerEffectiveNoiseDistance, abandonedBeforeValue, abandonedBeforeEffectiveNoiseDistance);
            if (tempUtilityValue > chosenUtilityValue) {
                chosenTaskID = tempTaskID;
                chosenUtilityValue = tempUtilityValue;
                xxxx
            }
        }

        this.workers[workerID].increaseBudgetIndex(i);

        DistanceBudgetPair newEffectiveDistanceBudgetPair = getNewEffectiveNoiseDistanceAndPrivacyBudget(workerID, i, newNoiseDistance, newPrivacyBudget);
        double tempCompeteDistance = newEffectiveDistanceBudgetPair.distance;
        double tempEffectivePrivacyBudget = newEffectiveDistanceBudgetPair.budget;
    }

    public void updateSuccessfulWorkerInfo(Integer workerID, TaskUpdateInfo taskUpdateInfo) {
        Integer taskID = taskUpdateInfo.getTaskID();

        Integer budgetIndexIncrement = taskUpdateInfo.getBudgetIndexIncrement();

        Double effectiveNoiseDistance = taskUpdateInfo.getEffectiveNoiseDistance();
        Double effectivePrivacyBudget = taskUpdateInfo.getEffectivePrivacyBudget();
        Double privacyBudgetCost = taskUpdateInfo.getNewTotalCostPrivacyBudget();

        Double newNoiseDistance = taskUpdateInfo.getNewNoiseDistance();
        Double newPrivacyBudget = taskUpdateInfo.getNewPrivacyBudget();

        Double successfulUtilityFunctionValue = taskUpdateInfo.getNewSuccessfulUtilityFunctionValue();

        this.workers[workerID].setBudgetIndex(taskID, this.workers[workerID].getBudgetIndex(taskID) + budgetIndexIncrement);
        this.workers[workerID].setEffectiveNoiseDistance(taskID, effectiveNoiseDistance);
        this.workers[workerID].setEffectivePrivacyBudget(taskID, effectivePrivacyBudget);
        this.workers[workerID].setPrivacyBudgetCost(taskID, privacyBudgetCost);
        this.workers[workerID].addElementToAlreadyPublishedNoiseDistanceAndBudgetTreeSet(taskID, new DistanceBudgetPair(newNoiseDistance, newPrivacyBudget));
        this.workers[workerID].setSuccessfullyUtilityFunctionValue(taskID, successfulUtilityFunctionValue);
    }

    public void updateDefeatedWorkerInfo(Integer workerID, TaskUpdateInfo taskUpdateInfo) {
        Integer taskID = taskUpdateInfo.getTaskID();

//        Integer budgetIndexIncrement = taskUpdateInfo.getBudgetIndexIncrement();

//        Double effectiveNoiseDistance = taskUpdateInfo.getEffectiveNoiseDistance();
//        Double effectivePrivacyBudget = taskUpdateInfo.getEffectivePrivacyBudget();
//        Double privacyBudgetCost = taskUpdateInfo.getNewTotalCostPrivacyBudget();

//        Double newNoiseDistance = taskUpdateInfo.getNewNoiseDistance();
//        Double newPrivacyBudget = taskUpdateInfo.getNewPrivacyBudget();

        Double successfulUtilityFunctionValue = taskUpdateInfo.getNewSuccessfulUtilityFunctionValue();

        this.workers[workerID].setBudgetIndex(taskID, this.workers[workerID].getBudgetIndex(taskID) + budgetIndexIncrement);
        this.workers[workerID].setEffectiveNoiseDistance(taskID, effectiveNoiseDistance);
        this.workers[workerID].setEffectivePrivacyBudget(taskID, effectivePrivacyBudget);
        this.workers[workerID].setPrivacyBudgetCost(taskID, privacyBudgetCost);
        this.workers[workerID].addElementToAlreadyPublishedNoiseDistanceAndBudgetTreeSet(taskID, new DistanceBudgetPair(newNoiseDistance, newPrivacyBudget));
        this.workers[workerID].setSuccessfullyUtilityFunctionValue(taskID, successfulUtilityFunctionValue);
    }

    public WorkerIDNoiseDistanceBudgetPair[] compete() {
        WorkerIDNoiseDistanceBudgetPair[] winnerPackedArray = new WorkerIDNoiseDistanceBudgetPair[this.tasks.length];
        initializeAllocation(winnerPackedArray);
        boolean strategyChangeState = true;
        TaskUpdateInfo tempChosenTaskUpdateInfo;
        while (strategyChangeState) {
            // 重复迭代所有worker
            for (int workerID = 0; workerID < workers.length; workerID++) {
                /**
                 *  针对每个worker，选出使其GTUtility最大的task
                 */
                tempChosenTaskUpdateInfo = getBestResponseTaskPackedInfo(workerID, winnerPackedArray);
                /**
                 *  更新该worker针对该task的相应信息
                 */
                // 1. worker 针对该 task 的 budgetIndex
                // 2. worker 针对该 task 的 effectiveNoiseDistance
                // 3. worker 针对该 task 的 effectivePrivacyBudget
                // 4. worker 针对该 task 的 privacyBudgetCost
                // 5. worker 针对该 task 的 alreadyPublishedNoiseDistanceAndBudgetTreeSetArray
                // 6. worker 针对该 task 的 successfullyUtilityFunctionValue
            }
        }

    }






}
