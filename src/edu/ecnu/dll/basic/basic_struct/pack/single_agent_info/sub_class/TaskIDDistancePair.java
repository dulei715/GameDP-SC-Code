package edu.ecnu.dll.basic.basic_struct.pack.single_agent_info.sub_class;

import edu.ecnu.dll.basic.basic_struct.pack.single_agent_info.SingleInfoPack;

public class TaskIDDistancePair extends SingleInfoPack {
    private Integer taskID = null;
    private Double distance = null;

    public TaskIDDistancePair() {
    }

    public TaskIDDistancePair(Integer taskID, Double distance) {
        this.taskID = taskID;
        this.distance = distance;
    }

    public Integer getTaskID() {
        return taskID;
    }

    public void setTaskID(Integer taskID) {
        this.taskID = taskID;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    @Override
    public String toString() {
        return "TaskIDDistancePair{" +
                "taskID=" + taskID +
                ", distance=" + distance +
                '}';
    }
}
