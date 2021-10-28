package edu.ecnu.dll.scheme.solution._3_multiple_task;

import edu.ecnu.dll.basic_struct.pack.DistanceBudgetPair;
import edu.ecnu.dll.basic_struct.pack.TargetInfo;
import edu.ecnu.dll.basic_struct.pack.Winner;
import tools.basic.BasicArray;
import tools.differential_privacy.compare.impl.LaplaceProbabilityDensityFunction;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class MultiTaskMultiCompetitionSolution extends MultiTaskSingleCompetitionSolution {


    public static final int proposalSize = 5;

    public Winner complete() {

        // 记录每个task的被竞争的总次数
        Integer[] competingTimes = new Integer[this.tasks.length];
        // 记录每个task被竞争过的worker id的集合
        HashSet<Integer>[] completedWorkerIDSet = new HashSet[this.tasks.length];

        // 记录worker竞争时临时效用函数值
        double[] tempUtilityArray = new double[this.workers.length];

        // 记录每个worker的申请到的task列表
        // List<Integer>[] allocatedTaskIDListArray = new ArrayList[this.workers.length];


        // 针对每个task，记录当前竞争成功的worker的ID
        Integer[] taskCurrentWinnerIDArray = new Integer[this.tasks.length];
        BasicArray.setIntArrayTo(taskCurrentWinnerIDArray, -1);
        // 针对每个task，记录当前竞争成功的worker的budget以及扰动距离
        Double[][] taskCurrentWinnerInfoArray = new Double[this.tasks.length][2];

        // todo: 封装到了initializeAllocations

        // 针对每个task，本轮提出竞争的worker的ID（每轮需要清空）
        List<Integer>[] newCandidateWorkerIDList, oldCandidateWorkerIDList;
        newCandidateWorkerIDList = new ArrayList[this.tasks.length];
//        BasicArray.setListArrayToEmptyList(newCandidateWorkerIDList);
        initializeAllocationByFirstTaskAndNullAllocation(newCandidateWorkerIDList, taskCurrentWinnerInfoArray, competingTimes, completedWorkerIDSet);


        double competeTemp;
        // 用于记录当前竞争的总的worker数量
        int totalCompleteWorkerNumber = this.workers.length;

        // 用来临时记录每个worker经过对budget使用情况考察后，能够去进行竞争的tasks
        List<Integer> tempCandidateTaskList = new ArrayList<>();

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
                    if (j.equals(taskCurrentWinnerIDArray[i])) {
                        continue;
                    }

                    // 进行是否竞争判断2：计算出所有预算充足的 task ID 的集合。如果该集合为空，则不作为
                    tempCandidateTaskList.clear();
                    setCandidateTaskByBudget(tempCandidateTaskList, this.workers[j]);
                    if (tempCandidateTaskList.isEmpty()) {
                        continue;
                    }


                    // 进行是否竞争判断3： 考察 utility函数、PPCF函数、PCF函数、任务熵

                    TargetInfo winnerInfo;
                    // todo: 暂时用一个函数测试task entropy 和 proposing value。 由于task entropy需要统计竞争次数，因此影响时间，需要和proposing value 分开对比
//                    winnerInfo = chooseByTaskEntropy(tempCandidateTaskList, j, competingTimes, taskCurrentWinnerIDArray, taskCurrentWinnerInfoArray, completedWorkerIDSet);
                    winnerInfo = chooseByProposingValue(tempCandidateTaskList, j, taskCurrentWinnerIDArray, taskCurrentWinnerInfoArray);
//                    winnerInfo = chooseByTaskEntropy();
                    if (winnerInfo == null) {
                        continue;
                    }

                    // 否则（竞争成功），发布当前扰动距离长度和隐私预算(这里只添加进候选列表供server进一步选择)，并将隐私自己的预算索引值加1
                    newCandidateWorkerIDList[winnerInfo.taskID].add(j);
                    completedWorkerIDSet[winnerInfo.taskID].add(j);
                    this.workers[j].alreadyPublishedNoiseDistanceAndBudgetTreeSetArray[winnerInfo.taskID].add(new DistanceBudgetPair(winnerInfo.newNoiseDistance, winnerInfo.newPrivacyBudget));
                    this.workers[j].effectiveNoiseDistance[winnerInfo.taskID] = winnerInfo.noiseEffectiveDistance;
                    this.workers[j].effectivePrivacyBudget[winnerInfo.taskID] = winnerInfo.effectivePrivacyBudget;
                    this.workers[j].completeUtilityFunctionValue[winnerInfo.taskID] = winnerInfo.newUtilityValue;
                    this.workers[j].budgetIndex[winnerInfo.taskID] ++;

                    this.workers[j].taskCompletingTimes[i] ++;
                    totalCompleteWorkerNumber ++;
                }
            }


            chosenByServer(competingTimes, taskCurrentWinnerIDArray, taskCurrentWinnerInfoArray, newCandidateWorkerIDList);
        }
        return new Winner(taskCurrentWinnerIDArray, taskCurrentWinnerInfoArray);
    }

    @Override
    public void chosenByServer(Integer[] competingTimes, Integer[] taskCurrentWinnerIDArray, Double[][] taskCurrentWinnerInfoArray, List<Integer>[] newCandidateWorkerIDList) {
        double competeTemp;
        // 服务器遍历每个worker，找出竞争成功的worker，修改他的competeState为false
        // todo: 设置 taskTempWinnerID 和 taskTempWinnerInfo
        Integer[] taskBeforeWinnerIDArray = new Integer[newCandidateWorkerIDList.length];
        for (int i = 0; i < newCandidateWorkerIDList.length; i++) {
            if (newCandidateWorkerIDList[i].size() == 0) {
                continue;
            }
            competingTimes[i] += newCandidateWorkerIDList[i].size();
            taskBeforeWinnerIDArray[i] = taskCurrentWinnerIDArray[i];
            for (Integer j : newCandidateWorkerIDList[i]) {
                competeTemp = LaplaceProbabilityDensityFunction.probabilityDensityFunction(this.workers[j].effectiveNoiseDistance[i], taskCurrentWinnerInfoArray[i][DISTANCE_TAG], this.workers[j].effectivePrivacyBudget[i], taskCurrentWinnerInfoArray[i][BUDGET_TAG]);
                if (competeTemp > 0.5) {
                    taskCurrentWinnerIDArray[i] = j;
                    taskCurrentWinnerInfoArray[i][DISTANCE_TAG] = this.workers[j].effectiveNoiseDistance[i];
                    taskCurrentWinnerInfoArray[i][BUDGET_TAG] = this.workers[j].effectivePrivacyBudget[i];
                }
            }
            if (!taskCurrentWinnerIDArray[i].equals(taskBeforeWinnerIDArray[i])) {
                if (!taskBeforeWinnerIDArray[i].equals(-1)) {
                    // todo: 封装
                    this.workers[taskBeforeWinnerIDArray[i]].currentUtilityFunctionValue[i] -= this.tasks[i].valuation;
                    // 将被竞争下去的worker加入竞争集合，以便下轮判断是否可以竞争
                    newCandidateWorkerIDList[i].add(taskBeforeWinnerIDArray[i]);
                }
                this.workers[taskCurrentWinnerIDArray[i]].currentUtilityFunctionValue[i] = this.workers[taskCurrentWinnerIDArray[i]].completeUtilityFunctionValue[i];
            }
        }
    }


}
