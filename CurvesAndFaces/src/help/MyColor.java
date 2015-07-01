package help;

import java.util.ArrayList;

/**
 *
 * @author Merlen
 */
public class MyColor {

    public static  ArrayList<MyColor> colors = new ArrayList();
    public static MyColor GREEN = new MyColor(0, 255, 0);
    public static MyColor WHITE = new MyColor(255, 255, 255);
    public static MyColor AQUA = new MyColor(0, 255, 255);
    public static MyColor BLUE = new MyColor(0, 0, 255);
    public static MyColor YELLOW = new MyColor(255, 255, 0);
    public static MyColor RED = new MyColor(255, 0, 0);
    public static MyColor ORANGERED = new MyColor(240, 69, 0);
    public static MyColor BLACK = new MyColor(0, 0, 0);
    public float r;
    public float g;
    public float b;

    private MyColor(int r, int g, int b) {
        if (r != 0) {
            this.r = 255 / (float) r;
        } else {
            this.r = 0f;
        }
        if (g != 0) {
            this.g = 255 / (float) g;
        } else {
            this.g = 0f;
        }
        if (b != 0) {
            this.b = 255 / (float) b;
        } else {
            this.b = 0f;
        }
        colors.add(this);
    }
}
