package edu.ecnu.dll.struct.solution;

import edu.ecnu.dll.struct.pack.TaskIDDistanceBudgetPair;
import edu.ecnu.dll.struct.pack.TaskIDDistanceBudgetPairProposingValue;
import edu.ecnu.dll.struct.pack.TaskIDDistanceBudgetPairTaskEntropy;
import edu.ecnu.dll.struct.task.BasicTask;
import edu.ecnu.dll.struct.task.Task;
import edu.ecnu.dll.struct.worker.MultiTaskBasicWorker;
import edu.ecnu.dll.struct.worker.MultiTaskNonPrivacyWorker;
import tools.basic.BasicArray;
import tools.basic.BasicCalculation;
import tools.differential_privacy.compare.impl.LaplaceProbabilityDensityFunction;
import tools.differential_privacy.noise.LaplaceUtils;
import tools.io.MyPrint;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class MultiTaskSolutionNonPrivacy {

    public static final int DISTANCE_TAG = 0;
    public static final int BUDGET_TAG = 1;

    public Task[] tasks = null;
    public MultiTaskNonPrivacyWorker[] workers = null;

    public static final int budgetSize = 3;

    private double getUtilityValue(List<Integer> taskList, Double workerMaxRange, int taskIndex, int workerIndex) {
        return 0;
    }

    private double getIncrementUtility(double distance, double maximumRange, double addingPrivacyBudget) {
        return 1 + distance - addingPrivacyBudget;
    }

    public void initializeBasicInformation() {
        // todo: 初始化 task 位置，以及 workers 的位置
        this.tasks = new Task[]{new BasicTask(new double[]{0.0, 0.0}), new BasicTask(new double[]{1.0, 1.0})};
        // todo: 初始化 workers 针对 task 的 privacy budget
        this.workers = new MultiTaskNonPrivacyWorker[4];
        for (int i = 0; i < this.workers.length; i++) {
            this.workers[i] = new MultiTaskNonPrivacyWorker(this.tasks.length);
        }

        this.workers[0].location = new double[]{2.0, 2.0};
        this.workers[0].maxRange = 4.0;

        this.workers[1].location = new double[]{-1.5, -1.5};
        this.workers[1].maxRange = 3.0;

        this.workers[2].location = new double[]{0.5, 0.5};
        this.workers[2].maxRange = 2.0;

        this.workers[3].location = new double[]{2.5, 2.5};
        this.workers[3].maxRange = 3.5;


    }

    public void initializeAgents() {
        for (int j = 0; j < this.workers.length; j++) {
            for (int i = 0; i < tasks.length; i++) {
                this.workers[j].toTaskDistance[i] = BasicCalculation.get2Norm(this.tasks[i].location, this.workers[i].location);
                this.workers[j].currentUtilityFunctionValue = 0.0;
            }
        }
    }


    public void complete() {

        // 记录每个task的被竞争的总次数
        Integer[] competingTimes = new Integer[this.tasks.length];
        // 记录每个task被竞争过的worker id的集合
        HashSet<Integer>[] completedWorkerIDSet = new HashSet[this.tasks.length];

        // 记录worker竞争时临时效用函数值
        double[] tempUtilityArray = new double[this.workers.length];

        // 记录每个worker的申请到的task列表
        List<Integer>[] allocatedTaskIDListArray = new ArrayList[this.workers.length];


        // 针对每个task，记录当前竞争成功的worker的ID
        int[] taskCurrentWinnerIDArray = new int[this.tasks.length];
        BasicArray.setIntArrayTo(taskCurrentWinnerIDArray, -1);

        // 针对每个task，记录当前竞争成功的worker的距离
        double[] taskCurrentWinnerInfoArray = new double[this.tasks.length];

        // 针对每个task，初始化距离为最大距离值
        // 针对每个task，初始化对应距离的隐私预算为最大隐私预算
        // 针对每个task，初始化总的被竞争次数为0
        // 针对每个task，初始化访问过被访问worker集合为空集合
        for (int i = 0; i < this.tasks.length; i++) {
            taskCurrentWinnerInfoArray[i] = Double.MAX_VALUE;
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
//        List<Integer> tempCandidateTaskList = new ArrayList<>();

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
//                    tempCandidateTaskList.clear();
//                    setCandidateTaskByBudget(tempCandidateTaskList, this.workers[i]);
//                    if (tempCandidateTaskList.isEmpty()) {
//                        continue;
//                    }



                    //todo: 好好设计一下：
                    // 进行是否竞争判断3： 遍历所有的可选task, 选出最大使得自身Utility增加最大的task, 如果为空，则不作为。
                    TaskIDDistanceBudgetPair maxIncrementUtilityInfo;
                    maxIncrementUtilityInfo = chooseByTaskEntropy(this.tasks, this.workers[i], competingTimes, taskCurrentWinnerIDArray, taskCurrentWinnerInfoArray, completedWorkerIDSet);
                    if (maxIncrementUtilityInfo == null) {
                        continue;
                    }

                    // 否则（竞争成功），发布当前扰动距离长度和隐私预算(这里只添加进候选列表供server进一步选择)，并将隐私自己的预算索引值加1
                    newCandidateWorkerIDList[maxIncrementUtilityInfo.taskID].add(i);
                    completedWorkerIDSet[i].add(i);
                    this.workers[i].taskCompletingTimes[k] ++;
                    totalCompleteWorkerNumber ++;
                }
            }


            // 服务器遍历每个worker，找出竞争成功的worker，修改他的competeState为false，将其他竞争状态为true的workerBudgetIndex值加1
            // todo: 设置 taskTempWinnerID 和 taskTempWinnerInfo
            for (int i = 0; i < newCandidateWorkerIDList.length; i++) {
                competingTimes[i] += newCandidateWorkerIDList[i].size();
                for (Integer j : newCandidateWorkerIDList[i]) {
                    if (taskCurrentWinnerInfoArray[i] > this.workers[j].toTaskDistance[i]) {
                        taskCurrentWinnerIDArray[i] = j;
                        taskCurrentWinnerInfoArray[i] = this.workers[j].toTaskDistance[i];
                    }
                }
            }
        }
        MyPrint.showIntegerArray(taskCurrentWinnerIDArray);
        MyPrint.showDoubleArray(taskCurrentWinnerInfoArray);
    }



    /**
     *
     * @param taskArray 候选的要去竞争的所有task(已经排除budget不足的task)
     * @param worker 要竞争的worker
     * @param totalCompetingTimesList 记录当前总的轮竞争每个task的次数
     * @param lastTermTaskWinnerIDArray 记录上轮竞争中成功竞争每个task的workerID
     * @param lastTermTaskWinnerInfoArray 记录上轮竞争中成功竞争每个task的worker其他信息
     * @return 经过考虑PPCF，PCF，task熵之后选择的taskID以及对应要的worker要修改的发布平均噪声距离，总的隐私预算和task熵值
     */
    private TaskIDDistanceBudgetPairTaskEntropy chooseByTaskEntropy(Task[] taskArray, MultiTaskNonPrivacyWorker worker, Integer[] totalCompetingTimesList, int[] lastTermTaskWinnerIDArray, double[] lastTermTaskWinnerInfoArray, HashSet<Integer>[] competingWorkerIDSet) {
        // 遍历taskIDList中的所有task，找出能使当前worker的utility增加最大的task，返回（该task的ID, 申请即将泄露的平均distance[此处可返回扰动], 申请泄露的budget总和[此处可返回当前budget]）
        Integer taskID = null;
        Double noiseAverageDistance = null;
        Double totalPrivacyBudget = null;
        double candidateTaskEntropy = Double.MAX_VALUE;
        for (int i = 0; i < taskArray.length; ++i) {
            if (worker.toTaskDistance[i] >= lastTermTaskWinnerInfoArray[i]) {
                continue;
            }
            double taskEntropy = getTaskEntropy(i, totalCompetingTimesList[i], competingWorkerIDSet[i]);
            if (taskEntropy < candidateTaskEntropy) {
                candidateTaskEntropy = taskEntropy;
                taskID = i;
            }

        }
        if (taskID == null) {
            return null;
        }
        return new TaskIDDistanceBudgetPairTaskEntropy(taskID, noiseAverageDistance, totalPrivacyBudget, candidateTaskEntropy);
    }

    private double getTaskEntropy(Integer taskID, Integer totalCompetingTime, HashSet<Integer> competingWorkerIDSet) {
        if (totalCompetingTime <= 0) {
            throw new RuntimeException("The total competing time is not positive value!");
        }
        double taskEntropy = 0;
        double tempRatio = 0;
        for (Integer j : competingWorkerIDSet) {
            tempRatio = this.workers[j].taskCompletingTimes[taskID] / totalCompetingTime;
            taskEntropy -= tempRatio*Math.log(tempRatio);
        }
        return taskEntropy;
    }

    private TaskIDDistanceBudgetPairProposingValue chooseByProposingValue(List<Integer> taskIDList, MultiTaskBasicWorker worker, Integer[] totalCompetingTimesList, int[] lastTermTaskWinnerIDArray, double[][] lastTermTaskWinnerInfoArray, HashSet<Integer>[] competingWorkerIDSet) {
        Integer taskID = null;
        Double noiseAverageDistance = null;
        Double totalPrivacyBudget = null;
        double candidateProposingValue = Double.MIN_VALUE;
        for (Integer i : taskIDList) {
            if (worker.toTaskDistance[i] >= lastTermTaskWinnerInfoArray[i][DISTANCE_TAG]) {
                continue;
            }
            double newNoiseDistance = worker.toTaskDistance[i] + LaplaceUtils.getLaplaceNoise(1, worker.privacyBudgetArray[i][worker.budgetIndex[i]]);
            double competeDistance = BasicCalculation.getAverage(worker.alreadyPublishedEverageNoiseDistance[i], newNoiseDistance, worker.budgetIndex[i] + 1);
            double completeTotalBudget = worker.alreadyPublishedTotalPrivacyBudget[i] + worker.privacyBudgetArray[i][worker.budgetIndex[i]];
            double pcfValue = LaplaceProbabilityDensityFunction.probabilityDensityFunction(competeDistance, lastTermTaskWinnerInfoArray[i][DISTANCE_TAG], completeTotalBudget, lastTermTaskWinnerInfoArray[i][BUDGET_TAG]);
            if (pcfValue <= 0.5) {
                continue;
            }
            /**
             * 获取Proposing Value
             */
            double proposingValue = pcfValue / worker.toTaskDistance[i];


            if (proposingValue > candidateProposingValue) {
                candidateProposingValue = proposingValue;
                taskID = i;
                noiseAverageDistance = competeDistance;
                totalPrivacyBudget = completeTotalBudget;
            }

        }
        if (taskID == null) {
            return null;
        }
        return new TaskIDDistanceBudgetPairProposingValue(taskID, noiseAverageDistance, totalPrivacyBudget, candidateProposingValue);
    }

    public static void main(String[] args) {
        MultiTaskSolutionNonPrivacy mTNPW = new MultiTaskSolutionNonPrivacy();
        mTNPW.initializeBasicInformation();
        mTNPW.initializeAgents();
        mTNPW.complete();
    }


}
