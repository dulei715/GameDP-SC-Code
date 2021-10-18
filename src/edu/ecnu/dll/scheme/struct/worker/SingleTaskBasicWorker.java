package edu.ecnu.dll.scheme.struct.worker;


import edu.ecnu.dll.basic_struct.agent.Worker;
import edu.ecnu.dll.basic_struct.pack.DistanceBudgetPair;

import java.util.List;
import java.util.TreeSet;

public class SingleTaskBasicWorker extends Worker {
    public Double toTaskDistance = null;
    public Double[] privacyBudgetArray = null;

    public int budgetIndex = 0;

    public Double currentUtilityFunctionValue = null;

    public TreeSet<DistanceBudgetPair> alreadyPublishedNoiseDistanceAndBudget = null;

//    public Double alreadyPublishedAverageNoiseDistance = null;
    public Double effectiveNoiseDistance = null;
//    public Double alreadyPublishedTotalPrivacyBudget = null;
    public Double effectivePrivacyBudget = null;

//    public Double toCompetePublishEverageNoiseDistance = null;
//    public Double toCompetePublishTotalPrivacyBudget = null;

    public SingleTaskBasicWorker() {
    }



}
