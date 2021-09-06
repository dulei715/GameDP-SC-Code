package tools.differential_privacy.ldp.mean_estimate;

public class PiecewiseMechanism {
    private double epsilon;
    private double p;
    private double q;
    private double s;

    public PiecewiseMechanism(double epsilon) {
        this.epsilon = epsilon;
        this.s = (Math.exp(epsilon / 2) + 1) / (Math.exp(epsilon / 2) - 1);
    }

    public double getRandomValue(double v) {
        if (v < -1 || v > 1) {
            throw new RuntimeException("v is not between [-1, 1]");
        }
        double lv = (Math.exp(this.epsilon/2) * v - 1) / (Math.exp(this.epsilon/2) - 1);
        double rv = (Math.exp(this.epsilon/2) * v + 1) / (Math.exp(this.epsilon/2) - 1);
        double innerProbability = Math.exp(this.epsilon/2) / (Math.exp(this.epsilon/2) + 1);
        double randomPosition = Math.random();
        if (randomPosition < innerProbability) {
            return (Math.random() * ( rv - lv ) + lv);
        }
        randomPosition = Math.random();
        if (randomPosition < 0.5) {
            return (Math.random() * (lv + this.s) - this.s);
        }
        return (Math.random() * (this.s - rv) + rv);
    }
}
