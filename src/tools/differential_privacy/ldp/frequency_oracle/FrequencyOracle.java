package tools.differential_privacy.ldp.frequency_oracle;

public interface FrequencyOracle {
    /**
     * Used by each user to perturb her input value
     * @param rawData
     * @return
     */
    Object perturb(Object rawData);

    /**
     * Used by the aggregator to aggregate and unbia values
     * @param data
     * @return
     */
    double aggregate(Object data, int noiseEstimate);
}
