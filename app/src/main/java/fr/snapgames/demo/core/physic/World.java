package fr.snapgames.demo.core.physic;

import fr.snapgames.demo.core.configuration.Configuration;
import fr.snapgames.demo.gdemoapp.ConfigAttribute;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * The {@link World} object intends to provide some world default attribute to configure the {@link PhysicEngine}
 * with some world game world.
 *
 * @author Frédéric Delorme
 * @since 0.0.8
 */
public class World {
    /**
     * Define World physic attributes limits.
     */
    public double minSpeed = 0.001;
    public double maxSpeedX = 120.0;
    public double maxSpeedY = 120.0;
    public double minAcc = 0.00001;
    public double maxAccX = 600.0;
    public double maxAccY = 600.0;
    /**
     * Play area
     */
    Rectangle2D playArea = new Rectangle2D.Double(0, 0, 320, 200);

    /**
     * The default World gravity
     */
    public Point2D gravity = new Point2D.Double(0, 0.981);

    public Material material = Material.AIR;

    /**
     * Initialization of the World object with only default values.
     */
    public World() {
    }

    /**
     * Initialization of the {@link World} object to define world context for {@link PhysicEngine}.
     *
     * @param configuration the parent {@link Configuration} instance to retrieve default World values from.
     */
    public World(Configuration configuration) {
        // get Gravity from the configuration file.
        gravity = (Point2D) configuration.get(ConfigAttribute.PHYSIC_GRAVITY);
        // get speed physic attribute limits
        minSpeed = (double) configuration.get(ConfigAttribute.PHYSIC_MIN_SPEED);
        maxSpeedX = (double) configuration.get(ConfigAttribute.PHYSIC_MAX_SPEED_X);
        maxSpeedY = (double) configuration.get(ConfigAttribute.PHYSIC_MAX_SPEED_Y);
        // get acceleration physic attribute limits
        minAcc = (double) configuration.get(ConfigAttribute.PHYSIC_MIN_ACCELERATION);
        maxAccX = (double) configuration.get(ConfigAttribute.PHYSIC_MAX_ACCELERATION_X);
        maxAccY = (double) configuration.get(ConfigAttribute.PHYSIC_MAX_ACCELERATION_Y);
        // get play area dimension
        playArea = new Rectangle2D.Double(0.0, 0.0,
                (double) configuration.get(ConfigAttribute.PLAY_AREA_WIDTH),
                (double) configuration.get(ConfigAttribute.PLAY_AREA_HEIGHT));
    }

    /**
     * Retrieve gravity for this {@link World}.
     *
     * @return a Point2D value corresponding to the World's gravity.
     */
    public Point2D getGravity() {
        return gravity;
    }

    /**
     * Define the {@link World}'s gravity with the provided {@link Point2D} value.
     *
     * @param g the {@link Point2D} gravity value to be set to that {@link World} object.
     * @return the updated {@link World} object (Fluent API)
     */
    public World setGravity(Point2D g) {
        this.gravity = g;
        return this;
    }

    /**
     * Define the default {@link Material} for this World, these characteristics
     * will be used by the {@link PhysicEngine} as a default friction.
     *
     * @param mat the {@link Material} to be applied to all {@link fr.snapgames.demo.core.entity.Entity} in this {@link World}
     * @return the updated {@link World} object (Fluent API)
     */
    public World setMaterial(Material mat) {
        this.material = mat;
        return this;
    }

    public Rectangle2D getPlayArea() {
        return playArea;
    }
}
