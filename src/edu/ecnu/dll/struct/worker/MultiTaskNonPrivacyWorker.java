package edu.ecnu.dll.struct.worker;


public class MultiTaskNonPrivacyWorker extends Worker {
    public Double[] toTaskDistance = null;
    //task number * budget number

    public Double currentUtilityFunctionValue = null;


    public Integer[] taskCompletingTimes = null;

    public MultiTaskNonPrivacyWorker() {

    }

    public MultiTaskNonPrivacyWorker(int taskNumber) {
        this.toTaskDistance = new Double[taskNumber];
        this.taskCompletingTimes = new Integer[taskNumber];
    }



}
