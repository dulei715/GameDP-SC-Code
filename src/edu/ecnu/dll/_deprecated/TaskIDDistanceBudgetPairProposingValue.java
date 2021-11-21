package edu.ecnu.dll._deprecated;

import edu.ecnu.dll.basic.basic_struct.pack.single_agent_info.sub_class.TaskIDDistanceBudgetPair;

@Deprecated
public class TaskIDDistanceBudgetPairProposingValue extends TaskIDDistanceBudgetPair {
    public Double proposingValue;

    public TaskIDDistanceBudgetPairProposingValue() {
    }

    public TaskIDDistanceBudgetPairProposingValue(Double proposingValue) {
        this.proposingValue = proposingValue;
    }

    public TaskIDDistanceBudgetPairProposingValue(Integer taskID, Double noiseAverageDistance, Double totalPrivacyBudget, Double proposingValue) {
        super(taskID, noiseAverageDistance, totalPrivacyBudget);
        this.proposingValue = proposingValue;
    }
}
