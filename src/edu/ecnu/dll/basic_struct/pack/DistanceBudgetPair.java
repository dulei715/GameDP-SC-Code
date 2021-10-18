package edu.ecnu.dll.basic_struct.pack;

public class DistanceBudgetPair implements Comparable<DistanceBudgetPair> {
    public Double distance = null;
    public Double budget = null;

    public DistanceBudgetPair() {
    }

    public DistanceBudgetPair(Double distance, Double budget) {
        this.distance = distance;
        this.budget = budget;
    }

    @Override
    public int compareTo(DistanceBudgetPair distanceBudgetPair) {
        if (this.distance < distanceBudgetPair.distance) {
            return -1;
        } else if (this.distance > distanceBudgetPair.distance) {
            return 1;
        }
        if (this.budget < distanceBudgetPair.budget) {
            return -1;
        } else if (this.budget > distanceBudgetPair.budget) {
            return 1;
        }
        return 0;
    }
}
