import edu.ecnu.dll.dataset.batch.BatchSplit;
import org.junit.Test;
import tools.io.read.PointRead;
import tools.io.write.PointWrite;
import tools.struct.Point;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class PrepareHandle {
    @Test
    public void removeRepeatWorker() {
        String inputPath = "E:\\1.学习\\4.数据集\\dataset\\original\\chengdu_total_dataset_km\\total_dataset_before\\worker_point.txt";
        String outPath = "E:\\1.学习\\4.数据集\\dataset\\original\\chengdu_total_dataset_km\\total_dataset\\worker_point.txt";
        Set<Point> workerPointSet = PointRead.readPointWithoutRepeat(inputPath);
        List<Point> workerPointList = new ArrayList<>();
        workerPointList.addAll(workerPointSet);
        PointWrite pointWrite = new PointWrite();
        pointWrite.startWriting(outPath);
        pointWrite.writePoint(workerPointList);
        pointWrite.endWriting();
    }

    @Test
    public void splitTaskToBatchesAndGenerateRandomWorker() {
        BatchSplit batchSplit = new BatchSplit();
        String inputFilePath = "E:\\1.学习\\4.数据集\\dataset\\original\\chengdu_total_dataset_km\\total_dataset";
        String inputTaskPointPath = inputFilePath + File.separator + "task_point.txt";
        String inputWorkerPointPath = inputFilePath + File.separator + "worker_point.txt";

        String outputParentPath = "E:\\1.学习\\4.数据集\\dataset\\original\\chengdu_total_dataset_km\\batch_dataset";
        String outputBasicTaskName = "task_point.txt";
        String outputBasicWorkerName = "worker_point.txt";
        int taskBatchSize = 1000;
        int workerBatchSize = 3000;
        int initBatchGroupSize = -1;
        int realBatchGroupSize = batchSplit.splitToSubFileWithFirstLineSize(inputTaskPointPath, outputParentPath, outputBasicTaskName, taskBatchSize, initBatchGroupSize);
        batchSplit.randomExtractSubFileWithFirstLineSize(inputWorkerPointPath, outputParentPath, outputBasicWorkerName, workerBatchSize, realBatchGroupSize);
    }



}
