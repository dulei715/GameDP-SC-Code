package edu.ecnu.dll.basic_struct.comparator;

import edu.ecnu.dll.basic_struct.pack.single_agent_info.sub_class.sub_class.TaskTargetInfo;

import java.util.Comparator;

public class TargetInfoForTaskEntropyComparator implements Comparator<TaskTargetInfo> {

    public static final boolean ASCENDING = false;
    public static final boolean DESCENDING = true;

    private Boolean rankType = false;

    public TargetInfoForTaskEntropyComparator(Boolean rankType) {
        this.rankType = rankType;
    }

    @Override
    public int compare(TaskTargetInfo elemA, TaskTargetInfo elemB) {
        int tempCompare = elemA.getTarget().compareTo(elemB.getTarget());
        if (tempCompare != 0) {
            return this.rankType.equals(ASCENDING) ? tempCompare : -tempCompare;
        }
        return elemA.getTaskID().compareTo(elemB.getTaskID());
    }
}
