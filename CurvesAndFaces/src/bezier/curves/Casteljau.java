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
                    Point castelPoint = getCasteljauPointOneT(tempPoints.get(i), tempPoints.get(i + 1), t);
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
                Point castelPoint = getCasteljauPointOneT(tempPoints.get(i), tempPoints.get(i + 1), t);


                tempPoints.get(i).set(castelPoint);
            }
        }
        return tempPoints.get(0);
    }


    /**
     * Return A Point with CastelJau Formula
     */
    public static Point getCasteljauPointOneT(Point a, Point b, float t) {
        Point ler = new Point();
        float homW = (1 - t) * a.weigth + t * b.weigth; //homgenized weigth (wki)

        ler.x = (1 - t) * a.x * (a.weigth / homW) + t * b.x * (b.weigth / homW);
        ler.y = (1 - t) * a.y * (a.weigth / homW) + t * b.y * (b.weigth / homW);
        ler.z = (1 - t) * a.z * (a.weigth / homW) + t * b.z * (b.weigth / homW);
        return ler;
    }

    /**
     * Return A Point with CastelJau Formula
     */
    public static Point getCasteljauPointTwoT(Point a, Point b, float t1, float t2) {
        Point ler = new Point();
        float homW = (1 - t1) * a.weigth + t2 * b.weigth; //homgenized weigth (wki)

        ler.x = (1 - t1) * a.x * (a.weigth / homW) + t2 * b.x * (b.weigth / homW);
        ler.y = (1 - t1) * a.y * (a.weigth / homW) + t2 * b.y * (b.weigth / homW);
        ler.z = (1 - t1) * a.z * (a.weigth / homW) + t2 * b.z * (b.weigth / homW);
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


    /**
     * TODO FIXME
     */
    public static Point[] blosControl(Point[] points, float t1, float t2) {
        int n = points.length;
        log("n: " + n);
        Point curveControl[] = new Point[n];
        Point[] casteljau;

        Point one = new Point();
        Point two = new Point();
        Point three = new Point();
        Point four = new Point();

        switch (n) {
            case 3:
                one = points[0];
                two = points[1];
                three = points[2];

                Point aa = new Point();
                Point ab = new Point();
                Point bb = new Point();

                //aa
                casteljau = deCasteljau(points, t1);
                aa = casteljau[casteljau.length - 1];

                //ab
                Point oneTwo = getCasteljauPointTwoT(one, two, t1, t1);
                Point twoThree = getCasteljauPointTwoT(two, three, t1, t1);

                ab = getCasteljauPointTwoT(oneTwo, twoThree, t2, t2);

                //bb
                casteljau = deCasteljau(points, t2);
                bb = casteljau[casteljau.length - 1];


                curveControl[0] = aa;
                curveControl[1] = ab;
                curveControl[2] = bb;
                break;
            case 4:
                one = points[0];
                two = points[1];
                three = points[2];
                four = points[3];

                Point aaa;
                Point aab;
                Point abb;
                Point bbb;

                // Point aaa
                casteljau = deCasteljau(points, t1);
                aaa = casteljau[casteljau.length - 1];

                //Point aab
                Point tmpOneTwo = getCasteljauPointTwoT(one, two, t1, t1);
                Point tmpTwoThree = getCasteljauPointTwoT(two, three, t1, t1);
                Point tmpThreeFour = getCasteljauPointTwoT(three, four, t1, t1);

                tmpOneTwo = getCasteljauPointTwoT(tmpOneTwo, tmpTwoThree, t1, t1);
                tmpTwoThree = getCasteljauPointTwoT(tmpTwoThree, tmpThreeFour, t1, t1);

                aab = getCasteljauPointTwoT(tmpOneTwo, tmpTwoThree, t2, t2);

                //Point abb
                tmpOneTwo = getCasteljauPointTwoT(one, two, t1, t1);
                tmpTwoThree = getCasteljauPointTwoT(two, three, t1, t1);
                tmpThreeFour = getCasteljauPointTwoT(three, four, t1, t1);

                tmpOneTwo = getCasteljauPointTwoT(tmpOneTwo, tmpTwoThree, t2, t2);
                tmpTwoThree = getCasteljauPointTwoT(tmpTwoThree, tmpThreeFour, t2, t2);

                abb = getCasteljauPointTwoT(tmpOneTwo, tmpTwoThree, t2, t2);

                //Point bbb
                casteljau = deCasteljau(points, t2);
                bbb = casteljau[casteljau.length - 1];

                curveControl[0] = aaa;
                curveControl[1] = aab;
                curveControl[2] = abb;
                curveControl[3] = bbb;
                break;
        }
/*
        ArrayList<Point> ctrlPoints = new ArrayList();
        ArrayList<Point> pointer = new ArrayList<>();
        tempPoints.clear();
        for (int i = 0; i <= n; i++) {
            Point p = new Point(points[i].x, points[i].y, points[i].z, points[i].weigth);
            tempPoints.add(i, p);
        }

        if (tempPoints.size() > 0) {
            for (int k = 1; k <= n; k++) {
                log("-------------------------- k: " + k);

                for (int i = 0; i <= n - k; i++) {
                    log("i: " + i + " step ");
                    Point castelPoint;

                    castelPoint = getCasteljauPoint(tempPoints.get(i), tempPoints.get(i + 1), t1);
                    tempPoints.get(i).set(castelPoint);
                    pointer.add(castelPoint);


                }

            }
            ctrlPoints.add(tempPoints.get(0));

        }


        log("ctrl size: " + ctrlPoints.size());
        Point[] curveLine = new Point[ctrlPoints.size()];
        ctrlPoints.toArray(curveLine);
*/
        return curveControl;
    }


    /**
     * Prints Information
     */
    private static void log(Object aObject) {
        System.out.println("Casteljau" + " " + String.valueOf(aObject));
    }
}


