package edu.ecnu.dll.dataset.batch.multi_thread.run_batch_gen_main;

import edu.ecnu.dll.dataset.batch.BatchPreprocess;

public class GenForWorkerScaleOnlyDistance {
    public static void main(String[] args) {
        String basicDirPath = args[0];
        Boolean isLLData = Boolean.valueOf(args[1]);

        Integer[] threadSizePerGroupArray = new Integer[]{
                1, 2, 2, 3, 3
        };
        BatchPreprocess.generateNoiseDistanceForEachWorkerBatchParallel3(basicDirPath, isLLData, threadSizePerGroupArray);
    }
}
