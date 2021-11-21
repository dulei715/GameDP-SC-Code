package edu.ecnu.dll.basic.basic_struct.pack.experiment_result_info;

public class NormalExperimentResult extends BasicExperimentResult {

    // 竞争执行时间
    public Long competingTime = null;

    public NormalExperimentResult() {
    }

    public NormalExperimentResult(Integer totalTaskSize, Integer totalWorkerSize, Integer totalUnServedTaskSize, Integer totalAllocatedWorkerSize, Double totalWinnerUtility, Double totalFailureUtility, /*Integer totalFailureNumber,*/ Double realTravelDistance, Long competingTime) {
        super(totalTaskSize, totalWorkerSize, totalUnServedTaskSize, totalAllocatedWorkerSize, totalWinnerUtility, totalFailureUtility, /*totalFailureNumber,*/ realTravelDistance);
        this.competingTime = competingTime;
    }

    public NormalExperimentResult(BasicExperimentResult basicExperimentResult, Long competingTime) {
        super(basicExperimentResult.totalTaskSize, basicExperimentResult.totalWorkerSize, basicExperimentResult.totalUnServedTaskSize, basicExperimentResult.totalAllocatedWorkerSize, basicExperimentResult.totalWinnerUtility, basicExperimentResult.totalFailureUtility, /*basicExperimentResult.totalFailureNumber,*/ basicExperimentResult.realTravelDistance);
        this.competingTime = competingTime;
    }

//    @Override
//    public String toString() {
//        return "NormalExperimentResult{" +
//                "competingTime=" + competingTime +
//                ", totalTaskSize=" + totalTaskSize +
//                ", totalWorkerSize=" + totalWorkerSize +
//                ", totalUnServedTaskSize=" + totalUnServedTaskSize +
//                ", totalAllocatedWorkerSize=" + totalAllocatedWorkerSize +
//                ", totalWinnerUtility=" + totalWinnerUtility +
//                ", totalFailureUtility=" + totalFailureUtility +
//                ", realTravelDistance=" + realTravelDistance +
//                '}';
//    }

    @Override
    public String toString() {
        return  competingTime + "," + totalTaskSize + ","
                + totalWorkerSize + "," + totalUnServedTaskSize + ","
                + totalAllocatedWorkerSize + "," + totalUtility + "," + totalWinnerUtility + ","
                + totalFailureUtility + "," + realTravelDistance;
    }

    public static String getTitleNameString(String split) {
        return "CompetingTime" + split + "TotalTaskSize" + split + "TotalWorkerSize" + split + "TotalUnServedTaskSize" + split
                + "TotalAllocatedWorkerSize" + split + "TotalUtility" + split + "TotalWinnerUtility" + split + "TotalFailureUtility" + split + "RealTravelDistance";
    }


}
