package edu.ecnu.dll.basic.basic_struct.pack.single_agent_info.sub_class;

import edu.ecnu.dll.basic.basic_solution.Solution;

public class WorkerIDNoDistanceUtilityDistancePair extends WorkerIDDistancePair {
    private Double noDistanceUtility = null;

    public WorkerIDNoDistanceUtilityDistancePair() {
    }

    public WorkerIDNoDistanceUtilityDistancePair(Integer workerID, Double distance, Double noDistanceUtility) {
        super(workerID, distance);
        this.noDistanceUtility = noDistanceUtility;
    }

    public Double getNoDistanceUtility() {
        return noDistanceUtility;
    }

    public void setNoDistanceUtility(Double noDistanceUtility) {
        this.noDistanceUtility = noDistanceUtility;
    }

    public Double getUtility() {
        return noDistanceUtility - Solution.transformDistanceToValue(distance);
    }

    @Override
    public String toString() {
        return super.toString() + "; WorkerIDNoDistanceUtilityDistancePair{" +
                "noDistanceUtility=" + noDistanceUtility +
                '}';
    }
}
