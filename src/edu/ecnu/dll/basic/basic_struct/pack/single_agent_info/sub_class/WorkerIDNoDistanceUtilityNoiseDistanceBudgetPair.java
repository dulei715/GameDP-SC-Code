package edu.ecnu.dll.basic.basic_struct.pack.single_agent_info.sub_class;

import edu.ecnu.dll.basic.basic_solution.PrivacySolution;

public class WorkerIDNoDistanceUtilityNoiseDistanceBudgetPair extends WorkerIDNoiseDistanceBudgetPair {
    private Double noDistanceUtility = null;

    public WorkerIDNoDistanceUtilityNoiseDistanceBudgetPair() {
    }

    public WorkerIDNoDistanceUtilityNoiseDistanceBudgetPair(Integer workerID, Double effectiveNoiseDistance, Double effectivePrivacyBudget, Double noDistanceUtility) {
        super(workerID, effectiveNoiseDistance, effectivePrivacyBudget);
        this.noDistanceUtility = noDistanceUtility;
    }

    public Double getNoDistanceUtility() {
        return noDistanceUtility;
    }

    public void setNoDistanceUtility(Double noDistanceUtility) {
        this.noDistanceUtility = noDistanceUtility;
    }

    public Double getEffectiveUtility() {
        return PrivacySolution.getUtilityFromWithoutDistanceUtilityAndDistance(this.noDistanceUtility, this.getEffectiveNoiseDistance());
    }

    @Override
    public String toString() {
        return super.toString() + "; WorkerIDNoDistanceUtilityNoiseDistanceBudgetPair{" +
                "noDistanceUtility=" + noDistanceUtility +
                '}';
    }
}
