package edu.ecnu.dll.run.batch_run_total.batch_combine;

import edu.ecnu.dll.run.batch_run_total.BatchTotalRunA_WorkerRatio;
import edu.ecnu.dll.run.batch_run_total.BatchTotalRunB_TaskValue;
import edu.ecnu.dll.run.batch_run_total.BatchTotalRunC_WorkerRange;
import edu.ecnu.dll.run.batch_run_total.BatchTotalRunD_PrivacyBudget;
import tools.basic.StringUtil;

public class PrivacyBudgetBatchRun {
    public static void main(String[] args) {
        String basicPath = args[0];
        String dataType = args[1];
        String datasetTitle = args[2];
        int batchSize = Integer.valueOf(args[3]);
        String[] params = null;
        for (int i = 0; i < batchSize; i++) {
            String batchNumber = StringUtil.getFixIndexNumberInteger((i+1),3);
//            BatchNonPrivacyRunA_WorkerRatio.main(new String[]{basicPath, dataType, datasetTitle, batchNumber});
            params = new String[]{basicPath, dataType, datasetTitle, batchNumber};
            BatchTotalRunD_PrivacyBudget.main(params);
        }
    }
}
