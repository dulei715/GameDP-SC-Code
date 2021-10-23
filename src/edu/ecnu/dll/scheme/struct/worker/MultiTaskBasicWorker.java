package edu.ecnu.dll.scheme.struct.worker;


import edu.ecnu.dll.basic_struct.agent.PrivacyWorker;
import edu.ecnu.dll.basic_struct.pack.DistanceBudgetPair;

import java.util.TreeSet;

public class MultiTaskBasicWorker extends PrivacyWorker {
    public Double[] toTaskDistance = null;
    //task number * budget number
    public Double[][] privacyBudgetArray = null;

    public int[] budgetIndex = null;

    public Double[] completeUtilityFunctionValue = null;
    public Double[] currentUtilityFunctionValue = null;

    public TreeSet<DistanceBudgetPair>[] alreadyPublishedNoiseDistanceAndBudgetTreeSetArray = null;


    public Double[] effectiveNoiseDistance = null;
    public Double[] effectivePrivacyBudget = null;
    public Double[] privacyBudgetCost = null;



    // 用于记录该worker对所有task的竞争次数 // todo: 竞争次数怎么定义有待考究，他会影响任务熵的定义
    public Integer[] taskCompletingTimes = null;

//    public Double toCompetePublishEverageNoiseDistance = null;
//    public Double toCompetePublishTotalPrivacyBudget = null;

    public MultiTaskBasicWorker() {
    }

    public MultiTaskBasicWorker(double[] location) {
        super(location);
    }

    @Override
    public Double getPrivacyBudget(Integer taskID) {
        return null;
    }
}
