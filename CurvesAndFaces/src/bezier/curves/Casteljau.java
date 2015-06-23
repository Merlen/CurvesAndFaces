package bezier.curves;

import help.Constants;
import struct.Point;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Merlen
 */
public class Casteljau {
    private static ArrayList<Point> tempPoints = new ArrayList<>();

    /**
     * DeCasteljau Formula for ControlPoints
     */
    public static Point[] deCasteljau(Point[] points, float t) {
        tempPoints.clear();
        ArrayList<Point> pointe = new ArrayList<>();
        int n = points.length - 1;
        for (int i = 0; i <= n; i++) {
            Point p = new Point(points[i].x, points[i].y, points[i].z, points[i].weigth);
            tempPoints.add(i, p);
        }
        //log(tempPoints.size() + " " + points.length);
        if (tempPoints.size() > 0) {
            for (int k = 1; k <= n; k++) {
                for (int i = 0; i <= n - k; i++) {
                    Point castelPoint = getCasteljauPoint(tempPoints.get(i), tempPoints.get(i + 1), t);
                    tempPoints.get(i).set(castelPoint);
                    pointe.add(castelPoint);
                }
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
        ArrayList<Point> curvePoints = new ArrayList<>();
        ArrayList<Point> cpyPoint = new ArrayList<>();
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

    /**
     * DeCasteljau rekursiv Version
     **/
    public static Point DeCasteljau(int k, int i, float t, Point[] P) {
        if (k == 0)
            return P[i];
        return DeCasteljau(k - 1, i, t, P).times((1 - t)).plus(DeCasteljau(k - 1, i + 1, t, P).times(t));
    }

    /**
     * TODO
     * Not Implemented Yet
     */
    public static Point[] blossom(Point[] points, float[] multiT) {
        int n = points.length - 1;

        float a = multiT[0];
        float b = multiT[1];

        Point[] aHalf = Casteljau.deCasteljau(Constants.points, a); // control Points
        Point[] bHalf = Casteljau.deCasteljau(Constants.points, b); // control Points


        log(aHalf.length + " " + bHalf.length);

        Point[] blossom = new Point[2];

        blossom[0] = aHalf[aHalf.length - 1];
        blossom[1] = bHalf[bHalf.length - 1];


        return blossom;
    }


    //TODO
    private static Point getBlossomVal(Point a, Point b) {
        Point blos = new Point();

        return blos;
    }


    /**
     * Prints Information
     */
    private static void log(Object aObject) {
        System.out.println("Casteljau" + " " + String.valueOf(aObject));
    }
}


