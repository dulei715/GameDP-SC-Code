package dataset;

import edu.ecnu.dll.dataset.preprocess.Preprocess;
import org.junit.Test;
import tools.io.read.PointRead;

public class PreprocessTest {
    @Test
    public void fun1(){
        String basicPath = "E:\\1.学习\\4.数据集\\dataset\\original\\chengdu_total_dataset\\task_worker_1_2_0\\";
        String inputTaskDataset = basicPath + "task_point_original.txt";
        String inputWorkerDataset = basicPath + "worker_point_original.txt";

        String outputTaskDataset = basicPath + "task_point.txt";
        String outputWorkerDataset = basicPath + "worker_point.txt";

        Double factorK = 0.001;
        Double constA = 0.0;

        Preprocess.multipleDataWithFirstLineUnchanged(inputTaskDataset, outputTaskDataset, factorK, constA, " ");
        Preprocess.multipleDataWithFirstLineUnchanged(inputWorkerDataset, outputWorkerDataset, factorK, constA, " ");
    }

    @Test
    public void fun2() {
//        String basicParentPath = "\\uniform_total_dataset_km";
        String basicParentPath = "\\normal_total_dataset_km";

        String inputTaskPointPath = "E:\\1.学习\\4.数据集\\dataset\\original" + basicParentPath + "\\task_point.txt";
        String inputWorkerPointPath = "E:\\1.学习\\4.数据集\\dataset\\original" + basicParentPath + "\\worker_point.txt";
        String outputBasic = "E:\\1.学习\\4.数据集\\dataset\\original" + basicParentPath + "\\";
        String[] outputPathParentPart = new String[]{
                "task_worker_1_1_0\\",
                "task_worker_1_1_5\\",
                "task_worker_1_2_0\\",
                "task_worker_1_2_5\\",
                "task_worker_1_3_0\\",
                "task_worker_1_3_5\\",
        };
        String workerPointFileName = "worker_point.txt";
        String taskPointFileName = "task_point.txt";
        double[] scales = new double[] {
                1, 1.5, 2, 2.5, 3, 3.5
        };
        double factorK = 1.0;
        double constA = 0.0;
        Integer taskSize = PointRead.readPointSizeWithFirstLineCount(inputTaskPointPath);
        double tempScale;
        int workerSize;
        for (int i = 0; i < scales.length; i++) {
            tempScale = scales[i];
            workerSize = (int)(taskSize*tempScale);
            Preprocess.extractRandomPointByGivenSize(inputTaskPointPath, outputBasic + outputPathParentPart[i] + taskPointFileName, taskSize, factorK, constA);
            Preprocess.extractRandomPointByGivenSize(inputWorkerPointPath, outputBasic + outputPathParentPart[i] + workerPointFileName, workerSize, factorK, constA);
        }
    }

}
