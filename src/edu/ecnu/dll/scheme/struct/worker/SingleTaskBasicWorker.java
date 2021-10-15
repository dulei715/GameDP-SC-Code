package edu.ecnu.dll.scheme.struct.worker;


import edu.ecnu.dll.basic_struct.agent.Worker;

public class SingleTaskBasicWorker extends Worker {
    public Double toTaskDistance = null;
    public Double[] privacyBudgetArray = null;

    public int budgetIndex = 0;

    public Double currentUtilityFunctionValue = null;

    public Double alreadyPublishedEverageNoiseDistance = null;
    public Double alreadyPublishedTotalPrivacyBudget = null;

//    public Double toCompetePublishEverageNoiseDistance = null;
//    public Double toCompetePublishTotalPrivacyBudget = null;

    public SingleTaskBasicWorker() {
    }



}
