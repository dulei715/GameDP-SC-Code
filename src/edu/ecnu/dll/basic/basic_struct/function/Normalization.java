package edu.ecnu.dll.basic.basic_struct.function;

public class Normalization {
    public static Double getNormalizedValue(Double originalValue, Double minValue, Double maxValue) {
        return (originalValue - minValue) / (maxValue - minValue);
    }

}
