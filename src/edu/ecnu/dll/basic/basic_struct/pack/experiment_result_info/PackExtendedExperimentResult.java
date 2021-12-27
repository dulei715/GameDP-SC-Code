package edu.ecnu.dll.basic.basic_struct.pack.experiment_result_info;

import tools.io.read.CSVRead;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PackExtendedExperimentResult {
    public static final Integer characterSize = 16;
    public String datasetName = null;
    public Integer dataType = null;
    public Boolean ppcfState = null;
    public String solutionName = null;
//    public Integer workerChosenState = null;

//    public Boolean eceaState = null;

    public ExtendedExperimentResult extendedExperimentResult = null;

//    public PackNormalExperimentResult(String datasetName, Integer dataType, Boolean ppcfState, Boolean eceaState, Integer workerChosenState, NormalExperimentResult normalExperimentResult) {
    public PackExtendedExperimentResult(String datasetName, Integer dataType, Boolean ppcfState, String solutionName, ExtendedExperimentResult extendedExperimentResult) {
        this.datasetName = datasetName;
        this.dataType = dataType;
        this.ppcfState = ppcfState;
//        this.eceaState = eceaState;
//        this.workerChosenState = workerChosenState;
        this.solutionName = solutionName;
        this.extendedExperimentResult = extendedExperimentResult;
    }

    public PackExtendedExperimentResult() {
    }

    public static PackExtendedExperimentResult valueOf(String valuesStr, String splitTag) {
        String[] characterValueArray = valuesStr.split(splitTag);
        if (characterValueArray.length != PackExtendedExperimentResult.characterSize) {
            throw new RuntimeException("The number of character size is not equal to " + PackExtendedExperimentResult.characterSize);
        }
        PackExtendedExperimentResult result = new PackExtendedExperimentResult();
        result.datasetName = characterValueArray[0];
        result.dataType = Integer.valueOf(characterValueArray[1]);
        result.ppcfState = Boolean.valueOf(characterValueArray[2]);
        result.solutionName = characterValueArray[3];

        Long competingTime = Long.valueOf(characterValueArray[4]);
        Integer totalTaskSize = Integer.valueOf(characterValueArray[5]);
        Integer totalWorkerSize = Integer.valueOf(characterValueArray[6]);
        Integer totalUnServedSize = Integer.valueOf(characterValueArray[7]);
        Integer totalAllocatedWorkerSize = Integer.valueOf(characterValueArray[8]);
//        Double totalUtility = Double.valueOf(characterValueArray[9]);
        Double totalWinnerUtility = Double.valueOf(characterValueArray[10]);
        Double totalFailureUtility = Double.valueOf(characterValueArray[11]);
        Double realTravelDistance = Double.valueOf(characterValueArray[12]);
        Integer proposalSize = Integer.valueOf(characterValueArray[13]);
        Double taskValue = Double.valueOf(characterValueArray[14]);
        Double workerRange = Double.valueOf(characterValueArray[15]);

        result.extendedExperimentResult = new ExtendedExperimentResult(totalTaskSize, totalWorkerSize, totalUnServedSize, totalAllocatedWorkerSize, totalWinnerUtility, totalFailureUtility, realTravelDistance, competingTime, proposalSize, taskValue, workerRange);
        return result;
    }

    protected boolean equalSelfVariables(PackExtendedExperimentResult dataB) {
        if (!this.datasetName.equals(dataB.datasetName) || !this.dataType.equals(dataB.dataType) || !this.ppcfState.equals(dataB.ppcfState) || !this.solutionName.equals(dataB.solutionName) || !this.extendedExperimentResult.proposalSize.equals(dataB.extendedExperimentResult.proposalSize)
                || !this.extendedExperimentResult.taskValue.equals(dataB.extendedExperimentResult.taskValue) || !this.extendedExperimentResult.workerRange.equals(dataB.extendedExperimentResult.workerRange)) {
            return false;
        }
        return true;
    }

    public static List<PackExtendedExperimentResult> readResult(String filePath) {
        List<String> strDataList = CSVRead.readDataLinesWithoutTitle(filePath);
        List<PackExtendedExperimentResult> resultList = new ArrayList<>(strDataList.size());
        for (int i = 0; i < strDataList.size(); i++) {
            resultList.add(PackExtendedExperimentResult.valueOf(strDataList.get(i), ","));
        }
        return resultList;
    }




    // 注意这里改变第一个dataA
    public static void concat(PackExtendedExperimentResult dataA, PackExtendedExperimentResult dataB) {
        // 检查非叠加项是否一致
        if (!dataA.equalSelfVariables(dataB)) {
            throw new RuntimeException("The self variables are not consistent!");
        }
        // 将叠加项相加
        dataA.extendedExperimentResult.competingTime += dataB.extendedExperimentResult.competingTime;
        dataA.extendedExperimentResult.totalTaskSize += dataB.extendedExperimentResult.totalTaskSize;
        dataA.extendedExperimentResult.totalWorkerSize += dataB.extendedExperimentResult.totalWorkerSize;
        dataA.extendedExperimentResult.totalUnServedTaskSize += dataB.extendedExperimentResult.totalUnServedTaskSize;
        dataA.extendedExperimentResult.totalAllocatedWorkerSize += dataB.extendedExperimentResult.totalAllocatedWorkerSize;
        dataA.extendedExperimentResult.totalUtility += dataB.extendedExperimentResult.totalUtility;
        dataA.extendedExperimentResult.totalWinnerUtility += dataB.extendedExperimentResult.totalWinnerUtility;
        dataA.extendedExperimentResult.totalFailureUtility += dataB.extendedExperimentResult.totalFailureUtility;
        dataA.extendedExperimentResult.realTravelDistance += dataB.extendedExperimentResult.realTravelDistance;
    }

    public void concat(PackExtendedExperimentResult dataB) {
        if (!this.equalSelfVariables(dataB)) {
            throw new RuntimeException("The self variables are not consistent!");
        }
        // 将叠加项相加
        this.extendedExperimentResult.competingTime += dataB.extendedExperimentResult.competingTime;
        this.extendedExperimentResult.totalTaskSize += dataB.extendedExperimentResult.totalTaskSize;
        this.extendedExperimentResult.totalWorkerSize += dataB.extendedExperimentResult.totalWorkerSize;
        this.extendedExperimentResult.totalUnServedTaskSize += dataB.extendedExperimentResult.totalUnServedTaskSize;
        this.extendedExperimentResult.totalAllocatedWorkerSize += dataB.extendedExperimentResult.totalAllocatedWorkerSize;
        this.extendedExperimentResult.totalUtility += dataB.extendedExperimentResult.totalUtility;
        this.extendedExperimentResult.totalWinnerUtility += dataB.extendedExperimentResult.totalWinnerUtility;
        this.extendedExperimentResult.totalFailureUtility += dataB.extendedExperimentResult.totalFailureUtility;
        this.extendedExperimentResult.realTravelDistance += dataB.extendedExperimentResult.realTravelDistance;
    }

    @Override
    public String toString() {
//        return  datasetName + "," + dataType + "," + ppcfState + "," + eceaState + "," + workerChosenState + "," + normalExperimentResult.toString();
        return  datasetName + "," + dataType + "," + ppcfState + "," + solutionName + "," + extendedExperimentResult.toString();
    }

    public static String getSelfTitle() {
        return "DatasetName" + ",DataType" + ",PPCFState" + ",SolutionName";
    }

}
