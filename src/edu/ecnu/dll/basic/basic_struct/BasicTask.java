package edu.ecnu.dll.basic.basic_struct;

import edu.ecnu.dll.basic.basic_struct.agent.Task;

public class BasicTask extends Task {
    public BasicTask() {
    }

    public BasicTask(double[] location) {
        super(location, 0);
    }

    public BasicTask(double[] location, double valuation) {
        super(location, valuation);
    }
}
