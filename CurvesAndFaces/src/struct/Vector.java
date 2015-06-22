package struct;

public class Vector {

    public float x;    // x-coordinate
    public float y;    // y-coordinate
    public float z;    // z-coordinate
    public float w = 1;

    // create the zero vector of length N
    public Vector(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector() {
        this.x = (float) Math.random();
        this.y = (float) Math.random();
        this.z = (float) Math.random();
    }

    // return this + that
    public Vector plus(Vector that) {
        Vector c = new Vector();
        c.x = this.x + that.x;
        c.y = this.y + that.y;
        c.z = this.z + that.z;
        return c;
    }

    // return this - that
    public Vector minus(Vector that) {
        Vector c = new Vector();
        c.x = this.x - that.x;
        c.y = this.y - that.y;
        c.z = this.z - that.z;
        return c;
    }

    // create and return a new object whose value is (this * factor)
    public Vector times(float factor) {
        Vector c = new Vector();
        c.x = this.x * factor;
        c.y = this.y * factor;
        c.z = this.z * factor;
        return c;
    }

    // create and return cross Product of 2 Vectors)
    public Vector cross(Vector that) {
        Vector c = new Vector();
        c.x = y * that.z - z * that.y;
        c.y = z * that.x - x * that.z;
        c.z = x * that.y - y * that.x;
        c.w = 0; //epsilon check
        return c;
    }

    // create and return scalar Product of 2 Vectors)
    public float scalar(Vector that) {
        return x * that.x + y * that.y + z * that.z;
    }

    // return a string representation of the vector
    public String toString() {
        String s = "(";
        s += "x: " + x + " y: " + y + " z: " + z + " w: " + w;
        return s + ")";
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
}
