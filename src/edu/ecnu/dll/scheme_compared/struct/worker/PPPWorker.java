package edu.ecnu.dll.scheme_compared.struct.worker;

import edu.ecnu.dll.basic_struct.agent.PrivacyWorker;
import edu.ecnu.dll.basic_struct.agent.Worker;

public class PPPWorker extends PrivacyWorker {
    public Double privacyBudget = null;

    public PPPWorker(double[] location, Double privacyBudget) {
        this.location = location;
        this.privacyBudget = privacyBudget;
    }

    public PPPWorker(double[] location) {
        this.location = location;
    }

    public PPPWorker() {
    }

    @Override
    public Double getPrivacyBudget(Integer taskID) {
        return this.privacyBudget;
    }
}
