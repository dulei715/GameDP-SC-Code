package edu.ecnu.dll.struct.pack;

public class TaskIDDIstanceBugetPair {
    public Integer taskID = null;
    public Double noiseEverageDistance = null;
    public Double totalPrivacyBudget = null;

    public TaskIDDIstanceBugetPair() {
    }

    public TaskIDDIstanceBugetPair(Integer taskID, Double noiseEverageDistance, Double totalPrivacyBudget) {
        this.taskID = taskID;
        this.noiseEverageDistance = noiseEverageDistance;
        this.totalPrivacyBudget = totalPrivacyBudget;
    }
}
