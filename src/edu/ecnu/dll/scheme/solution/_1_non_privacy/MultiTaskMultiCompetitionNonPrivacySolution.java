package edu.ecnu.dll.scheme.solution._1_non_privacy;

import edu.ecnu.dll.basic_struct.pack.single_agent_info.sub_class.WorkerIDDistancePair;
import edu.ecnu.dll.basic_struct.pack.single_agent_info.sub_class.sub_class.TaskTargetNonPrivacyInfo;
import edu.ecnu.dll.scheme.solution.Solution;
import edu.ecnu.dll.scheme.struct.task.BasicTask;
import edu.ecnu.dll.basic_struct.agent.Task;
import edu.ecnu.dll.scheme.struct.worker.MultiTaskNonPrivacyWorker;
import tools.basic.BasicArray;
import tools.basic.BasicCalculation;
import tools.struct.Point;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class MultiTaskMultiCompetitionNonPrivacySolution extends Solution {



    public List<Integer> tempCandidateTaskList = null;

    public Task[] tasks = null;
    public MultiTaskNonPrivacyWorker[] workers = null;



    // Task value 保证是[0,1]之间的值
    protected double getUtilityValue(double taskValue, double realDistance) {
        return taskValue * 2  - alpha * realDistance;
    }




//    public void initializeBasicInformation(List<Point> taskPositionList, Double[] taskValueArray, List<Point> workerPositionList, List<Double> workerRangeList) {
//        Point taskPosition, workerPosition;
//        this.tasks = new BasicTask[taskPositionList.size()];
//        for (int i = 0; i < taskPositionList.size(); i++) {
//            taskPosition = taskPositionList.get(i);
//            this.tasks[i] = new BasicTask(taskPosition.getIndex());
//            this.tasks[i].valuation = taskValueArray[i];
//        }
//        this.workers = new MultiTaskNonPrivacyWorker[workerPositionList.size()];
//        for (int j = 0; j < workers.length; j++) {
//            workerPosition = workerPositionList.get(j);
//            this.workers[j] = new MultiTaskNonPrivacyWorker(workerPosition.getIndex());
//            this.workers[j].setMaxRange(workerRangeList.get(j));
//        }
//        BasicArray.setIntegerListToContinuousNaturalNumber(this.tempCandidateTaskList, this.tasks.length - 1);
//    }

    public void initializeBasicInformation(List<Point> taskPositionList, Double[] taskValueArray, List<Point> workerPositionList, List<Double> workerRangeList) {
        Point taskPosition, workerPosition;
        this.tasks = new BasicTask[taskPositionList.size()];
        for (int i = 0; i < taskPositionList.size(); i++) {
            taskPosition = taskPositionList.get(i);
            this.tasks[i] = new BasicTask(taskPosition.getIndex());
            this.tasks[i].valuation = taskValueArray[i];
        }
        this.workers = new MultiTaskNonPrivacyWorker[workerPositionList.size()];
        for (int j = 0; j < workers.length; j++) {
            workerPosition = workerPositionList.get(j);
            this.workers[j] = new MultiTaskNonPrivacyWorker(workerPosition.getIndex());
//            this.workers[j].privacyBudgetArray = privacyBudgetListArray[j].toArray(new Double[0][0]);
//            this.workers[j].setPrivacyBudgetArray(privacyBudgetListArray[j].toArray(new Double[0][0]));
            this.workers[j].setMaxRange(workerRangeList.get(j));
            this.workers[j].taskIndex = BasicArray.getInitializedArray(-1, this.tasks.length);
            this.workers[j].currentWinningState = false;
        }
        BasicArray.setIntegerListToContinuousNaturalNumber(this.tempCandidateTaskList, this.tasks.length - 1);

    }

//    public void initializeAgents() {
//        for (int j = 0; j < this.workers.length; j++) {
//            for (int i = 0; i < tasks.length; i++) {
//                this.workers[j].setToTaskDistance(i, BasicCalculation.get2Norm(this.tasks[i].location, this.workers[i].location));
//                this.workers[j].currentUtilityFunctionValue[i] = 0.0;
//            }
//        }
//    }

    public void initializeAgents() {
        for (int j = 0; j < this.workers.length; j++) {
            this.workers[j].reverseIndex = new ArrayList<>();
            this.workers[j].toTaskDistance = new ArrayList<>();
            // 此处没有初始化privacyBudgetArray，因为会在initialize basic function 中初始化
            this.workers[j].competingTimes = new ArrayList<>();
            this.workers[j].currentUtilityFunctionValue = new ArrayList<>();
            this.workers[j].successfullyUtilityFunctionValue = new ArrayList<>();
            double tempDistance;
            int k = 0;
            for (int i = 0; i < tasks.length; i++) {
                // todo: 修改为经纬度的
                tempDistance = BasicCalculation.get2Norm(this.tasks[i].location, this.workers[j].location);
                if (tempDistance <= this.workers[j].maxRange) {
                    this.workers[j].taskIndex[i] = k++;
                    this.workers[j].reverseIndex.add(i);
                    this.workers[j].toTaskDistance.add(tempDistance);
                    this.workers[j].competingTimes.add(0);
                    this.workers[j].currentUtilityFunctionValue.add(0.0);
                    this.workers[j].successfullyUtilityFunctionValue.add(0.0);
                }
            }
        }
    }

    public void initializeAgentsWithLatitudeLongitude() {
        for (int j = 0; j < this.workers.length; j++) {
            this.workers[j].reverseIndex = new ArrayList<>();
            this.workers[j].toTaskDistance = new ArrayList<>();
            // 此处没有初始化privacyBudgetArray，因为会在initialize basic function 中初始化
            this.workers[j].competingTimes = new ArrayList<>();
            this.workers[j].currentUtilityFunctionValue = new ArrayList<>();
            this.workers[j].successfullyUtilityFunctionValue = new ArrayList<>();
            double tempDistance;
            int k = 0;
            for (int i = 0; i < tasks.length; i++) {
                // todo: 修改为经纬度的
                tempDistance = BasicCalculation.getDistanceFrom2LngLat(this.tasks[i].location[1], this.tasks[i].location[0], this.workers[j].location[1],this.workers[j].location[0]);
                if (tempDistance <= this.workers[j].maxRange) {
                    this.workers[j].taskIndex[i] = k++;
                    this.workers[j].reverseIndex.add(i);
                    this.workers[j].toTaskDistance.add(tempDistance);
                    this.workers[j].competingTimes.add(0);
                    this.workers[j].currentUtilityFunctionValue.add(0.0);
                    this.workers[j].successfullyUtilityFunctionValue.add(0.0);
                }
            }
        }
    }

    protected void initializeAllocationByFirstTaskAndNullAllocation(List<Integer>[] newCandidateWorkerIDList, WorkerIDDistancePair[] taskCurrentWinnerPackedArray, Integer[] competingTimes, HashSet<Integer>[] completedWorkerIDSet) {
        // 针对每个task，初始化距离为最大距离值
        // 针对每个task，初始化总的被竞争次数为0
        // 针对每个task，初始化访问过被访问worker集合为空集合
        for (int i = 0; i < this.tasks.length; i++) {
            newCandidateWorkerIDList[i] = new ArrayList<>();
            taskCurrentWinnerPackedArray[i] = new WorkerIDDistancePair();
            taskCurrentWinnerPackedArray[i].setWorkerID(-1);
            taskCurrentWinnerPackedArray[i].setDistance(Double.MAX_VALUE);
            competingTimes[i] = 0;
            completedWorkerIDSet[i] = new HashSet<>();
        }
        for (int i = 0; i < this.workers.length; i++) {
            newCandidateWorkerIDList[0].add(i);
        }
    }

    /**
     * 通过比较各个utility函数值选出最大的utility的task及其相关信息
     * @param taskIDList
     * @param workerID
     * @param lastTermTaskWinnerPackedArray
     * @return
     */
    protected TaskTargetNonPrivacyInfo chooseByUtilityFunction(List<Integer> taskIDList, Integer workerID, WorkerIDDistancePair[] lastTermTaskWinnerPackedArray) {
        Integer taskID = null;
        double maxUtilityValue = Double.MIN_VALUE;
        Double distance = null;
        for (Integer i : taskIDList) {
            if (lastTermTaskWinnerPackedArray[i].getWorkerID().equals(workerID)) {
                continue;
            }
            // 距离判断
            if (this.workers[workerID].getToTaskDistance(i) >= lastTermTaskWinnerPackedArray[i].getDistance()) {
                continue;
            }

            /**
             * 比较 utility函数，选出最大。
             *
             * 先比较候选utility是否大于之前对应的utility，不大于则舍弃。
             *
             */
            // Utility 函数判断
            Double tempNewUtilityValue = this.getUtilityValue(this.tasks[i].valuation, this.workers[workerID].getToTaskDistance(i));

            if (tempNewUtilityValue <= this.workers[workerID].getSuccessfullyUtilityFunctionValue(i)) {
                continue;
            }


            if (tempNewUtilityValue > maxUtilityValue) {
                maxUtilityValue = tempNewUtilityValue;
                taskID = i;
                distance = this.workers[workerID].getToTaskDistance(i);
            }

        }
        if (taskID == null) {
            return null;
        }
        return new TaskTargetNonPrivacyInfo(taskID, distance, maxUtilityValue, maxUtilityValue);
    }


    protected TaskTargetNonPrivacyInfo chooseByTaskEntropy(List<Integer> taskIDList, Integer workerID, Integer[] totalCompetingTimesList, WorkerIDDistancePair[] lastTermTaskWinnerPackedArray, HashSet<Integer>[] competingWorkerIDSetArray) {
        // 遍历taskIDList中的所有task，找出能使当前worker的utility增加最大的task，返回（该task的ID, 申请即将泄露的平均distance[此处可返回扰动], 申请泄露的budget总和[此处可返回当前budget]）
        Integer taskID = null;
        Double distance = null;
        double candidateTaskEntropy = -1;
        Double newUtilityValue = null;
        for (Integer i : taskIDList) {
            if (lastTermTaskWinnerPackedArray[i].getWorkerID().equals(workerID)) {
                continue;
            }
            // 距离 判断
            if (this.workers[workerID].getToTaskDistance(i) >= lastTermTaskWinnerPackedArray[i].getDistance()) {
                continue;
            }
//            Double tempNewCostPrivacyBudget = getNewCostPrivacyBudget(workerID, i);
//            Double tempNewPrivacyBudget =  this.workers[workerID].privacyBudgetArray[i][this.workers[workerID].budgetIndex[i]];
//            Double tempNewNoiseDistance = this.workers[workerID].toTaskDistance[i] + LaplaceUtils.getLaplaceNoise(1, tempNewPrivacyBudget);
//            DistanceBudgetPair newEffectiveDistanceBudgetPair = getNewEffectiveNoiseDistanceAndPrivacyBudget(workerID, i, tempNewNoiseDistance, tempNewPrivacyBudget);
//            double tempCompeteDistance = newEffectiveDistanceBudgetPair.distance;
//            double tempEffectivePrivacyBudget = newEffectiveDistanceBudgetPair.budget;

            // Utility 函数值判断
            Double tempNewUtilityValue = this.getUtilityValue(this.tasks[i].valuation, this.workers[workerID].getToTaskDistance(i));
            if (tempNewUtilityValue <= this.workers[workerID].getSuccessfullyUtilityFunctionValue(i)) {
                continue;
            }

            // 根据 taskEntropy 挑选
            double taskEntropy = getTaskEntropy(i, totalCompetingTimesList[i], competingWorkerIDSetArray[i]);
            if (taskEntropy > candidateTaskEntropy) {
                candidateTaskEntropy = taskEntropy;
                taskID = i;
                distance = this.workers[workerID].getToTaskDistance(i);
                newUtilityValue = tempNewUtilityValue;
            }

        }
        if (taskID == null) {
            return null;
        }
        return new TaskTargetNonPrivacyInfo(taskID, distance, candidateTaskEntropy, newUtilityValue);
    }


    public WorkerIDDistancePair[] complete() {

        // 记录每个task的被竞争的总次数
        Integer[] competingTimes = new Integer[this.tasks.length];
        // 记录每个task被竞争过的worker id的集合
        HashSet<Integer>[] completedWorkerIDSet = new HashSet[this.tasks.length];

        // 记录worker竞争时临时效用函数值
        double[] tempUtilityArray = new double[this.workers.length];


        // 针对每个task，记录当前竞争成功的worker的ID和距离
        WorkerIDDistancePair[] taskCurrentWinnerPackedArray = new WorkerIDDistancePair[this.tasks.length];

        // todo: 封装到了initializeAllocations

        // 针对每个task，本轮提出竞争的worker的ID（每轮需要清空）
        List<Integer>[] newCandidateWorkerIDList, oldCandidateWorkerIDList;
        newCandidateWorkerIDList = new ArrayList[this.tasks.length];
//        BasicArray.setListArrayToEmptyList(newCandidateWorkerIDList);
        initializeAllocationByFirstTaskAndNullAllocation(newCandidateWorkerIDList, taskCurrentWinnerPackedArray, competingTimes, completedWorkerIDSet);


        // 用于记录当前竞争的总的worker数量
        int totalCompleteWorkerNumber = this.workers.length;

        while (totalCompleteWorkerNumber > 0) {
//        while (!candidateWorkerIDList.isEmpty()) {
            totalCompleteWorkerNumber = 0;

            oldCandidateWorkerIDList = newCandidateWorkerIDList;
            newCandidateWorkerIDList = new ArrayList[oldCandidateWorkerIDList.length];
            BasicArray.setListArrayToEmptyList(newCandidateWorkerIDList);

            /*
             * 遍历每个 task 对应的候选集合中的worker。
             * 每个 worker 对所有的 tasks 进行竞争。但只能挑出其中 1 个 task 作为最终竞争对象。
             * 每轮结束后统计剩余的总的将要竞争的 workers 的数量。
             */
            for (int i = 0; i < oldCandidateWorkerIDList.length; i++) {

                for (Integer j : oldCandidateWorkerIDList[i]) {
                    //进行是否竞争判断1：如果当前 worker 不需要竞争(是上轮的胜利者)，就不作为
                    if (j.equals(taskCurrentWinnerPackedArray[i].getWorkerID())) {
                        continue;
                    }

                    // 进行是否竞争判断2：计算出所有预算充足的 task ID 的集合。如果该集合为空，则不作为（非隐私下不需要这一步）


                    // 进行是否竞争判断3： 考察 utility函数、PPCF函数、PCF函数、任务熵

                    TaskTargetNonPrivacyInfo winnerInfo;
                    // todo: 暂时用一个函数测试task entropy 和 proposing value。 由于task entropy需要统计竞争次数，因此影响时间，需要和proposing value 分开对比
//                    winnerInfo = chooseByTaskEntropy(tempCandidateTaskList, j, competingTimes, taskCurrentWinnerPackedArray, completedWorkerIDSet);
                    winnerInfo = chooseByUtilityFunction(tempCandidateTaskList, j, taskCurrentWinnerPackedArray);
                    if (winnerInfo == null) {
                        continue;
                    }

                    // 否则（竞争成功），发布当前扰动距离长度和隐私预算(这里只添加进候选列表供server进一步选择)，并将隐私自己的预算索引值加1
                    newCandidateWorkerIDList[winnerInfo.getTaskID()].add(j);
                    completedWorkerIDSet[winnerInfo.getTaskID()].add(j);
//                    this.workers[j].alreadyPublishedNoiseDistanceAndBudgetTreeSetArray[winnerInfo.getTaskID()].add(new DistanceBudgetPair(winnerInfo.getNewNoiseDistance(), winnerInfo.getNewPrivacyBudget()));
//                    this.workers[j].effectiveNoiseDistance[winnerInfo.getTaskID()] = winnerInfo.getNoiseEffectiveDistance();
//                    this.workers[j].effectivePrivacyBudget[winnerInfo.getTaskID()] = winnerInfo.getEffectivePrivacyBudget();
                    this.workers[j].setSuccessfullyUtilityFunctionValue(winnerInfo.getTaskID(), winnerInfo.getNewUtilityValue());
//                    this.workers[j].budgetIndex[winnerInfo.getTaskID()] ++;

//                    this.workers[j].taskCompetingTimes[winnerInfo.getTaskID()] ++;
                    this.workers[j].increaseTaskCompetingTimes(winnerInfo.getTaskID());
                    totalCompleteWorkerNumber ++;

                    competingTimes[winnerInfo.getTaskID()] ++;

                }
            }


            chosenByServer(taskCurrentWinnerPackedArray, newCandidateWorkerIDList);
        }
        return taskCurrentWinnerPackedArray;
    }

    protected void chosenByServer(WorkerIDDistancePair[] taskCurrentWinnerPackedArray, List<Integer>[] newCandidateWorkerIDList) {
        WorkerIDDistancePair[] taskBeforeWinnerPackedArray = new WorkerIDDistancePair[newCandidateWorkerIDList.length];
        for (int i = 0; i < newCandidateWorkerIDList.length; i++) {
            if (newCandidateWorkerIDList[i].size() == 0) {
                continue;
            }
            taskBeforeWinnerPackedArray[i] = taskCurrentWinnerPackedArray[i];
            for (Integer j : newCandidateWorkerIDList[i]) {
                if (this.workers[j].getToTaskDistance(i) < taskCurrentWinnerPackedArray[i].getDistance()) {
                    taskCurrentWinnerPackedArray[i].setWorkerID(j);
                    taskCurrentWinnerPackedArray[i].setDistance(this.workers[j].getToTaskDistance(i));
                }
            }
            if (!taskCurrentWinnerPackedArray[i].getWorkerID().equals(taskBeforeWinnerPackedArray[i].getWorkerID())) {
                if (!taskBeforeWinnerPackedArray[i].getWorkerID().equals(-1)) {
                    // todo: 封装
//                    this.workers[taskBeforeWinnerPackedArray[i].getWorkerID()].currentUtilityFunctionValue[i] -= this.tasks[i].valuation;
                    this.workers[taskBeforeWinnerPackedArray[i].getWorkerID()].increaseSuccessfullyUtilityFunctionValue(i, -this.tasks[i].valuation);
                    // 将被竞争下去的worker加入竞争集合，以便下轮判断是否可以竞争
                    newCandidateWorkerIDList[i].add(taskBeforeWinnerPackedArray[i].getWorkerID());
                }
                this.workers[taskCurrentWinnerPackedArray[i].getWorkerID()].setCurrentUtilityFunctionValue(i, this.workers[taskCurrentWinnerPackedArray[i].getWorkerID()].getSuccessfullyUtilityFunctionValue(i));
            }
        }
    }


}
