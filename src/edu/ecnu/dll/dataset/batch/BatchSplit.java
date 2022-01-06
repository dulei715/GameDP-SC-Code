package edu.ecnu.dll.dataset.batch;

import edu.ecnu.dll.config.Constant;
import tools.basic.BasicArray;
import tools.io.read.BasicRead;
import tools.io.write.BasicWrite;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class BatchSplit {

//    private static final int cacheSize = 1000;
    private static final int maxLineByteSize = 50;

    private static final String tagName = "batch";

    public int splitToSubFileWithFirstLineSize(String inputFilePath, String outputParentFilePath, String outputBasicName, int batchSize, int batchGroupSize) {
        int index = outputBasicName.lastIndexOf(".");
        String outputName = outputBasicName.substring(0, index);
        String outputFileType = outputBasicName.substring(index);
//        System.out.println(outputName);
//        System.out.println(outputFileType);
        BasicRead basicRead = new BasicRead();
        BasicWrite basicWrite = new BasicWrite();

        basicRead.startReading(inputFilePath);
        int size = Integer.valueOf(basicRead.readOneLine());
        basicRead.mark(size * maxLineByteSize + 1);
        int singleBatchGroupSize = (int)Math.ceil(size * 1.0 / batchSize);
        if (batchGroupSize <= 0) {
            batchGroupSize = singleBatchGroupSize;
        }
        String lineStr;
        List<String> stringList = new ArrayList<>();

//        int count = 0;

        for (int i = 0, k = 0; i < batchGroupSize; i++) {
            if (i % singleBatchGroupSize == 0) {
                basicRead.reset();
                k = 0;
            }
            stringList.clear();
            for (int j = 0; j < batchSize && k < size; j++, k++) {
                lineStr = basicRead.readOneLine();
//                if (lineStr == null) {
//                    break;
//                }
                stringList.add(lineStr);
            }
            String outputPath = outputParentFilePath + File.separator + tagName + "_" + String.format("%0"+ Constant.subNamePositionSize+"d", i+1) + "_" + outputName + outputFileType;
            basicWrite.startWriting(outputPath);
            basicWrite.writeSizeAndCollectionDataWithNewLineSplit(stringList);
            basicWrite.endWriting();
//            System.out.println(outputPath);
//            MyPrint.showList(stringList, "\r\n");
        }

        return batchGroupSize;

    }

    public void randomExtractSubFileWithFirstLineSize(String inputFilePath, String outputParentFilePath, String outputBasicName, int subFileSize, int subFileBatchNumber) {
        int index = outputBasicName.lastIndexOf(".");
        String outputName = outputBasicName.substring(0, index);
        String outputFileType = outputBasicName.substring(index);
        BasicRead basicRead = new BasicRead();
        BasicWrite basicWrite = new BasicWrite();

        basicRead.startReading(inputFilePath);
        List<String> stringList = basicRead.readAll();
        int size = stringList.size();

        Set<Integer> indexSet;


        for (int i = 0; i < subFileBatchNumber; i++) {

            indexSet = BasicArray.generateRandomSet(0, size, subFileSize, null);
            String outputPath = outputParentFilePath + File.separator + tagName + "_" + String.format("%0"+ Constant.subNamePositionSize+"d", i+1) + "_" + outputName + outputFileType;
            basicWrite.startWriting(outputPath);
            basicWrite.writeSizeAndCollectionDataWithNewLineSplit(stringList, indexSet);
            basicWrite.endWriting();
        }


    }

    public static void main0(String[] args) {
        BatchSplit batchSplit = new BatchSplit();
        String inputFilePath = "E:\\1.学习\\4.数据集\\dataset\\original\\chengdu_total_dataset_km\\total_dataset";
        String inputTaskPointPath = inputFilePath + File.separator + "task_point.txt";
        String inputWorkerPointPath = inputFilePath + File.separator + "worker_point.txt";

        String outputParentPath = "E:\\1.学习\\4.数据集\\dataset\\original\\chengdu_total_dataset_km\\batch_dataset";
        String outputBasicTaskName = "task_point.txt";
        String outputBasicWorkerName = "worker_point.txt";
        int taskBatchSize = 1000;
        int workerBatchSize = 3000;
        int initBatchGroupSize = -1;
        int realBatchGroupSize = batchSplit.splitToSubFileWithFirstLineSize(inputTaskPointPath, outputParentPath, outputBasicTaskName, taskBatchSize, initBatchGroupSize);
        batchSplit.splitToSubFileWithFirstLineSize(inputWorkerPointPath, outputParentPath, outputBasicWorkerName, workerBatchSize, realBatchGroupSize);
        System.out.println(realBatchGroupSize);
    }

    public static void main(String[] args) {
        BatchSplit batchSplit = new BatchSplit();
        String inputFilePath = "E:\\1.学习\\4.数据集\\dataset\\original\\chengdu_total_dataset_km\\total_dataset";
        String inputTaskPointPath = inputFilePath + File.separator + "task_point.txt";
        String inputWorkerPointPath = inputFilePath + File.separator + "worker_point.txt";

        String outputParentPath = "E:\\1.学习\\4.数据集\\dataset\\original\\chengdu_total_dataset_km\\batch_dataset";
        String outputBasicTaskName = "task_point.txt";
        String outputBasicWorkerName = "worker_point.txt";
        int taskBatchSize = 1000;
        int workerBatchSize = 3000;
        int initBatchGroupSize = -1;
        int realBatchGroupSize = batchSplit.splitToSubFileWithFirstLineSize(inputTaskPointPath, outputParentPath, outputBasicTaskName, taskBatchSize, initBatchGroupSize);
        batchSplit.randomExtractSubFileWithFirstLineSize(inputWorkerPointPath, outputParentPath, outputBasicWorkerName, workerBatchSize, realBatchGroupSize);

    }



}
