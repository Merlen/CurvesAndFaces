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




    private static void log(Object aObject) {
        System.out.println("Bernstein: " + " " +String.valueOf(aObject));
    }
}
