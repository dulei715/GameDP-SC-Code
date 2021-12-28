package edu.ecnu.dll.dataset.batch.necessary_data_gen_other_extract;

import edu.ecnu.dll.config.Constant;
import edu.ecnu.dll.dataset.batch.BatchPreprocess;

import java.io.File;

public class ExtractPointBudgetAndNoiseDistanceByWorkerScale {
    public static void main(String[] args) {
        String basicDirPath = args[0];

        String inputDirName = BatchPreprocess.scaleOutputPath[BatchPreprocess.scaleOutputPath.length-1];
        String inputDirPath = basicDirPath + File.separator + inputDirName;
        Integer scaleIndex = Integer.valueOf(args[1]);
        String outputFileName = BatchPreprocess.scaleOutputPath[scaleIndex];
        String scaleOutputDirPath = basicDirPath + File.separator + outputFileName;


        // 复制(并统一单位)task和worker到 task_worker_1_3_0 目录下
        int basicSize = 1000;
        double factorK = 1.0;
        double constA = 0;
        double scaleSize = BatchPreprocess.scale[scaleIndex];

        BatchPreprocess.scaleAndCopyTaskPointToGivenParentFile(inputDirPath, scaleOutputDirPath, factorK, constA);
        BatchPreprocess.scaleAndExtractWorkerPointToGivenParentFile(inputDirPath, scaleOutputDirPath, scaleSize, basicSize, factorK, constA);


        // 在 task_worker_1_3_0 目录下生成budget和 noise distance
        String budgetEndName = "budget.txt";
        String noiseDistanceEndName = "noise_distance.txt";
//        BatchPreprocess.generatePrivacyBudgetAndNoiseDistanceForGivenWorkerBatchParallel3(basicDirPath, parentPath, isLLData, threadSize);
        BatchPreprocess.scaleAndExtractWorkerTaskPairDataToGivenParentFile(inputDirPath, scaleOutputDirPath, budgetEndName, scaleSize, basicSize);
        BatchPreprocess.scaleAndExtractWorkerTaskPairDataToGivenParentFile(inputDirPath, scaleOutputDirPath, noiseDistanceEndName, scaleSize, basicSize);

    }
}
