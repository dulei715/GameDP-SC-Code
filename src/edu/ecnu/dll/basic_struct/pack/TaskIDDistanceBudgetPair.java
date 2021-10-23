package edu.ecnu.dll.basic_struct.pack;

public class TaskIDDistanceBudgetPair {
    public Integer taskID = null;
    public Double noiseEffectiveDistance = null;
    public Double effectivePrivacyBudget = null;

    public TaskIDDistanceBudgetPair() {
    }

    public TaskIDDistanceBudgetPair(Integer taskID, Double noiseEffectiveDistance, Double effectivePrivacyBudget) {
        this.taskID = taskID;
        this.noiseEffectiveDistance = noiseEffectiveDistance;
        this.effectivePrivacyBudget = effectivePrivacyBudget;
    }
}
