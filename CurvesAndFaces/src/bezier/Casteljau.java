/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bezier;

import struct.Point;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Merlen
 */
public class Casteljau {
    private static ArrayList<Point> curvePoints = new ArrayList();
    private static ArrayList<Point> tempPoints = new ArrayList();

    /**
     * DeCasteljau Formula for ControlPoints
     */
    public static Point[] deCasteljau(Point[] points, float t) {
        ArrayList<Point> pointList = new ArrayList();
        ArrayList<Point> pointe = new ArrayList();
        int n = points.length - 1;
        for (int i = 0; i <= n; i++) {
            Point p = new Point(points[i].x, points[i].y, points[i].z, points[i].weigth);
            pointList.add(i, p);
        }
        for (int k = 1; k <= n; k++) {
            for (int i = 0; i <= n - k; i++) {
                Point castelPoint = DeCasteljau(k, i, t, points);
                pointList.get(i).set(castelPoint);
                pointe.add(castelPoint);
            }
        }
        Point[] curveLine = new Point[pointe.size()];
        pointe.toArray(curveLine);
        return curveLine;
    }

    /**
     * @param lowT Lower Parameter
     * @param upT  Upper Parameter
     * @return List of Points for an Curve
     */
    public static Point[] deCasteljauCurve(Point[] points, float lowT, float upT) {
        curvePoints = new ArrayList();
        ArrayList<Point> cpyPoint = new ArrayList();
        cpyPoint.addAll(Arrays.asList(points));
        for (float i = lowT; i < upT; i += 0.02) {
            curvePoints.add(getCasteljauPoint(i, cpyPoint));
        }
        Point[] curveLine = new Point[curvePoints.size()];
        curvePoints.toArray(curveLine);
        return curveLine;
    }

    /**
     * Get a Point of Casteljau
     */
    private static Point getCasteljauPoint(float t, ArrayList<Point> ctrlPoints) {
        tempPoints.clear();
        int n = ctrlPoints.size() - 1;
        for (int i = 0; i <= n; i++) {
            Point p = new Point(ctrlPoints.get(i).x, ctrlPoints.get(i).y, ctrlPoints.get(i).z, ctrlPoints.get(i).weigth);
            tempPoints.add(i, p);
        }
        for (int k = 1; k <= n; k++) {
            for (int i = 0; i <= n - k; i++) {
                Point castelPoint = getCasteljauPoint(tempPoints.get(i), tempPoints.get(i + 1), t);
                tempPoints.get(i).set(castelPoint);
            }
        }
        return tempPoints.get(0);
    }

    /**
     * Return A Point with CastelJau Formula
     */
    public static Point getCasteljauPoint(Point a, Point b, float t) {
        Point ler = new Point();

        ler.x = (1 - t) * a.x * a.weigth + t * b.x * b.weigth;
        ler.y = (1 - t) * a.y * a.weigth + t * b.y * b.weigth;
        ler.z = (1 - t) * a.z * a.weigth + t * b.z * b.weigth;
        return ler;
    }

    public static Point DeCasteljau(int k, int i, float t, Point[] P) {
        if (k == 0)
            return P[i];
        return DeCasteljau(k - 1, i, t, P).times((1 - t)).plus(DeCasteljau(k - 1, i + 1, t, P).times(t));
    }

    /**
     * TODO
     *
     * @param points
     * @param multiT
     * @return
     */
    public static Point[] blossom(Point[] points, float[] multiT) {
        int n = points.length - 1;

        float t1 = multiT[0];
        float t2 = multiT[1];

        log(t1 + " " + t2);
        Point a = points[0];
        Point b = points[1];
        Point c = points[2];
        Point d = points[3];

        Point[] ctrl = new Point[points.length];

        Point aaa = new Point(0, 0, 0);
        aaa.x = a.x + (b.x / n) * (t1 * t1 * t1) + (c.x / n) * (t1 * t1 + t1 * t1 + t1 * t1) + d.x * t1 * t1 * t1;
        aaa.y = a.y + (b.y / n) * (t1 * t1 * t1) + (c.y / n) * (t1 * t1 + t1 * t1 + t1 * t1) + d.y * t1 * t1 * t1;
        aaa.z = a.z + (b.z / n) * (t1 * t1 * t1) + (c.z / n) * (t1 * t1 + t1 * t1 + t1 * t1) + d.z * t1 * t1 * t1;

        Point aab = new Point(0, 0, 0);
        aab.x = a.x + (b.x / n) * (t1 * t1 * t2) + (c.x / n) * (t1 * t1 + t1 * t2 + t1 * t2) + d.x * t1 * t1 * t2;
        aab.y = a.y + (b.y / n) * (t1 * t1 * t2) + (c.y / n) * (t1 * t1 + t1 * t2 + t1 * t2) + d.y * t1 * t1 * t2;
        aab.z = a.z + (b.z / n) * (t1 * t1 * t2) + (c.z / n) * (t1 * t1 + t1 * t2 + t1 * t2) + d.z * t1 * t1 * t2;

        Point abb = new Point(0, 0, 0);
        abb.x = a.x + (b.x / n) * (t1 * t2 * t2) + (c.x / n) * (t1 * t2 + t1 * t2 + t2 * t2) + d.x * t1 * t2 * t2;
        abb.y = a.y + (b.y / n) * (t1 * t2 * t2) + (c.y / n) * (t1 * t2 + t1 * t2 + t2 * t2) + d.y * t1 * t2 * t2;
        abb.z = a.z + (b.z / n) * (t1 * t2 * t2) + (c.z / n) * (t1 * t2 + t1 * t2 + t2 * t2) + d.z * t1 * t2 * t2;

        Point bbb = new Point(0, 0, 0);
        bbb.x = a.x + (b.x / n) * (t2 * t2 * t2) + (c.x / n) * (t2 * t2 + t2 * t2 + t2 * t2) + d.x * t2 * t2 * t2;
        bbb.y = a.y + (b.y / n) * (t2 * t2 * t2) + (c.y / n) * (t2 * t2 + t2 * t2 + t2 * t2) + d.y * t2 * t2 * t2;
        bbb.z = a.z + (b.z / n) * (t2 * t2 * t2) + (c.z / n) * (t2 * t2 + t2 * t2 + t2 * t2) + d.z * t2 * t2 * t2;

        ctrl[0] = aaa;
        ctrl[1] = aab;
        ctrl[2] = abb;
        ctrl[3] = bbb;

        for (Point p:ctrl) log(p);
        return ctrl;
    }

    /**
     * TODO
     * Ableitung
     */
    public static Point getDerivate(int derivate, float t, Point[] points) {
        int n = points.length - 1;
        if (n == 0) return new Point(0, 0, 0);


        return getDerivate(derivate - 1, t, points);
    }


    /**
     * Prints Information
     */
    private static void log(Object aObject) {
        System.out.println("Casteljau" + " " + String.valueOf(aObject));
    }
}


