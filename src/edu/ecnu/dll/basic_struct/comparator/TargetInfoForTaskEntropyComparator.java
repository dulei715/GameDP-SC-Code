package edu.ecnu.dll.basic_struct.comparator;

import edu.ecnu.dll.basic_struct.pack.TargetInfo;

import java.util.Comparator;

public class TargetInfoForTaskEntropyComparator implements Comparator<TargetInfo> {

    public static final boolean ASCENDING = false;
    public static final boolean DESCENDING = true;

    private Boolean rankType = false;

    public TargetInfoForTaskEntropyComparator(Boolean rankType) {
        this.rankType = rankType;
    }

    @Override
    public int compare(TargetInfo elemA, TargetInfo elemB) {
        int tempCompare = elemA.target.compareTo(elemB.target);
        if (tempCompare != 0) {
            return this.rankType.equals(ASCENDING) ? tempCompare : -tempCompare;
        }
        return elemA.taskID.compareTo(elemB.taskID);
    }
}
