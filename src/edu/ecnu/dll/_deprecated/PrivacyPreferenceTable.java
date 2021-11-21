package edu.ecnu.dll._deprecated;

import edu.ecnu.dll.basic.basic_struct.comparator.WorkerIDNoiseDistanceBudgetPairComparator;
import edu.ecnu.dll.basic.basic_struct.pack.single_agent_info.sub_class.WorkerIDNoiseDistanceBudgetPair;

import java.util.*;

@Deprecated
public class PrivacyPreferenceTable {
    public Integer taskSize = null;
    public WorkerIDNoiseDistanceBudgetPairComparator comparator = null;
    public List<WorkerIDNoiseDistanceBudgetPair>[] table = null;

    public PrivacyPreferenceTable(Integer taskSize, WorkerIDNoiseDistanceBudgetPairComparator comparator) {
        this.taskSize = taskSize;
        this.comparator = comparator;
        this.table = new ArrayList[this.taskSize];
        for (int i = 0; i < this.taskSize; i++) {
            this.table[i] = new ArrayList<>();
        }
    }

    public void setPreferenceTable(List<WorkerIDNoiseDistanceBudgetPair>[] table) {
        this.table = table;
        for (int i = 0; i < taskSize; i++) {
            Collections.sort(this.table[i], this.comparator);
        }
    }

    public static void sortedPreferenceTable(List<WorkerIDNoiseDistanceBudgetPair>[] table, WorkerIDNoiseDistanceBudgetPairComparator comparator) {
        for (int i = 0; i < table.length; i++) {
            Collections.sort(table[i], comparator);
        }
    }

    public static void sortedPreferenceTable(List<WorkerIDNoiseDistanceBudgetPair> tableElement, WorkerIDNoiseDistanceBudgetPairComparator comparator) {
        Collections.sort(tableElement, comparator);
    }

    public Iterator<WorkerIDNoiseDistanceBudgetPair>[] getNewIteratorArray() {
        Iterator<WorkerIDNoiseDistanceBudgetPair>[] iterators = new Iterator[this.taskSize];
        for (int i = 0; i < this.taskSize; i++) {
            iterators[i] = this.table[i].iterator();
        }
        return iterators;
    }



}
