package tools;

import org.junit.Test;
import tools.preprocessing.CoordinateTransformation;

import java.util.Map;

public class CoordinateTransformationTest {
    @Test
    public void fun1() {
        Map<String, Double> stringDoubleMap = CoordinateTransformation.convertMC2LL(402133.625298, 3399042.219073);
//        System.out.println(stringDoubleMap);
//        Map<String, Double> stringDoubleMap2 = CoordinateTransformation.convertMC2LL(3397617.105184, 410301.845147);
//        System.out.println(stringDoubleMap2);
        Double x = stringDoubleMap.get("lng");
        Double y = stringDoubleMap.get("lat");
        System.out.println(x*180/Math.PI);
        System.out.println(y*180/Math.PI);


    }
}
