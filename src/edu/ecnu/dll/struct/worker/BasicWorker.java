package edu.ecnu.dll.struct.worker;


import java.util.ArrayList;

public class BasicWorker extends Worker {
    public Double toTaskDistance = null;
    public Double[] privacyBudgetArray = null;

    public int budgetIndex = 0;

    public Double currentUtilityFunctionValue = null;

    public Double alreadyPublishedEverageNoiseDistance = null;
    public Double alreadyPublishedTotalPrivacyBudget = null;

    public Double toCompetePublishEverageNoiseDistance = null;
    public Double toCompetePublishTotalPrivacyBudget = null;

    public BasicWorker() {
    }



}
