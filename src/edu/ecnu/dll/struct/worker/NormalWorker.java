package edu.ecnu.dll.struct.worker;


public class NormalWorker extends Worker {
    public Double[] toTaskDistance = null;
    //task number * budget number
    public Double[][] privacyBudgetArray = null;

    public int[] budgetIndex = null;

    public Double currentUtilityFunctionValue = null;

    public Double[] alreadyPublishedEverageNoiseDistance = null;
    public Double[] alreadyPublishedTotalPrivacyBudget = null;

//    public Double toCompetePublishEverageNoiseDistance = null;
//    public Double toCompetePublishTotalPrivacyBudget = null;

    public NormalWorker() {
    }



}
