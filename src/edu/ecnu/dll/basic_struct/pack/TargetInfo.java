package edu.ecnu.dll.basic_struct.pack;

public class TargetInfo extends TaskIDDistanceBudgetPair {
    public Double target;
    public Double newCostPrivacyBudget;
    public Double newPrivacyBudget;
    public Double newNoiseDistance;
    public Double newUtilityValue;

    public TargetInfo() {
    }


    public TargetInfo(Integer taskID, Double noiseEffectiveDistance, Double effectivePrivacyBudget, Double target) {
        super(taskID, noiseEffectiveDistance, effectivePrivacyBudget);
        this.target = target;
    }


    public TargetInfo(Integer taskID, Double noiseEffectiveDistance, Double effectivePrivacyBudget, Double target, Double newCostPrivacyBudget, Double newPrivacyBudget, Double newNoiseDistance, Double newUtilityValue) {
        super(taskID, noiseEffectiveDistance, effectivePrivacyBudget);
        this.target = target;
        this.newCostPrivacyBudget = newCostPrivacyBudget;
        this.newPrivacyBudget = newPrivacyBudget;
        this.newNoiseDistance = newNoiseDistance;
        this.newUtilityValue = newUtilityValue;
    }
}
