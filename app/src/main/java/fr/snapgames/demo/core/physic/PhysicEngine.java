package fr.snapgames.demo.core.physic;

import fr.snapgames.demo.core.Game;
import fr.snapgames.demo.core.entity.Entity;

/**
 * Create a Physic Engine to compute Entity moves and behaviors.
 *
 * @author Frédéric Delorme
 * @since 0.0.8
 */
public class PhysicEngine {

    /**
     * Parent game
     */
    private final Game game;

    private World world;

    /**
     * Initialize the {@link PhysicEngine} with configuration values.
     * <ul>
     *         <li>PHYSIC_GRAVITY a Point2D value for gravity,</li>
     *         <li>PHYSIC_MIN_SPEED a double value for min speed below considered as zero,</li>
     *         <li>PHYSIC_MAX_SPEED_X the maximum speed value for X axis,</li>
     *         <li>PHYSIC_MAX_SPEED_Y the maximum speed value for Y axis.</li>
     * </ul>
     *
     * @param game the parent Game.
     */
    public PhysicEngine(Game game) {
        this.game = game;
        this.world = new World(this.game.getConfiguration());
    }

    /**
     * Update all entity managed by the game.
     *
     * @param elapsed
     */
    public void update(double elapsed) {
        game.getEntityManager().getEntities().forEach(e -> {
            updateEntity(game, e, elapsed);
            constrained(game, e, elapsed);
        });
    }

    /**
     * Update one entity.
     *
     * @param game    the parent Game instance.
     * @param e       the concerned entity.
     * @param elapsed the elapsed time since previous call.
     */
    private void updateEntity(Game game, Entity e, double elapsed) {
        double friction = 1.0;
        e.addForce(world.gravity);
        e.forces.forEach(f -> {
            e.ax += f.getX();
            e.ay += f.getY();
        });

        //e.ax = thresholdMinMax(e.ax, world.minAcc, world.maxAccX);
        //e.ay = thresholdMinMax(e.ay, world.minAcc, world.maxAccY);

        e.dx += e.ax * (0.5 * (elapsed * elapsed));
        e.dy += e.ay * (0.5 * e.mass * (elapsed * elapsed));
        e.dx = thresholdMinMax(e.dx, world.minSpeed, world.maxSpeedX);
        e.dy = thresholdMinMax(e.dy, world.minSpeed, world.maxSpeedY);

        if (e.contact != 0) {
            friction = e.material.friction;
        }

        e.x += e.dx * elapsed * friction;
        e.y += e.dy * elapsed * friction;

        e.updateBox();
        e.forces.clear();
    }

    /**
     * Apply the play area constrains to the Entity e.
     *
     * @param game    the parent Game instance.
     * @param e       the concerned entity.
     * @param elapsed the elapsed time since previous call.
     */
    private void constrained(Game game, Entity e, double elapsed) {
        e.contact = 0;
        if (e.x + e.width > world.paWidth) {
            e.x = world.paWidth - e.width;
            e.contact = 1;
            e.dx = thresholdMinMax(
                    -e.dx * e.material.elasticity,
                    world.minSpeed,
                    world.maxSpeedX);
            ;
        }
        if (e.y + e.height > world.paHeight) {
            e.y = world.paHeight - e.height;
            e.contact += 2;
            e.dy = thresholdMinMax(
                    -e.dy * e.material.elasticity,
                    world.minSpeed,
                    world.maxSpeedY);
            ;
        }
        if (e.x < 0.0) {
            e.x = 0.0;
            e.contact += 4;
            e.dx = thresholdMinMax(
                    -e.dx * e.material.elasticity,
                    world.minSpeed,
                    world.maxSpeedX);
        }
        if (e.y < 0.0) {
            e.y = 0.0;
            e.contact += 8;
            e.dy = thresholdMinMax(
                    -e.dy * e.material.elasticity,
                    world.minSpeed,
                    world.maxSpeedY);
        }
    }

    /**
     * toolbox to define and fix ceil value
     *
     * @param x    the value to "ceilled"
     * @param ceil the level of ceil to apply to x value.
     * @return value with ceil applied.
     */
    public static double ceilValue(double x, double ceil) {
        return Math.copySign((Math.abs(x) < ceil ? 0 : x), x);
    }

    /**
     * min-max-range to apply to a x value.
     *
     * @param x   the value to be constrained between min and max.
     * @param min minimum for the x value.
     * @param max maximum for the x value.
     * @return the constrained x value between max and lowered min value (is x<min, x=0).
     */
    public static double thresholdMinMax(double x, double min, double max) {
        return ceilValue(Math.copySign((Math.abs(x) > max ? max : x), x), min);
    }

    /**
     * Retrieve the {@link World} instance applied to the {@link PhysicEngine}
     *
     * @return a {@link World} instance.
     */
    public World getWorld() {
        return world;
    }
}
