package bezier;

import struct.Point;

import java.util.ArrayList;

/**
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

    public static Point bernsteinCurve(Point[] ctrl, float t, int n) {
        Point N = new Point();
        N.x = N.y = N.z = 0;
        for (int i = 0; i <= n; i++) {
            N.x += ctrl[i].x * binomial(i, n) * Math.pow(t, i) * Math.pow((1 - t), (n - i)) * ctrl[i].weigth;
            N.y += ctrl[i].y * binomial(i, n) * Math.pow(t, i) * Math.pow((1 - t), (n - i)) * ctrl[i].weigth;
            N.z += ctrl[i].z * binomial(i, n) * Math.pow(t, i) * Math.pow((1 - t), (n - i)) * ctrl[i].weigth;
        }
        return N;
    }

    public static Point bernsteinPolynomial(int i, int n, float t) {
        Point N = new Point();
        N.x = N.y = N.z = 0;

        N.x = t;
        N.y = (float) (binomial(i, n) * Math.pow(t, i) * Math.pow((1 - t), (n - i)));

        return N;
    }


    /**
     * TODO ?
     * Ableitung
     * */
    public static Point getDerivate(int derivate, float t, Point[] points){
        int n = points.length -1;
        if(n==0) return new Point(0,0,0);

        if(derivate == 0){
            Point N = new Point(0,0,0);

            for (int i = 0; i <= n; i++) {
                N.x += points[i].x * binomial(i, n) * Math.pow(t, i) * Math.pow((1 - t), (n - i)) * points[i].weigth;
                N.y += points[i].y * binomial(i, n) * Math.pow(t, i) * Math.pow((1 - t), (n - i)) * points[i].weigth;
                N.z += points[i].z * binomial(i, n) * Math.pow(t, i) * Math.pow((1 - t), (n - i)) * points[i].weigth;
            }

            return N;
        } else {
            Point[] _p = new Point[points.length-1];

            for (int i = 0; i < _p.length; i++) {

                float x = (points[i+1].x - points[i].x) * n;
                float y = (points[i+1].y - points[i].y) * n;
                float z = (points[i+1].z - points[i].z) * n;

                if(points[i+1].weigth > 1){
                    log("weight > 1");
                    x *= (points[i+1].weigth - points[i].weigth);
                    y *= (points[i+1].weigth - points[i].weigth);
                    z *= (points[i+1].weigth - points[i].weigth);
                }

                _p[i] = new Point(x,y,z);
            }

            return getDerivate(derivate - 1, t , _p);
        }
    }

    private static void log(Object aObject) {
        System.out.println("Bernstein: " + " " +String.valueOf(aObject));
    }
}
