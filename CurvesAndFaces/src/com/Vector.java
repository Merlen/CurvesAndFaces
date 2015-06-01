package com;

public class Vector {

    private final int N;         // length of the vector
    private double[] data;       // array of vector's components

    // create the zero vector of length N
    public Vector(int N) {
        this.N = N;
        this.data = new double[N];
    }

    // create a vector from an array
    public Vector(double[] data) {
        N = data.length;

        // defensive copy so that client can't alter our copy of data[]
        this.data = new double[N];
        for (int i = 0; i < N; i++) {
            this.data[i] = data[i];
        }
    }

    // return the length of the vector
    public int length() {
        return N;
    }

    // return the inner product of this Vector a and b
    public double dot(Vector that) {
        if (this.N != that.N) {
            throw new RuntimeException("Dimensions don't agree");
        }
        double sum = 0.0;
        for (int i = 0; i < N; i++) {
            sum = sum + (this.data[i] * that.data[i]);
        }
        return sum;
    }

    // return this + that
    public Vector plus(Vector that) {
        if (this.N != that.N) {
            throw new RuntimeException("Dimensions don't agree");
        }
        Vector c = new Vector(N);
        for (int i = 0; i < N; i++) {
            c.data[i] = this.data[i] + that.data[i];
        }
        return c;
    }

    // return this - that
    public Vector minus(Vector that) {
        if (this.N != that.N) {
            throw new RuntimeException("Dimensions don't agree");
        }
        Vector c = new Vector(N);
        for (int i = 0; i < N; i++) {
            c.data[i] = this.data[i] - that.data[i];
        }
        return c;
    }

    // create and return a new object whose value is (this * factor)
    public Vector times(double factor) {
        Vector c = new Vector(N);
        for (int i = 0; i < N; i++) {
            c.data[i] = factor * data[i];
        }
        return c;
    }

    // return a string representation of the vector
    public String toString() {
        String s = "(";
        for (int i = 0; i < N; i++) {
            s += data[i];
            if (i < N - 1) {
                s += ", ";
            }
        }
        return s + ")";
    }
}
