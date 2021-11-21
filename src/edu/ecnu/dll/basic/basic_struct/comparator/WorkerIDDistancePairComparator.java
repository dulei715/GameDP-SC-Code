package edu.ecnu.dll.basic.basic_struct.comparator;

import edu.ecnu.dll.basic.basic_struct.pack.single_agent_info.sub_class.WorkerIDDistancePair;

import java.util.Comparator;

public class WorkerIDDistancePairComparator implements Comparator<WorkerIDDistancePair> {
    @Override
    public int compare(WorkerIDDistancePair elemA, WorkerIDDistancePair elemB) {
        if ((elemA == null || elemA.getWorkerID() == null || elemA.getDistance() == null) && (elemB == null || elemB.getWorkerID() == null || elemB.getDistance() == null )) {
            return 0;
        }
        // 空就是无限大距离
        if (elemA == null || elemA.getWorkerID() == null || elemA.getDistance() == null) {
            return 1;
        }
        if (elemB == null || elemB.getWorkerID() == null || elemB.getDistance() == null) {
            return -1;
        }

        double compareValue = elemA.getDistance() - elemB.getDistance();
        if (compareValue < 0) {
            return -1;
        } else if (compareValue > 0) {
            return 1;
        }

        return elemA.getWorkerID().compareTo(elemB.getWorkerID());
    }
}
