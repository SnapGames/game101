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

    public String type;

    public double direction;
    public BufferedImage image;

    public GameObject(String name) {
        super(name);
        this.mass = 1.0;
        this.type = Rectangle2D.class.getTypeName();
    }

    public Entity<GameObject> setType(Class<?> t) {
        this.type = t.getTypeName();
        updateBox();
        return this;
    }

    @Override
    public void updateBox() {
        switch (type) {
            case "java.awt.geom.Rectangle2D",
                    "java.awt.image.BufferedImage" -> {
                this.box = new Rectangle2D.Double(x, y, width, height);
            }
            case "java.awt.geom.Ellipse2D" -> {
                this.box = new Ellipse2D.Double(x, y, width, height);
            }
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
        infos.add(String.format("(2)type:%s", type.substring(type.lastIndexOf(".") + 1)));
        infos.add(String.format("(2)pos:%4.2f,%4.2f", x, y));
        infos.add(String.format("(2)size:%4.2f,%4.2f", width, height));
        infos.add(String.format("(3)spd:%4.2f,%4.2f", dx, dy));
        infos.add(String.format("(3)acc:%4.2f,%4.2f", ax, ay));
        if (Optional.ofNullable(material).isPresent()) {
            infos.add(String.format("(4)map:%s[d=%4.2f,e=%4.2f,f=%4.2f]",
                    material.name,
                    material.density,
                    material.elasticity,
                    material.friction));
        }

        return infos;
    }

}
