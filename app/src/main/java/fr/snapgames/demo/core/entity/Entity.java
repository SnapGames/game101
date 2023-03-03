package fr.snapgames.demo.core.entity;

import fr.snapgames.demo.core.math.Vector2d;
import fr.snapgames.demo.core.physic.Material;
import fr.snapgames.demo.core.physic.PhysicType;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collection;
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
    protected int id = index++;
    protected String name = "entity_" + String.format("entity_%03d", id);

    // debug level
    protected int debug;

    // Position.
    public Vector2d position = new Vector2d();

    // size
    public Vector2d size = new Vector2d();

    // -- Physic Attributes --
    /**
     * Physic Type for this {@link Entity}.
     */
    protected PhysicType physicType;
    /**
     * Speed
     */
    public Vector2d velocity = new Vector2d();
    ;
    /**
     * Acceleration
     */
    public Vector2d acceleration = new Vector2d();
    ;

    /**
     * Mass for that entity.
     */
    protected double mass;
    /**
     * List of forces applied on this {@link Entity}.
     */
    protected List<Vector2d> forces = new ArrayList<>();
    /**
     * The physic characteristic's {@link Material} used by {@link fr.snapgames.demo.core.physic.PhysicEngine}
     * to compute {@link Entity} behavior.
     */
    public Material material;

    /**
     * Define contact sides if contact exists:
     * <ul>
     *     <li>+1 : contact on RIGHT side</li>
     *     <li>+2 : contact on BOTTOM side</li>
     *     <li>+4 : contact on LEFT side</li>
     *     <li>+8 : contact on TOP side</li>
     * </ul>
     */
    protected int contact;

    /**
     * The bounding box for that entity.
     */
    protected Shape box;

    // -- Graphics attributes --
    /**
     * Drawing Filling color
     */
    protected Color fillColor;
    /**
     * Drawing border color
     */
    protected Color borderColor;

    /**
     * Rendering layer for that object
     */
    protected int layer;
    /**
     * rendering priority in the entity's layer.
     */
    protected int priority;

    /**
     * Define if the object must be stick to the camera viewport.
     */
    private boolean stickToCamera = false;
    private boolean active = true;

    public Entity() {
        this.mass = 1.0;
        this.material = Material.DEFAULT;
        this.physicType = PhysicType.DYNAMIC;
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
        this.position = new Vector2d(x, y);
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
        this.velocity = new Vector2d(dx, dy);
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
        this.acceleration = new Vector2d(ax, ay);
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
        this.size = new Vector2d(w, h);
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
     * Set the {@link Entity}'s physicType.
     *
     * @param pt a {@link PhysicType} values
     * @return the updated {@link Entity}
     */
    public Entity<T> setPhysicType(PhysicType pt) {
        this.physicType = pt;
        return this;
    }

    /**
     * Set the active flag for this {@link Entity}
     *
     * @param active a boolean value. if true, this entity is active and will be processed and displayed, else not.
     * @return the updated {@link Entity}
     */
    public Entity<T> setActive(boolean active) {
        this.active = active;
        return this;
    }

    /**
     * Apply a force f to that {@link Entity}
     *
     * @param f a {@link Point2D} force to be applied.
     * @return the updated {@link Entity}.
     */
    public Entity<T> addForce(Vector2d f) {
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
        infos.add(String.format("pos:%s", position));
        infos.add(String.format("size:%s", size));
        infos.add(String.format("spd:%s", velocity));
        infos.add(String.format("acc:%s", acceleration));
        infos.add(String.format("mat:%s", material));

        return infos;
    }

    /**
     * Update the bounding box {@link Shape} according to the position
     * and size of the {@link Entity}.
     */
    public void updateBox() {
        this.box = new Rectangle2D.Double(position.x, position.y, size.x, size.y);
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

    /**
     * Set the name of the {@link Entity}.
     *
     * @param entityName the new name of te {@link Entity}
     * @return the updated {@link Entity}.
     */
    public Entity<T> setName(String entityName) {
        this.name = entityName;
        return this;
    }

    /**
     * Return true if this {@link Entity} instance must be stick to the {@link Camera} viewport.
     *
     * @return true if this {@link Entity} is stick to {@link Camera} viewport else false.
     */
    public boolean isNotStickToCamera() {
        return !stickToCamera;
    }

    public boolean isActive() {
        return active;
    }

    public double getMass() {
        return mass;
    }

    public PhysicType getPhysicType() {
        return physicType;
    }

    public Entity setContact(int c) {
        this.contact = c;
        return this;
    }

    public int getContact() {
        return contact;
    }

    public int getDebug() {
        return debug;
    }

    public int getId() {
        return id;
    }

    public List<Vector2d> getForces() {
        return this.forces;
    }

    public Color getFillColor() {
        return this.fillColor;
    }

    public Shape getBox() {
        return this.box;
    }

    public Color getBorderColor() {
        return borderColor;
    }

    public Vector2d getPosition() {
        return this.position;
    }

    public Shape getCollisionBox() {
        return box;
    }

    public Vector2d getSize() {
        return size;
    }

    public Vector2d getAcceleration() {
        return acceleration;
    }

    public Entity<T> setAcceleration(Vector2d a) {
        this.acceleration = a;
        return this;
    }

    public Vector2d getVelocity() {
        return velocity;
    }

    public Entity<T> setVelocity(Vector2d v) {
        this.velocity = v;
        return this;
    }

    public Entity<T> setVelocity(double vx, double vy) {
        this.velocity = new Vector2d(vx, vy);
        return this;
    }

    public Entity<T> setPosition(Vector2d p) {
        this.position = p;
        return this;
    }
}
