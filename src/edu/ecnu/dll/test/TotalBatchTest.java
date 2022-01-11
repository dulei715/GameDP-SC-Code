package edu.ecnu.dll.test;

import edu.ecnu.dll.run.batch_run_total.test.BatchNonPrivacyRunA_WorkerRatio;
import sun.applet.Main;
import tools.basic.StringUtil;

public class TotalBatchTest {
    public static void main(String[] args) {
        String basicPath = args[0];
        String dataType = args[1];
        String datasetTitle = args[2];
        int batchSize = Integer.valueOf(args[3]);
        for (int i = 0; i < batchSize; i++) {
            String batchNumber = StringUtil.getFixIndexNumberInteger((i+1),3);
            BatchNonPrivacyRunA_WorkerRatio.main(new String[]{basicPath, dataType, datasetTitle, batchNumber});
        }
    }

    public static void main0(String[] args) {
//        String basicPath = "E:\\gt-dp-cs\\3.normal";
//        String batchSize = String.valueOf(300);
//        NoPrivacyTest.main(new String[]{basicPath, batchSize});
//        MyTest.main1(null);
        System.out.println("xixi");
    }
}
