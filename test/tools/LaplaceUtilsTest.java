package tools;

import org.junit.Test;
import tools.differential_privacy.noise.LaplaceUtils;

public class LaplaceUtilsTest {
    @Test
    public void fun1() {
        double[][] data = new double[3][2];
        for (int i = 0; i < data.length; i++) {
            data[i][0] = Math.random()*100;
            data[i][1] = Math.random();
        }


        LaplaceUtils.getMaximumLikelihoodEstimation(data);

    }

}
