package edu.ecnu.dll.scheme.solution._3_multiple_task.struct;

import edu.ecnu.dll.basic_struct.pack.WorkerIDDistanceBudgetPair;
import edu.ecnu.dll.scheme_compared.solution.ConflictElimination;

public class EnhanceConflictElimination extends ConflictElimination {
    public EnhanceConflictElimination(Integer taskSize, Integer workerSize) {
        super(taskSize, workerSize);
    }

    @Override
    protected boolean compareFourValues(Integer workerID, Integer taskIDA, Integer taskIDB, WorkerIDDistanceBudgetPair taskIDANextWorkerInfo, WorkerIDDistanceBudgetPair taskIDBNextWorkerInfo) {
        return super.compareFourValues(workerID, taskIDA, taskIDB, taskIDANextWorkerInfo, taskIDBNextWorkerInfo);
    }
}
