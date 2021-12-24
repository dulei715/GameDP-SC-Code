package edu.ecnu.dll.dataset.batch;

import edu.ecnu.dll.config.Constant;
import edu.ecnu.dll.dataset.batch.filter.TaskFileNameFilter;
import edu.ecnu.dll.dataset.batch.filter.WorkerFileNameFilter;
import edu.ecnu.dll.dataset.batch.multi_thread.*;
import edu.ecnu.dll.dataset.dataset_generating.MainDataSetGenerator;
import edu.ecnu.dll.dataset.preprocess.Preprocess;
import tools.io.read.PointRead;
import tools.io.read.TwoDimensionDoubleRead;
import tools.struct.Point;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class BatchPreprocess {

    public static final String[] scaleOutputPath = new String[] {
            "task_worker_1_1_0",
            "task_worker_1_1_5",
            "task_worker_1_2_0",
            "task_worker_1_2_5",
            "task_worker_1_3_0"
    };
    public static final String[] budgetOutputPath = new String[] {
            "privacy_change" + File.separator + "privacy_1",
            "privacy_change" + File.separator + "privacy_2",
            "privacy_change" + File.separator + "privacy_3",
            "privacy_change" + File.separator + "privacy_4",
            "privacy_change" + File.separator + "privacy_5"
    };

    public static final double[] scale = new double[] {
            1.0, 1.5, 2.0, 2.5, 3.0
    };

    /**
     * 根据已经分好的task的batch，将task的所有batch分别拷贝到不同的worker scale文件夹下，并完成单位换算
     * @param basicDirPath
     * @param factorK
     * @param constA
     */
    public static void scaleAndCopyTaskPointToDifferentWorkerScaleParentFile(String basicDirPath, double factorK, double constA) {
        FilenameFilter taskFileNameFilter = new TaskFileNameFilter();
        String inputPathDir = basicDirPath + File.separator + "batch_dataset";
//        String outputBasic = "E:\\1.学习\\4.数据集\\dataset\\original\\chengdu_total_dataset_km\\";
//        String outputBasic = "";

//        double factorK = 0.001;
//        double constA = 0.0;
        File inputPathDirFile = new File(inputPathDir);
        String[] fileNameArray = inputPathDirFile.list(taskFileNameFilter);
        for (int i = 0; i < scaleOutputPath.length; i++) {
            for (int j = 0; j < fileNameArray.length; j++) {
                String inputFilePath = inputPathDir + File.separator + fileNameArray[j];
                String outputFilePath = basicDirPath + File.separator + scaleOutputPath[i] + File.separator + fileNameArray[j];
                Preprocess.multipleDataWithFirstLineUnchanged(inputFilePath, outputFilePath, factorK, constA, " ");
//                System.out.println(inputPathDir + File.separator + fileNameArray[j]);
//                System.out.println(basicDirPath + File.separator + outputPath[i] + File.separator + fileNameArray[j]);
            }
        }
//        MyPrint.showArray(fileNameArray);
    }

    /**
     * 根据已经分好的worker的batch，将worker的所有batch分别抽取不同的比例到不同的worker scale文件夹下，并完成单位换算
     * @param basicDirPath
     * @param basicSize
     * @param factorK
     * @param constA
     */
    public static void scaleAndExtractWorkerPointToDifferentWorkerScaleParentFile(String basicDirPath, int basicSize, double factorK, double constA) {
        FilenameFilter workerFileNameFilter = new WorkerFileNameFilter();
        String inputPathDir = basicDirPath + File.separator + "batch_dataset";
        File inputPathDirFile = new File(inputPathDir);
        String[] fileNameArray = inputPathDirFile.list(workerFileNameFilter);
        for (int i = 0; i < scaleOutputPath.length; i++) {
            for (int j = 0; j < fileNameArray.length; j++) {
                String inputFilePath = inputPathDir + File.separator + fileNameArray[j];
                String outputFilePath = basicDirPath + File.separator + scaleOutputPath[i] + File.separator + fileNameArray[j];
                Preprocess.extractRandomPointByGivenSize(inputFilePath, outputFilePath, (int) (basicSize * scale[i]), factorK, constA);
            }
        }
    }

    public static void generatePrivacyBudgetAndNoiseDistanceForEachWorkerBatch(String basicDirPath, boolean isLLData) {
        FilenameFilter workerFileNameFilter = new WorkerFileNameFilter();
//        File inputPathDirFile = new File(basicDirPath + File.separator );
        String basicOutputBudgetFileName = "batch_worker_budget.txt";

        for (int i = 0; i < scaleOutputPath.length; i++) {
            String inputFileDirPath = basicDirPath + File.separator + scaleOutputPath[i];
            File inputFileDirFile = new File(inputFileDirPath);
            File[] workerFileArray = inputFileDirFile.listFiles(workerFileNameFilter);
//            int workerSize, taskSize;
            List<Point> taskPointList = null, workerPointList = null;
            List<Double[]>[] budgetListArray = null;
            for (int j = 0; j < workerFileArray.length; j++) {
                String outputBudgetName = workerFileArray[j].getName().replaceAll("point", "budget");
                String outputNoiseDistanceName = workerFileArray[j].getName().replaceAll("point", "noise_distance");
//                System.out.println(outputBudgetName);
//                System.out.println(workerFileArray[j].getParent());
                String outputBudgetFilePath = workerFileArray[j].getParent() + File.separator + outputBudgetName;
                String outputNoiseDistanceFilePath = workerFileArray[j].getParent() + File.separator + outputNoiseDistanceName;
                // 读取worker数量
//                workerSize = PointRead.readPointSizeWithFirstLineCount(workerFileArray[j].getAbsolutePath());
                workerPointList = PointRead.readPointWithFirstLineCount(workerFileArray[j].getAbsolutePath());
                // 读取task数量
                String taskPointFileName = workerFileArray[j].getName().replaceAll("worker", "task");
//                taskSize = PointRead.readPointSizeWithFirstLineCount(workerFileArray[j].getParent() + File.separator + taskPointFileName);
                taskPointList = PointRead.readPointWithFirstLineCount(workerFileArray[j].getParent() + File.separator + taskPointFileName);

                MainDataSetGenerator.generateWorkerPrivacyBudgetDataSet(outputBudgetFilePath, workerPointList.size(), taskPointList.size(), Constant.defaultBudgetGroupSize, Constant.defaultPrivacyBudgetBound[0], Constant.defaultPrivacyBudgetBound[1], Constant.precision);
                budgetListArray = TwoDimensionDoubleRead.readDouble(outputBudgetFilePath, 1);
                MainDataSetGenerator.generateWorkerNoiseDistanceDataSet(outputNoiseDistanceFilePath, workerPointList, taskPointList, budgetListArray, isLLData);
//                System.out.println("outputBudgetName: " + outputBudgetName);
//                System.out.println("outputFilePath: " + outputFilePath);
//                System.out.println("workerSize: " + workerSize);
//                System.out.println("taskPointName: " + taskPointFileName);
//                System.out.println("taskSize: " + taskSize);
//                MyPrint.showSplitLine("*", 200);
//                System.exit(1);
            }
        }
    }

    // 多线程
    @Deprecated
    public static void generatePrivacyBudgetAndNoiseDistanceForEachWorkerBatchParallel(String basicDirPath, boolean isLLData) {
        FilenameFilter workerFileNameFilter = new WorkerFileNameFilter();
        File batchDatasetDirFile = new File(basicDirPath + File.separator + "batch_dataset");
        int threadArraySize = batchDatasetDirFile.list(workerFileNameFilter).length;
        Thread[] threadArray = new Thread[threadArraySize];

        for (int i = 0; i < threadArraySize; i++) {

            threadArray[i] = new GeneratePrivacyBudgetAndNoiseDistanceWithDifferentWorkerScaleThread(basicDirPath, String.format("%0"+Constant.subNamePositionSize+"d", i+1), isLLData)    ;
            threadArray[i].start();
        }
    }

    public static void generatePrivacyBudgetAndNoiseDistanceForEachWorkerBatchParallel2(String basicDirPath, boolean isLLData) {
        FilenameFilter workerFileNameFilter = new WorkerFileNameFilter();
        File batchDatasetDirFile = new File(basicDirPath + File.separator + "batch_dataset");
        int batchSize = batchDatasetDirFile.list(workerFileNameFilter).length;
        Thread[] threadArray = new Thread[Constant.parentPathArray.length];

        for (int i = 0; i < Constant.parentPathArray.length; i++) {

            threadArray[i] = new GeneratePrivacyBudgetAndNoiseDistanceWithDifferentWorkerScaleThread2(basicDirPath, Constant.parentPathArray[i], batchSize, isLLData)    ;
            threadArray[i].start();
        }
    }

    //多线程
    @Deprecated
    public static void generatePrivacyBudgetAndNoiseDistanceForEachPrivacyBudgetBatchParallel(String basicDirPath, boolean isLLData) {
        FilenameFilter workerFileNameFilter = new WorkerFileNameFilter();
        File basicDirFile = new File(basicDirPath);
        int threadArraySize = basicDirFile.list(workerFileNameFilter).length;
        Thread[] threadArray = new Thread[threadArraySize];

        for (int i = 0; i < threadArraySize; i++) {
            threadArray[i] = new GeneratePrivacyBudgetAndNoiseDistanceWithDifferentBudgetRangeThread(basicDirPath, String.format("%0"+Constant.subNamePositionSize+"d", i+1), isLLData);
            threadArray[i].start();
        }
    }

    public static void generatePrivacyBudgetAndNoiseDistanceForEachPrivacyBudgetBatchParallel2(String basicDirPath, boolean isLLData) {
        FilenameFilter workerFileNameFilter = new WorkerFileNameFilter();
        File basicDirFile = new File(basicDirPath);
        int batchSize = basicDirFile.list(workerFileNameFilter).length;
        Thread[] threadArray = new Thread[Constant.parentBudgetRange.length];

        for (int i = 0; i < Constant.parentBudgetRange.length; i++) {
            threadArray[i] = new GeneratePrivacyBudgetAndNoiseDistanceWithDifferentBudgetRangeThread2(basicDirPath, Constant.innerParentBudgetPathArray[i], batchSize, isLLData);
            threadArray[i].start();
        }
    }

    public static void generatePrivacyBudgetAndNoiseDistanceForEachPrivacyBudgetBatchParallel3(String basicDirPath, boolean isLLData, Integer[] threadSizePerGroupArray) {
        FilenameFilter workerFileNameFilter = new WorkerFileNameFilter();
        File basicDirFile = new File(basicDirPath + File.separator + "batch_dataset");
        int batchSize = basicDirFile.list(workerFileNameFilter).length;
//        int batchShareSize = (int)Math.ceil(batchSize*1.0 / threadSizePerGroup);
        int[] batchShareSizeArray = new int[Constant.parentBudgetRange.length];
        int threadSize = 0;
        for (int i = 0; i < batchShareSizeArray.length; i++) {
            if (threadSizePerGroupArray[i] == null || threadSizePerGroupArray[i] <= 0) {
                batchShareSizeArray[i] = -1;
            } else {
                batchShareSizeArray[i] = (int)Math.ceil(batchSize*1.0 / threadSizePerGroupArray[i]);
                threadSize += threadSizePerGroupArray[i];
            }
        }

        Thread[] threadArray = new Thread[threadSize];

        for (int i = 0, k = 0; i < Constant.parentBudgetRange.length; i++) {
            if (batchShareSizeArray[i] == -1) {
                continue;
            }
            for (int j = 0; j < threadSizePerGroupArray[i]; j++) {
                int startBatchID = 1 + batchShareSizeArray[i]*j;
                int endBatchID = batchShareSizeArray[i]*(j+1);
                if (endBatchID > batchSize) {
                    endBatchID = batchSize;
                }
                threadArray[k] = new GeneratePrivacyBudgetAndNoiseDistanceWithDifferentWorkerScaleThread3(basicDirPath, Constant.parentPathArray[i], startBatchID, endBatchID, isLLData);
                threadArray[k].start();
                ++k;
            }
        }
    }

    public static void generatePrivacyBudgetAndNoiseDistanceForEachPrivacyBudget(String basicDirPath, boolean isLLData) {
        FilenameFilter workerFileNameFilter = new WorkerFileNameFilter();
//        File inputPathDirFile = new File(basicDirPath + File.separator );
        String basicOutputBudgetFileName = "batch_worker_budget.txt";

        for (int i = 0; i < budgetOutputPath.length; i++) {
            String inputFileDirPath = basicDirPath + File.separator + budgetOutputPath[i];
            File inputFileDirFile = new File(inputFileDirPath);
            File[] workerFileArray = inputFileDirFile.listFiles(workerFileNameFilter);
//            int workerSize, taskSize;
            List<Point> taskPointList = null, workerPointList = null;
            List<Double[]>[] budgetListArray = null;
            for (int j = 0; j < workerFileArray.length; j++) {
                String outputBudgetName = workerFileArray[j].getName().replaceAll("point", "budget");
                String outputNoiseDistanceName = workerFileArray[j].getName().replaceAll("point", "noise_distance");
//                System.out.println(outputBudgetName);
//                System.out.println(workerFileArray[j].getParent());
                String outputBudgetFilePath = workerFileArray[j].getParent() + File.separator + outputBudgetName;
                String outputNoiseDistanceFilePath = workerFileArray[j].getParent() + File.separator + outputNoiseDistanceName;
                // 读取worker数量
//                workerSize = PointRead.readPointSizeWithFirstLineCount(workerFileArray[j].getAbsolutePath());
                workerPointList = PointRead.readPointWithFirstLineCount(workerFileArray[j].getAbsolutePath());
                // 读取task数量
                String taskPointFileName = workerFileArray[j].getName().replaceAll("worker", "task");
//                taskSize = PointRead.readPointSizeWithFirstLineCount(workerFileArray[j].getParent() + File.separator + taskPointFileName);
                taskPointList = PointRead.readPointWithFirstLineCount(workerFileArray[j].getParent() + File.separator + taskPointFileName);

//                MainDataSetGenerator.generateWorkerPrivacyBudgetDataSet(outputBudgetFilePath, workerPointList.size(), taskPointList.size(), Constant.defaultBudgetGroupSize, Constant.defaultPrivacyBudgetBound[0], Constant.defaultPrivacyBudgetBound[1], Constant.precision);
                MainDataSetGenerator.generateWorkerPrivacyBudgetDataSet(outputBudgetFilePath, workerPointList.size(), taskPointList.size(), Constant.defaultBudgetGroupSize, Constant.parentBudgetRange[i][0], Constant.parentBudgetRange[i][1], Constant.precision);
                budgetListArray = TwoDimensionDoubleRead.readDouble(outputBudgetFilePath, 1);
                MainDataSetGenerator.generateWorkerNoiseDistanceDataSet(outputNoiseDistanceFilePath, workerPointList, taskPointList, budgetListArray, isLLData);
//                System.out.println("outputBudgetName: " + outputBudgetName);
//                System.out.println("outputFilePath: " + outputFilePath);
//                System.out.println("workerSize: " + workerSize);
//                System.out.println("taskPointName: " + taskPointFileName);
//                System.out.println("taskSize: " + taskSize);
//                MyPrint.showSplitLine("*", 200);
//                System.exit(1);
            }
        }
    }

    public static void copyTaskAndWorkerPointToDifferentPrivacyBudgetParentFile(String basicDirPath) throws IOException {
        FilenameFilter taskFileNameFilter = new TaskFileNameFilter();
        FilenameFilter workerFileNameFilter = new WorkerFileNameFilter();
        File inputPathDirFile = new File(basicDirPath);
        File[] taskFileArray = inputPathDirFile.listFiles(taskFileNameFilter);
        File[] workerFileArray = inputPathDirFile.listFiles(workerFileNameFilter);
        int len = taskFileArray.length;

        for (int i = 0; i < budgetOutputPath.length; i++) {
            File dirFile = new File(basicDirPath + File.separator + budgetOutputPath[i]);
            if (!dirFile.exists()) {
                dirFile.mkdirs();
            }
        }


        for (int j = 0; j < len; j++) {
            Path inputTaskFilePath = Paths.get(taskFileArray[j].getParent(), taskFileArray[j].getName());
            Path inputWorkerFilePath = Paths.get(workerFileArray[j].getParent(), workerFileArray[j].getName());
            for (int i = 0; i < budgetOutputPath.length; i++) {
                Path outputTaskFilePath = Paths.get(taskFileArray[j].getParent() + File.separator + budgetOutputPath[i], taskFileArray[j].getName());
                Path outputWorkerFilePath = Paths.get(workerFileArray[j].getParent() + File.separator + budgetOutputPath[i], workerFileArray[j].getName());
                Files.copy(inputTaskFilePath, outputTaskFilePath);
                Files.copy(inputWorkerFilePath, outputWorkerFilePath);
            }
        }
    }


    public static void main0(String[] args) {
        double factorK = 0.001;
        double constA = 0;
        String basicDirPath = "E:\\1.学习\\4.数据集\\dataset\\original\\chengdu_total_dataset_km";
        Boolean isLLData = false;
//        String basicDirPath = args[0];
//        Boolean isLLData = Boolean.valueOf(args[1]);

        System.out.println(basicDirPath);
        System.out.println(isLLData);

//        scaleAndCopyTaskPointToDifferentWorkerScaleParentFile(basicDirPath, factorK, constA);
//        int basicSize = 1000;
//        scaleAndExtractWorkerPointToDifferentWorkerScaleParentFile(basicDirPath, basicSize, factorK, constA);
        generatePrivacyBudgetAndNoiseDistanceForEachWorkerBatchParallel(basicDirPath, isLLData);
    }

    public static void main2(String[] args) {
        double factorK = 1;
        double constA = 0;
//        String basicDirPath = "E:\\1.学习\\4.数据集\\dataset\\original\\chengdu_total_dataset_km";
//        Boolean isLLData = false;
        String basicDirPath = args[0];
        Boolean isLLData = Boolean.valueOf(args[1]);

        System.out.println(basicDirPath);
        System.out.println(isLLData);

        scaleAndCopyTaskPointToDifferentWorkerScaleParentFile(basicDirPath, factorK, constA);
        int basicSize = 1000;
        scaleAndExtractWorkerPointToDifferentWorkerScaleParentFile(basicDirPath, basicSize, factorK, constA);
//        generatePrivacyBudgetAndNoiseDistanceForEachWorkerBatch(basicDirPath, isLLData);
        generatePrivacyBudgetAndNoiseDistanceForEachWorkerBatchParallel2(basicDirPath, isLLData);
    }

    //todo: 用于处理privacy budget变化的部分
    public static void main1(String[] args) throws IOException {
//        String basicSourcePath = "E:\\1.学习\\4.数据集\\dataset\\original\\chengdu_total_dataset_km\\task_worker_1_2_0";
        String basicSourcePath = args[0];
//        Boolean isLLData = false;
        Boolean isLLData = Boolean.valueOf(args[1]);
        copyTaskAndWorkerPointToDifferentPrivacyBudgetParentFile(basicSourcePath);
//        generatePrivacyBudgetAndNoiseDistanceForEachPrivacyBudgetBatchParallel(basicSourcePath, isLLData);
//        generatePrivacyBudgetAndNoiseDistanceForEachPrivacyBudget(basicSourcePath, isLLData);
        generatePrivacyBudgetAndNoiseDistanceForEachPrivacyBudgetBatchParallel2(basicSourcePath, isLLData);

    }

    //todo: 用于真实数据集后三组的生成，设置线程数为每组4个
    public static void main3(String[] args) {
        double factorK = 0.001;
        double constA = 0;
//        String basicDirPath = "E:\\1.学习\\4.数据集\\dataset\\original\\chengdu_total_dataset_km";
//        Boolean isLLData = false;
        String basicDirPath = args[0];
        Boolean isLLData = Boolean.valueOf(args[1]);

        System.out.println(basicDirPath);
        System.out.println(isLLData);

//        scaleAndCopyTaskPointToDifferentWorkerScaleParentFile(basicDirPath, factorK, constA);
//        int basicSize = 1000;
//        scaleAndExtractWorkerPointToDifferentWorkerScaleParentFile(basicDirPath, basicSize, factorK, constA);
        Integer[] threadSizePerGroupArray = new Integer[]{
                0, 0, 3, 4, 4
        };
        generatePrivacyBudgetAndNoiseDistanceForEachPrivacyBudgetBatchParallel3(basicDirPath, isLLData, threadSizePerGroupArray);
    }

    // 用于normal数据集生成
    public static void main(String[] args) {
        double factorK = 1;
        double constA = 0;
//        String basicDirPath = "E:\\1.学习\\4.数据集\\dataset\\original\\chengdu_total_dataset_km";
//        Boolean isLLData = false;
        String basicDirPath = args[0];
        Boolean isLLData = Boolean.valueOf(args[1]);

        System.out.println(basicDirPath);
        System.out.println(isLLData);

        scaleAndCopyTaskPointToDifferentWorkerScaleParentFile(basicDirPath, factorK, constA);
        int basicSize = 1000;
        scaleAndExtractWorkerPointToDifferentWorkerScaleParentFile(basicDirPath, basicSize, factorK, constA);
        Integer[] threadSizePerGroupArray = new Integer[]{
                1, 2, 2, 3, 3
        };
        generatePrivacyBudgetAndNoiseDistanceForEachPrivacyBudgetBatchParallel3(basicDirPath, isLLData, threadSizePerGroupArray);
    }

}
