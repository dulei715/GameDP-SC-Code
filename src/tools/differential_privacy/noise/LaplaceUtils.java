package tools.differential_privacy.noise;

import org.apache.commons.math3.distribution.LaplaceDistribution;

public class LaplaceUtils {
    private LaplaceDistribution laplaceDistribution = null;

    public LaplaceUtils(double sensitivity, double epsilon) {
        this.laplaceDistribution = new LaplaceDistribution(0, sensitivity / epsilon);
    }

    public static double[] getLaplaceNoise(double sensitivity, double epsilon, int number){
        LaplaceDistribution laplaceDistribution = new LaplaceDistribution(0, sensitivity/epsilon);
        double[] result = laplaceDistribution.sample(number);
        return result;
    }

    public static double getLaplaceNoise(double sensitivity, double epsilon) {
        LaplaceDistribution laplaceDistribution = new LaplaceDistribution(0, sensitivity / epsilon);
        return laplaceDistribution.sample();
    }

    public double[] getLaplaceNoise(int number) {
        return this.laplaceDistribution.sample(number);
    }

    public double getLaplaceNoise() {
        return this.laplaceDistribution.sample();
    }

}
