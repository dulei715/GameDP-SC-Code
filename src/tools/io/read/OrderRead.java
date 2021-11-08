package tools.io.read;

import tools.io.print.MyPrint;
import tools.struct.Order;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class OrderRead {
    public static final String SPLIT_TAG = " ";

    protected List<Order> orderList = null;
    protected String filePath;
    protected Integer dataSize = null;

    public OrderRead(String filePath) {
        this.filePath = filePath;
    }

    /**
     * The first line should be the size of the dataset
     */

    public static List<Order> readOrderWithFirstLineCount(String filePath) {
        BufferedReader bufferedReader = null;
        String line = null;
        String[] dataElement;
        int dataSize;
        List<Order> pointList = null;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));
            dataSize = Integer.valueOf(bufferedReader.readLine());
            pointList = new ArrayList<>(dataSize);
            while ((line = bufferedReader.readLine()) != null) {
                dataElement = line.split(SPLIT_TAG);
                pointList.add(new Order(Long.valueOf(dataElement[0]), Integer.valueOf(dataElement[1]), Integer.valueOf(dataElement[2]), Integer.valueOf(dataElement[3])));
            }
            if (dataSize != pointList.size()) {
                throw new RuntimeException("The size of dataset is not inconsistent!");
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
        return pointList;
    }

    public List<Order> getOrderList() {
        return orderList;
    }

    public String getFilePath() {
        return filePath;
    }

    public Integer getTotalDataSize() {
        return dataSize;
    }

    public Integer getRealDataSize() {
        return this.orderList.size();
    }


    public static void main(String[] args) {
        String filePath = "E:\\1.学习\\4.数据集\\dataset\\original\\chengdu_order.txt";
        List<Order> orders = OrderRead.readOrderWithFirstLineCount(filePath);
        MyPrint.showList(orders);
        System.out.println(orders.size());
    }

}
