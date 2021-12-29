package edu.ecnu.dll.dataset.batch.necessary_data_gen_other_extract;

import edu.ecnu.dll.config.Constant;
import edu.ecnu.dll.dataset.batch.BatchPreprocess;

import java.io.File;

public class GenerateAllPrivacyScaleBudgetAndNoiseDistanceWithPointCopy {
    public static void main(String[] args) {
        String basicDirPath = args[0];
//        String basicDirPath = "E:\\1.学习\\4.数据集\\dataset\\original\\normal_total_dataset_km";
        Boolean isLLData = Boolean.valueOf(args[1]);
//        Boolean isLLData = false;
        Boolean onlyPositiveNoiseDistance = Boolean.valueOf(args[2]);
//        Boolean onlyPositiveNoiseDistance = false;

        String parentName = BatchPreprocess.scaleOutputPath[2];

        double factorK = 1.0;
        double constA = 0;

        // 复制(并统一单位)task和worker到 task_worker_1_3_0 目录下
        int basicSize = 1000;


        for (int i = 0; i < BatchPreprocess.budgetOutputPath.length; i++) {
            String inputDirPath = basicDirPath + File.separator + parentName;
            String outputDirPath = inputDirPath + File.separator + BatchPreprocess.budgetOutputPath[i];
            BatchPreprocess.scaleAndCopyTaskPointToGivenParentFile(inputDirPath, outputDirPath, factorK, constA);
            BatchPreprocess.scaleAndCopyWorkerPointToGivenParentFile(inputDirPath, outputDirPath, factorK, constA);
        }

        Integer[] threadSizePerGroupArray = new Integer[] {
                2, 2, 2, 2, 2
        };

        BatchPreprocess.generatePrivacyBudgetAndNoiseDistanceForEachWorkerBatchParallel3(basicDirPath, isLLData, threadSizePerGroupArray, onlyPositiveNoiseDistance);
    }
}
