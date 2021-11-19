package edu.ecnu.dll.scheme.struct.task;

import edu.ecnu.dll.basic_struct.agent.Task;
import edu.ecnu.dll.basic_struct.comparator.UtilityDistanceIDComparator;
import edu.ecnu.dll.basic_struct.pack.UtilityDistanceIDPair;

import java.util.Iterator;
import java.util.TreeSet;

public class NonPrivacyTask extends Task {
    public static final UtilityDistanceIDComparator UTILITY_DISTANCE_ID_COMPARATOR = new UtilityDistanceIDComparator(UtilityDistanceIDComparator.UTILITY_DESCENDING_ORDER, UtilityDistanceIDComparator.DISTANCE_ASCENDING_ORDER); ;
    public TreeSet<UtilityDistanceIDPair> utilityDistanceWorkerIDSet = null;

    public NonPrivacyTask() {
    }

    public NonPrivacyTask(double[] location) {
        super(location, 0);
        this.utilityDistanceWorkerIDSet = new TreeSet<>(UTILITY_DISTANCE_ID_COMPARATOR.reversed());
    }

    public NonPrivacyTask(double[] location, double valuation) {
        super(location, valuation);
        this.utilityDistanceWorkerIDSet = new TreeSet<>(UTILITY_DISTANCE_ID_COMPARATOR.reversed());
    }

    public UtilityDistanceIDPair getFirstElement() {
        return utilityDistanceWorkerIDSet.first();
    }

    public void addElement(Integer id, Double utilityValue, Double distance) {
        this.utilityDistanceWorkerIDSet.add(new UtilityDistanceIDPair(utilityValue, distance, id));
    }

    public void removeFirstElementByID(Integer id) {
        Iterator<UtilityDistanceIDPair> idPairIterator = this.utilityDistanceWorkerIDSet.iterator();
        UtilityDistanceIDPair tempPair;
        while (idPairIterator.hasNext()) {
            tempPair = idPairIterator.next();
            if (tempPair.getId().equals(id)) {
                idPairIterator.remove();
            }
        }
    }

}
