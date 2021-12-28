package edu.ecnu.dll.dataset.batch.multi_thread;

import edu.ecnu.dll.config.Constant;
import edu.ecnu.dll.dataset.dataset_generating.MainDataSetGenerator;
import tools.basic.StringUtil;
import tools.io.read.PointRead;
import tools.io.read.TwoDimensionDoubleRead;
import tools.struct.Point;

import java.io.File;
import java.util.List;

public class GeneratePrivacyBudgetAndNoiseDistanceWithDifferentWorkerScaleThread2 extends Thread {
    private String basicPath = null;
    private String scaleDir = null;
    private Integer batchArraySize = null;
    private Boolean isLLData = null;

    public GeneratePrivacyBudgetAndNoiseDistanceWithDifferentWorkerScaleThread2(String basicPath, String scaleDir, Integer batchArraySize, Boolean isLLData) {
        this.basicPath = basicPath;
        this.scaleDir = scaleDir;
        this.batchArraySize = batchArraySize;
        this.isLLData = isLLData;
    }

    /**
     * 每个线程完成一个scale
     */
    @Override
    public void run() {
        String innerBasicPath;
        String taskPointInputPath, workerPointInputPath, workerBudgetOutputPath, workerNoiseDistanceOutputPath;
        innerBasicPath = basicPath + scaleDir;
        for (int i = 0; i < this.batchArraySize; i++) {
            taskPointInputPath = innerBasicPath + File.separator + "batch_" + StringUtil.getFixIndexNumberInteger(i+1, Constant.subNamePositionSize) + "_task_point.txt";
            workerPointInputPath = innerBasicPath + File.separator + "batch_" + StringUtil.getFixIndexNumberInteger(i+1, Constant.subNamePositionSize) + "_worker_point.txt";
            workerBudgetOutputPath = innerBasicPath + File.separator + "batch_" + StringUtil.getFixIndexNumberInteger(i+1, Constant.subNamePositionSize) + "_worker_budget.txt";
            workerNoiseDistanceOutputPath = innerBasicPath + File.separator + "batch_" + StringUtil.getFixIndexNumberInteger(i+1, Constant.subNamePositionSize) + "_worker_noise_distance.txt";
//            System.out.println(innerBasicPath);
            List<Point> workerPointList = PointRead.readPointWithFirstLineCount(workerPointInputPath);
            List<Point> taskPointList = PointRead.readPointWithFirstLineCount(taskPointInputPath);
            MainDataSetGenerator.generateWorkerPrivacyBudgetDataSet(workerBudgetOutputPath, workerPointList.size(), taskPointList.size(), Constant.defaultBudgetGroupSize, Constant.defaultPrivacyBudgetBound[0], Constant.defaultPrivacyBudgetBound[1], Constant.precision);
            List<Double[]>[] budgetListArray = TwoDimensionDoubleRead.readDouble(workerBudgetOutputPath, 1);
            MainDataSetGenerator.generateWorkerNoiseDistanceDataSet(workerNoiseDistanceOutputPath, workerPointList, taskPointList, budgetListArray, isLLData);
        }
    }
}
