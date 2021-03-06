/*
 * The MIT License
 *
 * Copyright 2017 Bowen Peng.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package uvector;

import java.util.Arrays;

/**
 *
 * @author bowen
 */
public class Vector3 implements Vector<Vector3> {

    private final double[] content;
    
    /**
     * Creates a new 3-vector with all its elements set to 0
     */
    public Vector3() {
        content = new double[3];
    }
    
    /**
     * Creates a new 3-vector with all its elements set to a value
     * @param fill Value to fill the vector
     */
    public Vector3(double fill) {
        content = new double[] {fill, fill, fill};
    }
    
    /**
     * Creates a new 3-vector with specific values
     * @param a Value of i
     * @param b Value of j
     * @param c Value of k
     */
    public Vector3(double a, double b, double c) {
        content = new double[] {a, b, c};
    }
    
    @Override
    public Vector3 copy() {
        return new Vector3(content[0], content[1], content[2]);
    }

    @Override
    public Vector3 shell() {
        return new Vector3();
    }

    @Override
    public int size() {
        return 3;
    }

    @Override
    public double[] content() {
        return content;
    }

    @Override
    public double[] get() {
        return Arrays.copyOf(content, 3);
    }

    @Override
    public double get(int i) {
        if (i >= 0 && i < 3) {
            return content[i];
        }
        return Double.NaN;
    }

    @Override
    public Vector3 zero() {
        content[0] = 0;
        content[1] = 0;
        content[2] = 0;
        return this;
    }

    @Override
    public Vector3 fill(double d) {
        content[0] = d;
        content[1] = d;
        content[2] = d;
        return this;
    }

    @Override
    public Vector3 negate() {
        content[0] = -content[0];
        content[1] = -content[1];
        content[2] = -content[2];
        return this;
    }

    @Override
    public Vector3 normalise() {
        double norm = norm();
        if (norm == 0) {
            content[0] = 1;
            content[1] = 1;
            content[2] = 1;
            norm = norm();
        }
        div(norm);
        return this;
    }

    @Override
    public Vector3 set(double... dArr) {
        if (dArr.length >= 3) {
            content[0] = dArr[0];
            content[1] = dArr[1];
            content[2] = dArr[2];
        } else if (dArr.length >= 2) {
            content[0] = dArr[0];
            content[1] = dArr[1];
        } else if (dArr.length == 1) {
            content[0] = dArr[0];
        }
        return this;
    }

    @Override
    public Vector3 set(Vector3 vector) {
        content[0] = vector.get(0);
        content[1] = vector.get(1);
        content[2] = vector.get(2);
        return this;
    }

    @Override
    public Vector3 set(int i, double d) {
        if (i >= 0 && i < 3) {
            content[i] = d;
        }
        return this;
    }
    
    /**
     * @return Element at index 0 of the vector, same as {@link #get(0)}
     */
    public double getX() {
        return get(0);
    }
    /**
     * @return Element at index 1 of the vector, same as {@link #get(1)}
     */
    public double getY() {
        return get(1);
    }
    /**
     * @return Element at index 2 of the vector, same as {@link #get(2)}
     */
    public double getZ() {
        return get(2);
    }

    /**
     * Sets the element at index 0 to a value, sale as {@link #set(0, d)}
     * @param d
     * @return The same vector for method chaining
     */
    public Vector3 setX(double d) {
        return set(0, d);
    }
    /**
     * Sets the element at index 1 to a value, sale as {@link #set(1, d)}
     * @param d
     * @return The same vector for method chaining
     */
    public Vector3 setY(double d) {
        return set(1, d);
    }
    /**
     * Sets the element at index 2 to a value, sale as {@link #set(2, d)}
     * @param d
     * @return The same vector for method chaining
     */
    public Vector3 setZ(double d) {
        return set(2, d);
    }

    @Override
    public Vector3 add(Vector3 vector) {
        content[0] += vector.get(0);
        content[1] += vector.get(1);
        content[2] += vector.get(2);
        return this;
    }

    @Override
    public Vector3 add(double d) {
        content[0] += d;
        content[1] += d;
        content[2] += d;
        return this;
    }

    @Override
    public Vector3 sub(Vector3 vector) {
        content[0] -= vector.get(0);
        content[1] -= vector.get(1);
        content[2] -= vector.get(2);
        return this;
    }

    @Override
    public Vector3 sub(double d) {
        content[0] -= d;
        content[1] -= d;
        content[2] -= d;
        return this;
    }

    @Override
    public Vector3 mulElem(Vector3 vector) {
        content[0] *= vector.get(0);
        content[1] *= vector.get(1);
        content[2] *= vector.get(2);
        return this;
    }

    @Override
    public Vector3 mulElem(double d) {
        content[0] *= d;
        content[1] *= d;
        content[2] *= d;
        return this;
    }

    @Override
    public Vector3 div(Vector3 vector) {
        content[0] /= (vector.get(0) == 0) ? 1 : vector.get(0);
        content[1] /= (vector.get(1) == 0) ? 1 : vector.get(1);
        content[2] /= (vector.get(2) == 0) ? 1 : vector.get(2);
        return this;
    }

    @Override
    public Vector3 div(double d) {
        content[0] /= (d == 0) ? 1 : d;
        content[1] /= (d == 0) ? 1 : d;
        content[2] /= (d == 0) ? 1 : d;
        return this;
    }

    @Override
    public double norm() { //Frobenius norm
        return Math.sqrt((content[0] * content[0]) + (content[1] * content[1]) + (content[2] * content[2]));
    }
    @Override
    public double norm(double p) { //n-norm as vector norm
        if (p == 0) {
            return Double.NaN;
        } else {
            return Math.pow(Math.pow(content[0], p) + Math.pow(content[1], p) + Math.pow(content[2], p), 1/p);
        }
        
    }
    @Override
    public double normMax() { //returns max norm, where p = inf
        return Math.max(content[0], Math.max(content[1], content[2]));
    }
    @Override
    public double normSqr() { //Squared norm
        return (content[0] * content[0]) + (content[1] * content[1]) + (content[2] * content[2]);
    }
    
    @Override
    public String toString() {
        return "[" + content[0] + ", " + content[1] + ", " + content[2] + "]";
    }
    
}
