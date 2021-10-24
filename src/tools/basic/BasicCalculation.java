package tools.basic;

import edu.ecnu.dll.basic_struct.pack.DistanceBudgetPair;
import tools.io.print.MyPrint;

import java.text.DecimalFormat;
import java.util.TreeSet;

public class BasicCalculation {
    public static final int ESTIMATION_VALUE_INDEX = 0;
    public static final int WEIGHTED_INDEX = 1;
    public static double get2Norm(double[] pointA, double[] pointB) {
        if (pointA.length != pointB.length) {
            throw new RuntimeException("The dimensionality of two points are not equal!");
        }
        int len = pointA.length;
        double result = 0;
        for (int i = 0; i < len; i++) {
            result += Math.pow(pointA[i]-pointB[i], 2);
        }
        return Math.sqrt(result);
    }

    public static double getSum(double ... values) {
        double sum = 0;
        for (int i = 0; i < values.length; i++) {
            sum += values[i];
        }
        return sum;
    }

    public static double getAverage(double everageBefore, double newValue, int count) {
        return (everageBefore*(count-1)+newValue)/count;
    }

    public static double getAverage(double ... values) {
        return getSum(values)/ values.length;
    }

    public static double getWeightedFirstVectorNormOfDifference(double[][] estimationAndWeighted, double realValue) {
        double result = 0;
        for (int i = 0; i < estimationAndWeighted.length; i++) {
            result += estimationAndWeighted[i][ESTIMATION_VALUE_INDEX] * Math.abs(realValue - estimationAndWeighted[i][WEIGHTED_INDEX]);
        }
        return result;
    }

    public static double getWeightedFirstVectorNormOfDifference(DistanceBudgetPair[] distanceBudgetPairArray, double distanceValue) {
        double result = 0;
        for (int i = 0; i < distanceBudgetPairArray.length; i++) {
            result += distanceBudgetPairArray[i].budget * Math.abs(distanceValue - distanceBudgetPairArray[i].distance);
        }
        return result;
    }

    public static String getRandomValueInRange(double lowerBound, double upperBound, int precision) {
        if (precision < 0) {
            throw new RuntimeException("The precision should not be negative!");
        }
        double randomValue = Math.random();
        String result;
        String tag = "0";
        if (precision > 0) {
            tag += ".";
        }
        for (int i = 0; i < precision; i++) {
            tag += "0";
        }
        DecimalFormat decimalFormat = new DecimalFormat(tag);
        double candidateValue = lowerBound + randomValue * (upperBound - lowerBound);
        result = decimalFormat.format(candidateValue);
        return result;
    }

    public static String[] getRandomValueArrayInRange(double lowerBound, double upperBound, int precision, int arraySize) {
        if (precision < 0) {
            throw new RuntimeException("The precision should not be negative!");
        }
        double randomValue, candidateValue;
        String tag = "0";
        String[] result = new String[arraySize];
        if (precision > 0) {
            tag += ".";
        }
        for (int i = 0; i < precision; i++) {
            tag += "0";
        }
        DecimalFormat decimalFormat = new DecimalFormat(tag);
        for (int i = 0; i < arraySize; i++) {
            randomValue = Math.random();
            candidateValue = lowerBound + randomValue * (upperBound - lowerBound);
            result[i] = decimalFormat.format(candidateValue);
        }
        return result;
    }

    public static String[][] getRandomValueTwoDimensionArrayInRange(double lowerBound, double upperBound, int precision, int arraySize, int innerArraySize) {
        if (precision < 0) {
            throw new RuntimeException("The precision should not be negative!");
        }
        double randomValue, candidateValue;
        String tag = "0";
        String[][] result = new String[arraySize][innerArraySize];
        if (precision > 0) {
            tag += ".";
        }
        for (int i = 0; i < precision; i++) {
            tag += "0";
        }
        DecimalFormat decimalFormat = new DecimalFormat(tag);
        for (int i = 0; i < arraySize; i++) {
            for (int j = 0; j < innerArraySize; j++) {
                randomValue = Math.random();
                candidateValue = lowerBound + randomValue * (upperBound - lowerBound);
                result[i][j] = decimalFormat.format(candidateValue);
            }
        }
        return result;
    }

    public static void main(String[] args) {
//        String result = getRandomValueInRange(10, 200, 2);
//        System.out.println(result);
        String[] result2 = getRandomValueArrayInRange(10, 200, 2, 10);
        MyPrint.showStringArray(result2);
    }


}
