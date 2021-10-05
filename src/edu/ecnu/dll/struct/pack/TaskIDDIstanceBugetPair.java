package edu.ecnu.dll.struct.pack;

public class TaskIDDistanceBugetPair {
    public Integer taskID = null;
    public Double noiseEverageDistance = null;
    public Double totalPrivacyBudget = null;

    public TaskIDDistanceBugetPair() {
    }

    public TaskIDDistanceBugetPair(Integer taskID, Double noiseEverageDistance, Double totalPrivacyBudget) {
        this.taskID = taskID;
        this.noiseEverageDistance = noiseEverageDistance;
        this.totalPrivacyBudget = totalPrivacyBudget;
    }
}
