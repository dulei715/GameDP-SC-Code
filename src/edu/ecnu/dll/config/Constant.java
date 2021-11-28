package edu.ecnu.dll.config;

import java.io.File;

public class Constant {

    public static final String FILE_PATH_SPLIT = File.separator;



    public static final double ALPHA = 0.1;
//    public static final double BETA = 0.2;
    public static final double BETA = 1;

    // for worker ratio
    public static final String[] parentPathArray = new String[] {
            FILE_PATH_SPLIT + "task_worker_1_1_0",
            FILE_PATH_SPLIT + "task_worker_1_1_5",
            FILE_PATH_SPLIT + "task_worker_1_2_0",
            FILE_PATH_SPLIT + "task_worker_1_2_5",
            FILE_PATH_SPLIT + "task_worker_1_3_0"
//            FILE_PATH_SPLIT + "task_worker_1_3_5"
    };
    public static final String parentPathDefault = FILE_PATH_SPLIT + "task_worker_1_2_0";

    // for task value
    public static final Double[] taskValueArray = new Double[] {
            5.0, 10.0, 15.0, 20.0, 25.0
    };
    public static final Double taskValueDefault = 15.0;

    // for worker range
    public static final Double[] workerRangeArray = new Double[] {
            10.0, 15.0, 20.0, 25.0, 30.0
    };
    //    public static final Double workerRangeDefault = 30.0;
    public static final Double workerRangeDefault = 20.0;
    //    public static final Double workerRangeDefault = 5.0;
    public static final int defaultProposalSize = Integer.MAX_VALUE;





}
