package tools.differential_privacy.ldp.frequency_oracle.foImp;

import tools.differential_privacy.ldp.frequency_oracle.FrequencyOracle;

import java.util.Arrays;
import java.util.Random;

public class GeneralizedRandomizedResponse implements FrequencyOracle {

    private double epsilon;
    private int size;
    private double p;
    private double q;
    private double data[];

    private Random random;

    public GeneralizedRandomizedResponse(double epsilon, double[] data) {
        this.epsilon = epsilon;
        this.data = data;
        this.size = data.length;
        this.p = Math.exp(epsilon) / (Math.exp(epsilon) + this.size - 1);
        this.q = 1 / (Math.exp(epsilon) + this.size - 1);

        random = new Random();
        Arrays.sort(this.data);
    }

    private double getRandomDataExcept(int index) {
        int pos = this.random.nextInt(size - 1);
        if (pos < index) {
            return this.data[pos];
        }
        return this.data[pos+1];
    }

    @Override
    public Object perturb(Object rawData) {
        double randomPoint = Math.random();
        if (randomPoint < this.p) {
            return rawData;
        }
        double rawDoubleData = (double)rawData;
        int index = Arrays.binarySearch(this.data, rawDoubleData);
        return getRandomDataExcept(index);
    }

    @Override
    public double aggregate(Object data, int noiseEstimate) {
        return (noiseEstimate / this.size - this.q)/(this.p - this.q);
    }


}
