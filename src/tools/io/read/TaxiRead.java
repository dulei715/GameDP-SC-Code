package tools.io.read;

import tools.io.print.MyPrint;
import tools.struct.Order;
import tools.struct.Taxi;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TaxiRead {
    public static final String SPLIT_TAG = " ";

    protected List<Taxi> taxiList = null;
    protected String filePath;
    protected Integer dataSize = null;

    public TaxiRead(String filePath) {
        this.filePath = filePath;
    }

    /**
     * The first line should be the size of the dataset
     */

    public static List<Taxi> readTaxi(String filePath) {
        BufferedReader bufferedReader = null;
        String line = null;
        String[] dataElement;
        int dataSize;
        List<Taxi> taxiList = new ArrayList<>();
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));
            while ((line = bufferedReader.readLine()) != null) {
                dataElement = line.split(SPLIT_TAG);
                taxiList.add(new Taxi(Integer.valueOf(dataElement[0]), Integer.valueOf(dataElement[1])));
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
        return taxiList;
    }

    public List<Taxi> getTaxiList() {
        return taxiList;
    }

    public String getFilePath() {
        return filePath;
    }

    public Integer getTotalDataSize() {
        return dataSize;
    }

    public Integer getRealDataSize() {
        return this.taxiList.size();
    }


    public static void main(String[] args) {
        String filePath = "E:\\1.学习\\4.数据集\\dataset\\original\\chengdu_taxi.txt";
        List<Taxi> taxis = TaxiRead.readTaxi(filePath);
        MyPrint.showList(taxis);
        System.out.println(taxis.size());
    }
}
