package edu.ecnu.dll.dataset.run;

import edu.ecnu.dll.dataset.dataset_generating.MainDataSetGenerator;
import edu.ecnu.dll.dataset.dataset_generating.sample.SamplingFunction;
import edu.ecnu.dll.dataset.dataset_generating.sample.impl.MeanSamplingFunction;

public class GenerateDatasets {

    public static final String basicPath = System.getProperty("user.dir") + "\\dataset\\";

    public static void main(String[] args) {
        int taskSize = generateTaskPointDatasetFromDataset();
        int workerSize = generateWorkerPointDatasetFromDataset();
        int budgetSize = 10;
        generateTaskValueDataset(taskSize);
        generateWorkerBudgetDataset(workerSize, taskSize, budgetSize);
    }


    public static int generateTaskPointDatasetFromDataset() {
        String inputDatasetPath = basicPath + "real_dataset\\chengdu.node";
        String outputDatasetPath = basicPath + "test_dataset\\_2_multiple_task_dataset\\test2\\task_point.txt";
        int groupMemberSize = 1000;
        int shareSize = 10;
        int bias = 3;
        SamplingFunction samplingFunction = new MeanSamplingFunction(groupMemberSize, shareSize, bias);
        return MainDataSetGenerator.generateDataSet(inputDatasetPath, outputDatasetPath, samplingFunction);
    }

    public static int generateWorkerPointDatasetFromDataset() {
        String inputDatasetPath = basicPath + "real_dataset\\chengdu.node";
        String outputDatasetPath = basicPath + "test_dataset\\_2_multiple_task_dataset\\test2\\worker_point.txt";
        int groupMemberSize = 1000;
        int shareSize = 10;
        int bias = 6;
        SamplingFunction samplingFunction = new MeanSamplingFunction(groupMemberSize, shareSize, bias);
        return MainDataSetGenerator.generateDataSet(inputDatasetPath, outputDatasetPath, samplingFunction);
    }

    public static void generateTaskValueDataset(Integer taskSize) {
        String outputDatasetPath = basicPath + "test_dataset\\_2_multiple_task_dataset\\test2\\task_value.txt";
        if (taskSize == null || taskSize < 0) {
            taskSize = 645;
        }
        int lowerBound = 10000;
        int upperBound = 100000;
        int precision = 2;
        MainDataSetGenerator.generateTaskValuesDataSet(outputDatasetPath, taskSize, lowerBound, upperBound, precision);
    }

    public static void generateWorkerBudgetDataset(Integer workerSize, Integer taskSize, Integer budgetSize) {
        String outputDatasetPath = basicPath + "test_dataset\\_2_multiple_task_dataset\\test2\\worker_privacy_budget.txt";
        if (workerSize == null || workerSize < 0) {
            workerSize = 1000;
        }
        if (taskSize == null || taskSize < 0) {
            taskSize = 1000;
        }
        if (budgetSize == null || budgetSize < 0) {
            budgetSize = 5;
        }
        double lowerBound = 0;
        double upperBound = 10;
        int precision = 2;
        MainDataSetGenerator.generateWorkerPrivacyBudgetDataSet(outputDatasetPath, workerSize, taskSize, budgetSize, lowerBound, upperBound, precision);
    }

}
