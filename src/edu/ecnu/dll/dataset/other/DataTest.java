package edu.ecnu.dll.dataset.other;

import edu.ecnu.dll.dataset.batch.filter.DataFileNameFilter;
import tools.io.read.TwoDimensionDoubleRead;

import java.io.File;
import java.io.FilenameFilter;
import java.util.List;

public class DataTest {


    public static long[] getNegativeAndTotalNumberCount(String twoDimensionalDoubleDataPath) {
        List<Double[]>[] data = TwoDimensionDoubleRead.readDouble(twoDimensionalDoubleDataPath, 1);
        long totalSize = 0;
        long negativeSize = 0;
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
        return new long[]{negativeSize, totalSize};
    }

    public static long[] getNegativeAndTotalNumberCountInDir(String parentPath, String matchingName) {
        FilenameFilter filter = new DataFileNameFilter(matchingName);
        File parentFile = new File(parentPath);
        File[] files = parentFile.listFiles(filter);
        long totalNumberSize = 0;
        long totalNegativeSize = 0;
        long[] partResult;
        String filePath;
        for (int i = 0; i < files.length; i++) {
            filePath = files[i].getAbsolutePath();
            partResult = getNegativeAndTotalNumberCount(filePath);
            totalNegativeSize += partResult[0];
            totalNumberSize += partResult[1];
        }
        return new long[]{totalNegativeSize, totalNumberSize};
    }



    public static void main(String[] args) {
//        String path = "F:\\test";
        String path = args[0];
        String matchingName = "noise_distance.txt";
        long[] result = getNegativeAndTotalNumberCountInDir(path, matchingName);
        System.out.println(result[0]);
        System.out.println(result[1]);
        System.out.println(result[0]*1.0/result[1]);
    }



}
