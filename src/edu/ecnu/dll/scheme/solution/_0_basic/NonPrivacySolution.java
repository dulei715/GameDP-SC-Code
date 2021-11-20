package edu.ecnu.dll.scheme.solution._0_basic;

import edu.ecnu.dll.basic_struct.agent.Task;
import edu.ecnu.dll.scheme.struct.task.BasicTask;
import edu.ecnu.dll.scheme.struct.task.NonPrivacyTask;
import edu.ecnu.dll.scheme.struct.worker.MultiTaskBasicWorker;
import edu.ecnu.dll.scheme.struct.worker.MultiTaskNonPrivacyWorker;
import tools.basic.BasicArray;
import tools.basic.BasicCalculation;
import tools.struct.Point;

import java.util.ArrayList;
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
            this.workers[j].taskIndex = BasicArray.getInitializedArray(-1, this.tasks.length);
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
            this.workers[j].taskIndex = BasicArray.getInitializedArray(-1, this.tasks.length);
        }

    }

    public void initializeAgents() {
        for (int j = 0; j < this.workers.length; j++) {
            this.workers[j].reverseIndex = new ArrayList<>();
            this.workers[j].toTaskDistance = new ArrayList<>();
            // 此处没有初始化privacyBudgetArray，因为会在initialize basic function 中初始化
            this.workers[j].currentUtilityFunctionValue = new ArrayList<>();
            this.workers[j].successfullyUtilityFunctionValue = new ArrayList<>();
            double tempDistance;
            int k = 0;
            for (int i = 0; i < tasks.length; i++) {
                // todo: 修改为经纬度的
                tempDistance = BasicCalculation.get2Norm(this.tasks[i].location, this.workers[j].location);
                if (tempDistance <= this.workers[j].maxRange) {
                    this.workers[j].taskIndex[i] = k++;
                    this.workers[j].reverseIndex.add(i);
                    this.workers[j].toTaskDistance.add(tempDistance);
                    this.workers[j].currentUtilityFunctionValue.add(0.0);
                    this.workers[j].successfullyUtilityFunctionValue.add(0.0);
                }
            }
        }
    }

    public void initializeAgentsWithLatitudeLongitude() {
        for (int j = 0; j < this.workers.length; j++) {
            this.workers[j].reverseIndex = new ArrayList<>();
            this.workers[j].toTaskDistance = new ArrayList<>();
            // 此处没有初始化privacyBudgetArray，因为会在initialize basic function 中初始化
            this.workers[j].currentUtilityFunctionValue = new ArrayList<>();
            this.workers[j].successfullyUtilityFunctionValue = new ArrayList<>();
            double tempDistance;
            int k = 0;
            for (int i = 0; i < tasks.length; i++) {
                // todo: 修改为经纬度的
                tempDistance = BasicCalculation.getDistanceFrom2LngLat(this.tasks[i].location[1], this.tasks[i].location[0], this.workers[j].location[1],this.workers[j].location[0]);
                if (tempDistance <= this.workers[j].maxRange) {
                    this.workers[j].taskIndex[i] = k++;
                    this.workers[j].reverseIndex.add(i);
                    this.workers[j].toTaskDistance.add(tempDistance);
                    this.workers[j].currentUtilityFunctionValue.add(0.0);
                    this.workers[j].successfullyUtilityFunctionValue.add(0.0);
                }
            }
        }
    }


    protected double getUtilityValue(double taskValue, double realDistance) {
        return taskValue  - transformDistanceToValue(realDistance);
    }
}
