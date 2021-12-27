package edu.ecnu.dll.basic.basic_struct.pack.experiment_result_info;

import tools.io.read.CSVRead;

import java.util.ArrayList;
import java.util.List;

public class Pack2ExtendedExperimentResult extends PackExtendedExperimentResult {
    public static final Integer characterSize = 18;
//    public String datasetName = null;
//    public Integer dataType = null;
//    public Boolean ppcfState = null;
//    public String solutionName = null;
    public Double lowerPrivacyValue = null;
    public Double upperPrivacyValue = null;

//    ExtendedExperimentResult extendedExperimentResult = null;

    public Pack2ExtendedExperimentResult(String datasetName, Integer dataType, Boolean ppcfState, String solutionName, ExtendedExperimentResult extendedExperimentResult, Double lowerPrivacyValue, Double upperPrivacyValue) {
        this.datasetName = datasetName;
        this.dataType = dataType;
        this.ppcfState = ppcfState;
        this.solutionName = solutionName;
        this.extendedExperimentResult = extendedExperimentResult;
        this.lowerPrivacyValue = lowerPrivacyValue;
        this.upperPrivacyValue = upperPrivacyValue;
    }

    public Pack2ExtendedExperimentResult(PackExtendedExperimentResult packExtendedExperimentResult, Double lowerPrivacyValue, Double upperPrivacyValue) {
        this.datasetName = packExtendedExperimentResult.datasetName;
        this.dataType = packExtendedExperimentResult.dataType;
        this.ppcfState = packExtendedExperimentResult.ppcfState;
        this.solutionName = packExtendedExperimentResult.solutionName;
        this.extendedExperimentResult = packExtendedExperimentResult.extendedExperimentResult;
        this.lowerPrivacyValue = lowerPrivacyValue;
        this.upperPrivacyValue = upperPrivacyValue;
    }

    public static Pack2ExtendedExperimentResult valueOf(String valuesStr, String splitTag) {
        String[] characterValueArray = valuesStr.split(splitTag);
        if (characterValueArray.length != Pack2ExtendedExperimentResult.characterSize) {
            throw new RuntimeException("The number of character size is not equal to " + PackExtendedExperimentResult.characterSize);
        }
        Pack2ExtendedExperimentResult result = new Pack2ExtendedExperimentResult();
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

        result.lowerPrivacyValue = Double.valueOf(characterValueArray[16]);
        result.upperPrivacyValue = Double.valueOf(characterValueArray[17]);

        result.extendedExperimentResult = new ExtendedExperimentResult(totalTaskSize, totalWorkerSize, totalUnServedSize, totalAllocatedWorkerSize, totalWinnerUtility, totalFailureUtility, realTravelDistance, competingTime, proposalSize, taskValue, workerRange);
        return result;
    }

    public static List<Pack2ExtendedExperimentResult> read2Result(String filePath) {
        List<String> strDataList = CSVRead.readDataLinesWithoutTitle(filePath);
        List<Pack2ExtendedExperimentResult> resultList = new ArrayList<>(strDataList.size());
        for (int i = 0; i < strDataList.size(); i++) {
            resultList.add(Pack2ExtendedExperimentResult.valueOf(strDataList.get(i), ","));
        }
        return resultList;
    }

    public Pack2ExtendedExperimentResult() {
    }

    @Override
    protected boolean equalSelfVariables(PackExtendedExperimentResult dataB) {
        if (!(dataB instanceof Pack2ExtendedExperimentResult) || !super.equalSelfVariables(dataB)) {
            return false;
        }
        Pack2ExtendedExperimentResult dataBB = (Pack2ExtendedExperimentResult) dataB;
        if (!this.lowerPrivacyValue.equals(dataBB.lowerPrivacyValue) || !this.upperPrivacyValue.equals(dataBB.upperPrivacyValue)) {
            return false;
        }
        return true;
    }



    @Override
    public String toString() {
        return  datasetName + "," + dataType + "," + ppcfState + "," + solutionName + "," + extendedExperimentResult.toString();
    }

    public static String getAddingTitle() {
        return "MinBudget,MaxBudget";
    }

}
