package edu.ecnu.dll.scheme.scheme_compared.struct.agent.task;

import edu.ecnu.dll.basic.basic_struct.agent.Task;

public class PPPTask extends Task {

    public Integer chosenWorkerID = null;
    public Integer candidateWorkerID = null;

    public PPPTask(double[] location) {
        super(location, 0);
    }

    public PPPTask(double[] location, double valuation) {
        super(location, valuation);
    }

    public PPPTask() {

    }
}
