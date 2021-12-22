package edu.ecnu.dll.dataset.batch;

import edu.ecnu.dll.dataset.batch.filter.TaskFileNameFilter;
import edu.ecnu.dll.dataset.batch.filter.WorkerFileNameFilter;
import edu.ecnu.dll.dataset.dataset_generating.MainDataSetGenerator;
import edu.ecnu.dll.dataset.preprocess.Preprocess;

import java.io.File;
import java.io.FilenameFilter;

public class BatchPreprocess {

    public static final String[] outputPath = new String[] {
            "task_worker_1_1_0",
            "task_worker_1_1_5",
            "task_worker_1_2_0",
            "task_worker_1_2_5",
            "task_worker_1_3_0"
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
        for (int i = 0; i < outputPath.length; i++) {
            for (int j = 0; j < fileNameArray.length; j++) {
                String inputFilePath = inputPathDir + File.separator + fileNameArray[j];
                String outputFilePath = basicDirPath + File.separator + outputPath[i] + File.separator + fileNameArray[j];
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
        for (int i = 0; i < outputPath.length; i++) {
            for (int j = 0; j < fileNameArray.length; j++) {
                String inputFilePath = inputPathDir + File.separator + fileNameArray[j];
                String outputFilePath = basicDirPath + File.separator + outputPath[i] + File.separator + fileNameArray[j];
                Preprocess.extractRandomPointByGivenSize(inputFilePath, outputFilePath, (int) (basicSize * scale[i]), factorK, constA);
            }
        }
    }

    public static void generatePrivacyBudgetForEachWorkerBatch(String basicDirPath, double budgetLowerBound, double budgetUpperBound) {
        FilenameFilter workerFileNameFilter = new WorkerFileNameFilter();
//        File inputPathDirFile = new File(basicDirPath + File.separator );
        String basicOutputBudgetFileName = "batch_worker_budget.txt";
        int precision = 2;

        for (int i = 0; i < outputPath.length; i++) {
            String inputFileDirPath = basicDirPath + File.separator + outputPath[i];
            File inputFileDirFile = new File(inputFileDirPath);
            File[] workerFileArray = inputFileDirFile.listFiles(workerFileNameFilter);
            int workerSize;
            for (int j = 0; j < workerFileArray.length; j++) {
                String outputBudgetName = workerFileArray[j].getName().replaceAll("point", "budget");
//                System.out.println(outputBudgetName);
//                System.out.println(workerFileArray[j].getParent());
                String outputFilePath = workerFileArray[j].getParent() + File.separator + outputBudgetName;
                MainDataSetGenerator.generateWorkerPrivacyBudgetDataSet(outputFilePath, );
            }
        }
    }


    public static void main(String[] args) {
        double factorK = 0.001;
        double constA = 0;
        String basicDirPath = "E:\\1.学习\\4.数据集\\dataset\\original\\chengdu_total_dataset_km";
//        String basicDirPath = args[0];

//        scaleAndCopyTaskPointToDifferentWorkerScaleParentFile(basicDirPath, factorK, constA);
//        int basicSize = 1000;
//        scaleAndExtractWorkerPointToDifferentWorkerScaleParentFile(basicDirPath, basicSize, factorK, constA);
        generatePrivacyBudgetForEachWorkerBatch(basicDirPath, 0.1, 1);
    }

}
