package edu.ecnu.dll.scheme_compared.struct.worker;

import edu.ecnu.dll.basic_struct.agent.Worker;

public class PPPWorker extends Worker {
    public Double privacyBudget = null;

    public Double[] toTaskDistance = null;
    public Double[] toTaskNoiseDistance = null;

    public PPPWorker(double[] location, Double privacyBudget) {
        this.location = location;
        this.privacyBudget = privacyBudget;
    }

    public PPPWorker(double[] location) {
        this.location = location;
    }

    public PPPWorker() {
    }
}
