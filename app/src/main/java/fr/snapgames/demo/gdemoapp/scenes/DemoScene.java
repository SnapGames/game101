package fr.snapgames.demo.gdemoapp.scenes;

import fr.snapgames.demo.core.Game;
import fr.snapgames.demo.core.entity.*;
import fr.snapgames.demo.core.gfx.RandomColor;
import fr.snapgames.demo.core.gfx.Renderer;
import fr.snapgames.demo.core.math.Vector2D;
import fr.snapgames.demo.core.physic.Material;
import fr.snapgames.demo.core.scene.AbstractScene;
import fr.snapgames.demo.gdemoapp.ConfigAttribute;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DemoScene extends AbstractScene {

    private static Logger logger = Logger.getLogger(DemoScene.class.getName());

    BufferedImage imageBackground = null;
    BufferedImage imagePlayer = null;

    /**
     * Create the default Scene parameters
     *
     * @param g    the parent {@link Game} for that {@link fr.snapgames.demo.core.scene.Scene}
     * @param name the name fof this scene (initialize at {@link fr.snapgames.demo.core.scene.SceneManager}
     *             initialization from {@link fr.snapgames.demo.core.configuration.Configuration}.
     */
    public DemoScene(Game g, String name) {
        super(g, name);
    }


    /**
     * Load a {@link BufferedImage} from a file path.
     *
     * @param pathToImage path to the image file.
     * @return the corresponding {@link BufferedImage} instance.
     */
    private BufferedImage loadImage(String pathToImage) {
        try {
            return ImageIO.read(this.getClass().getResourceAsStream(pathToImage));
        } catch (NullPointerException | IOException e) {
            logger.severe("Unable to read image resource from " + pathToImage);
        }
        return null;
    }

    @Override
    public void prepare(Game g) {
        imageBackground = loadImage("/images/backgrounds/forest.jpg");
        imagePlayer = loadImage("/images/sprites01.png");
    }

    @Override
    public void create(Game g) {
        logger.log(Level.INFO, "- create stuff for {0}", game.getAppName());
        int screenWidth = (int) config.get(ConfigAttribute.SCREEN_WIDTH);
        int screenHeight = (int) config.get(ConfigAttribute.SCREEN_HEIGHT);

        // Add a Background image
        var background = (GameObject) new GameObject("background")
                .setImage(imageBackground)
                .setLayer(1)
                .setPriority(2);
        entityMgr.add(background);

        // Create the main player entity.
        BufferedImage playerFrame1 = imagePlayer.getSubimage(0, 0, 32, 32);
        var player = (GameObject) new GameObject("player")
                .setImage(playerFrame1)
                .setPosition((screenWidth - 32) * 0.5, (screenHeight - 32) * 0.5)
                .setSpeed(0.0, 0.0)
                .setAcceleration(0.0, 0.0)
                .setMass(80.0)
                .setDebug(1)
                .setMaterial(Material.STEEL)
                .setLayer(10)
                .setPriority(1);
        entityMgr.add(player);

        // Add some balls
        addNewBalls(
                "ball_#",
                10);

        // Add a background GridObject as re visual reference
        entityMgr.add(
                new GridObject("grid")
                        .setStepSize(16.0, 16.0)
                        .setLineStroke(new float[]{4.0f, 4.0f, 0.0f})
                        .setSize(
                                physicEngine.getWorld().getPlayArea().getWidth(),
                                physicEngine.getWorld().getPlayArea().getHeight())
                        .setBorderColor(new Color(0.1f, 0.1f, 0.1f, 0.38f))
                        .setLayer(2)
                        .setPriority(1));

        // Define the active camera
        setCamera(new Camera("cam01")
                .setTarget(player)
                .setTween(0.005)
                .setViewport(new Rectangle2D.Double(
                        0.0, 0.0,
                        screenWidth, screenHeight)));
    }


    /**
     * Create <code>nbBall</code> Ball GameObject with a <code>ballRadius</code> maximum radius and spread
     * randomly all over a <code>width</code> x <code>height</code> display area,
     * drawn with a <code>fillColor</code> and <code>borderColor</code>.
     *
     * @param nbBall      the number of ball object to be added
     * @param ballRadius  the ball maximum radius
     * @param width       the width of the area where to randomly generate ball
     * @param height      the height of the area where to randomly generate ball
     * @param fillColor   the  fill color for the ball rendering
     * @param borderColor the border color for the ball rendering
     */
    private void createBalls(String ballNamePrefix,
                             int nbBall,
                             double ballRadius,
                             int width, int height,
                             Color fillColor, Color borderColor) {
        for (int i = 0; i < nbBall; i++) {
            double radius = Math.random() * ballRadius;
            createBall(ballNamePrefix,
                    width, height,
                    (fillColor == null
                            ? RandomColor.get(
                            0.0f, 0.0f, 0.0f, 0.5f,
                            1.0f, 1.0f, 1.0f, 1.0f)
                            : fillColor),
                    borderColor,
                    radius);
        }
    }

    private void createBall(String name, int width, int height, Color fillColor, Color borderColor, double radius) {
        GameObject go = (GameObject) new GameObject()
                .setType(ObjectType.ELLIPSE)
                .setFillColor(fillColor)
                .setBorderColor(borderColor)
                .setSize(radius, radius)
                .setPosition((width - 32) * Math.random(), (height - 32) * Math.random())
                .setSpeed(10.0 * Math.random(), 10.0 * Math.random())
                .setMass(20000.0 * Math.random())
                .setAcceleration(0.0, 0.0)
                .setDebug(4)
                .setMaterial(Material.SUPER_BALL)
                .setLayer(3)
                .setPriority(2);
        String ballName = name.replace("#", "" + go.id);
        go.setName(ballName);
        entityMgr.add(go);
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
     * Add new Balls in the view
     *
     * @param objectName base name for the new balls
     * @param nb         the number of balls to create.
     */
    private void addNewBalls(String objectName, int nb) {
        int screenWidth = (int) config.get(ConfigAttribute.SCREEN_WIDTH);
        int screenHeight = (int) config.get(ConfigAttribute.SCREEN_HEIGHT);
        createBalls(objectName, nb,
                24.0,
                screenWidth,
                screenHeight,
                null,
                Color.BLACK);
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


    @Override
    public void input(Game g) {


        boolean move = false;
        double accelerationStep = 2000.0;
        double jumpFactor = 0.5 * accelerationStep;

        GameObject player = (GameObject) entityMgr.get("player");

        if (inputHandler.getKey(KeyEvent.VK_UP)) {
            player.addForce(new Vector2D(0.0, -jumpFactor));
            move = true;
        }
        if (inputHandler.getKey(KeyEvent.VK_DOWN)) {
            player.addForce(new Vector2D(0.0, accelerationStep));
            move = true;
        }
        if (inputHandler.getKey(KeyEvent.VK_LEFT)) {
            player.addForce(new Vector2D(-accelerationStep, 0.0));
            player.setDirection(-1.0);
            move = true;
        }
        if (inputHandler.getKey(KeyEvent.VK_RIGHT)) {
            player.addForce(new Vector2D(accelerationStep, 0.0));
            player.setDirection(1.0);
            move = true;
        }

        /*if (!move) {
            if (Optional.ofNullable(player.material).isPresent()) {
                player.velocity = player.velocity.multiply(player.material.friction);
            }
        }*/

        // Managing Balls
        if (inputHandler.getKey(KeyEvent.VK_PAGE_UP)) {
            // maximize number of managed entities.
            if (game.getEntityManager().getEntities().size() < 2000) {
                addNewBalls("ball_#", 10);
            }
        }
        if (inputHandler.getKey(KeyEvent.VK_PAGE_DOWN)) {
            removeNbObjectByNameFilter("ball_", 10);
        }
        if (inputHandler.getKey(KeyEvent.VK_DELETE)) {
            removeAllObjectByNameFilter("ball_");
        }
        if (inputHandler.getKey(KeyEvent.VK_R)) {
            reshuffleEntityByName("ball_", (Double) config.get(ConfigAttribute.GAME_RESHUFFLE_FORCE));
        }
    }

    private void reshuffleEntityByName(String filterEntities, double maxForce) {

        EntityManager emgr = game.getEntityManager();
        emgr.getEntities()
                .stream()
                .filter(o -> o.name.contains(filterEntities))
                .forEach(go -> go.forces.add(
                        new Vector2D(
                                (maxForce * 2.0 * Math.random()) - maxForce,
                                (maxForce * 10.0 * Math.random()) - (maxForce * 5.0))));
    }

    @Override
    public void update(Game g, double elapsed) {

    }

    @Override
    public void draw(Game g, Renderer r) {

    }
}
