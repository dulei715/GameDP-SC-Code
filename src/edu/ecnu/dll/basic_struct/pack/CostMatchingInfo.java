package edu.ecnu.dll.basic_struct.pack;

public class CostMatchingInfo {
    private Double cost = null;
    private int[][] matching = null;

    public CostMatchingInfo() {
    }

    public CostMatchingInfo(Double cost, int[][] matching) {
        this.cost = cost;
        this.matching = matching;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public int[][] getMatching() {
        return matching;
    }

    public void setMatching(int[][] matching) {
        this.matching = matching;
    }
}
