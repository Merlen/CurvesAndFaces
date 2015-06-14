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
