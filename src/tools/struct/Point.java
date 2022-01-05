package tools.struct;

import java.util.Objects;

public class Point implements Comparable<Point> {
    private Double xIndex;
    private Double yIndex;

    public Point() {
        this.xIndex = 0.0;
        this.yIndex = 0.0;
    }

    public Point(double xIndex, double yIndex) {
        this.xIndex = xIndex;
        this.yIndex = yIndex;
    }

    public Point(double[] indexes) {
        this.xIndex = indexes[0];
        this.yIndex = indexes[1];
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

    public static Point valueOf(double xIndex, double yIndex) {
        return new Point(xIndex, yIndex);
    }

    public void scalePosition(double factorK, double constA) {
        this.xIndex = this.xIndex * factorK + constA;
        this.yIndex = this.yIndex * factorK + constA;
    }

    @Override
    public String toString() {
        return "Point{" +
                "xIndex=" + xIndex +
                ", yIndex=" + yIndex +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return Double.compare(point.xIndex, xIndex) == 0 &&
                Double.compare(point.yIndex, yIndex) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(xIndex, yIndex);
    }


    @Override
    public int compareTo(Point point) {
        if (this == point) return 0;
        int xCMP = this.xIndex.compareTo(point.xIndex);
        if (xCMP != 0) {
            return xCMP;
        }
        int yCMP = this.yIndex.compareTo(point.yIndex);
        return yCMP;
    }
}
