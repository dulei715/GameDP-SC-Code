package edu.ecnu.dll.basic_struct.pack;

public class TaskWorkerIDSuccessfulValuationPair extends TaskWorkerIDPair {
    private Double successfulUtilityValue = null;

    public TaskWorkerIDSuccessfulValuationPair() {
    }

    public TaskWorkerIDSuccessfulValuationPair(Integer taskID, Integer workerID, Double successfulUtilityValue) {
        super(taskID, workerID);
        this.successfulUtilityValue = successfulUtilityValue;
    }

    public Double getSuccessfulUtilityValue() {
        return successfulUtilityValue;
    }

    public void setSuccessfulUtilityValue(Double successfulUtilityValue) {
        this.successfulUtilityValue = successfulUtilityValue;
    }
}
