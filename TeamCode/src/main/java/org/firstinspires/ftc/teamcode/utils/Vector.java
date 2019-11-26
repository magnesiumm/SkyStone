package org.firstinspires.ftc.teamcode.utils;

public class Vector {
    public float i;    // magnitude of vector in x direction
    public float j;    // magnitude of vector in y direction
    public float k;    // magnitude of vector in z direction

    /*
     * Vector class constuctor: initialize components
     *
     * @param i the x component
     * @param j the y component
     */
    public Vector(float i, float j) {
        this.i = i;
        this.j = j;
        this.k = 0;
    }

    /*
     * Vector class constuctor: initialize components
     *
     * @param i the x component
     * @param j the y component
     * @param k the z component
     */
    public Vector(float i, float j, float k) {
        this.i = i;
        this.j = j;
        this.k = k;
    }

    /*
     * Add two vectors together
     *
     * @param b the vector to be added to this vector
     */
    public Vector add(Vector b) {
        return new Vector(this.i + b.i, this.j + b.j, this.k + b.k);
    }

    /*
     * Subtract one vector from another
     *
     * @param b the vector to be subtracted from this vector
     */
    public Vector subtract(Vector b) {
        return new Vector(this.i - b.i, this.j - b.j, this.k - b.k);
    }
}