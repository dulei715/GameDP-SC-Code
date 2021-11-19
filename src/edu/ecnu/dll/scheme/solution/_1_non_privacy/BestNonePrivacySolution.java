package edu.ecnu.dll.scheme.solution._1_non_privacy;

import edu.ecnu.dll.basic_struct.pack.CostMatchingInfo;
import edu.ecnu.dll.scheme.solution._0_basic.NonPrivacySolution;
import tools.basic.BasicArray;
import tools.basic.MatrixArray;
import tools.io.print.MyPrint;

import java.util.*;

public class BestNonePrivacySolution extends NonPrivacySolution {



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







    public static void main(String[] args) {
//        double[][] data = new double[][] {
//                {10, 5, 9, 18, 11},
//                {13, 19, 6, 12, 14},
//                {3, 2, 4, 4, 5},
//                {18, 9, 12,	17,	15},
//                {11, 6,	14,	19,	10}
//        };
        double[][] data = new double[][] {
                {10, 5, 9, 18, 11, 6},
                {13, 19, 6, 12, 14, 7},
                {3, 2, 4, 4, 5, 5},
                {18, 9, 12,	17,	15, 9},
                {11, 6,	14,	19,	10, 10}
        };

        CostMatchingInfo result = BestNonePrivacySolution.hungarianEnhanced(data, true);
//        CostMatchingInfo result = BestNoPrivacySolution.hungarianEnhanced(data, false);
        System.out.println(result.getCost());
        MyPrint.show2DimensionIntegerArray(result.getMatching());
    }



}
