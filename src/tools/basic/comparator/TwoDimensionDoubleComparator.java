package tools.basic.comparator;

import java.util.Comparator;

public class TwoDimensionDoubleComparator implements Comparator<Double[]> {

    @Override
    public int compare(Double[] arrA, Double[] arrB) {
        if (arrA.length != arrB.length) {
            throw new RuntimeException("The length of two array is not equal!");
        }
        for (int i = 0; i < arrA.length; i++) {
            if (arrA[i] < arrB[i]) {
                return -1;
            } else if (arrA[i] > arrB[i]) {
                return 1;
            }
        }
        return 0;
    }
}
