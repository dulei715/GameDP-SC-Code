package edu.ecnu.dll.scheme.struct.worker;


import edu.ecnu.dll.basic_struct.agent.Worker;

import java.util.List;

public class MultiTaskNonPrivacyWorker extends Worker {

    public List<Double> toTaskDistance = null;
    //task number * budget number



    public List<Integer> competingTimes = null;


    // 记录成功进入候选的utility函数值
    public List<Double> successfullyUtilityFunctionValue = null;
    // 记录每次计算的utility函数值
    public List<Double> currentUtilityFunctionValue = null;

    public Boolean currentWinningState = null;


    public MultiTaskNonPrivacyWorker() {

    }

//    public MultiTaskNonPrivacyWorker(int taskNumber) {
//        this.toTaskDistance = new Double[taskNumber];
//        this.taskCompletingTimes = new Integer[taskNumber];
//    }

    public MultiTaskNonPrivacyWorker(double[] index) {
        super(index);
    }

    public Double getToTaskDistance(Integer taskID) {
        int index = taskIndex[taskID];
        if (index == -1) {
            return null;
        }
        return toTaskDistance.get(index);
    }

    @Override
    public Double getFinalUtility(Integer taskID) {
        return getSuccessfullyUtilityFunctionValue(taskID);
    }

    public int setToTaskDistance(Integer taskID, Double distance) {
        int index = taskIndex[taskID];
        if (index == -1) {
            return -1;
        }
        this.toTaskDistance.set(index, distance);
        return 0;
    }


    @Override
    public Integer getTaskCompetingTimes(Integer taskID) {
        int index = taskIndex[taskID];
        if (index == -1) {
            return null;
        }
        return competingTimes.get(index);
    }

    public int setTaskCompetingTimes(Integer taskID, Integer taskCompetingTimes) {
        int index = taskIndex[taskID];
        if (index == -1) {
            return -1;
        }
        this.competingTimes.set(index, taskCompetingTimes);
        return 0;
    }

    public int increaseTaskCompetingTimes(Integer taskID) {
        int index = taskIndex[taskID];
        if (index == -1) {
            return -1;
        }
        int increaseTime = this.competingTimes.get(index);
        this.competingTimes.set(index, increaseTime + 1);
        return 0;
    }

    public Double getSuccessfullyUtilityFunctionValue(Integer taskID) {
        int index = taskIndex[taskID];
        if (index == -1) {
            return null;
        }
        return successfullyUtilityFunctionValue.get(index);
    }

    public int setSuccessfullyUtilityFunctionValue(Integer taskID, Double successfullyUtilityFunctionValue) {
        int index = taskIndex[taskID];
        if (index == -1) {
            return -1;
        }
        this.successfullyUtilityFunctionValue.set(index, successfullyUtilityFunctionValue);
        return 0;
    }

    public Double getCurrentUtilityFunctionValue(Integer taskID) {
        int index = taskIndex[taskID];
        if (index == -1) {
            return null;
        }
        return currentUtilityFunctionValue.get(index);
    }

    public int setCurrentUtilityFunctionValue(Integer taskID, Double currentUtilityFunctionValue) {
        int index = taskIndex[taskID];
        if (index == -1) {
            return -1;
        }
        this.currentUtilityFunctionValue.set(index, currentUtilityFunctionValue);
        return 0;
    }

    public int increaseSuccessfullyUtilityFunctionValue(Integer taskID, Double additionValue) {
        int index = taskIndex[taskID];
        if (index == -1) {
            return -1;
        }
        Double originalValue = this.successfullyUtilityFunctionValue.get(index);
        this.successfullyUtilityFunctionValue.set(index, originalValue + additionValue);
        return 0;
    }

}
