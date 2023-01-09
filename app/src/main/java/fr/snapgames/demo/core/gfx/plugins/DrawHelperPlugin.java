package fr.snapgames.demo.core.gfx.plugins;


import fr.snapgames.demo.core.entity.Entity;
import fr.snapgames.demo.core.gfx.Renderer;

import java.awt.*;

/**
 * The DrawHelperPlugin define how a helper plugin must be implemented through
 * its {@link DrawHelperPlugin#draw(Renderer, Graphics2D, Entity)} method for a specific Entity implementation.
 *
 * @param <T> the Entity Implementation manage by this draw plugin.
 * @author Frédéric Delorme
 * @see Renderer
 * @since 0.0.9
 */
public interface DrawHelperPlugin<T extends Entity<?>> {

    /**
     * return the {@link Entity} type to be managed by this plugin.
     *
     * @return the Class for the concerned {@link Entity} implementation.
     */
    Class<T> getEntityType();

    /**
     * Define the way to draw this specific {@link Entity} implementation.
     *
     * @param r the parent {@link Renderer} service
     * @param g the {@link Graphics2D} API instance to be used
     * @param e the {@link Entity} instance to be drawn.
     */
    public void draw(Renderer r, Graphics2D g, Entity<?> e);

}
