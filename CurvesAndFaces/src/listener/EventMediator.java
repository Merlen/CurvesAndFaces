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
public class EventMediator implements KeyListener, MouseListener, MouseMotionListener {

    private final float rotFactor = 1;
    private final float traFactor = 0.01f;
    private final int zoomFactor = 1;
    private final Scanner user_input = new Scanner(System.in);
    private GLCanvas canvas;
    private float tmpTraX;
    private float tmpTraY;
    private float tmpTraZ;
    private float tmpRotX;
    private float tmpRotY;
    private float tmpRotZ;

    public Point[] points;
    public int count = 0;
    public float traX = 0;
    public float traY = 0;
    public float traZ = 0;
    public float rotX = 0;
    public float rotY = 0;
    public float rotZ = 0;
    public int zoom = 10;

    public int derivate = 0;
    public float t = 0.5f;
    public float STEPS = 0.1f;
    public boolean castel = true;
    public boolean blossom = true;

    public EventMediator(GLCanvas canvas) {
        this.canvas = canvas;

        log("\nKEY - SET \n" +
                        "x = Rotate+ on X-Axis\tX = Rotate- on X-Axis \n" +
                        "y = Rotate+ on Y-Axis\tY = Rotate- on Y-Axis \n" +
                        "z = Rotate+ on Z-Axis\tZ = Rotate- on Z-Axis \n" +
                        "4 = Translate+ on X-Axis\t6 = Translate- on X-Axis \n" +
                        "8 = Translate+ on Y-Axis\t2 = Translate- on Y-Axis \n" +
                        "+ =\tZoom In\t- = Zoom out\n" +
                        "a = Add Point to Model \n" +
                        "e = Edit Point By ID\t \n" +
                        "d = Delete Point By ID \n" +
                        "c = deCasteljau \n" +
                        "b = bernstein \n" +
                        "B = Blossoms \n" +
                        "w = Edit Point Weigth By ID\t \n"+
                        "F1 = Increase Degree\t \n"+
                        "F2 = Decreade Degree\t \n"
        );
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
            case KeyEvent.VK_F1:
                increaseDegree();
                break;
            case KeyEvent.VK_F2 :
                decreaseDegree();
                break;
        }
        canvas.repaint();
    }

    // Redisplay must be called if no animator is used.
    // Hence when the display is not updated regularly,
    // but an update should be triggered on some event.
    private void redisplay() {
        canvas.display();
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
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
            case 'a':
                addPoint();
                break;
            case 'e':
                editPoint();
                break;
            case 'd':
                deletePoint();
                break;
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
            case '-':
                zoom += zoomFactor;
                break;
            case '+':
                if (zoom > 0) zoom -= zoomFactor;
                break;
            case 'c':
                castel = true;
                break;
            case 'b':
                castel = false;
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
            case 'w':
                editPointWeight();
                break;
        }
        canvas.repaint();
    }

    private void log(Object aObject) {
        System.out.println(this.getClass() + " " + String.valueOf(aObject));
    }

    private void addPoint() {
        log("Value for x: ");
        float x = user_input.nextFloat();
        log("Value for y: ");
        float y = user_input.nextFloat();
        log("Value for z: ");
        float z = user_input.nextFloat();

        points = help.MyMath.copyPointArray(points);
        points[count] = new Point(x, y, z);
        count++;

    }

    private void editPoint() {
        log("Which Point do you want to edit ?");
        for (int i = 0; i < points.length; i++) {
            log(i + ": Point at" + points[i]);
        }
        int idx = user_input.nextInt();

        log("New Value for x: ");
        points[idx].x = user_input.nextFloat();
        log("New Value for y: ");
        points[idx].y = user_input.nextFloat();
        log("New Value for z: ");
        points[idx].z = user_input.nextFloat();
    }

    private void editPointWeight() {
        log("Which Point do you want to edit ?");
        for (int i = 0; i < points.length; i++) {
            log(i + ": Point at" + points[i]);
        }
        int idx = user_input.nextInt();

        log("New Value for Weight: ");
        points[idx].weigth = user_input.nextFloat();
    }

    private void deletePoint() {
        for (int i = 0; i < points.length; i++) {
            log(i + ": Point at" + points[i]);
        }

        log("Which Point do you want to delete ?");
        int idx = user_input.nextInt();
        points = MyMath.removeElt(points, idx);
        count--;
    }

    private void increaseDegree(){
        derivate++;
    }

    private void decreaseDegree(){
        derivate--;
    }

}
