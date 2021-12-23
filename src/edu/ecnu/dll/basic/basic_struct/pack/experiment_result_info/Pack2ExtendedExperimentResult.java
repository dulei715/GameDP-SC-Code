package edu.ecnu.dll.basic.basic_struct.pack.experiment_result_info;

public class Pack2ExtendedExperimentResult extends PackExtendedExperimentResult {
//    public String datasetName = null;
//    public Integer dataType = null;
//    public Boolean ppcfState = null;
//    public String solutionName = null;
    public Double lowerPrivacyValue = null;
    public Double upperPrivacyValue = null;

    ExtendedExperimentResult extendedExperimentResult = null;

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

    public Pack2ExtendedExperimentResult() {
    }

    @Override
    public String toString() {
        return  datasetName + "," + dataType + "," + ppcfState + "," + solutionName + "," + extendedExperimentResult.toString();
    }

    public static String getAddingTitle() {
        return "MinBudget,MaxBudget";
    }

}
