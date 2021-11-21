package edu.ecnu.dll.basic.basic_struct.pack.single_agent_info.sub_class;

public class WorkerTaskUpdateInfo extends TaskIDDistanceBudgetPair {
    protected Integer workerID;
    protected Integer budgetIndexIncrement;
    protected Double newTotalCostPrivacyBudget;
    protected Double newPrivacyBudget;
    protected Double newNoiseDistance;
    protected Double newSuccessfulUtilityFunctionValue;

    public WorkerTaskUpdateInfo() {
    }

    public WorkerTaskUpdateInfo(Integer taskID, Double effectiveNoiseDistance, Double effectivePrivacyBudget, Integer workerID, Integer budgetIndexIncrement, Double newTotalCostPrivacyBudget, Double newPrivacyBudget, Double newNoiseDistance, Double newSuccessfulUtilityFunctionValue) {
        super(taskID, effectiveNoiseDistance, effectivePrivacyBudget);
        this.workerID = workerID;
        this.budgetIndexIncrement = budgetIndexIncrement;
        this.newTotalCostPrivacyBudget = newTotalCostPrivacyBudget;
        this.newPrivacyBudget = newPrivacyBudget;
        this.newNoiseDistance = newNoiseDistance;
        this.newSuccessfulUtilityFunctionValue = newSuccessfulUtilityFunctionValue;
    }

    public WorkerIDNoiseDistanceBudgetPair getWorkerIDEffectiveNoiseDistancePair() {
        return new WorkerIDNoiseDistanceBudgetPair(this.workerID, this.effectiveNoiseDistance, this.effectivePrivacyBudget);
    }

    public Integer getWorkerID() {
        return workerID;
    }

    public void setWorkerID(Integer workerID) {
        this.workerID = workerID;
    }

    public Integer getBudgetIndexIncrement() {
        return budgetIndexIncrement;
    }

    public void setBudgetIndexIncrement(Integer budgetIndexIncrement) {
        this.budgetIndexIncrement = budgetIndexIncrement;
    }

    public Double getNewTotalCostPrivacyBudget() {
        return newTotalCostPrivacyBudget;
    }

    public void setNewTotalCostPrivacyBudget(Double newTotalCostPrivacyBudget) {
        this.newTotalCostPrivacyBudget = newTotalCostPrivacyBudget;
    }

    public Double getNewPrivacyBudget() {
        return newPrivacyBudget;
    }

    public void setNewPrivacyBudget(Double newPrivacyBudget) {
        this.newPrivacyBudget = newPrivacyBudget;
    }

    public Double getNewNoiseDistance() {
        return newNoiseDistance;
    }

    public void setNewNoiseDistance(Double newNoiseDistance) {
        this.newNoiseDistance = newNoiseDistance;
    }

    public Double getNewSuccessfulUtilityFunctionValue() {
        return newSuccessfulUtilityFunctionValue;
    }

    public void setNewSuccessfulUtilityFunctionValue(Double newSuccessfulUtilityFunctionValue) {
        this.newSuccessfulUtilityFunctionValue = newSuccessfulUtilityFunctionValue;
    }
}
