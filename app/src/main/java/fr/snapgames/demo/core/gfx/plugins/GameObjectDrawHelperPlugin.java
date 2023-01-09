package fr.snapgames.demo.core.gfx.plugins;

import fr.snapgames.demo.core.entity.Entity;
import fr.snapgames.demo.core.entity.GameObject;
import fr.snapgames.demo.core.gfx.Renderer;

import java.awt.*;
import java.util.Optional;

/**
 * This is the {@link GameObject} rendering plugin.
 *
 * @author Frédéric Delorme
 * @see DrawHelperPlugin
 * @see Renderer
 * @see GameObject
 * @since 0.0.9
 */
public class GameObjectDrawHelperPlugin implements DrawHelperPlugin<GameObject> {

    @Override
    public Class<GameObject> getEntityType() {
        return GameObject.class;
    }

    @Override
    public void draw(Renderer r, Graphics2D g, Entity<?> entity) {
        GameObject go = (GameObject) entity;
        switch (go.type) {
            case "java.awt.geom.Rectangle2D",
                    "java.awt.geom.Ellipse2D" -> {
                if (Optional.ofNullable(go.fillColor).isPresent()) {
                    g.setColor(go.fillColor);
                    g.fill(go.box);
                }
                if (Optional.ofNullable(go.borderColor).isPresent()) {
                    g.setColor(go.borderColor);
                    g.draw(go.box);
                }
            }
            case "java.awt.image.BufferedImage" -> {
                if (go.direction > 0) {
                    g.drawImage(go.image, (int) go.x, (int) go.y, null);
                } else {
                    g.drawImage(go.image,
                            (int) (go.x + go.width), (int) go.y, (int) go.x, (int) (go.y + go.height),
                            (int) go.x, (int) go.y, (int) go.width, (int) go.height,
                            null);

                }
            }
            default -> {
                // Nothing to do in that case.
            }
        }
    }

}

