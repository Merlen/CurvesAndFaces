/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Merlen
 */
public class Casteljau {
    private static ArrayList<Point> curvePoints = new ArrayList();
    private static ArrayList<Point> tempPoints = new ArrayList();

    /**
     * DeCastelJau Formula for ControlPoints
     */
    public static Point[] deCasteljau(Point[] points, float t) {
        ArrayList<Point> pointList = new ArrayList();
        ArrayList<Point> pointe = new ArrayList();
        int n = points.length - 1;
        for (int i = 0; i <= n; i++) {
            Point p = new Point(points[i].x, points[i].y, points[i].z);
            pointList.add(i, p);
        }
        for (int k = 1; k <= n; k++) {
            for (int i = 0; i <= n - k; i++) {
                Point castelPoint = getCasteljauPoint(pointList.get(i), pointList.get(i + 1), t);
                pointList.get(i).x = castelPoint.x;
                pointList.get(i).y = castelPoint.y;
                pointList.get(i).z = castelPoint.z;
                pointe.add(castelPoint);
            }
        }
        Point[] curveLine = new Point[pointe.size()];
        pointe.toArray(curveLine);
        return curveLine;
    }

    /**
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
            Point p = new Point(ctrlPoints.get(i).x, ctrlPoints.get(i).y, ctrlPoints.get(i).z);
            tempPoints.add(i, p);
        }
        for (int k = 1; k <= n; k++) {
            for (int i = 0; i <= n - k; i++) {
                Point castelPoint = getCasteljauPoint(tempPoints.get(i), tempPoints.get(i + 1), t);
                tempPoints.get(i).x = castelPoint.x;
                tempPoints.get(i).y = castelPoint.y;
                tempPoints.get(i).z = castelPoint.z;
            }
        }
        return tempPoints.get(0);
    }

    /**
     * Return A Point with CastelJau Formula
     */
    public static Point getCasteljauPoint(Point a, Point b, float t) {
        Point ler = new Point();
        ler.x = (1 - t) * a.x + t * b.x;
        ler.y = (1 - t) * a.y + t * b.y;
        ler.z = (1 - t) * a.z + t * b.z;
        return ler;
    }
    
}
