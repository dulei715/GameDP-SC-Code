package tools.struct;


import edu.ecnu.dll.basic_struct.comparator.WorkerIDDistanceBudgetPairComparator;
import edu.ecnu.dll.basic_struct.comparator.WorkerIDDistancePairComparator;
import edu.ecnu.dll.basic_struct.pack.single_agent_info.sub_class.WorkerIDDistanceBudgetPair;
import edu.ecnu.dll.basic_struct.pack.single_agent_info.sub_class.WorkerIDDistancePair;
import tools.basic.BasicArray;
import tools.differential_privacy.compare.impl.LaplaceProbabilityDensityFunction;
import tools.struct.table.PrivacyPreferenceTable;
import tools.struct.table.SimplePreferenceTable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class BasicConflictElimination {

    public static final WorkerIDDistancePair DEFAULT_WORKER_ID_DISTANCE_PAIR = new WorkerIDDistancePair(-1, Double.MAX_VALUE);

    public Integer taskSize;
    public Integer workerSize;

    // task的偏好表，其中每个元素记录的是[workerID, 有效噪声距离，有效隐私预算]
//    public int[][] taskPreferenceListArray = null;
    public SimplePreferenceTable simplePreferenceTable = null;
    // 记录每个task的当前考察到对应的偏好表里的哪个位置了 //TODO: 封装
    public Integer[] taskPreferenceIndex = null;

    public LinkedList[] candidateTaskIDListArray = null;
    public LinkedList<Integer> conflictWorkerIDList = null;

    public WorkerIDDistancePairComparator workerIDDistancePairComparator;


    public BasicConflictElimination(Integer taskSize, Integer workerSize) {
        this.taskSize = taskSize;
        this.workerSize = workerSize;
        this.workerIDDistancePairComparator = new WorkerIDDistancePairComparator();
    }

    public BasicConflictElimination(Integer taskSize, Integer workerSize, WorkerIDDistancePairComparator workerIDDistancePairComparator) {
        this.taskSize = taskSize;
        this.workerSize = workerSize;
        this.workerIDDistancePairComparator = workerIDDistancePairComparator;
    }

    /**
     * 初始化task的偏好表，并在每个worker中记录到各个task的距离以及扰动距离
     * 需要先初始化task和worker的位置以及worker能付出的隐私预算
     */
    public void initialize(List<WorkerIDDistancePair>[] data) {
//        this.taskPreferenceListArray = new int[this.tasks.length][this.workers.length];
        if (data.length != this.taskSize) {
            throw new RuntimeException("The size of data is not equal to the task number!");
        }
        this.simplePreferenceTable = new SimplePreferenceTable(this.taskSize, this.workerIDDistancePairComparator);
        this.simplePreferenceTable.setPreferenceTable(data);
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
    protected WorkerIDDistancePair getNextWorkerInfo(Integer taskID) {
        int nextIndex = this.taskPreferenceIndex[taskID] + 1;
        if (nextIndex >= this.simplePreferenceTable.table[taskID].size()) {
            return null;
        }
        return this.simplePreferenceTable.table[taskID].get(nextIndex);
    }

    protected WorkerIDDistancePair getCurrentWorkerInfo(Integer taskID) {
        int currentIndex = this.taskPreferenceIndex[taskID];
        if (currentIndex >= this.simplePreferenceTable.table[taskID].size()) {
            return null;
        }
        return this.simplePreferenceTable.table[taskID].get(currentIndex);
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
        WorkerIDDistancePair chosenTaskNextWorkerInfo, tempTaskNextWorkerInfo, chosenTaskCurrentWorkerInfo, tempTaskCurrentWorkerInfo;
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


//        /**
//         * for test
//         */
//        if (workerID.equals(115)) {
//            System.out.println("xixi");
//        }

         /*
            假设没有后继worker的task优先权最高(没有后继相当于后继worker距离其无限远)
            将没有后继worker的task的当前workerIDInfo加入二次判断列表 SecondJudgeMap
            SecondJudgeMap是空的，那就和之前一样，如果非空，就在SecondJudgeMap中选择
        */

        List<Integer> nonSucceedWorkerTaskIDList = new ArrayList<>();
        List<Integer> haveSucceedWorkerTaskIDList = new ArrayList<>();

        while (conflictIterator.hasNext()) {
            chosenTaskID = conflictIterator.next();
            chosenTaskNextWorkerInfo = getNextWorkerInfo(chosenTaskID);
            if (chosenTaskNextWorkerInfo == null) {
                nonSucceedWorkerTaskIDList.add(chosenTaskID);
            } else if (nonSucceedWorkerTaskIDList.isEmpty()) {
                haveSucceedWorkerTaskIDList.add(chosenTaskID);
            }
        }

        if (!nonSucceedWorkerTaskIDList.isEmpty()) {
            Iterator<Integer> iterator = nonSucceedWorkerTaskIDList.iterator();
            //获取其中一个冲突的task的ID
//            chosenTaskID = conflictIterator.next();
//            chosenTaskNextWorkerInfo = getNextWorkerInfo(chosenTaskID);
            chosenTaskID = iterator.next();
            chosenTaskCurrentWorkerInfo = getCurrentWorkerInfo(chosenTaskID);

            while (iterator.hasNext()) {
                tempTaskID = iterator.next();
                tempTaskCurrentWorkerInfo = getCurrentWorkerInfo(tempTaskID);

                if (compareWithCurrentInfo(chosenTaskCurrentWorkerInfo, tempTaskCurrentWorkerInfo)) {
                    chosenTaskID = tempTaskID;
                }
            }
        } else {
            Iterator<Integer> iterator = haveSucceedWorkerTaskIDList.iterator();
            //获取其中一个冲突的task的ID
            chosenTaskID = iterator.next();
            chosenTaskCurrentWorkerInfo = getCurrentWorkerInfo(chosenTaskID);
            chosenTaskNextWorkerInfo = getNextWorkerInfo(chosenTaskID);

            while (iterator.hasNext()) {
                tempTaskID = iterator.next();
                tempTaskCurrentWorkerInfo = getCurrentWorkerInfo(tempTaskID);
                tempTaskNextWorkerInfo = getNextWorkerInfo(tempTaskID);

//                if (compareFourValuesWithSuccessor(workerID, chosenTaskID, tempTaskID, chosenTaskNextWorkerInfo, tempTaskNextWorkerInfo)) {
                if (compareFourValuesWithSuccessor(chosenTaskCurrentWorkerInfo, tempTaskCurrentWorkerInfo, chosenTaskNextWorkerInfo, tempTaskNextWorkerInfo)) {
                    chosenTaskID = tempTaskID;
                    chosenTaskNextWorkerInfo = tempTaskNextWorkerInfo;
                }
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
            if (this.taskPreferenceIndex[tempTaskID] >= this.simplePreferenceTable.table[tempTaskID].size()) {
                // task已经没有候选的偏好的worker了
                continue;
            }
            tempTaskNextWorkerID =  this.simplePreferenceTable.table[tempTaskID].get(this.taskPreferenceIndex[tempTaskID]).getWorkerID();
            workerTaskSize = addToCandidateTaskIDListArray(tempTaskNextWorkerID, tempTaskID);
            if (workerTaskSize == 2) {
                this.conflictWorkerIDList.add(tempTaskNextWorkerID);
            }
        }

    }




    /**
     *
     * @param taskIDAWorkerInfo
     * @param taskIDBWorkerInfo
     * @param taskIDANextWorkerInfo
     * @param taskIDBNextWorkerInfo
     * @return 如果 taskIDB 比 taskIDA 占优，返回true，否则返回false
     */
    protected boolean compareFourValuesWithSuccessor(WorkerIDDistancePair taskIDAWorkerInfo, WorkerIDDistancePair taskIDBWorkerInfo, WorkerIDDistancePair taskIDANextWorkerInfo, WorkerIDDistancePair taskIDBNextWorkerInfo) {
        double tempPCFValue;
        tempPCFValue = taskIDBWorkerInfo.getDistance() + taskIDANextWorkerInfo.getDistance() - taskIDAWorkerInfo.getDistance() - taskIDBNextWorkerInfo.getDistance();

        if (tempPCFValue < 0) {
            return true;
        }
        return false;
    }

    /**
     *
     * @param taskIDAWorkerInfo
     * @param taskIDBWorkerInfo
     * @return 如果 taskIDB 比 taskIDA 占优(距离小)，返回true，否则返回false
     */
    protected boolean compareWithCurrentInfo(WorkerIDDistancePair taskIDAWorkerInfo, WorkerIDDistancePair taskIDBWorkerInfo) {
        double tempPCFValue;
        tempPCFValue = taskIDBWorkerInfo.getDistance() - taskIDAWorkerInfo.getDistance();
        if (tempPCFValue < 0) {
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
        for (int i = 0; i < this.simplePreferenceTable.taskSize; i++) {
            workerID = this.simplePreferenceTable.table[i].get(0).getWorkerID();

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
            if (this.taskPreferenceIndex[i] >= this.simplePreferenceTable.table[i].size()) {
                result[i] = BasicConflictElimination.DEFAULT_WORKER_ID_DISTANCE_PAIR.getWorkerID();
            } else {
                result[i] = this.simplePreferenceTable.table[i].get(this.taskPreferenceIndex[i]).getWorkerID();
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


    public WorkerIDDistancePair[] assignment(List<WorkerIDDistancePair>[] newCandidateWorkerIDList) {
        WorkerIDDistancePair[] result = new WorkerIDDistancePair[this.taskSize];
        initialize(newCandidateWorkerIDList);
        Integer workerID;
        int workerTaskSize;
        for (int i = 0; i < this.simplePreferenceTable.taskSize; i++) {
            if (this.simplePreferenceTable.table[i].isEmpty()) {
                continue;
            }
            workerID = this.simplePreferenceTable.table[i].get(0).getWorkerID();
            if (workerID.equals(DEFAULT_WORKER_ID_DISTANCE_PAIR.getWorkerID())) {
                continue;
            }

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
            if (this.taskPreferenceIndex[i] >= this.simplePreferenceTable.table[i].size()) {
                result[i] = BasicConflictElimination.DEFAULT_WORKER_ID_DISTANCE_PAIR;
            } else {
                result[i] = this.simplePreferenceTable.table[i].get(this.taskPreferenceIndex[i]);
            }
        }
        return result;
    }
    public void assignment(List<WorkerIDDistancePair>[] newCandidateWorkerIDList, Integer[] winnerArray) {
//        winnerArray = new Integer[this.taskSize];
        BasicArray.setIntArrayTo(winnerArray, -1);
        initialize(newCandidateWorkerIDList);
        Integer workerID;
        int workerTaskSize;
        for (int i = 0; i < this.simplePreferenceTable.taskSize; i++) {
            workerID = this.simplePreferenceTable.table[i].get(0).getWorkerID();

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
            if (this.taskPreferenceIndex[i] >= this.simplePreferenceTable.table[i].size()) {
                winnerArray[i] = null;
            } else {
                winnerArray[i] = this.simplePreferenceTable.table[i].get(this.taskPreferenceIndex[i]).getWorkerID();
            }
        }
    }
}
























