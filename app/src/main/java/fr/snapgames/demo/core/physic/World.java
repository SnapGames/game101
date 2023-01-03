package fr.snapgames.demo.core.physic;

import fr.snapgames.demo.core.configuration.Configuration;
import fr.snapgames.demo.gdemoapp.ConfigAttribute;

import java.awt.geom.Point2D;

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
    public final Double minSpeed;
    public final Double maxSpeedX;
    public final Double maxSpeedY;
    public double maxAccX;
    public double maxAccY;
    public double minAcc;
    /**
     * Play area width
     */
    int paWidth;
    /**
     * Play area height
     */
    int paHeight;

    /**
     * The default World gravity
     */
    public Point2D gravity = new Point2D.Double(0, 0.000981);

    /**
     * Initialization of the World object to define world context for PhysicEngine.
     *
     * @param configuration
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
        paWidth = (int) configuration.get(ConfigAttribute.PLAY_AREA_WIDTH);
        paHeight = (int) configuration.get(ConfigAttribute.PLAY_AREA_HEIGHT);
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
     */
    public void setGravity(Point2D g) {
        this.gravity = g;
    }
}
