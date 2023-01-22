package fr.snapgames.demo.core.entity;


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
     * Create a new {@link GameObject} instance named name, and set all default values.
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
     * Define the {@link GameObject} type for its rendering and physic management.
     *
     * @param t the type can be one of the following: Rectangle2D, Ellipse2D, BufferedImage. the bounding box will be updated accordingly.
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
            case RECTANGLE, IMAGE, LINE, POINT -> this.box = new Rectangle2D.Double(x, y, width, height);
            case ELLIPSE -> this.box = new Ellipse2D.Double(x, y, width, height);
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
        infos.add(String.format("(1)id:%04d", this.id));
        infos.add(String.format("(1)name:%s", this.name));
        infos.add(String.format("(2)type:%s", type.toString()));
        infos.add(String.format("(2)pos:%4.2f,%4.2f", x, y));
        infos.add(String.format("(2)size:%.0fx%.0f", width, height));
        infos.add(String.format("(3)spd:%4.2f,%4.2f", dx, dy));
        infos.add(String.format("(3)acc:%4.2f,%4.2f", ax, ay));
        infos.add(String.format("(4)l:%d p:%d", getLayer(), getPriority()));
        if (Optional.ofNullable(material).isPresent()) {
            infos.add(String.format("(4)map:%s[d=%4.2f,e=%4.2f,f=%4.2f]",
                    material.name,
                    material.density,
                    material.elasticity,
                    material.friction));
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
}
