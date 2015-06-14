package listener;

import help.MyMath;
import struct.Point;
import com.jogamp.opengl.awt.GLCanvas;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Scanner;

/**
 * @author Merlen
 */
public class PolynomialMediator implements KeyListener{

    private final float rotFactor = 1;
    private final float traFactor = 0.01f;
    private final int zoomFactor = 1;
    private GLCanvas canvas;
    private float tmpTraX;
    private float tmpTraY;
    private float tmpTraZ;
    private float tmpRotX;
    private float tmpRotY;
    private float tmpRotZ;

    public float traX = 0;
    public float traY = 0;
    public float traZ = 0;
    public float rotX = 0;
    public float rotY = 0;
    public float rotZ = 0;
    public int zoom = 10;

    public float t = 0.5f;
    public float STEPS = 0.1f;
    public PolynomialMediator(GLCanvas canvas) {
        this.canvas = canvas;
    }

    @Override
    public void keyPressed(KeyEvent e) {

        int key = e.getKeyCode();  // Tells which key was pressed.
        switch (key) {

            case KeyEvent.VK_LEFT:
                t -= STEPS;
                break;
            case KeyEvent.VK_RIGHT:
                t += STEPS;
                break;
            case KeyEvent.VK_DOWN:
                break;
            case KeyEvent.VK_UP:
                break;
            case KeyEvent.VK_ALT:
                traX = tmpTraX;
                traY = tmpTraY;
                traZ = tmpTraZ;

                rotX = tmpRotX;
                rotY = tmpRotY;
                rotZ = tmpRotZ;

                tmpTraX = tmpTraY = tmpTraZ = tmpRotX = tmpRotY = tmpRotZ = 0;
                break;
        }
        canvas.repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }


    /*Function to use Keys in ANSI Format
     z = Rotate+ on Z-Axis	Z = Rotate- on Z-Axis
     x =	-"-     on X-Axis	X = -"-
     y =	-"-     on Y-Axis	Y = -"-
     + =	Zoom In                 - = Zoom out		
     a = Add Point to Model
     e = Edit Point By ID	d = Delete Point By ID
     */
    @Override
    public void keyTyped(KeyEvent e) {
        switch (e.getKeyChar()) {
            case 'z':
                rotZ += rotFactor;
                tmpRotZ -= rotFactor;
                break;
            case 'y':
                rotY += rotFactor;
                tmpRotY -= rotFactor;
                break;
            case 'x':
                rotX += rotFactor;
                tmpRotX -= rotFactor;
                break;
            case 'Z':
                rotZ -= rotFactor;
                tmpRotZ += rotFactor;
                break;
            case 'Y':
                rotY -= rotFactor;
                tmpRotY += rotFactor;
                break;
            case 'X':
                rotX -= rotFactor;
                tmpRotX += rotFactor;
                break;
            case '+':
                zoom += zoomFactor;
                break;
            case '-':
                if(zoom > 0) zoom -= zoomFactor;
                break;
            case '4':
                traX -= traFactor;
                break;
            case '6':
                traX += traFactor;
                break;
            case '8':
                traY += traFactor;
                break;
            case '2':
                traY -= traFactor;
                break;
        }
        canvas.repaint();
    }

    private void log(Object aObject) {
        System.out.println(this.getClass() + " " + String.valueOf(aObject));
    }
}
