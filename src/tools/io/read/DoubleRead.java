package tools.io.read;

import tools.struct.Point;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DoubleRead {

    public static final String SPLIT_TAG = " ";

    public static Double[] readDouble(String filePath, int scale) {
        BufferedReader bufferedReader = null;
        String line;
        int dataSize;
        String[] dataElement;
        Double[] doubleArray = null;
        int i = 0, k = -1;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));
            dataSize = Integer.valueOf(bufferedReader.readLine());
            doubleArray = new Double[dataSize/scale];
            while ((line = bufferedReader.readLine()) != null) {
                ++i;
                if (i % scale != 0) {
                    continue;
                }
                doubleArray[++k] = Double.valueOf(line);
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
        return doubleArray;
    }

    public static List[] readDoubleList(String filePath, int scale) {
        BufferedReader bufferedReader = null;
        String line;
        int dataSize;
        String[] dataElement;
        List[] doubleListArray = null;
        int i = 0, k = -1;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));
            dataSize = Integer.valueOf(bufferedReader.readLine());
            doubleListArray = new List[dataSize/scale];
            while ((line = bufferedReader.readLine()) != null) {
                ++i;
                if (i % scale != 0) {
                    continue;
                }
                doubleListArray[++k] = new ArrayList();
                dataElement = line.split(SPLIT_TAG);
                for (String s : dataElement) {
                    doubleListArray[k].add(Double.valueOf(s));
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
        return doubleListArray;
    }

    public static List[] readDoubleList(String workerPrivacyBudgetPath) {
        return readDoubleList(workerPrivacyBudgetPath, 1);
    }

    public static Double[] readDouble(String filePath) {
        return readDouble(filePath, 1);
    }

}
