/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com;

import com.jogamp.opengl.awt.GLCanvas;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Scanner;

/**
 *
 * @author Merlen
 */
public class EventMediator implements KeyListener, MouseListener, MouseMotionListener {

    private float tmpZoom = 1;
    private final float rotFactor = 1;
    private final float traFactor = 1;
    private final float zoomFactor = 0.1f;
    private final Scanner user_input = new Scanner(System.in);
    private static GLCanvas canvas;
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
    public float zoom = 0;

    public float t = 0.5f;
    public float STEPS = 0.1f;
    public boolean castel = false;

    public EventMediator(GLCanvas canvas) {
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
                
                zoom = tmpZoom;
                tmpTraX = tmpTraY = tmpTraZ = tmpRotX = tmpRotY = tmpRotZ = 0;
                tmpZoom = 0;
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
            case '+':
                zoom += zoomFactor;
                tmpZoom -= zoomFactor;
                break;
            case '-':
                zoom -= zoomFactor;
                tmpZoom += zoomFactor;
                break;
            case 'c':
                castel = true;
                break;
            case 'b':
                castel = false;
                break;
        }
        canvas.repaint();
    }

    private void log(Object aObject) {
        System.out.println(String.valueOf(aObject));
    }

    private void addPoint() {
        log("Value for x: ");
        float x = user_input.nextFloat();
        log("Value for y: ");
        float y = user_input.nextFloat();
        log("Value for z: ");
        float z = user_input.nextFloat();

        points = com.MyMath.copyPointArray(points);
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

    private void deletePoint() {
        for (int i = 0; i < points.length; i++) {
            log(i + ": Point at" + points[i]);
        }

        log("Which Point do you want to delete ?");
        int idx = user_input.nextInt();
        points = MyMath.removeElt(points, idx);
        count--;
    }
    
        private void addPoint(float x, float y, float z) {
        points = com.MyMath.copyPointArray(points);
        points[count] = new Point(x, y, z);
        count++;

    }

}
