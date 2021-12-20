package edu.ecnu.dll.dataset.batch;

import edu.ecnu.dll.dataset.batch.filter.TaskFileNameFilter;
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
                Preprocess.multipleDataWithFirstLineUnchanged(inputPathDir + File.separator + fileNameArray[j], basicDirPath + File.separator + outputPath[i] + File.separator + fileNameArray[j], factorK, constA, " ");
//                System.out.println(inputPathDir + File.separator + fileNameArray[j]);
//                System.out.println(basicDirPath + File.separator + outputPath[i] + File.separator + fileNameArray[j]);
            }
        }
//        MyPrint.showArray(fileNameArray);
    }

    public static void main(String[] args) {
        double factorK = 0.001;
        double constA = 0;
        String basicDirPath = "E:\\1.学习\\4.数据集\\dataset\\original\\chengdu_total_dataset_km";
        scaleAndCopyTaskPointToDifferentWorkerScaleParentFile(basicDirPath, factorK, constA);
    }

}
