package edu.ecnu.dll.scheme.struct.worker;


import edu.ecnu.dll.basic_struct.agent.PrivacyWorker;
import edu.ecnu.dll.basic_struct.pack.single_agent_info.sub_class.DistanceBudgetPair;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class MultiTaskBasicWorker extends PrivacyWorker {


//    public List<Double> toTaskDistance = null;
    //task number * budget number
    public List<Double[]> privacyBudgetArrayList = null;
    public List<Double[]> noiseDistanceArrayList = null;

    public List<Integer> budgetIndex = null;

    // 记录成功进入候选的utility函数值
    public List<Double> successfullyUtilityFunctionValue = null;
    // 记录每次计算的utility函数值
    public List<Double> currentUtilityFunctionValue = null;

    public List<TreeSet<DistanceBudgetPair>> alreadyPublishedNoiseDistanceAndBudgetTreeSetArray = null;


    public List<Double> effectiveNoiseDistance = null;
    public List<Double> effectivePrivacyBudget = null;
    public List<Double> totalPrivacyBudgetCost = null;



    // 用于记录该worker对所有task的竞争次数 // todo: 竞争次数怎么定义有待考究，他会影响任务熵的定义
    public List<Integer> taskCompetingTimes = null;





//    public Double toCompetePublishAverageNoiseDistance = null;
//    public Double toCompetePublishTotalPrivacyBudget = null;

    public MultiTaskBasicWorker() {
    }

    public MultiTaskBasicWorker(double[] location) {
        super(location);
    }

    @Override
    public Double getPrivacyBudget(Integer taskID) {
        return getEffectivePrivacyBudget(taskID);
    }

    public Double getToTaskDistance(Integer taskID) {
        int index = taskIndex[taskID];
        if (index == -1) {
            return null;
        }
        return toTaskDistance.get(index);
    }

    @Override
    public Double getFinalUtility(Integer taskID) {
        return getSuccessfullyUtilityFunctionValue(taskID);
    }

    public int setToTaskDistance(Integer taskID, Double distance) {
        int index = taskIndex[taskID];
        if (index == -1) {
            return -1;
        }
        this.toTaskDistance.set(index, distance);
        return 0;
    }

    @Override
    public Double getToTaskEffectiveNoiseDistance(Integer taskID) {
        return getEffectiveNoiseDistance(taskID);
    }


    public Double[] getPrivacyBudgetArray(Integer taskID) {
        int index = taskIndex[taskID];
        if (index == -1) {
            return null;
        }
        return privacyBudgetArrayList.get(index);
    }

    public int setPrivacyBudgetArray(Integer taskID, Double[] privacyBudgetArray) {
        int index = taskIndex[taskID];
        if (index == -1) {
            return -1;
        }
        this.privacyBudgetArrayList.set(index, privacyBudgetArray);
        return 0;
    }

    public Double[] getNoiseDistanceArray(Integer taskID) {
        int index = taskIndex[taskID];
        if (index == -1) {
            return null;
        }
        return noiseDistanceArrayList.get(index);
    }



    public int setNoiseDistanceArray(Integer taskID, Double[] noiseDistanceArray) {
        int index = taskIndex[taskID];
        if (index == -1) {
            return -1;
        }
        this.noiseDistanceArrayList.set(index, noiseDistanceArray);
        return 0;
    }

    public int setPrivacyBudgetArray(Double[][] totalPrivacyBudgetArray) {
        int tempResult = -1;
        if (this.privacyBudgetArrayList == null) {
            this.privacyBudgetArrayList = new ArrayList<>();
        }

        for (int i = 0; i < totalPrivacyBudgetArray.length; i++) {
            tempResult *= setPrivacyBudgetArray(i, totalPrivacyBudgetArray[i]);
        }
        if (tempResult != 0) {
            return -1;
        }
        return 0;
    }


    public Integer getBudgetIndex(Integer taskID) {
        int index = taskIndex[taskID];
        if (index == -1) {
            return null;
        }
        return budgetIndex.get(index);
    }

    public int setBudgetIndex(Integer taskID, Integer budgetIndex) {
        int index = taskIndex[taskID];
        if (index == -1) {
            return -1;
        }
        this.budgetIndex.set(index, budgetIndex);
        return 0;
    }

    public int increaseBudgetIndex(Integer taskID) {
        int index = taskIndex[taskID];
        if (index == -1) {
            return -1;
        }
        Integer originalBudgetIndex = this.budgetIndex.get(index);
        this.budgetIndex.set(index, originalBudgetIndex + 1);
        return 0;
    }

    public Double getSuccessfullyUtilityFunctionValue(Integer taskID) {
        int index = taskIndex[taskID];
        if (index == -1) {
            return null;
        }
        return successfullyUtilityFunctionValue.get(index);
    }

    public int setSuccessfullyUtilityFunctionValue(Integer taskID, Double successfullyUtilityFunctionValue) {
        int index = taskIndex[taskID];
        if (index == -1) {
            return -1;
        }
        this.successfullyUtilityFunctionValue.set(index, successfullyUtilityFunctionValue);
        return 0;
    }

    public int increaseSuccessfullyUtilityFunctionValue(Integer taskID, Double additionValue) {
        int index = taskIndex[taskID];
        if (index == -1) {
            return -1;
        }
        Double originalValue = this.successfullyUtilityFunctionValue.get(index);
        this.successfullyUtilityFunctionValue.set(index, originalValue + additionValue);
        return 0;
    }


    public Double getCurrentUtilityFunctionValue(Integer taskID) {
        int index = taskIndex[taskID];
        if (index == -1) {
            return null;
        }
        return currentUtilityFunctionValue.get(index);
    }

    public int setCurrentUtilityFunctionValue(Integer taskID, Double currentUtilityFunctionValue) {
        int index = taskIndex[taskID];
        if (index == -1) {
            return -1;
        }
        this.currentUtilityFunctionValue.set(index, currentUtilityFunctionValue);
        return 0;
    }

    public TreeSet<DistanceBudgetPair> getAlreadyPublishedNoiseDistanceAndBudgetTreeSet(Integer taskID) {
        int index = taskIndex[taskID];
        if (index == -1) {
            return null;
        }
        return alreadyPublishedNoiseDistanceAndBudgetTreeSetArray.get(index);
    }

    public int setAlreadyPublishedNoiseDistanceAndBudgetTreeSet(Integer taskID, TreeSet<DistanceBudgetPair> alreadyPublishedNoiseDistanceAndBudgetTreeSet) {
        int index = taskIndex[taskID];
        if (index == -1) {
            return -1;
        }
        this.alreadyPublishedNoiseDistanceAndBudgetTreeSetArray.set(index, alreadyPublishedNoiseDistanceAndBudgetTreeSet);
        return 0;
    }

    public int addElementToAlreadyPublishedNoiseDistanceAndBudgetTreeSet(Integer taskID, DistanceBudgetPair element) {
        int index = taskIndex[taskID];
        if (index == -1) {
            return -1;
        }
        this.alreadyPublishedNoiseDistanceAndBudgetTreeSetArray.get(index).add(element);
        return 0;
    }

    public Double getEffectiveNoiseDistance(Integer taskID) {
        int index = taskIndex[taskID];
        if (index == -1) {
            return null;
        }
        return effectiveNoiseDistance.get(index);
    }

    public int setEffectiveNoiseDistance(Integer taskID, Double effectiveNoiseDistance) {
        int index = taskIndex[taskID];
        if (index == -1) {
            return -1;
        }
        this.effectiveNoiseDistance.set(index, effectiveNoiseDistance);
        return 0;
    }

    public Double getEffectivePrivacyBudget(Integer taskID) {
        int index = taskIndex[taskID];
        if (index == -1) {
            return null;
        }
        return effectivePrivacyBudget.get(index);
    }

    public int setEffectivePrivacyBudget(Integer taskID, Double effectivePrivacyBudget) {
        int index = taskIndex[taskID];
        if (index == -1) {
            return -1;
        }
        this.effectivePrivacyBudget.set(index, effectivePrivacyBudget);
        return 0;
    }

    @Override
    public Double getTotalPrivacyBudgetCost(Integer taskID) {
        int index = taskIndex[taskID];
        if (index == -1) {
            return null;
        }
        return totalPrivacyBudgetCost.get(index);
    }

    public int setTotalPrivacyBudgetCost(Integer taskID, Double privacyBudgetCost) {
        int index = taskIndex[taskID];
        if (index == -1) {
            return -1;
        }
        this.totalPrivacyBudgetCost.set(index, privacyBudgetCost);
        return 0;
    }

    public Integer getTaskCompetingTimes(Integer taskID) {
        int index = taskIndex[taskID];
        if (index == -1) {
            return null;
        }
        return taskCompetingTimes.get(index);
    }

    public int setTaskCompetingTimes(Integer taskID, Integer taskCompetingTimes) {
        int index = taskIndex[taskID];
        if (index == -1) {
            return -1;
        }
        this.taskCompetingTimes.set(index, taskCompetingTimes);
        return 0;
    }

    public int increaseTaskCompetingTimes(Integer taskID) {
        int index = taskIndex[taskID];
        if (index == -1) {
            return -1;
        }
        int increaseTime = this.taskCompetingTimes.get(index);
        this.taskCompetingTimes.set(index, increaseTime + 1);
        return 0;
    }


}
