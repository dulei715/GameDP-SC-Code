package edu.ecnu.dll.basic_struct.pack.single_agent_info.sub_class;

import edu.ecnu.dll.basic_struct.pack.single_agent_info.SingleInfoPack;

/**
 * 用于记录竞争该task成功的worker的有效噪声距离和有效隐私预算
 */
public class TaskIDDistanceBudgetPair extends SingleInfoPack {
    private Integer taskID = null;
    private Double effectiveNoiseDistance = null;
    private Double effectivePrivacyBudget = null;

    public TaskIDDistanceBudgetPair() {
    }

    public Integer getTaskID() {
        return taskID;
    }

    public void setTaskID(Integer taskID) {
        this.taskID = taskID;
    }

    public Double getEffectiveNoiseDistance() {
        return this.effectiveNoiseDistance;
    }

    public void setEffectiveNoiseDistance(Double effectiveNoiseDistance) {
        this.effectiveNoiseDistance = effectiveNoiseDistance;
    }

    public Double getEffectivePrivacyBudget() {
        return this.effectivePrivacyBudget;
    }

    public void setEffectivePrivacyBudget(Double effectivePrivacyBudget) {
        this.effectivePrivacyBudget = effectivePrivacyBudget;
    }

    public TaskIDDistanceBudgetPair(Integer taskID, Double effectiveNoiseDistance, Double effectivePrivacyBudget) {
        this.taskID = taskID;
        this.effectiveNoiseDistance = effectiveNoiseDistance;
        this.effectivePrivacyBudget = effectivePrivacyBudget;
    }
}
