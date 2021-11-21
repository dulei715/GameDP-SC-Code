package edu.ecnu.dll.basic.basic_struct.pack.experiment_result_info;

public class BasicExperimentResult {
    
    // task总个数
    public Integer totalTaskSize = null;
    
    // worker总个数
    public Integer totalWorkerSize = null;
    
    // 未被服务的task个数
    public Integer totalUnServedTaskSize = null;
    
    // 被分配的worker个数
    public Integer totalAllocatedWorkerSize = null;

    // utility 结果
//    public Double utilityValue = null;
    public Double totalWinnerUtility = null;
    public Double totalFailureUtility = null;
    public Double totalUtility = null;

//    public Integer totalFailureNumber = null;

    // 实际旅行距离
    public Double realTravelDistance = null;

    public BasicExperimentResult(Integer totalTaskSize, Integer totalWorkerSize, Integer totalUnServedTaskSize, Integer totalAllocatedWorkerSize, Double totalWinnerUtility, Double totalFailureUtility, /*Integer totalFailureNumber,*/ Double realTravelDistance) {
        this.totalTaskSize = totalTaskSize;
        this.totalWorkerSize = totalWorkerSize;
        this.totalUnServedTaskSize = totalUnServedTaskSize;
        this.totalAllocatedWorkerSize = totalAllocatedWorkerSize;
        this.totalWinnerUtility = totalWinnerUtility;
        this.totalFailureUtility = totalFailureUtility;
//        this.totalFailureNumber = totalFailureNumber;
        this.realTravelDistance = realTravelDistance;

        this.totalUtility = this.totalWinnerUtility + this.totalFailureUtility;
    }

    public BasicExperimentResult() {
    }

    @Override
    public String toString() {
        return "BasicExperimentResult{" +
                "totalTaskSize=" + totalTaskSize +
                ", totalWorkerSize=" + totalWorkerSize +
                ", totalUnServedTaskSize=" + totalUnServedTaskSize +
                ", totalAllocatedWorkerSize=" + totalAllocatedWorkerSize +
                ", totalWinnerUtility=" + totalWinnerUtility +
                ", totalFailureUtility=" + totalFailureUtility +
                ", totalUtility=" + totalUtility +
                ", realTravelDistance=" + realTravelDistance +
                '}';
    }
}
