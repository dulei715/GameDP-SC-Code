package edu.ecnu.dll.scheme.solution._3_multiple_task.struct;

import edu.ecnu.dll.basic_struct.pack.single_agent_info.sub_class.WorkerIDDistanceBudgetPair;
import edu.ecnu.dll.scheme_compared.solution.ConflictElimination;
import tools.differential_privacy.compare.impl.LaplaceProbabilityDensityFunction;

public class EnhanceConflictElimination extends ConflictElimination {
    public EnhanceConflictElimination(Integer taskSize, Integer workerSize) {
        super(taskSize, workerSize);
    }

    /**
     *
     * @param taskIDAWorkerInfo
     * @param taskIDBWorkerInfo
     * @param taskIDANextWorkerInfo
     * @param taskIDBNextWorkerInfo
     * @return 如果 taskIDB 比 taskIDA 占优，返回true，否则返回false
     */
    @Override
    protected boolean compareFourValuesWithSuccessor(WorkerIDDistanceBudgetPair taskIDAWorkerInfo, WorkerIDDistanceBudgetPair taskIDBWorkerInfo, WorkerIDDistanceBudgetPair taskIDANextWorkerInfo, WorkerIDDistanceBudgetPair taskIDBNextWorkerInfo) {
//        return super.compareFourValuesWithSuccessor(workerID, taskIDA, taskIDB, taskIDANextWorkerInfo, taskIDBNextWorkerInfo);
        double p_1, p_2;
        p_1 = LaplaceProbabilityDensityFunction.probabilityDensityFunction(taskIDAWorkerInfo.getNoiseEffectiveDistance(), taskIDBWorkerInfo.getNoiseEffectiveDistance(), taskIDAWorkerInfo.getEffectivePrivacyBudget(), taskIDBWorkerInfo.getEffectivePrivacyBudget());
        p_2 = LaplaceProbabilityDensityFunction.probabilityDensityFunction(taskIDANextWorkerInfo.getNoiseEffectiveDistance(), taskIDBNextWorkerInfo.getNoiseEffectiveDistance(), taskIDANextWorkerInfo.getEffectivePrivacyBudget(), taskIDBNextWorkerInfo.getEffectivePrivacyBudget());
        double p_1_2 = p_1 * p_2;
        if (p_1_2 > 0.5) {
            return true;
        }
        if (p_1 + p_2 - p_1_2 < 0.5) {
            return false;
        }
        if (p_2 > 0.5) {
            // tempPCFValue 大于 0.5，说明taskIDA的候选距离小于taskIDB的候选距离的概率更大，说明taskIDB的更应该充当被选择的角色，因此taskIDB占优势
            return true;
        }
        return false;
    }
}
