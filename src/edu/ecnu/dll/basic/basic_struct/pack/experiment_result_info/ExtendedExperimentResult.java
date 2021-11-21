package edu.ecnu.dll.basic.basic_struct.pack.experiment_result_info;

public class ExtendedExperimentResult extends NormalExperimentResult {
    public Integer proposalSize = null;
    public Double taskValue = null;
    public Double workerRange = null;

    public ExtendedExperimentResult(NormalExperimentResult normalExperimentResult, Integer proposalSize, Double taskValue, Double workerRange) {
        super(normalExperimentResult.totalTaskSize, normalExperimentResult.totalWorkerSize, normalExperimentResult.totalUnServedTaskSize, normalExperimentResult.totalAllocatedWorkerSize, normalExperimentResult.totalWinnerUtility, normalExperimentResult.totalFailureUtility, normalExperimentResult.realTravelDistance, normalExperimentResult.competingTime);
        this.proposalSize = proposalSize;
        this.taskValue = taskValue;
        this.workerRange = workerRange;
    }

    public ExtendedExperimentResult(Integer totalTaskSize, Integer totalWorkerSize, Integer totalUnServedTaskSize, Integer totalAllocatedWorkerSize, Double totalWinnerUtility, Double totalFailureUtility, Double realTravelDistance, Long competingTime, Integer proposalSize, Double taskValue, Double workerRange) {
        super(totalTaskSize, totalWorkerSize, totalUnServedTaskSize, totalAllocatedWorkerSize, totalWinnerUtility, totalFailureUtility, realTravelDistance, competingTime);
        this.proposalSize = proposalSize;
        this.taskValue = taskValue;
        this.workerRange = workerRange;
    }

    @Override
    public String toString() {
        return super.toString() + "," + proposalSize + "," + taskValue + "," + workerRange;
    }

    public static String getTitleNameString(String split) {
        return NormalExperimentResult.getTitleNameString(split) + split +  "ProposalSize" + split + "TaskValue" + split + "WorkerRange";
    }



}
