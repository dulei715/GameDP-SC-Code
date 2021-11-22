package edu.ecnu.dll.basic.basic_solution;

import edu.ecnu.dll.basic.basic_struct.agent.Task;
import edu.ecnu.dll.basic.basic_struct.pack.single_agent_info.sub_class.DistanceBudgetPair;
import edu.ecnu.dll.basic.basic_struct.pack.single_agent_info.sub_class.WorkerIDNoDistanceUtilityNoiseDistanceBudgetPair;
import edu.ecnu.dll.basic.basic_struct.pack.single_agent_info.sub_class.WorkerIDNoiseDistanceBudgetPair;
import edu.ecnu.dll.basic.basic_struct.BasicTask;
import edu.ecnu.dll.scheme.scheme_main.struct.agent.worker.MultiTaskPrivacyBasicWorker;
import tools.basic.BasicArray;
import tools.basic.BasicCalculation;
import tools.differential_privacy.noise.LaplaceUtils;
import tools.struct.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public abstract class PrivacySolution extends Solution {

    public static final WorkerIDNoiseDistanceBudgetPair DEFAULT_WORKER_ID_DISTANCE_BUDGET_PAIR = new WorkerIDNoiseDistanceBudgetPair(-1, Double.MAX_VALUE, Double.MAX_VALUE);
    public static final WorkerIDNoDistanceUtilityNoiseDistanceBudgetPair DEFAULT_WORKER_ID_NO_DISTANCE_DISTANCE_BUDGET_PAIR = new WorkerIDNoDistanceUtilityNoiseDistanceBudgetPair(-1, Double.MAX_VALUE, Double.MAX_VALUE, 0.0);
    public static final Integer DEFAULT_WORKER_WINNING_STATE = -1;
    public Task[] tasks = null;
    public MultiTaskPrivacyBasicWorker[] workers = null;




//    protected double getUtilityValue(double taskValue, double effectivePrivacyBudget, double realDistance, double privacyBudgetCost) {
////        return taskValue + taskValue * toNormalValue(effectivePrivacyBudget) - alpha * realDistance - beta * privacyBudgetCost;
//        return taskValue - transformDistanceToValue(realDistance) - transformPrivacyBudgetToValue(privacyBudgetCost);
//    }
    protected double getUtilityValue(double taskValue, double realDistance, double privacyBudgetCost) {
        return taskValue - transformDistanceToValue(realDistance) - transformPrivacyBudgetToValue(privacyBudgetCost);
    }

    public static double getValueAndDistancePartOfUtilityValue(double taskValue, double realDistance) {
        return taskValue - transformDistanceToValue(realDistance);
    }

    public double getUtilityWithoutDistance(Integer taskID, Integer workerID){
//        if (this.workers[workerID].getTotalPrivacyBudgetCost(taskID) != 0.0) {
//            System.out.println("haha");
//        }
        return this.tasks[taskID].valuation - transformPrivacyBudgetToValue(this.workers[workerID].getTotalPrivacyBudgetCost(taskID));
    }

//    public static double getUtilityWithoutDistance(double taskValue, double privacyBudgetCost) {
//        return taskValue - transformPrivacyBudgetToValue(privacyBudgetCost);
//    }

    /**
     *  5个初始化函数
     */
    public void initializeBasicInformation(List<Point> taskPositionList, Double[] taskValueArray, List<Point> workerPositionList, List<Double> workerRangeList) {
        Point taskPosition, workerPosition;
        // 同时初始化父类
        super.tasks = this.tasks = new BasicTask[taskPositionList.size()];
        for (int i = 0; i < taskPositionList.size(); i++) {
            taskPosition = taskPositionList.get(i);
            this.tasks[i] = new BasicTask(taskPosition.getIndex());
            this.tasks[i].valuation = taskValueArray[i];
        }
        // 同时初始化父类
        super.workers = this.workers = new MultiTaskPrivacyBasicWorker[workerPositionList.size()];
        for (int j = 0; j < workers.length; j++) {
            workerPosition = workerPositionList.get(j);
            this.workers[j] = new MultiTaskPrivacyBasicWorker(workerPosition.getIndex());
            this.workers[j].setMaxRange(workerRangeList.get(j));
            this.workers[j].taskIndex = BasicArray.getInitializedArray(-1, this.tasks.length);
        }

    }

    public void initializeBasicInformation(List<Point> taskPositionList, Double taskValue, List<Point> workerPositionList, Double workerRange) {
        Point taskPosition, workerPosition;
        // 同时初始化父类
        super.tasks = this.tasks = new BasicTask[taskPositionList.size()];
        for (int i = 0; i < taskPositionList.size(); i++) {
            taskPosition = taskPositionList.get(i);
            this.tasks[i] = new BasicTask(taskPosition.getIndex());
            this.tasks[i].valuation = taskValue;
        }
        // 同时初始化父类
        super.workers = this.workers = new MultiTaskPrivacyBasicWorker[workerPositionList.size()];
        for (int j = 0; j < workers.length; j++) {
            workerPosition = workerPositionList.get(j);
            this.workers[j] = new MultiTaskPrivacyBasicWorker(workerPosition.getIndex());
            this.workers[j].setMaxRange(workerRange);
            this.workers[j].taskIndex = BasicArray.getInitializedArray(-1, this.tasks.length);
        }

    }

    public void initializeAgents(List<Double[]>[] privacyBudgetListArray, List<Double[]>[] noiseDistanceListArray) {
        for (int j = 0; j < this.workers.length; j++) {
            this.workers[j].reverseIndex = new ArrayList<>();
            this.workers[j].toTaskDistance = new ArrayList<>();
            // 此处没有初始化privacyBudgetArray，因为会在initialize basic function 中初始化
            this.workers[j].privacyBudgetArrayList = new ArrayList<>();
            this.workers[j].noiseDistanceArrayList = new ArrayList<>();
            this.workers[j].budgetIndex = new ArrayList<>();
            this.workers[j].alreadyPublishedNoiseDistanceAndBudgetTreeSetArray = new ArrayList<>();
            this.workers[j].effectiveNoiseDistance = new ArrayList<>();
            this.workers[j].effectivePrivacyBudget = new ArrayList<>();
            this.workers[j].totalPrivacyBudgetCost = new ArrayList<>();
//            this.workers[j].taskCompetingTimes = new ArrayList<>();
            this.workers[j].currentUtilityFunctionValue = new ArrayList<>();
            this.workers[j].successfullyUtilityFunctionValue = new ArrayList<>();
            this.workers[j].currentWinningState = DEFAULT_WORKER_WINNING_STATE;
            double tempDistance;
            int k = 0;
            for (int i = 0; i < tasks.length; i++) {
                // todo: 修改为经纬度的
                tempDistance = BasicCalculation.get2Norm(this.tasks[i].location, this.workers[j].location);
                if (tempDistance <= this.workers[j].maxRange) {
                    this.workers[j].taskIndex[i] = k++;
                    this.workers[j].reverseIndex.add(i);
                    this.workers[j].toTaskDistance.add(tempDistance);
                    this.workers[j].budgetIndex.add(0);
                    this.workers[j].privacyBudgetArrayList.add(privacyBudgetListArray[j].get(i));
//                    this.workers[j].noiseDistanceArrayList.add(LaplaceUtils.getLaplaceNoiseWithOriginalValue(tempDistance, privacyBudgetListArray[j].get(i)));
                    this.workers[j].noiseDistanceArrayList.add(noiseDistanceListArray[j].get(i));
                    this.workers[j].alreadyPublishedNoiseDistanceAndBudgetTreeSetArray.add(new TreeSet<>());
                    this.workers[j].effectiveNoiseDistance.add(0.0);
                    this.workers[j].effectivePrivacyBudget.add(0.0);
                    this.workers[j].totalPrivacyBudgetCost.add(0.0);
//                    this.workers[j].taskCompetingTimes.add(0);
                    this.workers[j].currentUtilityFunctionValue.add(0.0);
                    this.workers[j].successfullyUtilityFunctionValue.add(0.0);
                }
            }
        }
    }

    public void initializeAgentsWithLatitudeLongitude(List<Double[]>[] privacyBudgetListArray, List<Double[]>[] noiseDistanceListArray) {
        for (int j = 0; j < this.workers.length; j++) {
            this.workers[j].reverseIndex = new ArrayList<>();
            this.workers[j].toTaskDistance = new ArrayList<>();
            // 此处没有初始化privacyBudgetArray，因为会在initialize basic function 中初始化
            this.workers[j].privacyBudgetArrayList = new ArrayList<>();
            this.workers[j].noiseDistanceArrayList = new ArrayList<>();
            this.workers[j].budgetIndex = new ArrayList<>();
            this.workers[j].alreadyPublishedNoiseDistanceAndBudgetTreeSetArray = new ArrayList<>();
            this.workers[j].effectiveNoiseDistance = new ArrayList<>();
            this.workers[j].effectivePrivacyBudget = new ArrayList<>();
            this.workers[j].totalPrivacyBudgetCost = new ArrayList<>();
//            this.workers[j].taskCompetingTimes = new ArrayList<>();
            this.workers[j].currentUtilityFunctionValue = new ArrayList<>();
            this.workers[j].successfullyUtilityFunctionValue = new ArrayList<>();
            this.workers[j].currentWinningState = DEFAULT_WORKER_WINNING_STATE;
            double tempDistance;
            int k = 0;
            for (int i = 0; i < tasks.length; i++) {
                // todo: 修改为经纬度的
                tempDistance = BasicCalculation.getDistanceFrom2LngLat(this.tasks[i].location[1], this.tasks[i].location[0], this.workers[j].location[1],this.workers[j].location[0]);
                if (tempDistance <= this.workers[j].maxRange) {
                    this.workers[j].taskIndex[i] = k++;
                    this.workers[j].reverseIndex.add(i);
                    this.workers[j].toTaskDistance.add(tempDistance);
                    this.workers[j].budgetIndex.add(0);
                    this.workers[j].privacyBudgetArrayList.add(privacyBudgetListArray[j].get(i));
//                    this.workers[j].noiseDistanceArrayList.add(LaplaceUtils.getLaplaceNoiseWithOriginalValue(tempDistance, privacyBudgetListArray[j].get(i)));
                    this.workers[j].noiseDistanceArrayList.add(noiseDistanceListArray[j].get(i));
                    this.workers[j].alreadyPublishedNoiseDistanceAndBudgetTreeSetArray.add(new TreeSet<>());
                    this.workers[j].effectiveNoiseDistance.add(0.0);
                    this.workers[j].effectivePrivacyBudget.add(0.0);
                    this.workers[j].totalPrivacyBudgetCost.add(0.0);
//                    this.workers[j].taskCompetingTimes.add(0);
                    this.workers[j].currentUtilityFunctionValue.add(0.0);
                    this.workers[j].successfullyUtilityFunctionValue.add(0.0);
                }
            }
        }
    }



    protected DistanceBudgetPair getNewEffectiveNoiseDistanceAndPrivacyBudget(Integer workerID, Integer taskID, double newNoiseDistance, double newPrivacyBudget) {
        //todo: test maximumLikelihood
        TreeSet<DistanceBudgetPair> tempTreeSet = new TreeSet<>();
//        tempTreeSet.addAll(this.workers[workerID].alreadyPublishedNoiseDistanceAndBudgetTreeSetArray[taskID]);
        tempTreeSet.addAll(this.workers[workerID].getAlreadyPublishedNoiseDistanceAndBudgetTreeSet(taskID));
        tempTreeSet.add(new DistanceBudgetPair(newNoiseDistance, newPrivacyBudget));
        double[] distanceBudget = LaplaceUtils.getMaximumLikelihoodEstimationInGivenPoint(tempTreeSet);
        return new DistanceBudgetPair(distanceBudget[0], distanceBudget[1]);
    }
    protected Double getNewTotalCostPrivacyBudget(Integer workerID, Integer taskID) {
        Double result;
//        int index = this.workers[workerID].budgetIndex[taskID];
        int index = this.workers[workerID].getBudgetIndex(taskID);
        if (index >= this.workers[workerID].getPrivacyBudgetArray(taskID).length) {
            return null;
        }
//        result = this.workers[workerID].privacyBudgetCost[taskID] + this.workers[workerID].privacyBudgetArray[taskID][index];
        result = this.workers[workerID].getTotalPrivacyBudgetCost(taskID) + this.workers[workerID].getPrivacyBudgetArray(taskID)[index];
        return result;
    }

    protected Double getNewNoiseDistance(Integer workerID, Integer taskID) {
        Double result;
        int index = this.workers[workerID].getBudgetIndex(taskID);
        if (index >= this.workers[workerID].getPrivacyBudgetArray(taskID).length) {
            return null;
        }
        result = this.workers[workerID].getNoiseDistanceArray(taskID)[index];

        return result;
    }

    protected Double getNewPrivacyBudget(Integer workerID, Integer taskID) {
        Double result;
        int index = this.workers[workerID].getBudgetIndex(taskID);
        if (index >= this.workers[workerID].getPrivacyBudgetArray(taskID).length) {
            return null;
        }
        result = this.workers[workerID].getPrivacyBudgetArray(taskID)[index];

        return result;
    }

    /**
     * 封装前三个函数
     * @return
     */
    protected Double[] getNewNoiseDistancePrivacyBudgetTotalPrivacyBudgetCost(Integer workerID, Integer taskID) {
        int index = this.workers[workerID].getBudgetIndex(taskID);
        if (index >= this.workers[workerID].getPrivacyBudgetArray(taskID).length) {
            return null;
        }
        Double[] result = new Double[3];
        result[0] = this.workers[workerID].getNoiseDistanceArray(taskID)[index];
        result[1] = this.workers[workerID].getPrivacyBudgetArray(taskID)[index];
        result[2] = this.workers[workerID].getTotalPrivacyBudgetCost(taskID) + result[1];
        return result;
    }

    protected void setCandidateTaskByBudget(List<Integer> tempCandidateTaskList, MultiTaskPrivacyBasicWorker worker) {
        // 只遍历privacybudget列表，即限制遍历的task为range范围内
        for (int i = 0; i < worker.privacyBudgetArrayList.size(); i++) {
            if (worker.budgetIndex.get(i) < worker.privacyBudgetArrayList.get(i).length) {
                tempCandidateTaskList.add(worker.reverseIndex.get(i));
            }
        }
    }


}
