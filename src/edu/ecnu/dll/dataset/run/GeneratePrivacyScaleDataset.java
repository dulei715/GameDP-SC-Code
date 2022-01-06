package edu.ecnu.dll.dataset.run;

import edu.ecnu.dll.dataset.batch.BatchPreprocess;

import java.io.File;
import java.io.IOException;

public class GeneratePrivacyScaleDataset {
    public static void main(String[] args) throws IOException {
//        String basicSourcePath = "E:\\1.学习\\4.数据集\\dataset\\original\\chengdu_total_dataset_km\\task_worker_1_2_0";
        String basicSourcePath = args[0];
//        Boolean isLLData = false;
        Boolean isLLData = Boolean.valueOf(args[1]);
        Boolean onlyPositiveNoiseDistance = Boolean.valueOf(args[2]);

        BatchPreprocess.copyTaskAndWorkerPointToDifferentPrivacyBudgetParentFile(basicSourcePath);
        Integer[] threadSizePerGroupArray = new Integer[]{
                2, 2, 2, 2, 2
        };
        BatchPreprocess.generatePrivacyBudgetAndNoiseDistanceForEachPrivacyBudgetBatchParallel3(basicSourcePath, isLLData, threadSizePerGroupArray,onlyPositiveNoiseDistance);
    }
}
