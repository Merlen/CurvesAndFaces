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
public class MyBezier {

    public static Point ab;
    public static Point bc;
    public static Point cd;

    public static Point abbc;
    public static Point bccd;

    //returns point on bezier Curve
    public static Point bezier(Point a, Point b, Point c, Point d, float t) {
        log("Calculate Bezier with t: " + t);

        ab = bezierBetweenPoints(a, b, t);
        bc = bezierBetweenPoints(b, c, t);
        cd = bezierBetweenPoints(c, d, t);

        abbc = bezierBetweenPoints(ab, bc, t);
        bccd = bezierBetweenPoints(bc, cd, t);

        Point bezier = bezierBetweenPoints(abbc, bccd, t);

        log("Bezier Points: " + bezier.x + "," + bezier.y);
        return bezier;
    }

    public static Point bezierBetweenPoints(Point a, Point b, float t) {
        Point ler = new Point();
        ler.x = a.x + (b.x - a.x) * t;
        ler.y = a.y + (b.y - a.y) * t;

        log("Point between Points x: " + ler.x + " y: " + ler.y);
        return ler;
    }

    private static void log(Object aObject) {
        System.out.println(String.valueOf(aObject));
    }
}
