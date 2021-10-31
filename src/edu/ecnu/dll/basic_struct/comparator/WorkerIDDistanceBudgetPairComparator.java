package edu.ecnu.dll.basic_struct.comparator;

import edu.ecnu.dll.basic_struct.pack.WorkerIDDistanceBudgetPair;
import tools.differential_privacy.compare.ProbabilityCompareFunction;
import tools.differential_privacy.compare.impl.LaplaceProbabilityDensityFunction;
import tools.differential_privacy.noise.LaplaceUtils;

import java.util.Comparator;

public class WorkerIDDistanceBudgetPairComparator implements Comparator<WorkerIDDistanceBudgetPair> {
    @Override
    public int compare(WorkerIDDistanceBudgetPair elemA, WorkerIDDistanceBudgetPair elemB) {
        if ((elemA == null || elemA.workerID == null || elemA.noiseEffectiveDistance == null || elemA.effectivePrivacyBudget == null) && (elemB == null || elemB.workerID == null || elemB.noiseEffectiveDistance == null || elemB.effectivePrivacyBudget == null)) {
            return 0;
        }
        // 空就是无限大距离
        if (elemA == null || elemA.workerID == null || elemA.noiseEffectiveDistance == null || elemA.effectivePrivacyBudget == null) {
            return 1;
        }
        if (elemB == null || elemB.workerID == null || elemB.noiseEffectiveDistance == null || elemB.effectivePrivacyBudget == null) {
            return -1;
        }

        double compareValue = LaplaceProbabilityDensityFunction.probabilityDensityFunction(elemA.noiseEffectiveDistance, elemB.noiseEffectiveDistance, elemA.effectivePrivacyBudget, elemB.effectivePrivacyBudget);
        if (compareValue > 0.5) {
            return -1;
        } else if (compareValue < 0.5) {
            return 1;
        }

        return elemA.workerID.compareTo(elemB.workerID);
    }
}
