package tools.io.read;

import tools.struct.Point;

import java.util.List;

public class TaskWorkerPointRead {
    public String taskFilePath = null;
    public String workerFilePath = null;
    public List<Point> taskPointList = null;
    public List<Point> workerPointList = null;

    public TaskWorkerPointRead(String taskFilePath, String workerFilePath) {
        this.taskFilePath = taskFilePath;
        this.workerFilePath = workerFilePath;
    }


    public void readTaskPoint() {
        this.taskPointList = PointRead.readPoint(this.taskFilePath);
    }

    public void readWorkerPoint() {
        this.workerPointList = PointRead.readPoint(this.workerFilePath);
    }

}
