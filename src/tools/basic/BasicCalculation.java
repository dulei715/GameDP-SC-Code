package tools.basic;

import edu.ecnu.dll.basic_struct.pack.single_agent_info.sub_class.DistanceBudgetPair;
import tools.io.print.MyPrint;

import java.text.DecimalFormat;
import java.util.Arrays;

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


    public static double radians(double d) {
        return d * Math.PI / 180.0;
    }
    /**
     * 根据两点经纬度坐标计算直线距离
     * <p>
     * S = 2arcsin√sin²(a/2)+cos(lat1)*cos(lat2)*sin²(b/2)￣*6378.137
     * <p>
     * 1. lng1 lat1 表示A点经纬度，lng2 lat2 表示B点经纬度；<br>
     * 2. a=lat1 – lat2 为两点纬度之差  b=lng1 -lng2 为两点经度之差；<br>
     * 3. 6378.137为地球赤道半径，单位为千米；
     *
     * @param lng1 点1经度
     * @param lat1 点1纬度
     * @param lng2 点2经度
     * @param lat2 点2纬度
     * @return 距离，单位千米(KM)
     * @see <a href="https://zh.wikipedia.org/wiki/%E5%8D%8A%E6%AD%A3%E7%9F%A2%E5%85%AC%E5%BC%8F">半正矢(Haversine)公式</a>
     */
    public static double getDistanceFrom2LngLat(double lng1, double lat1, double lng2, double lat2) {
        double radLng1 = radians(lng1);
        double radLat1 = radians(lat1);
        double radLng2 = radians(lng2);
        double radLat2 = radians(lat2);

        double a = radLat1 - radLat2;
        double b = radLng1 - radLng2;

        return 2 * Math.asin(Math.sqrt(Math.sin(a / 2) * Math.sin(a / 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.sin(b / 2) * Math.sin(b / 2))) * 6378.137;
    }

    public static double getAverage(double averageBefore, double newValue, int count) {
        return (averageBefore*(count-1)+newValue)/count;
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

    public static Double getRandomDoubleValueInRange(double lowerBound, double upperBound, int precision) {
        if (precision < 0) {
            throw new RuntimeException("The precision should not be negative!");
        }
        double randomValue = Math.random();
        Double result;
        String tag = "0";
        if (precision > 0) {
            tag += ".";
        }
        for (int i = 0; i < precision; i++) {
            tag += "0";
        }
        DecimalFormat decimalFormat = new DecimalFormat(tag);
        double candidateValue = lowerBound + randomValue * (upperBound - lowerBound);
        result = Double.valueOf(decimalFormat.format(candidateValue));
        return result;
    }

    public static Double[] getRandomDoubleValueArrayInRange(double lowerBound, double upperBound, int precision, int arraySize) {
        if (precision < 0) {
            throw new RuntimeException("The precision should not be negative!");
        }
        double randomValue, candidateValue;
        String tag = "0";
        Double[] result = new Double[arraySize];
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
            result[i] = Double.valueOf(decimalFormat.format(candidateValue));
        }
        return result;
    }

    public static String getRandomStringValueInRange(double lowerBound, double upperBound, int precision) {
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

    public static String[] getRandomStringValueArrayInRange(double lowerBound, double upperBound, int precision, int arraySize) {
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




    public static String[][] getRandomStringValueTwoDimensionArrayInRange(double lowerBound, double upperBound, int precision, int arraySize, int innerArraySize) {
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

    public static String[][] getSortedRandomStringValueTwoDimensionArrayInRange(double lowerBound, double upperBound, int precision, int arraySize, int innerArraySize) {
        if (precision < 0) {
            throw new RuntimeException("The precision should not be negative!");
        }
        double randomValue, candidateValue;
        String tag = "0";
        String[][] result = new String[arraySize][innerArraySize];
        double[] tempArray = new double[innerArraySize];
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
                tempArray[j] = candidateValue;
            }

            Arrays.sort(tempArray);

            for (int j = 0; j < innerArraySize; j++) {
                result[i][j] = decimalFormat.format(tempArray[j]);
            }

        }
        return result;
    }


    public static void main(String[] args) {
//        String result = getRandomValueInRange(10, 200, 2);
//        System.out.println(result);
        String[] result2 = getRandomStringValueArrayInRange(10, 200, 2, 10);
        MyPrint.showStringArray(result2);
    }


}
