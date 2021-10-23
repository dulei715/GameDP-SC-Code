package edu.ecnu.dll.scheme.solution._2_single_task;

import edu.ecnu.dll.basic_struct.pack.DistanceBudgetPair;
import edu.ecnu.dll.scheme.struct.task.BasicTask;
import edu.ecnu.dll.basic_struct.agent.Task;
import edu.ecnu.dll.scheme.struct.worker.SingleTaskBasicWorker;
import tools.basic.BasicCalculation;
import tools.differential_privacy.compare.impl.LaplaceProbabilityDensityFunction;
import tools.differential_privacy.noise.LaplaceUtils;
import tools.io.print.MyPrint;
import tools.io.read.DoubleRead;
import tools.io.read.PointRead;
import tools.struct.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class SingleTaskSolution {

    public static final int DISTANCE_TAG = 0;
    public static final int BUDGET_TAG = 1;

    public static final double alpha = 1;
    public static final double beta = 1;


    public Task task = null;
    public SingleTaskBasicWorker[] workers = null;

    public static final int budgetSize = 3;

//    private double getUlitityValue(List<Integer> taskList, Double workerMaxRange, double[][] workerBudgetMatrix, int taskIndex, int workerIndex, int bugetIndex) {
//        return 0;
//    }

//    private double getIncrementUtility(double distance, double maximumRange, double addingPrivacyBudget) {
//        return 1 + distance - addingPrivacyBudget;
//    }

    private double getUtilityValue(double taskValue, double effectivePrivacyBudget, double realDistance, double privacyBudgetCost) {
        return taskValue + taskValue * effectivePrivacyBudget - alpha * realDistance - beta * privacyBudgetCost;
    }

//    public void initializeBasicInformation() {
//        // todo: 初始化 task 位置，以及 workers 的位置
//        this.task = new BasicTask(new double[]{0.0, 0.0});
//        // todo: 初始化 workers 针对 task 的 privacy budget
//        this.workers = new SingleTaskBasicWorker[2];
//        this.workers[0].location = new double[]{2.0, 2.0};
////        this.workers[0].maxRange = 4.0;
//        this.workers[0].privacyBudgetArray = new Double[]{0.2, 0.3, 0.5};
//
//        this.workers[1].location = new double[]{-1.5, -1.5};
////        this.workers[1].maxRange = 3.0;
//        this.workers[1].privacyBudgetArray = new Double[]{0.3, 0.4, 0.3};
//
//
//    }

    public void initializeBasicInformation(List<Point> taskPositionList, Double[] taskValueArray, List<Point> workerPositionList, List<Double>[] privacyBudgetListArray) {
//        // todo: 初始化 task 位置，以及 workers 的位置
//        this.task = new BasicTask(new double[]{0.0, 0.0});
//        // todo: 初始化 workers 针对 task 的 privacy budget
//        this.workers = new SingleTaskBasicWorker[2];
//        this.workers[0].location = new double[]{2.0, 2.0};
//        this.workers[0].privacyBudgetArray = new Double[]{0.2, 0.3, 0.5};
//        this.workers[1].location = new double[]{-1.5, -1.5};
//        this.workers[1].privacyBudgetArray = new Double[]{0.3, 0.4, 0.3};
        Point taskPosition = taskPositionList.get(0);
        Point workerPosition;
        this.task = new BasicTask(new double[]{taskPosition.getxIndex(), taskPosition.getyIndex()});
        this.task.valuation = taskValueArray[0];
        this.workers = new SingleTaskBasicWorker[workerPositionList.size()];
        for (int i = 0; i < workers.length; i++) {
            workerPosition = workerPositionList.get(i);
            this.workers[i] = new SingleTaskBasicWorker(new double[]{workerPosition.getxIndex(), workerPosition.getyIndex()});
            this.workers[i].privacyBudgetArray = privacyBudgetListArray[i].toArray(new Double[0]);
        }

    }

    public void initializeAgents() {
        for (int i = 0; i < this.workers.length; i++) {
            this.workers[i].toTaskDistance = BasicCalculation.get2Norm(this.task.location, this.workers[i].location);
//            this.workers[i].alreadyPublishedAverageNoiseDistance = 0.0;
//            this.workers[i].alreadyPublishedTotalPrivacyBudget = 0.0;
            this.workers[i].alreadyPublishedNoiseDistanceAndBudget = new TreeSet<>();
            this.workers[i].effectiveNoiseDistance = 0.0;
            this.workers[i].effectivePrivacyBudget = 0.0;
            // currentUtilityFunctionValue初始化为0，但是可能由于被竞争下去导致值为负
            this.workers[i].currentUtilityFunctionValue = 0.0;
            this.workers[i].budgetIndex = 0;
            this.workers[i].privacyBudgetCost = 0.0;
        }
    }

    private Double getNewCostPrivacyBudget(Integer workerID) {
        Double result;
        result = this.workers[workerID].privacyBudgetCost + this.workers[workerID].privacyBudgetArray[this.workers[workerID].budgetIndex];
        return result;
    }

    public void complete() {


        // 记录worker竞争时临时效用函数值
        double[] tempUtilityArray = new double[this.workers.length];

        // 记录每个worker的申请到的task列表
//        todo: whether to add:  List<Integer>[] allocatedTaskIDListArray = new ArrayList[this.workers.length];

        // 针对该task，记录当前竞争成功的worker的ID
        Integer taskWinnerID = -1;
        // 针对该task，记录当前竞争成功的worker的budget以及扰动距离
        double[] taskWinnerInfo = new double[2];
        // 针对该task，初始化距离为最大距离值
        taskWinnerInfo[DISTANCE_TAG] = Double.MAX_VALUE;
        // 针对该task，初始化对应距离的隐私预算为最大隐私预算
        taskWinnerInfo[BUDGET_TAG] = Double.MAX_VALUE;

        // 针对该task，本轮提出竞争的worker的ID（每轮需要清空）
        List<Integer> candidateWorkerIDList;
        candidateWorkerIDList = new ArrayList<>();
        initializeCandidateWorkers(candidateWorkerIDList);

        Integer[] candidateWorkerIDArray;
        double competeTemp;
        while (!candidateWorkerIDList.isEmpty()) {
            candidateWorkerIDArray = candidateWorkerIDList.toArray(new Integer[0]);
            candidateWorkerIDList.clear();
            for (Integer j : candidateWorkerIDArray) {
                //进行是否竞争判断1：如果当前 worker 不需要竞争(是上轮的胜利者)，就不作为
                if (j.equals(taskWinnerID)) {
                    continue;
                }

                // 进行是否竞争判断2：如果隐私预算数量不足，不作为
                if (this.workers[j].budgetIndex >= this.workers[j].privacyBudgetArray.length) {
//                    competeState[i] = false;
                    continue;
                }

                // 进行是否竞争判断3：如果PPCF函数计算出来的距离大于之前胜利者的距离，不作为
                //todo: 假设扰动距离的均值精度更高
                if (this.workers[j].toTaskDistance >= taskWinnerInfo[0]) {
                    continue;
                }

                // 进行是否竞争判断4：如果发布当前隐私预算以及扰动距离长度使得效用函数值为负值，不作为
//                tempUtilityArray[i] = getUlitityValue(allocatedTaskIDListArray[i], , workerArray[i].getMaxRange(), budgetMatrix, taskIndex, i, workerBudgetIndex[i]);
//                double incrementUtility = getIncrementUtility(this.workers[i].toTaskDistance, this.workers[i].getMaxRange(), this.workers[i].privacyBudgetArray[this.workers[i].budgetIndex]);
                double newCostPrivacyBudget = getNewCostPrivacyBudget(j);
                double newPrivacyBudget = this.workers[j].privacyBudgetArray[this.workers[j].budgetIndex];
                double newNoiseDistance = this.workers[j].toTaskDistance + LaplaceUtils.getLaplaceNoise(1, newPrivacyBudget);
//                double[] newEffectiveDistanceAndPrivacyBudget = LaplaceUtils.getMaximumLikelihoodEstimationInGivenPoint(this.workers[i].alreadyPublishedNoiseDistanceAndBudget, new DistanceBudgetPair(newNoiseDistance, newPrivacyBudget));
                DistanceBudgetPair newEffectiveDistanceBudgetPair = getNewEffectiveNoiseDistanceAndPrivacyBudget(j, newNoiseDistance, newPrivacyBudget);
                double newUtilityValue = getUtilityValue(this.task.valuation, newEffectiveDistanceBudgetPair.budget, this.workers[j].toTaskDistance, newCostPrivacyBudget);
                if (newUtilityValue <= this.workers[j].currentUtilityFunctionValue) {
                    continue;
                }



                // 进行是否竞争判断5：根据将要竞争的预算，计算扰动的距离值。如果PCF函数计算出来的距离大于之前胜利者的距离，不作为
//                double competeDistance = BasicCalculation.getAverage(this.workers[i].alreadyPublishedAverageNoiseDistance, newNoiseDistance, this.workers[i].budgetIndex + 1);
//                double totalBudget = this.workers[i].alreadyPublishedTotalPrivacyBudget + this.workers[i].privacyBudgetArray[this.workers[i].budgetIndex];
//                DistanceBudgetPair newEffectiveDistanceBudgetPair = getNewEffectiveNoiseDistanceAndPrivacyBudget(i, newNoiseDistance, this.workers[i].privacyBudgetArray[this.workers[i].budgetIndex]);
                double competeDistance = newEffectiveDistanceBudgetPair.distance;
                double effectivePrivacyBudget = newEffectiveDistanceBudgetPair.budget;
                double competeValue = LaplaceProbabilityDensityFunction.probabilityDensityFunction(competeDistance, taskWinnerInfo[DISTANCE_TAG], effectivePrivacyBudget, taskWinnerInfo[BUDGET_TAG]);
                if (competeValue <= 0.5) {
//                    competeState[i] = false;
                    continue;
                }


                // 否则（竞争成功），发布当前扰动距离长度和隐私预算(这里只添加进候选列表供server进一步选择)，并将隐私自己的预算索引值加1
                candidateWorkerIDList.add(j);
//                this.workers[i].toCompetePublishEverageNoiseDistance = competeDistance;
//                this.workers[i].toCompetePublishTotalPrivacyBudget = totalBudget;
//                this.workers[i].alreadyPublishedAverageNoiseDistance = competeDistance;
//                this.workers[i].alreadyPublishedTotalPrivacyBudget = totalBudget;
                this.workers[j].alreadyPublishedNoiseDistanceAndBudget.add(new DistanceBudgetPair(newNoiseDistance, newPrivacyBudget));
                this.workers[j].effectiveNoiseDistance = competeDistance;
                this.workers[j].effectivePrivacyBudget = effectivePrivacyBudget;
                this.workers[j].completeUtilityFunctionValue = newUtilityValue;
                this.workers[j].privacyBudgetCost = newCostPrivacyBudget;
                this.workers[j].budgetIndex ++;
//                candidateWorkerDistanceAndBudget.add(new double[]{competeDistance, totalBudget});

            }

            if (candidateWorkerIDList.size() == 0) {
                // 此处等价于continue;
                break;
            }

            // 服务器遍历每个worker，找出竞争成功的worker，修改他的competeState为false，将其他竞争状态为true的workerBudgetIndex值加1
            // todo: 设置 taskTempWinnerID 和 taskTempWinnerInfo. 需要额外更新胜利者的 currentUtilityFunctionValue，被竞争下去的失败者的 currentUtilityFunctionValue
            //继续一轮竞争，直到除了胜利者其他的竞争者的competeState都为false.
            Integer taskWinnerIDBefore = taskWinnerID;
            for (Integer j : candidateWorkerIDArray) {
//                competeTemp = LaplaceProbabilityDensityFunction.probabilityDensityFunction(this.workers[i].toCompetePublishEverageNoiseDistance, taskTempWinnerInfo[0], this.workers[i].toCompetePublishTotalPrivacyBudget, taskTempWinnerInfo[1]);
                competeTemp = LaplaceProbabilityDensityFunction.probabilityDensityFunction(this.workers[j].effectiveNoiseDistance, taskWinnerInfo[DISTANCE_TAG], this.workers[j].effectivePrivacyBudget, taskWinnerInfo[BUDGET_TAG]);

                if (competeTemp > 0.5) {
                    taskWinnerID = j;
//                    taskTempWinnerInfo[i] = this.workers[i].toCompetePublishEverageNoiseDistance;
//                    taskTempWinnerInfo[i] = this.workers[i].toCompetePublishTotalPrivacyBudget;
                    taskWinnerInfo[DISTANCE_TAG] = this.workers[j].effectiveNoiseDistance;
                    taskWinnerInfo[BUDGET_TAG] = this.workers[j].effectivePrivacyBudget;
                }
            }
            // 调整竞争成功的worker和被竞争下去的worker
            if (!taskWinnerIDBefore.equals(taskWinnerID)) {
                if (!taskWinnerIDBefore.equals(-1)) {
                    this.workers[taskWinnerIDBefore].currentUtilityFunctionValue -= this.task.valuation;
                }
                this.workers[taskWinnerID].currentUtilityFunctionValue = this.workers[taskWinnerID].completeUtilityFunctionValue;
            }

//            Integer beforeWinnerID = this.task

        }
        System.out.println("The winner worker's id is " + taskWinnerID);
        System.out.println("The winner worker's noise distance is " + taskWinnerInfo[DISTANCE_TAG]);
        System.out.println("The winner worker's budget is " + taskWinnerInfo[BUDGET_TAG]);
    }

    private void initializeCandidateWorkers(List<Integer> candidateWorkerIDList) {
        for (int i = 0; i < this.workers.length; i++) {
            candidateWorkerIDList.add(i);
        }
    }


    private DistanceBudgetPair getNewEffectiveNoiseDistanceAndPrivacyBudget(Integer workerID, double newNoiseDistance, double newPrivacyBudget) {
        TreeSet<DistanceBudgetPair> tempTreeSet = new TreeSet<>();
        tempTreeSet.addAll(this.workers[workerID].alreadyPublishedNoiseDistanceAndBudget);
        tempTreeSet.add(new DistanceBudgetPair(newNoiseDistance, newPrivacyBudget));
        double[] distanceBudget = LaplaceUtils.getMaximumLikelihoodEstimationInGivenPoint(tempTreeSet);
        return new DistanceBudgetPair(distanceBudget[0], distanceBudget[1]);
    }

    public static void main(String[] args) {
        String basicPath = System.getProperty("user.dir") + "\\dataset\\test_dataset\\_1_single_task_dataset\\test1\\";
        String taskPointPath = basicPath + "task_point.txt";
        String taskValuePath = basicPath + "task_value.txt";
        String workerPointPath = basicPath + "worker_point.txt";
        String workerPrivacyBudgetPath = basicPath + "worker_privacy_budget.txt";

        List<Point> taskPointList = PointRead.readPoint(taskPointPath);
        Double[] taskValueArray = DoubleRead.readDouble(taskValuePath);
        List<Point> workerPointList = PointRead.readPoint(workerPointPath);
        List[] workerPrivacyBudgetList = DoubleRead.readDoubleList(workerPrivacyBudgetPath);


        MyPrint.showList(taskPointList);
        MyPrint.showDoubleArray(taskValueArray);
        MyPrint.showList(workerPointList);
        MyPrint.showListArray(workerPrivacyBudgetList);

        SingleTaskSolution singleTaskSolution = new SingleTaskSolution();
        singleTaskSolution.initializeBasicInformation(taskPointList, taskValueArray, workerPointList, workerPrivacyBudgetList);
        singleTaskSolution.initializeAgents();
        singleTaskSolution.complete();



    }


}
