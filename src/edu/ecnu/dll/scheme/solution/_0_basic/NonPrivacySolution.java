package edu.ecnu.dll.scheme.solution._0_basic;

import edu.ecnu.dll.basic_struct.agent.Task;
import edu.ecnu.dll.scheme.struct.task.BasicTask;
import edu.ecnu.dll.scheme.struct.task.NonPrivacyTask;
import edu.ecnu.dll.scheme.struct.worker.MultiTaskBasicWorker;
import edu.ecnu.dll.scheme.struct.worker.MultiTaskNonPrivacyWorker;
import tools.basic.BasicArray;
import tools.struct.Point;

import java.util.List;

public abstract class NonPrivacySolution extends Solution { // Task value 保证是[0,1]之间的值

    public NonPrivacyTask[] tasks = null;
    public MultiTaskNonPrivacyWorker[] workers = null;

    public void initializeBasicInformation(List<Point> taskPositionList, Double[] taskValueArray, List<Point> workerPositionList, List<Double> workerRangeList) {
        Point taskPosition, workerPosition;
        // 同时初始化父类
        super.tasks = this.tasks = new NonPrivacyTask[taskPositionList.size()];
        for (int i = 0; i < taskPositionList.size(); i++) {
            taskPosition = taskPositionList.get(i);
            this.tasks[i] = new NonPrivacyTask(taskPosition.getIndex());
            this.tasks[i].valuation = taskValueArray[i];
        }
        // 同时初始化父类
        super.workers = this.workers = new MultiTaskNonPrivacyWorker[workerPositionList.size()];
        for (int j = 0; j < workers.length; j++) {
            workerPosition = workerPositionList.get(j);
            this.workers[j] = new MultiTaskNonPrivacyWorker(workerPosition.getIndex());
            this.workers[j].setMaxRange(workerRangeList.get(j));
        }

    }

    public void initializeBasicInformation(List<Point> taskPositionList, Double taskValue, List<Point> workerPositionList, Double workerRange) {
        Point taskPosition, workerPosition;
        // 同时初始化父类
        super.tasks = this.tasks = new NonPrivacyTask[taskPositionList.size()];
        for (int i = 0; i < taskPositionList.size(); i++) {
            taskPosition = taskPositionList.get(i);
            this.tasks[i] = new NonPrivacyTask(taskPosition.getIndex());
            this.tasks[i].valuation = taskValue;
        }
        // 同时初始化父类
        super.workers = this.workers = new MultiTaskNonPrivacyWorker[workerPositionList.size()];
        for (int j = 0; j < workers.length; j++) {
            workerPosition = workerPositionList.get(j);
            this.workers[j] = new MultiTaskNonPrivacyWorker(workerPosition.getIndex());
            this.workers[j].setMaxRange(workerRange);
        }

    }

    protected double getUtilityValue(double taskValue, double realDistance) {
        return taskValue  - transformDistanceToValue(realDistance);
    }
}
