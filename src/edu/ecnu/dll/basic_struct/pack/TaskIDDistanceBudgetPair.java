package edu.ecnu.dll.basic_struct.pack;

/**
 * 用于记录竞争该task成功的worker的有效噪声距离和有效隐私预算
 */
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
