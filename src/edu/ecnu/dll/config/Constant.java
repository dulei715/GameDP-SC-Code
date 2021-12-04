package edu.ecnu.dll.config;

import java.io.File;

public class Constant {

    public static final String FILE_PATH_SPLIT = File.separator;



//    public static final double ALPHA = 0.1;
    public static final double ALPHA = 1;
    //    public static final double BETA = 0.2;
    public static final double BETA = 1;

    // for worker ratio
    public static final String[] parentPathArray = new String[] {
            FILE_PATH_SPLIT + "task_worker_1_1_0",
            FILE_PATH_SPLIT + "task_worker_1_1_5",
            FILE_PATH_SPLIT + "task_worker_1_2_0",
            FILE_PATH_SPLIT + "task_worker_1_2_5",
            FILE_PATH_SPLIT + "task_worker_1_3_0",
//            FILE_PATH_SPLIT + "task_worker_1_3_5"
    };
    // for privacy budgte
    public static final String[] parentBudgetPathArray = new String[] {
            FILE_PATH_SPLIT + "task_worker_1_2_0" + FILE_PATH_SPLIT + "privacy_change" + FILE_PATH_SPLIT + "privacy_1",
            FILE_PATH_SPLIT + "task_worker_1_2_0" + FILE_PATH_SPLIT + "privacy_change" + FILE_PATH_SPLIT + "privacy_2",
            FILE_PATH_SPLIT + "task_worker_1_2_0" + FILE_PATH_SPLIT + "privacy_change" + FILE_PATH_SPLIT + "privacy_3",
            FILE_PATH_SPLIT + "task_worker_1_2_0" + FILE_PATH_SPLIT + "privacy_change" + FILE_PATH_SPLIT + "privacy_4",
            FILE_PATH_SPLIT + "task_worker_1_2_0" + FILE_PATH_SPLIT + "privacy_change" + FILE_PATH_SPLIT + "privacy_5",
    };
    public static final double[][] parentBudgetRange = new double[][] {
            {1.0, 1.5},
            {1.5, 2.0},
            {2.0, 2.5},
            {2.5, 3.0},
            {3.0, 3.5}
    };
    public static final String parentPathDefault = FILE_PATH_SPLIT + "task_worker_1_2_0";

    // for task value
//    public static final Double[] taskValueArray = new Double[] {
//            5.0, 10.0, 15.0, 20.0, 25.0
//    };
//    public static final Double[] taskValueArray = new Double[] {
//            3.0, 6.0, 9.0, 12.0, 15.0
//    };
    public static final Double[] taskValueArray = new Double[] {
            1.5, 3.0, 4.5, 6.0, 7.5
    };
//    public static final Double[] taskValueArray = new Double[] {
//            5.0*5, 10.0*5, 15.0*5, 20.0*5, 25.0*5
//    };
    public static final Double taskValueDefault = 4.5;

    // for worker range
//    public static final Double[] workerRangeArray = new Double[] {
//            10.0, 15.0, 20.0, 25.0, 30.0
//    };
    public static final Double[] workerRangeArray = new Double[] {
            0.8, 1.1, 1.4, 1.7, 2.0
    };
//    public static final Double workerRangeDefault = 20.0;
    public static final Double workerRangeDefault = 1.4;
//    public static final Double workerRangeDefault = 40.0;
//    public static final Double[] workerRangeArray = new Double[] {
//            0.8, 1.1, 1.4, 1.7, 2.0
//    };
//    public static final Double workerRangeDefault = 1.4;
    public static final int defaultProposalSize = Integer.MAX_VALUE;

//    public static final Double[] privacyBudgetArray = new Double[] {
//            1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0
//    };
//
//    public static final Double privacyBudgetDefault = 5.0;





}
