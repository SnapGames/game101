package fr.snapgames.demo.core.physic;

import fr.snapgames.demo.core.math.Vector2D;

import java.util.Objects;

/**
 * A {@link Material} describes a set of Physic attributes to ba applied to an
 * {@link fr.snapgames.demo.core.entity.Entity} to feed {@link PhysicEngine} processing.
 *
 * @author Frédéric Delorme
 * @since 0.0.8
 */
public class Material {
    /**
     * A list of default {@link Material}s ready to be applied to {@link fr.snapgames.demo.core.entity.Entity}.
     */
    public static final Material DEFAULT =
            new Material("default", 0.0, 1.0, 1.0);
    public static final Material AIR =
            new Material("air", 0.1, 0.01, 1.0);
    public static final Material RUBBER =
            new Material("rubber", 0.90, 0.7, 0.56);
    public static final Material SUPER_BALL =
            new Material("superBall", 0.98, 0.7, 0.98);
    public static final Material STEEL =
            new Material("steel", 0.25, 1.2, 0.96);
    public static final Material FLOOR =
            new Material("floor", 0.02, 0.6, 0.70);

    /**
     * Name of this Material
     */
    public String name;
    /**
     * Elasticity physic attribute for this material.
     */
    public double elasticity = 1.0;
    /**
     * Density physic attribute for this material.
     */
    public double density = 1.0;
    /**
     * Friction physic attribute for this material.
     */
    public double friction = 1.0;


    /**
     * Create a {@link Material} with default attributes values:
     * <ul>
     *     <li>density=1.0</li>
     *     <li>elasticity=1.0</li>
     *     <li>friction=1.0</li>
     * </ul>
     *
     * @param name the name for this {@link Material}.
     */
    public Material(String name) {
        this.name = name;
    }

    /**
     * Create new {@link Material}
     *
     * @param name name of this {@link Material}
     * @param e    elasticity for this new {@link Material}
     * @param d    density for this new {@link Material}
     * @param f    friction for this new {@link Material}
     */
    public Material(String name, double e, double d, double f) {
        this(name);
        withDensity(d);
        withElasticity(e);
        withFriction(f);
    }

    /**
     * Helper to set friction in a fluent API.
     *
     * @param friction value for the friction {@link Material}'s attribute
     * @return the updated {@link Material}
     */
    public Material withFriction(double friction) {
        this.friction = friction;
        return this;
    }

    /**
     * Helper to set elasticity in a fluent API.
     *
     * @param elasticity value for the elasticity {@link Material}'s attribute
     * @return the updated {@link Material}
     */
    public Material withElasticity(double elasticity) {
        this.elasticity = elasticity;
        return this;
    }

    /**
     * Helper to set density in a fluent API.
     *
     * @param density value for the density {@link Material}'s attribute
     * @return the updated {@link Material}
     */
    public Material withDensity(double density) {
        this.density = density;
        return this;
    }

    public String toString() {
        return String.format("[n=%s,d=%4.2f,e=%4.2f,f=%4.2f]",
                name,
                density,
                elasticity,
                friction);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (getClass() != o.getClass()) {
            return false;
        }
        Material vo = (Material) o;
        return Objects.equals(name, vo.name)
                && Objects.equals(density, vo.density)
                && Objects.equals(elasticity, vo.elasticity)
                && Objects.equals(friction, vo.friction);
    }
}
