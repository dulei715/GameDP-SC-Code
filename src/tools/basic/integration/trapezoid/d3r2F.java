package tools.basic.integration.trapezoid;

import java.text.DecimalFormat;

public class d3r2F {
    public double qtrap_s;

    public double func(double a) {
        return a * a * (a * a - 2) * Math.sin(a);
    }

    void qtrap(double a, double b, double s) {
        double eps = 0.000001;
        int jmax = 20;
        double olds = 1e+30;
        for (int j = 1; j <= jmax; j++) {
            s = d3r1F.trapzd_static(a, b, s, j);
            if (Math.abs(s-olds)<eps * Math.abs(olds)) {
                this.qtrap_s = s;
                return;
            }
            olds = s;
        }
        System.out.println("Too many steps");
    }

    double fint(double x) {
        double aaa = 4.0 * x * (x * x -7.0) * Math.sin(x);
        return aaa - (x * x * x * x - 14.0 * x * x + 28.0) * Math.cos(x);
    }

    public static void main(String[] args) {
        double pio2 = 1.5707963;
        double s, b, a = 0.0;
        d3r2F g = new d3r2F();
        DecimalFormat form = new DecimalFormat("0.000000");
        b = pio2;
        s = 0;
        System.out.println();
        System.out.printf("integral of func computed with qtrap");
        System.out.println("actual value of integral is: ");
        System.out.println(form.format((g.fint(b) - g.fint(a))));
        g.qtrap(a, b, s);
        s = g.qtrap_s;
        System.out.println("result from routine qtrap is: ");
        System.out.println(form.format(s));
    }

}
