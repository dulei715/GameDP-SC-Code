package edu.ecnu.dll.dataset.run;

import edu.ecnu.dll.dataset.dataset_generating.DataSetGenerator;

public class GenerateDatasets {

    public static void main(String[] args) {
        generateTaskValueDataset();
    }

    public static void generateTaskValueDataset() {
        String basicPath = System.getProperty("user.dir") + "\\dataset\\";
        String outputDatasetPath = basicPath + "test_dataset\\_2_multiple_task_dataset\\test1\\task_value.txt";
        int taskSize = 645;
        int lowerBound = 10000;
        int upperBound = 100000;
        int precision = 2;
        DataSetGenerator.generateTaskValuesDataSet(outputDatasetPath, taskSize, lowerBound, upperBound, precision);
    }
}
