package edu.ecnu.dll.basic_struct.pack;

public class TaskIDDistanceBudgetPair {
    public Integer taskID = null;
    public Double noiseAverageDistance = null;
    public Double totalPrivacyBudget = null;

    public TaskIDDistanceBudgetPair() {
    }

    public TaskIDDistanceBudgetPair(Integer taskID, Double noiseAverageDistance, Double totalPrivacyBudget) {
        this.taskID = taskID;
        this.noiseAverageDistance = noiseAverageDistance;
        this.totalPrivacyBudget = totalPrivacyBudget;
    }
}
