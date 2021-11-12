package basic;

import org.junit.Test;
import tools.basic.BasicArray;
import tools.basic.BasicCalculation;
import tools.io.print.MyPrint;

import java.util.ArrayList;
import java.util.List;
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

}
