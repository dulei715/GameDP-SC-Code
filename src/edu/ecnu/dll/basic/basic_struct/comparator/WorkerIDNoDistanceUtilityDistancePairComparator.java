package edu.ecnu.dll.basic.basic_struct.comparator;

import edu.ecnu.dll.basic.basic_struct.pack.single_agent_info.sub_class.WorkerIDNoDistanceUtilityDistancePair;

import java.util.Comparator;

public class WorkerIDNoDistanceUtilityDistancePairComparator implements Comparator<WorkerIDNoDistanceUtilityDistancePair> {
    @Override
    public int compare(WorkerIDNoDistanceUtilityDistancePair elemA, WorkerIDNoDistanceUtilityDistancePair elemB) {
        if (elemA == null && elemB == null) {
            return 0;
        }
        if (elemA == null){
            return 1;
        } else if (elemB == null) {
            return -1;
        }
        Double noDistanceUtilityA = elemA.getNoDistanceUtility();
        Double noDistanceUtilityB = elemB.getNoDistanceUtility();

        Double elemADistance = elemA.getDistance();
        Double elemBDistance = elemB.getDistance();
        Double utilityA = noDistanceUtilityA - elemADistance;
        Double utilityB = noDistanceUtilityB - elemBDistance;

        // 根据utility逆序排列
        if (utilityA < utilityB) {
            return 1;
        } else if (utilityA > utilityB) {
            return -1;
        }

        // 根据distance升序排列
        if (elemADistance < elemBDistance) {
            return -1;
        } else if (elemADistance > elemBDistance) {
            return 1;
        }

        return elemA.getWorkerID().compareTo(elemB.getWorkerID());

    }
}
