package fr.snapgames.demo.core.behavior;

import fr.snapgames.demo.core.Game;
import fr.snapgames.demo.core.entity.Entity;

import java.awt.*;

/**
 * The Behavior interface to define specific processing on a GameEntity.
 *
 * @author Frédéric Delorme
 * @since 0.0.1
 */
public interface Behavior<T> {
    default void update(Game game, T entity, double dt) {
    }

    default void input(Game game, T entity) {
    }

    default void draw(Game game, Graphics2D g, T entity) {
    }

    default void onCollision(Game game, T collided, Entity collider) {

    }
}
