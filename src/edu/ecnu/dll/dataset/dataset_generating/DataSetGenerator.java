package edu.ecnu.dll.dataset.dataset_generating;

import edu.ecnu.dll.dataset.dataset_generating.sample.SamplingFunction;

import java.io.*;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;
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

    public void generateDataSet(String dataPointInputPath, String samplingOutputPath, SamplingFunction samplingFunction) {
        BufferedReader bufferedReader = null;
        BufferedWriter bufferedWriter = null;
        File outputFile = null;
        File outputFileParent = null;
        String lineString;
        int dataSize, dataIndex = 0;
        List<Integer> sampleIndexList = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(new File(dataPointInputPath)));
            outputFile = new File(samplingOutputPath);
            outputFileParent = outputFile.getParentFile();
            if (!outputFileParent.exists()) {
                outputFileParent.mkdirs();
                outputFile.createNewFile();
            } else if (!outputFile.exists()) {
                outputFile.createNewFile();
            }

            bufferedWriter = new BufferedWriter(new FileWriter(new File(samplingOutputPath)));

            lineString = bufferedReader.readLine();
            dataSize = Integer.valueOf(lineString);
            sampleIndexList = samplingFunction.sample(dataSize);
            bufferedWriter.write(String.valueOf(sampleIndexList.size()));
            bufferedWriter.newLine();

            for (Integer chosenIndex : sampleIndexList) {
                for (; dataIndex < chosenIndex; ++dataIndex) {
                    bufferedReader.readLine();
                }
                lineString = bufferedReader.readLine();
                ++ dataIndex;
//                if (lineString == null) {
//                    System.out.println(lineString);
//                }
                bufferedWriter.write(lineString);
                bufferedWriter.newLine();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bufferedWriter.close();
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void generateWorkerDataSet() {

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
