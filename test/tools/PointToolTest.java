package tools;

import edu.ecnu.dll.basic.basic_struct.pack.TaskWorkerIDDistancePair;
import edu.ecnu.dll.basic.basic_struct.pack.TaskWorkerIDPair;
import org.junit.Test;
import tools.io.print.MyPrint;
import tools.io.read.PointRead;
import tools.struct.Point;
import tools.struct.PointTool;

import java.util.List;
import java.util.Map;

public class PointToolTest {
    @Test
    public void fun1() {
        String basicPath = "E:\\1.学习\\4.数据集\\dataset\\original\\chengdu_total_dataset_km\\task_worker_1_2_0";
        String taskPath = basicPath + "\\task_point.txt";
        String workerPath = basicPath + "\\worker_point.txt";
        List<Point> taskPointList = PointRead.readPointWithFirstLineCount(taskPath);
        List<Point> workerPointList = PointRead.readPointWithFirstLineCount(workerPath);
        TaskWorkerIDDistancePair[] boundData = PointTool.getBoundDistanceFromNormData(taskPointList, workerPointList, true);
        TaskWorkerIDPair lowerBound = boundData[0];
        TaskWorkerIDPair upperBound = boundData[1];
        Integer equalSize = PointTool.getEqualPointNumberBetweenTwoList(taskPointList, workerPointList);

        System.out.println(lowerBound);
        System.out.println(upperBound);
        System.out.println(equalSize);
    }

    @Test
    public void fun2() {
        String basicPath = "E:\\1.学习\\4.数据集\\dataset\\original\\nyc_total_dataset_ll\\task_worker_1_2_0";
        String taskPath = basicPath + "\\task_point.txt";
        String workerPath = basicPath + "\\worker_point.txt";
        List<Point> taskPointList = PointRead.readPointWithFirstLineCount(taskPath);
        List<Point> workerPointList = PointRead.readPointWithFirstLineCount(workerPath);
        TaskWorkerIDDistancePair[] boundData = PointTool.getBoundDistanceFromLLData(taskPointList, workerPointList, true);
        TaskWorkerIDPair lowerBound = boundData[0];
        TaskWorkerIDPair upperBound = boundData[1];
        Integer equalSize = PointTool.getEqualPointNumberBetweenTwoList(taskPointList, workerPointList);

        System.out.println(lowerBound);
        System.out.println(upperBound);
        System.out.println(equalSize);
    }

    @Test
    public void fun2_2() {
        String basicPath = "E:\\1.学习\\4.数据集\\dataset\\original\\nyc_total_dataset_ll\\task_worker_1_2_0";
        String taskPath = basicPath + "\\task_point.txt";
        String workerPath = basicPath + "\\worker_point.txt";
        List<Point> taskPointList = PointRead.readPointWithFirstLineCount(taskPath);
        List<Point> workerPointList = PointRead.readPointWithFirstLineCount(workerPath);
        Map<Double, Integer> resultMap = PointTool.getDistanceStatisticDataFromLLData(taskPointList, workerPointList, 5);
        MyPrint.showMap(resultMap);
    }

    @Test
    public void fun3() {
        String basicPath = "E:\\1.学习\\4.数据集\\dataset\\original\\tky_total_dataset_ll\\task_worker_1_2_0";
        String taskPath = basicPath + "\\task_point.txt";
        String workerPath = basicPath + "\\worker_point.txt";
        List<Point> taskPointList = PointRead.readPointWithFirstLineCount(taskPath);
        List<Point> workerPointList = PointRead.readPointWithFirstLineCount(workerPath);
        TaskWorkerIDDistancePair[] boundData = PointTool.getBoundDistanceFromLLData(taskPointList, workerPointList, true);
        TaskWorkerIDPair lowerBound = boundData[0];
        TaskWorkerIDPair upperBound = boundData[1];
        Integer equalSize = PointTool.getEqualPointNumberBetweenTwoList(taskPointList, workerPointList);

        System.out.println(lowerBound);
        System.out.println(upperBound);
        System.out.println(equalSize);
    }
}
