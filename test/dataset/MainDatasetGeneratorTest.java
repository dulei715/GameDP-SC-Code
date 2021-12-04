package dataset;

import edu.ecnu.dll.config.Constant;
import edu.ecnu.dll.dataset.dataset_generating.MainDataSetGenerator;
import org.junit.Test;
import tools.io.print.MyPrint;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

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

    @Test
    public void copyTaskWorkerPointToPrivacyChangeDirector() throws IOException {
        String basicParentParentParentPath = "E:\\1.学习\\4.数据集\\dataset\\original";
        String defaultParentParentPath = "task_worker_1_2_0";
        String newParentPath = "privacy_change";
        String[] subNewParentPathArray = new String[5];
        for (int i = 1; i <= subNewParentPathArray.length; i++) {
            subNewParentPathArray[i-1] = "privacy_" + i;
        }
        File basicParentParentParentFile = new File(basicParentParentParentPath);
        File[] basicSubFileArray = basicParentParentParentFile.listFiles();
        File tempPPPFile, tempSourceParentFile, tempSourceFile, tempDesParentFile, tempDesFile;

        for (int i = 0; i < basicSubFileArray.length; i++) {
            tempPPPFile = basicSubFileArray[i];
            tempSourceParentFile = new File(tempPPPFile, defaultParentParentPath);
//            tempDesParentFileArray = new File[subNewParentPathArray.length];

            for (int j = 0; j < subNewParentPathArray.length; j++) {

                tempDesParentFile = new File(tempPPPFile, defaultParentParentPath + Constant.FILE_PATH_SPLIT + newParentPath + Constant.FILE_PATH_SPLIT + subNewParentPathArray[j]);
                if (!tempDesParentFile.exists()) {
                    tempDesParentFile.mkdirs();
                }
                tempSourceFile = new File(tempSourceParentFile, "task_point.txt");
                tempDesFile = new File(tempDesParentFile, "task_point.txt");
                Files.copy(tempSourceFile.toPath(), tempDesFile.toPath());
//            System.out.println(tempSourceParentFile.getAbsoluteFile());

                tempSourceFile = new File(tempSourceParentFile, "worker_point.txt");
                tempDesFile = new File(tempDesParentFile, "worker_point.txt");
                Files.copy(tempSourceFile.toPath(), tempDesFile.toPath());

            }



        }
    }

    @Test
    public void generateDatasetWithDifferentPrivacyBudgetRange() {
        String basicParentParentParentPath = "E:\\1.学习\\4.数据集\\dataset\\original";
        String defaultParentParentPath = "task_worker_1_2_0";
        String newParentPath = "privacy_change";
        int privacyGroupSize = 7;
        String[] subNewParentPathArray = new String[5];
        double[][] privacyRangeArray = new double[5][2];
        for (int i = 1; i <= subNewParentPathArray.length; i++) {
            subNewParentPathArray[i-1] = "privacy_" + i;
            privacyRangeArray[i-1] = new double[]{1.0+(i-1)*0.5, 1.5+(i-1)*0.5};
        }
        File basicParentParentParentFile = new File(basicParentParentParentPath);
        File[] basicSubFileArray = basicParentParentParentFile.listFiles();
        Boolean[] isLongLatitudeArray = new Boolean[basicSubFileArray.length];
        for (int i = 0; i < basicSubFileArray.length; i++) {
            if (basicSubFileArray[i].getName().startsWith("nyc") || basicSubFileArray[i].getName().startsWith("tky")) {
                isLongLatitudeArray[i] = true;
            } else {
                isLongLatitudeArray[i] = false;
            }
        }
        MyPrint.showArray(isLongLatitudeArray);
        File tempPPPFile, tempSourceParentFile, tempSourceFile, tempDesParentFile, tempDesFile;
        File[] tempPrivacyFileArray;

        for (int i = 0; i < basicSubFileArray.length; i++) {
            tempPPPFile = basicSubFileArray[i];
            tempSourceParentFile = new File(tempPPPFile, defaultParentParentPath + Constant.FILE_PATH_SPLIT + newParentPath);
            tempPrivacyFileArray = tempSourceParentFile.listFiles();
            for (int j = 0; j < tempPrivacyFileArray.length; j++) {
                tempSourceFile = tempPrivacyFileArray[j];
//                System.out.println(tempSourceFile.getAbsoluteFile());
//                System.out.println(privacyRangeArray[j][0] + "; " + privacyRangeArray[j][1]);
//                MyPrint.showSplitLine("*", 200);
                // 生成dataset
                MainDataSetGenerator.generatePrivacyBudgetFromTaskWorkerPoint(tempSourceFile.getAbsolutePath(), privacyRangeArray[j], privacyGroupSize);
                MainDataSetGenerator.generateNoiseDistanceFromTaskWorkerPointAndPrivacyBudget(tempSourceFile.getAbsolutePath(), isLongLatitudeArray[j]);
            }



        }

    }


}
