package fr.snapgames.demo.core.physic;

import fr.snapgames.demo.core.Game;
import fr.snapgames.demo.core.entity.Entity;
import fr.snapgames.demo.core.entity.GameObject;
import fr.snapgames.demo.core.entity.Influencer;
import fr.snapgames.demo.core.math.Vector2d;

import java.awt.geom.Rectangle2D;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Create a Physic Engine to compute Entity moves and behaviors.
 *
 * @author Frédéric Delorme
 * @since 0.0.8
 */
public class PhysicEngine {

    public static final double TIME_FACTOR = 1.0;
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
     * Update all {@link Entity} managed by the game, according to their own {@link Entity#getPhysicType()},
     * the fact there are {@link Entity#isNotStickToCamera()}  viewport and if there are {@link Entity#isActive()}.
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
                        e1.getPhysicType().equals(PhysicType.DYNAMIC))
                .forEach(e2 -> {
                    updateEntity(game, e2, time);
                });
    }

    /**
     * Update one entity.
     *
     * @param game   the parent Game instance.
     * @param entity the concerned entity.
     * @param time   the elapsed time since previous call.
     */
    private void updateEntity(Game game, Entity<?> entity, double time) {
        double elapsed = time * TIME_FACTOR;
        // apply gravity
        entity.getForces().add(world.getGravity());
        entity.getForces().addAll(world.getForces());

        if (entity instanceof GameObject) {
            GameObject go = (GameObject) entity;
            // Apply influencer Effects (Material and force impacted)
            Material material = appliedInfluencerToEntity(go, world);            // compute acceleration
            go.setAcceleration(go.getAcceleration().addAll(entity.getForces()).multiply(material.getDensity()));
            go.setAcceleration(go.getAcceleration().multiply((double) go.getMass()));
            //entity.getAcceleration().maximize((double) entity.getAttribute("maxAcceleration", maxAcceleration));

            // compute velocity
            double roughness = go.getContact() == 0
                    ? world.getMaterial().getFriction()
                    : material.getFriction();
            go.setVelocity(go.getVelocity().add(entity.getAcceleration().multiply(elapsed)).multiply(roughness));
            //entity.speed.maximize((double) entity.getAttribute("maxVelocity", maxVelocity));

            // compute position
            go.setPosition(go.getPosition().add(go.getVelocity().multiply(elapsed)));
            //entity.getChild().forEach(c -> updateEntity(c, elapsed));
            constrainsEntity(go, material);
            // apply behaviors
            go.getBehaviors().forEach(b -> b.update(game, go, elapsed));
        }
        // reset forces
        entity.getForces().clear();
        // update the Entity's bounding box.
        entity.updateBox();
    }

    private Material appliedInfluencerToEntity(GameObject entity, World world) {
        Material material = entity.material.copy();
        List<Entity> il = game.getEntityManager().getEntities().stream()
                .filter(influencer -> influencer instanceof Influencer)
                .filter(influencer -> ((Influencer) influencer).getBox().intersects(entity.getBox().getBounds2D()))
                .collect(Collectors.toList());
        for (Entity ge : il) {
            Influencer influencer = (Influencer) ge;
            material = material.merge(influencer.getMaterial());
            entity.setContact(entity.getContact() + 16);

            influencer.getBehaviors().forEach(b -> b.onCollision(game, influencer, entity));
            Rectangle2D intersection = ge.getCollisionBox().getBounds2D().createIntersection(entity.getCollisionBox().getBounds2D());
            double factor = intersection.getHeight() - entity.getSize().getY();
            if (influencer.getForces().size() > 0) {
                influencer.getForces().stream().forEach(f ->
                        entity.getForces().add(f.multiply(entity.getMaterial().getDensity())));
            }
        }
        return material;
    }

    /**
     * Apply the play area constrains to the Entity e.
     *
     * @param e        the concerned entity.
     * @param material the retained {@link Material}.
     */
    private void constrainsEntity(Entity e, Material material) {
        e.setContact(0);
        if (e.getPosition().getY() + e.getSize().getY() >= this.world.getPlayArea().getBounds().getHeight()) {
            e.setPosition(e.getPosition().getX(), this.world.getPlayArea().getBounds().getHeight() - e.getSize().getY() - 1);
            e.setVelocity(e.getVelocity().getX(), -e.getVelocity().getY() * material.getElasticity());
            e.setContact(e.getContact() + 1);
        }
        if (e.getPosition().getX() + e.getSize().getX() >= this.world.getPlayArea().getBounds().getWidth()) {
            e.setPosition(this.world.getPlayArea().getBounds().getWidth() - e.getSize().getX() - 1, e.getPosition().getY());
            e.setVelocity(-e.getVelocity().getX() * material.getElasticity(), e.getVelocity().getY());
            e.setContact(e.getContact() + 2);
        }
        if (e.getPosition().getY() < 0) {
            e.setPosition(e.getPosition().getX(), 0);
            e.setVelocity(e.getVelocity().getX(), -e.getVelocity().getY() * material.getElasticity());
            e.setContact(e.getContact() + 4);

        }
        if (e.getPosition().getX() < 0) {
            e.setPosition(0, e.getPosition().getY());
            e.setVelocity(-e.getVelocity().getX() * material.getElasticity(), e.getVelocity().getY());
            e.setContact(e.getContact() + 8);

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
