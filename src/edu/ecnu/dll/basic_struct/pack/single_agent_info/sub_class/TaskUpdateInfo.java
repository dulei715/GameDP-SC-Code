package edu.ecnu.dll.basic_struct.pack.single_agent_info.sub_class;

public class TaskUpdateInfo extends TaskIDDistanceBudgetPair {
    protected Integer budgetIndexIncrement;
    protected Double newTotalCostPrivacyBudget;
    protected Double newPrivacyBudget;
    protected Double newNoiseDistance;
    protected Double newSuccessfulUtilityFunctionValue;

    public TaskUpdateInfo() {
    }

    public TaskUpdateInfo(Integer taskID, Double effectiveNoiseDistance, Double effectivePrivacyBudget, Integer budgetIndexIncrement, Double newTotalCostPrivacyBudget, Double newPrivacyBudget, Double newNoiseDistance, Double newSuccessfulUtilityFunctionValue) {
        super(taskID, effectiveNoiseDistance, effectivePrivacyBudget);
        this.budgetIndexIncrement = budgetIndexIncrement;
        this.newTotalCostPrivacyBudget = newTotalCostPrivacyBudget;
        this.newPrivacyBudget = newPrivacyBudget;
        this.newNoiseDistance = newNoiseDistance;
        this.newSuccessfulUtilityFunctionValue = newSuccessfulUtilityFunctionValue;
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
