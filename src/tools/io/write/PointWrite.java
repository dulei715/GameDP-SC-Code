package tools.io.write;

import tools.struct.Point;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class PointWrite extends BasicWrite {

    public PointWrite() {
    }

    public PointWrite(String OUTPUT_SPLIT_SYMBOL) {
        super(OUTPUT_SPLIT_SYMBOL);
    }

    public void writePoint(Collection<Point> dataCollection) {
        int i = 0;
        Point tempPoint;
        try {
            super.bufferedWriter.write(String.valueOf(dataCollection.size()));
            super.bufferedWriter.newLine();
            for (Point point : dataCollection) {
                super.bufferedWriter.write(String.valueOf(point.getxIndex()));
                super.bufferedWriter.write(super.OUTPUT_SPLIT_SYMBOL);
                super.bufferedWriter.write(String.valueOf(point.getyIndex()));
                super.bufferedWriter.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writePoint(List<Point> dataList, Set<Integer> declaredIndexSet) {
        Point point;
        try {
            super.bufferedWriter.write(String.valueOf(declaredIndexSet.size()));
            super.bufferedWriter.newLine();
            for (int i = 0; i < dataList.size(); i++) {
                if (!declaredIndexSet.contains(i)) {
                    continue;
                }
                point = dataList.get(i);
                super.bufferedWriter.write(String.valueOf(point.getxIndex()));
                super.bufferedWriter.write(super.OUTPUT_SPLIT_SYMBOL);
                super.bufferedWriter.write(String.valueOf(point.getyIndex()));
                super.bufferedWriter.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writePoint(List<Point> dataList, Set<Integer> declaredIndexSet, double factorK, double constA) {
        Point point;
        try {
            super.bufferedWriter.write(String.valueOf(declaredIndexSet.size()));
            super.bufferedWriter.newLine();
            for (int i = 0; i < dataList.size(); i++) {
                if (!declaredIndexSet.contains(i)) {
                    continue;
                }
                point = dataList.get(i);
//                point.scalePosition(factorK, constA);
                super.bufferedWriter.write(String.valueOf(point.getxIndex() * factorK + constA));
                super.bufferedWriter.write(super.OUTPUT_SPLIT_SYMBOL);
                super.bufferedWriter.write(String.valueOf(point.getyIndex() * factorK + constA));
                super.bufferedWriter.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
