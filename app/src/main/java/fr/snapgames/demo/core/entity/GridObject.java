package fr.snapgames.demo.core.entity;

import java.awt.*;

/**
 * The GridObject is only exits to help debug and develop.
 *
 * @author Frédéric Delorme
 * @since 0.0.9
 **/
public class GridObject extends GameObject {
    private double stepX = 16.0;
    private double stepY = 16.0;

    private float[] dash = {0.2f, 0.6f, 0.0f};
    private BasicStroke stroke;

    public GridObject(String name) {
        super(name);
    }

    public double getStepX() {
        return stepX;
    }

    public double getStepY() {
        return stepY;
    }

    public GridObject setStepSize(double sx, double sy) {
        this.stepX = sx;
        this.stepY = sy;
        return this;
    }

    /**
     * Set the Stroke line dash or not.
     *
     * @param dash the float array defining the list of dot width.
     * @return
     * @link https://docs.oracle.com/en/java/javase/19/docs/api/java.desktop/java/awt/BasicStroke.html
     */
    public GridObject setLineStroke(float[] dash) {
        this.dash = dash;
        this.stroke = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, dash, 0);
        return this;
    }

    public Stroke getStroke() {
        return stroke;
    }
}
