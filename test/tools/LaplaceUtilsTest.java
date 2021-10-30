package tools;

import org.junit.Test;
import tools.basic.BasicArray;
import tools.differential_privacy.compare.impl.LaplaceProbabilityDensityFunction;
import tools.differential_privacy.noise.LaplaceUtils;
import tools.io.print.MyPrint;

import java.util.Random;

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

    public int comparePCFFunction(double noiseDistanceA, double noiseDistanceB, double privacyBudgetA, double privacyBudgetB) {
        double pcfValue = LaplaceProbabilityDensityFunction.probabilityDensityFunction(noiseDistanceA, noiseDistanceB, privacyBudgetA, privacyBudgetB);
        if (pcfValue > 0.5){
            return -1;
        }
        if (pcfValue < 0.5) {
            return 1;
        }
        return 0;
    }

    @Test
    public void fun2 () {
        int size = 1000;
        double[] basicDistances = new double[size];
        double[] noiseDistances = new double[size];
        double[] privacyBudgets = new double[size];
        for (int i = 0; i < size; i++) {
            privacyBudgets[i] = Math.random();
            basicDistances[i] = new Random().nextInt(20000);
            noiseDistances[i] = basicDistances[i] + LaplaceUtils.getLaplaceNoise(1, privacyBudgets[i]);
        }
        MyPrint.showDoubleArray(noiseDistances);
        MyPrint.showDoubleArray(privacyBudgets);

        MyPrint.showSplitLine("*", 200);

//        double pcfValueA, pcfValueB;
        int valueA, valueB;

        for (int i = 0; i < size; i++) {
            for (int j = i + 1; j < size; j++) {
                valueA = comparePCFFunction(noiseDistances[i], noiseDistances[j], privacyBudgets[i], privacyBudgets[j]);
                valueB = comparePCFFunction(noiseDistances[j], noiseDistances[i], privacyBudgets[j], privacyBudgets[i]);
                if (valueA != -valueB) {
                    System.out.println(i + ", " + j);
                }
            }
        }

        MyPrint.showSplitLine("*", 200);



    }

}
