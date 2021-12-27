package edu.ecnu.dll.dataset.batch.multi_thread;

import edu.ecnu.dll.config.Constant;
import edu.ecnu.dll.dataset.dataset_generating.MainDataSetGenerator;
import tools.basic.StringUtil;
import tools.io.read.PointRead;
import tools.io.read.TwoDimensionDoubleRead;
import tools.struct.Point;

import java.io.File;
import java.util.List;

public class GeneratePrivacyBudgetAndNoiseDistanceWithDifferentBudgetRangeThread3 extends Thread {
    private String basicPath = null;
    private String privacyDir = null;
    private double[] budgetLUBound = null;
//    private Integer batchArraySize = null;
    private Integer startBatchNumber = null;
    private Integer endBatchNumber = null;
    private Boolean isLLData = null;

    public GeneratePrivacyBudgetAndNoiseDistanceWithDifferentBudgetRangeThread3(String basicPath, String privacyDir, double[] budgetLUBound, Integer startBatchNumber, Integer endBatchNumber, Boolean isLLData) {
        this.basicPath = basicPath;
        this.privacyDir = privacyDir;
        this.budgetLUBound = budgetLUBound;
        this.startBatchNumber = startBatchNumber;
        this.endBatchNumber = endBatchNumber;
        this.isLLData = isLLData;
    }

    /**
     * 每个线程完成一个batch的生成
     */
    @Override
    public void run() {
        String innerBasicPath;
        String taskPointInputPath, workerPointInputPath, workerBudgetOutputPath, workerNoiseDistanceOutputPath;
        innerBasicPath = basicPath + File.separator + privacyDir;
        for (int i = startBatchNumber; i <= endBatchNumber; i++) {
            taskPointInputPath = innerBasicPath + File.separator + "batch_" + StringUtil.getFixIndexNumberInteger(i, Constant.subNamePositionSize) + "_task_point.txt";
            workerPointInputPath = innerBasicPath + File.separator + "batch_" + StringUtil.getFixIndexNumberInteger(i, Constant.subNamePositionSize) + "_worker_point.txt";
            workerBudgetOutputPath = innerBasicPath + File.separator + "batch_" + StringUtil.getFixIndexNumberInteger(i, Constant.subNamePositionSize) + "_worker_budget.txt";
            workerNoiseDistanceOutputPath = innerBasicPath + File.separator + "batch_" + StringUtil.getFixIndexNumberInteger(i, Constant.subNamePositionSize) + "_worker_noise_distance.txt";
            List<Point> workerPointList = PointRead.readPointWithFirstLineCount(workerPointInputPath);
            List<Point> taskPointList = PointRead.readPointWithFirstLineCount(taskPointInputPath);
            MainDataSetGenerator.generateWorkerPrivacyBudgetDataSet(workerBudgetOutputPath, workerPointList.size(), taskPointList.size(), Constant.defaultBudgetGroupSize, budgetLUBound[0], budgetLUBound[1], Constant.precision);
            List<Double[]>[] budgetListArray = TwoDimensionDoubleRead.readDouble(workerBudgetOutputPath, 1);
            MainDataSetGenerator.generateWorkerNonNegativeNoiseDistanceDataSet(workerNoiseDistanceOutputPath, workerPointList, taskPointList, budgetListArray, isLLData);
        }
    }
}
