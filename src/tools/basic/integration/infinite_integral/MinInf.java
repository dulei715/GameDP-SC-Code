package tools.basic.integration.infinite_integral;

import tools.basic.integration.simpson.d3r3F;

import java.text.DecimalFormat;

public class MinInf {
    public double func(double x) {
        return 1 / (2 * Math.exp(x));
    }

    public double inf(double x) {
        return func(1/x)/Math.pow(x,2);
    }

    public double midinf_s;
    public void midinf(double aa, double bb, double s, int n) {
        int ii, tnm, j;
        double del, ddel, x, sum;
        double b = 1.0 / aa;
        double a = 1.0 / bb;
        if (n == 1) {
            s = (b - a) * inf(0.5 * (a + b));
            ii = 1;
        } else {
            ii = (int)Math.pow(3, n-2);
            tnm = ii;
            del = (b - a) / (3.0 * tnm);
            ddel = del + del;
            x = a + 0.5 * del;
            sum = 0.0;
            for (j = 1; j <= ii; j++) {
                sum += inf(x);
                x += ddel;
                sum += inf(x);
                x += del;
            }
            s = (s + (b-a) * sum / tnm) / 3.0;
        }
        this.midinf_s = s;
    }

    public static void main(String[] args) {
        double s, b = Double.MAX_VALUE, aa = 0.00001;
        int n;
        MinInf g = new MinInf();
        DecimalFormat form = new DecimalFormat("0.000000");
        s = 0;
        n = 15;

        for (int i = 1; i <= n; i++) {
            g.midinf(aa, b, s, i);
            s = g.midinf_s;
            System.out.println(i + " ");
            System.out.println(form.format(s));
        }

    }

}
