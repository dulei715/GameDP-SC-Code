package edu.ecnu.dll.basic.basic_struct.pack.single_agent_info.sub_class.sub_class;

import edu.ecnu.dll.basic.basic_struct.pack.single_agent_info.sub_class.TaskIDDistanceBudgetPair;

public class TaskTargetInfo extends TaskIDDistanceBudgetPair {
    protected Double target;
    protected Double newTotalCostPrivacyBudget;
    protected Double newPrivacyBudget;
    protected Double newNoiseDistance;
    protected Double newUtilityValue;

    public TaskTargetInfo() {
    }

    public Double getTarget() {
        return target;
    }


    public void setTarget(Double target) {
        this.target = target;
    }

    public Double getNewTotalCostPrivacyBudget() {
        return newTotalCostPrivacyBudget;
    }

    public void setNewTotalCostPrivacyBudget(Double newTotalCostPrivacyBudget) {
        this.newTotalCostPrivacyBudget = newTotalCostPrivacyBudget;
    }

    public Double getNewPrivacyBudget() {
        return this.newPrivacyBudget;
    }

    public void setNewPrivacyBudget(Double newPrivacyBudget) {
        this.newPrivacyBudget = newPrivacyBudget;
    }

    public Double getNewNoiseDistance() {
        return this.newNoiseDistance;
    }

    public void setNewNoiseDistance(Double newNoiseDistance) {
        this.newNoiseDistance = newNoiseDistance;
    }

    public Double getNewUtilityValue() {
        return newUtilityValue;
    }

    public void setNewUtilityValue(Double newUtilityValue) {
        this.newUtilityValue = newUtilityValue;
    }

    public TaskTargetInfo(Integer taskID, Double noiseEffectiveDistance, Double effectivePrivacyBudget, Double target) {
        super(taskID, noiseEffectiveDistance, effectivePrivacyBudget);
        this.target = target;
    }


    public TaskTargetInfo(Integer taskID, Double noiseEffectiveDistance, Double effectivePrivacyBudget, Double target, Double newTotalCostPrivacyBudget, Double newPrivacyBudget, Double newNoiseDistance, Double newUtilityValue) {
        super(taskID, noiseEffectiveDistance, effectivePrivacyBudget);
        this.target = target;
        this.newTotalCostPrivacyBudget = newTotalCostPrivacyBudget;
        this.newPrivacyBudget = newPrivacyBudget;
        this.newNoiseDistance = newNoiseDistance;
        this.newUtilityValue = newUtilityValue;
    }



}
