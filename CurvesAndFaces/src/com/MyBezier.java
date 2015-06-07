/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com;

import java.util.ArrayList;

/**
 *
 * @author Merlen
 */
public class MyBezier {

    public static Point ab;
    public static Point bc;
    public static Point cd;

    public static Point abbc;
    public static Point bccd;

    //returns point on bezier Curve
    public static Point deCasteljau(Point a, Point b, Point c, Point d, float t) {
        ab = bezierBetweenPoints(a, b, t);
        bc = bezierBetweenPoints(b, c, t);
        cd = bezierBetweenPoints(c, d, t);

        abbc = bezierBetweenPoints(ab, bc, t);
        bccd = bezierBetweenPoints(bc, cd, t);

        Point bezier = bezierBetweenPoints(abbc, bccd, t);

        return bezier;
    }

    public static Point[] deCasteljau(Point[] points, float t) {
        ArrayList<Point> pointList = new ArrayList();
        ArrayList<Point> tmp = new ArrayList();

        for (int i = 0; i < (points.length - 1); i++) {
            tmp.add(bezierBetweenPoints(points[i], points[i + 1], t));
        }
        pointList.addAll(tmp);
        tmp.clear();

        tmp.addAll(calcPoints(pointList, t));
        pointList.addAll(tmp);

        pointList.addAll(calcPoints(tmp, t));

        Point[] curveLine = new Point[pointList.size()];
        pointList.toArray(curveLine);

        return curveLine;
    }

    private static ArrayList<Point> calcPoints(ArrayList<Point> points, float t) {
        ArrayList<Point> tmp = new ArrayList();
        for (int i = 0; i < (points.size() - 1); i++) {
            tmp.add(bezierBetweenPoints(points.get(i), points.get(i + 1), t));
        }
        return tmp;
    }

    private static Point bezierBetweenPoints(Point a, Point b, float t) {
        Point ler = new Point();
        ler.x = a.x + (b.x - a.x) * t;
        ler.y = a.y + (b.y - a.y) * t;
        ler.z = a.z + (b.z - a.z) * t;

        return ler;
    }

    private static double factorial(int x) {
        double fact = 1;
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

    private static double binomial(int f, int n) {
        double com = factorial(n) / (factorial(f) * factorial(n - f));
        return com;
    }

    public static Point bernstein(Point[] ctrl, float t, int n) {

        Point N = new Point();
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
