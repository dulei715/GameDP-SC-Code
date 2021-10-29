package edu.ecnu.dll.scheme_compared.solution;


import com.sun.deploy.util.ArrayUtil;
import edu.ecnu.dll.basic_struct.comparator.WorkerIDDistanceBudgetPairComparator;
import edu.ecnu.dll.basic_struct.pack.DistanceBudgetPair;
import edu.ecnu.dll.basic_struct.pack.WorkerIDDistanceBudgetPair;
import edu.ecnu.dll.scheme_compared.struct.task.PPPTask;
import edu.ecnu.dll.scheme_compared.struct.worker.PPPWorker;
import tools.basic.BasicArray;
import tools.differential_privacy.compare.impl.LaplaceProbabilityDensityFunction;
import tools.struct.PreferenceTable;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ConflictElimination {

    public Integer taskSize;
    public Integer workerSize;

    // task的偏好表，其中每个元素记录的是[workerID, 有效噪声距离，有效隐私预算]
//    public int[][] taskPreferenceListArray = null;
    public PreferenceTable preferenceTable = null;
    // 记录每个task的当前考察到对应的偏好表里的哪个位置了 //TODO: 封装
    public Integer[] taskPreferenceIndex = null;

    public LinkedList[] candidateTaskIDListArray = null;
    public LinkedList<Integer> conflictWorkerIDList = null;

    public WorkerIDDistanceBudgetPairComparator workerIDDistanceBudgetPairComparator;


    public ConflictElimination(Integer taskSize, Integer workerSize) {
        this.taskSize = taskSize;
        this.workerSize = workerSize;
        this.workerIDDistanceBudgetPairComparator = new WorkerIDDistanceBudgetPairComparator();
    }

    public ConflictElimination(Integer taskSize, Integer workerSize, WorkerIDDistanceBudgetPairComparator workerIDDistanceBudgetPairComparator) {
        this.taskSize = taskSize;
        this.workerSize = workerSize;
        this.workerIDDistanceBudgetPairComparator = workerIDDistanceBudgetPairComparator;
    }

    /**
     * 初始化task的偏好表，并在每个worker中记录到各个task的距离以及扰动距离
     * 需要先初始化task和worker的位置以及worker能付出的隐私预算
     */
    public void initialize(List<WorkerIDDistanceBudgetPair>[] data) {
//        this.taskPreferenceListArray = new int[this.tasks.length][this.workers.length];
        if (data.length != this.taskSize) {
            throw new RuntimeException("The size of data is not equal to the task number!");
        }
        this.preferenceTable = new PreferenceTable(this.taskSize, this.workerIDDistanceBudgetPairComparator);
        this.preferenceTable.setPreferenceTable(data);
        this.taskPreferenceIndex = new Integer[this.taskSize];
        BasicArray.setIntArrayToZero(this.taskPreferenceIndex);
        this.conflictWorkerIDList = new LinkedList();
        this.candidateTaskIDListArray = new LinkedList[this.workerSize];
        for (int i = 0; i < this.workerSize; i++) {
            this.candidateTaskIDListArray[i] = new LinkedList();
        }
    }

    /**
     * 返回当前task的下一个候选worker的id,扰动距离和该worker提供的budget
     * @param taskID
     * @return
     */
    protected WorkerIDDistanceBudgetPair getNextWorkerInfo(Integer taskID) {
        int nextIndex = this.taskPreferenceIndex[taskID] + 1;
        if (nextIndex >= this.preferenceTable.table[taskID].size()) {
            return null;
        }
        return this.preferenceTable.table[taskID].get(nextIndex);
    }

    /**
     * 添加task到worker的候选集合，并返回当前worker候选集合的task个数
     * @param workerID
     * @param taskID
     */
    protected int addToCandidateTaskIDListArray(Integer workerID, Integer taskID) {
        if (this.candidateTaskIDListArray[workerID] == null) {
            this.candidateTaskIDListArray[workerID] = new LinkedList();
        }
        this.candidateTaskIDListArray[workerID].add(taskID);
        return this.candidateTaskIDListArray[workerID].size();
    }

    protected void solveConflict(Integer workerID) {
        WorkerIDDistanceBudgetPair chosenTaskNextWorkerInfo, tempTaskNextWorkerInfo;
        Integer chosenTaskID, tempTaskID, tempTaskNextWorkerID;
        int workerTaskSize;

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
        chosenTaskNextWorkerInfo = getNextWorkerInfo(chosenTaskID);
        while (conflictIterator.hasNext()) {
            tempTaskID = conflictIterator.next();
            tempTaskNextWorkerInfo = getNextWorkerInfo(tempTaskID);

//            nextWorkerInfo = compareFourValues(nextWorkerInfo, tempTaskID);
            if (compareFourValues(workerID, chosenTaskID, tempTaskID, chosenTaskNextWorkerInfo, tempTaskNextWorkerInfo)) {
                chosenTaskID = tempTaskID;
                chosenTaskNextWorkerInfo = tempTaskNextWorkerInfo;
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
            if (this.taskPreferenceIndex[tempTaskID] >= this.preferenceTable.table[tempTaskID].size()) {
                // task已经没有候选的偏好的worker了
                continue;
            }
            tempTaskNextWorkerID =  this.preferenceTable.table[tempTaskID].get(this.taskPreferenceIndex[tempTaskID]).workerID;
            workerTaskSize = addToCandidateTaskIDListArray(tempTaskNextWorkerID, tempTaskID);
            if (workerTaskSize == 2) {
                this.conflictWorkerIDList.add(tempTaskNextWorkerID);
            }
        }

    }

    /**
     *
     * @param workerID
     * @param taskIDA
     * @param taskIDB
     * @param taskIDANextWorkerInfo
     * @param taskIDBNextWorkerInfo
     * @return 如果 taskIDB 比 taskIDA 占优，返回true，否则返回false
     */
    protected boolean compareFourValues(Integer workerID, Integer taskIDA, Integer taskIDB, WorkerIDDistanceBudgetPair taskIDANextWorkerInfo, WorkerIDDistanceBudgetPair taskIDBNextWorkerInfo) {
        double tempPCFValue;
        tempPCFValue = LaplaceProbabilityDensityFunction.probabilityDensityFunction(taskIDANextWorkerInfo.noiseEffectiveDistance, taskIDBNextWorkerInfo.noiseEffectiveDistance, taskIDANextWorkerInfo.effectivePrivacyBudget, taskIDBNextWorkerInfo.effectivePrivacyBudget);
        if (tempPCFValue > 0.5) {
            // tempPCFValue 大于 0.5，说明taskIDA的候选距离小于taskIDB的候选距离的概率更大，说明taskIDB的更应该充当被选择的角色，因此taskIDB占优势
            return true;
        }
        return false;
    }


    public Integer[] assignment() {
        Integer[] result = new Integer[this.taskSize];
//        this.candidateTaskIDListArray = new LinkedList[this.workerSize];
//        this.conflictWorkerIDList = new LinkedList<>();
        Integer workerID;
        int workerTaskSize;
        for (int i = 0; i < this.preferenceTable.taskSize; i++) {
            workerID = this.preferenceTable.table[i].get(0).workerID;

            workerTaskSize = addToCandidateTaskIDListArray(workerID, i);

            if (workerTaskSize == 2){
                // 2代表有冲突，且第一次发现有冲突
                this.conflictWorkerIDList.add(workerID);
            }
        }
        while (!conflictWorkerIDList.isEmpty()) {
            workerID = conflictWorkerIDList.removeFirst();
            solveConflict(workerID);
        }
        for (int i = 0; i < this.taskSize; i++) {
            if (this.taskPreferenceIndex[i] >= this.preferenceTable.table[i].size()) {
                result[i] = null;
            } else {
                result[i] = this.preferenceTable.table[i].get(this.taskPreferenceIndex[i]).workerID;
            }
        }
        return result;
    }

    public void reset() {
        if (this.taskPreferenceIndex == null) {
            this.taskPreferenceIndex = new Integer[this.taskSize];
        }
        if (this.conflictWorkerIDList == null) {
            this.conflictWorkerIDList = new LinkedList();
        } else {
            this.conflictWorkerIDList.clear();
        }
        if (this.candidateTaskIDListArray == null) {
            this.candidateTaskIDListArray = new LinkedList[this.workerSize];
        }
        for (int i = 0; i < this.workerSize; i++) {
            this.taskPreferenceIndex[i] = 0;
            if (this.taskPreferenceIndex[i] == null) {
                this.candidateTaskIDListArray[i] = new LinkedList();
            } else {
                this.candidateTaskIDListArray[i].clear();
            }
        }
    }


    public WorkerIDDistanceBudgetPair[] assignment(List<WorkerIDDistanceBudgetPair>[] newCandidateWorkerIDList) {
        WorkerIDDistanceBudgetPair[] result = new WorkerIDDistanceBudgetPair[this.taskSize];
        initialize(newCandidateWorkerIDList);
        Integer workerID;
        int workerTaskSize;
        for (int i = 0; i < this.preferenceTable.taskSize; i++) {
            if (this.preferenceTable.table[i].isEmpty()) {
                continue;
            }
            workerID = this.preferenceTable.table[i].get(0).workerID;

            workerTaskSize = addToCandidateTaskIDListArray(workerID, i);

            if (workerTaskSize == 2){
                // 2代表有冲突，且第一次发现有冲突
                this.conflictWorkerIDList.add(workerID);
            }
        }
        while (!conflictWorkerIDList.isEmpty()) {
            workerID = conflictWorkerIDList.removeFirst();
            solveConflict(workerID);
        }
        for (int i = 0; i < this.taskSize; i++) {
            if (this.taskPreferenceIndex[i] >= this.preferenceTable.table[i].size()) {
                result[i] = null;
            } else {
                result[i] = this.preferenceTable.table[i].get(this.taskPreferenceIndex[i]);
            }
        }
        return result;
    }
    public void assignment(List<WorkerIDDistanceBudgetPair>[] newCandidateWorkerIDList, Integer[] winnerArray) {
//        winnerArray = new Integer[this.taskSize];
        BasicArray.setIntArrayTo(winnerArray, -1);
        initialize(newCandidateWorkerIDList);
        Integer workerID;
        int workerTaskSize;
        for (int i = 0; i < this.preferenceTable.taskSize; i++) {
            workerID = this.preferenceTable.table[i].get(0).workerID;

            workerTaskSize = addToCandidateTaskIDListArray(workerID, i);

            if (workerTaskSize == 2){
                // 2代表有冲突，且第一次发现有冲突
                this.conflictWorkerIDList.add(workerID);
            }
        }
        while (!conflictWorkerIDList.isEmpty()) {
            workerID = conflictWorkerIDList.removeFirst();
            solveConflict(workerID);
        }
        for (int i = 0; i < this.taskSize; i++) {
            if (this.taskPreferenceIndex[i] >= this.preferenceTable.table[i].size()) {
                winnerArray[i] = null;
            } else {
                winnerArray[i] = this.preferenceTable.table[i].get(this.taskPreferenceIndex[i]).workerID;
            }
        }
    }
}
























