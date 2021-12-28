package edu.ecnu.dll.dataset.other;

import edu.ecnu.dll.dataset.batch.filter.DataFileNameFilter;
import tools.io.read.TwoDimensionDoubleRead;

import java.io.File;
import java.io.FilenameFilter;
import java.util.List;

public class DataTest {


    public static int[] getNegativeAndTotalNumberCount(String twoDimensionalDoubleDataPath) {
        List<Double[]>[] data = TwoDimensionDoubleRead.readDouble(twoDimensionalDoubleDataPath, 1);
        int totalSize = 0;
        int negativeSize = 0;
        for (int i = 0; i < data.length; i++) {
            for (Double[] doubles : data[i]) {
                for (int j = 0; j < doubles.length; j++) {
                    ++totalSize;
                    if (doubles[j] < 0) {
                        ++negativeSize;
                    }
                }
            }
        }
        return new int[]{negativeSize, totalSize};
    }

    public static int[] getNegativeAndTotalNumberCountInDir(String parentPath, String matchingName) {
        FilenameFilter filter = new DataFileNameFilter(matchingName);
        File parentFile = new File(parentPath);
        File[] files = parentFile.listFiles(filter);
        int totalNumberSize = 0;
        int totalNegativeSize = 0;
        int[] partResult;
        String filePath;
        for (int i = 0; i < files.length; i++) {
            filePath = files[i].getAbsolutePath();
            partResult = getNegativeAndTotalNumberCount(filePath);
            totalNegativeSize += partResult[0];
            totalNumberSize += partResult[1];
        }
        return new int[]{totalNegativeSize, totalNumberSize};
    }



    public static void main(String[] args) {
//        String path = "F:\\test";
        String path = args[0];
        String matchingName = "noise_distance.txt";
        int[] result = getNegativeAndTotalNumberCountInDir(path, matchingName);
        System.out.println(result[0]);
        System.out.println(result[1]);
        System.out.println(result[0]*1.0/result[1]);
    }



}
