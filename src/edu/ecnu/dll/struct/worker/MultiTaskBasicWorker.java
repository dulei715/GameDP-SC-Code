package edu.ecnu.dll.struct.worker;


public class MultiTaskBasicWorker extends Worker {
    public Double[] toTaskDistance = null;
    //task number * budget number
    public Double[][] privacyBudgetArray = null;

    public int[] budgetIndex = null;

    public Double currentUtilityFunctionValue = null;

    public Double[] alreadyPublishedEverageNoiseDistance = null;
    public Double[] alreadyPublishedTotalPrivacyBudget = null;

    // 用于记录该worker对所有task的竞争次数 // todo: 竞争次数怎么定义有待考究，他会影响任务熵的定义
    public Integer[] taskCompletingTimes = null;

//    public Double toCompetePublishEverageNoiseDistance = null;
//    public Double toCompetePublishTotalPrivacyBudget = null;

    public MultiTaskBasicWorker() {
    }



}
