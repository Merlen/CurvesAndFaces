package bezier;

import struct.Point;

public class BezierMath {

    public static Point[] DegreeInc(Point[] oldPoints) {
        int n = oldPoints.length;
        Point[] newPoints = new Point[n + 1];

        for (int i = 0; i <= n; i++) {
            if (i == 0) {
                newPoints[i] = oldPoints[i];
            } else {
                float calc1 = (((float) i) / (float)n);
                float calc2 = (1 - calc1);
                log("i: " + i + " n: " + n + " " + calc2 + " " + calc1);

                if (i == n) newPoints[(i)] = oldPoints[(i - 1)];
                else
                    newPoints[i] = oldPoints[(i)].times(calc2).plus(oldPoints[(i - 1)].times(calc1));
            }
        }
        return newPoints;
    }


    private static void log(Object aObject) {
        System.out.println("BezierMath" + " " + String.valueOf(aObject));
    }

}
