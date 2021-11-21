package edu.ecnu.dll._deprecated;

import edu.ecnu.dll.basic.basic_struct.pack.single_agent_info.sub_class.WorkerIDNoiseDistanceBudgetPair;
import edu.ecnu.dll.basic.basic_solution.Solution;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Deprecated
public class MultiTaskSingleCompetitionSolution extends Solution {



//    public Task[] tasks = null;
//    public MultiTaskBasicWorker[] workers = null;
//
//    public static final int budgetSize = 3;







    protected void initializeAllocationByFirstTaskAndNullAllocation(List<Integer>[] newCandidateWorkerIDList, WorkerIDNoiseDistanceBudgetPair[] taskCurrentWinnerPackedArray, Integer[] competingTimes, HashSet<Integer>[] completedWorkerIDSet) {
        // 针对每个task，初始化距离为最大距离值
        // 针对每个task，初始化对应距离的隐私预算为最大隐私预算
        // 针对每个task，初始化总的被竞争次数为0
        // 针对每个task，初始化访问过被访问worker集合为空集合
        for (int i = 0; i < this.tasks.length; i++) {
            newCandidateWorkerIDList[i] = new ArrayList<>();
            taskCurrentWinnerPackedArray[i] = new WorkerIDNoiseDistanceBudgetPair();
            taskCurrentWinnerPackedArray[i].setWorkerID(-1);
            taskCurrentWinnerPackedArray[i].setEffectiveNoiseDistance(Double.MAX_VALUE);
            taskCurrentWinnerPackedArray[i].setEffectivePrivacyBudget(Double.MAX_VALUE);
            competingTimes[i] = 0;
            completedWorkerIDSet[i] = new HashSet<>();
        }
        for (int i = 0; i < this.workers.length; i++) {
            newCandidateWorkerIDList[0].add(i);
        }
    }


    /**
     * 3种worker的选择函数：根据效用函数、根据最大任务熵、根据最大请求值
     */
    /**
     * 通过比较各个utility函数值选出最大的utility的task及其相关信息
     * @param taskIDList
     * @param workerID
     * @param lastTermTaskWinnerPackedArray
     * @return
     */
//    protected TaskTargetInfo chooseByUtilityFunction(List<Integer> taskIDList, Integer workerID, WorkerIDDistanceBudgetPair[] lastTermTaskWinnerPackedArray) {
//        Integer taskID = null;
//        Double noiseEfficientDistance = null;
//        Double effectivePrivacyBudget = null;
////        double candidateProposingValue = Double.MIN_VALUE;
//        double maxUtilityValue = Double.MIN_VALUE;
//        Double newCostPrivacyBudget = null, newPrivacyBudget = null, newNoiseDistance = null, newUtilityValue = null;
//        for (Integer i : taskIDList) {
//            if (lastTermTaskWinnerPackedArray[i].getWorkerID().equals(workerID)) {
//                continue;
//            }
//            // PPCF 判断
////            if (this.workers[workerID].toTaskDistance[i] >= lastTermTaskWinnerPackedArray[i].getNoiseEffectiveDistance()) {
////                continue;
////            }
//            if (this.workers[workerID].getToTaskDistance(i) >= lastTermTaskWinnerPackedArray[i].getNoiseEffectiveDistance()) {
//                continue;
//            }
//
//            Double tempNewCostPrivacyBudget = getNewCostPrivacyBudget(workerID, i);
//            Double tempNewPrivacyBudget =  this.workers[workerID].getPrivacyBudgetArray(i)[this.workers[workerID].getBudgetIndex(i)];
////            Double tempNewNoiseDistance = this.workers[workerID].getToTaskDistance(i) + LaplaceUtils.getLaplaceNoise(1, tempNewPrivacyBudget);
//            Double tempNewNoiseDistance = this.workers[workerID].getNoiseDistanceArray(i)[this.workers[workerID].getBudgetIndex(i)];
//            this.workers[workerID].increaseBudgetIndex(i);
//
//            DistanceBudgetPair newEffectiveDistanceBudgetPair = getNewEffectiveNoiseDistanceAndPrivacyBudget(workerID, i, tempNewNoiseDistance, tempNewPrivacyBudget);
//            double tempCompeteDistance = newEffectiveDistanceBudgetPair.distance;
//            double tempEffectivePrivacyBudget = newEffectiveDistanceBudgetPair.budget;
//
//            // PCF 判断
//            double pcfValue = LaplaceProbabilityDensityFunction.probabilityDensityFunction(tempCompeteDistance, lastTermTaskWinnerPackedArray[i].getNoiseEffectiveDistance(), tempEffectivePrivacyBudget, lastTermTaskWinnerPackedArray[i].getEffectivePrivacyBudget());
//            if (pcfValue <= 0.5) {
//                continue;
//            }
//
//            /**
//             * 比较 utility函数，选出最大。
//             *
//             * 先比较候选utility是否大于之前对应的utility，不大于则舍弃。
//             *
//             */
//            // Utility 函数判断
//            Double tempNewUtilityValue = this.getUtilityValue(this.tasks[i].valuation, tempEffectivePrivacyBudget, this.workers[workerID].getToTaskDistance(i), tempNewCostPrivacyBudget);
//
////            if (tempNewUtilityValue <= this.workers[workerID].successfullyUtilityFunctionValue[i]) {
////                continue;
////            }
//            if (tempNewUtilityValue <= 0) {
//                continue;
//            }
//
//
//            if (tempNewUtilityValue > maxUtilityValue) {
//                maxUtilityValue = tempNewUtilityValue;
//                taskID = i;
//                noiseEfficientDistance = tempCompeteDistance;
//                effectivePrivacyBudget = tempEffectivePrivacyBudget;
//                newCostPrivacyBudget = tempNewCostPrivacyBudget;
//                newPrivacyBudget = tempNewPrivacyBudget;
//                newNoiseDistance = tempNewNoiseDistance;
//                newUtilityValue = tempNewUtilityValue;
//            }
//
//        }
//        if (taskID == null) {
//            return null;
//        }
//        return new TaskTargetInfo(taskID, noiseEfficientDistance, effectivePrivacyBudget, maxUtilityValue, newCostPrivacyBudget, newPrivacyBudget, newNoiseDistance, newUtilityValue);
//    }
    /**
     *
     * @param taskIDList 候选的要去竞争的所有task(已经排除budget不足的task)
     * @param workerID 要竞争的worker的ID
     * @param totalCompetingTimesList 记录当前总的轮竞争每个task的次数
     * @param lastTermTaskWinnerPackedArray 记录上轮竞争中成功竞争每个task的worker其他信息
     * @param competingWorkerIDSetArray 竞争过某个 task 的worker ID 的集合的列标
     * @return 经过考虑utility函数，PPCF，PCF，task熵之后选择的taskID以及对应要的worker要修改的有效噪声距离，有效隐私预算和task熵值
     */
//    protected TaskTargetInfo chooseByTaskEntropy(List<Integer> taskIDList, Integer workerID, Integer[] totalCompetingTimesList, WorkerIDDistanceBudgetPair[] lastTermTaskWinnerPackedArray, HashSet<Integer>[] competingWorkerIDSetArray) {
//        // 遍历taskIDList中的所有task，找出能使当前worker的utility增加最大的task，返回（该task的ID, 申请即将泄露的平均distance[此处可返回扰动], 申请泄露的budget总和[此处可返回当前budget]）
//        Integer taskID = null;
//        Double noiseEfficientDistance = null;
//        Double effectivePrivacyBudget = null;
////        Double efficientPrivacyBudget = null;
////        double candidateTaskEntropy = Double.MAX_VALUE;
//        double candidateTaskEntropy = -1;
//        Double newCostPrivacyBudget = null, newPrivacyBudget = null, newNoiseDistance = null, newUtilityValue = null;
//        for (Integer i : taskIDList) {
//            if (lastTermTaskWinnerPackedArray[i].getWorkerID().equals(workerID)) {
//                continue;
//            }
//            // PPCF 判断
//            if (this.workers[workerID].getToTaskDistance(i) >= lastTermTaskWinnerPackedArray[i].getNoiseEffectiveDistance()) {
//                continue;
//            }
////            Double tempNewCostPrivacyBudget = getNewCostPrivacyBudget(workerID, i);
////            Double tempNewPrivacyBudget =  this.workers[workerID].privacyBudgetArrayList[i][this.workers[workerID].budgetIndex[i]];
////            Double tempNewNoiseDistance = this.workers[workerID].toTaskDistance[i] + LaplaceUtils.getLaplaceNoise(1, tempNewPrivacyBudget);
////            this.workers[workerID].budgetIndex[i] ++;
//
//            Double tempNewCostPrivacyBudget = getNewCostPrivacyBudget(workerID, i);
//            Double tempNewPrivacyBudget =  this.workers[workerID].getPrivacyBudgetArray(i)[this.workers[workerID].getBudgetIndex(i)];
//            Double tempNewNoiseDistance = this.workers[workerID].getNoiseDistanceArray(i)[this.workers[workerID].getBudgetIndex(i)];
//            this.workers[workerID].increaseBudgetIndex(i);
//
//            DistanceBudgetPair newEffectiveDistanceBudgetPair = getNewEffectiveNoiseDistanceAndPrivacyBudget(workerID, i, tempNewNoiseDistance, tempNewPrivacyBudget);
//            double tempCompeteDistance = newEffectiveDistanceBudgetPair.distance;
//            double tempEffectivePrivacyBudget = newEffectiveDistanceBudgetPair.budget;
//
//            // Utility 函数值判断
//            Double tempNewUtilityValue = this.getUtilityValue(this.tasks[i].valuation, tempEffectivePrivacyBudget, this.workers[workerID].getToTaskDistance(i), tempNewCostPrivacyBudget);
//            if (tempNewUtilityValue <= this.workers[workerID].getSuccessfullyUtilityFunctionValue(i)) {
//                // todo: 有待修改
//                continue;
//            }
//
//            // PCF 判断
//            double pcfValue = LaplaceProbabilityDensityFunction.probabilityDensityFunction(tempCompeteDistance, lastTermTaskWinnerPackedArray[i].getNoiseEffectiveDistance(), tempEffectivePrivacyBudget, lastTermTaskWinnerPackedArray[i].getEffectivePrivacyBudget());
//            if (pcfValue <= 0.5) {
//                continue;
//            }
//
//            // 根据 taskEntropy 挑选
//            double taskEntropy = getTaskEntropy(i, totalCompetingTimesList[i], competingWorkerIDSetArray[i]);
//            if (taskEntropy > candidateTaskEntropy) {
//                candidateTaskEntropy = taskEntropy;
//                taskID = i;
//                noiseEfficientDistance = tempCompeteDistance;
//                effectivePrivacyBudget = tempEffectivePrivacyBudget;
//                newCostPrivacyBudget = tempNewCostPrivacyBudget;
//                newPrivacyBudget = tempNewPrivacyBudget;
//                newNoiseDistance = tempNewNoiseDistance;
//                newUtilityValue = tempNewUtilityValue;
//            }
//
//        }
//        if (taskID == null) {
//            return null;
//        }
//        return new TaskTargetInfo(taskID, noiseEfficientDistance, effectivePrivacyBudget, candidateTaskEntropy, newCostPrivacyBudget, newPrivacyBudget, newNoiseDistance, newUtilityValue);
//    }
//    protected TaskTargetInfo chooseByProposingValue(List<Integer> taskIDList, Integer workerID, WorkerIDDistanceBudgetPair[] lastTermTaskWinnerPackedArray) {
//        Integer taskID = null;
//        Double noiseEfficientDistance = null;
//        Double effectivePrivacyBudget = null;
//        double candidateProposingValue = Double.MIN_VALUE;
//        Double newCostPrivacyBudget = null, newPrivacyBudget = null, newNoiseDistance = null, newUtilityValue = null;
//        for (Integer i : taskIDList) {
//            if (lastTermTaskWinnerPackedArray[i].getWorkerID().equals(workerID)) {
//                continue;
//            }
//            // PPCF 判断
//            if (this.workers[workerID].getToTaskDistance(i) >= lastTermTaskWinnerPackedArray[i].getNoiseEffectiveDistance()) {
//                continue;
//            }
//
//            Double tempNewCostPrivacyBudget = getNewCostPrivacyBudget(workerID, i);
//            Double tempNewPrivacyBudget =  this.workers[workerID].getPrivacyBudgetArray(i)[this.workers[workerID].getBudgetIndex(i)];
//            Double tempNewNoiseDistance = this.workers[workerID].getNoiseDistanceArray(i)[this.workers[workerID].getBudgetIndex(i)];
//            this.workers[workerID].increaseBudgetIndex(i);
//
//            DistanceBudgetPair newEffectiveDistanceBudgetPair = getNewEffectiveNoiseDistanceAndPrivacyBudget(workerID, i, tempNewNoiseDistance, tempNewPrivacyBudget);
//            double tempCompeteDistance = newEffectiveDistanceBudgetPair.distance;
//            double tempEffectivePrivacyBudget = newEffectiveDistanceBudgetPair.budget;
//
//            // Utility 函数判断
//            Double tempNewUtilityValue = this.getUtilityValue(this.tasks[i].valuation, tempEffectivePrivacyBudget, this.workers[workerID].getToTaskDistance(i), tempNewCostPrivacyBudget);
//            if (tempNewUtilityValue <= this.workers[workerID].getSuccessfullyUtilityFunctionValue(i)) {
//                continue;
//            }
//
//            // PCF 判断
//            double pcfValue = LaplaceProbabilityDensityFunction.probabilityDensityFunction(tempCompeteDistance, lastTermTaskWinnerPackedArray[i].getNoiseEffectiveDistance(), tempEffectivePrivacyBudget, lastTermTaskWinnerPackedArray[i].getEffectivePrivacyBudget());
//            if (pcfValue <= 0.5) {
//                continue;
//            }
//
//            /**
//             * 获取Proposing Value
//             */
//            double proposingValue = getProposingValue(pcfValue, this.workers[workerID].getToTaskDistance(i));
//
//
//            if (proposingValue > candidateProposingValue) {
//                candidateProposingValue = proposingValue;
//                taskID = i;
//                noiseEfficientDistance = tempCompeteDistance;
//                effectivePrivacyBudget = tempEffectivePrivacyBudget;
//                newCostPrivacyBudget = tempNewCostPrivacyBudget;
//                newPrivacyBudget = tempNewPrivacyBudget;
//                newNoiseDistance = tempNewNoiseDistance;
//                newUtilityValue = tempNewUtilityValue;
//            }
//
//        }
//        if (taskID == null) {
//            return null;
//        }
//        return new TaskTargetInfo(taskID, noiseEfficientDistance, effectivePrivacyBudget, candidateProposingValue, newCostPrivacyBudget, newPrivacyBudget, newNoiseDistance, newUtilityValue);
//    }


    /**
     * 1种server的选择函数
     */
//    protected void chosenByServer(WorkerIDDistanceBudgetPair[] taskCurrentWinnerPackedArray, List<Integer>[] newCandidateWorkerIDList) {
//        double competeTemp;
//        // todo: 设置 taskTempWinnerID 和 taskTempWinnerInfo
////        Integer[] taskBeforeWinnerIDArray = new Integer[newCandidateWorkerIDList.length];
//        WorkerIDDistanceBudgetPair[] taskBeforeWinnerPackedArray = new WorkerIDDistanceBudgetPair[newCandidateWorkerIDList.length];
//        for (int i = 0; i < newCandidateWorkerIDList.length; i++) {
//            if (newCandidateWorkerIDList[i].size() == 0) {
//                continue;
//            }
//            taskBeforeWinnerPackedArray[i] = taskCurrentWinnerPackedArray[i];
//            for (Integer j : newCandidateWorkerIDList[i]) {
//                competeTemp = LaplaceProbabilityDensityFunction.probabilityDensityFunction(this.workers[j].getEffectiveNoiseDistance(i), taskCurrentWinnerPackedArray[i].getNoiseEffectiveDistance(), this.workers[j].getEffectivePrivacyBudget(i), taskCurrentWinnerPackedArray[i].getEffectivePrivacyBudget());
//                if (competeTemp > 0.5) {
//                    taskCurrentWinnerPackedArray[i].setWorkerID(j);
//                    taskCurrentWinnerPackedArray[i].setNoiseEffectiveDistance(this.workers[j].getEffectiveNoiseDistance(i));
//                    taskCurrentWinnerPackedArray[i].setEffectivePrivacyBudget(this.workers[j].getEffectivePrivacyBudget(i));
//                }
//            }
//            if (!taskCurrentWinnerPackedArray[i].getWorkerID().equals(taskBeforeWinnerPackedArray[i].getWorkerID())) {
//                if (!taskBeforeWinnerPackedArray[i].getWorkerID().equals(-1)) {
//                    // todo: 封装
////                    this.workers[taskBeforeWinnerPackedArray[i].getWorkerID()].currentUtilityFunctionValue[i] -= this.tasks[i].valuation;
//                    this.workers[taskBeforeWinnerPackedArray[i].getWorkerID()].setSuccessfullyUtilityFunctionValue(i, -this.tasks[i].valuation) ;
//                    // 将被竞争下去的worker加入竞争集合，以便下轮判断是否可以竞争
//                    newCandidateWorkerIDList[i].add(taskBeforeWinnerPackedArray[i].getWorkerID());
//                }
////                this.workers[taskCurrentWinnerPackedArray[i].getWorkerID()].currentUtilityFunctionValue[i] = this.workers[taskCurrentWinnerPackedArray[i].getWorkerID()].successfullyUtilityFunctionValue[i];
//                this.workers[taskCurrentWinnerPackedArray[i].getWorkerID()].setSuccessfullyUtilityFunctionValue(i, this.workers[taskCurrentWinnerPackedArray[i].getWorkerID()].getCurrentUtilityFunctionValue(i));
//            }
//        }
//    }



//    public WorkerIDDistanceBudgetPair[] complete() {
//
//        // 记录每个task的被竞争的总次数
//        Integer[] competingTimes = new Integer[this.tasks.length];
//        // 记录每个task被竞争过的worker id的集合
//        HashSet<Integer>[] completedWorkerIDSet = new HashSet[this.tasks.length];
//
//        // 记录worker竞争时临时效用函数值
//        double[] tempUtilityArray = new double[this.workers.length];
//
//
//        // 针对每个task，记录当前竞争成功的worker的ID和扰动距离以及隐私预算
//        WorkerIDDistanceBudgetPair[] taskCurrentWinnerPackedArray = new WorkerIDDistanceBudgetPair[this.tasks.length];
//
//        // todo: 封装到了initializeAllocations
//
//        // 针对每个task，本轮提出竞争的worker的ID（每轮需要清空）
//        List<Integer>[] newCandidateWorkerIDList, oldCandidateWorkerIDList;
//        newCandidateWorkerIDList = new ArrayList[this.tasks.length];
////        BasicArray.setListArrayToEmptyList(newCandidateWorkerIDList);
//        initializeAllocationByFirstTaskAndNullAllocation(newCandidateWorkerIDList, taskCurrentWinnerPackedArray, competingTimes, completedWorkerIDSet);
//
//
//        // 用于记录当前竞争的总的worker数量
//        int totalCompleteWorkerNumber = this.workers.length;
//
//        // 用来临时记录每个worker经过对budget使用情况考察后，能够去进行竞争的tasks
//        List<Integer> tempCandidateTaskList = new ArrayList<>();
//
//        while (totalCompleteWorkerNumber > 0) {
////        while (!candidateWorkerIDList.isEmpty()) {
//            totalCompleteWorkerNumber = 0;
//
//            oldCandidateWorkerIDList = newCandidateWorkerIDList;
//            newCandidateWorkerIDList = new ArrayList[oldCandidateWorkerIDList.length];
//            BasicArray.setListArrayToEmptyList(newCandidateWorkerIDList);
//
//            /*
//             * 遍历每个 task 对应的候选集合中的worker。
//             * 每个 worker 对所有的 tasks 进行竞争。但只能挑出其中 1 个 task 作为最终竞争对象。
//             * 每轮结束后统计剩余的总的将要竞争的 workers 的数量。
//             */
//            for (int i = 0; i < oldCandidateWorkerIDList.length; i++) {
//
//                for (Integer j : oldCandidateWorkerIDList[i]) {
//                    //进行是否竞争判断1：如果当前 worker 不需要竞争(是上轮的胜利者)，就不作为
//                    if (j.equals(taskCurrentWinnerPackedArray[i].getWorkerID())) {
//                        continue;
//                    }
//
//                    // 进行是否竞争判断2：计算出所有预算充足的 task ID 的集合。如果该集合为空，则不作为
//                    tempCandidateTaskList.clear();
//                    setCandidateTaskByBudget(tempCandidateTaskList, this.workers[j]);
//                    if (tempCandidateTaskList.isEmpty()) {
//                        continue;
//                    }
//
//
//                    // 进行是否竞争判断3： 考察 utility函数、PPCF函数、PCF函数、任务熵
//
//                    TaskTargetInfo winnerInfo;
//                    // todo: 暂时用一个函数测试task entropy 和 proposing value。 由于task entropy需要统计竞争次数，因此影响时间，需要和proposing value 分开对比
//                    winnerInfo = chooseByTaskEntropy(tempCandidateTaskList, j, competingTimes, taskCurrentWinnerPackedArray, completedWorkerIDSet);
////                    winnerInfo = chooseByProposingValue(tempCandidateTaskList, j, taskCurrentWinnerPackedArray);
//                    if (winnerInfo == null) {
//                        continue;
//                    }
//
//                    // 否则（竞争成功），发布当前扰动距离长度和隐私预算(这里只添加进候选列表供server进一步选择)，并将隐私自己的预算索引值加1
//                    newCandidateWorkerIDList[winnerInfo.getTaskID()].add(j);
//                    completedWorkerIDSet[winnerInfo.getTaskID()].add(j);
////                    this.workers[j].alreadyPublishedNoiseDistanceAndBudgetTreeSetArray[winnerInfo.getTaskID()].add(new DistanceBudgetPair(winnerInfo.getNewNoiseDistance(), winnerInfo.getNewPrivacyBudget()));
//                    this.workers[j].addElementToAlreadyPublishedNoiseDistanceAndBudgetTreeSet(winnerInfo.getTaskID(),new DistanceBudgetPair(winnerInfo.getNewNoiseDistance(), winnerInfo.getNewPrivacyBudget()));
////                    this.workers[j].effectiveNoiseDistance[winnerInfo.getTaskID()] = winnerInfo.getNoiseEffectiveDistance();
//                    this.workers[j].setEffectiveNoiseDistance(winnerInfo.getTaskID(), winnerInfo.getNoiseEffectiveDistance());
////                    this.workers[j].effectivePrivacyBudget[winnerInfo.getTaskID()] = winnerInfo.getEffectivePrivacyBudget();
//                    this.workers[j].setEffectivePrivacyBudget(winnerInfo.getTaskID(), winnerInfo.getEffectivePrivacyBudget());
////                    this.workers[j].currentUtilityFunctionValue[winnerInfo.getTaskID()] = winnerInfo.getNewUtilityValue();
//                    this.workers[j].setCurrentUtilityFunctionValue(winnerInfo.getTaskID(), winnerInfo.getNewUtilityValue());
////                    this.workers[j].budgetIndex[winnerInfo.getTaskID()] ++;
//
////                    this.workers[j].taskCompetingTimes[winnerInfo.getTaskID()] ++;
//                    this.workers[j].increaseTaskCompetingTimes(winnerInfo.getTaskID());
//                    totalCompleteWorkerNumber ++;
//
//                    competingTimes[winnerInfo.getTaskID()] ++;
//
//                }
//            }
//
//
//            chosenByServer(taskCurrentWinnerPackedArray, newCandidateWorkerIDList);
//        }
//        return taskCurrentWinnerPackedArray;
//    }

}
