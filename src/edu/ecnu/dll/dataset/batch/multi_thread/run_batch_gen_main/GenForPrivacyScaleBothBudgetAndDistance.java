package edu.ecnu.dll.dataset.batch.multi_thread.run_batch_gen_main;

import edu.ecnu.dll.dataset.batch.BatchPreprocess;

public class GenForPrivacyScaleBothBudgetAndDistance {
    public static void main(String[] args) {
        String basicDirPath = args[0];
//        String basicDirPath = "E:\\1.学习\\4.数据集\\dataset\\original\\normal_total_dataset_km\\task_worker_1_2_0";
        Boolean isLLData = Boolean.valueOf(args[1]);
//        Boolean isLLData = false;

        Boolean onlyPositiveNoiseDistance = Boolean.valueOf(args[2]);

        Integer[] threadSizePerGroupArray = new Integer[]{
                2, 2, 2, 2, 2
        };
        BatchPreprocess.generatePrivacyBudgetAndNoiseDistanceForEachPrivacyBudgetBatchParallel3(basicDirPath, isLLData, threadSizePerGroupArray, onlyPositiveNoiseDistance);
    }
}
