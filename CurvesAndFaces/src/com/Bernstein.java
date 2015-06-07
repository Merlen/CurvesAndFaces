package com;

/**
 *
 * @author Merlen
 */
public class Bernstein {


    /**
     * Calcs factor for given x
     */
    private static float factorial(int x) {
        float fact = 1;
        int p = 1;
        if (x == 0 || x == 1) {
            return 1;
        } else {
            while (p <= x) {
                fact *= p;
                p++;
            }
        }
        return fact;
    }

    /**
     * Calc Binom
     */
    private static float binomial(int f, int n) {
        return factorial(n) / (factorial(f) * factorial(n - f));
    }

    public static Point bernstein(Point[] ctrl, float t, int n) {
        Point N = new Point();
        N.x = N.y = N.z = 0;
        for (int i = 0; i <= n; i++) {
            N.x += ctrl[i].x * binomial(i, n) * Math.pow(t, i) * Math.pow((1 - t), (n - i));
            N.y += ctrl[i].y * binomial(i, n) * Math.pow(t, i) * Math.pow((1 - t), (n - i));
            N.z += ctrl[i].z * binomial(i, n) * Math.pow(t, i) * Math.pow((1 - t), (n - i));
        }

        return N;
    }

    private static void log(Object aObject) {
        System.out.println(String.valueOf(aObject));
    }
}
