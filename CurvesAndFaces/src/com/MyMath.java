/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com;

import javax.vecmath.Point3f;

/**
 *
 * @author Merlen
 */
public class MyMath {
    
    public static Point3f[] copyPointArray(Point3f[] points){
        Point3f[] tmp = new Point3f[points.length + 1];
        System.arraycopy(points, 0, tmp, 0, points.length);
        
        return tmp;
    }
}
