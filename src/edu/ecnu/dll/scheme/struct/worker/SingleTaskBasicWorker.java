package edu.ecnu.dll.scheme.struct.worker;


import edu.ecnu.dll.basic_struct.agent.Worker;
import edu.ecnu.dll.basic_struct.pack.single_agent_info.sub_class.DistanceBudgetPair;

import java.util.TreeSet;

public class SingleTaskBasicWorker extends Worker {
    public Double toTaskDistance = null;
    public Double[] privacyBudgetArray = null;
//    public Double[] noiseToTaskDistance

    public int budgetIndex = 0;

    public Double successfulUtilityFunctionValue = null;
    public Double currentUtilityFunctionValue = null;

    public TreeSet<DistanceBudgetPair> alreadyPublishedNoiseDistanceAndBudget = null;

//    public Double alreadyPublishedAverageNoiseDistance = null;
    public Double effectiveNoiseDistance = null;
//    public Double alreadyPublishedTotalPrivacyBudget = null;
    public Double effectivePrivacyBudget = null;
    public Double privacyBudgetCost = null;

//    public Double toCompetePublishEverageNoiseDistance = null;
//    public Double toCompetePublishTotalPrivacyBudget = null;

    public SingleTaskBasicWorker() {
    }

    public SingleTaskBasicWorker(double[] location) {
        super(location);
    }

    @Override
    public Double getToTaskDistance(Integer taskID) {
        return this.toTaskDistance;
    }

    @Override
    public Double getFinalUtility(Integer taskID) {
        return successfulUtilityFunctionValue;
    }

    @Override
    public Integer getTaskCompetingTimes(Integer taskID) {
        // todo: handle
        return null;
    }


}
