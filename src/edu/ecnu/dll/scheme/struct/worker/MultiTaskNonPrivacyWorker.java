package edu.ecnu.dll.scheme.struct.worker;


import edu.ecnu.dll.basic_struct.agent.Worker;

public class MultiTaskNonPrivacyWorker extends Worker {
    public Double[] toTaskDistance = null;
    //task number * budget number



    public Integer[] taskCompetingTimes = null;


    // 记录成功进入候选的utility函数值
    public Double[] successfullyUtilityFunctionValue = null;
    // 记录每次计算的utility函数值
    public Double[] currentUtilityFunctionValue = null;


    public MultiTaskNonPrivacyWorker() {

    }

//    public MultiTaskNonPrivacyWorker(int taskNumber) {
//        this.toTaskDistance = new Double[taskNumber];
//        this.taskCompletingTimes = new Integer[taskNumber];
//    }

    public MultiTaskNonPrivacyWorker(double[] index) {
        super(index);
    }



}
