package fr.snapgames.demo.core.entity;

import fr.snapgames.demo.core.physic.Material;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

/**
 * An Entity is a minimum game object to be managed by the Game on its moves and its display.
 * our entity will have at least a name, a position (x,y) and a speed (dx,dy).
 * It will also have a size (width and height), and some border and fill color.
 *
 * @author Frédéric Delorme
 * @since 0.0.7
 */
public class Entity<T> {

    private static int index = 1;
    public int id = index++;
    public String name = "entity_" + String.format("entity_%03d", id);

    // debug level
    public int debug;

    // Position.
    public double x;
    public double y;

    // Speed
    public double dx;
    public double dy;

    // Acceleration
    public double ax;
    public double ay;
    /**
     * List of forces applied to this entity.
     * (used by {@link fr.snapgames.demo.core.physic.PhysicEngine})
     */
    public List<Point2D> forces = new ArrayList<>();

    public double width;

    public double height;

    private double direction;

    public Color fillColor;

    public Color borderColor;

    /**
     * The {@link Material} defining physic attributes for that {@link Entity}.
     */
    public Material material;

    /**
     * Mass for that entity.
     */
    public double mass;

    /**
     * Define contact sides if contact exists:
     * <ul>
     *     <li>+1 : contact on RIGHT side</li>
     *     <li>+2 : contact on BOTTOM side</li>
     *     <li>+4 : contact on LEFT side</li>
     *     <li>+8 : contact on TOP side</li>
     * </ul>
     */
    public int contact;

    /**
     * The bounding box for that entity.
     */
    public Shape box;
    /**
     * Rendering layer for that object
     */
    private int layer;
    /**
     * rendering priority in the entity's layer.
     */
    private int priority;

    public Entity() {
        this.mass = 1.0;
        this.material = Material.DEFAULT;
        this.width = 16.0;
        this.height = 16.0;
        this.fillColor = Color.RED;
        this.borderColor = Color.BLACK;
        layer = 1;
        priority = 1;
    }

    /**
     * Create a new Entity with its name.
     * According to size and position default value, the {@link Entity#box} is updated.
     *
     * @param name the name for this new {@link Entity}.
     */
    public Entity(String name) {
        this();
        this.name = name;
    }


    /**
     * Define the {@link Entity} position in a fluent API
     *
     * @param x horizontal position
     * @param y vertical position
     * @return the updated {@link Entity}.
     */
    public Entity<T> setPosition(double x, double y) {
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
    public Entity<T> setSpeed(double dx, double dy) {
        this.dx = dx;
        this.dy = dy;
        return this;
    }

    /**
     * Define the {@link Entity} acceleration in a fluent API
     *
     * @param ax horizontal acceleration
     * @param ay vertical acceleration
     * @return the updated {@link Entity}.
     */
    public Entity<T> setAcceleration(double ax, double ay) {
        this.ax = ax;
        this.ay = ay;
        return this;
    }

    /**
     * Define the {@link Entity} size in a fluent API
     *
     * @param w horizontal size
     * @param h vertical size
     * @return the updated {@link Entity}.
     */

    public Entity<T> setSize(double w, double h) {
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
    public Entity<T> setDirection(double d) {
        this.direction = d;
        return this;
    }

    /**
     * Set the {@link Entity} draw border color
     *
     * @param c the border {@link Color}
     * @return the updated {@link Entity}.
     */
    public Entity<T> setBorderColor(Color c) {
        this.borderColor = c;
        return this;
    }

    /**
     * Set the {@link Entity} fill color
     *
     * @param c the fill {@link Color}
     * @return the updated {@link Entity}.
     */
    public Entity<T> setFillColor(Color c) {
        this.fillColor = c;
        return this;
    }

    /**
     * Set the debug display level for this {@link Entity}.
     *
     * @param d the int level from 0 to 5
     * @return the updated {@link Entity}.
     */
    public Entity<T> setDebug(int d) {
        this.debug = d;
        return this;
    }

    /**
     * Seth the {@link Material} for this {@link Entity}
     *
     * @param m the material instance to be applied to the Entity.
     * @return the updated {@link Entity}.
     */
    public Entity<T> setMaterial(Material m) {
        this.material = m;
        return this;
    }

    /**
     * Set the mass value for this {@link Entity}
     *
     * @param mass a double value from 1.0 to what_ever_you_want_that_physic_can_do
     * @return the updated {@link Entity}.
     */
    public Entity<T> setMass(double mass) {
        this.mass = mass;
        return this;
    }

    /**
     * Apply a force f to that {@link Entity}
     *
     * @param f a {@link Point2D} force to be applied.
     * @return the updated {@link Entity}.
     */
    public Entity<T> addForce(Point2D f) {
        this.forces.add(f);
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
     * Prepare debug information for the new debug display.
     *
     * @return a list of String containing debug {@link Entity}'s attributes information
     */
    public List<String> getDebugInfo() {
        List<String> infos = new ArrayList<>();
        infos.add(String.format("name:%s", name));
        infos.add(String.format("pos:%4.2f,%4.2f", x, y));
        infos.add(String.format("size:%4.2f,%4.2f", width, height));
        infos.add(String.format("spd:%4.2f,%4.2f", dx, dy));
        infos.add(String.format("acc:%4.2f,%4.2f", ax, ay));
        infos.add(String.format("map:%s[d=%4.2f,e=%4.2f,f=%4.2f]",
                material.name,
                material.density,
                material.elasticity,
                material.friction));

        return infos;
    }

    /**
     * Update the bounding box {@link Shape} according to the position
     * and size of the {@link Entity}.
     */
    public void updateBox() {
        this.box = new Rectangle2D.Double(x, y, width, height);
    }

    public int getLayer() {
        return layer;
    }

    public int getPriority() {
        return priority;
    }

    /**
     * Set the display layer for that object.
     *
     * @param l the drawing layer number.
     * @return the updated {@link Entity}.
     */
    public Entity<T> setLayer(int l) {
        this.layer = l;
        return this;
    }

    /**
     * Set the display priority for that object.
     *
     * @param p the drawing priority value.
     * @return the updated {@link Entity}.
     */
    public Entity<T> setPriority(int p) {
        this.priority = p;
        return this;
    }
}
