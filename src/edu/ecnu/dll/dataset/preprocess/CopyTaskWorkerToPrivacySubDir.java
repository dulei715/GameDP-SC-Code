package edu.ecnu.dll.dataset.preprocess;

import edu.ecnu.dll.config.Constant;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class CopyTaskWorkerToPrivacySubDir {
    public static void main(String[] args) throws IOException {
//        String basicParentParentParentPath = "E:\\1.学习\\4.数据集\\dataset\\original";
        String basicParentParentParentPath = args[0];
        String realDatasetParentParent = "1_real";
        String[] realDatasetSub = new String[] {
                "1_nyc", "2_tky", "3_chengdu"
        };
        String syntheticDatasetParentParent = "2_synthetic";
        String[] syntheticDatasetSub = new String[] {
                "1_uniform", "2_normal"
        };
        String defaultParentParentPath = "task_worker_1_2_0";
        String newParentPath = "privacy_change";

        String[] subNewParentPathArray = new String[5];
        for (int i = 1; i <= subNewParentPathArray.length; i++) {
            subNewParentPathArray[i-1] = "privacy_" + i;
        }
        File basicParentParentParentFile = new File(basicParentParentParentPath);
        File[] basicSubFileArray = basicParentParentParentFile.listFiles();
        File tempPPPFile, tempSourceParentFile, tempSourceFile, tempDesParentFile, tempDesFile;

        List<File> fileList = new ArrayList<>();
        for (int i = 0; i < realDatasetSub.length; i++) {
            fileList.add(new File(basicParentParentParentFile, realDatasetParentParent + Constant.FILE_PATH_SPLIT + realDatasetSub[i]));
        }
        for (int i = 0; i < syntheticDatasetSub.length; i++) {
            fileList.add(new File(basicParentParentParentFile, syntheticDatasetParentParent + Constant.FILE_PATH_SPLIT + syntheticDatasetSub[i]));
        }

        for (int i = 0; i < fileList.size(); i++) {
            tempPPPFile = fileList.get(i);
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
//                System.out.println(tempSourceFile.getAbsoluteFile() + "; " + tempDesFile.getAbsoluteFile());

                tempSourceFile = new File(tempSourceParentFile, "worker_point.txt");
                tempDesFile = new File(tempDesParentFile, "worker_point.txt");
                Files.copy(tempSourceFile.toPath(), tempDesFile.toPath());
//                System.out.println(tempSourceFile.getAbsoluteFile() + "; " + tempDesFile.getAbsoluteFile());

            }



        }
    }
}
