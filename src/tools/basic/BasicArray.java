package tools.basic;

import java.util.ArrayList;
import java.util.List;

public class BasicArray {
    public static void setIntArrayTo(int[] element, int value) {
        for (int i = 0; i < element.length; i++) {
            element[i] = value;
        }
    }

    public static void setIntArrayToZero(int[] element) {
        setIntArrayTo(element, 0);
    }

    public static void setDoubleArrayTo(double[] element, double value) {
        for (int i = 0; i < element.length; i++) {
            element[i] = value;
        }
    }

    public static void setListArrayToEmptyList(List[] element) {
        for (int i = 0; i < element.length; i++) {
            element[i] = new ArrayList();
        }
    }


    public static void setListArrayTo(List[] element, List list) {
        for (int i = 0; i < element.length; i++) {
            element[i] = new ArrayList(list);
        }
    }

    public static void setDoubleArrayToZero(double[] element) {
        setDoubleArrayTo(element, 0.0);
    }


    public static Integer[] toIntegerArray(String[] arr) {
        Integer[] result = new Integer[arr.length];
        for (int i = 0; i < arr.length; i++) {
            result[i] = Integer.valueOf(arr[i]);
        }
        return result;
    }


    public static int[] toIntArray(String[] arr) {
        int[] result = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            result[i] = Integer.parseInt(arr[i]);
        }
        return result;
    }

    public static Double[] toDoubleArray(String[] arr) {
        Double[] result = new Double[arr.length];
        for (int i = 0; i < arr.length; i++) {
            result[i] = Double.valueOf(arr[i]);
        }
        return result;
    }

    public static double[] toDouArray(String[] arr) {
        double[] result = new double[arr.length];
        for (int i = 0; i < arr.length; i++) {
            result[i] = Double.parseDouble(arr[i]);
        }
        return result;
    }




}
