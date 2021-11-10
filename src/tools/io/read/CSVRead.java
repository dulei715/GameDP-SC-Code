package tools.io.read;

import tools.io.print.MyPrint;
import tools.struct.Order;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CSVRead {
    private BufferedReader bufferedReader = null;
    public static List<Map<String, String>> readData(String filePath) {
        BufferedReader bufferedReader = null;
        String line = null;
        String[] dataElement;
        int dataSize;
        List<Map<String, String>> elementList = new ArrayList<>();
        List<String> keyList = new ArrayList<>();
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));
            line = bufferedReader.readLine();
            String[] keyStrs = line.split(",");
            String[] valueStrs = null;
            Map<String, String> tempMap;
            while ((line = bufferedReader.readLine()) != null) {
                valueStrs = line.split(",");
                tempMap = new HashMap<>();
                for (int i = 0; i < keyStrs.length; i++) {
                    tempMap.put(keyStrs[i], valueStrs[i]);
                }
                elementList.add(tempMap);
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
        return elementList;
    }

    public static void main(String[] args) {
        String filePath = "E:\\1.学习\\4.数据集\\1.FourSquare-NYCandTokyoCheck-ins\\dataset_TSMC2014_NYC.csv";
        List<Map<String, String>> data = CSVRead.readData(filePath);
        MyPrint.showList(data);

    }
}
