package edu.ecnu.dll.struct.pack;

public class TaskIDistanceBugetPairTaskEntropy extends TaskIDDistanceBugetPair{
    public Double taskEntropy;

    public TaskIDistanceBugetPairTaskEntropy() {
    }

    public TaskIDistanceBugetPairTaskEntropy(Double taskEntropy) {
        this.taskEntropy = taskEntropy;
    }

    public TaskIDistanceBugetPairTaskEntropy(Integer taskID, Double noiseEverageDistance, Double totalPrivacyBudget, Double taskEntropy) {
        super(taskID, noiseEverageDistance, totalPrivacyBudget);
        this.taskEntropy = taskEntropy;
    }
}
