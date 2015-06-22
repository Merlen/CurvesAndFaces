package listener;

import com.jogamp.opengl.awt.GLCanvas;
import help.Constants;
import help.MyMath;
import struct.Point;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Scanner;

/**
 * @author Merlen
 */
public class EventMediator implements KeyListener {

    private final Scanner user_input = new Scanner(System.in);
    private GLCanvas canvas;
    private float tmpTraX;
    private float tmpTraY;
    private float tmpTraZ;
    private float tmpRotX;
    private float tmpRotY;
    private float tmpRotZ;

    public EventMediator(GLCanvas canvas) {
        this.canvas = canvas;

        hint();
    }

    private void hint() {
        log("\nKEY - SET \n" +
                        "h = Show this Help Frame \n" +
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
                        "s = Blossoms \n" +
                        "S = set Blossom Values \n" +
                        "w = Edit Point Weigth By ID\t \n" +
                        "F1 = Increase Derivate\t \n" +
                        "F2 = Decreade Derivate\t \n" +
                        "F3 = Increase Points\t \n" +
                        "F10 = Switch Control Point Visibility (Only DeCasteljau)\n"
        );
    }

    @Override
    public void keyPressed(KeyEvent e) {

        int key = e.getKeyCode();  // Tells which key was pressed.
        switch (key) {
            case KeyEvent.VK_LEFT:
                Constants.t = Constants.t - Constants.STEPS;
                break;
            case KeyEvent.VK_RIGHT:
                Constants.t = Constants.t + Constants.STEPS;
                break;
            case KeyEvent.VK_DOWN:
                break;
            case KeyEvent.VK_UP:
                break;
            case KeyEvent.VK_ALT:
                Constants.traX = tmpTraX;
                Constants.traY = tmpTraY;
                Constants.traZ = tmpTraZ;

                Constants.rotX = tmpRotX;
                Constants.rotY = tmpRotY;
                Constants.rotZ = tmpRotZ;

                tmpTraX = tmpTraY = tmpTraZ = tmpRotX = tmpRotY = tmpRotZ = 0;
                break;
            case KeyEvent.VK_F1:
                increaseDerivate();
                break;
            case KeyEvent.VK_F2:
                decreaseDerivate();
                break;
            case KeyEvent.VK_F3:
                increasePoints();
                break;
            case KeyEvent.VK_F10:
                Constants.showControl = !Constants.showControl;
                break;
        }
        canvas.repaint();
    }

    private void increasePoints() {
        Constants.incPoints = true;
    }


    @Override
    public void keyReleased(KeyEvent e) {
    }

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
                Constants.rotZ = Constants.rotZ + Constants.rotFactor;
                tmpRotZ -= Constants.rotFactor;
                break;
            case 'y':
                Constants.rotY = Constants.rotY + Constants.rotFactor;
                tmpRotY -= Constants.rotFactor;
                break;
            case 'x':
                Constants.rotX = Constants.rotX + Constants.rotFactor;
                tmpRotX -= Constants.rotFactor;
                break;
            case 'Z':
                Constants.rotZ = Constants.rotZ - Constants.rotFactor;
                tmpRotZ += Constants.rotFactor;
                break;
            case 'Y':
                Constants.rotY = Constants.rotY - Constants.rotFactor;
                tmpRotY += Constants.rotFactor;
                break;
            case 'X':
                Constants.rotX = Constants.rotX - Constants.rotFactor;
                tmpRotX += Constants.rotFactor;
                break;
            case '-':
                Constants.zoom = Constants.zoom + Constants.zoomFactor;
                break;
            case '+':
                if (Constants.zoom > 0) Constants.zoom = Constants.zoom - Constants.zoomFactor;
                break;
            case 'c':
                Constants.castel = true;
                if (Constants.blossom) Constants.blossom = false;
                break;
            case 'b':
                Constants.castel = false;
                if (Constants.blossom) Constants.blossom = false;
                break;
            case '4':
                Constants.traX = Constants.traX - Constants.traFactor;
                break;
            case '6':
                Constants.traX = Constants.traX + Constants.traFactor;
                break;
            case '8':
                Constants.traY = Constants.traY + Constants.traFactor;
                break;
            case '2':
                Constants.traY = Constants.traY - Constants.traFactor;
                break;
            case 'w':
                editPointWeight();
                break;
            case 'S':
                blossoming();
                break;
            case 's':
                Constants.blossom = !Constants.blossom;
                break;
            case 'h':
                hint();
                break;

        }
        canvas.repaint();
    }

    private void blossoming() {

        log("Value for t1: ");
        float t1 = user_input.nextFloat();
        log("Value for t2: ");
        float t2 = user_input.nextFloat();

        while (t1 >= t2) {
            log("t2 must be higher than t1: " + t1 + " > " + t2);
            log("Value for t1: ");
            t1 = user_input.nextFloat();
            log("Value for t2: ");
            t2 = user_input.nextFloat();
        }

        Constants.firstT = t1;
        Constants.secondT = t2;
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

        Constants.points = MyMath.copyPointArray(Constants.points);
        Constants.points[Constants.count] = new Point(x, y, z);
        Constants.count = Constants.count + 1;

    }

    private void editPoint() {
        log("Which Point do you want to edit ?");
        for (int i = 0; i < Constants.points.length; i++) {
            log(i + ": Point at" + Constants.points[i]);
        }
        int idx = user_input.nextInt();

        log("New Value for x: ");
        Constants.points[idx].x = user_input.nextFloat();
        log("New Value for y: ");
        Constants.points[idx].y = user_input.nextFloat();
        log("New Value for z: ");
        Constants.points[idx].z = user_input.nextFloat();
    }

    private void editPointWeight() {
        log("Which Point do you want to edit ?");
        for (int i = 0; i < Constants.points.length; i++) {
            log(i + ": Point at" + Constants.points[i]);
        }
        int idx = user_input.nextInt();

        log("New Value for Weight: ");
        Constants.points[idx].weigth = user_input.nextFloat();
    }

    private void deletePoint() {
        for (int i = 0; i < Constants.points.length; i++) {
            log(i + ": Point at" + Constants.points[i]);
        }

        log("Which Point do you want to delete ?");
        int idx = user_input.nextInt();
        Constants.points = MyMath.removeElt(Constants.points, idx);
        Constants.count = Constants.count - 1;
    }

    private void increaseDerivate() {
        Constants.derivate = Constants.derivate + 1;
    }

    private void decreaseDerivate() {
        Constants.derivate = Constants.derivate - 1;
    }

}
