package edu.ecnu.dll.basic_struct.pack;

/**
 * 用于记录竞争该task成功的worker的有效噪声距离和有效隐私预算
 */
public class WorkerIDDistanceBudgetPair {
    public Integer workerID = null;
    public Double noiseEffectiveDistance = null;
    public Double effectivePrivacyBudget = null;

    public WorkerIDDistanceBudgetPair() {
    }

    public WorkerIDDistanceBudgetPair(Integer workerID, Double noiseEffectiveDistance, Double effectivePrivacyBudget) {
        this.workerID = workerID;
        this.noiseEffectiveDistance = noiseEffectiveDistance;
        this.effectivePrivacyBudget = effectivePrivacyBudget;
    }

    @Override
    public String toString() {
        return "WorkerIDDistanceBudgetPair{" +
                "workerID=" + workerID +
                ", noiseEffectiveDistance=" + noiseEffectiveDistance +
                ", effectivePrivacyBudget=" + effectivePrivacyBudget +
                '}';
    }
}
