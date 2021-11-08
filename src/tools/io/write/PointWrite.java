package tools.io.write;

import tools.struct.Point;

import java.io.IOException;
import java.util.Collection;

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
}
