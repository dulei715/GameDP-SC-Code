package edu.ecnu.dll.basic.basic_struct.pack.single_agent_info.sub_class;

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

    @Override
    public String toString() {
        return super.toString() + "; WorkerIDNoDistanceUtilityNoiseDistanceBudgetPair{" +
                "noDistanceUtility=" + noDistanceUtility +
                '}';
    }
}
