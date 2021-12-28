package edu.ecnu.dll.dataset.batch.necessary_data_gen_other_extract;

import edu.ecnu.dll.config.Constant;
import edu.ecnu.dll.dataset.batch.BatchPreprocess;

import java.io.File;

public class GenerateMaximumWorkerScaleBudgetAndNoiseDistance {
    public static void main(String[] args) {
        String basicDirPath = args[0];
        Boolean isLLData = Boolean.valueOf(args[1]);

        double factorK = Double.valueOf(args[2]);
        double constA = 0;

        // 复制(并统一单位)task和worker到 task_worker_1_3_0 目录下
        int basicSize = 1000;
        String scaleOutputPath = BatchPreprocess.scaleOutputPath[BatchPreprocess.scaleOutputPath.length-1];
        String inputDirPath = basicDirPath + File.separator + "batch_dataset";
        String outputDirPath = basicDirPath + File.separator + scaleOutputPath;

        double scaleSize = BatchPreprocess.scale[BatchPreprocess.scale.length-1];
        BatchPreprocess.scaleAndCopyTaskPointToGivenParentFile(inputDirPath, outputDirPath, factorK, constA);
        BatchPreprocess.scaleAndExtractWorkerPointToGivenParentFile(inputDirPath, outputDirPath, scaleSize, basicSize, factorK, constA);


        // 在 task_worker_1_3_0 目录下生成budget和 noise distance
        Integer threadSize = 8;
        String parentPath = Constant.parentPathArray[Constant.parentPathArray.length-1];
        BatchPreprocess.generatePrivacyBudgetAndNoiseDistanceForGivenWorkerBatchParallel3(basicDirPath, parentPath, isLLData, threadSize);
    }
}
