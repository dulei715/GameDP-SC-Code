package edu.ecnu.dll.dataset.dataset_generating;

import edu.ecnu.dll.dataset.dataset_generating.sample.SamplingFunction;
import tools.basic.BasicCalculation;
import tools.differential_privacy.noise.LaplaceUtils;
import tools.io.read.PointRead;
import tools.io.read.TwoDimensionDoubleRead;
import tools.struct.Point;

import java.io.*;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Random;

public class MainDataSetGenerator {

    public static final String WRITING_SPLIT_TAG_IN_LINE = " ";

    public static void generateUniformPlaneDataPoint(int dimensionLength, int pointSize, String outputPath) {
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
                bufferedWriter.write(decimalFormat.format(x) + WRITING_SPLIT_TAG_IN_LINE + decimalFormat.format(y));
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

    public static void generateNormalPlaneDataPoint(int pointSize, double mean, double variance, String outputPath) {
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
                bufferedWriter.write(decimalFormat.format(x) + WRITING_SPLIT_TAG_IN_LINE + decimalFormat.format(y));
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

    public static int generateDataSet(String dataPointInputPath, String samplingOutputPath, SamplingFunction samplingFunction) {
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
        return sampleIndexList.size();
    }

    public static void generateTaskValuesDataSet(String outputPath, int taskSize, double lowerBound, double upperBound, int precision) {
        BufferedWriter bufferedWriter = null;
        String taskValue = null;
        try {
            File outputFile = new File(outputPath);
            File parentDir = outputFile.getParentFile();
            if (!parentDir.exists()) {
                parentDir.mkdirs();
            }
            bufferedWriter = new BufferedWriter(new FileWriter(outputFile));
            bufferedWriter.write(String.valueOf(taskSize));
            bufferedWriter.newLine();
            for (int i = 0; i < taskSize; i++) {
                taskValue = BasicCalculation.getRandomStringValueInRange(lowerBound, upperBound, precision);
                bufferedWriter.write(taskValue);
                bufferedWriter.newLine();
            }
        }  catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bufferedWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public static void generateWorkerRangesDataSet(String outputPath, int workerSize, double lowerBound, double upperBound, int precision) {
        BufferedWriter bufferedWriter = null;
        String taskValue = null;
        try {
            File outputFile = new File(outputPath);
            File parentDir = outputFile.getParentFile();
            if (!parentDir.exists()) {
                parentDir.mkdirs();
            }
            bufferedWriter = new BufferedWriter(new FileWriter(outputFile));
            bufferedWriter.write(String.valueOf(workerSize));
            bufferedWriter.newLine();
            for (int i = 0; i < workerSize; i++) {
                taskValue = BasicCalculation.getRandomStringValueInRange(lowerBound, upperBound, precision);
                bufferedWriter.write(taskValue);
                bufferedWriter.newLine();
            }
        }  catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bufferedWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public static void generateWorkerPrivacyBudgetDataSet(String outputPath, int workerSize, int taskSize, int budgetGroupSize, double lowerBound, double upperBound, int precision) {
        BufferedWriter bufferedWriter = null;
        String[][] taskValue = null;
        try {
            File outputFile = new File(outputPath);
            File parentDir = outputFile.getParentFile();
            if (!parentDir.exists()) {
                parentDir.mkdirs();
            }
            bufferedWriter = new BufferedWriter(new FileWriter(outputFile));
            bufferedWriter.write(String.valueOf(workerSize) + WRITING_SPLIT_TAG_IN_LINE + String.valueOf(taskSize) + WRITING_SPLIT_TAG_IN_LINE + String.valueOf(budgetGroupSize));
            bufferedWriter.newLine();
            for (int i = 0, j, k; i < workerSize; i++) {
//                taskValue = BasicCalculation.getRandomStringValueTwoDimensionArrayInRange(lowerBound, upperBound, precision, taskSize, budgetGroupSize);
                taskValue = BasicCalculation.getSortedRandomStringValueTwoDimensionArrayInRange(lowerBound, upperBound, precision, taskSize, budgetGroupSize);
                for (j = 0; j < taskValue.length; j++) {
                    for (k = 0; k < taskValue[0].length - 1; k++) {
                        bufferedWriter.write(taskValue[j][k]);
                        bufferedWriter.write(WRITING_SPLIT_TAG_IN_LINE);
                    }
                    bufferedWriter.write(taskValue[j][k]);
                    bufferedWriter.newLine();
                }
            }
        }  catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bufferedWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public static void generateWorkerNoiseDistanceDataSet(String outputPath, List<Point> workerPointList, List<Point> taskPointList, List<Double[]>[] workerPrivacyBudgetList, boolean isLongitudeLatitude) {
        BufferedWriter bufferedWriter = null;
        int workerSize = workerPointList.size();
        int taskSize = taskPointList.size();
        int budgetGroupSize = workerPrivacyBudgetList[0].get(0).length;
        Double tempRealDistance = null;
        Double[] tempBudget = null;
        Double[] tempNoiseDistance = null;
        try {
            File outputFile = new File(outputPath);
            File parentDir = outputFile.getParentFile();
            if (!parentDir.exists()) {
                parentDir.mkdirs();
            }
            bufferedWriter = new BufferedWriter(new FileWriter(outputFile));
            bufferedWriter.write(String.valueOf(workerSize) + WRITING_SPLIT_TAG_IN_LINE + String.valueOf(taskSize) + WRITING_SPLIT_TAG_IN_LINE + String.valueOf(budgetGroupSize));
            bufferedWriter.newLine();
            for (int i = 0, j, k; i < workerSize; i++) {
//                taskValue = BasicCalculation.getSortedRandomStringValueTwoDimensionArrayInRange(lowerBound, upperBound, precision, taskSize, budgetGroupSize);
                for (j = 0; j < taskSize; j++) {
                    if (!isLongitudeLatitude) {
                        tempRealDistance = BasicCalculation.get2Norm(workerPointList.get(i).getIndex(), taskPointList.get(j).getIndex());
                    } else {
                        tempRealDistance = BasicCalculation.getDistanceFrom2LngLat(workerPointList.get(i).getyIndex(), workerPointList.get(i).getxIndex(), taskPointList.get(j).getyIndex(), taskPointList.get(j).getxIndex());
                    }
                    tempBudget = workerPrivacyBudgetList[i].get(j);
                    tempNoiseDistance = LaplaceUtils.getLaplaceNoiseWithOriginalValue(tempRealDistance, tempBudget);

                    for (k = 0; k < tempNoiseDistance.length - 1; k++) {
                        bufferedWriter.write(String.valueOf(tempNoiseDistance[k]));
                        bufferedWriter.write(WRITING_SPLIT_TAG_IN_LINE);
                    }
                    bufferedWriter.write(String.valueOf(tempNoiseDistance[k]));
                    bufferedWriter.newLine();
                }
            }
        }  catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bufferedWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public static void generateTaskValuesWorkerRangesAndPrivacyBudgetFromTaskWorkerPoint(String parentDirPath) {
        String taskFileName = "\\task_point.txt";
        String workerFileName = "\\worker_point.txt";
        String outputTaskValueFileName = "\\task_value.txt";
        String outputWorkerRangeFileName = "\\worker_range.txt";
        String outputWorkerPrivacyBudgetFileName = "\\worker_budget.txt";

//        double valueLowerBound = 7000;
//        double valueUpperBound = 15000;
        double valueLowerBound = 10;
        double valueUpperBound = 200;
        int precision = 2;

        double rangeLowerBound = 10;
        double rangeUpperBound = 100;

        int budgetGroupSize = 7;
        double budgetGroupLowerBound = 1;
        double budgetGroupUpperBound = 20;

        Integer taskSize = PointRead.readPointSizeWithFirstLineCount(parentDirPath + taskFileName);
        Integer workerSize = PointRead.readPointSizeWithFirstLineCount(parentDirPath + workerFileName);

        generateTaskValuesDataSet(parentDirPath + outputTaskValueFileName, taskSize, valueLowerBound, valueUpperBound, precision);

        generateWorkerRangesDataSet(parentDirPath + outputWorkerRangeFileName, workerSize, rangeLowerBound, rangeUpperBound, precision);

        generateWorkerPrivacyBudgetDataSet(parentDirPath + outputWorkerPrivacyBudgetFileName, workerSize, taskSize, budgetGroupSize, budgetGroupLowerBound, budgetGroupUpperBound, precision);

    }

    public static void generateNoiseDistanceFromTaskWorkerPointAndPrivacyBudget(String parentDirPath, boolean isLongitudeLatitude) {
        String taskFileName = "\\task_point.txt";
        String workerFileName = "\\worker_point.txt";
        String workerPrivacyBudgetFileName = "\\worker_budget.txt";
        String workerNoiseDistanceFileName = "\\worker_noise_distance.txt";

        List<Point> taskPointList = PointRead.readPointWithFirstLineCount(parentDirPath + taskFileName);
        List<Point> workerPointList = PointRead.readPointWithFirstLineCount(parentDirPath + workerFileName);
        List<Double[]>[] workerPrivacyBudgetList = TwoDimensionDoubleRead.readDouble(parentDirPath + workerPrivacyBudgetFileName, 1);
        generateWorkerNoiseDistanceDataSet(parentDirPath + workerNoiseDistanceFileName, workerPointList, taskPointList, workerPrivacyBudgetList, isLongitudeLatitude);
    }


    public static void main(String[] args) {
//        String outputPath = System.getProperty("user.dir") + "\\dataset\\UniformDataPoint.txt";
//        DataSetGenerator dataSetGenerator = new DataSetGenerator();
//        dataSetGenerator.generateUniformPlaneDataPoint(200000, 10000, outputPath);


//        String outputPath = System.getProperty("user.dir") + "\\dataset\\NormalDataPoint.txt";
//        DataSetGenerator dataSetGenerator = new DataSetGenerator();
//        dataSetGenerator.generateNormalPlaneDataPoint(200000, 10000, 0, 10000000000L, outputPath);

//        String outputPath = "E:\\1.学习\\4.数据集\\dataset\\original\\chengdu\\task_value.txt";
//        DataSetGenerator.generateTaskValuesDataSet(outputPath, 1286, 0, 1, 6);

//        String outputPath = "E:\\1.学习\\4.数据集\\dataset\\original\\chengdu\\worker_privacy_budget.txt";
//        DataSetGenerator.generateWorkerPrivacyBudgetDataSet(outputPath, 28029, 1286, 5, 0, 10, 6);



//        String parentDirSYN = "E:\\1.学习\\4.数据集\\1.FourSquare-NYCandTokyoCheck-ins\\output\\SYN";
//        String parentDirTKY = "E:\\1.学习\\4.数据集\\1.FourSquare-NYCandTokyoCheck-ins\\output\\TKY";
//        MainDataSetGenerator.generateTaskValuesWorkerRangesAndPrivacyBudgetFromTaskWorkerPoint(parentDirSYN);
//        MainDataSetGenerator.generateTaskValuesWorkerRangesAndPrivacyBudgetFromTaskWorkerPoint(parentDirTKY);
//        boolean isLongitudeLatitude = true;
//        MainDataSetGenerator.generateNoiseDistanceFromTaskWorkerPointAndPrivacyBudget(parentDirSYN, isLongitudeLatitude);
//        MainDataSetGenerator.generateNoiseDistanceFromTaskWorkerPointAndPrivacyBudget(parentDirTKY, isLongitudeLatitude);


//        String parentDirSYN = "E:\\1.学习\\4.数据集\\1.FourSquare-NYCandTokyoCheck-ins\\output\\test";
        String parentDirSYN = "E:\\1.学习\\4.数据集\\dataset\\original\\chengdu_default";
        MainDataSetGenerator.generateTaskValuesWorkerRangesAndPrivacyBudgetFromTaskWorkerPoint(parentDirSYN);
//        boolean isLongitudeLatitude = true;
        boolean isLongitudeLatitude = false;
//        double factor = 0.001;
        MainDataSetGenerator.generateNoiseDistanceFromTaskWorkerPointAndPrivacyBudget(parentDirSYN, isLongitudeLatitude);

    }
}
