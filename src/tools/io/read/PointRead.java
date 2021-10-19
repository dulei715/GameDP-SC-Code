package tools.io.read;

import tools.io.print.MyPrint;
import tools.struct.Point;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PointRead {
    protected List<Point> pointList = null;
    protected String filePath;
    protected Integer dataSize = null;

    public PointRead(String filePath) {
        this.filePath = filePath;
    }

    /**
     * The first line should be the size of the dataset
     */
    public void readPoint(int scale) {
        BufferedReader bufferedReader = null;
        String line = null;
        String[] dataElement;
        int i = 0;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(this.filePath)));
            this.dataSize = Integer.valueOf(bufferedReader.readLine());
            this.pointList = new ArrayList<>(this.dataSize);
            while ((line = bufferedReader.readLine()) != null) {
                ++i;
                if (i % scale != 0) {
                    continue;
                }
                dataElement = line.split(" ");
                this.pointList.add(new Point(Double.valueOf(dataElement[0]), Double.valueOf(dataElement[1])));
            }
//            if (!this.dataSize.equals(this.pointList.size())) {
//                throw new RuntimeException("The size of dataset is not inconsistent!");
//            }

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
    }

    public void readPoint() {
        this.readPoint(1);
    }

    public static List<Point> readPoint(String filePath) {
        BufferedReader bufferedReader = null;
        String line = null;
        String[] dataElement;
        int dataSize;
        List<Point> pointList = null;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));
            dataSize = Integer.valueOf(bufferedReader.readLine());
            pointList = new ArrayList<>(dataSize);
            while ((line = bufferedReader.readLine()) != null) {
                dataElement = line.split(" ");
                pointList.add(new Point(Double.valueOf(dataElement[0]), Double.valueOf(dataElement[1])));
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

    public List<Point> getPointList() {
        return pointList;
    }

    public String getFilePath() {
        return filePath;
    }

    public Integer getTotalDataSize() {
        return dataSize;
    }

    public Integer getRealDataSize() {
        return this.pointList.size();
    }

    public static void main(String[] args) {
        String filePath = "E:\\1.学习\\4.论文\\程鹏\\dataset\\dataset\\Chengdu\\chengdu.node";
        PointRead pointRead = new PointRead(filePath);
        pointRead.readPoint();
        List<Point> pointSet = pointRead.getPointList();
        MyPrint.showList(pointSet);
    }
}
