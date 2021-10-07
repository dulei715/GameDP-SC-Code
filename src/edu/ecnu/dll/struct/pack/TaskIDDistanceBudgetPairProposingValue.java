package edu.ecnu.dll.struct.pack;

public class TaskIDDistanceBudgetPairProposingValue extends TaskIDDistanceBudgetPair {
    public Double proposingValue;

    public TaskIDDistanceBudgetPairProposingValue() {
    }

    public TaskIDDistanceBudgetPairProposingValue(Double proposingValue) {
        this.proposingValue = proposingValue;
    }

    public TaskIDDistanceBudgetPairProposingValue(Integer taskID, Double noiseAverageDistance, Double totalPrivacyBudget, Double proposingValue) {
        super(taskID, noiseAverageDistance, totalPrivacyBudget);
        this.proposingValue = proposingValue;
    }
}
