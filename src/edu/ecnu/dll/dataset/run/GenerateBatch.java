package edu.ecnu.dll.dataset.run;

import edu.ecnu.dll.dataset.batch.BatchSplit;

import java.io.File;

public class GenerateBatch {
    public static void main(String[] args) {
        BatchSplit batchSplit = new BatchSplit();

//        String basicPath = "E:\\1.学习\\4.数据集\\dataset\\original\\normal_total_dataset_km";
        String basicPath = args[0];

        String inputFilePath = basicPath + File.separator + "total_dataset";
        String inputTaskPointPath = inputFilePath + File.separator + "task_point.txt";
        String inputWorkerPointPath = inputFilePath + File.separator + "worker_point.txt";

        String outputParentPath = basicPath + File.separator + "batch_dataset";
        String outputBasicTaskName = "task_point.txt";
        String outputBasicWorkerName = "worker_point.txt";
        int taskBatchSize = 1000;
        int workerBatchSize = 3000;
        int initBatchGroupSize = -1;
        int realBatchGroupSize = batchSplit.splitToSubFileWithFirstLineSize(inputTaskPointPath, outputParentPath, outputBasicTaskName, taskBatchSize, initBatchGroupSize);
        batchSplit.randomExtractSubFileWithFirstLineSize(inputWorkerPointPath, outputParentPath, outputBasicWorkerName, workerBatchSize, realBatchGroupSize);
    }
}
