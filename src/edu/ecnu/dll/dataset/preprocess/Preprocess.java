package edu.ecnu.dll.dataset.preprocess;

import tools.basic.StringUtil;
import tools.io.read.CSVRead;
import tools.io.read.OrderRead;
import tools.io.read.PointRead;
import tools.io.read.TaxiRead;
import tools.io.write.PointWrite;
import tools.struct.Order;
import tools.struct.Point;
import tools.struct.Taxi;

import java.io.*;
import java.nio.file.Files;
import java.util.*;

public class Preprocess {

    public static void multipleDataWithFirstLineUnchanged(String inputPath, String outputPath, Double factorK, Double constA, String split) {
        BufferedReader bufferedReader = null;
        BufferedWriter bufferedWriter = null;
        String readLine, writeLine;
        try {
            bufferedReader = new BufferedReader(new FileReader(inputPath));
            File outputFile = new File(outputPath);
            File outputParentFile = outputFile.getParentFile();
            if (!outputParentFile.exists()) {
                outputParentFile.mkdirs();
            }
            if (!outputFile.exists()) {
                outputFile.createNewFile();
            }
            bufferedWriter = new BufferedWriter(new FileWriter(outputFile));
            readLine = bufferedReader.readLine();
            bufferedWriter.write(readLine);
            bufferedWriter.newLine();

            String[] splitLine;
            Double[] values;
            Double tempValue;
            while ((readLine = bufferedReader.readLine()) != null) {
                readLine = readLine.trim();
                splitLine = readLine.split(split);
                values = new Double[splitLine.length];
                for (int i = 0; i < splitLine.length; i++) {
                    tempValue = Double.valueOf(splitLine[i]);
                    tempValue = tempValue * factorK + constA;
                    values[i] = tempValue;
                }
                writeLine = StringUtil.concat(split, values);
                bufferedWriter.write(writeLine);
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

    public static void extractChengduDataToDataset(String inputDataParentPath, String outputParentPath) {
        String nodeFileName = "\\chengdu.node";
        String taxiFileName = "\\chengdu_taxi.txt";
        String orderFileName = "\\chengdu_order.txt";

        String taskPointOutputFileName = "\\task_point.txt";
        String workerPointOutputFileName = "\\worker_point.txt";

        PointWrite pointWrite = new PointWrite();

        List<Point> nodeList = PointRead.readPointWithFirstLineCount(inputDataParentPath + nodeFileName);
        List<Order> orderList = OrderRead.readOrderWithFirstLineCount(inputDataParentPath + orderFileName);
        Set<Point> writingPointSet = new HashSet<>();

        Integer pointID;
        for (Order order : orderList) {
            pointID = order.getBeginPointID();
            writingPointSet.add(nodeList.get(pointID));
        }

        pointWrite.startWriting(outputParentPath + taskPointOutputFileName);
        pointWrite.writePoint(writingPointSet);
        pointWrite.endWriting();

        orderList = null;
        writingPointSet.clear();
        System.gc();

        writingPointSet = new HashSet<>();
        List<Taxi> taxiList = TaxiRead.readTaxi(inputDataParentPath + taxiFileName);
        for (Taxi taxi : taxiList) {
            pointID = taxi.getPointID();
            writingPointSet.add(nodeList.get(pointID));
        }

        pointWrite.startWriting(outputParentPath + workerPointOutputFileName);
        pointWrite.writePoint(writingPointSet);
        pointWrite.endWriting();

    }

    public static void extractTSMCNYCAndTKYToDataset(String inputDataParentPath, String outputParentPath) {
        String nycFileName = "\\dataset_TSMC2014_NYC.csv";
        String tkyFileName = "\\dataset_TSMC2014_TKY.csv";

        String sncSubParentOutputDir = "\\SYN";
        String tkySubParentOutputDir = "\\TKY";

        String taskPointOutputFileName = "\\task_point.txt";
        String workerPointOutputFileName = "\\worker_point.txt";

        String effectiveCharacter = "venueCategory";
        String taskCharacterValue = "Subway";
        String workerCharacterValue = "Office";
        String xIndexCharacterName = "latitude";
        String yIndexCharacterName = "longitude";

        String tempValue;
        PointWrite pointWrite = new PointWrite();

//        List<Point> taskPointCollection, workerPointCollection;
//        taskPointCollection = new ArrayList<>();
//        workerPointCollection = new ArrayList<>();
        Set<Point> taskPointCollection, workerPointCollection;
        Set<Point>[] taskWorkerPointSetArray;
        taskPointCollection = new HashSet<>();
        workerPointCollection = new HashSet<>();

        taskWorkerPointSetArray = readAndFilterData(inputDataParentPath, nycFileName, effectiveCharacter, taskCharacterValue, workerCharacterValue, xIndexCharacterName, yIndexCharacterName);
        taskPointCollection = taskWorkerPointSetArray[0];
        workerPointCollection = taskWorkerPointSetArray[1];

        pointWrite.startWriting(outputParentPath + sncSubParentOutputDir + taskPointOutputFileName);
        pointWrite.writePoint(taskPointCollection);
        pointWrite.endWriting();

        pointWrite.startWriting(outputParentPath + sncSubParentOutputDir + workerPointOutputFileName);
        pointWrite.writePoint(workerPointCollection);
        pointWrite.endWriting();


        taskWorkerPointSetArray = readAndFilterData(inputDataParentPath, tkyFileName, effectiveCharacter, taskCharacterValue, workerCharacterValue, xIndexCharacterName, yIndexCharacterName);
        taskPointCollection = taskWorkerPointSetArray[0];
        workerPointCollection = taskWorkerPointSetArray[1];

        pointWrite.startWriting(outputParentPath + tkySubParentOutputDir + taskPointOutputFileName);
        pointWrite.writePoint(taskPointCollection);
        pointWrite.endWriting();

        pointWrite.startWriting(outputParentPath + tkySubParentOutputDir + workerPointOutputFileName);
        pointWrite.writePoint(workerPointCollection);
        pointWrite.endWriting();

//        System.out.println(nycData.size());
//        System.out.println(taskPointCollection.size());
//        System.out.println(workerPointCollection.size());

    }

    protected static HashSet<Point>[] readAndFilterData(String inputDataParentPath, String dataFileName, String effectiveCharacter, String taskCharacterValue, String workerCharacterValue, String xIndexCharacterName, String yIndexCharacterName) {
        String tempValue;
        List<Map<String, String>> nycData = CSVRead.readData(inputDataParentPath + dataFileName);
        Point tempPoint;
        HashSet<Point> taskPointCollection = new HashSet<>(), workerPointCollection = new HashSet<>();
        for (Map<String, String> nycDatum : nycData) {
            tempValue = nycDatum.get(effectiveCharacter).trim();
            if (taskCharacterValue.equalsIgnoreCase(tempValue)) {
                tempPoint = new Point(Double.valueOf(nycDatum.get(xIndexCharacterName)), Double.valueOf(nycDatum.get(yIndexCharacterName)));
                taskPointCollection.add(tempPoint);
//                System.out.println(tempValue + ": " + tempPoint);
            } else if (workerCharacterValue.equalsIgnoreCase(tempValue)) {
                tempPoint = new Point(Double.valueOf(nycDatum.get(xIndexCharacterName)), Double.valueOf(nycDatum.get(yIndexCharacterName)));
                workerPointCollection.add(tempPoint);
//                System.out.println(tempValue + ": " + tempPoint);
            }
        }
        return new HashSet[]{taskPointCollection, workerPointCollection};
    }

    public static void extractRandomPointByGivenSize(String inputPath, String outputPath, int size) {
        List<Point> points = PointRead.readPointWithFirstLineCount(inputPath);
        int pointSize = points.size();
        int interval = pointSize / size;
        List<Point> newPoints = new ArrayList<>(size);
        for (int i = 0, k = 0; i < pointSize && k < size; i+=interval, ++k) {
            newPoints.add(points.get(i));
        }
        PointWrite pointWrite = new PointWrite();
        pointWrite.startWriting(outputPath);
        pointWrite.writePoint(newPoints);
        pointWrite.endWriting();
    }


    public static void main(String[] args) {
//        String inputParentPath = "E:\\1.学习\\4.数据集\\dataset\\original";
//        String outputParentPath = "E:\\1.学习\\4.数据集\\dataset\\original\\chengdu";
//        extractChengduDataToDataset(inputParentPath, outputParentPath);

//        String parentInputPath = "E:\\1.学习\\4.数据集\\1.FourSquare-NYCandTokyoCheck-ins";
//        String parentOutputPath = parentInputPath + "\\output";
//        Preprocess.extractTSMCNYCAndTKYToDataset(parentInputPath, parentOutputPath);

        String inputPath = "E:\\1.学习\\4.数据集\\dataset\\original\\chengdu\\worker_point.txt";
        String outputBasic = "E:\\1.学习\\4.数据集\\dataset\\original\\chengdu_total_dataset\\";
        String[] outputPath = new String[]{
                "task_worker_1_1_0\\worker_point.txt",
                "task_worker_1_1_5\\worker_point.txt",
                "task_worker_1_2_0\\worker_point.txt",
                "task_worker_1_2_5\\worker_point.txt",
                "task_worker_1_3_0\\worker_point.txt",
                "task_worker_1_3_5\\worker_point.txt",
        };
        double[] scales = new double[] {
                1, 1.5, 2, 2.5, 3, 3.5
        };
        int taskSize = 1286;
        double tempScale;
        int workerSize;
        for (int i = 0; i < scales.length; i++) {
            tempScale = scales[i];
            workerSize = (int)(taskSize*tempScale);
            extractRandomPointByGivenSize(inputPath, outputBasic + outputPath[i], workerSize);
        }
//        System.out.println(workerSize);
    }

}
