package help;

import struct.Point;

/**
 *
 * @author Merlen
 */
public class MyMath {
    
    public static Point[] copyPointArray(Point[] arr) {
        Point[] tmp = new Point[arr.length + 1];
        
        System.arraycopy(arr, 0, tmp, 0, arr.length);
     
        return tmp;
    }

    public static Point[] removeElt(Point[] arr, int element) {

        Point[] tmp = new Point[arr.length - 1];

        System.arraycopy(arr, 0, tmp, 0, element);
        System.arraycopy(arr, element + 1, tmp, element, arr.length - element - 1);

        return tmp;
    }

    private double bernstein(int n, int k, float t) {
        return choose(n, k) * Math.pow(1 - t, n - k) * Math.pow(t, k);
    }

    private long choose(int n, int k) {
        return fact(n) / (fact(k) * fact(n - k));
    }

    private long fact(int n) {
        if (n > 1) {
            return fact(n - 1) * n;
        }
        return 1;
    }

    private static void log(Object aObject) {
        System.out.println(String.valueOf(aObject));
    }
}
