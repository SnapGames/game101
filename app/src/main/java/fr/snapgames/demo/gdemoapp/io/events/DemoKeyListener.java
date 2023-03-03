package fr.snapgames.demo.gdemoapp.io.events;

import fr.snapgames.demo.core.Game;
import fr.snapgames.demo.core.configuration.Configuration;
import fr.snapgames.demo.core.entity.Entity;
import fr.snapgames.demo.core.entity.EntityManager;
import fr.snapgames.demo.core.entity.GameObject;
import fr.snapgames.demo.core.math.Vector2d;
import fr.snapgames.demo.gdemoapp.ConfigAttribute;
import fr.snapgames.demo.gdemoapp.scenes.DemoScene;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

public class DemoKeyListener implements KeyListener {

    private final Game game;
    private Configuration config;

    public DemoKeyListener(Game g) {
        this.game = g;
        this.config = game.getConfiguration();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Managing Balls
        switch (e.getKeyCode()) {
            case KeyEvent.VK_PAGE_UP -> {
                // maximize number of managed entities.
                if (game.getEntityManager().getEntities().size() < 2000) {
                    addNewBalls(game, "ball_#", 10);
                }
            }
            case KeyEvent.VK_PAGE_DOWN -> {
                removeNbObjectByNameFilter("ball_", 10);
            }
            case KeyEvent.VK_DELETE -> {
                removeAllObjectByNameFilter("ball_");
            }
            case KeyEvent.VK_R -> {
                reshuffleEntityByName("ball_", (Double) config.get(ConfigAttribute.GAME_RESHUFFLE_FORCE));
            }
            default -> {
            }
        }

    }

    /**
     * Add new Balls in the view
     *
     * @param objectName base name for the new balls
     * @param nb         the number of balls to create.
     */
    public static void addNewBalls(Game game, String objectName, int nb) {
        Configuration config = game.getConfiguration();
        int screenWidth = (int) config.get(ConfigAttribute.SCREEN_WIDTH);
        int screenHeight = (int) config.get(ConfigAttribute.SCREEN_HEIGHT);
        DemoScene.createBalls(game, objectName, nb,
                24.0,
                screenWidth,
                screenHeight,
                null,
                Color.BLACK);
    }

    /**
     * Remove a number of {@link Entity} based on a filtering name.
     *
     * @param objectName the name to filter entities on.
     * @param nb         the number of object to be removed.
     */
    private void removeNbObjectByNameFilter(String objectName, int nb) {
        java.util.List<Entity<?>> toBeRemoved = new ArrayList<>();
        int count = 0;
        for (Entity<?> e : game.getEntityManager().getEntities()) {
            if (e.getName().contains(objectName)) {
                toBeRemoved.add(e);
                count++;
                if (count > nb) {
                    break;
                }
            }
        }
        toBeRemoved.forEach(e -> game.getEntityManager().getEntityMap().remove(e.getName()));
    }


    /**
     * Remove all {@link Entity} based on a filtering name.
     *
     * @param objectNameFilter the object name filter used to remove corresponding {@link GameObject}.
     */
    private void removeAllObjectByNameFilter(String objectNameFilter) {
        List<Entity<?>> toBeRemoved = new ArrayList<>();
        for (Entity<?> e : game.getEntityManager().getEntities()) {
            if (e.getName().contains(objectNameFilter)) {
                toBeRemoved.add(e);
            }
        }
        toBeRemoved.forEach(e -> game.getEntityManager().getEntityMap().remove(e.getName()));
    }


    private void reshuffleEntityByName(String filterEntities, double maxForce) {

        EntityManager emgr = game.getEntityManager();
        emgr.getEntities()
                .stream()
                .filter(o -> o.getName().contains(filterEntities))
                .forEach(go -> go.getForces().add(
                        new Vector2d(
                                (maxForce * 2.0 * Math.random()) - maxForce,
                                (maxForce * 10.0 * Math.random()) - (maxForce * 5.0))));
    }
}