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
public class MyColor {

    float r;
    float g;
    float b;

    public static MyColor GREEN = new MyColor(0, 255, 0);
    public static MyColor WHITE = new MyColor(255, 255, 255);
    public static MyColor AQUA = new MyColor(0, 255, 255);
    public static MyColor BLUE = new MyColor(0, 0, 255);
    public static MyColor BLACK = new MyColor(0, 0, 0);
    public static MyColor YELLOW = new MyColor(255, 255, 0);
    public static MyColor RED = new MyColor(255, 0, 0);
    public static MyColor ORANGERED = new MyColor(240, 69, 0);

    private static int STEPS = 15;

    private MyColor(int r, int g, int b) {
        if(r != 0) this.r = 255 / r; else this.r = 0;
        if(g != 0) this.g = 255 / g; else this.g = 0;
        if(b != 0) this.b = 255 / b; else this.b = 0;
    }

    public static MyColor COLORSWITCH() {
        if (STEPS < 0) {
            STEPS = 255;
        }
        return new MyColor(0, 255 - STEPS, 0);
    }
}
