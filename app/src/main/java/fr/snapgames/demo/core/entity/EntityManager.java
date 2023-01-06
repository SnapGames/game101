package fr.snapgames.demo.core.entity;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The {@link EntityManager} will hold all the {@link Entity} in a central map.
 *
 * @author Frédéric Delorme
 * @since 0.0.7
 */
public class EntityManager {
    /**
     * internal map of handled {@link Entity}.
     */
    Map<String, Entity> entities = new ConcurrentHashMap<>();

    /**
     * Create the Entity Manager.
     */
    public EntityManager() {
    }

    /**
     * retrieved the named {@link Entity}.
     *
     * @param name the name of the {@link Entity} to be retrieved from manager.
     * @return the Entity corresponding to the name.
     */
    public Entity get(String name) {
        return entities.get(name);
    }

    /**
     * Add an {@link Entity} to the Manager's internal Map.
     *
     * @param e the entity to be added.
     */
    public void add(Entity e) {
        entities.put(e.getName(), e);
    }

    /**
     * Return the full Map of entities.
     *
     * @return the map of all handled entities.
     */
    public Map<String, Entity> getEntityMap() {
        return entities;
    }

    /**
     * Return a collection of the handled {@link Entity}'s.
     *
     * @return the current collection of {@link Entity}.
     */
    public Collection<Entity> getEntities() {
        return entities.values();
    }
}
