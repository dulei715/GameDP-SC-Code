package edu.ecnu.dll.dataset.run;

import edu.ecnu.dll.dataset.batch.BatchPreprocess;

public class GenerateWorkerScaleDataset {
    public static void main(String[] args) {
//        String basicDirPath = "E:\\1.学习\\4.数据集\\dataset\\original\\chengdu_total_dataset_km";
//        String basicDirPath = "E:\\1.学习\\4.数据集\\dataset\\original\\normal_total_dataset_km";
//        Boolean isLLData = false;
        String basicDirPath = args[0];
        Boolean isLLData = Boolean.valueOf(args[1]);
//        Boolean onlyPositiveNoiseDistance = true;
        Boolean onlyPositiveNoiseDistance = Boolean.valueOf(args[2]);
//        double factorK = 0.001;
//        double constA = 0;
        double factorK = Double.valueOf(args[3]);
        double constA = Double.valueOf(args[4]);

        BatchPreprocess.scaleAndCopyTaskPointToDifferentWorkerScaleParentFile(basicDirPath, factorK, constA);
        int basicSize = 1000;
        BatchPreprocess.scaleAndExtractRandomWorkerPointToDifferentWorkerScaleParentFile(basicDirPath, basicSize, factorK, constA);
        Integer[] threadSizePerGroupArray = new Integer[]{
                1, 2, 2, 3, 3
        };
        BatchPreprocess.generatePrivacyBudgetAndNoiseDistanceForEachWorkerBatchParallel3(basicDirPath, isLLData, threadSizePerGroupArray, onlyPositiveNoiseDistance);
    }
}
