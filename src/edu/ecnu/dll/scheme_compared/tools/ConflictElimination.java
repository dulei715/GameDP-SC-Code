package edu.ecnu.dll.scheme_compared.tools;

import edu.ecnu.dll.basic_struct.agent.*;
import edu.ecnu.dll.basic_struct.pack.single_agent_info.sub_class.DistanceBudgetPair;
import tools.differential_privacy.compare.impl.LaplaceProbabilityDensityFunction;

import java.util.Iterator;
import java.util.LinkedList;

public class ConflictElimination {

    public PrivacyTask[] agentTaskArray = null;
    public PrivacyWorker[] workers = null;

    // 记录每个task对worker的偏好排名
    public int[][] taskPreferenceTable = null;
    // 记录每个task的当前考察到对应的偏好表里的哪个位置了
    public int[] taskPreferenceIndex = null;

    // 记录存在冲突的worker的ID
    public LinkedList<Integer> conflictWorkerIDList = null;

    // 记录每个worker冲突下的task集合
    public LinkedList[] candidateTaskIDListArray = null;






    public void solveConflict(Integer workerID) {
        DistanceBudgetPair chosenDistanceBudgetPair, tempDistanceBudgetPair;
        Integer chosenTaskID, tempTaskID;
        Integer chosenTaskNextWorkerID, tempTaskNextWorkerID;
        int chosenTaskNextWorkerIndex, tempTaskNextWorkerIndex;
        int workerTaskSize;
        double tempPCFValue;

        /**
         * 1. 从冲突列表中移除该 worker ID
         */
        this.conflictWorkerIDList.remove(workerID);

        /**
         * 2. 获取该 worker 的候选task列表(记录冲突该worker的所有task) this.candidateTaskIDListArray[workerID]
         */
        LinkedList<Integer> conflictTaskIDList = this.candidateTaskIDListArray[workerID];

        /** 3. 计算解决当前冲突的所有可能的调整后的距离值，并比较选出最小的
         */
        Iterator<Integer> conflictIterator = conflictTaskIDList.iterator();
        //获取其中一个冲突的task的ID
        chosenTaskID = conflictIterator.next();
        chosenDistanceBudgetPair = getNextNoiseDistance(chosenTaskID);
        while (conflictIterator.hasNext()) {
            tempTaskID = conflictIterator.next();
            tempDistanceBudgetPair = getNextNoiseDistance(tempTaskID);
            tempPCFValue = LaplaceProbabilityDensityFunction.probabilityDensityFunction(chosenDistanceBudgetPair.distance, tempDistanceBudgetPair.distance, chosenDistanceBudgetPair.budget, tempDistanceBudgetPair.budget);
            if (tempPCFValue > 0.5) {
                // tempPCFValue 大于 0.5，说明chosen的候选距离小于temp的候选距离的概率更大，说明temp的更应该充当被选择的角色，因此需要替换chosen为temp
                chosenDistanceBudgetPair = tempDistanceBudgetPair;
            }
        }

        /** 4. 设置当前冲突该worker的所有task的候选worker值(后移指标变量)，并更新下一个worker的候选列表
         */
        conflictIterator = conflictTaskIDList.iterator();
        while (conflictIterator.hasNext()) {
            tempTaskID = conflictIterator.next();
            if (tempTaskID.equals(chosenTaskID)) {
                continue;
            }
            this.taskPreferenceIndex[tempTaskID] ++;    // todo: 解决出界问题
            // 调整冲突集合
            conflictIterator.remove();
            tempTaskNextWorkerID =  this.taskPreferenceTable[tempTaskID][this.taskPreferenceIndex[tempTaskID]];
            workerTaskSize = addToCandidateTaskIDListArray(tempTaskNextWorkerID, tempTaskID);
            if (workerTaskSize == 2) {
                this.conflictWorkerIDList.add(tempTaskNextWorkerID);
            }
        }

    }

    /**
     * 返回当前task的下一个候选worker的扰动距离和该worker提供的budget
     * @param taskID
     * @return
     */
    private DistanceBudgetPair getNextNoiseDistance(Integer taskID) {
        int nextIndex = this.taskPreferenceIndex[taskID] + 1;
        Integer nextWorkerID;
        Double noiseDistance, budget;
        if (nextIndex >= this.workers.length) {
            // todo 处理 worker 不足的情况
            throw new RuntimeException("There is no candidate worker for " + taskID + " task!");
        }
        // 获取该task到下一个候选worker的距离(先获取该task下一个候选worker的ID)
        nextWorkerID = this.taskPreferenceTable[taskID][nextIndex];
//        noiseDistance = this.workers[nextWorkerID].toTaskNoiseDistance[taskID];
        noiseDistance = this.workers[nextWorkerID].getToTaskNoiseDistance(taskID);
        budget = this.workers[nextWorkerID].getPrivacyBudget(taskID);
        return new DistanceBudgetPair(noiseDistance, budget);
    }

    /**
     * 添加task到worker的候选集合，并返回当前worker候选集合的task个数
     * @param workerID
     * @param taskID
     */
    private int addToCandidateTaskIDListArray(Integer workerID, Integer taskID) {
        if (this.candidateTaskIDListArray[workerID] == null) {
            this.candidateTaskIDListArray[workerID] = new LinkedList();
        }
        this.candidateTaskIDListArray[workerID].add(taskID);
        return this.candidateTaskIDListArray[workerID].size();
    }

}
