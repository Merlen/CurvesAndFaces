package scene;

import bezier.Bernstein;
import bezier.BezierMath;
import bezier.Casteljau;
import com.ObjReader;
import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;
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

        gl.glRotatef(Constants.rotX, 1, 0, 0);                //Rotation with angle TempRot on X-Axis
        gl.glRotatef(Constants.rotY, 0, 1, 0);                //Rotation with angle TempRot on Y-Axis
        gl.glRotatef(Constants.rotZ, 0, 0, 1);                //Rotation with angle TempRot on Z-Axis
        gl.glTranslatef(Constants.traX, Constants.traY, Constants.traZ); //Movement on X-, Y-, Z-Axis

        scaleFactor = Constants.zoom; // zoom
        axes(gl);
        drawPointsAndLine(gl, Constants.points, false);

        if (Constants.incPoints) {
            Constants.points = BezierMath.DegreeInc(Constants.points);
            Constants.count = Constants.count + 1;
        }
        if (Constants.castel) {
            Casteljau(gl);
        } else {
            Bernstein(gl);
        }

        resetTransform();
    }

    private void Casteljau(GL2 gl) {
        Point[] bez;

        if (!Constants.blossom) {
            bez = Casteljau.deCasteljau(Constants.points, Constants.t); // control Points
            log("PRE " + Constants.points.length + " " + bez.length);
            drawPointsAndLine(gl, bez, true);

            Point[] castelCurve = Casteljau.deCasteljauCurve(Constants.points, -1f, 2f); //curve
            drawCurve(gl, castelCurve, MyColor.AQUA);
        } else {
            Constants.firstT = 1f / 5f;
            Constants.secondT = 4f / 5f;

            float[] multiT = new float[2];
            multiT[0] = Constants.firstT;
            multiT[1] = Constants.secondT;
            bez = Casteljau.blossom(Constants.points, multiT); // control Points

            log("PRE Blossom " + Constants.points.length + " " + bez.length);
            drawPointsAndLine(gl, bez, false);

            for (Point p : bez) drawPoint(gl, p, MyColor.RED);

            Point[] castelCurve1 = Casteljau.deCasteljauCurve(Constants.points, 0, Constants.firstT);
            drawCurve(gl, castelCurve1, MyColor.GREEN);

            Point[] castelCurve2 = Casteljau.deCasteljauCurve(Constants.points, Constants.firstT, Constants.secondT); //curve
            drawCurve(gl, castelCurve2, MyColor.AQUA);

            Point[] castelCurve3 = Casteljau.deCasteljauCurve(Constants.points, Constants.secondT, 1); //curve
            drawCurve(gl, castelCurve3, MyColor.YELLOW);
        }
    }

    private void Bernstein(GL2 gl) {
        Point M;
        Point P = null;

        if (Constants.derivate >= 0) {
            log("Draw Derivate " + Constants.derivate);
            M = Bernstein.getDerivate(Constants.derivate, Constants.t, Constants.points);

            drawPoint(gl, Bernstein.getDerivate(Constants.derivate, Constants.t, Constants.points), MyColor.YELLOW); //curve

        }

        P = Constants.points[0];
        for (float t = 0; t <= 1.0; t += 0.01) {
            M = Bernstein.bernsteinCurve(Constants.points, t, Constants.count - 1); //curve
            drawLine(gl, P, M, MyColor.AQUA);
            P = M;

        }

        if (polynomialScene == null) {
            polynomialScene = new PolynomialScene(Constants.points, Constants.t);

        } else {
            polynomialScene.setPoints(Constants.points, Constants.t);
        }

    }

    private void resetTransform() {
        if (polynomialScene != null && polynomialScene.closed) {
            polynomialScene = null;
        }

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
    public void setPoints(List<Point> points) {
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
            log("POST " + Constants.count + " " + list.length);
            drawControlPoints(gl, list, Constants.count);
        }

    }

    /**
     * Draw ControlPoints with Lines
     */
    void drawControlPoints(GL2 gl, Point[] list, int length) {

        Point[] tmp = list;
        log(tmp.length + " le " + length);
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
