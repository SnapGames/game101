package fr.snapgames.demo.core.entity;

import java.awt.*;

/**
 * An Entity is a minimum game object to be managed by the Game on its moves and its display.
 * our entity will have at least a name, a position (x,y) and a speed (dx,dy).
 * It will also have a size (width and height), and some border and fill color.
 *
 * @author Frédéric Delorme
 * @since 0.0.7
 */
public class Entity {

    private final String name;
    public double x;
    public double y;
    public double dx;
    public double dy;

    public double width;

    public double height;

    private double direction;

    public Color fillColor;

    public Color borderColor;

    /**
     * Create a new Entity with its name.
     *
     * @param name the name for this new {@link Entity}.
     */
    public Entity(String name) {
        this.name = name;
    }

    /**
     * Define the {@link Entity} position in a fluent API
     *
     * @param x horizontal position
     * @param y vertical position
     * @return the updated {@link Entity}.
     */
    public Entity setPosition(double x, double y) {
        this.x = x;
        this.y = y;
        return this;
    }

    /**
     * Define the {@link Entity} speed in a fluent API
     *
     * @param dx horizontal velocity
     * @param dy vertical velocity
     * @return the updated {@link Entity}.
     */
    public Entity setSpeed(double dx, double dy) {
        this.dx = dx;
        this.dy = dy;
        return this;
    }

    /**
     * Define the {@link Entity} size in a fluent API
     *
     * @param w horizontal size
     * @param h vertical size
     * @return the updated {@link Entity}.
     */

    public Entity setSize(double w, double h) {
        this.width = w;
        this.height = h;
        return this;
    }

    /**
     * Set the {@link Entity} direction (-1 (left)  to 1 (right))
     *
     * @param d the direction encoding
     * @return the updated {@link Entity}.
     */
    public Entity setDirection(double d) {
        this.direction = d;
        return this;
    }

    /**
     * Set the {@link Entity} draw border color
     *
     * @param c the border {@link Color}
     * @return the updated {@link Entity}.
     */
    public Entity setBorderColor(Color c) {
        this.borderColor = c;
        return this;
    }

    /**
     * Set the {@link Entity} fill color
     *
     * @param c the fill {@link Color}
     * @return the updated {@link Entity}.
     */
    public Entity setFillColor(Color c) {
        this.fillColor = c;
        return this;
    }

    /**
     * Retrieve name of this {@link Entity}
     *
     * @return the name for this {@link Entity}.
     */
    public String getName() {
        return name;
    }

    /**
     * Update the {@link Entity} position according to its current speed.
     *
     * @param elapsed the elapsed time since previous call.
     */
    public void update(double elapsed) {
        x += dx * elapsed;
        y += dy * elapsed;
    }
}
