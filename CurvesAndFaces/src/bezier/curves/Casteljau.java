package bezier.curves;

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
        ArrayList<Point> pointe = new ArrayList<>();
        int n = points.length - 1;

        setTempPoints(points);

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
        float homW = (1 - t) * a.weigth + t * b.weigth; //homgenized weigth (wki)

        ler.x = (1 - t) * a.x * (a.weigth / homW) + t * b.x * (b.weigth / homW);
        ler.y = (1 - t) * a.y * (a.weigth / homW) + t * b.y * (b.weigth / homW);
        ler.z = (1 - t) * a.z * (a.weigth / homW) + t * b.z * (b.weigth / homW);

        return ler;
    }

    /**
     * DeCasteljau rekursiv Version
     * unused
     **/
    public static Point DeCasteljau(int k, int i, float t, Point[] P) {
        if (k == 0)
            return P[i];
        return DeCasteljau(k - 1, i, t, P).times((1 - t)).plus(DeCasteljau(k - 1, i + 1, t, P).times(t));
    }


    public static Point[] blosControl(Point[] points, float t1, float t2) {
        int n = points.length - 1;
        log("n: " + n);
        int step = n;

        ArrayList<Point> ctrlPoints = new ArrayList();
        ArrayList<Point> pointer = new ArrayList<>();


        setTempPoints(points);
        for (int j = n + 1; j > 0; j--) {
            //log("-------------------------------------------------------- j: " + j);
            setTempPoints(points);
            if (tempPoints.size() > 0) {
                for (int k = 1; k <= n; k++) {
                    //log("-------------------------- k: " + k);

                    for (int i = 0; i <= n - k; i++) {
                        //log("i: " + (j+i) + " step " + step);
                        Point castelPoint;

                        if ((k) <= step) {
                            //log("---------------------- A");
                            castelPoint = getCasteljauPoint(tempPoints.get(i), tempPoints.get(i + 1), t1);
                            tempPoints.get(i).set(castelPoint);
                            pointer.add(castelPoint);

                        } else {
                            //log("---------------------- B");
                            castelPoint = getCasteljauPoint(tempPoints.get(i), tempPoints.get(i + 1), t2);
                            tempPoints.get(i).set(castelPoint);
                            pointer.add(castelPoint);
                        }
                    }

                }
                ctrlPoints.add(tempPoints.get(0));
            }
            step -= 1;
        }

        //log("ctrl size: " + ctrlPoints.size());
        Point[] curveLine = new Point[ctrlPoints.size()];
        ctrlPoints.toArray(curveLine);

        return curveLine;

    }


    /**
     * Prints Information
     */
    private static void log(Object aObject) {
        System.out.println("Casteljau" + " " + String.valueOf(aObject));
    }

    public static void setTempPoints(Point[] points) {
        tempPoints.clear();
        for (int i = 0; i <= points.length - 1; i++) {
            Point p = new Point(points[i].x, points[i].y, points[i].z, points[i].weigth);
            tempPoints.add(i, p);
        }
    }
}


