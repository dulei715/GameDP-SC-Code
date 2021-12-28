package edu.ecnu.dll.dataset.batch.multi_thread.budget_exist;

import edu.ecnu.dll.config.Constant;
import edu.ecnu.dll.dataset.dataset_generating.MainDataSetGenerator;
import tools.basic.StringUtil;
import tools.io.read.PointRead;
import tools.io.read.TwoDimensionDoubleRead;
import tools.struct.Point;

import java.io.File;
import java.util.List;

public class GenerateNoiseDistanceWithDifferentBudgetRangeThread3 extends Thread {
    private String basicPath = null;
    private String privacyDir = null;
    private double[] budgetLUBound = null;
//    private Integer batchArraySize = null;
    private Integer startBatchNumber = null;
    private Integer endBatchNumber = null;
    private Boolean isLLData = null;
    private Boolean onlyPositiveNoiseDistance = null;

    public GenerateNoiseDistanceWithDifferentBudgetRangeThread3(String basicPath, String privacyDir, double[] budgetLUBound, Integer startBatchNumber, Integer endBatchNumber, Boolean isLLData, Boolean onlyPositiveNoiseDistance) {
        this.basicPath = basicPath;
        this.privacyDir = privacyDir;
        this.budgetLUBound = budgetLUBound;
        this.startBatchNumber = startBatchNumber;
        this.endBatchNumber = endBatchNumber;
        this.isLLData = isLLData;
        this.onlyPositiveNoiseDistance = onlyPositiveNoiseDistance;
    }

    /**
     * 每个线程完成一个batch的生成
     */
    @Override
    public void run() {
        String innerBasicPath;
        String taskPointInputPath, workerPointInputPath, workerBudgetInputPath, workerNoiseDistanceOutputPath;
        innerBasicPath = basicPath + File.separator + privacyDir;
        for (int i = startBatchNumber; i <= endBatchNumber; i++) {
            taskPointInputPath = innerBasicPath + File.separator + "batch_" + StringUtil.getFixIndexNumberInteger(i, Constant.subNamePositionSize) + "_task_point.txt";
            workerPointInputPath = innerBasicPath + File.separator + "batch_" + StringUtil.getFixIndexNumberInteger(i, Constant.subNamePositionSize) + "_worker_point.txt";
            workerBudgetInputPath = innerBasicPath + File.separator + "batch_" + StringUtil.getFixIndexNumberInteger(i, Constant.subNamePositionSize) + "_worker_budget.txt";
            workerNoiseDistanceOutputPath = innerBasicPath + File.separator + "batch_" + StringUtil.getFixIndexNumberInteger(i, Constant.subNamePositionSize) + "_worker_noise_distance.txt";
            List<Point> workerPointList = PointRead.readPointWithFirstLineCount(workerPointInputPath);
            List<Point> taskPointList = PointRead.readPointWithFirstLineCount(taskPointInputPath);
            List<Double[]>[] budgetListArray = TwoDimensionDoubleRead.readDouble(workerBudgetInputPath, 1);
            MainDataSetGenerator.generateWorkerNoiseDistanceDataSet(workerNoiseDistanceOutputPath, workerPointList, taskPointList, budgetListArray, isLLData, onlyPositiveNoiseDistance);
        }
    }
}
