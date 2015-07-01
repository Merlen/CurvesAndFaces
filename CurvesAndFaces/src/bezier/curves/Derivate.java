package bezier.curves;

import help.MyMath;
import struct.Point;
import struct.Vector;

import java.util.ArrayList;

/**
 * @author Merlen
 *
 */
public class Derivate {


    /**
     * Returns Vector with Casteljau
     * */
    public static Vector getCasteljauDerivate(float t, Point[] points) {
        ArrayList<Point> pointe = new ArrayList<>();
        ArrayList<Point> tempPoints = new ArrayList<>();
        int n = points.length - 1;
        for (int i = 0; i <= n; i++) {
            Point p = new Point(points[i].x, points[i].y, points[i].z, points[i].weigth);
            tempPoints.add(i, p);
        }
        //log(tempPoints.size() + " " + points.length);
        if (tempPoints.size() > 0) {
            for (int k = 1; k <= n; k++) {
                for (int i = 0; i <= n - k; i++) {
                    Point castelPoint = Casteljau.getCasteljauPointOneT(tempPoints.get(i), tempPoints.get(i + 1), t);
                    tempPoints.get(i).set(castelPoint);
                    pointe.add(castelPoint);
                }
            }
        }

        Point castelMin2 = pointe.get(pointe.size() - 3); //links von t-Point
        Point castelMin1 = pointe.get(pointe.size() - 2); // Rechts von t-Point

        Vector vector = new Vector();

        vector.x = castelMin1.x - castelMin2.x;
        vector.y = castelMin1.y - castelMin2.y;
        vector.z = castelMin1.z - castelMin2.z;

        return vector;
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
