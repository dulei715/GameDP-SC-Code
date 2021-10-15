package edu.ecnu.dll.scheme_compared.solution;


import edu.ecnu.dll.basic_struct.pack.DistanceIDPair;
import edu.ecnu.dll.scheme_compared.struct.task.PPPTask;
import edu.ecnu.dll.scheme_compared.struct.worker.PPPWorker;
import tools.basic.BasicCalculation;
import tools.differential_privacy.noise.LaplaceUtils;

import java.util.*;

public class MultiTaskSolution {
    public PPPTask[] tasks  = null;
    public PPPWorker[] workers = null;

    public int[][] preferenceTable = null;
    public LinkedList[] candidateTaskIDListArray = null;
    public LinkedList<Integer> conflictWorkerIDList = null;

    public int[] result = null;

    /**
     * 初始化task的偏好表，并在每个worker中记录到各个task的距离以及扰动距离
     * 需要先初始化task和worker的位置以及worker能付出的隐私预算
     */
    public void initialize() {
        this.preferenceTable = new int[this.tasks.length][this.workers.length];
        DistanceIDPair[] distanceIDPairs;
        for (int i = 0; i < this.tasks.length; i++) {
            distanceIDPairs = new DistanceIDPair[this.workers.length];
            for (int j = 0; j < this.workers.length; j++) {
                this.workers[j].toTaskDistance[i] = BasicCalculation.get2Norm(this.workers[j].location, this.tasks[i].location);
                this.workers[j].toTaskNoiseDistance[i] = this.workers[j].toTaskDistance[i] + LaplaceUtils.getLaplaceNoise(1, this.workers[j].privacyBudget);
                distanceIDPairs[j] = new DistanceIDPair(this.workers[j].toTaskNoiseDistance[i], j);
            }
            Arrays.sort(distanceIDPairs);
            for (int j = 0; j < this.workers.length; j++) {
                this.preferenceTable[i][j] = distanceIDPairs[j].id;
            }
        }
    }

    private Integer solveConflict(Integer workerID) {
        // 1. 获取该 worker 的冲突列表 this.candidateTaskIDListArray[workerID]
        LinkedList<Integer> conflictTaskIDList = this.candidateTaskIDListArray[workerID];
        double minValue = Double.MIN_VALUE;
        for (Integer taskID : conflictTaskIDList) {

        }

        // 2. 计算解决当前冲突的所有可能的调整后的距离值，并比较选出最小的
        
        // 3. 设置当前冲突该worker的所有task的候选worker值(后移指标变量)



    }


    public void assignment() {
        this.result = new int[this.tasks.length];
        this.candidateTaskIDListArray = new LinkedList[this.workers.length];
        this.conflictWorkerIDList = new LinkedList<>();
        Integer workerID, taskID;
        for (int i = 0; i < this.preferenceTable.length; i++) {
            workerID = this.preferenceTable[i][0];
            if (candidateTaskIDListArray[workerID] == null) {
                candidateTaskIDListArray[workerID] = new LinkedList();
            } else if (candidateTaskIDListArray[workerID].size() > 0) {
                conflictWorkerIDList.add(workerID);
            }
            this.candidateTaskIDListArray[workerID].add(i);
        }
        while (!conflictWorkerIDList.isEmpty()) {
            workerID = conflictWorkerIDList.removeFirst();
            solveConflict(workerID);
        }
        for (int i = 0; i < this.tasks.length; i++) {
            this.result[i] = this.tasks[i].chosenWorkerID;
        }
    }
}
























