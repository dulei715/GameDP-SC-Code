package tools.io.print;

import tools.io.read.DoubleRead;

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.List;

public class MyPrint {

    public static void showSplitLine(String unitString, int number) {
        if (number <= 0) {
            return;
        }
        int num = number - 1;
        for (int i = 0; i < num; i++) {
            System.out.print(unitString);
        }
        System.out.println(unitString);
    }

    public static void showDoubleArray(double[] data, String split, int precision){
        int i;
        if (precision >= 0) {
            StringBuilder pattern = null;
            pattern = new StringBuilder("0.");
            for (int j = 0; j < precision; j++) {
                pattern.append("0");
            }
//            System.out.println(pattern.toString());
            DecimalFormat df = new DecimalFormat(pattern.toString());
            for (i = 0; i < data.length - 1; i++) {
                System.out.print(df.format(data[i]) + split);
            }
            System.out.println(df.format(data[i]));
        } else {
            for (i = 0; i < data.length - 1; i++) {
                System.out.printf(data[i] + split);
            }
            System.out.println(data[i]);
        }
    }
    public static void showDoubleArray(double[] data) {
        showDoubleArray(data, ", ", -1);
    }

    public static void showDoubleArray(Double[] data, String split, int precision) {
        int i;
        if (precision >= 0) {
            StringBuilder pattern = null;
            pattern = new StringBuilder("0.");
            for (int j = 0; j < precision; j++) {
                pattern.append("0");
            }
//            System.out.println(pattern.toString());
            DecimalFormat df = new DecimalFormat(pattern.toString());
            for (i = 0; i < data.length - 1; i++) {
                System.out.print(df.format(data[i]) + split);
            }
            System.out.println(df.format(data[i]));
        } else {
            for (i = 0; i < data.length - 1; i++) {
                System.out.printf(data[i] + split);
            }
            System.out.println(data[i]);
        }
    }

    public static void showDoubleArray(Double[] data) {
        showDoubleArray(data, ", ", -1);
    }

    public static void showIntegerArray(int[] data, String split, int precision){
        int i;
        if (precision >= 0) {
            StringBuilder pattern = null;
            pattern = new StringBuilder("0.");
            for (int j = 0; j < precision; j++) {
                pattern.append("0");
            }
//            System.out.println(pattern.toString());
            DecimalFormat df = new DecimalFormat(pattern.toString());
            for (i = 0; i < data.length - 1; i++) {
                System.out.print(df.format(data[i]) + split);
            }
            System.out.println(df.format(data[i]));
        } else {
            for (i = 0; i < data.length - 1; i++) {
                System.out.printf(data[i] + split);
            }
            System.out.println(data[i]);
        }
    }

    public static void showIntegerArray(int[] data) {
        showIntegerArray(data, ", ", -1);
    }

    public static void showIntegerArray(Integer[] data, String split, int precision){
        int i;
        if (precision >= 0) {
            StringBuilder pattern = null;
            pattern = new StringBuilder("0.");
            for (int j = 0; j < precision; j++) {
                pattern.append("0");
            }
//            System.out.println(pattern.toString());
            DecimalFormat df = new DecimalFormat(pattern.toString());
            for (i = 0; i < data.length - 1; i++) {
                System.out.print(df.format(data[i]) + split);
            }
            System.out.println(df.format(data[i]));
        } else {
            for (i = 0; i < data.length - 1; i++) {
                System.out.printf(data[i] + split);
            }
            System.out.println(data[i]);
        }
    }

    public static void showIntegerArray(Integer[] data) {
        showIntegerArray(data, ", ", -1);
    }

    public static void show2DimensionDoubleArray(double[][] data, String split, int precision) {
        int i, j;
        if (precision >= 0) {
            StringBuilder pattern = null;
            pattern = new StringBuilder("0.");
            for (int k = 0; k < precision; k++) {
                pattern.append("0");
            }
//            System.out.println(pattern.toString());
            DecimalFormat df = new DecimalFormat(pattern.toString());
            for (i = 0; i < data.length; i++) {
                for (j = 0; j < data[0].length - 1; j++) {
                    System.out.print(df.format(data[i][j]) + split);
                }
                System.out.println(df.format(data[i][j]));
            }
        } else {
            for (i = 0; i < data.length; i++) {
                for (j = 0; j < data[0].length - 1; j++) {
                    System.out.print(data[i][j] + split);
                }
                System.out.println(data[i][j]);
            }
        }
    }

    public static void show2DimensionDoubleArray(double[][] data) {
        show2DimensionDoubleArray(data, ", ", -1);
    }

    public static void showList(List<? extends Object> list) {
        if (list.isEmpty()) {
            System.out.println("Empty!!!");
            return;
        }
        int i;
        for (i = 0; i < list.size() - 1; i++) {
            System.out.print(list.get(i) + ", ");
        }
        System.out.println(list.get(i));
    }

    public static void showList(List<? extends Object> list, String splitSymbol) {
        if (list.isEmpty()) {
            System.out.println("Empty!!!");
            return;
        }
        int i;
        for (i = 0; i < list.size() - 1; i++) {
            System.out.print(list.get(i) + splitSymbol);
        }
        System.out.println(list.get(i));
    }

    public static void showByteArray(byte[] data, String split){
        int i;
        for (i = 0; i < data.length - 1; i++) {
            System.out.printf(data[i] + split);
        }
        System.out.println(data[i]);
    }

    public static void showStringArray(String[] data, String split){
        int i;
        for (i = 0; i < data.length - 1; i++) {
            System.out.printf(data[i] + split);
        }
        System.out.println(data[i]);
    }

    public static void showStringArray(String[] data) {
        showStringArray(data, ", ");
    }

    public static void showListArray(List[] data) {
        for (int i = 0; i < data.length; i++) {
            for (Object o : data[i]) {
                System.out.print(o + " ");
            }
            System.out.println();
        }
    }

    public static void showListDoubleArray(List<Double[]>[] data, String doubleArraySplitSymbol, String listSplitSymbol) {
        List<Double[]> doubleArrayList;
        Double[] tempDoubleArray;
        for (int i = 0; i < data.length; i++) {
            doubleArrayList = data[i];
            for (int j = 0, k; j < doubleArrayList.size(); j++) {
                tempDoubleArray = doubleArrayList.get(j);
                for (k = 0; k < tempDoubleArray.length - 1; k++) {
                    System.out.print(tempDoubleArray[k] + doubleArraySplitSymbol);
                }
                System.out.print(tempDoubleArray[k] + listSplitSymbol);
            }
            System.out.println();
        }
    }

    public static void showListDoubleArray(List<Double[]>[] data) {
        showListDoubleArray(data, ", ", "; ");
    }
}
