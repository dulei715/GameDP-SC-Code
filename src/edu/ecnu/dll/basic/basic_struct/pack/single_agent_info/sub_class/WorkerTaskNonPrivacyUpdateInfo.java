package edu.ecnu.dll.basic.basic_struct.pack.single_agent_info.sub_class;

public class WorkerTaskNonPrivacyUpdateInfo extends TaskIDDistancePair {
    protected Integer workerID;
    protected Double newSuccessfulUtilityFunctionValue;

    public WorkerTaskNonPrivacyUpdateInfo() {
    }

    public WorkerTaskNonPrivacyUpdateInfo(Integer taskID, Double distance, Integer workerID, Double newSuccessfulUtilityFunctionValue) {
        super(taskID, distance);
        this.workerID = workerID;
        this.newSuccessfulUtilityFunctionValue = newSuccessfulUtilityFunctionValue;
    }

    public WorkerIDDistancePair getWorkerIDDistancePair() {
        return new WorkerIDDistancePair(this.workerID, this.distance);
    }

    public Integer getWorkerID() {
        return workerID;
    }

    public void setWorkerID(Integer workerID) {
        this.workerID = workerID;
    }


    public Double getNewSuccessfulUtilityFunctionValue() {
        return newSuccessfulUtilityFunctionValue;
    }

    public void setNewSuccessfulUtilityFunctionValue(Double newSuccessfulUtilityFunctionValue) {
        this.newSuccessfulUtilityFunctionValue = newSuccessfulUtilityFunctionValue;
    }
}
