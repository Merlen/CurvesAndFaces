package com;

public final class Point {

    private final double x;    // x-coordinate
    private final double y;    // y-coordinate
    private final double z;    // z-coordinate

    // random point
    public Point() {
        x = Math.random();
        y = Math.random();
        z = Math.random();
    }
    // point initialized from parameters
    public Point(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    // accessor methods
    public double x() {
        return x;
    }
    public double y() {
        return y;
    }
    public double z() {
        return z;
    }
    public double distanceTo(Point that) {
        double dx = this.x - that.x;
        double dy = this.y - that.y;
        double dz = this.z - that.z;
        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }

    public Point addVectorToPoint(Vector v){
        return new Point();
    };
    
    Point SubtractVectorFromPoint(Vector v){
        return new Point();
    };
    Vector SubtractPointFromPoint(Point p){
        double[] xdata = {1.0, 2.0, 3.0, 4.0};
        return new Vector(xdata);
    }

                
    // return a string representation of this point
    public String toString() {
        return "(" + x + ", " + y + ", " + z + ")";
    }

    /*
     public static void main(String[] args) {        
     Point p = new Point();
     System.out.println("p  = " + p);
     System.out.println("   x     = " + p.x());
     System.out.println("   y     = " + p.y());
     System.out.println("   r     = " + p.r());
     System.out.println();

     Point q = new Point(0.5, 0.5, 0.5);
     System.out.println("q  = " + q);
     System.out.println("dist(p, q) = " + p.distanceTo(q));
     }
     */
}
