package edu.ecnu.dll.dataset.run;

import edu.ecnu.dll.config.Constant;
import edu.ecnu.dll.dataset.dataset_generating.MainDataSetGenerator;
import tools.basic.StringUtil;

import java.io.File;

public class GenerateBasicNormalDataset {
    public static void main(String[] args) {
//        String basicPath = "E:\\1.学习\\4.数据集\\dataset\\original\\normal_total_dataset_km";
        String basicPath = args[0];
        String totalDatasetPath = basicPath + File.separator + "total_dataset";
        int totalTaskNumber = 1000 * 300;
        int totalWorkerNumber = 3000 * 10;
        double mean = 0;
        double variance = 150;

        MainDataSetGenerator.generateNormalPlaneDataPoint(totalTaskNumber, mean, variance, totalDatasetPath + File.separator + "task_point.txt");
        MainDataSetGenerator.generateNormalPlaneDataPointWithoutDuplication(totalWorkerNumber, mean, variance, totalDatasetPath + File.separator + "worker_point.txt");

    }
}
