package function;

import edu.ecnu.dll.basic_struct.pack.DistanceBudgetPair;
import org.junit.Test;
import tools.basic.BasicCalculation;
import tools.differential_privacy.noise.LaplaceUtils;
import tools.io.print.MyPrint;

import java.util.Arrays;
import java.util.TreeSet;

public class MaximumLikelihoodEstimationTest {
    @Test
    public void fun1() {
        int size = 100;
        double distance = 10;
        Double[] privacyBudgetArray, noiseDistance;
        privacyBudgetArray = BasicCalculation.getRandomDoubleValueArrayInRange(0, 0.5, 2, size);

//        Arrays.sort(privacyBudgetArray);

        noiseDistance = new Double[size];
        for (int i = 0; i < size; i++) {
            noiseDistance[i] = distance + LaplaceUtils.getLaplaceNoise(1, privacyBudgetArray[i]);
        }

        MyPrint.showDoubleArray(privacyBudgetArray);
        MyPrint.showDoubleArray(noiseDistance);


        // 对每个子序列进行极大似然估计
        TreeSet<DistanceBudgetPair> sortedDistanceBudgetPariSet = new TreeSet<>();
        for (int i = 0; i < size; i++) {
            sortedDistanceBudgetPariSet.add(new DistanceBudgetPair(noiseDistance[i], privacyBudgetArray[i]));
            double[] estimation = LaplaceUtils.getMaximumLikelihoodEstimationInGivenPoint(sortedDistanceBudgetPariSet);
            System.out.println(estimation[0]+"; "+estimation[1]);
        }

    }
}
