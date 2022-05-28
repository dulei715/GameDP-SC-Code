package edu.ecnu.dll.dataset.batch.concat_data;

import edu.ecnu.dll.basic.basic_struct.pack.experiment_result_info.Pack2ExtendedExperimentResult;
import edu.ecnu.dll.basic.basic_struct.pack.experiment_result_info.PackExtendedExperimentResult;
import edu.ecnu.dll.config.Constant;
import tools.io.read.CSVRead;
import tools.io.write.WriteExperimentResult;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AverageTotalExperimentData {
    public static final String inputBasicParentPath_basic =  "outputPath_total_run_";
    public static final String outputParentPath = "outputPath_average";

    public static final String[] resultDataFileName = {
            "output_worker_ratio_change.csv",
            "output_task_value_change.csv",
            "output_worker_range_change.csv",
            "output_worker_budget_change.csv",
    };

    public static void averageCSVFile(String basicPath, int times) {
        List<PackExtendedExperimentResult> workerRatioChangeResult, taskValueChangeResult, workerRangeChangeResult;
        List<String> workerRatioChangeResultStr, taskValueChangeResultStr, workerRangeChangeResultStr;
        List<PackExtendedExperimentResult> workerRatioChangeResultB, taskValueChangeResultB, workerRangeChangeResultB;
        List<Pack2ExtendedExperimentResult> workerBudgetChangeResult;
        List<String> workerBudgetChangeResultStr;
        List<Pack2ExtendedExperimentResult> workerBudgetChangeResultB;
        int i = 1;
        String inputBasicParentPath = inputBasicParentPath_basic + String.valueOf(i);

        String workerRatioChangeTitle = CSVRead.readDataTitle(basicPath + File.separator + inputBasicParentPath + File.separator + File.separator + resultDataFileName[0]);
        String taskValueChangeTitle = CSVRead.readDataTitle(basicPath + File.separator + inputBasicParentPath + File.separator +  File.separator + resultDataFileName[1]);
        String workerRangeChangeTitle = CSVRead.readDataTitle(basicPath + File.separator + inputBasicParentPath + File.separator + File.separator + resultDataFileName[2]);
        String workerBudgetChangeTitle = CSVRead.readDataTitle(basicPath + File.separator + inputBasicParentPath + File.separator + File.separator + resultDataFileName[3]);

        workerRatioChangeResult = PackExtendedExperimentResult.readResult(basicPath + File.separator + inputBasicParentPath + File.separator + File.separator + resultDataFileName[0]);
        taskValueChangeResult = PackExtendedExperimentResult.readResult(basicPath + File.separator + inputBasicParentPath + File.separator + File.separator + resultDataFileName[1]);
        workerRangeChangeResult = PackExtendedExperimentResult.readResult(basicPath + File.separator + inputBasicParentPath + File.separator + File.separator + resultDataFileName[2]);
        workerBudgetChangeResult = Pack2ExtendedExperimentResult.read2Result(basicPath + File.separator + inputBasicParentPath + File.separator + File.separator + resultDataFileName[3]);

        int listSize = workerRatioChangeResult.size();

        ++i;
        for (; i <= times; i++) {
            inputBasicParentPath = inputBasicParentPath_basic + String.valueOf(i);
            workerRatioChangeResultB = PackExtendedExperimentResult.readResult(basicPath + File.separator + inputBasicParentPath + File.separator + File.separator + resultDataFileName[0]);
            taskValueChangeResultB = PackExtendedExperimentResult.readResult(basicPath + File.separator + inputBasicParentPath + File.separator +  File.separator + resultDataFileName[1]);
            workerRangeChangeResultB = PackExtendedExperimentResult.readResult(basicPath + File.separator + inputBasicParentPath + File.separator +  File.separator + resultDataFileName[2]);
            workerBudgetChangeResultB = Pack2ExtendedExperimentResult.read2Result(basicPath + File.separator + inputBasicParentPath + File.separator +  File.separator + resultDataFileName[3]);

            for (int j = 0; j < listSize; j++) {
                workerRatioChangeResult.get(j).concat(workerRatioChangeResultB.get(j));
                taskValueChangeResult.get(j).concat(taskValueChangeResultB.get(j));
                workerRangeChangeResult.get(j).concat(workerRangeChangeResultB.get(j));
                workerBudgetChangeResult.get(j).concat(workerBudgetChangeResultB.get(j));
            }

        }

        for (int j = 0; j < listSize; j++) {
            workerRatioChangeResult.get(j).shrink(times);
            taskValueChangeResult.get(j).shrink(times);
            workerRangeChangeResult.get(j).shrink(times);
            workerBudgetChangeResult.get(j).shrink(times);
        }

        workerRatioChangeResultStr = new ArrayList<>();
        taskValueChangeResultStr = new ArrayList<>();
        workerRangeChangeResultStr = new ArrayList<>();
        workerBudgetChangeResultStr = new ArrayList<>();

        for (int j = 0; j < listSize; j++) {
            workerRatioChangeResultStr.add(workerRatioChangeResult.get(j).toString());
            taskValueChangeResultStr.add(taskValueChangeResult.get(j).toString());
            workerRangeChangeResultStr.add(workerRangeChangeResult.get(j).toString());
            workerBudgetChangeResultStr.add(workerBudgetChangeResult.get(j).toString());
        }


        WriteExperimentResult writeExperimentResult = new WriteExperimentResult();
        writeExperimentResult.writeResultList(basicPath + File.separator + outputParentPath + File.separator + resultDataFileName[0], workerRatioChangeTitle, workerRatioChangeResultStr);
        writeExperimentResult.writeResultList(basicPath + File.separator + outputParentPath + File.separator + resultDataFileName[1], taskValueChangeTitle, taskValueChangeResultStr);
        writeExperimentResult.writeResultList(basicPath + File.separator + outputParentPath + File.separator + resultDataFileName[2], workerRangeChangeTitle, workerRangeChangeResultStr);
        writeExperimentResult.writeResultList(basicPath + File.separator + outputParentPath + File.separator + resultDataFileName[3], workerBudgetChangeTitle, workerBudgetChangeResultStr);
    }

    public static void main(String[] args) {
//        String basicPath = args[0];
//        String basicPath = "E:\\gt-dp\\experiment\\1.chengdu";
//        String basicPath = "E:\\gt-dp\\experiment\\2.normal";
        String basicPath = "E:\\gt-dp\\experiment\\3.uniform";
//        Integer batchSize = Integer.valueOf(args[1]);
        Integer batchSize = 5;
        averageCSVFile(basicPath, batchSize);
    }
}
