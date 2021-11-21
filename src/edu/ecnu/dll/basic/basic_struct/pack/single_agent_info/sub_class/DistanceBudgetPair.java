package edu.ecnu.dll.basic.basic_struct.pack.single_agent_info.sub_class;

import edu.ecnu.dll.basic.basic_struct.pack.single_agent_info.SingleInfoPack;

public class DistanceBudgetPair extends SingleInfoPack implements Comparable<DistanceBudgetPair> {
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
