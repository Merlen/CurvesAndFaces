package com;

import bezier.Bernstein;
import bezier.Casteljau;
import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import help.MyColor;
import help.MyMath;
import listener.EventMediator;
import struct.Point;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author Merlen
 */
public class Scene implements GLEventListener {

    Point[] plaPts = new Point[0];
    private float pointSize = 10;
    int scaleFactor = 10;
    private PolynomialScene polynomialScene;

    private static EventMediator listener;

    public static void main(String[] args) {
        GLProfile glp = GLProfile.getDefault();
        GLCapabilities caps = new GLCapabilities(glp);
        GLCanvas canvas = new GLCanvas(caps);

        Frame frame = new Frame("Curve Frame");
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

        listener = new EventMediator(canvas);

        canvas.addKeyListener(listener);
        canvas.addMouseListener(listener);
        canvas.addMouseMotionListener(listener);

    }

    public void display(GLAutoDrawable drawable) {
        render(drawable);
    }

    public void init(GLAutoDrawable drawable) {
        readOBJ("src/input/UB1_1.obj");
        drawable.getGL().setSwapInterval(1);

    }

    public void dispose(GLAutoDrawable drawable) {
        // put your cleanup code here
    }

    public void reshape(GLAutoDrawable gldrawable, int x, int y, int width, int height) {
        final GL2 gl = gldrawable.getGL().getGL2();
        if (height <= 0) {
            height = 1;
        }
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
    }


    private void render(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);
        gl.glPointSize(pointSize);

        gl.glRotatef(listener.rotX, 1, 0, 0);                //Rotation with angle TempRot on X-Axis
        gl.glRotatef(listener.rotY, 0, 1, 0);                //Rotation with angle TempRot on Y-Axis
        gl.glRotatef(listener.rotZ, 0, 0, 1);                //Rotation with angle TempRot on Z-Axis
        gl.glTranslatef(listener.traX, listener.traY, listener.traZ); //Movement on X-, Y-, Z-Axis

        scaleFactor =  listener.zoom; // zoom
        axes(gl);
        drawPointsAndLine(gl, listener.points, false);
        Point[] bez;

        if (listener.castel) {
            bez = Casteljau.deCasteljau(listener.points, listener.t); // control Points
            drawPointsAndLine(gl, bez, true);

            Point[] castelCurve = Casteljau.deCasteljauCurve(listener.points, -1f, 2f); //curve
            drawCurve(gl, castelCurve, MyColor.AQUA);
        } else {

            Point M;
            Point P = listener.points[0];
            for (float t = 0; t <= 1.0; t += 0.01) {
                M = Bernstein.bernsteinCurve(listener.points, t, listener.count - 1); //curve
                drawLine(gl, P, M, MyColor.AQUA);
                P = M;
            }

           if(polynomialScene == null){
               polynomialScene =  new PolynomialScene(listener.points, listener.t);

           }else {
               polynomialScene.setPoints(listener.points, listener.t);
           }
        }

        resetTransform();
    }

    private void resetTransform() {
        if (polynomialScene != null && polynomialScene.closed){
            polynomialScene = null;
        }

        listener.rotX = listener.rotY = listener.rotZ = listener.traX = listener.traY = listener.traZ = 0;
    }

    /**
     * Init ObjReader
     */
    public final void readOBJ(String filename) {
        File f = new File(filename);
        ObjReader p = new ObjReader(f.getAbsolutePath());
        try {
            p.processLineByLine();
            setPoints(p.getPoints());

        } catch (IOException ex) {
            log(Scene.class
                    .getName() + ex);
        }
    }

    /**
     * Set Points in Listener
     */
    public void setPoints(List<Point> points) {
        for (Point point : points) {
            plaPts = help.MyMath.copyPointArray(plaPts);
            plaPts[listener.count] = new Point(point.x, point.y, point.z);
            listener.count++;
        }
        listener.points = plaPts;
    }

    /**
     * Draws Axises in Model
     */
    void axes(GL2 gl) {
        gl.glBegin(GL.GL_LINES);
        gl.glColor3f(1, 0, 0);
        gl.glVertex3f(-50, 0, 0);
        gl.glVertex3f(50, 0, 0);
        gl.glEnd();

        gl.glBegin(GL.GL_LINES);
        gl.glColor3f(0, 0, 1);
        gl.glVertex3f(0, -50, 0);
        gl.glVertex3f(0, 50, 0);
        gl.glEnd();

        gl.glBegin(GL.GL_LINES);
        gl.glColor3f(0, 1, 0);
        gl.glVertex3f(0, 0, -50);
        gl.glVertex3f(0, 0, 50);
        gl.glEnd();
    }

    /**
     * Prints Information
     */
    private void log(Object aObject) {
        System.out.println(this.getClass() + " " + String.valueOf(aObject));
    }

    /**
     * Draw Point
     */
    void drawPoint(GL2 gl, Point p, MyColor color) {//draw control points
        gl.glColor3f(color.r, color.g, color.b);
        gl.glBegin(GL.GL_POINTS);
        gl.glVertex3f(p.x / scaleFactor, p.y / scaleFactor, p.z / scaleFactor);
        gl.glEnd();
        gl.glFlush();
    }

    /**
     * DrawLine Between two Points
     */
    void drawLine(GL2 gl, Point p1, Point p2, MyColor color) {//draw the line between control points
        if (gl != null) {
            gl.glColor3f(color.r, color.g, color.b);
            gl.glLineWidth(2.0f);
            gl.glBegin(GL.GL_LINES);
            gl.glVertex3f(p1.x / scaleFactor, p1.y / scaleFactor, p1.z / scaleFactor);
            gl.glVertex3f(p2.x / scaleFactor, p2.y / scaleFactor, p2.z / scaleFactor);
            gl.glEnd();
            gl.glFlush();
        }
    }

    /**
     * Draw Curve with PointList
     */
    void drawCurve(GL2 gl, Point[] points, MyColor color) {
        gl.glColor3d(color.r, color.g, color.b);
        gl.glBegin(GL.GL_LINE_STRIP);
        for (Point p : points) {
            gl.glVertex3f(p.x / scaleFactor, p.y / scaleFactor, p.z / scaleFactor);
        }
        gl.glEnd();
    }

    /**
     * Draw Points and Lines between them
     */
    void drawPointsAndLine(GL2 gl, Point[] list, boolean bez) {
        if (!bez) {
            for (int i = 0; i < (list.length - 1); i++) {
                drawLine(gl, list[i], list[i + 1], MyColor.WHITE);
                drawPoint(gl, list[i], MyColor.BLUE);
                drawPoint(gl, list[i + 1], MyColor.BLUE);
            }
        } else {
            drawControlPoints(gl, list, listener.count);
        }

    }

    /**
     * Draw ControlPoints with Lines
     */
    void drawControlPoints(GL2 gl, Point[] list, int length) {
        Point[] tmp = list;
        int counter = 1;
        if (length > 2) {
            while (counter < (length - 1)) {
                drawLine(gl, tmp[0], tmp[1], MyColor.COLORSWITCH());
                drawPoint(gl, tmp[0], MyColor.RED);
                drawPoint(gl, tmp[1], MyColor.RED);
                tmp = MyMath.removeElt(tmp, 0);
                counter++;
            }
            tmp = MyMath.removeElt(tmp, 0);
            drawControlPoints(gl, tmp, length - 1);
        } else {
            log("Target Point: " + tmp[0]);
            drawPoint(gl, tmp[0], MyColor.AQUA);
        }
    }
}
