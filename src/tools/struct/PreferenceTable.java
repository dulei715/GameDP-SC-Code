package tools.struct;

import edu.ecnu.dll.basic_struct.comparator.TaskWorkerIDComparator;
import edu.ecnu.dll.basic_struct.comparator.WorkerIDDistanceBudgetPairComparator;
import edu.ecnu.dll.basic_struct.pack.WorkerIDDistanceBudgetPair;

import java.util.*;

public class PreferenceTable {
    public Integer taskSize = null;
    public WorkerIDDistanceBudgetPairComparator comparator = null;
    public List<WorkerIDDistanceBudgetPair>[] table = null;

    public PreferenceTable(Integer taskSize, WorkerIDDistanceBudgetPairComparator comparator) {
        this.taskSize = taskSize;
        this.comparator = comparator;
        this.table = new ArrayList[this.taskSize];
        for (int i = 0; i < this.taskSize; i++) {
            this.table[i] = new ArrayList<>();
        }
    }

    public void setPreferenceTable(List<WorkerIDDistanceBudgetPair>[] table) {
        this.table = table;
        for (int i = 0; i < taskSize; i++) {
            Collections.sort(this.table[i], this.comparator);
        }
    }

    public static void sortedPreferenceTable(List<WorkerIDDistanceBudgetPair>[] table, WorkerIDDistanceBudgetPairComparator comparator) {
        for (int i = 0; i < table.length; i++) {
            Collections.sort(table[i], comparator);
        }
    }

    public static void sortedPreferenceTable(List<WorkerIDDistanceBudgetPair> tableElement, WorkerIDDistanceBudgetPairComparator comparator) {
        Collections.sort(tableElement, comparator);
    }

    public Iterator<WorkerIDDistanceBudgetPair>[] getNewIteratorArray() {
        Iterator<WorkerIDDistanceBudgetPair>[] iterators = new Iterator[this.taskSize];
        for (int i = 0; i < this.taskSize; i++) {
            iterators[i] = this.table[i].iterator();
        }
        return iterators;
    }



}
