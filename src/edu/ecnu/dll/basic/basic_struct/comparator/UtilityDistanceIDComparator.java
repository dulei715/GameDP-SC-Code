package edu.ecnu.dll.basic.basic_struct.comparator;

import edu.ecnu.dll.basic.basic_struct.pack.UtilityDistanceIDPair;
import tools.io.print.MyPrint;

import java.util.Comparator;
import java.util.TreeSet;

public class UtilityDistanceIDComparator implements Comparator<UtilityDistanceIDPair> {

    public static final Integer UTILITY_ASCENDING_ORDER = 1;
    public static final Integer UTILITY_DESCENDING_ORDER = -1;

    public static final Integer DISTANCE_ASCENDING_ORDER = 1;
    public static final Integer DISTANCE_DESCENDING_ORDER = -1;

    private Integer utilityOrder = null;
    private Integer distanceOrder = null;

    public UtilityDistanceIDComparator(Integer utilityOrder, Integer distanceOrder) {
        if (utilityOrder != UTILITY_ASCENDING_ORDER && utilityOrder != UTILITY_DESCENDING_ORDER || distanceOrder != DISTANCE_ASCENDING_ORDER && distanceOrder != DISTANCE_DESCENDING_ORDER) {
            throw new RuntimeException("The parameter is wrong!");
        }
        this.utilityOrder = utilityOrder;
        this.distanceOrder = distanceOrder;
    }

    @Override
    public int compare(UtilityDistanceIDPair elemA, UtilityDistanceIDPair elemB) {
        if (elemA == null && elemB == null) {
            return 0;
        }
        if (elemA == null) {
            return 1;
        }
        if (elemB == null) {
            return -1;
        }

        Double utilityA = elemA.getUtility();
        Double utilityB = elemB.getUtility();
        int utilityCompared = utilityA.compareTo(utilityB) * utilityOrder;
        if (utilityCompared < 0) {
            return -1;
        } else if (utilityCompared > 0) {
            return 1;
        }

        Double distanceA = elemA.getDistance();
        Double distanceB = elemB.getDistance();
        int distanceCompared = distanceA.compareTo(distanceB) * distanceOrder;
        if (distanceCompared < 0) {
            return -1;
        } else if (distanceCompared > 0) {
            return 1;
        }

        Integer idA = elemA.getId();
        Integer idB = elemB.getId();

        return idA.compareTo(idB) ;
    }

    public static void main(String[] args) {
        Comparator<UtilityDistanceIDPair> comparator = new UtilityDistanceIDComparator(UtilityDistanceIDComparator.UTILITY_DESCENDING_ORDER, UtilityDistanceIDComparator.DISTANCE_ASCENDING_ORDER);
        TreeSet<UtilityDistanceIDPair> treeSet = new TreeSet<>(comparator);
        treeSet.add(new UtilityDistanceIDPair(3.0, 2.0, 1));
        treeSet.add(new UtilityDistanceIDPair(3.0, 1.0, 1));
        treeSet.add(new UtilityDistanceIDPair(5.0, 7.0, 1));
        treeSet.add(new UtilityDistanceIDPair(5.0, 7.0, 2));
        treeSet.add(new UtilityDistanceIDPair(5.0, 6.8, 2));
        treeSet.add(new UtilityDistanceIDPair(4.0, 3.0, 3));
        treeSet.add(new UtilityDistanceIDPair(1.0, 4.0, 4));

        MyPrint.showSet(treeSet);

    }

}
