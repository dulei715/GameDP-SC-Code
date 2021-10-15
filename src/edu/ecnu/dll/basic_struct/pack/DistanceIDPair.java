package edu.ecnu.dll.basic_struct.pack;

public class DistanceIDPair implements Comparable<DistanceIDPair> {
    public Double distance = null;
    public Integer id = null;

    public DistanceIDPair() {
    }

    public DistanceIDPair(Double distance, Integer id) {
        this.distance = distance;
        this.id = id;
    }

    @Override
    public int compareTo(DistanceIDPair distanceIDPair) {
        if (this.distance < distanceIDPair.distance) {
            return -1;
        }
        if (this.distance > distanceIDPair.distance) {
            return 1;
        }
        return this.id - distanceIDPair.id;
    }
}
