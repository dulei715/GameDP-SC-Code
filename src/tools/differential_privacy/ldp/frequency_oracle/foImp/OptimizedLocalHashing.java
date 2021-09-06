package tools.differential_privacy.ldp.frequency_oracle.foImp;

import tools.differential_privacy.ldp.frequency_oracle.FrequencyOracle;

public class OptimizedLocalHashing extends GeneralizedRandomizedResponse implements FrequencyOracle {

    private int g;
    private double[] data;

    public OptimizedLocalHashing(double epsilon, double[] data) {
        super(epsilon, data);
    }

    //todo: design hash function first
    @Override
    public Object perturb(Object rawData) {
        return null;
    }

    @Override
    public double aggregate(Object data, int noiseEstimate) {
        return 0;
    }
}
