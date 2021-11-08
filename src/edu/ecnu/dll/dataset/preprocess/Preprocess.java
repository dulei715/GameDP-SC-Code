package edu.ecnu.dll.dataset.preprocess;

import tools.io.read.OrderRead;
import tools.io.read.PointRead;
import tools.io.read.TaxiRead;
import tools.io.write.PointWrite;
import tools.struct.Order;
import tools.struct.Point;
import tools.struct.Taxi;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Preprocess {
    public static void extractPointToDataset(String inputDataParentPath, String outputParentPath) {
        String nodeFileName = "\\chengdu.node";
        String taxiFileName = "\\chengdu_taxi.txt";
        String orderFileName = "\\chengdu_order.txt";

        String taskPointOutputFileName = "\\task_point.txt";
        String workerPointOutputFileName = "\\worker_point.txt";

        PointWrite pointWrite = new PointWrite();

        List<Point> nodeList = PointRead.readPointWithFirstLineCount(inputDataParentPath + nodeFileName);
        List<Order> orderList = OrderRead.readOrderWithFirstLineCount(inputDataParentPath + orderFileName);
        Set<Point> writingPointSet = new HashSet<>();

        Integer pointID;
        for (Order order : orderList) {
            pointID = order.getBeginPointID();
            writingPointSet.add(nodeList.get(pointID));
        }

        pointWrite.startWriting(outputParentPath + taskPointOutputFileName);
        pointWrite.writePoint(writingPointSet);
        pointWrite.endWriting();

        orderList = null;
        writingPointSet.clear();
        System.gc();

        writingPointSet = new HashSet<>();
        List<Taxi> taxiList = TaxiRead.readTaxi(inputDataParentPath + taxiFileName);
        for (Taxi taxi : taxiList) {
            pointID = taxi.getPointID();
            writingPointSet.add(nodeList.get(pointID));
        }

        pointWrite.startWriting(outputParentPath + workerPointOutputFileName);
        pointWrite.writePoint(writingPointSet);
        pointWrite.endWriting();

    }

    public static void main(String[] args) {
        String inputParentPath = "E:\\1.学习\\4.数据集\\dataset\\original";
        String outputParentPath = "E:\\1.学习\\4.数据集\\dataset\\original\\chengdu";
        extractPointToDataset(inputParentPath, outputParentPath);
    }

}
