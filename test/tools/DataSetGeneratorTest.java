package tools;

import edu.ecnu.dll.dataset.dataset_generating.DataSetGenerator;
import edu.ecnu.dll.dataset.dataset_generating.sample.impl.MeanSamplingFunction;
import org.junit.Test;

public class DataSetGeneratorTest {
    @Test
    public void fun1() {
        DataSetGenerator dataSetGenerator = new DataSetGenerator();
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
        DataSetGenerator dataSetGenerator = new DataSetGenerator();
        String basicPath = System.getProperty("user.dir") + "\\dataset\\";
        String inputDataSetPath = basicPath + "real_dataset\\chengdu.node";
        String outputDataSetPath = basicPath + "test_dataset\\_2_multiple_task_dataset\\test1\\worker_point.txt";
        int groupMemberSize = 1000;
        int shareSize = 1;
        int bias = 3;
        dataSetGenerator.generateDataSet(inputDataSetPath, outputDataSetPath, new MeanSamplingFunction(groupMemberSize, shareSize, bias));
    }
}
