package edu.ecnu.dll.scheme.run.target_tools;

import edu.ecnu.dll.basic_struct.pack.single_agent_info.sub_class.WorkerIDDistancePair;

import java.util.HashSet;

public class TargetTool {
    public static Long getRunningTime(Long startTime, Long endTime) {
        return endTime - startTime;
    }

    public static Double getUtilityRatio(Double realUtility, Double bestUtility) {
        return (bestUtility - realUtility) / bestUtility;
    }

    public static Double getFailureRatio(Integer totalFailureTimes, Integer totalTimes) {
        return totalFailureTimes * 1.0 / totalTimes;
    }

    public static Double getFreeTaskRatio(WorkerIDDistancePair... competingResult) {
        int freeCount = 0;
        for (int i = 0; i < competingResult.length; i++) {
            if (competingResult[i] == null || competingResult[i].getWorkerID().equals(-1)) {
                ++ freeCount;
            }
        }
        return freeCount * 1.0 / competingResult.length;
    }

    public static Double getFreeWorkerRatio(Integer workerSize, WorkerIDDistancePair... competingResult) {
        HashSet<Integer> nonFreeWorkerSet = new HashSet<>();
        for (int i = 0; i < competingResult.length; i++) {
            if (competingResult[i] == null || competingResult[i].getWorkerID().equals(-1)) {
                continue;
            }
            nonFreeWorkerSet.add(competingResult[i].getWorkerID());
        }
        return (workerSize - nonFreeWorkerSet.size()) * 1.0 / workerSize;
    }


}
