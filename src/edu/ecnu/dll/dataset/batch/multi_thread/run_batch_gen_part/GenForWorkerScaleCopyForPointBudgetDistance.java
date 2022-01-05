package edu.ecnu.dll.dataset.batch.multi_thread.run_batch_gen_part;

import edu.ecnu.dll.dataset.batch.BatchPreprocess;

public class GenForWorkerScaleCopyForPointBudgetDistance {
    public static void main(String[] args) {
        String basicDirPath = args[0];
        Boolean isLLData = Boolean.valueOf(args[1]);

        double factorK = Integer.valueOf(args[2]);

        Boolean onlyPositiveNoiseDistance = Boolean.valueOf(args[3]);

        double constA = 0;

        BatchPreprocess.scaleAndCopyTaskPointToDifferentWorkerScaleParentFile(basicDirPath, factorK, constA);
        int basicSize = 1000;
//        String exclusionParent = BatchPreprocess.scaleOutputPath[2];

        BatchPreprocess.scaleAndExtractRandomWorkerPointToDifferentWorkerScaleParentFile(basicDirPath, basicSize, factorK, constA);


        Integer[] threadSizePerGroupArray = new Integer[]{
                1, 2, 2, 3, 3
        };
        BatchPreprocess.generatePrivacyBudgetAndNoiseDistanceForEachWorkerBatchParallel3(basicDirPath, isLLData, threadSizePerGroupArray, onlyPositiveNoiseDistance);
    }
}
