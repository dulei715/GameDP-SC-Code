package edu.ecnu.dll.dataset.batch.multi_thread;

import edu.ecnu.dll.config.Constant;
import edu.ecnu.dll.dataset.dataset_generating.MainDataSetGenerator;
import tools.io.read.PointRead;
import tools.io.read.TwoDimensionDoubleRead;
import tools.struct.Point;

import java.io.File;
import java.util.List;

@Deprecated
public class GeneratePrivacyBudgetAndNoiseDistanceWithDifferentBudgetRangeThread extends Thread {
    private String basicPath = null;
    private String batchNumberStr = null;
    private Boolean isLLData = null;
    private Boolean onlyPositiveNoiseDistance;

    public GeneratePrivacyBudgetAndNoiseDistanceWithDifferentBudgetRangeThread(String basicPath, String batchNumberStr, Boolean isLLData, Boolean onlyPositiveNoiseDistance) {
        this.basicPath = basicPath;
        this.batchNumberStr = batchNumberStr;
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
        for (int i = 0; i < Constant.parentBudgetRange.length; i++) {
            innerBasicPath = basicPath + File.separator + Constant.innerParentBudgetPathArray[i];
            taskPointInputPath = innerBasicPath + File.separator + "batch_" + batchNumberStr + "_task_point.txt";
            workerPointInputPath = innerBasicPath + File.separator + "batch_" + batchNumberStr + "_worker_point.txt";
            workerBudgetOutputPath = innerBasicPath + File.separator + "batch_" + batchNumberStr + "_worker_budget.txt";
            workerNoiseDistanceOutputPath = innerBasicPath + File.separator + "batch_" + batchNumberStr + "_worker_noise_distance.txt";
            List<Point> workerPointList = PointRead.readPointWithFirstLineCount(workerPointInputPath);
            List<Point> taskPointList = PointRead.readPointWithFirstLineCount(taskPointInputPath);
            MainDataSetGenerator.generateWorkerPrivacyBudgetDataSet(workerBudgetOutputPath, workerPointList.size(), taskPointList.size(), Constant.defaultBudgetGroupSize, Constant.parentBudgetRange[i][0], Constant.parentBudgetRange[i][1], Constant.precision);
            List<Double[]>[] budgetListArray = TwoDimensionDoubleRead.readDouble(workerBudgetOutputPath, 1);
            MainDataSetGenerator.generateWorkerNoiseDistanceDataSet(workerNoiseDistanceOutputPath, workerPointList, taskPointList, budgetListArray, isLLData, onlyPositiveNoiseDistance);
        }
    }
}
