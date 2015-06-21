package bezier;

import help.MyMath;
import struct.Point;
import struct.Vector;

import java.util.ArrayList;

/**
 * @author Merlen
 */
public class Bernstein {


    public static Point bernsteinCurve(Point[] ctrl, float t, int n) {
        Point N = new Point();
        N.x = N.y = N.z = 0;
        for (int i = 0; i <= n; i++) {
            N.x += ctrl[i].x * MyMath.binomial(i, n) * Math.pow(t, i) * Math.pow((1 - t), (n - i)) * ctrl[i].weigth;
            N.y += ctrl[i].y * MyMath.binomial(i, n) * Math.pow(t, i) * Math.pow((1 - t), (n - i)) * ctrl[i].weigth;
            N.z += ctrl[i].z * MyMath.binomial(i, n) * Math.pow(t, i) * Math.pow((1 - t), (n - i)) * ctrl[i].weigth;
        }
        return N;
    }

    public static Point bernsteinPolynomial(int i, int n, float t) {
        Point N = new Point();
        N.x = N.y = N.z = 0;

        N.x = t;
        N.y = (float) (MyMath.binomial(i, n) * Math.pow(t, i) * Math.pow((1 - t), (n - i)));

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
                N.x += points[i].x * MyMath.binomial(i, n) * Math.pow(t, i) * Math.pow((1 - t), (n - i)) * points[i].weigth;
                N.y += points[i].y * MyMath.binomial(i, n) * Math.pow(t, i) * Math.pow((1 - t), (n - i)) * points[i].weigth;
                N.z += points[i].z * MyMath.binomial(i, n) * Math.pow(t, i) * Math.pow((1 - t), (n - i)) * points[i].weigth;
            }

            return N;
        } else {
            Point[] _p = new Point[points.length-1];
            for (int i = 0; i < _p.length; i++) {
                float x = (points[i+1].x - points[i].x) * n;
                float y = (points[i+1].y - points[i].y) * n;
                float z = (points[i+1].z - points[i].z) * n;

                _p[i] = new Point(x,y,z);
            }


            return getDerivate(derivate - 1, t , _p);
        }
    }

    private static void log(Object aObject) {
        System.out.println("Bernstein: " + " " +String.valueOf(aObject));
    }
}
