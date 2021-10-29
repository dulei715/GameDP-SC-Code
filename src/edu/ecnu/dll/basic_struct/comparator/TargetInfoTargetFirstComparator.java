package edu.ecnu.dll.basic_struct.comparator;

import edu.ecnu.dll.basic_struct.pack.TargetInfo;

import java.util.Comparator;

public class TargetInfoTargetFirstComparator implements Comparator<TargetInfo> {
    @Override
    public int compare(TargetInfo elemA, TargetInfo elemB) {
        int tempCompare = elemA.target.compareTo(elemB.target);
        if (tempCompare != 0) {
            return tempCompare;
        }
        return elemA.taskID.compareTo(elemB.taskID);
    }
}
