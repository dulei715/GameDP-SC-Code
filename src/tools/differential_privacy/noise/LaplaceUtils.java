package tools.differential_privacy.noise;

import org.apache.commons.math3.distribution.LaplaceDistribution;
import tools.basic.TwoDimensionDoubleComparator;
import tools.io.MyPrint;

import java.util.Arrays;

public class LaplaceUtils {
    private LaplaceDistribution laplaceDistribution = null;
    private static final int DISTANCE_TAG = 0;
    private static final int BUDGET_TAG = 1;

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

    public static double[] getMaximumLikelihoodEstimation(double[][] distanceBudgetArray) {
        double[] result = new double[2];
        int length = distanceBudgetArray.length;
        double[][] tempDistanceBudgetArray = new double[length][2];
        double budgetSum = 0;
        int resultLeftIndex;
        System.arraycopy(distanceBudgetArray, 0, tempDistanceBudgetArray, 0, length);
//        MyPrint.show2DimensionDoubleArray(distanceBudgetArray);
//        System.out.println("************************************************");
//        MyPrint.show2DimensionDoubleArray(tempBudgetArray);
        Arrays.sort(tempDistanceBudgetArray, new TwoDimensionDoubleComparator());
//        System.out.println("************************************************");
//        MyPrint.show2DimensionDoubleArray(tempBudgetArray);
        for (int i = 0; i < length; i++) {
            budgetSum += tempDistanceBudgetArray[i][BUDGET_TAG];
        }
        for (resultLeftIndex = length - 1; resultLeftIndex >= 0; resultLeftIndex--) {
            if (budgetSum <= 0) {
                break;
            }
            budgetSum -= tempDistanceBudgetArray[resultLeftIndex][BUDGET_TAG] * 2;
        }
        if (Math.abs(budgetSum) < 1e-6) {
            result[0] = result[1] = tempDistanceBudgetArray[resultLeftIndex][DISTANCE_TAG];
        } else {
            result[0] = tempDistanceBudgetArray[resultLeftIndex][DISTANCE_TAG];
            result[1] = tempDistanceBudgetArray[resultLeftIndex+1][DISTANCE_TAG];
        }
        return result;
    }

}
