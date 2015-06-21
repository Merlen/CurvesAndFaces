package help;

import struct.Point;

/**
 * @author Merlen
 */
public class MyMath {

    public static Point[] copyPointArray(Point[] arr) {
        Point[] tmp = new Point[arr.length + 1];

        System.arraycopy(arr, 0, tmp, 0, arr.length);

        return tmp;
    }


    /**
     * Calcs factor for given x
     */
    public static float factorial(int x) {
        float fact = 1;
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

    /**
     * Calc Binom
     */
    public static float binomial(int f, int n) {
        return factorial(n) / (factorial(f) * factorial(n - f));
    }


    public static Point[] removeElt(Point[] arr, int element) {

        Point[] tmp = new Point[arr.length - 1];

        System.arraycopy(arr, 0, tmp, 0, element);
        System.arraycopy(arr, element + 1, tmp, element, arr.length - element - 1);

        return tmp;
    }


    private void log(Object aObject) {
        System.out.println(this.getClass() + " " + String.valueOf(aObject));
    }
}
