package dataset;

import edu.ecnu.dll.dataset.preprocess.Preprocess;
import org.junit.Test;

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
}
