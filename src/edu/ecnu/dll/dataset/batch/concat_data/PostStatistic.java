package edu.ecnu.dll.dataset.batch.concat_data;

import edu.ecnu.dll.basic.basic_struct.pack.experiment_result_info.PackExtendedExperimentResult;
import tools.basic.BasicArray;
import tools.basic.BasicCalculation;
import tools.io.print.MyPrint;

import java.io.File;
import java.util.*;

public class PostStatistic {

    public static String[] schemeNameArray = new String[] {
            "ucs_p_f",
            "ucs_p_t",
            "dcs_p_f",
            "dcs_p_t",
            "gts_p",
            "ucs_np",
            "dcs_np",
            "gts_np",
            "grs_np"
    };

    public static String basicBasicResultParentPath = "E:\\gt-dp\\experiment_add_statistic";


    public static Map<String, String> basicResultParentPathMap = null;

    static {
        basicResultParentPathMap = new HashMap<>();
        basicResultParentPathMap.put("chengdu", "1.chengdu" + File.separator + "outputPath_average");
        basicResultParentPathMap.put("normal", "2.normal" + File.separator + "outputPath_average");
        basicResultParentPathMap.put("uniform", "3.uniform" + File.separator + "outputPath_average");
    }

    public static final String[] resultDataFileName = {
            "output_worker_ratio_change.csv",
            "output_task_value_change.csv",
            "output_worker_range_change.csv",
            "output_worker_budget_change.csv",
    };

    public static Map<String, List<PackExtendedExperimentResult>> splitBySchemeType(List<PackExtendedExperimentResult> totalList) {
        int interval = schemeNameArray.length;
        if (totalList.size() % interval != 0) {
            throw new RuntimeException("The size is not complete!");
        }
        List[] listArray = new List[interval];
        for (int i = 0; i < listArray.length; i++) {
            listArray[i] = new ArrayList();
        }
        Iterator<PackExtendedExperimentResult> packResultIterator = totalList.listIterator();
        while (packResultIterator.hasNext()) {
            for (int i = 0; i < listArray.length; i++) {
                listArray[i].add(packResultIterator.next());
            }
        }
        Map<String, List<PackExtendedExperimentResult>> result = new HashMap<>(interval);
        for (int i = 0; i < interval; i++) {
            result.put(schemeNameArray[i], listArray[i]);
        }
        return result;
    }

    public static double[] calculateTimeRatio(String datasetName) {

//        workerRatioChangeResult = PackExtendedExperimentResult.readResult(basicResultPath + File.separator + inputBasicParentPath + File.separator + File.separator + resultDataFileName[0]);
        List<PackExtendedExperimentResult> data = PackExtendedExperimentResult.readResult(basicBasicResultParentPath + File.separator + basicResultParentPathMap.get(datasetName) + File.separator + resultDataFileName[0]);
        Map<String, List<PackExtendedExperimentResult>> dataMap = splitBySchemeType(data);
//        MyPrint.showMapWithListValue(dataMap);
        Map<String, double[]>  ratioMap = new HashMap<>();
        int innerElementSize = dataMap.entrySet().iterator().next().getValue().size();

        // gts_p和dcs_p_t比例
        double[] tempDoubleArray = new double[innerElementSize];
        for (int i = 0; i < tempDoubleArray.length; i++) {
            tempDoubleArray[i] = dataMap.get("gts_p").get(i).extendedExperimentResult.competingTime * 1.0 / dataMap.get("dcs_p_t").get(i).extendedExperimentResult.competingTime;
        }
        // 获取各种非隐私方案的时间
        MyPrint.showDoubleArray(tempDoubleArray);

        // ucs_p和dcs_p_t比例
        tempDoubleArray = new double[innerElementSize];
        for (int i = 0; i < tempDoubleArray.length; i++) {
            tempDoubleArray[i] = dataMap.get("ucs_p_t").get(i).extendedExperimentResult.competingTime * 1.0 / dataMap.get("dcs_p_t").get(i).extendedExperimentResult.competingTime;
        }
        // 获取各种非隐私方案的时间
//        MyPrint.showDoubleArray(tempDoubleArray);
        return tempDoubleArray;

    }

    public static double[][] calculateUtilityRatio(String datasetName) {
        // for worker range change
        List<PackExtendedExperimentResult> data = PackExtendedExperimentResult.readResult(basicBasicResultParentPath + File.separator + basicResultParentPathMap.get(datasetName) + File.separator + resultDataFileName[2]);
        Map<String, List<PackExtendedExperimentResult>> dataMap = splitBySchemeType(data);
//        MyPrint.showMapWithListValue(dataMap);
        Map<String, double[]>  ratioMap = new HashMap<>();
        int innerElementSize = dataMap.entrySet().iterator().next().getValue().size();

        double gts_p_value, dcs_p_t_value, ucs_p_value;

        // gts_p和dcs_p_t比例
        double[][] tempDoubleArray = new double[2][innerElementSize];
        for (int i = 0; i < tempDoubleArray[0].length; i++) {
            gts_p_value = dataMap.get("gts_p").get(i).extendedExperimentResult.totalUtility / dataMap.get("gts_p").get(i).extendedExperimentResult.totalAllocatedWorkerSize;
            dcs_p_t_value = dataMap.get("dcs_p_t").get(i).extendedExperimentResult.totalUtility / dataMap.get("dcs_p_t").get(i).extendedExperimentResult.totalAllocatedWorkerSize;
            tempDoubleArray[0][i] = gts_p_value / dcs_p_t_value;
        }
        // 获取各种非隐私方案的时间
//        MyPrint.showDoubleArray(tempDoubleArray);

        // ucs_p和dcs_p_t比例
//        tempDoubleArray = new double[innerElementSize];
        for (int i = 0; i < tempDoubleArray[1].length; i++) {
            ucs_p_value = dataMap.get("ucs_p_t").get(i).extendedExperimentResult.totalUtility / dataMap.get("ucs_p_t").get(i).extendedExperimentResult.totalAllocatedWorkerSize;
            dcs_p_t_value = dataMap.get("dcs_p_t").get(i).extendedExperimentResult.totalUtility / dataMap.get("dcs_p_t").get(i).extendedExperimentResult.totalAllocatedWorkerSize;
            tempDoubleArray[1][i] = ucs_p_value / dcs_p_t_value;
        }
        // 获取各种非隐私方案的时间
//        MyPrint.showDoubleArray(tempDoubleArray);
        return tempDoubleArray;
    }

    public static void main(String[] args) {
//        String datasetName = "chengdu";
        String datasetName = "normal";
//        calculateTimeRatio(datasetName);
        double[][] result = calculateUtilityRatio(datasetName);
        MyPrint.show2DimensionDoubleArray(result);

        MyPrint.showSplitLine("*",150);

        System.out.println(BasicCalculation.getSum(result[0]) / result[0].length);
    }
}
