package edu.ecnu.dll.test;

import edu.ecnu.dll.basic.basic_struct.pack.experiment_result_info.ExtendedExperimentResult;
import edu.ecnu.dll.config.Constant;
import edu.ecnu.dll.run.run_compared.ConflictEliminationNonPrivacyCompleteRun;
import edu.ecnu.dll.run.run_main.AbstractRun;
import tools.basic.StringUtil;
import tools.io.read.PointRead;
import tools.struct.Point;

import java.io.File;
import java.util.List;

public class NoPrivacyTest {
    public static void main(String[] args) {
//        String basicPath = "E:\\gt-dp-cs\\3.normal";
        String basicPath = args[0];
        int batchSize = Integer.valueOf(args[1]);
        String[] parentPartPathArray = Constant.parentPathArray;
        String codeNumber;


        double taskValueDefault = Constant.taskValueDefault;
        double workerRangeDefault = Constant.workerRangeDefault;
        int proposalSize = Constant.defaultProposalSize;
        String dataType = String.valueOf(AbstractRun.COORDINATE);

        long[] competingTimeArray = new long[parentPartPathArray.length];
        for (int i = 0; i < competingTimeArray.length; i++) {
            competingTimeArray[i] = 0L;
        }


        for (int i = 0; i < batchSize; i++) {
            for (int j = 0; j < parentPartPathArray.length; j++) {
                codeNumber = StringUtil.getFixIndexNumberInteger((i+1), 3);
                String taskPointFileName = "batch_" + codeNumber + "_task_point.txt";
                String workerPointFileName = "batch_" + codeNumber + "_worker_point.txt";
                List<Point> taskPointList, workerPointList;
                String taskPointABName = basicPath + File.separator + parentPartPathArray[j] + File.separator + taskPointFileName;
                String workerPointABName = basicPath + File.separator + parentPartPathArray[j] + File.separator + workerPointFileName;
                taskPointList = PointRead.readPointWithFirstLineCount(taskPointABName);
                workerPointList = PointRead.readPointWithFirstLineCount(workerPointABName);
                ExtendedExperimentResult uConflictNonPrivacy = ConflictEliminationNonPrivacyCompleteRun.runningOnSingleDatasetWithUtilityConflictElimination(taskPointList, workerPointList, taskValueDefault, workerRangeDefault, proposalSize, dataType);
//                System.out.println(uConflictNonPrivacy.competingTime);
                competingTimeArray[j] += uConflictNonPrivacy.competingTime;
            }
        }

        for (int i = 0; i < parentPartPathArray.length; i++) {
            System.out.println(competingTimeArray[i]);
        }
//        String solutionName = "UtilityConflictNonPrivacySolution";
//        PackExtendedExperimentResult uConflictNonPrivacyResult = new PackExtendedExperimentResult(datasetName, Integer.valueOf(dataType), false, solutionName, uConflictNonPrivacy);
//        resultList.add(uConflictNonPrivacyResult.toString());

    }
}
