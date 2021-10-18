package tools.basic;

import edu.ecnu.dll.basic_struct.pack.DistanceBudgetPair;

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


}
