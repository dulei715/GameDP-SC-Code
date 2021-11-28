package edu.ecnu.dll.scheme.scheme_compared.solution._1_non_privacy;

import edu.ecnu.dll.basic.basic_struct.pack.CostMatchingInfo;
import edu.ecnu.dll.basic.basic_struct.pack.UtilityDistanceIDPair;
import edu.ecnu.dll.run.result_tools.CommonFunction;
import edu.ecnu.dll.run.result_tools.TargetTool;
import edu.ecnu.dll.basic.basic_solution.NonPrivacySolution;
import tools.basic.BasicArray;
import tools.basic.BasicCalculation;
import tools.basic.MatrixArray;
import tools.io.read.DoubleRead;
import tools.io.read.PointRead;
import tools.struct.Point;

import java.util.*;

public class BestNonePrivacySolution extends NonPrivacySolution {

    private double[][] utilityMatrix = null;

    @Override
    public void initializeAgents() {
        this.utilityMatrix = new double[this.tasks.length][this.workers.length];
        for (int j = 0; j < this.workers.length; j++) {
            this.workers[j].reverseIndex = new ArrayList<>();
            this.workers[j].toTaskDistance = new ArrayList<>();
            // 此处没有初始化privacyBudgetArray，因为会在initialize basic function 中初始化
            this.workers[j].currentUtilityFunctionValue = new ArrayList<>();
            double tempDistance, utility;
            int k = 0;
            for (int i = 0; i < tasks.length; i++) {
                // todo: 修改为经纬度的
                tempDistance = BasicCalculation.get2Norm(this.tasks[i].location, this.workers[j].location);
                if (tempDistance <= this.workers[j].maxRange) {
                    this.workers[j].taskIndex[i] = k++;
                    this.workers[j].reverseIndex.add(i);
                    this.workers[j].toTaskDistance.add(tempDistance);
                    utility = getUtilityValue(this.tasks[i].valuation, tempDistance);
                    if (utility > 0) {
                        this.workers[j].currentUtilityFunctionValue.add(utility);
                        utilityMatrix[i][j] = utility;
                    } else {
                        this.workers[j].currentUtilityFunctionValue.add(0.0);
                        utilityMatrix[i][j] = 0;
                    }
                } else {
                    utilityMatrix[i][j] = 0;
                }
            }
        }
    }


    @Override
    public void initializeAgentsWithLatitudeLongitude() {
        this.utilityMatrix = new double[this.tasks.length][this.workers.length];
        for (int j = 0; j < this.workers.length; j++) {
            this.workers[j].reverseIndex = new ArrayList<>();
            this.workers[j].toTaskDistance = new ArrayList<>();
            // 此处没有初始化privacyBudgetArray，因为会在initialize basic function 中初始化
            this.workers[j].currentUtilityFunctionValue = new ArrayList<>();
            double tempDistance, utility;
            int k = 0;
            for (int i = 0; i < tasks.length; i++) {
                // todo: 修改为经纬度的
                tempDistance = BasicCalculation.getDistanceFrom2LngLat(this.tasks[i].location[1], this.tasks[i].location[0], this.workers[j].location[1],this.workers[j].location[0]);
                if (tempDistance <= this.workers[j].maxRange) {
                    this.workers[j].taskIndex[i] = k++;
                    this.workers[j].reverseIndex.add(i);
                    this.workers[j].toTaskDistance.add(tempDistance);
                    utility = getUtilityValue(this.tasks[i].valuation, tempDistance);
                    if (utility > 0) {
                        this.workers[j].currentUtilityFunctionValue.add(utility);
                        utilityMatrix[i][j] = utility;
                    } else {
                        this.workers[j].currentUtilityFunctionValue.add(0.0);
                        utilityMatrix[i][j] = 0;
                    }
                } else {
                    utilityMatrix[i][j] = 0;
                }
            }
        }
    }

    private static double[][] getPreHandledMatrix(double[][] matrix, boolean isMinimized, Double maxMatrixValue) {
        double[][] resultMatrix;
        int[] matrixSize = MatrixArray.getMatrixSize(matrix);
        int taskSize = matrixSize[0];
        int workerSize = matrixSize[1];
        int maxNumber = taskSize > workerSize ? taskSize : workerSize;

        double paramMulti = 1;
        double paramAdd = 0;

        resultMatrix = new double[maxNumber][maxNumber];

        if (!isMinimized) {
            paramMulti = -1;
            paramAdd = maxMatrixValue;
        }
        MatrixArray.linearHandleMatrix(matrix, resultMatrix, 0.0, paramMulti, paramAdd);
        return resultMatrix;
    }

    public static double getCost(double[][] matrix, int[][] state, boolean isMinimized, Double originalMaxMatrixValue) {
        if (isMinimized) {
            return MatrixArray.getMatrixElementMultipleResult(matrix, state);
        }
        return MatrixArray.getMatrixLinearChangeElementMultipleResult(matrix, state, -1, originalMaxMatrixValue);
    }


    public static double getMinimalValueFromMatrixExcludingRowIndexListAndColIndexList(double[][] data, List<Integer> rowList, List<Integer> colList) {
        double result = Double.MAX_VALUE;
        Set<Integer> rowSet = new HashSet<>(rowList);
        Set<Integer> colSet = new HashSet<>(colList);
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                if (rowSet.contains(i) || colSet.contains(j)) {
                    continue;
                }
                if (data[i][j] < result) {
                    result = data[i][j];
                }
            }
        }
        return result;
    }


    public static List<Integer> generateRemainIndexList(List<Integer> indexList, Integer maxIndex) {
        Set<Integer> resultSet = new HashSet<>();
        List<Integer> resultList;
        Set<Integer> indexSet = new HashSet<>(indexList);
        for (int i = 0; i <= maxIndex; i++) {
            if (indexSet.contains(i)) {
                continue;
            }
            resultSet.add(i);
        }
        resultList = new ArrayList<>(resultSet);
        return resultList;
    }


    public static void minusMinimalValueForEachRow(double[][] matrix) {
        Double tempMinimalValue;
        for (int i = 0; i < matrix.length; i++) {
            tempMinimalValue = MatrixArray.getRowMinimalValue(matrix, i);
            for (int j = 0; j < matrix[i].length; j++) {
                matrix[i][j] -= tempMinimalValue;
            }
        }
    }

    public static void minusMinimalValueForEachCol(double[][] matrix) {
        Double tempMinimalValue;
        for (int j = 0; j < matrix[0].length; j++) {
            tempMinimalValue = MatrixArray.getColMinimalValue(matrix, j);
            for (int i = 0; i < matrix.length; i++) {
                matrix[i][j] -= tempMinimalValue;
            }
        }
    }





    public static CostMatchingInfo hungarian(double[][] paramMatrix, boolean isMinimized) {

        double cost = 0.0;
        int[][] resultState;

        int taskSize = paramMatrix.length;
        int workerSize = paramMatrix[0].length;

        int matrixSize = taskSize > workerSize ? taskSize : workerSize;

        Double maxMatrixValue = null;
        if (!isMinimized) {
            maxMatrixValue = MatrixArray.getMaxValue(paramMatrix);
        }

        double[][] handleDataMatrix = getPreHandledMatrix(paramMatrix, isMinimized, maxMatrixValue);
        double[][] originalHandleDataMatrix = MatrixArray.generateSubMatrix(handleDataMatrix, handleDataMatrix.length, handleDataMatrix[0].length);

        minusMinimalValueForEachRow(handleDataMatrix);
        minusMinimalValueForEachCol(handleDataMatrix);

        while (true) {
//            int[][] stateMatrix = getIntegerMatrixWithDeclaredSize(getMatrixSize(resultMatrix), 0);
            int[][] stateMatrix = MatrixArray.getResultStateWhetherEqualsToDeclaredValue(handleDataMatrix, 0);
            int independentZeroSize = 0;
            while (true) {
                int[] rowZeroNumber = MatrixArray.getRowCountEqualDeclaredValue(stateMatrix, 1);
                List<Integer> onlyOneZeroRowNumberIndexList = BasicArray.getDeclaredValueIndexList(rowZeroNumber, 1);
                if (!onlyOneZeroRowNumberIndexList.isEmpty()) {
                    int onlyOneZeroRowNumberIndex = onlyOneZeroRowNumberIndexList.get(0);
                    List<Integer> colIndexList = MatrixArray.getColIndexListWithDeclareRowIndexAndValue(stateMatrix, onlyOneZeroRowNumberIndex, 1);
                    MatrixArray.setValue(stateMatrix, onlyOneZeroRowNumberIndex, colIndexList, 2);
                    List<Integer> rowIndexList = MatrixArray.getRowIndexListWithDeclareColIndexAndValue(stateMatrix, colIndexList.get(0), 1);
                    // colIndexList只会有一个元素
                    MatrixArray.setValue(stateMatrix, rowIndexList, colIndexList, -2);
                    ++ independentZeroSize;

                } else {
                    int [] colZeroNumber = MatrixArray.getColCountEqualDeclaredValue(stateMatrix, 1);
                    List<Integer> onlyOneZeroColNumberIndexList = BasicArray.getDeclaredValueIndexList(colZeroNumber, 1);
                    if (!onlyOneZeroColNumberIndexList.isEmpty()) {
                        int onlyOneZeroColNumberIndex = onlyOneZeroColNumberIndexList.get(0);
                        List<Integer> rowIndexList = MatrixArray.getRowIndexListWithDeclareColIndexAndValue(stateMatrix, onlyOneZeroColNumberIndex, 1);
                        MatrixArray.setValue(stateMatrix, rowIndexList, onlyOneZeroColNumberIndex, 2);
                        List<Integer> colIndexList = MatrixArray.getColIndexListWithDeclareRowIndexAndValue(stateMatrix, rowIndexList.get(0), 1);
                        MatrixArray.setValue(stateMatrix, rowIndexList, colIndexList, -2);
                        ++ independentZeroSize;
                    } else {
                        break;
                    }
                }
            }

            if (independentZeroSize == matrixSize) {
                int[][] resultStateMatrix = MatrixArray.getResultStateWhetherEqualsToDeclaredValue(stateMatrix, 2);
                cost = getCost(originalHandleDataMatrix, resultStateMatrix, isMinimized, maxMatrixValue);
                resultState = MatrixArray.generateSubMatrix(resultStateMatrix, taskSize, workerSize);
                break;
            } else {
                int[] rowCountResult = MatrixArray.getRowCountEqualDeclaredValue(stateMatrix, 2);
//                getRowIndexListWithDeclareColIndexAndValue()
                List<Integer> rowIndexList = BasicArray.getDeclaredValueIndexList(rowCountResult, 0);
                List<Integer> rowRowIndexList = new ArrayList<>(rowIndexList);
                List<Integer> colColIndexList = new ArrayList<>();
                while (true) {
                    List<Integer> colIndexList = MatrixArray.getColIndexListWithDeclareRowIndexAndValue(stateMatrix, rowRowIndexList, -2);
                    if (colIndexList.isEmpty()) {
                        break;
                    }
                    rowRowIndexList = MatrixArray.getRowIndexListWithDeclareColIndexAndValue(stateMatrix, colIndexList, 2);
                    rowIndexList.addAll(rowRowIndexList);
                    colColIndexList.addAll(colIndexList);
                }
                List<Integer> remainRowIndexList = generateRemainIndexList(rowIndexList, matrixSize - 1);
                double minUnOverLapValue = getMinimalValueFromMatrixExcludingRowIndexListAndColIndexList(handleDataMatrix, remainRowIndexList, colColIndexList);
                MatrixArray.addValue(handleDataMatrix, -1, -1, -minUnOverLapValue);
                MatrixArray.addValue(handleDataMatrix, remainRowIndexList, -1, minUnOverLapValue);
                MatrixArray.addValue(handleDataMatrix, -1, colColIndexList, minUnOverLapValue);
            }
        }

        return new CostMatchingInfo(cost, resultState);
    }


    public static CostMatchingInfo hungarianEnhanced(double[][] paramMatrix, boolean isMinimized) {

        double cost = 0.0;

        int taskSize = paramMatrix.length;
        int workerSize = paramMatrix[0].length;

        int matrixSize = taskSize > workerSize ? taskSize : workerSize;


        Double maxMatrixValue = null;
        if (!isMinimized) {
            maxMatrixValue = MatrixArray.getMaxValue(paramMatrix);
        }

        double[][] handleDataMatrix = getPreHandledMatrix(paramMatrix, isMinimized, maxMatrixValue);
        double[][] originalHandleDataMatrix = MatrixArray.generateSubMatrix(handleDataMatrix, handleDataMatrix.length, handleDataMatrix[0].length);

        minusMinimalValueForEachRow(handleDataMatrix);
        minusMinimalValueForEachCol(handleDataMatrix);

        List<Integer> indexX = new ArrayList<>();
        List<Integer> indexY = new ArrayList<>();
        List<Integer> sRow = new ArrayList<>();
        List<Integer> sCol = new ArrayList<>();


        int[][] resultStateMatrix = null;

        while (true) {

            resultStateMatrix = MatrixArray.generateMatrixWithDeclaredValue(0, matrixSize, matrixSize);
            int nLines = 0;

//            int[][] stateMatrix = getIntegerMatrixWithDeclaredSize(getMatrixSize(resultMatrix), 0);
            int[][] stateMatrix = MatrixArray.getResultStateWhetherEqualsToDeclaredValue(handleDataMatrix, 0);
            indexX.clear();
            indexY.clear();
            sRow.clear();
            sCol.clear();

            while (true) {
                int[] rowZeroNumber = MatrixArray.getRowCountEqualDeclaredValue(stateMatrix, 1);
                int[] colZeroNumber = MatrixArray.getColCountEqualDeclaredValue(stateMatrix, 1);
                MatrixArray.setArrayToDeclaredValueByIndexList(rowZeroNumber, sRow, 0);
                MatrixArray.setArrayToDeclaredValueByIndexList(colZeroNumber, sCol, 0);
                List<Integer> trc = MatrixArray.getCombinedListOfTwoIntegerArray(rowZeroNumber, colZeroNumber);
                int index = MatrixArray.getIndexOfMinimalValueGreaterThanDeclaredValue(trc, 0);

                if (index == -1) {
                    break;
                }

                if (index >= matrixSize) {
                    // index 指向的是列
                    List<Integer> rowIndexList = MatrixArray.getRowIndexListWithDeclareColIndexAndValue(stateMatrix, index - matrixSize, 1);
                    Integer rowIndexFirst = rowIndexList.get(0);
                    MatrixArray.setValue(stateMatrix, rowIndexFirst, -1, 0);
                    ++ nLines;
                    indexY.add(rowIndexFirst);
                    MatrixArray.setValue(resultStateMatrix, rowIndexFirst, index - matrixSize, 1);
                    sCol.add(index - matrixSize);
                } else {
                    // index 指向的是行
                    List<Integer> colIndexList = MatrixArray.getColIndexListWithDeclareRowIndexAndValue(stateMatrix, index, 1);
                    Integer colIndexFirst = colIndexList.get(0);
                    MatrixArray.setValue(stateMatrix, -1, colIndexFirst, 0);
                    ++ nLines;
                    indexX.add(colIndexFirst);
                    MatrixArray.setValue(resultStateMatrix, index, colIndexFirst, 1);
                    sRow.add(index);
                }

            }

            if (nLines == matrixSize) {
                cost = getCost(originalHandleDataMatrix, resultStateMatrix, isMinimized, maxMatrixValue);
                resultStateMatrix = MatrixArray.generateSubMatrix(resultStateMatrix, taskSize, workerSize);
                break;
            } else {

                double minUnOverLapValue = getMinimalValueFromMatrixExcludingRowIndexListAndColIndexList(handleDataMatrix, indexY, indexX);
                MatrixArray.addValue(handleDataMatrix, -1, -1, -minUnOverLapValue);
                MatrixArray.addValue(handleDataMatrix, indexY, -1, minUnOverLapValue);
                MatrixArray.addValue(handleDataMatrix, -1, indexX, minUnOverLapValue);
            }
        }

        return new CostMatchingInfo(cost, resultStateMatrix);
    }

//    public double[][] getUtilityMatrix() {
//
//    }

    public UtilityDistanceIDPair[] bestResponse(){
        CostMatchingInfo costMatchingInfo = hungarianEnhanced(this.utilityMatrix, false);
        Double utility = costMatchingInfo.getCost();
        int[][] matching = costMatchingInfo.getMatching();
        UtilityDistanceIDPair[] response = new UtilityDistanceIDPair[this.tasks.length];
        for (int i = 0; i < matching.length; i++) {
            for (int j = 0; j < matching[i].length; j++) {
                if (matching[i][j] == 1) {
                    response[i] = new UtilityDistanceIDPair(this.workers[j].getCurrentUtilityFunctionValue(i),this.workers[j].getToTaskDistance(i),j);
                    break;
                }
            }
        }
        System.out.println(utility);
        return response;
    }







    public static void main(String[] args) {
////        double[][] data = new double[][] {
////                {10, 5, 9, 18, 11},
////                {13, 19, 6, 12, 14},
////                {3, 2, 4, 4, 5},
////                {18, 9, 12,	17,	15},
////                {11, 6,	14,	19,	10}
////        };
//        double[][] data = new double[][] {
//                {10, 5, 9, 18, 11, 6},
//                {13, 19, 6, 12, 14, 7},
//                {3, 2, 4, 4, 5, 5},
//                {18, 9, 12,	17,	15, 9},
//                {11, 6,	14,	19,	10, 10}
//        };
//
//        CostMatchingInfo result = BestNonePrivacySolution.hungarianEnhanced(data, true);
////        CostMatchingInfo result = BestNoPrivacySolution.hungarianEnhanced(data, false);
//        System.out.println(result.getCost());
//        MyPrint.show2DimensionIntegerArray(result.getMatching());


//        String basicDatasetPath = "E:\\1.学习\\4.数据集\\1.FourSquare-NYCandTokyoCheck-ins\\output\\SYN";
        String basicDatasetPath = "E:\\1.学习\\4.数据集\\1.FourSquare-NYCandTokyoCheck-ins\\output\\test";

        String workerPointPath = basicDatasetPath + "\\worker_point.txt";
        String taskPointPath = basicDatasetPath + "\\task_point.txt";
        String taskValuePath = basicDatasetPath + "\\task_value.txt";
        String workerRangePath = basicDatasetPath + "\\worker_range.txt";
//        String workerPrivacyBudgetPath = basicDatasetPath + "\\worker_budget.txt";
//        String workerNoiseDistancePath = basicDatasetPath + "\\worker_noise_distance.txt";

        List<Point> taskPointList = PointRead.readPointWithFirstLineCount(taskPointPath);
        List<Double> taskValueList = DoubleRead.readDoubleWithFirstSizeLineToList(taskValuePath);

        List<Point> workerPointList = PointRead.readPointWithFirstLineCount(workerPointPath);
        List<Double> workerRangeList = DoubleRead.readDoubleWithFirstSizeLineToList(workerRangePath);
//        List<Double[]>[] workerPrivacyBudgetList = TwoDimensionDoubleRead.readDouble(workerPrivacyBudgetPath);
//        List<Double[]>[] workerNoiseDistanceList = TwoDimensionDoubleRead.readDouble(workerNoiseDistancePath);


        // 初始化 task 和 workers
        Double taskValue = 20.0, workerRange = 2.0;
        BestNonePrivacySolution competitionSolution = new BestNonePrivacySolution();
        competitionSolution.initializeBasicInformation(taskPointList, taskValueList, workerPointList, workerRangeList);


        //todo: 根据不同的数据集选用不同的初始化
//        multiTaskMultiCompetitionSolution.initializeAgents();
//        Integer dataTypeValue = Integer.valueOf(dataType);
        competitionSolution.initializeAgentsWithLatitudeLongitude();

        long startCompetingTime = System.currentTimeMillis();
        UtilityDistanceIDPair[] winner = competitionSolution.bestResponse();
        long endCompetingTime = System.currentTimeMillis();
        Long runningTime = TargetTool.getRunningTime(startCompetingTime, endCompetingTime);

        System.out.println(runningTime);

        CommonFunction.showResultB(winner);

    }



}
