package tools.struct;

import tools.basic.BasicCalculation;
import tools.io.print.MyPrint;
import tools.io.read.PointRead;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class PointTool {
    public static Map<Double, Integer> getDistanceStatisticDataFromNormData(List<Point> taskPointList, List<Point> workerPointList, double interval) {
        TreeMap<Double, Integer> countResultMap = new TreeMap<>();
        Point taskPoint, workerPoint;
        for (int i = 0; i < taskPointList.size(); i++) {
            taskPoint = taskPointList.get(i);
            for (int j = 0; j < workerPointList.size(); j++) {
                workerPoint = workerPointList.get(j);
                Double distance = BasicCalculation.get2Norm(taskPoint.getIndex(), workerPoint.getIndex());
                Double ceilDistance = Math.ceil(distance / interval);
                Double keyDistance = ceilDistance * interval;
                if (!countResultMap.containsKey(keyDistance)) {
                    countResultMap.put(keyDistance, 1);
                } else {
                    Integer value = countResultMap.get(keyDistance);
                    countResultMap.put(keyDistance, value + 1);
                }
            }
        }
        return countResultMap;
    }

    public static Map<Double, Integer> getDistanceStatisticDataFromLLData(List<Point> taskPointList, List<Point> workerPointList, double interval) {
        TreeMap<Double, Integer> countResultMap = new TreeMap<>();
        Point taskPoint, workerPoint;
        for (int i = 0; i < taskPointList.size(); i++) {
            taskPoint = taskPointList.get(i);
            for (int j = 0; j < workerPointList.size(); j++) {
                workerPoint = workerPointList.get(j);
//                Double distance = BasicCalculation.get2Norm(taskPoint.getIndex(), workerPoint.getIndex());
                Double distance = BasicCalculation.getDistanceFrom2LngLat(taskPoint.getyIndex(), taskPoint.getxIndex(), workerPoint.getyIndex(), workerPoint.getxIndex());
                Double ceilDistance = Math.ceil(distance / interval);
                Double keyDistance = ceilDistance * interval;
                if (!countResultMap.containsKey(keyDistance)) {
                    countResultMap.put(keyDistance, 1);
                } else {
                    Integer value = countResultMap.get(keyDistance);
                    countResultMap.put(keyDistance, value + 1);
                }
            }
        }
        return countResultMap;
    }

    public static void main(String[] args) {
        String basicPath = "E:\\1.学习\\4.数据集\\1.FourSquare-NYCandTokyoCheck-ins\\output\\TKY";
        String taskPath = basicPath + "\\task_point.txt";
        String workerPath = basicPath + "\\worker_point.txt";
        List<Point> taskPointList = PointRead.readPointWithFirstLineCount(taskPath);
        List<Point> workerPointList = PointRead.readPointWithFirstLineCount(workerPath);
        Map<Double, Integer> resultMap = getDistanceStatisticDataFromLLData(taskPointList, workerPointList, 0.1);
        MyPrint.showMap(resultMap);
    }
}