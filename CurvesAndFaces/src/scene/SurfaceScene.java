package scene;

import bezier.BezierMath;
import bezier.curves.Casteljau;
import bezier.curves.Derivate;
import com.ObjReader;
import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import help.Constants;
import help.MyColor;
import listener.EventMediator;
import struct.Point;
import struct.Vector;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Merlen
 */
public class SurfaceScene implements GLEventListener {


    private static EventMediator listener;
    private static GLCanvas canvas;
    Point[] plaPts = new Point[0];
    int scaleFactor = 10;
    private float pointSize = 10;

    public static void SurfaceScene() {
        GLProfile glp = GLProfile.getDefault();
        GLCapabilities caps = new GLCapabilities(glp);
        canvas = new GLCanvas(caps);

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

        canvas.addGLEventListener(new SurfaceScene());

        listener = new EventMediator(canvas);

        canvas.addKeyListener(listener);
    }


    private void render(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);
        gl.glPointSize(pointSize);

        gl.glRotatef(Constants.rotX, 1, 0, 0);                //Rotation with angle TempRot on X-Axis
        gl.glRotatef(Constants.rotY, 0, 1, 0);                //Rotation with angle TempRot on Y-Axis
        gl.glRotatef(Constants.rotZ, 0, 0, 1);                //Rotation with angle TempRot on Z-Axis
        gl.glTranslatef(Constants.traX, Constants.traY, Constants.traZ); //Movement on X-, Y-, Z-Axis

        scaleFactor = Constants.zoom; // zoom
        //axes(gl);

        if (Constants.showControl) {
            drawControl(gl);
        }

        if (Constants.incPoints) {
            if (Constants.INCREASE_DIRECTION == Constants.U_INCREASE) incPointsOnU();
            if (Constants.INCREASE_DIRECTION == Constants.V_INCREASE) incPointsOnV();
            Constants.INCREASE_DIRECTION = 0;
            canvas.repaint();
        }

        Surface(gl);

        resetTransform();
    }


    private void incPointsOnU() {
        int newU = Constants.u + 1;
        Point[][] tempCopy = Constants.surfaceCtrl;
        Constants.surfaceCtrl = new Point[Constants.v + 1][newU + 1];

        for (int v = 0; v <= Constants.v; v++) {
            Point[] newPoints;
            Point[] PointsOnU = new Point[Constants.u + 1];

            for (int u = 0; u <= Constants.u; u++) {
                PointsOnU[u] = tempCopy[v][u]; // Punkt v,u
                Constants.surfaceCtrl[v][u] = tempCopy[v][u];
            }


            newPoints = BezierMath.DegreeInc(PointsOnU);
            log(newPoints);

            System.arraycopy(newPoints, 0, Constants.surfaceCtrl[v], 0, newU + 1);
        }
        Constants.u += 1;
    }

    private void incPointsOnV() {
        int newV = Constants.v + 1;
        Point[][] tempCopy = Constants.surfaceCtrl;

        Constants.surfaceCtrl = new Point[newV + 1][Constants.u + 1];

        for (int u = 0; u <= Constants.u; u++) {
            Point[] newPoints;
            Point[] PointsOnV = new Point[Constants.v + 1];

            for (int v = 0; v <= Constants.v; v++) {
                PointsOnV[v] = tempCopy[v][u]; // Punkt v,u
                Constants.surfaceCtrl[v][u] = tempCopy[v][u];
            }


            newPoints = BezierMath.DegreeInc(PointsOnV);
            log(newPoints);


            for (int v = 0; v <= newV; v++) {
                Constants.surfaceCtrl[v][u] = newPoints[v];
            }
        }
        Constants.v += 1;
    }

    private void drawControl(GL2 gl) {

        for (int v = 0; v <= Constants.v; v++) {
            for (int u = 0; u < Constants.u; u++) {
                drawPoint(gl, Constants.surfaceCtrl[v][u], MyColor.AQUA);
                drawPoint(gl, Constants.surfaceCtrl[v][u + 1], MyColor.AQUA);

                drawLine(gl, Constants.surfaceCtrl[v][u], Constants.surfaceCtrl[v][u + 1], MyColor.WHITE);
            }
        }

        for (int v = 0; v < Constants.v; v++) {
            for (int u = 0; u <= Constants.u; u++) {
                drawLine(gl, Constants.surfaceCtrl[v][u], Constants.surfaceCtrl[v + 1][u], MyColor.RED);
            }
        }
        drawPoint(gl, Constants.surfaceCtrl[0][0], MyColor.RED); //unten links
        drawPoint(gl, Constants.surfaceCtrl[0][Constants.u], MyColor.RED); //unten Rechts

        drawPoint(gl, Constants.surfaceCtrl[Constants.v][Constants.u], MyColor.BLUE); //oben Rechts
        drawPoint(gl, Constants.surfaceCtrl[Constants.v][0], MyColor.BLUE); //oben Links
    }

    private void Surface(GL2 gl) {
        log("Surface Building");

        drawCurvesUDir(gl);
        drawCurvesVDir(gl);

        if (Constants.derivate > 0) {
            Vector v = calcDerivateVDir(gl);
            Vector u = calcDerivateUDir(gl);

            Vector n = new Vector();

            n.x = (v.y * u.z) - (u.y * v.z);
            n.y = -((v.x * u.z) - (u.x * v.z));
            n.z = (v.x * u.y) - (u.x * v.y);

            float L = (float) Math.sqrt(Math.pow(n.x, 2) + Math.pow(n.y, 2) + Math.pow(n.x, 2));
            if (L > 0) {
                L = 1.0f / L;
                n.x = n.x / L;
                n.y = n.y / L;
                n.z = n.z / L;
            }

            Point p = new Point(n.x, n.y, n.z);
            drawPoint(gl, p, MyColor.RED);
        }

    }

    private void drawCurvesUDir(GL2 gl) {
        for (float t = 0; t <= 1.0 + Constants.T_STEP; t += Constants.T_STEP) {
            Point[] curveControl = new Point[Constants.v + 1];

            for (int v = 0; v <= Constants.v; v++) {
                Point[] uList = new Point[Constants.u + 1];

                for (int u = 0; u <= Constants.u; u++) {
                    uList[u] = Constants.surfaceCtrl[v][u];
                }

                Point[] uCasteljau = Casteljau.deCasteljau(uList, t);
                Point tOnCurve = uCasteljau[uCasteljau.length - 1]; //t Point on U

                curveControl[v] = tOnCurve;
            }

            Point[] castelCurve = Casteljau.deCasteljauCurve(curveControl, 0f, 1f); //curve
            log(castelCurve[castelCurve.length - 1]);
            drawCurve(gl, castelCurve, MyColor.AQUA);
        }
    }

    private void drawCurvesVDir(GL2 gl) {
        for (float t = 0; t <= 1.0 + Constants.T_STEP; t += Constants.T_STEP) {
            Point[] curveControl = new Point[Constants.u + 1];

            for (int u = 0; u <= Constants.u; u++) {
                Point[] vList = new Point[Constants.v + 1];

                for (int v = 0; v <= Constants.v; v++) {
                    vList[v] = Constants.surfaceCtrl[v][u];

                }
                Point[] uCasteljau = Casteljau.deCasteljau(vList, t);
                Point tOnCurve = uCasteljau[uCasteljau.length - 1]; //t Point on U

                curveControl[u] = tOnCurve;
            }

            Point[] castelCurve = Casteljau.deCasteljauCurve(curveControl, 0f, 1f); //curve
            log(castelCurve[castelCurve.length - 1]);
            drawCurve(gl, castelCurve, MyColor.RED);
        }
    }


    // TODO CALC TANGENT
    private Vector calcDerivateVDir(GL2 gl) {
        Point[] curveControl = new Point[Constants.u + 1];

        for (int u = 0; u <= Constants.u; u++) {
            Point[] vList = new Point[Constants.v + 1];

            for (int v = 0; v <= Constants.v; v++) {
                vList[v] = Constants.surfaceCtrl[v][u];

            }
            Point[] uCasteljau = Casteljau.deCasteljau(vList, Constants.t);
            Point tOnCurve = uCasteljau[uCasteljau.length - 1]; //t Point on U

            curveControl[u] = tOnCurve;
        }

        Vector pointT = Derivate.getBernsteinDerivate(0, Constants.t, curveControl);
        Vector v = Derivate.getBernsteinDerivate(Constants.derivate, Constants.t, curveControl);


        //Point point = new Point(pointT.x, pointT.y, pointT.z);
        //drawPoint(gl, point, MyColor.ORANGERED);

        //Point p2 = new Point(point.x + v.x, point.y + v.y, point.z + v.z);
        return v;//drawLine(gl, point, p2, MyColor.YELLOW);
    }

    //TODO CALC TANGENT
    private Vector calcDerivateUDir(GL2 gl) {
        Point[] curveControl = new Point[Constants.u + 1];

        for (int v = 0; v <= Constants.v; v++) {
            Point[] uList = new Point[Constants.v + 1];

            for (int u = 0; u <= Constants.u; u++) {
                uList[v] = Constants.surfaceCtrl[v][u];

            }
            Point[] uCasteljau = Casteljau.deCasteljau(uList, Constants.t);
            Point tOnCurve = uCasteljau[uCasteljau.length - 1]; //t Point on U

            curveControl[v] = tOnCurve;
        }

        //Vector pointT = Derivate.getBernsteinDerivate(0, Constants.t, curveControl);
        Vector v = Derivate.getBernsteinDerivate(Constants.derivate, Constants.t, curveControl);


        //Point point = new Point(pointT.x, pointT.y, pointT.z);
        //drawPoint(gl, point, MyColor.ORANGERED);

        //Point p2 = new Point(point.x + v.x, point.y + v.y, point.z + v.z);
        return v;//drawLine(gl, point, p2, MyColor.YELLOW);

    }


    private void resetTransform() {
        Constants.rotX = Constants.rotY = Constants.rotZ = Constants.traX = Constants.traY = Constants.traZ = 0;
        Constants.incPoints = false;
        if (Constants.derivate < 0) Constants.derivate = 0;
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
            log(CurveScene.class
                    .getName() + ex);
        }
    }

    /**
     * Set Points in Listener
     */
    public void setPoints(ArrayList<Point> points) {
        for (Point point : points) {
            plaPts = help.MyMath.copyPointArray(plaPts);
            plaPts[Constants.count] = new Point(point.x, point.y, point.z);
            Constants.count = Constants.count + 1;
        }
        Constants.points = plaPts;
        setPoints();
    }

    private void setPoints() {
        Constants.surfaceCtrl = new Point[Constants.v + 1][Constants.u + 1];

        for (int idx = 0; idx <= Constants.v; idx++) {
            for (int pos = 0; pos <= Constants.u; pos++) {
                int index = idx * (Constants.v + 1) + pos;
                Point p = Constants.points[index];
                Constants.surfaceCtrl[idx][pos] = p;

            }
        }

        for (int idx = 0; idx <= Constants.u; idx++) {
            for (int pos = 0; pos <= Constants.v; pos++) {

                int index = idx * (Constants.u + 1) + pos;
                Point p = Constants.points[index];
                Constants.surfaceCtrl[idx][pos] = p;
            }
        }

    }

    /**
     * Prints Information
     */
    private void log(Object aObject) {
        System.out.println(this.getClass() + " " + String.valueOf(aObject));
    }

    void drawDerivate(GL2 gl) {
        struct.Vector v = null;

        if (Constants.derivate > 0) {
            struct.Vector pointT = null;

            log("Derivate Vector: " + v);
            log("Attention: Derivate ignores Weights on Points");

            if (pointT != null) {
                Point p = new Point(pointT.x, pointT.y, pointT.z);
                drawPoint(gl, p, MyColor.ORANGERED);

                Point p2 = new Point(p.x + v.x, p.y + v.y, p.z + v.z);
                drawLine(gl, p, p2, MyColor.YELLOW);
            }
        }
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


    public void display(GLAutoDrawable drawable) {
        render(drawable);
    }

    public void init(GLAutoDrawable drawable) {
        readOBJ("src/input/beziersurf.obj");
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


}
