package fr.snapgames.demo.core.math;

import java.util.List;
import java.util.Objects;

/**
 * {@link Vector2D} class is to define, manage and operate  2D Vector.
 * <p>
 * eg.usage :
 *
 * <pre>
 *     // create a vector
 *     Vector2D v1 = new Vector2D(0.0,0.0);
 *     // apply some common operation
 *     Vector2D v2 = v1.multiply(12.0).ceil(0.001).maximize(2000.0);
 *     // normalize the resulting vector
 *     double norm = v2.normalize();
 *     // compute length of the vector
 *     double l = v2.length();
 *     // define distance between 2 vector
 *     double distance = v1.distance(v2);
 *     // get the opposite vector
 *     Vector2D v3 = v2.negate();
 *     // compute dot product between v1 and v2.
 *     double d = v1.dot(v2);
 * </pre>
 *
 * @author Frédéric Delorme
 * @since 0.1.3
 */
public class Vector2D {
    public String ATTRIBUTE_OUTPUT_FORMAT = "%04.2f";
    public String TO_STRING_FORMAT = "{x:" + ATTRIBUTE_OUTPUT_FORMAT + ",y:" + ATTRIBUTE_OUTPUT_FORMAT + "}";
    public double x, y;

    public Vector2D() {
        x = 0.0f;
        y = 0.0f;
    }

    /**
     * @param x
     * @param y
     */
    public Vector2D(double x, double y) {
        super();
        this.x = x;
        this.y = y;
    }

    public Vector2D add(Vector2D v) {
        return new Vector2D(x + v.x, y + v.y);
    }

    public Vector2D substract(Vector2D v1) {
        return new Vector2D(x - v1.x, y - v1.y);
    }

    public Vector2D multiply(double f) {
        return new Vector2D(x * f, y * f);
    }

    public double dot(Vector2D v1) {

        return v1.x * y + v1.y * x;
    }

    public double length() {
        return Math.sqrt(x * x + y * y);
    }

    public double distance(Vector2D v1) {
        return substract(v1).length();
    }

    public Vector2D divide(double f) {
        return new Vector2D(x / f, y / f);
    }

    public Vector2D normalize() {
        return divide(length());
    }

    public Vector2D negate() {
        return new Vector2D(-x, -y);
    }

    public double angle(Vector2D v1) {
        double vDot = this.dot(v1) / (this.length() * v1.length());
        if (vDot < -1.0)
            vDot = -1.0;
        if (vDot > 1.0)
            vDot = 1.0;
        return Math.acos(vDot);
    }

    public Vector2D addAll(List<Vector2D> forces) {
        Vector2D sum = new Vector2D();
        for (Vector2D f : forces) {
            sum = sum.add(f);
        }
        return sum;
    }

    public Vector2D maximize(double max) {
        if (Math.abs(x) > max) {
            x = Math.signum(x) * max;
        }
        if (Math.abs(y) > max) {
            y = Math.signum(y) * max;
        }
        return this;
    }

    public Vector2D maximize(double maxX, double maxY) {
        if (Math.abs(x) > maxX) {
            x = Math.signum(x) * maxX;
        }
        if (Math.abs(y) > maxY) {
            y = Math.signum(y) * maxY;
        }
        return this;
    }

    public Vector2D ceil(double ceilThreshold) {
        x = Math.copySign((Math.abs(x) < ceilThreshold ? 0.0 : x), x);
        y = Math.copySign((Math.abs(y) < ceilThreshold ? 0.0 : y), y);
        return this;
    }

    public Vector2D ceil(double ceilThresholdX, double ceilThresholdY) {
        x = Math.copySign((Math.abs(x) < ceilThresholdX ? 0.0 : x), x);
        y = Math.copySign((Math.abs(y) < ceilThresholdY ? 0.0 : y), y);
        return this;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (getClass() != o.getClass()) {
            return false;
        }
        Vector2D vo = (Vector2D) o;
        return Objects.equals(x, vo.x) && Objects.equals(y, vo.y);
    }

    public String toString() {
        return TO_STRING_FORMAT.formatted(x, y);
    }

}