package edu.ecnu.dll.basic.basic_struct.pack.experiment_result_info;

public class PackExtendedExperimentResult {
    public String datasetName = null;
    public Integer dataType = null;
    public Boolean ppcfState = null;
    public String solutionName = null;
//    public Integer workerChosenState = null;

//    public Boolean eceaState = null;

    ExtendedExperimentResult extendedExperimentResult = null;

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

    @Override
    public String toString() {
//        return  datasetName + "," + dataType + "," + ppcfState + "," + eceaState + "," + workerChosenState + "," + normalExperimentResult.toString();
        return  datasetName + "," + dataType + "," + ppcfState + "," + solutionName + "," + extendedExperimentResult.toString();
    }

    public static String getSelfTitle() {
        return "DatasetName" + ",DataType" + ",PPCFState" + ",SolutionName";
    }

}
