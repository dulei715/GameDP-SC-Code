package edu.ecnu.dll.dataset.batch.multi_thread.budget_exist;

import edu.ecnu.dll.config.Constant;
import edu.ecnu.dll.dataset.dataset_generating.MainDataSetGenerator;
import tools.basic.StringUtil;
import tools.io.read.PointRead;
import tools.io.read.TwoDimensionDoubleRead;
import tools.struct.Point;

import java.io.File;
import java.util.List;

public class GenerateNoiseDistanceWithDifferentWorkerScaleThread3 extends Thread {
    private String basicPath = null;
    private String scaleDir = null;
//    private Integer batchArraySize = null;
    private Integer startBatchNumber = null;
    private Integer endBatchNumber = null;
    private Boolean isLLData = null;

    public GenerateNoiseDistanceWithDifferentWorkerScaleThread3(String basicPath, String scaleDir, Integer startBatchNumber, Integer endBatchNumber, Boolean isLLData) {
        this.basicPath = basicPath;
        this.scaleDir = scaleDir;
        this.startBatchNumber = startBatchNumber;
        this.endBatchNumber = endBatchNumber;
        this.isLLData = isLLData;
    }

    /**
     * 每个线程完成一个scale
     */
    @Override
    public void run() {
        String innerBasicPath;
        String taskPointInputPath, workerPointInputPath, workerBudgetInput, workerNoiseDistanceOutputPath;
        innerBasicPath = basicPath + scaleDir;
        for (int i = startBatchNumber; i <= endBatchNumber; i++) {
            taskPointInputPath = innerBasicPath + File.separator + "batch_" + StringUtil.getFixIndexNumberInteger(i, Constant.subNamePositionSize) + "_task_point.txt";
            workerPointInputPath = innerBasicPath + File.separator + "batch_" + StringUtil.getFixIndexNumberInteger(i, Constant.subNamePositionSize) + "_worker_point.txt";
            workerBudgetInput = innerBasicPath + File.separator + "batch_" + StringUtil.getFixIndexNumberInteger(i, Constant.subNamePositionSize) + "_worker_budget.txt";
            workerNoiseDistanceOutputPath = innerBasicPath + File.separator + "batch_" + StringUtil.getFixIndexNumberInteger(i, Constant.subNamePositionSize) + "_worker_noise_distance.txt";
//            System.out.println(innerBasicPath);
            List<Point> workerPointList = PointRead.readPointWithFirstLineCount(workerPointInputPath);
            List<Point> taskPointList = PointRead.readPointWithFirstLineCount(taskPointInputPath);
            List<Double[]>[] budgetListArray = TwoDimensionDoubleRead.readDouble(workerBudgetInput, 1);
            MainDataSetGenerator.generateWorkerNoiseDistanceDataSet(workerNoiseDistanceOutputPath, workerPointList, taskPointList, budgetListArray, isLLData);
        }
    }
}
