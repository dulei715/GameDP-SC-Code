package edu.ecnu.dll.dataset.batch.multi_thread.run_batch_gen_main.part;

import edu.ecnu.dll.dataset.batch.BatchPreprocess;

public class GenForSingleScaleOnlyDistanceWithBias {
    public static void main(String[] args) {
        String basicDirPath = args[0];
        Boolean isLLData = Boolean.valueOf(args[1]);
        Boolean onlyPositiveNoiseDistance = Boolean.valueOf(args[2]);
        Integer genScaleIndex = Integer.valueOf(args[3]);
        Integer threadSize = Integer.valueOf(args[4]);

        Integer[] threadSizePerGroupArray = new Integer[]{
                0, 0, 0, 0, 0
        };
        threadSizePerGroupArray[genScaleIndex] = threadSize;
        BatchPreprocess.generateNoiseDistanceForEachWorkerBatchParallel3(basicDirPath, isLLData, threadSizePerGroupArray, onlyPositiveNoiseDistance);
    }
}
