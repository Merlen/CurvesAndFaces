package scene;

import bezier.BezierMath;
import bezier.curves.Bernstein;
import bezier.curves.Casteljau;
import bezier.curves.Derivate;
import com.ObjReader;
import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import help.Constants;
import help.MyColor;
import help.MyMath;
import listener.EventMediator;
import struct.Point;
import struct.Vector;

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

    private static EventMediator listener;
    Point[] plaPts = new Point[0];
    int scaleFactor = 10;
    private float pointSize = 10;
    private PolynomialScene polynomialScene;

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
        if (Constants.blossom) {
            blossom(gl);

        } else {
            if (Constants.castel) {
                Casteljau(gl);
            } else {
                Bernstein(gl);
            }
        }


        resetTransform();
    }

    private void blossom(GL2 gl) {
        log("Blossom Building");
        Point[] ctrlPoints;

        float[] multiT = new float[2];
        multiT[0] = Constants.firstT;
        multiT[1] = Constants.secondT;

        ctrlPoints = Casteljau.blossom(Constants.points, multiT); // control Points

        for (Point p : ctrlPoints){
            drawPoint(gl, p, MyColor.GREEN);
        }

        //drawPointsAndLine(gl, ctrlPoints, false);

        Point[] castelCurve2 = Casteljau.deCasteljauCurve(Constants.points, Constants.firstT, Constants.secondT); //curve
        drawCurve(gl, castelCurve2, MyColor.AQUA);
    }

    private void Casteljau(GL2 gl) {
        log("DeCasteljau Building");

        Point[] ctrlPoints;
        drawDerivate(gl);

        if (!Constants.blossom) {
            ctrlPoints = Casteljau.deCasteljau(Constants.points, Constants.t); // control Points
            if(Constants.showControl) drawPointsAndLine(gl, ctrlPoints, true);

            Point[] castelCurve = Casteljau.deCasteljauCurve(Constants.points, -1f, 2f); //curve
            drawCurve(gl, castelCurve, MyColor.AQUA);
        }
    }

    private void Bernstein(GL2 gl) {
        log("Bernstein Building");
        Point M;
        Point P;

        drawDerivate(gl);

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

    void drawDerivate(GL2 gl) {
        Vector v;

        if (Constants.derivate > 0) {
            Vector pointT;

            if (Constants.castel) {
                pointT = Derivate.getCasteljauDerivate(0, Constants.t, Constants.points);
                v = Derivate.getCasteljauDerivate(Constants.derivate, Constants.t, Constants.points);
                log("CastelJau Derivate not implemented. I chose Bernstein instead " + pointT);

                pointT = Derivate.getBernsteinDerivate(0, Constants.t, Constants.points);
                v = Derivate.getBernsteinDerivate(Constants.derivate, Constants.t, Constants.points);
            } else {
                pointT = Derivate.getBernsteinDerivate(0, Constants.t, Constants.points);
                v = Derivate.getBernsteinDerivate(Constants.derivate, Constants.t, Constants.points);
            }

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
     */
    void drawPointsAndLine(GL2 gl, Point[] list, boolean bez) {
        if (!bez) {
            for (int i = 0; i < (list.length - 1); i++) {
                drawLine(gl, list[i], list[i + 1], MyColor.WHITE);
                drawPoint(gl, list[i], MyColor.BLUE);
                drawPoint(gl, list[i + 1], MyColor.BLUE);
            }
        } else {
            drawControlPoints(gl, list, Constants.count);
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
            drawPoint(gl, tmp[0], MyColor.AQUA);
        }
    }
}
