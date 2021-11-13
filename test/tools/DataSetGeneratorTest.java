package tools;

import edu.ecnu.dll.dataset.dataset_generating.MainDataSetGenerator;
import edu.ecnu.dll.dataset.dataset_generating.sample.impl.MeanSamplingFunction;
import org.junit.Test;

public class DataSetGeneratorTest {
    @Test
    public void fun1() {
        MainDataSetGenerator dataSetGenerator = new MainDataSetGenerator();
        String basicPath = System.getProperty("user.dir") + "\\dataset\\";
        String inputDataSetPath = basicPath + "real_dataset\\chengdu.node";
        String outputDataSetPath = basicPath + "test_dataset\\_2_multiple_task_dataset\\test1\\task_point.txt";
        int groupMemberSize = 1000;
        int shareSize = 3;
        int bias = 0;
        dataSetGenerator.generateDataSet(inputDataSetPath, outputDataSetPath, new MeanSamplingFunction(groupMemberSize, shareSize, bias));
    }

    @Test
    public void fun2() {
        MainDataSetGenerator dataSetGenerator = new MainDataSetGenerator();
        String basicPath = System.getProperty("user.dir") + "\\dataset\\";
        String inputDataSetPath = basicPath + "real_dataset\\chengdu.node";
        String outputDataSetPath = basicPath + "test_dataset\\_2_multiple_task_dataset\\test1\\worker_point.txt";
        int groupMemberSize = 1000;
        int shareSize = 1;
        int bias = 3;
        dataSetGenerator.generateDataSet(inputDataSetPath, outputDataSetPath, new MeanSamplingFunction(groupMemberSize, shareSize, bias));
    }

    @Test
    public void fun3() {
        String basicPath = System.getProperty("user.dir") + "\\dataset\\";
        String outputDataSetPath = basicPath + "test_dataset\\_2_multiple_task_dataset\\test1\\task_value.txt";
        int taskSize = 645;
        double lowerBound = 100;
        double upperBound = 200;
        int precision = 2;
        MainDataSetGenerator.generateTaskValuesDataSet(outputDataSetPath, taskSize, lowerBound, upperBound, precision);
    }

    @Test
    public void fun4() {
        String basicPath = System.getProperty("user.dir") + "\\dataset\\";
        String outputDataSetPath = basicPath + "test_dataset\\_2_multiple_task_dataset\\test1\\worker_privacy_budget.txt";
        int taskSize = 645;
        int workerSize = 215;
        int budgetGroupSize = 3;
        double lowerBound = 0;
        double upperBound = 10;
        int precision = 2;
        MainDataSetGenerator.generateWorkerPrivacyBudgetDataSet(outputDataSetPath, workerSize, taskSize, budgetGroupSize, lowerBound, upperBound, precision);
    }
}
