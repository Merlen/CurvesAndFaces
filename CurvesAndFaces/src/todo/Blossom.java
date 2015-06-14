package todo;

import struct.Point;

/**
 * To get the curve: Set t1 = t2 = t3 = t:
 * b0 = b[0,0,0]
 * b1 = b[0,0,1] b[0,0,t]
 * b2 = b[0,1,1] b[0,t,1] b[0,t,t]
 * b3 = b[1,1,1] b[t,1,1] b[t,t,1] b[t,t,t]
 * <r>: t r-times as argument
 * • Bézier control points in blossom notation:
 * 1. Bézier Curves 39 [ ] [ ] [ ] [ ]
 *
 * @author Merlen
 */
public class Blossom {

    public static Point[] Blossom(Point[] pointList) {
        Point[] blossoms = new Point[0];

        for (Point p : pointList) {

        }

        return blossoms;
    }

}
