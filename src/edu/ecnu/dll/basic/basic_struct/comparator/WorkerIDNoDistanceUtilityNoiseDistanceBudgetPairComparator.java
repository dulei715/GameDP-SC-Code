package edu.ecnu.dll.basic.basic_struct.comparator;

import edu.ecnu.dll.basic.basic_struct.pack.single_agent_info.sub_class.WorkerIDNoDistanceUtilityNoiseDistanceBudgetPair;
import tools.differential_privacy.compare.impl.LaplaceProbabilityDensityFunction;

import java.util.Comparator;

public class WorkerIDNoDistanceUtilityNoiseDistanceBudgetPairComparator implements Comparator<WorkerIDNoDistanceUtilityNoiseDistanceBudgetPair> {


    @Override
    public int compare(WorkerIDNoDistanceUtilityNoiseDistanceBudgetPair elemA, WorkerIDNoDistanceUtilityNoiseDistanceBudgetPair elemB) {
        if ((elemA == null || elemA.getWorkerID() == null || elemA.getNoDistanceUtility() == null || elemA.getEffectiveNoiseDistance() == null || elemA.getEffectivePrivacyBudget() == null) && (elemB == null || elemB.getWorkerID() == null || elemB.getNoDistanceUtility() == null || elemB.getEffectiveNoiseDistance() == null || elemB.getEffectivePrivacyBudget() == null)) {
            return 0;
        }
        // 空就是无限大距离
        if (elemA == null || elemA.getWorkerID() == null || elemA.getNoDistanceUtility() == null || elemA.getEffectiveNoiseDistance() == null || elemA.getEffectivePrivacyBudget() == null) {
            return 1;
        }
        if (elemB == null || elemB.getWorkerID() == null || elemB.getNoDistanceUtility() == null || elemB.getEffectiveNoiseDistance() == null || elemB.getEffectivePrivacyBudget() == null) {
            return -1;
        }

        Double noDistanceUtilityA = elemA.getNoDistanceUtility();
        Double noDistanceUtilityB = elemB.getNoDistanceUtility();

        Double effectiveNoiseDistanceA = elemA.getEffectiveNoiseDistance();
        Double effectiveNoiseDistanceB = elemB.getEffectiveNoiseDistance();

        Double effectivePrivacyBudgetA = elemA.getEffectivePrivacyBudget();
        Double effectivePrivacyBudgetB = elemB.getEffectivePrivacyBudget();

        double compareValueUtility = LaplaceProbabilityDensityFunction.probabilityDensityFunction(effectiveNoiseDistanceA, effectiveNoiseDistanceB + noDistanceUtilityA - noDistanceUtilityB, effectivePrivacyBudgetA, effectivePrivacyBudgetB);
        if (compareValueUtility > 0.5) {
            // 说明effectiveA的utility更大(为了降序排列，将规定u1<u2为u1大于u2)
            return -1;
        } else if (compareValueUtility < 0.5) {
            return 1;
        }

        double compareValue = LaplaceProbabilityDensityFunction.probabilityDensityFunction(effectiveNoiseDistanceA, effectiveNoiseDistanceB, effectivePrivacyBudgetA, effectivePrivacyBudgetB);
        if (compareValue > 0.5) {
            return -1;
        } else if (compareValue < 0.5) {
            return 1;
        }

        return elemA.getWorkerID().compareTo(elemB.getWorkerID());
    }
}
