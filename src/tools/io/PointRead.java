package tools.io;

import tools.struct.Point;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PointRead {
    private List<Point> pointSet = null;
    private String filePath;
    private Integer dataSize = null;

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
            this.pointSet = new ArrayList<>(this.dataSize);
            while ((line = bufferedReader.readLine()) != null) {
                ++i;
                if (i % scale != 0) {
                    continue;
                }
                dataElement = line.split(" ");
                this.pointSet.add(new Point(Double.valueOf(dataElement[0]), Double.valueOf(dataElement[1])));
            }
            if (this.dataSize.equals(this.pointSet.size())) {
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
    }

    public void readPoint() {
        this.readPoint(1);
    }

    public static Set<Point> readPoint(String filePath) {
        BufferedReader bufferedReader = null;
        String line = null;
        String[] dataElement;
        int dataSize;
        Set<Point> pointSet = null;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));
            dataSize = Integer.valueOf(bufferedReader.readLine());
            pointSet = new HashSet<>(dataSize);
            while ((line = bufferedReader.readLine()) != null) {
                dataElement = line.split(" ");
                pointSet.add(new Point(Double.valueOf(dataElement[0]), Double.valueOf(dataElement[1])));
            }
            if (dataSize != pointSet.size()) {
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
        return pointSet;
    }

    public List<Point> getPointList() {
        return pointSet;
    }

    public String getFilePath() {
        return filePath;
    }

    public Integer getTotalDataSize() {
        return dataSize;
    }

    public Integer getRealDataSize() {
        return this.pointSet.size();
    }

    public static void main(String[] args) {
        String filePath = "E:\\1.学习\\4.论文\\程鹏\\dataset\\dataset\\Chengdu\\chengdu.node";
        PointRead pointRead = new PointRead(filePath);
        pointRead.readPoint();
        List<Point> pointSet = pointRead.getPointList();
        MyPrint.showList(pointSet);
    }
}
