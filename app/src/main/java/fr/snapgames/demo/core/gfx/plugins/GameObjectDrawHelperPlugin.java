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
            case IMAGE -> {
                if (go.direction > 0) {
                    g.drawImage(go.image, (int) go.position.x, (int) go.position.y, null);
                } else {
                    g.drawImage(go.image,
                            (int) (go.position.x + go.size.x), (int) go.position.y, -(int) go.size.x, (int) go.size.y,
                            null);

                }
            }
            case POINT, RECTANGLE, ELLIPSE -> {
                if (Optional.ofNullable(go.fillColor).isPresent()) {
                    g.setColor(go.fillColor);
                    g.fill(go.box);
                }
                if (Optional.ofNullable(go.borderColor).isPresent()) {
                    g.setColor(go.borderColor);
                    g.draw(go.box);
                }
            }
            case LINE -> {
                if (Optional.ofNullable(go.borderColor).isPresent()) {
                    g.setColor(go.borderColor);
                    g.drawLine((int) go.position.x, (int) go.position.y,
                            (int) (go.position.x + go.size.x), (int) (go.position.y + go.size.y));
                }
            }
            default -> {
                // nothing to test here
            }
        }
    }

}

