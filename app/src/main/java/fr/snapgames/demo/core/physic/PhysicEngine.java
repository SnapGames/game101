package fr.snapgames.demo.core.physic;

import fr.snapgames.demo.core.Game;
import fr.snapgames.demo.core.entity.Entity;
import fr.snapgames.demo.core.math.Vector2D;

/**
 * Create a Physic Engine to compute Entity moves and behaviors.
 *
 * @author Frédéric Delorme
 * @since 0.0.8
 */
public class PhysicEngine {

    public static final double TIME_FACTOR = 0.04;
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
     * Update all {@link Entity} managed by the game, according to their own {@link Entity#physicType},
     * the fact there are {@link Entity#stickToCamera} viewport and if there are {@link Entity#active}.
     * <p>
     * If an {@link Entity} is active, not stick to {@link fr.snapgames.demo.core.entity.Camera} and based on a physic
     * type {@link PhysicType#DYNAMIC}, it is processed by the {@link PhysicEngine}.
     * <p>
     * <blockquote><em>IMPORTANT</em> A thing to take in account is the fact that a {@link PhysicEngine#TIME_FACTOR} is applied to
     * the elapsed time before computation. This time factor is useful to tune thinly the physic computation processing.
     * In a second step, it will be possible to accelerate or reduce the time speed on the game processing.
     * </blockquote>
     *
     * @param elapsed a double value for the elapsed time since previous call.
     */
    public void update(double elapsed) {
        double time = elapsed * TIME_FACTOR;
        game.getEntityManager().getEntities().stream()
                .filter(e1 -> e1.isActive() &&
                        e1.isNotStickToCamera() &&
                        e1.physicType.equals(PhysicType.DYNAMIC))
                .forEach(e2 -> {
                    updateEntity(game, e2, time);
                    constrained(game, e2);
                });
    }

    /**
     * Update one entity.
     *
     * @param game    the parent Game instance.
     * @param entity  the concerned entity.
     * @param elapsed the elapsed time since previous call.
     */
    private void updateEntity(Game game, Entity<?> entity, double elapsed) {

        double friction = entity.contact == 0 ? world.getMaterial().friction : entity.material.friction * world.getMaterial().friction;
        double density = entity.material != null ? entity.material.density : world.getMaterial().density;

        //
        // compute acceleration and apply gravity (negate because of AWT/Swing display coordinates origin)
        entity.acceleration = entity.acceleration.addAll(entity.forces);
        entity.acceleration = entity.acceleration.add(world.getGravity().negate())
                .multiply(entity.getMass()).multiply(density);
        entity.acceleration = entity.acceleration
                .ceil((double) entity.getAttribute("minAcceleration", world.minAcc))
                .maximize(
                        (double) entity.getAttribute("maxAcceleration", world.maxAccX),
                        (double) entity.getAttribute("maxAcceleration", world.maxAccY));

        // compute velocity

        entity.velocity = entity.velocity.add(entity.acceleration.multiply(elapsed))
                .multiply(friction)
                .ceil((double) entity.getAttribute("minSpeed", world.minSpeed))
                .maximize(
                        (double) entity.getAttribute("maxVelocity", world.maxSpeedX),
                        (double) entity.getAttribute("maxVelocity", world.maxSpeedY));

        // compute position
        entity.position = entity.position.add(entity.velocity.multiply(elapsed));

        // Update the bounding box accordingly to last position changes
        entity.updateBox();

        // clear forces for that entity.
        entity.forces.clear();
    }

    /**
     * Apply the play area constrains to the Entity e.
     *
     * @param game the parent Game instance.
     * @param e    the concerned entity.
     */
    private void constrained(Game game, Entity<?> e) {
        e.contact = 0;
        if (e.position.x + e.size.x > world.playArea.getWidth()) {
            e.position.x = world.playArea.getWidth() - e.size.x;
            e.contact = 1;
            e.velocity.x = thresholdMinMax(
                    -e.velocity.x * e.material.elasticity,
                    world.minSpeed,
                    world.maxSpeedX);
            e.acceleration.x = 0.0;
        }
        if (e.position.y + e.size.y > world.playArea.getHeight()) {
            e.position.y = world.playArea.getHeight() - e.size.y;
            e.contact += 2;
            e.velocity.y = thresholdMinMax(
                    -e.velocity.y * e.material.elasticity,
                    world.minSpeed,
                    world.maxSpeedY);
            e.acceleration.y = 0.0;
        }
        if (e.position.x < 0.0) {
            e.position.x = 0.0;
            e.contact += 4;
            e.velocity.x = thresholdMinMax(
                    -e.velocity.x * e.material.elasticity,
                    world.minSpeed,
                    world.maxSpeedX);
            e.acceleration.x = 0.0;

        }
        if (e.position.y < 0.0) {
            e.position.y = 0.0;
            e.contact += 8;
            e.velocity.y = thresholdMinMax(
                    -e.velocity.y * e.material.elasticity,
                    world.minSpeed,
                    world.maxSpeedY);
            e.acceleration.y = 0.0;
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
