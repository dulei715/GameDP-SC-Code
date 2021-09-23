package tools.basic.integration.simpson;

import tools.basic.integration.trapezoid.d3r1F;

import java.text.DecimalFormat;

public class d3r3F {
    public double qsimp_s;

    double fint(double x) {
        double aaa = 4.0 * x * (x * x -7.0) * Math.sin(x);
        return aaa - (x * x * x * x - 14.0 * x * x + 28.0) * Math.cos(x);
    }

    public void qsimp(double a, double b, double s) {
        double eps = 0.000001;
        int jmax = 20;
        double ost = -1e+30;
        double st, os = -1e+30;
        st = 0;
        for (int j = 0; j < jmax; j++) {
            st = d3r1F.trapzd_static(a, b, st, j);
            s= (4.0 * st - ost) / 3.0;
            if (Math.abs(s-os)<eps*Math.abs(os)) {
                this.qsimp_s = s;
                return;
            }
            os = s;
            ost = st;
        }
        System.out.println("Too many steps");

    }

    public static void main(String[] args) {
        double pio2 = Math.PI / 2;
        double s, b, a = 0.0;
        d3r3F g = new d3r3F();
        DecimalFormat form = new DecimalFormat("0.000000");
        b = pio2;
        s = 0;
        System.out.println("integral of func computed with qsimp");
        System.out.println("actual value of integral is: ");
        System.out.println(form.format((g.fint(b) - g.fint(a))));
        g.qsimp(a, b, s);
        s = g.qsimp_s;
        System.out.println("result from routine qtrap is: ");
        System.out.println(form.format(s));
    }


}
