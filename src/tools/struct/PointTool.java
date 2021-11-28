package tools.struct;

import edu.ecnu.dll.basic.basic_struct.pack.TaskWorkerIDDistancePair;
import edu.ecnu.dll.basic.basic_struct.pack.TaskWorkerIDPair;
import tools.basic.BasicCalculation;
import tools.io.print.MyPrint;
import tools.io.read.PointRead;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class PointTool {

    /**
     * 获取两个集合各个点之间的距离值的统计量（用于距离数据）
     * @param taskPointList
     * @param workerPointList
     * @param interval
     * @return
     */
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

    /**
     * 获取两个集合各个点之间的距离值的统计量（用于经纬度数据）
     * @param taskPointList
     * @param workerPointList
     * @param interval
     * @return
     */
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

    public static TaskWorkerIDDistancePair[] getBoundDistanceFromNormData(List<Point> taskPointList, List<Point> workerPointList, boolean notEqualToZero) {
        Integer minDistanceTaskID = -1, maxDistanceTaskID = -1, minDistanceWorkerID = -1, maxDistanceWorkerID = 1;
        Double minDistance = Double.MAX_VALUE, maxDistance = 0.0;
        Point taskPoint, workerPoint;
        for (int i = 0; i < taskPointList.size(); i++) {
            taskPoint = taskPointList.get(i);
            for (int j = 0; j < workerPointList.size(); j++) {
                workerPoint = workerPointList.get(j);
                Double distance = BasicCalculation.get2Norm(taskPoint.getIndex(), workerPoint.getIndex());

                if (distance > maxDistance) {
                    maxDistance = distance;
                    maxDistanceTaskID = i;
                    maxDistanceWorkerID = j;
                }
                if (notEqualToZero && distance.equals(0.0)) {
                    continue;
                }
                if (distance < minDistance) {
                    minDistance = distance;
                    minDistanceTaskID = i;
                    minDistanceWorkerID = j;
                }
            }
        }
        return new TaskWorkerIDDistancePair[]{new TaskWorkerIDDistancePair(minDistanceTaskID, minDistanceWorkerID, minDistance), new TaskWorkerIDDistancePair(maxDistanceTaskID, maxDistanceWorkerID, maxDistance)};
    }

    public static TaskWorkerIDDistancePair[] getBoundDistanceFromLLData(List<Point> taskPointList, List<Point> workerPointList, boolean notEqualToZero) {
        Integer minDistanceTaskID = -1, maxDistanceTaskID = -1, minDistanceWorkerID = -1, maxDistanceWorkerID = 1;
        Double minDistance = Double.MAX_VALUE, maxDistance = 0.0;
        Point taskPoint, workerPoint;
        for (int i = 0; i < taskPointList.size(); i++) {
            taskPoint = taskPointList.get(i);
            for (int j = 0; j < workerPointList.size(); j++) {
                workerPoint = workerPointList.get(j);
                Double distance = BasicCalculation.getDistanceFrom2LngLat(taskPoint.getyIndex(), taskPoint.getxIndex(), workerPoint.getyIndex(), workerPoint.getxIndex());
                if (distance > maxDistance) {
                    maxDistance = distance;
                    maxDistanceTaskID = i;
                    maxDistanceWorkerID = j;
                }
                if (notEqualToZero && distance.equals(0.0)) {
                    continue;
                }
                if (distance < minDistance) {
                    minDistance = distance;
                    minDistanceTaskID = i;
                    minDistanceWorkerID = j;
                }
            }
        }
        return new TaskWorkerIDDistancePair[]{new TaskWorkerIDDistancePair(minDistanceTaskID, minDistanceWorkerID, minDistance), new TaskWorkerIDDistancePair(maxDistanceTaskID, maxDistanceWorkerID, maxDistance)};
    }

    public static Integer getEqualPointNumberBetweenTwoList(List<Point> taskPointList, List<Point> workerPointList) {
        Point taskPoint, workerPoint;
        int equalNumber = 0;
        for (int i = 0; i < taskPointList.size(); i++) {
            taskPoint = taskPointList.get(i);
            for (int j = 0; j < workerPointList.size(); j++) {
                workerPoint = workerPointList.get(j);
                if (taskPoint.equals(workerPoint)) {
                    ++ equalNumber;
                }
            }
        }
        return equalNumber;
    }

    public static void main(String[] args) {
        String basicPath = "E:\\1.学习\\4.数据集\\dataset\\original\\chengdu_total_dataset_km\\task_worker_1_2_0";
        String taskPath = basicPath + "\\task_point.txt";
        String workerPath = basicPath + "\\worker_point.txt";
        List<Point> taskPointList = PointRead.readPointWithFirstLineCount(taskPath);
        List<Point> workerPointList = PointRead.readPointWithFirstLineCount(workerPath);
        Map<Double, Integer> resultMap = getDistanceStatisticDataFromNormData(taskPointList, workerPointList, 5);
        MyPrint.showMap(resultMap);
    }

    public static void main1(String[] args) {
        String basicPath = "E:\\1.学习\\4.数据集\\dataset\\original\\chengdu_total_dataset_km\\task_worker_1_2_0";
        String taskPath = basicPath + "\\task_point.txt";
        String workerPath = basicPath + "\\worker_point.txt";
        List<Point> taskPointList = PointRead.readPointWithFirstLineCount(taskPath);
        List<Point> workerPointList = PointRead.readPointWithFirstLineCount(workerPath);
        TaskWorkerIDDistancePair[] boundData = getBoundDistanceFromNormData(taskPointList, workerPointList, false);
        TaskWorkerIDPair lowerBound = boundData[0];
        TaskWorkerIDPair upperBound = boundData[1];

        System.out.println(lowerBound);
        System.out.println(upperBound);

    }

    public static void main2(String[] args) {
        String basicPath = "E:\\1.学习\\4.数据集\\dataset\\original\\chengdu_total_dataset_km\\task_worker_1_2_0";
        String taskPath = basicPath + "\\task_point.txt";
        String workerPath = basicPath + "\\worker_point.txt";
        List<Point> taskPointList = PointRead.readPointWithFirstLineCount(taskPath);
        List<Point> workerPointList = PointRead.readPointWithFirstLineCount(workerPath);
        TaskWorkerIDDistancePair[] boundData = getBoundDistanceFromNormData(taskPointList, workerPointList, true);
        TaskWorkerIDPair lowerBound = boundData[0];
        TaskWorkerIDPair upperBound = boundData[1];
        Integer equalSize = getEqualPointNumberBetweenTwoList(taskPointList, workerPointList);

        System.out.println(lowerBound);
        System.out.println(upperBound);
        System.out.println(equalSize);

    }

}
