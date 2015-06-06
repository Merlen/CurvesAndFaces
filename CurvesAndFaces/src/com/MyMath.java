/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com;

/**
 *
 * @author Merlen
 */
public class MyMath {

    public static Point[] copyPointArray(Point[] points) {
        Point[] tmp = new Point[points.length + 1];
        System.arraycopy(points, 0, tmp, 0, points.length);

        return tmp;
    }

    public static Point[] removeElt(Point[] arr, int remIndex) {
        int numElts = arr.length - (remIndex + 1);
        Point[] tmp = new Point[numElts];
        
        System.arraycopy(arr, remIndex + 1, tmp, remIndex, numElts);
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
