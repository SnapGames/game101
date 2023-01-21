package fr.snapgames.demo.core.entity;

/**
 * The GridObject is only exits to help debug and develop.
 *
 * @author Frédéric Delorme
 * @since 0.0.9
 **/
public class GridObject extends GameObject {
    private double stepX = 16.0;
    private double stepY = 16.0;

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
}
