package tools;

import org.junit.Test;
import tools.basic.BasicArray;
import tools.differential_privacy.noise.LaplaceUtils;
import tools.io.MyPrint;

import java.util.Arrays;

public class LaplaceUtilsTest {
    @Test
    public void fun1() {
        double[][] data = new double[100][2];
        for (int i = 0; i < data.length; i++) {
            data[i][0] = Math.random()*100;
            data[i][1] = Math.random();
        }

        BasicArray.twoDimensionalDoubleArraySort(data);

        int[] result = LaplaceUtils.getMaximumLikelihoodEstimation(data);
        MyPrint.showIntegerArray(result);
        double[] resultA = LaplaceUtils.getMaximumLikelihoodEstimationInGivenPoint(data);
        MyPrint.showDoubleArray(resultA);

    }

}
