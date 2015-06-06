/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.*;

import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author Merlen
 */
public class Scene implements GLEventListener {

    Point[] plaPts = new Point[0];
    private float pointSize = 10;
    int my = 0, mx = 0, mz = 0;
    int scaleFactor = 10;

    private float tempRotX;
    private float tempRotY;
    private float tempRotZ;
    private float tempTraX;
    private float tempTraY;
    private float tempTraZ;
    private float zoom;

    private static EventMediator listener;

    public static void main(String[] args) {
        GLProfile glp = GLProfile.getDefault();
        GLCapabilities caps = new GLCapabilities(glp);
        GLCanvas canvas = new GLCanvas(caps);

        Frame frame = new Frame("AWT Window Test");
        frame.setSize(500, 500);
        frame.add(canvas);
        frame.setVisible(true);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        canvas.addGLEventListener(new Scene());

        Animator animator = new Animator(canvas);
        animator.start();

        listener = new EventMediator(canvas);

        canvas.addKeyListener(listener);
        canvas.addMouseListener(listener);
        canvas.addMouseMotionListener(listener);

    }

    public void display(GLAutoDrawable drawable) {
        update();
        render(drawable);
    }

    public void init(GLAutoDrawable drawable) {
        readOBJ("src/input/UB1_1.obj");
        drawable.getGL().setSwapInterval(1);
    }

    public void dispose(GLAutoDrawable drawable) {
        // put your cleanup code here
    }

    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        // called when user resizes the window
    }

    private void update() {
    }

    private void render(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);
        gl.glPointSize(pointSize);

        gl.glRotatef(listener.tempRotX, 1, 0, 0);				//Rotation with angle TempRot on X-Axis
        gl.glRotatef(listener.tempRotY, 0, 1, 0);				//Rotation with angle TempRot on Y-Axis
        gl.glRotatef(listener.tempRotZ, 0, 0, 1);				//Rotation with angle TempRot on Z-Axis
        gl.glScalef(listener.zoom, listener.zoom, listener.zoom);					//Pseudo zoom with factor zoom
        gl.glTranslatef(listener.tempTraX, listener.tempTraY, listener.tempTraZ); //Movement on X-, Y-, Z-Axis

        axes(gl);

        gl.glBegin(GL.GL_POINTS);
        gl.glColor3f(1, 0, 0);

        for (Point point : listener.points) {
            gl.glVertex3f(point.x / scaleFactor, point.y / scaleFactor, point.z / scaleFactor);
        }
        gl.glEnd();
    }

    public final void readOBJ(String filename) {
        File f = new File("src/input/UB1_1.obj");
        ObjReader p = new ObjReader(f.getAbsolutePath());
        try {
            p.processLineByLine();
            setPoints(p.getPoints());
        } catch (IOException ex) {
            log(Scene.class.getName() + ex);
        }
    }

    public void setPoints(List<Point> points) {
        for (Point point : points) {
            plaPts = com.MyMath.copyPointArray(plaPts);
            plaPts[listener.count] = new Point(point.x, point.y, point.z);
            log(plaPts[listener.count]);
            listener.count++;
        }
        listener.points = plaPts;
    }

    /*Draws Axises in Model*/
    void axes(GL2 gl) {
        gl.glBegin(gl.GL_LINES);
        gl.glColor3f(1, 0, 0);
        gl.glVertex3f(-50, 0, 0);
        gl.glVertex3f(50, 0, 0);
        gl.glEnd();

        gl.glBegin(gl.GL_LINES);
        gl.glColor3f(0, 0, 1);
        gl.glVertex3f(0, -50, 0);
        gl.glVertex3f(0, 50, 0);
        gl.glEnd();

        gl.glBegin(gl.GL_LINES);
        gl.glColor3f(0, 1, 0);
        gl.glVertex3f(0, 0, -50);
        gl.glVertex3f(0, 0, 50);
        gl.glEnd();
    }

    private void log(Object aObject) {
        System.out.println(String.valueOf(aObject));
    }
}
