package edu.ecnu.dll.basic.basic_struct.pack.multi_agent_info;

import edu.ecnu.dll.basic.basic_struct.pack.TaskWorkerIDSuccessfulValuationPair;
import edu.ecnu.dll.basic.basic_struct.pack.single_agent_info.sub_class.WorkerTaskUpdateInfo;

public class ResponseWorkerTaskInfo {

    private Double gtUtilityValue = null;
    private WorkerTaskUpdateInfo winnerInfo = null;
    private TaskWorkerIDSuccessfulValuationPair abandonedInfo = null;
    private TaskWorkerIDSuccessfulValuationPair defeatedInfo = null;

    public ResponseWorkerTaskInfo() {
    }

    public ResponseWorkerTaskInfo(Double gtUtilityValue, WorkerTaskUpdateInfo winnerInfo, TaskWorkerIDSuccessfulValuationPair abandonedInfo, TaskWorkerIDSuccessfulValuationPair defeatedInfo) {
        this.gtUtilityValue = gtUtilityValue;
        this.winnerInfo = winnerInfo;
        this.abandonedInfo = abandonedInfo;
        this.defeatedInfo = defeatedInfo;
    }

    public Double getGtUtilityValue() {
        return gtUtilityValue;
    }

    public void setGtUtilityValue(Double gtUtilityValue) {
        this.gtUtilityValue = gtUtilityValue;
    }

    public WorkerTaskUpdateInfo getWinnerInfo() {
        return winnerInfo;
    }

    public void setWinnerInfo(WorkerTaskUpdateInfo winnerInfo) {
        this.winnerInfo = winnerInfo;
    }

    public TaskWorkerIDSuccessfulValuationPair getAbandonedInfo() {
        return abandonedInfo;
    }

    public void setAbandonedInfo(TaskWorkerIDSuccessfulValuationPair abandonedInfo) {
        this.abandonedInfo = abandonedInfo;
    }

    public TaskWorkerIDSuccessfulValuationPair getDefeatedInfo() {
        return defeatedInfo;
    }

    public void setDefeatedInfo(TaskWorkerIDSuccessfulValuationPair defeatedInfo) {
        this.defeatedInfo = defeatedInfo;
    }
}
