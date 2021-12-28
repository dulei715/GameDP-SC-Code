package edu.ecnu.dll.dataset.batch.multi_thread;

import edu.ecnu.dll.config.Constant;
import edu.ecnu.dll.dataset.dataset_generating.MainDataSetGenerator;
import tools.basic.StringUtil;
import tools.io.read.PointRead;
import tools.io.read.TwoDimensionDoubleRead;
import tools.struct.Point;

import java.io.File;
import java.util.List;

public class GeneratePrivacyBudgetAndNoiseDistanceWithDifferentBudgetRangeThread2 extends Thread {
    private String basicPath = null;
    private String privacyDir = null;
    private double[] budgetLUBound = null;
    private Integer batchArraySize = null;
    private Boolean isLLData = null;
    private Boolean onlyPositiveNoiseDistance;

    public GeneratePrivacyBudgetAndNoiseDistanceWithDifferentBudgetRangeThread2(String basicPath, String privacyDir, double[] budgetLUBound, Integer batchArraySize, Boolean isLLData, Boolean onlyPositiveNoiseDistance) {
        this.basicPath = basicPath;
        this.privacyDir = privacyDir;
        this.budgetLUBound = budgetLUBound;
        this.batchArraySize = batchArraySize;
        this.isLLData = isLLData;
        this.onlyPositiveNoiseDistance = onlyPositiveNoiseDistance;
    }

    /**
     * 每个线程完成一个batch的生成
     */
    @Override
    public void run() {
        String innerBasicPath;
        String taskPointInputPath, workerPointInputPath, workerBudgetOutputPath, workerNoiseDistanceOutputPath;
        innerBasicPath = basicPath + File.separator + privacyDir;
        for (int i = 0; i < batchArraySize; i++) {
            taskPointInputPath = innerBasicPath + File.separator + "batch_" + StringUtil.getFixIndexNumberInteger(i+1, Constant.subNamePositionSize) + "_task_point.txt";
            workerPointInputPath = innerBasicPath + File.separator + "batch_" + StringUtil.getFixIndexNumberInteger(i+1, Constant.subNamePositionSize) + "_worker_point.txt";
            workerBudgetOutputPath = innerBasicPath + File.separator + "batch_" + StringUtil.getFixIndexNumberInteger(i+1, Constant.subNamePositionSize) + "_worker_budget.txt";
            workerNoiseDistanceOutputPath = innerBasicPath + File.separator + "batch_" + StringUtil.getFixIndexNumberInteger(i+1, Constant.subNamePositionSize) + "_worker_noise_distance.txt";
            List<Point> workerPointList = PointRead.readPointWithFirstLineCount(workerPointInputPath);
            List<Point> taskPointList = PointRead.readPointWithFirstLineCount(taskPointInputPath);
            MainDataSetGenerator.generateWorkerPrivacyBudgetDataSet(workerBudgetOutputPath, workerPointList.size(), taskPointList.size(), Constant.defaultBudgetGroupSize, budgetLUBound[0], budgetLUBound[1], Constant.precision);
            List<Double[]>[] budgetListArray = TwoDimensionDoubleRead.readDouble(workerBudgetOutputPath, 1);
            MainDataSetGenerator.generateWorkerNoiseDistanceDataSet(workerNoiseDistanceOutputPath, workerPointList, taskPointList, budgetListArray, isLLData, onlyPositiveNoiseDistance);
        }
    }
}
