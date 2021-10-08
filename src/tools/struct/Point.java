package tools.struct;

import java.util.Objects;

public class Point {
    private double xIndex;
    private double yIndex;

    public Point() {
        this.xIndex = 0;
        this.yIndex = 0;
    }

    public Point(double xIndex, double yIndex) {
        this.xIndex = xIndex;
        this.yIndex = yIndex;
    }

    public double getxIndex() {
        return xIndex;
    }

    public void setxIndex(double xIndex) {
        this.xIndex = xIndex;
    }

    public double getyIndex() {
        return yIndex;
    }

    public void setyIndex(double yIndex) {
        this.yIndex = yIndex;
    }

    public double[] getIndex() {
        return new double[]{this.xIndex, this.yIndex};
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return Double.compare(point.xIndex, xIndex) == 0 && Double.compare(point.yIndex, yIndex) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(xIndex, yIndex);
    }

    @Override
    public String toString() {
        return "Point{" +
                "xIndex=" + xIndex +
                ", yIndex=" + yIndex +
                '}';
    }
}
