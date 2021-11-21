package tools.differential_privacy.noise;

import edu.ecnu.dll.basic.basic_struct.pack.single_agent_info.sub_class.DistanceBudgetPair;
import org.apache.commons.math3.distribution.LaplaceDistribution;
import tools.basic.BasicCalculation;

import java.util.Iterator;
import java.util.TreeSet;

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

    public Double getLaplaceNoise() {
        return this.laplaceDistribution.sample();
    }

    public static Double[] getLaplaceNoiseWithOriginalValue(Double originalValue, Double[] privacyBudgets) {
        Double[] result = new Double[privacyBudgets.length];
        for (int i = 0; i < privacyBudgets.length; i++) {
            result[i] = originalValue + getLaplaceNoise(1, privacyBudgets[i]);
        }
        return result;
    }

    /**
     * 根据给定的distance和budget获取极大似然估计的值
     * @param sortedDistanceBudgetArray
     * @return 返回估计范围的两个端点值
     */
    @Deprecated
    public static int[] getMaximumLikelihoodEstimation(double[][] sortedDistanceBudgetArray) {
        int[] result = new int[2];
        int length = sortedDistanceBudgetArray.length;
        double[][] tempDistanceBudgetArray = new double[length][2];
        double budgetSum = 0;
        int resultLeftIndex;
//        System.arraycopy(sortedDistanceBudgetArray, 0, tempDistanceBudgetArray, 0, length);
//        MyPrint.show2DimensionDoubleArray(distanceBudgetArray);
//        System.out.println("************************************************");
//        MyPrint.show2DimensionDoubleArray(tempBudgetArray);
//        Arrays.sort(tempDistanceBudgetArray, new TwoDimensionDouComparator());
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
//            result[0] = result[1] = tempDistanceBudgetArray[resultLeftIndex][DISTANCE_TAG];
            result[0] = result[1] = resultLeftIndex;
        } else {
//            result[0] = tempDistanceBudgetArray[resultLeftIndex][DISTANCE_TAG];
//            result[1] = tempDistanceBudgetArray[resultLeftIndex+1][DISTANCE_TAG];
            result[0] = resultLeftIndex;
            result[1] = resultLeftIndex + 1;
        }
        return result;
    }

    public static int[] getMaximumLikelihoodEstimation(TreeSet<DistanceBudgetPair> sortedDistanceBudgetPairSet) {
        if (sortedDistanceBudgetPairSet.size() == 1) {
            return new int[]{0, 0};
        }
        int[] result = new int[2];
        int length = sortedDistanceBudgetPairSet.size();
//        double[][] tempDistanceBudgetArray = new double[length][2];
        double budgetSum = 0;
        int resultLeftIndex;
        DistanceBudgetPair tempDistanceBudgetPair;
//        System.arraycopy(sortedDistanceBudgetPair, 0, tempDistanceBudgetArray, 0, length);
//        Arrays.sort(tempDistanceBudgetArray, new TwoDimensionDouComparator());
//        System.out.println("************************************************");
//        MyPrint.show2DimensionDoubleArray(tempBudgetArray);
        Iterator<DistanceBudgetPair> descendingIterator = sortedDistanceBudgetPairSet.descendingIterator();
        while (descendingIterator.hasNext()) {
            tempDistanceBudgetPair = descendingIterator.next();
            budgetSum += tempDistanceBudgetPair.budget;
        }

        descendingIterator = sortedDistanceBudgetPairSet.descendingIterator();
        for (resultLeftIndex = length - 1; descendingIterator.hasNext(); resultLeftIndex--) {
            if (budgetSum <= 0) {
                break;
            }
            tempDistanceBudgetPair = descendingIterator.next();
            budgetSum -= tempDistanceBudgetPair.budget*2;
        }

        if (resultLeftIndex < 0) {
            result[0] = result[1] = 0;
        } else if (Math.abs(budgetSum) < 1e-6) {
            result[0] = result[1] = resultLeftIndex;
        } else {
            result[0] = resultLeftIndex;
            result[1] = resultLeftIndex + 1;
        }
        return result;
    }

    /**
     * 根据给定的distance和budget获取极大似然估计的值(该值必须是给定的点中的一个)
     * @param sortedDistanceBudgetArray
     * @return
     */
    public static double[] getMaximumLikelihoodEstimationInGivenPoint(final double[][] sortedDistanceBudgetArray) {
        int[] index = getMaximumLikelihoodEstimation(sortedDistanceBudgetArray);
        double leftResult = BasicCalculation.getWeightedFirstVectorNormOfDifference(sortedDistanceBudgetArray, sortedDistanceBudgetArray[index[0]][DISTANCE_TAG]);
        double rightResult = BasicCalculation.getWeightedFirstVectorNormOfDifference(sortedDistanceBudgetArray, sortedDistanceBudgetArray[index[1]][DISTANCE_TAG]);
        return leftResult <= rightResult ? new double[]{sortedDistanceBudgetArray[index[0]][DISTANCE_TAG],sortedDistanceBudgetArray[index[0]][BUDGET_TAG]} : new double[]{sortedDistanceBudgetArray[index[1]][DISTANCE_TAG],sortedDistanceBudgetArray[index[1]][BUDGET_TAG]};
    }

    public static double[] getMaximumLikelihoodEstimationInGivenPoint(final TreeSet<DistanceBudgetPair> sortedDistanceBudgetPairSet) {
        int[] index = getMaximumLikelihoodEstimation(sortedDistanceBudgetPairSet);
//        double leftResult = BasicCalculation.getWeightedFirstVectorNormOfDifference(sortedDistanceBudgetPairSet, sortedDistanceBudgetPairSet[index[0]][DISTANCE_TAG]);
        DistanceBudgetPair[] distanceBudgetPairArray = sortedDistanceBudgetPairSet.toArray(new DistanceBudgetPair[0]);
        double leftResult = BasicCalculation.getWeightedFirstVectorNormOfDifference(distanceBudgetPairArray, distanceBudgetPairArray[index[0]].distance);
        double rightResult = BasicCalculation.getWeightedFirstVectorNormOfDifference(distanceBudgetPairArray, distanceBudgetPairArray[index[1]].distance);
        return leftResult <= rightResult ? new double[]{distanceBudgetPairArray[index[0]].distance, distanceBudgetPairArray[index[0]].budget} : new double[]{distanceBudgetPairArray[index[1]].distance, distanceBudgetPairArray[index[1]].budget};
    }

    public static double[] getMaximumLikelihoodEstimationInGivenPoint(final TreeSet<DistanceBudgetPair> sortedDistanceBudgetPairSet, final DistanceBudgetPair newDistanceBudget) {
        // todo: 想想怎么优化
        TreeSet<DistanceBudgetPair> newSortedDistanceBudgetPairSet = new TreeSet<>();
        newSortedDistanceBudgetPairSet.addAll(sortedDistanceBudgetPairSet);
        newSortedDistanceBudgetPairSet.add(newDistanceBudget);
        return getMaximumLikelihoodEstimationInGivenPoint(newSortedDistanceBudgetPairSet);

    }

}
