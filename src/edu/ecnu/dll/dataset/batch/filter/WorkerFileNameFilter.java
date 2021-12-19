package edu.ecnu.dll.dataset.batch.filter;

import java.io.File;
import java.io.FilenameFilter;

public class WorkerFileNameFilter implements FilenameFilter {
    @Override
    public boolean accept(File dir, String name) {
        int index = name.indexOf("worker_point.txt");
        if (index < 0) {
            return false;
        }
        return true;
    }
}
