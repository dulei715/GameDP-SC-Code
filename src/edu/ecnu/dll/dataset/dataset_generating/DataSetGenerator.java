package edu.ecnu.dll.dataset.dataset_generating;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Random;

public class DataSetGenerator {

    public void generateUniformPlaneDataPoint(int dimensionLength, int pointSize, String outputPath) {
        BufferedWriter bufferedWriter = null;
        double x, y;
        DecimalFormat decimalFormat = new DecimalFormat("0.000000");
        decimalFormat.setRoundingMode(RoundingMode.HALF_UP);
        try {
            bufferedWriter = new BufferedWriter(new FileWriter(new File(outputPath)));
            bufferedWriter.write(String.valueOf(pointSize));
            bufferedWriter.newLine();
            for (int i = 0; i < pointSize; i++) {
                x = Math.random() * dimensionLength;
                y = Math.random() * dimensionLength;
                bufferedWriter.write(decimalFormat.format(x) + " " + decimalFormat.format(y));
                bufferedWriter.newLine();
            }
            bufferedWriter.flush();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bufferedWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void generateNormalPlaneDataPoint(int dimensionLength, int pointSize, double mean, double variance, String outputPath) {
        Random random = new Random();
        BufferedWriter bufferedWriter = null;
        double x, y;
        DecimalFormat decimalFormat = new DecimalFormat("0.000000");
        decimalFormat.setRoundingMode(RoundingMode.HALF_UP);
        try {
            bufferedWriter = new BufferedWriter(new FileWriter(new File(outputPath)));
            bufferedWriter.write(String.valueOf(pointSize));
            bufferedWriter.newLine();
            for (int i = 0; i < pointSize; i++) {
                x = Math.sqrt(variance)*random.nextGaussian() + mean;
                y = Math.sqrt(variance)*random.nextGaussian() + mean;
                bufferedWriter.write(decimalFormat.format(x) + " " + decimalFormat.format(y));
                bufferedWriter.newLine();
            }
            bufferedWriter.flush();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bufferedWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static void main(String[] args) {
//        String outputPath = System.getProperty("user.dir") + "\\dataset\\UniformDataPoint.txt";
//        DataSetGenerator dataSetGenerator = new DataSetGenerator();
//        dataSetGenerator.generateUniformPlaneDataPoint(200000, 10000, outputPath);
        String outputPath = System.getProperty("user.dir") + "\\dataset\\NormalDataPoint.txt";
        DataSetGenerator dataSetGenerator = new DataSetGenerator();
        dataSetGenerator.generateNormalPlaneDataPoint(200000, 10000, 0, 10000000000L, outputPath);
    }
}
