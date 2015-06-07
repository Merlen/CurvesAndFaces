/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Merlen
 */
public class Bernstein {


    /**
     * Calcs factor for given x
     */
    private static double factorial(int x) {
        double fact = 1;
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
    private static double binomial(int f, int n) {
        double com = factorial(n) / (factorial(f) * factorial(n - f));
        return com;
    }

    public static Point bernstein(Point[] ctrl, float t, int n) {
        Point N = new Point();
        for (int i = 0; i <= n; i++) {
            N.x += ctrl[i].x * binomial(i, n) * Math.pow(t, i) * Math.pow((1 - t), (n - i));
            N.y += ctrl[i].y * binomial(i, n) * Math.pow(t, i) * Math.pow((1 - t), (n - i));
            N.z += ctrl[i].z * binomial(i, n) * Math.pow(t, i) * Math.pow((1 - t), (n - i));
        }

        return N;
    }

    private static void log(Object aObject) {
        System.out.println(String.valueOf(aObject));
    }
}
