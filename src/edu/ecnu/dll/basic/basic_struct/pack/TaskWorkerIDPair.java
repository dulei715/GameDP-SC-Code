package edu.ecnu.dll.basic.basic_struct.pack;

public class TaskWorkerIDPair{
    private Integer taskID;
    private Integer workerID;

    public TaskWorkerIDPair() {
    }

    public Integer getTaskID() {
        return taskID;
    }

    public void setTaskID(Integer taskID) {
        this.taskID = taskID;
    }

    public Integer getWorkerID() {
        return workerID;
    }

    public void setWorkerID(Integer workerID) {
        this.workerID = workerID;
    }

    public TaskWorkerIDPair(Integer taskID, Integer workerID) {
        this.taskID = taskID;
        this.workerID = workerID;
    }

    @Override
    public String toString() {
        return "TaskWorkerIDPair{" +
                "taskID=" + taskID +
                ", workerID=" + workerID +
                '}';
    }
}
