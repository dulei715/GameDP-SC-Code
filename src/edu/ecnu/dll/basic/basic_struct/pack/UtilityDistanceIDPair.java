package edu.ecnu.dll.basic.basic_struct.pack;

public class UtilityDistanceIDPair {
    private Double utility = null;
    private Double distance = null;
    private Integer id = null;

    public UtilityDistanceIDPair() {
    }

    public UtilityDistanceIDPair(Double utility, Double distance, Integer id) {
        this.utility = utility;
        this.distance = distance;
        this.id = id;
    }

    public Double getUtility() {
        return utility;
    }

    public void setUtility(Double utility) {
        this.utility = utility;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "UtilityDistanceIDPair{" +
                "utility=" + utility +
                ", distance=" + distance +
                ", id=" + id +
                '}';
    }
}
