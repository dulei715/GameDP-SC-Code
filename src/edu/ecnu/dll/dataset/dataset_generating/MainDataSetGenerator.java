package edu.ecnu.dll.dataset.dataset_generating;

import edu.ecnu.dll.config.Constant;
import edu.ecnu.dll.dataset.dataset_generating.sample.SamplingFunction;
import tools.basic.BasicCalculation;
import tools.basic.StringUtil;
import tools.differential_privacy.noise.LaplaceUtils;
import tools.io.read.PointRead;
import tools.io.read.TwoDimensionDoubleRead;
import tools.io.write.BasicWrite;
import tools.struct.Point;

import java.io.*;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;

public class MainDataSetGenerator {

    public static final String WRITING_SPLIT_TAG_IN_LINE = " ";

    public static void generateUniformPlaneDataPoint(int dimensionLength, int pointSize, String outputPath) {
        double x, y;
        DecimalFormat decimalFormat = new DecimalFormat("0.000000");
        decimalFormat.setRoundingMode(RoundingMode.HALF_UP);
        BasicWrite basicWrite = new BasicWrite();
        basicWrite.startWriting(outputPath);
        basicWrite.writeOneLine(String.valueOf(pointSize));
        for (int i = 0; i < pointSize; i++) {
            x = Math.random() * dimensionLength;
            y = Math.random() * dimensionLength;
            basicWrite.writeOneLine(decimalFormat.format(x) + WRITING_SPLIT_TAG_IN_LINE + decimalFormat.format(y));
        }
        basicWrite.endWriting();
    }

    public static void generateUniformPlaneDataPointWithoutDuplication(int dimensionLength, int pointSize, String outputPath) {
        BasicWrite basicWrite = new BasicWrite();
        Set<String> lineSet = new HashSet<>();
        double x, y;
        String line;
        DecimalFormat decimalFormat = new DecimalFormat("0.000000");
        decimalFormat.setRoundingMode(RoundingMode.HALF_UP);

        while (lineSet.size() < pointSize) {
            x = Math.random() * dimensionLength;
            y = Math.random() * dimensionLength;
            line = decimalFormat.format(x) + WRITING_SPLIT_TAG_IN_LINE + decimalFormat.format(y);
            if (!lineSet.contains(line)) {
                lineSet.add(line);
            }
        }

        basicWrite.startWriting(outputPath);

        basicWrite.writeSizeAndCollectionDataWithNewLineSplit(new ArrayList(lineSet));
        basicWrite.endWriting();
    }

    public static void generateNormalPlaneDataPoint(int pointSize, double mean, double variance, String outputPath) {
        Random random = new Random();
        double x, y;
        DecimalFormat decimalFormat = new DecimalFormat("0.000000");
        decimalFormat.setRoundingMode(RoundingMode.HALF_UP);
        BasicWrite basicWrite = new BasicWrite();
        basicWrite.startWriting(outputPath);
        basicWrite.writeOneLine(String.valueOf(pointSize));
        for (int i = 0; i < pointSize; i++) {
            x = Math.sqrt(variance)*random.nextGaussian() + mean;
            y = Math.sqrt(variance)*random.nextGaussian() + mean;
            basicWrite.writeOneLine(decimalFormat.format(x) + WRITING_SPLIT_TAG_IN_LINE + decimalFormat.format(y));
        }
        basicWrite.endWriting();
    }

    public static void generateNormalPlaneDataPointWithoutDuplication(int pointSize, double mean, double variance, String outputPath) {
        Random random = new Random();
        BasicWrite basicWrite = new BasicWrite();
        Set<String> lineSet = new HashSet<>();
        double x, y;
        String line;
        DecimalFormat decimalFormat = new DecimalFormat("0.000000");
        decimalFormat.setRoundingMode(RoundingMode.HALF_UP);

        while (lineSet.size() < pointSize) {
            x = Math.sqrt(variance)*random.nextGaussian() + mean;
            y = Math.sqrt(variance)*random.nextGaussian() + mean;
            line = decimalFormat.format(x) + WRITING_SPLIT_TAG_IN_LINE + decimalFormat.format(y);
            if (!lineSet.contains(line)) {
                lineSet.add(line);
            }
        }

        basicWrite.startWriting(outputPath);
        basicWrite.writeSizeAndCollectionDataWithNewLineSplit(new ArrayList(lineSet));
        basicWrite.endWriting();

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

    public static void generateWorkerNoiseDistanceDataSet(String outputPath, List<Point> workerPointList, List<Point> taskPointList, List<Double[]>[] workerPrivacyBudgetList, boolean isLongitudeLatitude, boolean onlyPositiveNoiseDistance) {
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
                    tempNoiseDistance = LaplaceUtils.getLaplaceNoiseWithOriginalValue(tempRealDistance, tempBudget, onlyPositiveNoiseDistance);

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
        String taskFileName = Constant.FILE_PATH_SPLIT + "task_point.txt";
        String workerFileName = Constant.FILE_PATH_SPLIT + "worker_point.txt";
        String outputTaskValueFileName = Constant.FILE_PATH_SPLIT + "task_value.txt";
        String outputWorkerRangeFileName = Constant.FILE_PATH_SPLIT + "worker_range.txt";
        String outputWorkerPrivacyBudgetFileName = Constant.FILE_PATH_SPLIT + "worker_budget.txt";

//        double valueLowerBound = 7000;
//        double valueUpperBound = 15000;
        double valueLowerBound = 5;
        double valueUpperBound = 25;
        int precision = 2;

        double rangeLowerBound = 10;
        double rangeUpperBound = 100;

        int budgetGroupSize = 7;
//        double budgetGroupLowerBound = 1;
//        double budgetGroupUpperBound = 20;
        double budgetGroupLowerBound = 1;
        double budgetGroupUpperBound = 10;

        Integer taskSize = PointRead.readPointSizeWithFirstLineCount(parentDirPath + taskFileName);
        Integer workerSize = PointRead.readPointSizeWithFirstLineCount(parentDirPath + workerFileName);

        generateTaskValuesDataSet(parentDirPath + outputTaskValueFileName, taskSize, valueLowerBound, valueUpperBound, precision);

        generateWorkerRangesDataSet(parentDirPath + outputWorkerRangeFileName, workerSize, rangeLowerBound, rangeUpperBound, precision);

        generateWorkerPrivacyBudgetDataSet(parentDirPath + outputWorkerPrivacyBudgetFileName, workerSize, taskSize, budgetGroupSize, budgetGroupLowerBound, budgetGroupUpperBound, precision);

    }

    public static void generatePrivacyBudgetFromTaskWorkerPoint(String parentDirPath, double[] budgetBound, int budgetGroupSize) {
        String taskFileName = Constant.FILE_PATH_SPLIT + "task_point.txt";
        String workerFileName = Constant.FILE_PATH_SPLIT + "worker_point.txt";
        String outputTaskValueFileName = Constant.FILE_PATH_SPLIT + "task_value.txt";
        String outputWorkerRangeFileName = Constant.FILE_PATH_SPLIT + "worker_range.txt";
        String outputWorkerPrivacyBudgetFileName = Constant.FILE_PATH_SPLIT + "worker_budget.txt";

        int precision = 2;


        double budgetGroupLowerBound = budgetBound[0];
        double budgetGroupUpperBound = budgetBound[1];

        Integer taskSize = PointRead.readPointSizeWithFirstLineCount(parentDirPath + taskFileName);
        Integer workerSize = PointRead.readPointSizeWithFirstLineCount(parentDirPath + workerFileName);
        generateWorkerPrivacyBudgetDataSet(parentDirPath + outputWorkerPrivacyBudgetFileName, workerSize, taskSize, budgetGroupSize, budgetGroupLowerBound, budgetGroupUpperBound, precision);

    }

    public static void generateTaskValuesWorkerRangesAndPrivacyBudgetFromTaskWorkerPoint(String parentDirPath, double[] valueBound, double[] rangeBound, double[] budgetBound, int budgetGroupSize) {
        String taskFileName = Constant.FILE_PATH_SPLIT + "task_point.txt";
        String workerFileName = Constant.FILE_PATH_SPLIT + "worker_point.txt";
        String outputTaskValueFileName = Constant.FILE_PATH_SPLIT + "task_value.txt";
        String outputWorkerRangeFileName = Constant.FILE_PATH_SPLIT + "worker_range.txt";
        String outputWorkerPrivacyBudgetFileName = Constant.FILE_PATH_SPLIT + "worker_budget.txt";

//        double valueLowerBound = 7000;
//        double valueUpperBound = 15000;
        double valueLowerBound = valueBound[0];
        double valueUpperBound = valueBound[1];
        int precision = 2;

        double rangeLowerBound = rangeBound[0];
        double rangeUpperBound = rangeBound[1];

//        int budgetGroupSize = 7;
//        double budgetGroupLowerBound = 1;
//        double budgetGroupUpperBound = 20;
        double budgetGroupLowerBound = budgetBound[0];
        double budgetGroupUpperBound = budgetBound[1];

        Integer taskSize = PointRead.readPointSizeWithFirstLineCount(parentDirPath + taskFileName);
        Integer workerSize = PointRead.readPointSizeWithFirstLineCount(parentDirPath + workerFileName);

        generateTaskValuesDataSet(parentDirPath + outputTaskValueFileName, taskSize, valueLowerBound, valueUpperBound, precision);

        generateWorkerRangesDataSet(parentDirPath + outputWorkerRangeFileName, workerSize, rangeLowerBound, rangeUpperBound, precision);

        generateWorkerPrivacyBudgetDataSet(parentDirPath + outputWorkerPrivacyBudgetFileName, workerSize, taskSize, budgetGroupSize, budgetGroupLowerBound, budgetGroupUpperBound, precision);

    }

    public static void generateNoiseDistanceFromTaskWorkerPointAndPrivacyBudget(String parentDirPath, boolean isLongitudeLatitude, boolean onlyPositiveNoiseDistance) {
        String taskFileName = Constant.FILE_PATH_SPLIT + "task_point.txt";
        String workerFileName = Constant.FILE_PATH_SPLIT + "worker_point.txt";
        String workerPrivacyBudgetFileName = Constant.FILE_PATH_SPLIT + "worker_budget.txt";
        String workerNoiseDistanceFileName = Constant.FILE_PATH_SPLIT + "worker_noise_distance.txt";

        List<Point> taskPointList = PointRead.readPointWithFirstLineCount(parentDirPath + taskFileName);
        List<Point> workerPointList = PointRead.readPointWithFirstLineCount(parentDirPath + workerFileName);
        List<Double[]>[] workerPrivacyBudgetList = TwoDimensionDoubleRead.readDouble(parentDirPath + workerPrivacyBudgetFileName, 1);
        generateWorkerNoiseDistanceDataSet(parentDirPath + workerNoiseDistanceFileName, workerPointList, taskPointList, workerPrivacyBudgetList, isLongitudeLatitude, onlyPositiveNoiseDistance);
    }



    public static void main1(String[] args) {
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
        String basicPath = "E:\\1.学习\\4.数据集\\dataset\\original\\chengdu_total_dataset_km\\";
        String[] inputParentPath = new String[]{
                "task_worker_1_1_0",
                "task_worker_1_1_5",
                "task_worker_1_2_0",
                "task_worker_1_2_5",
                "task_worker_1_3_0",
                "task_worker_1_3_5"
        };
//        String[] outputPath = new String[]{
//                "task_worker_1_1_0\\worker_point.txt",
//                "task_worker_1_1_5\\worker_point.txt",
//                "task_worker_1_2_0\\worker_point.txt",
//                "task_worker_1_2_5\\worker_point.txt",
//                "task_worker_1_3_0\\worker_point.txt",
//                "task_worker_1_3_5\\worker_point.txt",
//        };
        boolean isLongitudeLatitude = false;
        for (int i = 0; i < inputParentPath.length; i++) {
            MainDataSetGenerator.generateTaskValuesWorkerRangesAndPrivacyBudgetFromTaskWorkerPoint(basicPath + inputParentPath[i]);
            MainDataSetGenerator.generateNoiseDistanceFromTaskWorkerPointAndPrivacyBudget(basicPath + inputParentPath[i], isLongitudeLatitude, false);
        }
//        boolean isLongitudeLatitude = true;
//        double factor = 0.001;

    }
    public static void main0(String[] args) {


//        String basicPath = "E:\\1.学习\\4.数据集\\dataset\\original\\nyc_total_dataset_ll\\";
//        String basicPath = "E:\\1.学习\\4.数据集\\dataset\\original\\tky_total_dataset_ll\\";
        String basicPath = args[0] + Constant.FILE_PATH_SPLIT;
        String[] inputParentPath = new String[]{
                "task_worker_1_1_0",
                "task_worker_1_1_5",
                "task_worker_1_2_0",
                "task_worker_1_2_5",
                "task_worker_1_3_0",
//                "task_worker_1_3_5"
        };
//        String[] outputPath = new String[]{
//                "task_worker_1_1_0\\worker_point.txt",
//                "task_worker_1_1_5\\worker_point.txt",
//                "task_worker_1_2_0\\worker_point.txt",
//                "task_worker_1_2_5\\worker_point.txt",
//                "task_worker_1_3_0\\worker_point.txt",
//                "task_worker_1_3_5\\worker_point.txt",
//        };
        boolean isLongitudeLatitude = false;
        String dataType = args[1];
        if ("1".equals(dataType)) {
            isLongitudeLatitude = true;
        }
//        double[] valueBound = new double[]{5,25};
        double[] valueBound = new double[]{Double.valueOf(args[2]), Double.valueOf(args[3])};
//        double[] rangeBound = new double[]{0.5,2};
//        double[] rangeBound = new double[]{10,30};
        double[] rangeBound = new double[]{Double.valueOf(args[4]), Double.valueOf(args[5])};
//        double[] budgetBound = new double[]{1,10};
        double[] budgetBound = new double[]{Double.valueOf(args[6]), Double.valueOf(args[7])};
//        int budgetGroupSize = 7;
        int budgetGroupSize = Integer.valueOf(args[8]);
        for (int i = 0; i < inputParentPath.length; i++) {
            MainDataSetGenerator.generateTaskValuesWorkerRangesAndPrivacyBudgetFromTaskWorkerPoint(basicPath + inputParentPath[i], valueBound, rangeBound, budgetBound, budgetGroupSize);
            MainDataSetGenerator.generateNoiseDistanceFromTaskWorkerPointAndPrivacyBudget(basicPath + inputParentPath[i], isLongitudeLatitude, false);
        }
//        boolean isLongitudeLatitude = true;
//        double factor = 0.001;

    }


    // 生成uniform的数据
    public static void main_u(String[] args) {


//        String basicPath = "E:\\1.学习\\4.数据集\\dataset\\original\\nyc_total_dataset_ll\\";
        String basicPath = "F:\\test\\";
//        String basicPath = args[0] + Constant.FILE_PATH_SPLIT;

        //生成原始数据集
        for (int i = 0; i < 300; i++) {
            MainDataSetGenerator.generateUniformPlaneDataPoint(100, 1000, basicPath + "batch_" + StringUtil.getFixIndexNumberInteger(i+1, Constant.subNamePositionSize) + "" +
                    "_task_point.txt");
            MainDataSetGenerator.generateUniformPlaneDataPoint(100, 3000, basicPath + "batch_" + StringUtil.getFixIndexNumberInteger(i+1, Constant.subNamePositionSize) + "" +
                    "_worker_point.txt");
        }



    }

    // 生成 normal的数据
    public static void main_n(String[] args) {


//        String basicPath = "E:\\1.学习\\4.数据集\\dataset\\original\\nyc_total_dataset_ll\\";
        String basicPath = "E:\\test\\";
//        String basicPath = args[0] + Constant.FILE_PATH_SPLIT;

        //生成原始数据集
        for (int i = 0; i < 300; i++) {
            MainDataSetGenerator.generateNormalPlaneDataPoint(1000, 0, 150, basicPath + "batch_" + StringUtil.getFixIndexNumberInteger(i+1, Constant.subNamePositionSize) + "" +
                    "_task_point.txt");
            MainDataSetGenerator.generateNormalPlaneDataPoint(3000, 0, 150, basicPath + "batch_" + StringUtil.getFixIndexNumberInteger(i+1, Constant.subNamePositionSize) + "" +
                    "_worker_point.txt");
        }



    }

    public static void main(String[] args) {
        String basicPath = "E:\\debug\\task_worker_1_2_0\\";
        String taskPointPath = basicPath + "batch_001_task_point.txt";
        String workerPointPath = basicPath + "batch_001_worker_point.txt";
        String workerBudgetPath = basicPath + "batch_001_worker_budget.txt";

        String workerNoiseDistanceOutputPath = basicPath + "batch_001_worker_noise_distance.txt";

        List<Point> taskPointList = PointRead.readPointWithFirstLineCount(taskPointPath);
        List<Point> workerPointList = PointRead.readPointWithFirstLineCount(workerPointPath);
        List<Double[]>[] budgetListArray = TwoDimensionDoubleRead.readDouble(workerBudgetPath, 1);
        MainDataSetGenerator.generateWorkerNoiseDistanceDataSet(workerNoiseDistanceOutputPath, workerPointList, taskPointList, budgetListArray, false, false);
    }



}
