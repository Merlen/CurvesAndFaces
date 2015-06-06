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

    public Point[] points;
    public int count = 0;
    public float tempRotX;
    public float tempRotY;
    public float tempRotZ;
    public float rotFactor = 1;

    public float tempTraX;
    public float tempTraY;
    public float tempTraZ;
    public float traFactor = 1;

    public float zoom = 1;
    public float zoomFactor = 0.1f;

    private Scanner user_input = new Scanner(System.in);
    private static GLCanvas canvas;

    public EventMediator(GLCanvas canvas) {
        this.canvas = canvas;
    }

    public void keyPressed(KeyEvent e) {

        if (e.getKeyCode() == KeyEvent.VK_UP) {

        }
        //redisplay();
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
            case 'z':
                tempRotZ += rotFactor;
                if (tempRotZ >= 360) {
                    tempRotZ = 0;
                }
                log(tempRotZ);
                break;
            case 'y':
                tempRotY += rotFactor;
                if (tempRotY >= 360) {
                    tempRotY = 0;
                }
                log(tempRotY);
                break;
            case 'x':
                tempRotX += rotFactor;
                if (tempRotX >= 360) {
                    tempRotX = 0;
                }
                log(tempRotX);
                break;
            case 'Z':
                tempRotZ -= rotFactor;
                if (tempRotZ < 0) {
                    tempRotZ = 359;
                }
                log(tempRotZ);
                break;
            case 'Y':
                tempRotY -= rotFactor;
                if (tempRotY < 0) {
                    tempRotY = 359;
                }
                log(tempRotY);
                break;
            case 'X':
                tempRotX -= rotFactor;
                if (tempRotX < 0) {
                    tempRotX = 359;
                }
                log(tempRotX);
                break;
            case '+':
                zoom += zoomFactor;
                log(zoomFactor);
                break;
            case '-':
                zoom -= zoomFactor;
                if (zoom <= 0) {
                    zoom = 0;
                }
                log(zoomFactor);
                break;
        }
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
        log(points[count]);
        count++;

    }

    private void editPoint() {
        log("Which Point do you want to edit ?");
        for (int i = 0; i < points.length; i++) {
            log(i + ": Point at" + points[i]);
        }
        int idx = user_input.nextInt();

        log(points[idx]);

        log("New Value for x: ");
        points[idx].x = user_input.nextFloat();
        log("New Value for y: ");
        points[idx].y = user_input.nextFloat();
        log("New Value for z: ");
        points[idx].z = user_input.nextFloat();

        log(points[idx]);

    }

    private void deletePoint() {
        for (int i = 0; i < points.length; i++) {
            log(i + ": Point at" + points[i]);
        }
        log("Which Point do you want to delete ?");
        int idx = user_input.nextInt();
        points = MyMath.removeElt(points, idx);
    }
}
