package bezier;

import help.MyMath;
import struct.Point;
import struct.Vector;

/**
 * @author Merlen
 *
 */
public class Derivate {


    /**
     * Not Implemented Yet
     * @deprecated
     * */
    public static Vector getCasteljauDerivate(int derivate, float t, Point[] points) {
        int n = points.length - 1;

        Point[] deltas = new Point[n];

        for (int i = 0; i < n; i++) {
            deltas[i] = new Point();
            deltas[i].x = points[i + 1].x - points[i].x;
            deltas[i].y = points[i + 1].y - points[i].y;
            deltas[i].z = points[i + 1].z - points[i].z;
        }

        Point[] ctrl = Casteljau.deCasteljau(deltas, t); // control Points

        return new Vector(0, 0, 0);
    }

    /**
     * Derivate with Bernstein
     *  ! Without Weights !
     */
    public static Vector getBernsteinDerivate(int derivate, float t, Point[] points) {
        int n = points.length - 1;
        if (n == 0) return new Vector(0, 0, 0);

        if (derivate == 0) {
            Vector N = new Vector(0, 0, 0);

            for (int i = 0; i <= n; i++) {
                //(points[i + 1].weigth - points[i].weigth)
                N.x += points[i].x * MyMath.binomial(i, n) * Math.pow(t, i) * Math.pow((1 - t), (n - i)) * points[i].weigth;
                N.y += points[i].y * MyMath.binomial(i, n) * Math.pow(t, i) * Math.pow((1 - t), (n - i)) * points[i].weigth;
                N.z += points[i].z * MyMath.binomial(i, n) * Math.pow(t, i) * Math.pow((1 - t), (n - i)) * points[i].weigth;
            }

            return N;
        } else {
            Point[] _p = new Point[points.length - 1];
            for (int i = 0; i < _p.length; i++) {
                float x = (points[i + 1].x - points[i].x) * n;
                float y = (points[i + 1].y - points[i].y) * n;
                float z = (points[i + 1].z - points[i].z) * n;

                _p[i] = new Point(x, y, z);
            }
            return getBernsteinDerivate(derivate - 1, t, _p);
        }
    }

}
