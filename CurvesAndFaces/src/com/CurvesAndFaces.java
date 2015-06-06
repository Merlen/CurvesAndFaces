package com;

import com.sun.j3d.utils.universe.SimpleUniverse;
import java.awt.BorderLayout;
import java.awt.GraphicsConfiguration;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.media.j3d.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.vecmath.*;

/**
 *
 * @author Niclas
 */
public class CurvesAndFaces extends JPanel {

    int count = 0;
    Point3f[] plaPts = new Point3f[0];

    public CurvesAndFaces() {
        readOBJ("src/input/UB1_1.obj");
        
        setLayout(new BorderLayout());
        GraphicsConfiguration gc = SimpleUniverse.getPreferredConfiguration();
        Canvas3D canvas3D = new Canvas3D(gc);
        add("Center", canvas3D);

        BranchGroup scene = null;
        try {
            scene = createSceneGraph();
        } catch (IOException ex) {
            log(CurvesAndFaces.class.getName() + ex);
        }
        if (scene != null) {
            scene.compile();
        }

        // SimpleUniverse is a Convenience Utility class
        SimpleUniverse simpleU = new SimpleUniverse(canvas3D);

        // This moves the ViewPlatform back a bit so the
        // objects in the scene can be viewed.
        simpleU.getViewingPlatform().setNominalViewingTransform();

        simpleU.addBranchGraph(scene);
    }

    public BranchGroup createSceneGraph() throws IOException {
        BranchGroup lineGroup = new BranchGroup();
        Appearance app = new Appearance();
        ColoringAttributes ca = new ColoringAttributes(new Color3f(204.0f, 204.0f, 204.0f), ColoringAttributes.SHADE_FLAT);
        app.setColoringAttributes(ca);

        PointArray pla = new PointArray(plaPts.length, GeometryArray.COORDINATES);

        //Set Points in Graph
        pla.setCoordinates(0, plaPts);

        //Point Attributes
        PointAttributes a_point_just_bigger = new PointAttributes();
        a_point_just_bigger.setPointSize(15.0f);//10 pixel-wide point
        a_point_just_bigger.setPointAntialiasingEnable(true);//now points are sphere-like(not a cube)
        app.setPointAttributes(a_point_just_bigger);

        Shape3D plShape = new Shape3D(pla, app);
        TransformGroup objRotate = new TransformGroup();

        objRotate.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        objRotate.addChild(plShape);
        lineGroup.addChild(objRotate);

        return lineGroup;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.add(new JScrollPane(new CurvesAndFaces()));
        frame.setSize(500, 500);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public final void readOBJ(String filename) {
        File f = new File("src/input/UB1_1.obj");
        ObjReader p = new ObjReader(f.getAbsolutePath());
        try {
            p.processLineByLine();
            setPoints(p.getPoints());
        } catch (IOException ex) {
            log(CurvesAndFaces.class.getName() + ex);
        }
    }

    public void setPoints(List<Point> points) {
        for (Point point : points) {
            plaPts = com.MyMath.copyPointArray(plaPts);
            plaPts[count] = new Point3f(point.x / 10, point.y / 10, point.z / 10);
            log(plaPts[count]);
            count++;
        }
    }

    public void bezierCurve(Point a, Point b) {

    }

    private double bernstein(int n, int k, float t) {
        return choose(n, k) * Math.pow(1 - t, n - k) * Math.pow(t, k);
    }

    private long choose(int n, int k) {
        return fact(n) / (fact(k) * fact(n - k));
    }

    private long fact(int n) {
        if (n > 1) {
            return fact(n - 1) * n;
        }
        return 1;
    }
    
     private static void log(Object aObject) {
        System.out.println(String.valueOf(aObject));
    }
}
