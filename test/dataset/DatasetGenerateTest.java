package dataset;

import edu.ecnu.dll.dataset.dataset_generating.DataSetGenerator;
import org.junit.Test;

public class DatasetGenerateTest {
    @Test
    public void generateUniformDataset() {
        String basicPath = "E:\\dataset\\uniform_dataset.txt";
        int dimensionLength = 10000;
        int pointSize = 200000;
        DataSetGenerator.generateUniformPlaneDataPoint(dimensionLength, pointSize, basicPath);

    }

    @Test
    public void generateNormalDataset() {
        String basicPath = "E:\\dataset\\normal_dataset.txt";
        int pointSize = 200_000;
        double mean = 0;
        double variance = 1_000_000_000;
        DataSetGenerator.generateNormalPlaneDataPoint(pointSize, mean, variance, basicPath);
    }

    


}
