package edu.ecnu.dll.basic_struct.comparator;

import edu.ecnu.dll.basic_struct.pack.single_agent_info.sub_class.WorkerIDDistanceBudgetPair;
import tools.differential_privacy.compare.impl.LaplaceProbabilityDensityFunction;

import java.util.Comparator;

public class WorkerIDDistanceBudgetPairComparator implements Comparator<WorkerIDDistanceBudgetPair> {
    @Override
    public int compare(WorkerIDDistanceBudgetPair elemA, WorkerIDDistanceBudgetPair elemB) {
        if ((elemA == null || elemA.getWorkerID() == null || elemA.getNoiseEffectiveDistance() == null || elemA.getEffectivePrivacyBudget() == null) && (elemB == null || elemB.getWorkerID() == null || elemB.getNoiseEffectiveDistance() == null || elemB.getEffectivePrivacyBudget() == null)) {
            return 0;
        }
        // 空就是无限大距离
        if (elemA == null || elemA.getWorkerID() == null || elemA.getNoiseEffectiveDistance() == null || elemA.getEffectivePrivacyBudget() == null) {
            return 1;
        }
        if (elemB == null || elemB.getWorkerID() == null || elemB.getNoiseEffectiveDistance() == null || elemB.getEffectivePrivacyBudget() == null) {
            return -1;
        }

        double compareValue = LaplaceProbabilityDensityFunction.probabilityDensityFunction(elemA.getNoiseEffectiveDistance(), elemB.getNoiseEffectiveDistance(), elemA.getEffectivePrivacyBudget(), elemB.getEffectivePrivacyBudget());
        if (compareValue > 0.5) {
            return -1;
        } else if (compareValue < 0.5) {
            return 1;
        }

        return elemA.getWorkerID().compareTo(elemB.getWorkerID());
    }
}
