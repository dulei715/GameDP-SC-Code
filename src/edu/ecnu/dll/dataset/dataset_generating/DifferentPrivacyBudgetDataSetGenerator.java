package edu.ecnu.dll.dataset.dataset_generating;

import edu.ecnu.dll.config.Constant;
import edu.ecnu.dll.dataset.dataset_generating.sample.SamplingFunction;
import tools.basic.BasicCalculation;
import tools.differential_privacy.noise.LaplaceUtils;
import tools.io.print.MyPrint;
import tools.io.read.PointRead;
import tools.io.read.TwoDimensionDoubleRead;
import tools.struct.Point;

import java.io.*;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DifferentPrivacyBudgetDataSetGenerator {


    public static void main(String[] args) {

//        String basicParentParentParentPath = "E:\\1.学习\\4.数据集\\dataset_for_linux";
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
        int privacyGroupSize = 7;
        String[] subNewParentPathArray = new String[5];
//        double[][] privacyRangeArray = new double[5][2];
        for (int i = 1; i <= subNewParentPathArray.length; i++) {
            subNewParentPathArray[i-1] = "privacy_" + i;
//            privacyRangeArray[i-1] = new double[]{1.0+(i-1)*0.5, 1.5+(i-1)*0.5};
        }
        File basicParentParentParentFile = new File(basicParentParentParentPath);
        List<File> fileList = new ArrayList<>();
        for (int i = 0; i < realDatasetSub.length; i++) {
            fileList.add(new File(basicParentParentParentFile, realDatasetParentParent + Constant.FILE_PATH_SPLIT + realDatasetSub[i]));
        }
        for (int i = 0; i < syntheticDatasetSub.length; i++) {
            fileList.add(new File(basicParentParentParentFile, syntheticDatasetParentParent + Constant.FILE_PATH_SPLIT + syntheticDatasetSub[i]));
        }



        Boolean[] isLongLatitudeArray = new Boolean[fileList.size()];
        for (int i = 0; i < fileList.size(); i++) {
            if (fileList.get(i).getName().endsWith("nyc") || fileList.get(i).getName().endsWith("tky")) {
                isLongLatitudeArray[i] = true;
            } else {
                isLongLatitudeArray[i] = false;
            }
        }
//        MyPrint.showArray(isLongLatitudeArray);
        File tempPPPFile, tempSourceParentFile, tempSourceFile, tempDesParentFile, tempDesFile;
        File[] tempPrivacyFileArray;

        for (int i = 0; i < fileList.size(); i++) {
            tempPPPFile = fileList.get(i);
            tempSourceParentFile = new File(tempPPPFile, defaultParentParentPath + Constant.FILE_PATH_SPLIT + newParentPath);
            tempPrivacyFileArray = tempSourceParentFile.listFiles();
            for (int j = 0; j < tempPrivacyFileArray.length; j++) {
                tempSourceFile = tempPrivacyFileArray[j];
//                System.out.println(tempSourceFile.getAbsoluteFile());
//                System.out.println(privacyRangeArray[j][0] + "; " + privacyRangeArray[j][1]);
//                MyPrint.showSplitLine("*", 200);
                // 生成dataset
                MainDataSetGenerator.generatePrivacyBudgetFromTaskWorkerPoint(tempSourceFile.getAbsolutePath(), Constant.parentBudgetRange[j], privacyGroupSize);
                MainDataSetGenerator.generateNoiseDistanceFromTaskWorkerPointAndPrivacyBudget(tempSourceFile.getAbsolutePath(), isLongLatitudeArray[i]);
            }



        }

    }
}
