package edu.ecnu.dll.basic_struct.agent;

public abstract class PrivacyWorker extends Worker {

    public Double[] toTaskDistance = null;
    public Double[] toTaskNoiseDistance = null;

    public abstract Double getPrivacyBudget(Integer taskID);
}
