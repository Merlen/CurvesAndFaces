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
    public static MyColor YELLOW = new MyColor(255,255,0);
    public static MyColor RED = new MyColor(255,0,0);
    public static MyColor ORANGERED = new MyColor(240,69,0);
    

    private MyColor(int r, int g, int b) {
        this.r = r / 255;
        this.g = g / 255;
        this.b = b / 255;
    }
}
