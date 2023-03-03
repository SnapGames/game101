package fr.snapgames.demo.gdemoapp.scenes;

import fr.snapgames.demo.core.Game;
import fr.snapgames.demo.core.entity.*;
import fr.snapgames.demo.core.gfx.RandomColor;
import fr.snapgames.demo.core.gfx.Renderer;
import fr.snapgames.demo.core.io.ResourceManager;
import fr.snapgames.demo.core.math.Vector2d;
import fr.snapgames.demo.core.physic.Material;
import fr.snapgames.demo.core.scene.AbstractScene;
import fr.snapgames.demo.gdemoapp.ConfigAttribute;
import fr.snapgames.demo.gdemoapp.io.events.DemoKeyListener;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
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


    @Override
    public void prepare(Game g) {
        imageBackground = ResourceManager.getImage("/images/backgrounds/forest.jpg");
        imagePlayer = ResourceManager.getImage("/images/sprites01.png");
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
        DemoKeyListener.addNewBalls(game,
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

        // add specific Listener for this Demo.
        game.getInputHandler().addListener(new DemoKeyListener(game));
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
    public static void createBalls(Game game, String ballNamePrefix,
                                   int nbBall,
                                   double ballRadius,
                                   int width, int height,
                                   Color fillColor, Color borderColor) {
        for (int i = 0; i < nbBall; i++) {
            double radius = Math.random() * ballRadius;
            game.getEntityManager().add(createBall(ballNamePrefix,
                    width, height,
                    (fillColor == null
                            ? RandomColor.get(
                            0.0f, 0.0f, 0.0f, 0.5f,
                            1.0f, 1.0f, 1.0f, 1.0f)
                            : fillColor),
                    borderColor,
                    radius));
        }
    }

    public static GameObject createBall(String name, int width, int height, Color fillColor, Color borderColor, double radius) {
        GameObject go = (GameObject) new GameObject()
                .setType(ObjectType.ELLIPSE)
                .setFillColor(fillColor)
                .setBorderColor(borderColor)
                .setSize(radius, radius)
                .setPosition((width - 32) * Math.random(), (height - 32) * Math.random())
                .setSpeed(10.0 * Math.random(), 10.0 * Math.random())
                .setMass(5.0 + 20.0 * Math.random())
                .setAcceleration(0.0, 0.0)
                .setDebug(4)
                .setMaterial(Material.SUPER_BALL)
                .setLayer(3)
                .setPriority(2);
        String ballName = name.replace("#", "" + go.getId());
        go.setName(ballName);
        return go;
    }


    @Override
    public void input(Game g) {


        boolean move = false;
        double accelerationStep = 50.0;
        double jumpFactor = 0.5 * accelerationStep;

        GameObject player = (GameObject) entityMgr.get("player");

        if (inputHandler.getKey(KeyEvent.VK_UP)) {
            player.addForce(new Vector2d(0.0, -jumpFactor));
            move = true;
        }
        if (inputHandler.getKey(KeyEvent.VK_DOWN)) {
            player.addForce(new Vector2d(0.0, accelerationStep));
            move = true;
        }
        if (inputHandler.getKey(KeyEvent.VK_LEFT)) {
            player.addForce(new Vector2d(-accelerationStep, 0.0));
            player.setDirection(-1.0);
            move = true;
        }
        if (inputHandler.getKey(KeyEvent.VK_RIGHT)) {
            player.addForce(new Vector2d(accelerationStep, 0.0));
            player.setDirection(1.0);
            move = true;
        }

    }


    @Override
    public void update(Game g, double elapsed) {

    }

    @Override
    public void draw(Game g, Renderer r) {

    }
}
