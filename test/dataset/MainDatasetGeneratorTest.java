package dataset;

import edu.ecnu.dll.dataset.dataset_generating.MainDataSetGenerator;
import org.junit.Test;

public class MainDatasetGeneratorTest {


    @Test
    public void fun1() {
        String basicPath = "E:\\1.学习\\4.数据集\\dataset\\original\\uniform_total_dataset_km\\";
//        String basicPath = "E:\\1.学习\\4.数据集\\dataset\\original\\normal_total_dataset_km\\"
        String[] inputParentPath = new String[]{
                "task_worker_1_1_0",
                "task_worker_1_1_5",
                "task_worker_1_2_0",
                "task_worker_1_2_5",
                "task_worker_1_3_0",
                "task_worker_1_3_5"
        };

        boolean isLongitudeLatitude = false;
        double[] valueBound = new double[]{5,25};
        double[] rangeBound = new double[]{10,30};
        double[] budgetBound = new double[]{1,10};
        int budgetGroupSize = 7;
        for (int i = 0; i < inputParentPath.length; i++) {
            MainDataSetGenerator.generateTaskValuesWorkerRangesAndPrivacyBudgetFromTaskWorkerPoint(basicPath + inputParentPath[i], valueBound, rangeBound, budgetBound, budgetGroupSize);
            MainDataSetGenerator.generateNoiseDistanceFromTaskWorkerPointAndPrivacyBudget(basicPath + inputParentPath[i], isLongitudeLatitude);
        }
    }
}
