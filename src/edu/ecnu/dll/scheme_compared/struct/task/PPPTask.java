package edu.ecnu.dll.scheme_compared.struct.task;

import edu.ecnu.dll.basic_struct.pack.DistanceIDPair;
import edu.ecnu.dll.basic_struct.agent.Task;

public class PPPTask extends Task {

    public Integer chosenWorkerID = null;
    public Integer candidateWorkerID = null;

    public PPPTask(double[] location) {
        super(location);
    }

    public PPPTask() {

    }
}
