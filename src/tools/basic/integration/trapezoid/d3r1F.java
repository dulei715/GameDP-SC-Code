package tools.basic.integration.trapezoid;

import java.text.DecimalFormat;

public class d3r1F {
    double trapzd_s;

    public static double func(double a) {
        return a * a * (a * a - 2) * Math.sin(a);
    }

    public void trapzd(double a, double b, double s, int n) {
        double del, x, sum;
        int j, it, tnm;
        if (n == 1) {
            s = 0.5 * (b - a) * (func(a) + func(b));
            it = 1;
        } else {
            it = (int)Math.pow(2, n - 2);
            tnm = it;
            del = (b - a) / tnm;
            x = a + 0.5 * del;
            sum = 0.0;
            for (j = 1; j <= it; j++) {
                sum += func(x);
                x += del;
            }
            s = 0.5 * (s + (b - a) * sum / tnm);
        }
        trapzd_s = s;
    }

    public static double trapzd_static(double a, double b, double s, int n) {
        double del, x, sum;
        int j, it, tnm;
        if (n == 1) {
            s = 0.5 * (b - a) * (func(a) + func(b));
            it = 1;
        } else {
            it = (int)Math.pow(2, n - 2);
            tnm = it;
            del = (b - a) / tnm;
            x = a + 0.5 * del;
            sum = 0.0;
            for (j = 1; j <= it; j++) {
                sum += func(x);
                x += del;
            }
            s = 0.5 * (s + (b - a) * sum / tnm);
        }
        return s;
    }

    double fint(double x) {
        double aaa = 4.0 * x * (x * x -7.0) * Math.sin(x);
        return aaa - (x * x * x * x - 14.0 * x * x + 28.0) * Math.cos(x);
    }

    public static void main(String[] args) {
        int i;
        int nmax = 14;
        double pio2 = 1.5707963;
        double s, b, a = 0.0;
        s = 0;
        d3r1F g = new d3r1F();
        DecimalFormat form = new DecimalFormat("0.000000");
        b = pio2;
        System.out.println();
        System.out.printf("integral of func with 2^(n-1) points");
        System.out.println("actual value of integral is: ");
        System.out.println(form.format((g.fint(b) - g.fint(a))));
        System.out.println();
        System.out.println("n approx.integral");
        for (i = 0; i < nmax; i++) {
            g.trapzd(a, b, s, i);
            s = g.trapzd_s;
            System.out.println(i + " ");
            System.out.println(form.format(s));
        }
    }

}
