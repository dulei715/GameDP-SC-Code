package edu.ecnu.dll.dataset.batch.concat_data.test;

import edu.ecnu.dll.basic.basic_struct.pack.experiment_result_info.Pack2ExtendedExperimentResult;
import edu.ecnu.dll.basic.basic_struct.pack.experiment_result_info.PackExtendedExperimentResult;
import edu.ecnu.dll.config.Constant;
import tools.io.read.CSVRead;
import tools.io.write.WriteExperimentResult;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ConcatExperimentDataOnlyForWorkerRatio {
    public static final String inputBasicParentPath =  "outputPath";
    public static final String outputParentPath = "outputPath_total";

    public static final String[] resultDataFileName = {
            "output_worker_ratio_change.csv",
    };

    public static void concatCSVFile(String basicPath, int batchSize) {
        List<PackExtendedExperimentResult> workerRatioChangeResult, taskValueChangeResult, workerRangeChangeResult;
        List<String> workerRatioChangeResultStr, taskValueChangeResultStr, workerRangeChangeResultStr;
        List<PackExtendedExperimentResult> workerRatioChangeResultB, taskValueChangeResultB, workerRangeChangeResultB;
        List<Pack2ExtendedExperimentResult> workerBudgetChangeResult;
        List<String> workerBudgetChangeResultStr;
        List<Pack2ExtendedExperimentResult> workerBudgetChangeResultB;

        String workerRatioChangeTitle = CSVRead.readDataTitle(basicPath + File.separator + inputBasicParentPath + File.separator + String.format("_%0"+Constant.subNamePositionSize+"d_", 1)+ File.separator + resultDataFileName[0]);

        workerRatioChangeResult = PackExtendedExperimentResult.readResult(basicPath + File.separator + inputBasicParentPath + File.separator + String.format("_%0"+Constant.subNamePositionSize+"d_", 1)+ File.separator + resultDataFileName[0]);

        int listSize = workerRatioChangeResult.size();

        for (int i = 2; i <= batchSize; i++) {
            workerRatioChangeResultB = PackExtendedExperimentResult.readResult(basicPath + File.separator + inputBasicParentPath + File.separator + String.format("_%0"+Constant.subNamePositionSize+"d_", i)+ File.separator + resultDataFileName[0]);

            for (int j = 0; j < listSize; j++) {
                workerRatioChangeResult.get(j).concat(workerRatioChangeResultB.get(j));
            }

        }

        workerRatioChangeResultStr = new ArrayList<>();

        for (int j = 0; j < listSize; j++) {
            workerRatioChangeResultStr.add(workerRatioChangeResult.get(j).toString());
        }


        WriteExperimentResult writeExperimentResult = new WriteExperimentResult();
        writeExperimentResult.writeResultList(basicPath + File.separator + outputParentPath + File.separator + resultDataFileName[0], workerRatioChangeTitle, workerRatioChangeResultStr);
    }

    public static void main(String[] args) {
        String basicPath = args[0];
//        String basicPath = "F:\\debug";
        Integer batchSize = Integer.valueOf(args[1]);
//        Integer batchSize = 260;
        concatCSVFile(basicPath, batchSize);
    }

}
