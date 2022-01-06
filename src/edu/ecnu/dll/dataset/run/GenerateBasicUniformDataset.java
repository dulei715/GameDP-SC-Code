package edu.ecnu.dll.dataset.run;

import edu.ecnu.dll.dataset.dataset_generating.MainDataSetGenerator;

import java.io.File;

public class GenerateBasicUniformDataset {
    public static void main(String[] args) {
//        String basicPath = "E:\\1.学习\\4.数据集\\dataset\\original\\uniform_total_dataset_km";
        String basicPath = args[0];
        String totalDatasetPath = basicPath + File.separator + "total_dataset";
        int dimensionLength = 100;
        int totalTaskNumber = 1000 * 300;
        int totalWorkerNumber = 3000 * 10;

        MainDataSetGenerator.generateUniformPlaneDataPoint(dimensionLength, totalTaskNumber, totalDatasetPath + File.separator + "task_point.txt");
        MainDataSetGenerator.generateUniformPlaneDataPointWithoutDuplication(dimensionLength, totalWorkerNumber, totalDatasetPath + File.separator + "worker_point.txt");

    }
}
