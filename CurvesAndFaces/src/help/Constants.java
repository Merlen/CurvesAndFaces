package help;

import struct.Point;

public class Constants {
    public static final float rotFactor = 1;
    public static final float traFactor = 0.01f;
    public static final int zoomFactor = 1;
    public static Point[] points;
    public static int count = 0;
    public static float traX = 0;
    public static float traY = 0;
    public static float traZ = 0;
    public static float rotX = 0;
    public static float rotY = 0;
    public static float rotZ = 0;
    public static int zoom = 10;
    public static int derivate = 0;
    public static float t = 0.0f;
    public static float STEPS = 0.1f;
    public static boolean castel = true;
    public static boolean blossom = false;
    public static boolean incPoints = false;
    public static boolean showControl = true;

    public static float firstT = 0f;
    public static float secondT = 1f;


    public static int u;
    public static int v;
    public static boolean surface = false;
    public static Point[][] surfaceCtrl;
    public static float T_STEP = 0.01f;

    public static int U_INCREASE = -1;
    public static int V_INCREASE = 1;
    public static int INCREASE_DIRECTION = 0;
}