package struct;

public final class Point {

    public float x;    // x-coordinate
    public float y;    // y-coordinate
    public float z;    // z-coordinate
    public float w = 0;    //epsilon
    // random point

    public Point() {
        x = (float) Math.random();
        y = (float) Math.random();
        z = (float) Math.random();
    }

    // point initialized from parameters
    public Point(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    // accessor methods
    public float x() {
        return x;
    }

    public float y() {
        return y;
    }

    public float z() {
        return z;
    }

    public float w() {
        return w;
    }

    public Point plus(Vector v) {
        Point sum = new Point();
        sum.x = x + v.x();
        sum.y = y + v.y();
        sum.z = z + v.z();
        sum.w = w + v.w();
        return sum;
    }

    public Point minus(Vector v) {
        Point sum = new Point();
        sum.x = x - v.x();
        sum.y = y - v.y();
        sum.z = z - v.z();
        sum.w = w - v.w();
        return sum;
    }

    public Vector minus(Point p) {
        Vector sum = new Vector();
        sum.x = x - p.x();
        sum.y = y - p.y();
        sum.z = z - p.z();
        sum.w = w - p.w();
        return sum;
    }

    public float scalar(float alpha) {
        return x * alpha + y * alpha + z * alpha + w * alpha;
    }

    // return a string representation of this point
    @Override
    public String toString() {
        return "(" + x + ", " + y + ", " + z + ")";
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setZ(float z) {
        this.z = z;
    }
}
