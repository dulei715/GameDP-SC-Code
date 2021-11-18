package edu.ecnu.dll.scheme.solution._1_non_privacy;

import edu.ecnu.dll.basic_struct.pack.CostMatchingInfo;
import edu.ecnu.dll.scheme.solution._0_basic.NonPrivacySolution;
import tools.basic.BasicArray;
import tools.io.print.MyPrint;

import java.util.*;

public class BestNoPrivacySolution extends NonPrivacySolution {

    public static double getMaxValue(double[][] matrix) {
        double result = Double.MIN_VALUE;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                if (matrix[i][j] > result) {
                    result = matrix[i][j];
                }
            }
        }
        return result;
    }

    public static void linearHandleEachElement(double[][] matrix, double paramMulti, double paramAdd) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                matrix[i][j] = paramMulti * matrix[i][j] + paramAdd;
            }
        }
    }

    public static int[] getMatrixSize(double[][] matrix) {
        int lineNumber = matrix.length;
        int colNumber = matrix[0].length;
        return new int[]{lineNumber, colNumber};
    }

    public static void linearHandleMatrix(double[][] fromMatrix, double[][] toMatrix, double defaultValue, double paramMulti, double paramAdd) {
        int[] fromMatrixSize = getMatrixSize(fromMatrix);
        for (int i = 0; i < toMatrix.length; i++) {
            for (int j = 0; j < toMatrix[0].length; j++) {
                if (i < fromMatrixSize[0] && j < fromMatrixSize[1]) {
                    toMatrix[i][j] = fromMatrix[i][j] * paramMulti + paramAdd;
                } else {
                    toMatrix[i][j] = defaultValue;
                }
            }
        }
    }

    private static double[][] getPreHandledMatrix(double[][] matrix, boolean isMinimized, Double maxMatrixValue) {
        double[][] resultMatrix;
        int[] matrixSize = getMatrixSize(matrix);
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
        linearHandleMatrix(matrix, resultMatrix, 0.0, paramMulti, paramAdd);
        return resultMatrix;
    }

    public static int[][] getIntegerMatrixWithDeclaredSize(int[] size, int defaultValue) {
        int[][] result = new int[size[0]][size[1]];
        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < result[0].length; j++) {
                result[i][j] = defaultValue;
            }
        }
        return result;
    }

    public static int[][] getResultStateWhetherEqualsToDeclaredValue(double[][] matrix, double value) {
        // 相等置为1， 不等置为0
        int[][] result = new int[matrix.length][matrix[0].length];
        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < result[i].length; j++) {
                if (matrix[i][j] == value) {
                    result[i][j] = 1;
                } else {
                    result[i][j] = 0;
                }
            }
        }
        return result;
    }

    public static int[][] getResultStateWhetherEqualsToDeclaredValue(int[][] matrix, int value) {
        // 相等置为1， 不等置为0
        int[][] result = new int[matrix.length][matrix[0].length];
        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < result[i].length; j++) {
                if (matrix[i][j] == value) {
                    result[i][j] = 1;
                } else {
                    result[i][j] = 0;
                }
            }
        }
        return result;
    }

    public static int[] getRowCountEqualDeclaredValue(int[][] matrix, int value) {
        int size = matrix.length;
        int[] result = new int[size];
//        BasicArray.setIntArrayToZero(result);
        for (int i = 0; i < size; i++) {
            result[i] = 0;
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j] == value) {
                    result[i] ++;
                }
            }
        }
        return result;
    }

    public static int[] getColCountEqualDeclaredValue(int[][] matrix, int value) {
        int size = matrix[0].length;
        int[] result = new int[size];
//        BasicArray.setIntArrayToZero(result);
        for (int j = 0; j < size; j++) {
            result[j] = 0;
            for (int i = 0; i < matrix.length; i++) {
                if (matrix[i][j] == value) {
                    result[j] ++;
                }
            }
        }
        return result;
    }

    public static List<Integer> getColIndexListWithDeclareRowIndexAndValue(int[][] matrix, int rowIndex, int value) {
        List<Integer> colIndexList = new ArrayList<>();
        for (int j = 0; j < matrix[rowIndex].length; j++) {
            if (matrix[rowIndex][j] == value) {
                colIndexList.add(j);
            }
        }
        return colIndexList;
    }

    public static List<Integer> getColIndexListWithDeclareRowIndexAndValue(int[][] matrix, List<Integer> rowIndexList, int value) {
        TreeSet<Integer> colIndexTreeSet = new TreeSet<>();
        List<Integer> result = new ArrayList<>();
        List<Integer> tempList;
        for (int i = 0; i < rowIndexList.size(); i++) {
            tempList = getColIndexListWithDeclareRowIndexAndValue(matrix, rowIndexList.get(i), value);
            colIndexTreeSet.addAll(tempList);
        }
        result.addAll(colIndexTreeSet);
        return result;
    }

    public static List<Integer> getRowIndexListWithDeclareColIndexAndValue(int[][] matrix, int colIndex, int value) {
        List<Integer> rowIndexList = new ArrayList<>();
        for (int i = 0; i < matrix.length; i++) {
            if (matrix[i][colIndex] == value) {
                rowIndexList.add(i);
            }
        }
        return rowIndexList;
    }


    public static List<Integer> getRowIndexListWithDeclareColIndexAndValue(int[][] matrix, List<Integer> colIndexList, int value) {
        TreeSet<Integer> rowIndexTreeSet = new TreeSet<>();
        List<Integer> result = new ArrayList<>();
        List<Integer> tempList;
        for (int i = 0; i < colIndexList.size(); i++) {
            tempList = getRowIndexListWithDeclareColIndexAndValue(matrix, colIndexList.get(i), value);
            rowIndexTreeSet.addAll(tempList);
        }
        result.addAll(rowIndexTreeSet);
        return result;
    }

    public static void setValue(int[][] matrix, List<Integer> rowIndexList, List<Integer> colIndexList, int value) {
        for (Integer rowIndex : rowIndexList) {
            for (Integer colIndex : colIndexList) {
                matrix[rowIndex][colIndex] = value;
            }
        }
    }

    public static void setValue(int[][] matrix, int rowIndex, List<Integer> colIndexList, int value) {
        if (rowIndex <= -1) {
            for (int i = 0; i < matrix.length; i++) {
                for (Integer colIndex : colIndexList) {
                    matrix[i][colIndex] = value;
                }
            }
        } else {
            for (Integer colIndex : colIndexList) {
                matrix[rowIndex][colIndex] = value;
            }
        }
    }

    public static void setValue(int[][] matrix, List<Integer> rowIndexList, int colIndex, int value) {
        if (colIndex <= -1) {
            for (int j = 0; j < matrix[0].length; j++) {
                for (Integer rowIndex : rowIndexList) {
                    matrix[rowIndex][j] = value;
                }
            }
        } else {
            for (Integer rowIndex : rowIndexList) {
                matrix[rowIndex][colIndex] = value;
            }
        }
    }

    public static double getMatrixElementMultipleResult(double[][] matrix, int[][] state) {
        return getMatrixLinearChangeElementMultipleResult(matrix, state, 1, 0);
    }

    public static double getMatrixLinearChangeElementMultipleResult(double[][] matrix, int[][] state, double paramMulti, double paramAdd) {
        double result = 0;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                result += (matrix[i][j] * paramMulti + paramAdd) * state[i][j];
            }
        }
        return result;
    }

    public static double getCost(double[][] matrix, int[][] state, boolean isMinimized, Double originalMaxMatrixValue) {
        if (isMinimized) {
            return getMatrixElementMultipleResult(matrix, state);
        }
        return getMatrixLinearChangeElementMultipleResult(matrix, state, -1, originalMaxMatrixValue);
    }

    public static int[][] generateSubMatrix(int[][] matrix, int rowSize, int colSize) {
        int[][] result = new int[rowSize][colSize];
        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < result[i].length; j++) {
                result[i][j] = matrix[i][j];
            }
        }
        return result;
    }

    public static double[][] generateSubMatrix(double[][] matrix, int rowSize, int colSize) {
        double[][] result = new double[rowSize][colSize];
        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < result[i].length; j++) {
                result[i][j] = matrix[i][j];
            }
        }
        return result;
    }

//    public static int[][] generateSubMatrix(int[][] matrix, List<Integer> rowIndexList, List<Integer> colIndexList) {
//
//    }

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

    public static void addValue(double[][] matrix, Integer rowIndex, Integer colIndex, double addingValue) {
        if (rowIndex <= -1 && colIndex <= -1) {
            for (int i = 0; i < matrix.length; i++) {
                for (int j = 0; j < matrix[i].length; j++) {
                    matrix[i][j] += addingValue;
                }
            }
        } else {
            if (colIndex <= -1) {
                for (int j = 0; j < matrix[rowIndex].length; j++) {
                    matrix[rowIndex][j] += addingValue;
                }
            } else if (rowIndex <= -1) {
                for (int i = 0; i < matrix.length; i++) {
                    matrix[i][colIndex] += addingValue;
                }
            } else {
                matrix[rowIndex][colIndex] += addingValue;
            }
        }
    }

    public static void addValue(double[][] matrix, List<Integer> rowIndexList, Integer colIndex, double addingValue) {
        if (colIndex <= -1) {
            for (int j = 0; j < matrix[0].length; j++) {
                for (Integer rowIndex : rowIndexList) {
                    matrix[rowIndex][j] += addingValue;
                }
            }
        } else {
            for (Integer rowIndex : rowIndexList) {
                matrix[rowIndex][colIndex] += addingValue;
            }
        }
    }

    public static void addValue(double[][] matrix, Integer rowIndex, List<Integer> colIndexList, double addingValue) {
        if (rowIndex <= -1) {
            for (int i = 0; i < matrix.length; i++) {
                for (Integer colIndex : colIndexList) {
                    matrix[i][colIndex] += addingValue;
                }
            }
        } else {
            for (Integer colIndex : colIndexList) {
                matrix[rowIndex][colIndex] += addingValue;
            }
        }
    }

    public static void addValue(double[][] matrix, List<Integer> rowIndexList, List<Integer> colIndexList, double addingValue) {
        for (Integer rowIndex : rowIndexList) {
            for (Integer colIndex : colIndexList) {
                matrix[rowIndex][colIndex] += addingValue;
            }
        }
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

    public static double getRowMinimalValue(double[][] matrix, Integer rowIndex) {
        Double result = Double.MAX_VALUE;
        for (int j = 0; j < matrix[rowIndex].length; j++) {
            if (matrix[rowIndex][j] < result) {
                result = matrix[rowIndex][j];
            }
        }
        return result;
    }

    public static double getColMinimalValue(double[][] matrix, Integer colIndex) {
        Double result = Double.MAX_VALUE;
        for (int i = 0; i < matrix.length; i++) {
            if (matrix[i][colIndex] < result) {
                result = matrix[i][colIndex];
            }
        }
        return result;
    }

    public static void minusMinimalValueForEachRow(double[][] matrix) {
        Double tempMinimalValue;
        for (int i = 0; i < matrix.length; i++) {
            tempMinimalValue = getRowMinimalValue(matrix, i);
            for (int j = 0; j < matrix[i].length; j++) {
                matrix[i][j] -= tempMinimalValue;
            }
        }
    }

    public static void minusMinimalValueForEachCol(double[][] matrix) {
        Double tempMinimalValue;
        for (int j = 0; j < matrix[0].length; j++) {
            tempMinimalValue = getColMinimalValue(matrix, j);
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
            maxMatrixValue = getMaxValue(paramMatrix);
        }

        double[][] handleDataMatrix = getPreHandledMatrix(paramMatrix, isMinimized, maxMatrixValue);
        double[][] originalHandleDataMatrix = generateSubMatrix(handleDataMatrix, handleDataMatrix.length, handleDataMatrix[0].length);

        minusMinimalValueForEachRow(handleDataMatrix);
        minusMinimalValueForEachCol(handleDataMatrix);

        while (true) {
//            int[][] stateMatrix = getIntegerMatrixWithDeclaredSize(getMatrixSize(resultMatrix), 0);
            int[][] stateMatrix = getResultStateWhetherEqualsToDeclaredValue(handleDataMatrix, 0);
            int independentZeroSize = 0;
            while (true) {
                int[] rowZeroNumber = getRowCountEqualDeclaredValue(stateMatrix, 1);
                List<Integer> onlyOneZeroRowNumberIndexList = BasicArray.getDeclaredValueIndexList(rowZeroNumber, 1);
                if (!onlyOneZeroRowNumberIndexList.isEmpty()) {
                    int onlyOneZeroRowNumberIndex = onlyOneZeroRowNumberIndexList.get(0);
                    List<Integer> colIndexList = getColIndexListWithDeclareRowIndexAndValue(stateMatrix, onlyOneZeroRowNumberIndex, 1);
                    setValue(stateMatrix, onlyOneZeroRowNumberIndex, colIndexList, 2);
                    List<Integer> rowIndexList = getRowIndexListWithDeclareColIndexAndValue(stateMatrix, colIndexList.get(0), 1);
                    // colIndexList只会有一个元素
                    setValue(stateMatrix, rowIndexList, colIndexList, -2);
                    ++ independentZeroSize;

                } else {
                    int [] colZeroNumber = getColCountEqualDeclaredValue(stateMatrix, 1);
                    List<Integer> onlyOneZeroColNumberIndexList = BasicArray.getDeclaredValueIndexList(colZeroNumber, 1);
                    if (!onlyOneZeroColNumberIndexList.isEmpty()) {
                        int onlyOneZeroColNumberIndex = onlyOneZeroColNumberIndexList.get(0);
                        List<Integer> rowIndexList = getRowIndexListWithDeclareColIndexAndValue(stateMatrix, onlyOneZeroColNumberIndex, 1);
                        setValue(stateMatrix, rowIndexList, onlyOneZeroColNumberIndex, 2);
                        List<Integer> colIndexList = getColIndexListWithDeclareRowIndexAndValue(stateMatrix, rowIndexList.get(0), 1);
                        setValue(stateMatrix, rowIndexList, colIndexList, -2);
                        ++ independentZeroSize;
                    } else {
                        break;
                    }
                }
            }

            if (independentZeroSize == matrixSize) {
                int[][] resultStateMatrix = getResultStateWhetherEqualsToDeclaredValue(stateMatrix, 2);
                cost = getCost(originalHandleDataMatrix, resultStateMatrix, isMinimized, maxMatrixValue);
                resultState = generateSubMatrix(resultStateMatrix, taskSize, workerSize);
                break;
            } else {
                int[] rowCountResult = getRowCountEqualDeclaredValue(stateMatrix, 2);
//                getRowIndexListWithDeclareColIndexAndValue()
                List<Integer> rowIndexList = BasicArray.getDeclaredValueIndexList(rowCountResult, 0);
                List<Integer> rowRowIndexList = new ArrayList<>(rowIndexList);
                List<Integer> colColIndexList = new ArrayList<>();
                while (true) {
                    List<Integer> colIndexList = getColIndexListWithDeclareRowIndexAndValue(stateMatrix, rowRowIndexList, -2);
                    if (colIndexList.isEmpty()) {
                        break;
                    }
                    rowRowIndexList = getRowIndexListWithDeclareColIndexAndValue(stateMatrix, colIndexList, 2);
                    rowIndexList.addAll(rowRowIndexList);
                    colColIndexList.addAll(colIndexList);
                }
                List<Integer> remainRowIndexList = generateRemainIndexList(rowIndexList, matrixSize - 1);
                double minUnOverLapValue = getMinimalValueFromMatrixExcludingRowIndexListAndColIndexList(handleDataMatrix, remainRowIndexList, colColIndexList);
                addValue(handleDataMatrix, -1, -1, -minUnOverLapValue);
                addValue(handleDataMatrix, remainRowIndexList, -1, minUnOverLapValue);
                addValue(handleDataMatrix, -1, colColIndexList, minUnOverLapValue);
            }
        }

        return new CostMatchingInfo(cost, resultState);
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

        CostMatchingInfo result = BestNoPrivacySolution.hungarian(data, false);
        System.out.println(result.getCost());
        MyPrint.show2DimensionIntegerArray(result.getMatching());
    }

}
