package dataset;

import edu.ecnu.dll.dataset.dataset_generating.MainDataSetGenerator;
import edu.ecnu.dll.dataset.dataset_generating.sample.SamplingFunction;
import edu.ecnu.dll.dataset.dataset_generating.sample.impl.MeanSamplingFunction;
import edu.ecnu.dll.dataset.preprocess.Preprocess;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class DatasetGenerateTest {
    @Test
    public void generateUniformDataset() {
        String basicPath = "E:\\dataset\\uniform_dataset.txt";
        int dimensionLength = 100;
        int pointSize = 10000;
        MainDataSetGenerator.generateUniformPlaneDataPoint(dimensionLength, pointSize, basicPath);

    }

    @Test
    public void generateNormalDataset() {
        String basicPath = "E:\\dataset\\normal_dataset.txt";
        int pointSize = 10000;
        double mean = 0;
        double variance = 150;
        MainDataSetGenerator.generateNormalPlaneDataPoint(pointSize, mean, variance, basicPath);
    }

    @Test
    public void fun1() {
        String uniformPath = "E:\\1.学习\\4.数据集\\dataset\\original\\uniform_total_dataset_km\\uniform_dataset.txt";
        String uniformTaskOutputPath = "E:\\1.学习\\4.数据集\\dataset\\original\\uniform_total_dataset_km\\task_point.txt";
        String uniformWorkerOutputPath = "E:\\1.学习\\4.数据集\\dataset\\original\\uniform_total_dataset_km\\worker_point.txt";

        String normalPath = "E:\\1.学习\\4.数据集\\dataset\\original\\normal_total_dataset_km\\normal_dataset.txt";
        String normalTaskOutputPath = "E:\\1.学习\\4.数据集\\dataset\\original\\normal_total_dataset_km\\task_point.txt";
        String normalWorkerOutputPath = "E:\\1.学习\\4.数据集\\dataset\\original\\normal_total_dataset_km\\worker_point.txt";


        int size = 2000;
        Preprocess.extractRandomPointByGivenSize(uniformPath, uniformTaskOutputPath, size, 1, 0);
        Preprocess.extractRemainPointByGivenSet(uniformPath, uniformTaskOutputPath, uniformWorkerOutputPath, 1, 0);

        Preprocess.extractRandomPointByGivenSize(normalPath, normalTaskOutputPath, size, 1, 0);
        Preprocess.extractRemainPointByGivenSet(normalPath, normalTaskOutputPath, normalWorkerOutputPath, 1, 0);



    }

    @Test
    public void generateDatasetsFromBothSyntheticAndRealDataset() {
        String totalUniformDatasetPath = "E:\\dataset\\uniform_dataset.txt";
        String totalNormalDatasetPath = "E:\\dataset\\normal_dataset.txt";
        String totalRealDatasetPath = "D:\\workspace\\5.github\\GTDP\\dataset\\real_dataset\\chengdu.node";

        String basicOutputPath = "E:\\dataset\\output\\";
        String[] totalUniformDatasetOutputPathArray = new String[3];
        String[] totalNormalDatasetOutputPathArray = new String[3];
        String[] totalRealDatasetOutputPathArray = new String[3];

        for (int i = 0; i < totalRealDatasetOutputPathArray.length; i++) {
            totalUniformDatasetOutputPathArray[i] = basicOutputPath + "uniform_dataset_1_" + Math.pow(10,3-i) + ".txt";
            totalNormalDatasetOutputPathArray[i] = basicOutputPath + "normal_dataset_1_" + Math.pow(10,3-i) + ".txt";
            totalRealDatasetOutputPathArray[i] = basicOutputPath + "chengdu_1_" + Math.pow(10,3-i) + ".txt";
        }

        int groupMemberSize;
        int shareSize = 1;
        int bias = 0;
        SamplingFunction samplingFunction;  

        for (int i = 0; i < totalRealDatasetOutputPathArray.length; i++) {
            groupMemberSize = (int)Math.pow(10, 3 - i);
            samplingFunction = new MeanSamplingFunction(groupMemberSize, shareSize, bias);
            MainDataSetGenerator.generateDataSet(totalUniformDatasetPath, totalUniformDatasetOutputPathArray[i], samplingFunction);
            MainDataSetGenerator.generateDataSet(totalNormalDatasetPath, totalNormalDatasetOutputPathArray[i], samplingFunction);
            MainDataSetGenerator.generateDataSet(totalRealDatasetPath, totalRealDatasetOutputPathArray[i], samplingFunction);
        }

    }

    private int getDataSize(String dataFileStr) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(dataFileStr));
        String line = bufferedReader.readLine();
        Integer size = Integer.valueOf(line);
        bufferedReader.close();
        return size;
    }

    @Test
    public void generateTaskAndWorkerFromDataset() throws IOException {
        String basicBasicPath = "E:\\dataset\\output\\";
        String[] subParentPaths = new String[]{"normal", "uniform", "chengdu"};
//        String[] datasetScaleStrings = new String[]{"1000.0", "100.0", "10.0", "1.0"};
        String[] datasetScaleStrings = new String[]{"1000.0", "100.0", "10.0"};
//        String[]
//        Map<String, String[]> map = new HashMap();
        int totalSize, taskSize, workerSize;
        int budgetGroupSize = 7;
        int lowerBound = 7000;
        int upperBound = 15000;
        SamplingFunction taskSamplingFunction, workerSamplingFunction;
        taskSamplingFunction = new MeanSamplingFunction(2, 1, 0);
        workerSamplingFunction = new MeanSamplingFunction(2, 1, 1);
        String tempDirPath, tempTotalDatasetPath, taskPointOutputPath, workerPointOutputPath, taskValueOutputPath, workerBudgetOutputPath;
//        for (int i = 0; i < subParentPaths.length; i++) {
        for (int i = 1; i < subParentPaths.length; i++) {
            for (int j = 0; j < datasetScaleStrings.length; j++) {
                tempDirPath = basicBasicPath + subParentPaths[i] + "\\" + subParentPaths[i] + "_1_" + datasetScaleStrings[j];
                tempTotalDatasetPath = tempDirPath + ".txt";

                taskPointOutputPath = tempDirPath + "\\task_point.txt";
                workerPointOutputPath = tempDirPath + "\\worker_point.txt";
                taskValueOutputPath = tempDirPath + "\\task_value.txt";
                workerBudgetOutputPath = tempDirPath + "\\worker_budget.txt";
                totalSize = getDataSize(tempTotalDatasetPath);
                // 生成task节点
                taskSize = MainDataSetGenerator.generateDataSet(tempTotalDatasetPath, taskPointOutputPath, taskSamplingFunction);
                // 生成worker节点
                workerSize = MainDataSetGenerator.generateDataSet(tempTotalDatasetPath, workerPointOutputPath, workerSamplingFunction);
                // 生成value值
                MainDataSetGenerator.generateTaskValuesDataSet(taskValueOutputPath, taskSize, lowerBound, upperBound, 2);
                // 生成budget值
                MainDataSetGenerator.generateWorkerPrivacyBudgetDataSet(workerBudgetOutputPath, workerSize, taskSize, budgetGroupSize, 0, 10, 2);
            }
        }
    }

    @Test
    public void generateValueAndPrivacyBudgetFromTaskAndWorkerDataset() throws IOException {
        String basicBasicPath = "E:\\dataset\\output\\";
        String[] subParentPaths = new String[]{"normal", "uniform", "chengdu"};
//        String[] datasetScaleStrings = new String[]{"1000.0", "100.0", "10.0", "1.0"};
        String[] datasetScaleStrings = new String[]{"1000.0", "100.0", "10.0"};
//        String[]
//        Map<String, String[]> map = new HashMap();
        int totalSize, taskSize, workerSize;
        int budgetGroupSize = 7;
        int lowerBound = 7000;
        int upperBound = 15000;
        SamplingFunction taskSamplingFunction, workerSamplingFunction;
        taskSamplingFunction = new MeanSamplingFunction(2, 1, 0);
        workerSamplingFunction = new MeanSamplingFunction(2, 1, 1);
        String tempDirPath, tempTotalDatasetPath, taskPointOutputPath, workerPointOutputPath, taskValueOutputPath, workerBudgetOutputPath;
//        for (int i = 0; i < subParentPaths.length; i++) {
        for (int i = 1; i < subParentPaths.length; i++) {
            for (int j = 0; j < datasetScaleStrings.length; j++) {
                tempDirPath = basicBasicPath + subParentPaths[i] + "\\" + subParentPaths[i] + "_1_" + datasetScaleStrings[j];
                tempTotalDatasetPath = tempDirPath + ".txt";

                taskPointOutputPath = tempDirPath + "\\task_point.txt";
                workerPointOutputPath = tempDirPath + "\\worker_point.txt";
                taskValueOutputPath = tempDirPath + "\\task_value.txt";
                workerBudgetOutputPath = tempDirPath + "\\worker_budget.txt";
                totalSize = getDataSize(tempTotalDatasetPath);
                // 生成task节点
                taskSize = MainDataSetGenerator.generateDataSet(tempTotalDatasetPath, taskPointOutputPath, taskSamplingFunction);
                // 生成worker节点
                workerSize = MainDataSetGenerator.generateDataSet(tempTotalDatasetPath, workerPointOutputPath, workerSamplingFunction);
                // 生成value值
                MainDataSetGenerator.generateTaskValuesDataSet(taskValueOutputPath, taskSize, lowerBound, upperBound, 2);
                // 生成budget值
                MainDataSetGenerator.generateWorkerPrivacyBudgetDataSet(workerBudgetOutputPath, workerSize, taskSize, budgetGroupSize, 0, 10, 2);
            }
        }
    }

    @Test
    public void generateValueAndPrivacyBudgetNoiseDistance() {


    }


}
