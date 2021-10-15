package edu.ecnu.dll.basic_struct.pack;

public class TaskIDDistanceBudgetPairTaskEntropy extends TaskIDDistanceBudgetPair {
    public Double taskEntropy;

    public TaskIDDistanceBudgetPairTaskEntropy() {
    }

    public TaskIDDistanceBudgetPairTaskEntropy(Double taskEntropy) {
        this.taskEntropy = taskEntropy;
    }

    public TaskIDDistanceBudgetPairTaskEntropy(Integer taskID, Double noiseAverageDistance, Double totalPrivacyBudget, Double taskEntropy) {
        super(taskID, noiseAverageDistance, totalPrivacyBudget);
        this.taskEntropy = taskEntropy;
    }
}
