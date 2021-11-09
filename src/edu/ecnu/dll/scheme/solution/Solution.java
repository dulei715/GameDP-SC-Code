package edu.ecnu.dll.scheme.solution;

import edu.ecnu.dll.basic_struct.agent.Task;
import edu.ecnu.dll.basic_struct.agent.Worker;
import edu.ecnu.dll.basic_struct.function.Normalization;
import edu.ecnu.dll.basic_struct.pack.single_agent_info.SingleInfoPack;
public abstract class Solution {
    protected Double minDistance = 0.0;
    protected Double maxDistance = 100_000.0;
    protected Double minPrivacyBudget = 0.0;
    protected Double maxPrivacyBudget = 100.0;   // 最大单个隐私预算*budget个数
//    protected Double maxValue = 100000.0;
//    protected Double minValue = 10000.0;
    protected Double maxValue = 0.0;
    protected Double minValue = 1.0;

    public Solution() {
    }

    public Solution(Double minDistance, Double maxDistance, Double minPrivacyBudget, Double maxPrivacyBudget) {
        this.minDistance = minDistance;
        this.maxDistance = maxDistance;
        this.minPrivacyBudget = minPrivacyBudget;
        this.maxPrivacyBudget = maxPrivacyBudget;
    }

    public Double getMinDistance() {
        return minDistance;
    }

    public void setMinDistance(Double minDistance) {
        this.minDistance = minDistance;
    }

    public Double getMaxDistance() {
        return maxDistance;
    }

    public void setMaxDistance(Double maxDistance) {
        this.maxDistance = maxDistance;
    }

    public Double getMinPrivacyBudget() {
        return minPrivacyBudget;
    }

    public void setMinPrivacyBudget(Double minPrivacyBudget) {
        this.minPrivacyBudget = minPrivacyBudget;
    }

    public Double getMaxPrivacyBudget() {
        return maxPrivacyBudget;
    }

    public void setMaxPrivacyBudget(Double maxPrivacyBudget) {
        this.maxPrivacyBudget = maxPrivacyBudget;
    }

    public Double normalizeDistance(Double distance) {
        return Normalization.getNormalizedValue(distance, minDistance, maxDistance);
    }

    public Double normalizePrivacybudget(Double privacyBudget) {
        return Normalization.getNormalizedValue(privacyBudget, minPrivacyBudget, maxPrivacyBudget);
    }

    public Double normalizeTaskValue(Double taskValue) {
        return Normalization.getNormalizedValue(taskValue, minValue, maxValue);
    }




}
