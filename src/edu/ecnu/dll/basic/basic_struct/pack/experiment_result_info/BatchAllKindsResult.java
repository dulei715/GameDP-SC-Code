package edu.ecnu.dll.basic.basic_struct.pack.experiment_result_info;

import java.util.List;

public class BatchAllKindsResult {
    public String title = null;
    public List<PackExtendedExperimentResult> allKindsResult = null;

    public BatchAllKindsResult() {
    }

    public BatchAllKindsResult(String title, List<PackExtendedExperimentResult> allKindsResult) {
        this.title = title;
        this.allKindsResult = allKindsResult;
    }
}
