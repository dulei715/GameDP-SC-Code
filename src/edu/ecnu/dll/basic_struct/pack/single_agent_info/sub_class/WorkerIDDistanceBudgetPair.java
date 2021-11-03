package edu.ecnu.dll.basic_struct.pack.single_agent_info.sub_class;

import edu.ecnu.dll.basic_struct.pack.single_agent_info.SingleInfoPack;

/**
 * 用于记录竞争该task成功的worker的有效噪声距离和有效隐私预算
 */
public class WorkerIDDistanceBudgetPair extends SingleInfoPack {
    private Integer workerID = null;
    private Double noiseEffectiveDistance = null;
    private Double effectivePrivacyBudget = null;

    public WorkerIDDistanceBudgetPair() {
    }

    public WorkerIDDistanceBudgetPair(Integer workerID, Double noiseEffectiveDistance, Double effectivePrivacyBudget) {
        this.workerID = workerID;
        this.noiseEffectiveDistance = noiseEffectiveDistance;
        this.effectivePrivacyBudget = effectivePrivacyBudget;
    }

    public Integer getWorkerID() {
        return workerID;
    }

    public void setWorkerID(Integer workerID) {
        this.workerID = workerID;
    }

    public Double getNoiseEffectiveDistance() {
        return noiseEffectiveDistance;
    }

    public void setNoiseEffectiveDistance(Double noiseEffectiveDistance) {
        this.noiseEffectiveDistance = noiseEffectiveDistance;
    }

    public Double getEffectivePrivacyBudget() {
        return effectivePrivacyBudget;
    }

    public void setEffectivePrivacyBudget(Double effectivePrivacyBudget) {
        this.effectivePrivacyBudget = effectivePrivacyBudget;
    }

    @Override
    public String toString() {
        return "WorkerIDDistanceBudgetPair{" +
                "workerID=" + workerID +
                ", noiseEffectiveDistance=" + this.noiseEffectiveDistance +
                ", effectivePrivacyBudget=" + this.effectivePrivacyBudget +
                '}';
    }
}
