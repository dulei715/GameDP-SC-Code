package basic;

import org.junit.Test;
import tools.basic.BasicArray;
import tools.basic.BasicCalculation;
import tools.differential_privacy.compare.impl.LaplaceProbabilityDensityFunction;
import tools.differential_privacy.noise.LaplaceUtils;
import tools.io.print.MyPrint;
import tools.io.read.PointRead;
import tools.io.read.TwoDimensionDoubleRead;
import tools.struct.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.TreeSet;

public class BasicTest {
    @Test
    public void fun1() {
        int step = 1000000;
    }

    @Test
    public void fun2() {
        List<Integer> list = new ArrayList<>(new ArrayList<>(0));
        list.add(2);
        list.add(3);
        list.add(4);
        MyPrint.showList(list);
    }

    @Test
    public void fun3() {
        double value = -1e30;
        System.out.println(value);
    }

    @Test
    public void fun4() {
        String[] initializedArray = BasicArray.getInitializedArray("abcd", 10);
        MyPrint.showStringArray(initializedArray);
    }
    
    @Test
    public void fun5() {
        int[] data = new int[] {
                23, -6, 11, -4, -7, 8, 9
        };
        int[] divides = new int[]{3, -4};
        for (int j = 0; j < divides.length; j++) {
            for (int i = 0; i < data.length; i++) {
                System.out.print(data[i] % divides[j] + ", ");
            }
            System.out.println();
        }
    }

    @Test
    public void fun6() {
        TreeSet<Integer> treeSet = new TreeSet<>();
        treeSet.add(23);
        treeSet.add(78);
        treeSet.add(56);

        Object[] arr = treeSet.stream().toArray();
        for (int i = 0; i < arr.length; i++) {
            System.out.println(arr[i]);
        }

    }

    @Test
    public void fun7() {
        double[] pointValueA = new double[] {40.81705715, -73.94792318};
        double[] pointValueB = new double[] {40.96462184, -74.06785745};
        double result = BasicCalculation.getDistanceFrom2LngLat(pointValueA[1], pointValueA[0], pointValueB[1], pointValueB[0]);
        System.out.println(result);
    }

    @Test
    public void fun8() {
        int size = 20;
//        double distance = 10;
        Random random = new Random();
        double[] noiseDistances = new double[size];
        double[] epsilons = new double[size];
        for (int i = 0; i < epsilons.length; i++) {
            epsilons[i] = Math.random();
            noiseDistances[i] = random.nextInt(100) + LaplaceUtils.getLaplaceNoise(1, epsilons[i]);
        }

        for (int i = 0; i < epsilons.length; i++) {
            for (int j = i + 1; j < epsilons.length; j++) {
                Double differ = noiseDistances[i] - noiseDistances[j];
                double pcfResult = LaplaceProbabilityDensityFunction.probabilityDensityFunction(noiseDistances[i], noiseDistances[j], epsilons[i], epsilons[j]);
                System.out.println(differ + "; " + pcfResult);
                if (differ < 0 && pcfResult < 0.5 || differ > 0 && pcfResult > 0.5) {
                    System.out.println("haha");
                }
            }
        }

    }

    @Test
    public void basicFun1() {
        Double valueA = Double.MAX_VALUE;
        Double valueB = Double.MIN_VALUE;
        Double valueC = valueB - valueA;
        System.out.println(valueA);
        System.out.println(valueB);
        System.out.println(valueC);
        System.out.println(valueA*valueB);
        System.out.println(valueB == 0);
    }


    @Test
    public void fun9(){
//        String path = "E:\\1.学习\\4.数据集\\dataset\\original\\chengdu\\worker_budget.txt";
//        String path = "E:\\1.学习\\4.数据集\\dataset\\original\\chengdu_default\\worker_noise_distance.txt";
        String path = "E:\\1.学习\\4.数据集\\dataset\\original\\chengdu_total_dataset\\task_worker_1_2_0\\" +  "worker_noise_distance.txt";
        List<Double[]>[] lists = TwoDimensionDoubleRead.readTopKDouble(path, 10);
        for (int i = 0; i < lists.length; i++) {
            for (Double[] doubles : lists[i]) {
                for (int j = 0; j < doubles.length; j++) {
                    System.out.print(doubles[j] + " ");
                }
                System.out.println();
            }
        }
    }

























}
