package edu.ecnu.dll.scheme.solution._2_single_task;

import edu.ecnu.dll.basic_struct.pack.single_agent_info.sub_class.DistanceBudgetPair;
import edu.ecnu.dll.basic_struct.pack.single_agent_info.sub_class.WorkerIDNoiseDistanceBudgetPair;
import edu.ecnu.dll.scheme.solution._0_basic.Solution;
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

public class SingleTaskSolution extends Solution {

    public static final int DISTANCE_TAG = 0;
    public static final int BUDGET_TAG = 1;

    public static final double alpha = 1;
    public static final double beta = 1;



    public Task task = null;
    public SingleTaskBasicWorker[] workers = null;


    /**
     *
     */
    protected double getUtilityValue(double taskValue, double effectivePrivacyBudget, double realDistance, double privacyBudgetCost) {
//        return taskValue + taskValue * effectivePrivacyBudget - alpha * realDistance - beta * privacyBudgetCost;
        return taskValue + taskValue * toNormalValue(effectivePrivacyBudget) - alpha * realDistance - beta * privacyBudgetCost;
    }
    private DistanceBudgetPair getNewEffectiveNoiseDistanceAndPrivacyBudget(Integer workerID, double newNoiseDistance, double newPrivacyBudget) {
        TreeSet<DistanceBudgetPair> tempTreeSet = new TreeSet<>();
        tempTreeSet.addAll(this.workers[workerID].alreadyPublishedNoiseDistanceAndBudget);
        tempTreeSet.add(new DistanceBudgetPair(newNoiseDistance, newPrivacyBudget));
        double[] distanceBudget = LaplaceUtils.getMaximumLikelihoodEstimationInGivenPoint(tempTreeSet);
        return new DistanceBudgetPair(distanceBudget[0], distanceBudget[1]);
    }
    private Double getNewCostPrivacyBudget(Integer workerID) {
        Double result;
        result = this.workers[workerID].privacyBudgetCost + this.workers[workerID].privacyBudgetArray[this.workers[workerID].budgetIndex];
        return result;
    }



    public void initializeBasicInformation(List<Point> taskPositionList, Double[] taskValueArray, List<Point> workerPositionList, List<Double>[] privacyBudgetListArray) {
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
    private void initializeCandidateWorkers(List<Integer> candidateWorkerIDList) {
        for (int i = 0; i < this.workers.length; i++) {
            candidateWorkerIDList.add(i);
        }
    }




    public WorkerIDNoiseDistanceBudgetPair compete() {


        // 记录worker竞争时临时效用函数值
        double[] tempUtilityArray = new double[this.workers.length];

        // 记录每个worker的申请到的task列表
//        todo: whether to add:  List<Integer>[] allocatedTaskIDListArray = new ArrayList[this.workers.length];

        // 针对该task，记录当前竞争成功的worker的ID，初始化为-1
        // 针对该task，初始化距离为最大距离值
        // 针对该task，初始化对应距离的隐私预算为最大隐私预算
        WorkerIDNoiseDistanceBudgetPair taskWinnerInfo = new WorkerIDNoiseDistanceBudgetPair(-1, Double.MAX_VALUE, Double.MAX_VALUE);

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
                if (j.equals(taskWinnerInfo.getWorkerID())) {
                    continue;
                }

                // 进行是否竞争判断2：如果隐私预算数量不足，不作为
                if (this.workers[j].budgetIndex >= this.workers[j].privacyBudgetArray.length) {
//                    competeState[i] = false;
                    continue;
                }

                // 进行是否竞争判断3：如果PPCF函数计算出来的距离大于之前胜利者的距离，不作为
                //todo: 假设扰动距离的均值精度更高
                if (this.workers[j].toTaskDistance >= taskWinnerInfo.getEffectiveNoiseDistance()) {
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
                if (newUtilityValue <= this.workers[j].successfulUtilityFunctionValue) {
                    continue;
                }



                // 进行是否竞争判断5：根据将要竞争的预算，计算扰动的距离值。如果PCF函数计算出来的距离大于之前胜利者的距离，不作为
//                double competeDistance = BasicCalculation.getAverage(this.workers[i].alreadyPublishedAverageNoiseDistance, newNoiseDistance, this.workers[i].budgetIndex + 1);
//                double totalBudget = this.workers[i].alreadyPublishedTotalPrivacyBudget + this.workers[i].privacyBudgetArray[this.workers[i].budgetIndex];
//                DistanceBudgetPair newEffectiveDistanceBudgetPair = getNewEffectiveNoiseDistanceAndPrivacyBudget(i, newNoiseDistance, this.workers[i].privacyBudgetArray[this.workers[i].budgetIndex]);
                double competeDistance = newEffectiveDistanceBudgetPair.distance;
                double effectivePrivacyBudget = newEffectiveDistanceBudgetPair.budget;
                double competeValue = LaplaceProbabilityDensityFunction.probabilityDensityFunction(competeDistance, taskWinnerInfo.getEffectiveNoiseDistance(), effectivePrivacyBudget, taskWinnerInfo.getEffectivePrivacyBudget());
                if (competeValue <= 0.5) {
//                    competeState[i] = false;
                    continue;
                }


                // 否则（竞争成功），发布当前扰动距离长度和隐私预算(这里只添加进候选列表供server进一步选择)，并将隐私自己的预算索引值加1
                candidateWorkerIDList.add(j);
                this.workers[j].alreadyPublishedNoiseDistanceAndBudget.add(new DistanceBudgetPair(newNoiseDistance, newPrivacyBudget));
                this.workers[j].effectiveNoiseDistance = competeDistance;
                this.workers[j].effectivePrivacyBudget = effectivePrivacyBudget;
                this.workers[j].successfulUtilityFunctionValue = newUtilityValue;
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
            Integer taskWinnerIDBefore = taskWinnerInfo.getWorkerID();
            for (Integer j : candidateWorkerIDArray) {
//                competeTemp = LaplaceProbabilityDensityFunction.probabilityDensityFunction(this.workers[i].toCompetePublishEverageNoiseDistance, taskTempWinnerInfo[0], this.workers[i].toCompetePublishTotalPrivacyBudget, taskTempWinnerInfo[1]);
                competeTemp = LaplaceProbabilityDensityFunction.probabilityDensityFunction(this.workers[j].effectiveNoiseDistance, taskWinnerInfo.getEffectiveNoiseDistance(), this.workers[j].effectivePrivacyBudget, taskWinnerInfo.getEffectivePrivacyBudget());

                if (competeTemp > 0.5) {
                    taskWinnerInfo.setWorkerID(j);
                    taskWinnerInfo.setEffectiveNoiseDistance(this.workers[j].effectiveNoiseDistance);
                    taskWinnerInfo.setEffectivePrivacyBudget(this.workers[j].effectivePrivacyBudget);
                }
            }
            // 调整竞争成功的worker和被竞争下去的worker
            if (!taskWinnerIDBefore.equals(taskWinnerInfo.getWorkerID())) {
                if (!taskWinnerIDBefore.equals(-1)) {
                    this.workers[taskWinnerIDBefore].currentUtilityFunctionValue -= this.task.valuation;
                    // 将被竞争下去的worker加入竞争集合，以便下轮判断是否可以竞争
                    candidateWorkerIDList.add(taskWinnerIDBefore);
                }
                this.workers[taskWinnerInfo.getWorkerID()].currentUtilityFunctionValue = this.workers[taskWinnerInfo.getWorkerID()].successfulUtilityFunctionValue;
            }

//            Integer beforeWinnerID = this.task

        }
        System.out.println("The winner worker's id is " + taskWinnerInfo.getWorkerID());
        System.out.println("The winner worker's noise distance is " + taskWinnerInfo.getEffectiveNoiseDistance());
        System.out.println("The winner worker's real distance is " + this.workers[taskWinnerInfo.getWorkerID()].toTaskDistance);
        System.out.println("The winner worker's budget is " + taskWinnerInfo.getEffectivePrivacyBudget());


        return taskWinnerInfo;

    }


    public static void main(String[] args) {
        String basicPath = System.getProperty("user.dir") + "\\dataset\\test_dataset\\_1_single_task_dataset\\test1\\";
        String taskPointPath = basicPath + "task_point.txt";
        String taskValuePath = basicPath + "task_value.txt";
        String workerPointPath = basicPath + "worker_point.txt";
        String workerPrivacyBudgetPath = basicPath + "worker_privacy_budget.txt";

        List<Point> taskPointList = PointRead.readPointWithFirstLineCount(taskPointPath);
        Double[] taskValueArray = DoubleRead.readDouble(taskValuePath);
        List<Point> workerPointList = PointRead.readPointWithFirstLineCount(workerPointPath);
        List[] workerPrivacyBudgetList = DoubleRead.readDoubleList(workerPrivacyBudgetPath);


        MyPrint.showList(taskPointList);
        MyPrint.showDoubleArray(taskValueArray);
        MyPrint.showList(workerPointList);
        MyPrint.showListArray(workerPrivacyBudgetList);

        SingleTaskSolution singleTaskSolution = new SingleTaskSolution();
        singleTaskSolution.initializeBasicInformation(taskPointList, taskValueArray, workerPointList, workerPrivacyBudgetList);
        singleTaskSolution.initializeAgents();
        singleTaskSolution.compete();



    }


}
