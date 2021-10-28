package edu.ecnu.dll.basic_struct.comparator;

import edu.ecnu.dll.basic_struct.pack.TaskWorkerIDPair;

import java.util.Comparator;

public class TaskWorkerIDComparator implements Comparator<TaskWorkerIDPair> {

    public static final int TASK_FIRST = 0;
    public static final int WORKER_FIRST = 1;

    public static final int TASK_ASCENDING = 0;
    public static final int TASK_DESCENDING = 1;

    public static final int WORKER_ASCENDING = 3;
    public static final int WORKER_DESCENDING = 4;

    int firstCompared;
    int taskOrderType;
    int workerOrderType;

    public TaskWorkerIDComparator(int firstCompared, int taskOrderType, int workerOrderType) {
        this.firstCompared = firstCompared;
        this.taskOrderType = taskOrderType;
        this.workerOrderType = workerOrderType;
    }

    private int compareTask(TaskWorkerIDPair elemA, TaskWorkerIDPair elemB) {
        if (this.taskOrderType == TASK_ASCENDING) {
            if (elemA.taskID < elemB.taskID) {
                return -1;
            } else if (elemA.taskID > elemB.taskID) {
                return 1;
            }
            return 0;
        } else if (this.taskOrderType == TASK_DESCENDING) {
            if (elemA.taskID < elemB.taskID) {
                return 1;
            } else if (elemA.taskID > elemB.taskID) {
                return -1;
            }
            return 0;
        }
        throw new RuntimeException("The task order type is wrong!");
    }

    private int compareWorker(TaskWorkerIDPair elemA, TaskWorkerIDPair elemB) {
        if (this.workerOrderType == WORKER_ASCENDING) {
            if (elemA.workerID < elemB.workerID) {
                return -1;
            } else if (elemA.workerID > elemB.workerID) {
                return 1;
            }
            return 0;
        } else if (this.workerOrderType == WORKER_DESCENDING) {
            if (elemA.workerID < elemB.workerID) {
                return 1;
            } else if (elemA.workerID > elemB.workerID) {
                return -1;
            }
            return 0;
        }
        throw new RuntimeException("The worker order type is wrong!");
    }

    @Override
    public int compare(TaskWorkerIDPair elemA, TaskWorkerIDPair elemB) {
        if (this.firstCompared == TASK_FIRST) {
            int taskResult = compareTask(elemA, elemB);
            if (taskResult != 0) {
                return taskResult;
            }
            return compareWorker(elemA, elemB);
        } else if (this.firstCompared == WORKER_FIRST) {
            int workerResult = compareWorker(elemA, elemB);
            if (workerResult != 0) {
                return workerResult;
            }
            return compareTask(elemA, elemB);
        } else {
            throw new RuntimeException("The first parameter is wrong!");
        }
    }
}
