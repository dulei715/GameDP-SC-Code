package edu.ecnu.dll.dataset.batch.filter;

import java.io.File;
import java.io.FilenameFilter;

public class DataFileNameFilter implements FilenameFilter {
    private String matchingEndName = null;

    public DataFileNameFilter(String matchingEndName) {
        this.matchingEndName = matchingEndName;
    }

    @Override
    public boolean accept(File dir, String name) {
        int index = name.indexOf(this.matchingEndName);
        if (index < 0) {
            return false;
        }
        return true;
    }
}
