package edu.ecnu.dll.scheme.solution._0_basic;

public abstract class NonPrivacySolution extends Solution { // Task value 保证是[0,1]之间的值
    protected double getUtilityValue(double taskValue, double realDistance) {
        return taskValue  - transformDistanceToValue(realDistance);
    }
}
