package tools.io.read;

import tools.io.print.MyPrint;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TwoDimensionDoubleRead {
    public static final String SPLIT_TAG = " ";

    public static List<Double[]>[] readDouble(String filePath, double factor) {
        BufferedReader bufferedReader = null;
        String line;
        String[] lineSplitValues;
        int workerSize, taskSize, budgetGroupSize;
        List<Double[]>[] data = null;
        Double[] budgetArrays = null;
//        int i = 0, k = -1;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));
            lineSplitValues = bufferedReader.readLine().split(SPLIT_TAG);
            workerSize = Integer.valueOf(lineSplitValues[0]);
            taskSize = Integer.valueOf(lineSplitValues[1]);
            budgetGroupSize = Integer.valueOf(lineSplitValues[2]);

//            while ((line = bufferedReader.readLine()) != null) {
//
//            }
            data = new ArrayList[workerSize];
            for (int i = 0; i < workerSize; i++) {
                data[i] = new ArrayList<>(taskSize);
                for (int j = 0; j < taskSize; j++) {
                    lineSplitValues = bufferedReader.readLine().split(SPLIT_TAG);
                    budgetArrays = new Double[lineSplitValues.length]; // 等于budgetGroupSize
                    for (int k = 0; k < lineSplitValues.length; k++) {
                        budgetArrays[k] = Double.valueOf(lineSplitValues[k]) * factor;
                    }
                    data[i].add(budgetArrays);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return data;
    }

    public static List<Double[]>[] readTopKDouble(String filePath, int topK) {
        BufferedReader bufferedReader = null;
        String line;
        String[] lineSplitValues;
        int workerSize, taskSize, budgetGroupSize;
        List<Double[]>[] data = null;
        Double[] budgetArrays = null;
//        int i = 0, k = -1;
        int a = 0;
        boolean breakStatue = false;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));
            lineSplitValues = bufferedReader.readLine().split(SPLIT_TAG);
            workerSize = Integer.valueOf(lineSplitValues[0]);
            taskSize = Integer.valueOf(lineSplitValues[1]);
            budgetGroupSize = Integer.valueOf(lineSplitValues[2]);

//            while ((line = bufferedReader.readLine()) != null) {
//
//            }
            data = new ArrayList[workerSize];
            for (int i = 0; i < workerSize; i++) {
                data[i] = new ArrayList<>(taskSize);
                for (int j = 0; j < taskSize; j++) {
                    if(a >= topK) {
                        breakStatue = true;
                        break;
                    }
                    lineSplitValues = bufferedReader.readLine().split(SPLIT_TAG);
                    budgetArrays = new Double[lineSplitValues.length]; // 等于budgetGroupSize
                    for (int k = 0; k < lineSplitValues.length; k++) {
                        budgetArrays[k] = Double.valueOf(lineSplitValues[k]);
                    }
                    data[i].add(budgetArrays);
                    ++a;
                }
                if (breakStatue) {
                    break;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return data;
    }

    public static void main(String[] args) {
        String basicPath = System.getProperty("user.dir") + "\\dataset\\test_dataset\\_2_multiple_task_dataset\\test1\\";
        String taskPointPath = basicPath + "worker_privacy_budget.txt";
        List<Double[]>[] result = TwoDimensionDoubleRead.readDouble(taskPointPath,1);
        for (int i = 0; i < result.length; i++) {
            List<Double[]> innerList = result[i];
            for (int j = 0; j < innerList.size(); j++) {
                MyPrint.showDoubleArray(innerList.get(j));
            }
            System.out.println("**********************************************");
        }
    }

}
