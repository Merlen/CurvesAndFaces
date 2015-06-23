package scene;

import bezier.curves.Bernstein;
import com.ObjReader;
import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import help.Constants;
import help.MyColor;
import help.MyMath;
import listener.EventMediator;
import struct.Point;

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


    Point[] plaPts = new Point[0];

    int scaleFactor = 10;
    private float pointSize = 10;

    public static void SurfaceScene() {
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

        canvas.addGLEventListener(new SurfaceScene());

        listener = new EventMediator(canvas);

        canvas.addKeyListener(listener);
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

        //TODO
        if (Constants.showControl) drawPointsAndLine(gl, Constants.points);


        if (Constants.incPoints) {
            //TODO

            for (int i = 0; i < (Constants.v + 1); i++) {
                Point[] vPoints = getVDirPoints(Constants.points, i); //Points on VDirection

                //Constants.points = BezierMath.DegreeInc(vPoints);
                //Constants.v = Constants.v + 1;
            }

        }

        Bernstein(gl);


        resetTransform();
    }


    private Point[] getUDirPoints(Point[] list, int pos) {
        ArrayList<Point> pointArrayList = new ArrayList<>();
        Point temp;

        for (int v = 0; v < (Constants.v + 1); v++) { // u - Dir

            //for (int u = 0; u < (Constants.u + 1); u++) {
            int index = v * (Constants.v + 1) + pos;
            Point p = Constants.points[index];
            pointArrayList.add(p);
            //}


        }
        Point[] uLine = new Point[pointArrayList.size()];
        pointArrayList.toArray(uLine);

        return uLine;
    }

    private Point[] getVDirPoints(Point[] list, int pos) {
        ArrayList<Point> pointArrayList = new ArrayList<>();

        for (int u = 0; u < (Constants.u + 1); u++) {  // v - Dir
            int index = pos * (Constants.v + 1) + u;
            Point p = Constants.points[index];
            pointArrayList.add(p);
        }

        Point[] uLine = new Point[pointArrayList.size()];
        pointArrayList.toArray(uLine);

        return uLine;
    }

    private void Bernstein(GL2 gl) {
        log("Bernstein Building");
        Point[] M = new Point[Constants.u + 1];
        Point P;
        Point tan;

        for (int i = 0; i < (Constants.u + 1); i++) {
            Point[] pointList = getVDirPoints(Constants.points, i); //oben nach unten
            M[i] = Bernstein.bernsteinCurve(pointList, Constants.t, pointList.length - 1); //T Points on U Control

            drawPoint(gl, M[i], MyColor.RED);
        }

        for (int i = 0; i < (Constants.v + 1); i++) {
            Point[] pointList = getUDirPoints(M, i); //links nach rechts
            P = M[0];
            for (float t = 0; t < 1.0; t += 0.01) {
                tan = Bernstein.bernsteinCurve(M, t, pointList.length - 1); //T Points on M Control
                drawLine(gl, P, tan, MyColor.GREEN);

                P = tan;
            }

        }

        for (int i = 0; i < (Constants.v + 1); i++) {
            Point[] pointList = getUDirPoints(Constants.points, i); //links nach rechts
            M[i] = Bernstein.bernsteinCurve(pointList, Constants.t, pointList.length - 1); //T Points on V Control
            drawPoint(gl, M[i], MyColor.BLUE);
        }

        for (int i = 0; i < (Constants.u + 1); i++) {
            Point[] pointList = getVDirPoints(M, i); //oben nach unten
            P = M[0];
            for (float t = 0; t < 1.0; t += 0.01) {
                tan = Bernstein.bernsteinCurve(M, t, pointList.length - 1); //T Points on M Control
                drawLine(gl, P, tan, MyColor.AQUA);


                P = tan;
            }
        }

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
            log(Scene.class
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

    void drawDerivate(GL2 gl) {
        struct.Vector v = null;

        if (Constants.derivate > 0) {
            struct.Vector pointT = null;


            //pointT = Derivate.getBernsteinDerivate(0, Constants.t, Constants.pointMulti);
            //v = Derivate.getBernsteinDerivate(Constants.derivate, Constants.t, Constants.pointMulti);


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

    /**
     * Draw Points and Lines between them
     * TODO
     */
    void drawPointsAndLine(GL2 gl, Point[] list) {
        Point temp = null;

        for (int u = 0; u < (Constants.u + 1); u++) {  // v - Dir
            temp = null;
            for (int v = 0; v < (Constants.v + 1); v++) {
                int index = v * (Constants.v + 1) + u;
                Point p = list[index];

                drawPoint(gl, p, MyColor.BLUE);
                if (temp != null) drawLine(gl, p, temp, MyColor.WHITE);
                temp = p;
            }
        }

        for (int v = 0; v < (Constants.v + 1); v++) { // u - Dir
            temp = null;
            for (int u = 0; u < (Constants.u + 1); u++) {
                int index = v * (Constants.v + 1) + u;
                Point p = list[index];

                drawPoint(gl, p, MyColor.BLUE);
                if (temp != null) drawLine(gl, p, temp, MyColor.GREEN);
                temp = p;

            }
        }
        //gl.glEnd();

        /*
            for (int i = 0; i < (list.length - 1); i++) {
                drawLine(gl, list[i], list[i + 1], MyColor.WHITE);
                drawPoint(gl, list[i], MyColor.BLUE);
                drawPoint(gl, list[i + 1], MyColor.BLUE);
            }*/
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
            drawPoint(gl, tmp[0], MyColor.AQUA);
        }
    }
}
