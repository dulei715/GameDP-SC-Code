package edu.ecnu.dll.run.result_tools;

import tools.io.write.BasicWrite;

import java.util.ArrayList;
import java.util.List;

public class ResultOutput {

    public static void outputData(String outputPath, List<Double> targetList, List<Double> ... dataResult) {
        BasicWrite basicWrite = new BasicWrite();
        basicWrite.startWriting(outputPath);
        basicWrite.writeData(targetList);
        for (int i = 0; i < dataResult.length; i++) {
            basicWrite.writeData(dataResult[i]);
        }
        basicWrite.endWriting();
    }

    public static void main(String[] args) {
        List<Double> targetList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            targetList.add((double) i);
        }

        List<Double>[] listArray = new ArrayList[3];
        for (int i = 0; i < listArray.length; i++) {
            listArray[i] = new ArrayList<>();
            for (int j = 0; j < targetList.size(); j++) {
                listArray[i].add(j + Math.random());
            }
        }

        String outputPath = "D:\\temp\\output_test\\1.txt";
        ResultOutput.outputData(outputPath, targetList, listArray);

    }

}
