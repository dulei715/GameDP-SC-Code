package tools.basic;

import tools.basic.comparator.TwoDimensionDouComparator;
import tools.basic.comparator.TwoDimensionDoubleComparator;
import tools.io.print.MyPrint;

import java.lang.reflect.Array;
import java.util.*;

public class BasicArray {
    public static void setIntArrayTo(int[] element, int value) {
        for (int i = 0; i < element.length; i++) {
            element[i] = value;
        }
    }

    public static void setIntArrayTo(Integer[] element, int value) {
        for (int i = 0; i < element.length; i++) {
            element[i] = value;
        }
    }

    public static void setIntArrayToZero(int[] element) {
        setIntArrayTo(element, 0);
    }

    public static void setIntArrayToZero(Integer[] element) {
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

    public static void setIntegerListToContinuousNaturalNumber(List<Integer> list, Integer maxNaturalNumber) {
        for (int i = 0; i <= maxNaturalNumber; i++) {
            list.add(i);
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

    public static void twoDimensionalDoubleArraySort(double[][] arr) {
        Arrays.sort(arr, new TwoDimensionDouComparator());
    }

    public static void twoDimensionalDoubleArraySort(Double[][] arr) {
        Arrays.sort(arr, new TwoDimensionDoubleComparator());
    }

    public static <T> T[] getInitializedArray(T value, int size) {
        Class clazz = value.getClass();
        Object array = null;
        array = Array.newInstance(clazz, size);
        for (int i = 0; i < size; i++) {
            Array.set(array, i, value);
        }
        T[] result = (T[]) array;
        return result;
    }


    public static int getFirstFindValueIndex(int[] data, int value) {
        for (int i = 0; i < data.length; i++) {
            if (data[i] == value) {
                return i;
            }
        }
        return -1;
    }

    public static List<Integer> getDeclaredValueIndexList(int[] data, int value) {
        List<Integer> resultList = new ArrayList<>();
        for (int i = 0; i < data.length; i++) {
            if (data[i] == value) {
                resultList.add(i);
            }
        }
        return resultList;
    }

    public static Set<Integer> generateRandomSet(Integer minimalValue, Integer upperBoundValue, int generateSize, Set<Integer> excludedSet) {
        if (excludedSet == null) {
            excludedSet = new HashSet<>();
        }
        if (upperBoundValue - minimalValue - excludedSet.size() < generateSize) {
            throw new RuntimeException("The size of generating is too large!");
        }

        Random random = new Random();
        int tempInt;
        Set<Integer> result = new HashSet<>(generateSize);
        for (int i = 0; i < generateSize; i++) {
            while ( (tempInt = random.nextInt(upperBoundValue)) < minimalValue || excludedSet.contains(tempInt) || result.contains(tempInt));
            result.add(tempInt);
        }

        return result;

    }

    public static void main(String[] args) {
        int minValue = 1;
        int upperBoundValue = 100;
        int generateSize = 10;
        Set<Integer> excludedSet = new HashSet<>();
        excludedSet.add(33);
        excludedSet.add(35);

        Set<Integer> result = BasicArray.generateRandomSet(minValue, upperBoundValue, generateSize, excludedSet);
        MyPrint.showSet(result);
    }

}
