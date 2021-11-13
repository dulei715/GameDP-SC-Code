package edu.ecnu.dll.basic_struct.agent;

import java.util.List;

public abstract class Worker extends Agent {
    public double[] location = null;
    public Double maxRange = null;

    // 记录范围内的task在各个List中的位置，不在范围内的为-1
    public Integer[] taskIndex = null;
    // 记录每个位置对应的taskID
    public List<Integer> reverseIndex = null;

    // 记录当前是否已经竞争到了task。只用于多竞争情况。
    public Integer currentWinningState = null;

    public double[] getLocation() {
        return location;
    }

    public void setLocation(double[] location) {
        this.location = location;
    }

    public Double getMaxRange() {
        return maxRange;
    }

    public void setMaxRange(Double maxRange) {
        this.maxRange = maxRange;
    }

    public Worker() {
    }

    public Worker(double[] location) {
        this.location = location;
    }

    public abstract Double getToTaskDistance(Integer taskID);

    public Integer getCurrentWinningState() {
        return currentWinningState;
    }

    public void setCurrentWinningState(Integer currentWinningState) {
        this.currentWinningState = currentWinningState;
    }


    public abstract Double getFinalUtility(Integer taskID);

    public abstract Integer getTaskCompetingTimes(Integer taskID);

}
