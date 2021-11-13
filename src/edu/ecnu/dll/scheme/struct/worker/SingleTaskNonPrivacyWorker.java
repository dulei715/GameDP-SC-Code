package edu.ecnu.dll.scheme.struct.worker;


import edu.ecnu.dll.basic_struct.agent.Worker;

public class SingleTaskNonPrivacyWorker extends Worker {
    public Double toTaskDistance = null;

//    public Integer[] taskCompetingTimes = null;


    // 记录成功进入候选的utility函数值
    public Double successfullyUtilityFunctionValue = null;
    // 记录每次计算的utility函数值
    public Double currentUtilityFunctionValue = null;


    public SingleTaskNonPrivacyWorker() {

    }

//    public MultiTaskNonPrivacyWorker(int taskNumber) {
//        this.toTaskDistance = new Double[taskNumber];
//        this.taskCompletingTimes = new Integer[taskNumber];
//    }

    public SingleTaskNonPrivacyWorker(double[] index) {
        super(index);
    }

    @Override
    public Double getToTaskDistance(Integer taskID) {
        return this.toTaskDistance;
    }

    @Override
    public Double getFinalUtility(Integer taskID) {
        return successfullyUtilityFunctionValue;
    }

    @Override
    public Integer getTaskCompetingTimes(Integer taskID) {
        // todo: handle
        return null;
    }


}
