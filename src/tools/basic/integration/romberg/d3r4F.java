package tools.basic.integration.romberg;

import tools.basic.integration.trapezoid.d3r1F;

public class d3r4F {
    public double qromb_ss;
    public void qromb(double a, double b, double ss) {
        double dss, eps = 0.000001;
        int jmax = 20;
        int jmaxp = jmax + 1;
        int j,k = 5;
        int km = k - 1;
        double[] s = new double[21];
        double[] h = new double[21];
        dss = 0;
        h[1] = 1.0;
        for (j = 1; j <= jmax; j++) {
            s[j] = d3r1F.trapzd_static(a, b, s[j], j);
            if (j >= k) {
//                polint(h, s, k, 0.0, ss, dss);
//                ss
                // todo: to be continue...
            }

        }
    }
}
