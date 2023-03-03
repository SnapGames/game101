package fr.snapgames.demo.core.entity;

import fr.snapgames.demo.core.behavior.Behavior;
import fr.snapgames.demo.core.physic.Material;

import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * An Entity Implementation to draw some basic Game object on screen,
 * based on geometrical shapes or a simple image.
 *
 * @author Frédéric Delorme
 * @since 0.0.9
 */
public class GameObject extends Entity<GameObject> {
    /**
     * The type of this new GameObject.
     */
    public ObjectType type;
    /**
     * the direction where this object is moving.
     */
    public double direction;
    /**
     * the possible image instance if type is set to BufferedImage.
     */
    public BufferedImage image;


    private List<Behavior<GameObject>> behaviors = new ArrayList<>();

    /**
     * Create a brand new {@link GameObject} instance with only default values.
     *
     * @see Entity
     */
    public GameObject() {
        super();
        setMass(1.0);
        setType(ObjectType.RECTANGLE);
    }

    /**
     * Create a new {@link GameObject} instance named name, and set all default
     * values.
     *
     * @param name name for that new {@link GameObject}.
     * @see Entity
     */
    public GameObject(String name) {
        super(name);
        setMass(1.0);
        setDirection(1.0);
        setType(ObjectType.RECTANGLE);
    }


    /**
     * Add a new {@link Behavior} to this entity.
     *
     * @param be the {@link Behavior} to be applied to this {@link GameObject} for input, update or draw operation.
     * @return the updated {@link GameObject}.
     * @see fr.snapgames.demo.core.physic.PhysicEngine
     */
    public GameObject add(Behavior<GameObject> be) {
        behaviors.add(be);
        return this;
    }

    /**
     * Retrieve the list of applied {@link Behavior}s on this {@link GameObject}
     *
     * @return
     */
    public List<Behavior<GameObject>> getBehaviors() {
        return this.behaviors;
    }

    /**
     * Define the {@link GameObject} type for its rendering and physic management.
     *
     * @param t the type can be one of the following: Rectangle2D, Ellipse2D,
     *          BufferedImage. the bounding box will be updated accordingly.
     * @return the updated Entity according to fluent API pattern.
     */
    public Entity<GameObject> setType(ObjectType t) {
        this.type = t;
        setDirection(1.0);
        updateBox();
        return this;
    }

    @Override
    public void updateBox() {
        switch (type) {
            case RECTANGLE, IMAGE, LINE, POINT ->
                    this.box = new Rectangle2D.Double(position.x, position.y, size.x, size.y);
            case ELLIPSE -> this.box = new Ellipse2D.Double(position.x, position.y, size.x, size.y);
            default -> {
                // nothing to do !
            }
        }
    }

    /**
     * Prepare debug info to be displayed on debug mode.
     *
     * @return a list of string for debug info.
     */
    public List<String> getDebugInfo() {
        List<String> infos = new ArrayList<>();
        infos.add(String.format("(1)id:%04d", this.getId()));
        infos.add(String.format("(1)name:%s", this.getName()));
        infos.add(String.format("(1)type:%s", type.toString()));
        infos.add(String.format("(2)pos:%s", position));
        infos.add(String.format("(2)size:%s", size));
        infos.add(String.format("(2)d:%s", direction < 0 ? "L" : "R"));
        infos.add(String.format("(3)spd:%s", velocity));
        infos.add(String.format("(3)acc:%s", acceleration));
        infos.add(String.format("(4)l:%d p:%d", getLayer(), getPriority()));
        if (Optional.ofNullable(material).isPresent()) {
            infos.add(String.format("(4)mat:%s", material));
        }

        return infos;
    }

    /**
     * Set the image for this GameObject.
     *
     * @param image the new image to set as GameObject image.
     * @return the updated GameObject thanks to fluent API.
     */
    public GameObject setImage(BufferedImage image) {
        setType(ObjectType.IMAGE);
        this.image = image;
        setSize(image.getWidth(), image.getHeight());
        updateBox();
        return this;
    }

    /**
     * Set the {@link Entity} direction (-1 (left)  to 1 (right))
     *
     * @param d the direction encoding
     * @return the updated {@link Entity}.
     */
    public GameObject setDirection(double d) {
        this.direction = d;
        return this;
    }

    public Material getMaterial() {
        return material;
    }
}
