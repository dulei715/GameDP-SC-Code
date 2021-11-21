package edu.ecnu.dll.basic.basic_struct.data_structure;

import edu.ecnu.dll.basic.basic_struct.pack.single_agent_info.SingleInfoPack;

import java.util.*;

public class PreferenceTable<E extends SingleInfoPack, F extends Comparator<E>> {
    public Integer taskSize = null;
    public F comparator = null;
    public List<E>[] table = null;

    public PreferenceTable(Integer taskSize, F comparator) {
        this.taskSize = taskSize;
        this.comparator = comparator;
        this.table = new ArrayList[this.taskSize];
        for (int i = 0; i < this.taskSize; i++) {
            this.table[i] = new ArrayList<>();
        }
    }

    public void setPreferenceTable(List<E>[] table) {
        this.table = table;
        for (int i = 0; i < taskSize; i++) {
            Collections.sort(this.table[i], this.comparator);
        }
    }

    public static <E, F extends Comparator<E>> void sortedPreferenceTable(List<E>[] table, F comparator) {
        for (int i = 0; i < table.length; i++) {
            Collections.sort(table[i], comparator);
        }
    }

    public static <E, F extends Comparator<E>> void sortedPreferenceTable(List<E> tableElement, F comparator) {
        Collections.sort(tableElement, comparator);
    }

    public Iterator<E>[] getNewIteratorArray() {
        Iterator<E>[] iterators = new Iterator[this.taskSize];
        for (int i = 0; i < this.taskSize; i++) {
            iterators[i] = this.table[i].iterator();
        }
        return iterators;
    }



}
