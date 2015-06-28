package scene;

import bezier.curves.Bernstein;
import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import help.MyColor;
import listener.PolynomialMediator;
import struct.Point;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * @author Merlen
 */
class PolynomialScene implements GLEventListener {

    GLCanvas canvas;
    boolean closed = true;
    private PolynomialMediator listener;
    private Point[] plaPts = new Point[0];
    private float t = 1;
    private float pointSize = 10;
    private int scaleFactor = 1;


    PolynomialScene(Point[] points, float t) {
        plaPts = points;
        this.t = t;
        closed = false;

        GLProfile glp = GLProfile.getDefault();
        GLCapabilities caps = new GLCapabilities(glp);
        canvas = new GLCanvas(caps);

        final Frame frame = new Frame("Polynomial CurveScene");
        frame.setSize(300, 300);
        frame.add(canvas);
        frame.setVisible(true);


        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                frame.dispose();
                closed = true;
            }
        });

        canvas.addGLEventListener(this);

        listener = new PolynomialMediator(canvas);
        listener.zoom = scaleFactor;


        canvas.addKeyListener(listener);
    }


    public void display(GLAutoDrawable drawable) {
        render(drawable);
    }

    public void init(GLAutoDrawable drawable) {
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

        scaleFactor = listener.zoom; // zoom
        axes(gl);

        int j = 0;

        Point M;
        Point P;

        for (int i = 0; i < plaPts.length; i++) {
            P = null;

            for (float t = 0; t <= 1.0; t += 0.01) {
                M = Bernstein.bernsteinPolynomial(i, plaPts.length - 1, t);
                if (P != null) drawLine(gl, P, M, MyColor.colors.get(j));
                P = M;
            }
            j++;
            if (j > MyColor.colors.size() - 1) j = 0;
        }

        resetTransform();

    }

    private void resetTransform() {
        listener.rotX = listener.rotY = listener.rotZ = listener.traX = listener.traY = listener.traZ = 0;
    }

    /**
     * Prints Information
     */
    private void log(Object aObject) {
        System.out.println(this.getClass() + " " + String.valueOf(aObject));
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

    public void setPoints(Point[] plaPts, float t) {
        this.plaPts = plaPts;
        this.t = t;

        if (canvas != null) canvas.repaint();
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

}
