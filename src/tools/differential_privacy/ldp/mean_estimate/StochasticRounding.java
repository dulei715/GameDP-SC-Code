package tools.differential_privacy.ldp.mean_estimate;

public class StochasticRounding {
    private double epsilon;
    private double p;
    private double q;

    public StochasticRounding(double epsilon) {
        this.epsilon = epsilon;
        this.p = Math.exp(epsilon) / (Math.exp(epsilon) + 1);
        this.q = 1 - this.p;
    }

    public int getRandomValue(double v) {
        if (v < -1 || v > 1) {
            throw new RuntimeException("v is not between [-1, 1]");
        }
        double randomPoint = Math.random();
        double probability = this.q + (this.p - this.q) * (1 - v) / 2;
        if (randomPoint < probability) {
            return -1;
        }
        return 1;
    }
}
