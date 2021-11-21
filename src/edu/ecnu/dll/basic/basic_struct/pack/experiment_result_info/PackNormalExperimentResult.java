package edu.ecnu.dll.basic.basic_struct.pack.experiment_result_info;

public class PackNormalExperimentResult {
    public String datasetName = null;
    public Integer dataType = null;
    public Boolean ppcfState = null;
//    public Integer workerChosenState = null;

//    public Boolean eceaState = null;

    NormalExperimentResult normalExperimentResult = null;

//    public PackNormalExperimentResult(String datasetName, Integer dataType, Boolean ppcfState, Boolean eceaState, Integer workerChosenState, NormalExperimentResult normalExperimentResult) {
    public PackNormalExperimentResult(String datasetName, Integer dataType, Boolean ppcfState, NormalExperimentResult normalExperimentResult) {
        this.datasetName = datasetName;
        this.dataType = dataType;
        this.ppcfState = ppcfState;
//        this.eceaState = eceaState;
//        this.workerChosenState = workerChosenState;
        this.normalExperimentResult = normalExperimentResult;
    }

    public PackNormalExperimentResult() {
    }

    @Override
    public String toString() {
//        return  datasetName + "," + dataType + "," + ppcfState + "," + eceaState + "," + workerChosenState + "," + normalExperimentResult.toString();
        return  datasetName + "," + dataType + "," + ppcfState + "," + normalExperimentResult.toString();
    }

    public static String getSelfTitle() {
        return "DatasetName" + ",DataType" + ",PPCFState";
    }

}
