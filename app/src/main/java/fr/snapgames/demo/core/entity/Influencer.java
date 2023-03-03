package fr.snapgames.demo.core.entity;


import fr.snapgames.demo.core.math.Vector2d;

/**
 * An {@link Influencer} object consists of an area in the world where some forces are applied
 * according to some physic mechanism and behaviors..
 *
 * @author Frédéric Delorme
 * @since 0.0.1
 */
public class Influencer extends GameObject {

    /**
     * a new {@link Influencer} with its own name !
     *
     * @param name name for this {@link Influencer}.
     */
    public Influencer(String name) {
        super(name);
    }

    /**
     * Defin ethe area of influence for this instance.
     *
     * @param x      horizontal position
     * @param y      vertical position
     * @param width  width  of this influencing area
     * @param height height of this influencing area
     * @return the instance of the {@link Influencer} area is now defined.
     */
    public Influencer setArea(double x, double y, double width, double height) {
        setPosition(x, y);
        setSize(width, height);
        return this;
    }
}
