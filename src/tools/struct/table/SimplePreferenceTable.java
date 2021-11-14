package tools.struct.table;

import edu.ecnu.dll.basic_struct.comparator.WorkerIDDistanceBudgetPairComparator;
import edu.ecnu.dll.basic_struct.comparator.WorkerIDDistancePairComparator;
import edu.ecnu.dll.basic_struct.pack.single_agent_info.sub_class.WorkerIDDistanceBudgetPair;
import edu.ecnu.dll.basic_struct.pack.single_agent_info.sub_class.WorkerIDDistancePair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class SimplePreferenceTable {
    public Integer taskSize = null;
    public WorkerIDDistancePairComparator comparator = null;
    public List<WorkerIDDistancePair>[] table = null;

    public SimplePreferenceTable(Integer taskSize, WorkerIDDistancePairComparator comparator) {
        this.taskSize = taskSize;
        this.comparator = comparator;
        this.table = new ArrayList[this.taskSize];
        for (int i = 0; i < this.taskSize; i++) {
            this.table[i] = new ArrayList<>();
        }
    }

    public void setPreferenceTable(List<WorkerIDDistancePair>[] table) {
        this.table = table;
        for (int i = 0; i < taskSize; i++) {
            Collections.sort(this.table[i], this.comparator);
        }
    }

    public static void sortedPreferenceTable(List<WorkerIDDistancePair>[] table, WorkerIDDistancePairComparator comparator) {
        for (int i = 0; i < table.length; i++) {
            Collections.sort(table[i], comparator);
        }
    }

    public static void sortedPreferenceTable(List<WorkerIDDistancePair> tableElement, WorkerIDDistancePairComparator comparator) {
        Collections.sort(tableElement, comparator);
    }

    public Iterator<WorkerIDDistancePair>[] getNewIteratorArray() {
        Iterator<WorkerIDDistancePair>[] iterators = new Iterator[this.taskSize];
        for (int i = 0; i < this.taskSize; i++) {
            iterators[i] = this.table[i].iterator();
        }
        return iterators;
    }



}
