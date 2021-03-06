package struct;

public final class Point {

    public float x;    // x-coordinate
    public float y;    // y-coordinate
    public float z;    // z-coordinate
    public float w = 0;    //epsilon
    public float weigth = 1;
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

    // point initialized from parameters
    public Point(float x, float y, float z, float weigth) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.weigth = weigth;
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


    public Point plus(Point p) {
        return new Point(x + p.x, y + p.y, z + p.z);
    }

    public Point times(float n) {
        Point sum = new Point();
        sum.x = x * n;
        sum.y = y * n;
        sum.z = z * n;
        //sum.weigth = weigth * n;
        return sum;
    }

    public Point delta(Point p) {
        Point delta = new Point();
        delta.x = p.x - x;
        delta.y = p.y - y;
        delta.z = p.z - z;
        return delta;
    }

    public float scalar(float alpha) {
        return x * alpha + y * alpha + z * alpha + w * alpha;
    }

    // return a string representation of this point
    @Override
    public String toString() {
        return "(" + x + ", " + y + ", " + z + ", weight " + weigth + ")";
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

    public void setWeight(float w) {
        this.weigth = w;
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

    public void set(Point p) {
        this.x = p.x;
        this.y = p.y;
        this.z = p.z;
        this.weigth = p.weigth;
    }
/*
    public Vector minus(Point p) {
        Vector sum = new Vector();
        sum.x = x - p.x();
        sum.y = y - p.y();
        sum.z = z - p.z();
        sum.w = w - p.w();
        return sum;
    }
*/

}
