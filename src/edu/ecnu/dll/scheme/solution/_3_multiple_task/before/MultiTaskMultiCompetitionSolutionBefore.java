package edu.ecnu.dll.scheme.solution._3_multiple_task.before;

import edu.ecnu.dll.basic_struct.pack.TaskIDDistanceBudgetPair;
import edu.ecnu.dll.basic_struct.pack.Winner;
import edu.ecnu.dll.scheme.solution._3_multiple_task.MultiTaskSingleCompetitionSolution;
import tools.basic.BasicArray;
import tools.differential_privacy.compare.impl.LaplaceProbabilityDensityFunction;
import tools.io.print.MyPrint;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Deprecated
public class MultiTaskMultiCompetitionSolutionBefore extends MultiTaskSingleCompetitionSolution {

    public static final int proposalSize = 5;

    public Winner complete() {

        // 记录每个task的被竞争的总次数
        Integer[] competingTimes = new Integer[this.tasks.length];
        // 记录每个task被竞争过的worker id的集合
        HashSet<Integer>[] completedWorkerIDSet = new HashSet[this.tasks.length];

        // 记录worker竞争时临时效用函数值
        double[] tempUtilityArray = new double[this.workers.length];

        // 记录每个worker的申请到的task列表
        List<Integer>[] allocatedTaskIDListArray = new ArrayList[this.workers.length];


        // 针对每个task，记录当前竞争成功的worker的ID
        Integer[] taskCurrentWinnerIDArray = new Integer[this.tasks.length];
        BasicArray.setIntArrayTo(taskCurrentWinnerIDArray, -1);

        // 针对每个task，记录当前竞争成功的worker的budget以及扰动距离
        Double[][] taskCurrentWinnerInfoArray = new Double[this.tasks.length][2];

        // 针对每个task，初始化距离为最大距离值
        // 针对每个task，初始化对应距离的隐私预算为最大隐私预算
        // 针对每个task，初始化总的被竞争次数为0
        // 针对每个task，初始化访问过被访问worker集合为空集合
        for (int i = 0; i < this.tasks.length; i++) {
            taskCurrentWinnerInfoArray[i][DISTANCE_TAG] = Double.MAX_VALUE;
            taskCurrentWinnerInfoArray[i][BUDGET_TAG] = Double.MAX_VALUE;
            competingTimes[i] = 0;
            completedWorkerIDSet[i] = new HashSet<>();
        }

        // 针对每个task，本轮提出竞争的worker的ID（每轮需要清空）
        List<Integer>[] newCandidateWorkerIDList, oldCandidateWorkerIDList;
        newCandidateWorkerIDList = new ArrayList[this.tasks.length];
        BasicArray.setListArrayToEmptyList(newCandidateWorkerIDList);


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
            for (int k = 0; k < oldCandidateWorkerIDList.length; k++) {

                for (Integer i : oldCandidateWorkerIDList[k]) {
                    //进行是否竞争判断1：如果当前 worker 不需要竞争(是上轮的胜利者)，就不作为
                    if (i.equals(taskCurrentWinnerIDArray[k])) {
                        continue;
                    }

                    // 进行是否竞争判断2：计算出所有预算充足的 task ID 的集合。如果该集合为空，则不作为
                    tempCandidateTaskList.clear();
                    setCandidateTaskByBudget(tempCandidateTaskList, this.workers[i]);
                    if (tempCandidateTaskList.isEmpty()) {
                        continue;
                    }



                    //todo: 好好设计一下：
                    // 进行是否竞争判断3： 遍历所有的可选task, 选出最大使得自身Utility增加最大的task, 如果为空，则不作为。
                    TaskIDDistanceBudgetPair maxIncrementUtilityInfo;
                    maxIncrementUtilityInfo = chooseByTaskEntropy(tempCandidateTaskList, i, competingTimes, taskCurrentWinnerIDArray, taskCurrentWinnerInfoArray, completedWorkerIDSet);
                    if (maxIncrementUtilityInfo == null) {
                        continue;
                    }

                    // 否则（竞争成功），发布当前扰动距离长度和隐私预算(这里只添加进候选列表供server进一步选择)，并将隐私自己的预算索引值加1
                    newCandidateWorkerIDList[maxIncrementUtilityInfo.taskID].add(i);
                    completedWorkerIDSet[i].add(i);
                    this.workers[i].effectiveNoiseDistance[maxIncrementUtilityInfo.taskID] = maxIncrementUtilityInfo.noiseEffectiveDistance;
                    this.workers[i].effectivePrivacyBudget[maxIncrementUtilityInfo.taskID] = maxIncrementUtilityInfo.effectivePrivacyBudget;
                    this.workers[i].budgetIndex[maxIncrementUtilityInfo.taskID] ++;
                    this.workers[i].taskCompletingTimes[k] ++;
                    totalCompleteWorkerNumber ++;
                }
            }


            // 服务器遍历每个worker，找出竞争成功的worker，修改他的competeState为false，将其他竞争状态为true的workerBudgetIndex值加1
            // todo: 设置 taskTempWinnerID 和 taskTempWinnerInfo
            // 继续一轮竞争，直到除了胜利者其他的竞争者的competeState都为false.
            for (int i = 0; i < newCandidateWorkerIDList.length; i++) {
                competingTimes[i] += newCandidateWorkerIDList[i].size();
                for (Integer j : newCandidateWorkerIDList[i]) {
                    competeTemp = LaplaceProbabilityDensityFunction.probabilityDensityFunction(this.workers[j].effectiveNoiseDistance[i], taskCurrentWinnerInfoArray[i][DISTANCE_TAG], this.workers[j].effectivePrivacyBudget[i], taskCurrentWinnerInfoArray[i][BUDGET_TAG]);
                    if (competeTemp > 0.5) {
                        taskCurrentWinnerIDArray[i] = j;
                        taskCurrentWinnerInfoArray[i][DISTANCE_TAG] = this.workers[j].effectiveNoiseDistance[i];
                        taskCurrentWinnerInfoArray[i][BUDGET_TAG] = this.workers[j].effectivePrivacyBudget[i];
                    }
                }
            }
        }
//        System.out.println("The winner worker's id is " + taskTempWinnerID);
//        System.out.println("The winner worker's noise distance is " + taskTempWinnerInfo[0]);
//        System.out.println("The winner worker's budget is " + taskTempWinnerInfo[1]);
        MyPrint.showIntegerArray(taskCurrentWinnerIDArray);
        MyPrint.showDoubleArray(taskCurrentWinnerInfoArray[DISTANCE_TAG]);
        MyPrint.showDoubleArray(taskCurrentWinnerInfoArray[BUDGET_TAG]);
        return null;    // todo
    }


}
