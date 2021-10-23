package edu.ecnu.dll.basic_struct.pack;

import tools.differential_privacy.noise.LaplaceUtils;

public class EntropyInfo extends TaskIDDistanceBudgetPair {
    public Double taskEntropy;
    public Double newCostPrivacyBudget;
    public Double newPrivacyBudget;
    public Double newNoiseDistance;
    public Double newUtilityValue;

    public EntropyInfo() {
    }


    public EntropyInfo(Integer taskID, Double noiseEffectiveDistance, Double effectivePrivacyBudget, Double taskEntropy) {
        super(taskID, noiseEffectiveDistance, effectivePrivacyBudget);
        this.taskEntropy = taskEntropy;
    }


    public EntropyInfo(Integer taskID, Double noiseEffectiveDistance, Double effectivePrivacyBudget, Double taskEntropy, Double newCostPrivacyBudget, Double newPrivacyBudget, Double newNoiseDistance, Double newUtilityValue) {
        super(taskID, noiseEffectiveDistance, effectivePrivacyBudget);
        this.taskEntropy = taskEntropy;
        this.newCostPrivacyBudget = newCostPrivacyBudget;
        this.newPrivacyBudget = newPrivacyBudget;
        this.newNoiseDistance = newNoiseDistance;
        this.newUtilityValue = newUtilityValue;
    }
}
