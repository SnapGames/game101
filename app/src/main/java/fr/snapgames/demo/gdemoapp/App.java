/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package fr.snapgames.demo.gdemoapp;

import fr.snapgames.demo.core.Game;
import fr.snapgames.demo.core.Utils;
import fr.snapgames.demo.core.configuration.Configuration;
import fr.snapgames.demo.core.entity.*;
import fr.snapgames.demo.core.events.CommonGameKeyListener;
import fr.snapgames.demo.core.gfx.Renderer;
import fr.snapgames.demo.core.gfx.Window;
import fr.snapgames.demo.core.io.InputHandler;
import fr.snapgames.demo.core.physic.Material;
import fr.snapgames.demo.core.physic.PhysicEngine;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * Define a common Game interface with some default implementation for main core
 * function.
 * <p>
 * Basically the Game is started by a call to the run() method, and :
 * <ul>
 * <li>run first execute the {@link Game#initialize(String[])},</li>
 * <li>and if initialization is ok (=0), call {@link Game#create()},</li>
 * <li>then call the {@link Game#loop()}, until a {@link Game#isExitRequested()}
 * become true</li>
 * <li>and finally call the #{@link Game#dispose()} to free all reserved
 * resources.</li>
 * </ul>
 *
 * @author Frédéric Delorme
 * @since 0.0.1
 */
public class App implements Game {

    // Logger management
    private static final LogManager logManager = LogManager.getLogManager();
    private static final Logger logger = Logger.getLogger(App.class.getName());

    static {
        try {
            logManager.readConfiguration(App.class.getResourceAsStream("/logging.properties"));
        } catch (IOException exception) {
            logger.log(Level.SEVERE, "Cannot read configuration file", exception);
        }
    }

    // Configuration attributes
    Configuration config = new Configuration(ConfigAttribute.values());

    /**
     * Internal loop counter
     */
    private long updateTestCounter = -1;
    /**
     * the target exit value for loop counter in test mode.
     */
    private long exitValueTestCounter = -1;

    /**
     * internal time tracking counter.
     */
    private long appStartTime = 0;

    /**
     * exit required if true
     */
    private boolean exitFlag = false;

    /**
     * pause required if true
     */
    private boolean pauseFlag = false;

    /**
     * Debug mode activation if greater than 0.
     */
    private int debugMode = 0;
    /**
     * targeted frame per second for rendering and loop processing.
     */
    private int targetFPS = 60;

    private int targetUPS = 60;

    /**
     * Window to display our game app.
     */
    private Window window;

    /**
     * The InputHandler to support mouse and key events
     */
    private InputHandler inputHandler;

    /**
     * The Renderer service to use to draw ion window.
     */
    private Renderer renderer;

    /**
     * Entity manager to hold al the game entities
     */
    private EntityManager entityMgr;

    /**
     * Physic computation engine
     */
    private PhysicEngine physicEngine;

    /**
     * Displayed application title on the screen/window.
     */
    private String appTitle = "Game101";

    /**
     * Default application constructor.
     */
    public App() {
        config.setConfigurationFile("/config.properties");
    }

    /**
     * a specific constructor for test and debug mode.
     *
     * @param configFile the configuration file to be loaded.
     */
    public App(String configFile) {
        config.setConfigurationFile(configFile);
    }

    @Override
    public String getAppName() {
        return appTitle;
    }

    @Override
    public int initialize(String[] args) {
        logger.log(Level.INFO, "Start {0}%n- initializing...%n", getAppName());

        appStartTime = System.currentTimeMillis();

        int initStatus = applyConfiguration(args);
        // initialize your system and services from here
        window = new Window(
                (String) config.get(ConfigAttribute.APP_TITLE),
                (int) config.get(ConfigAttribute.WINDOW_WIDTH),
                (int) config.get(ConfigAttribute.WINDOW_HEIGHT))
                .setIcon("/images/sg-logo-image.png");

        inputHandler = new InputHandler();
        inputHandler.addListener(new CommonGameKeyListener(this));
        window.addListener(inputHandler);

        entityMgr = new EntityManager();
        renderer = new Renderer(this);
        physicEngine = new PhysicEngine(this);

        logger.log(Level.INFO, "- initialization done.");
        return initStatus;
    }

    /**
     * Request configuration file all the config values to initialize internals.
     * It also parses the provided CLI args to override configuration value if
     * necessary.
     *
     * @param args a String array containing the CLI arguments from the main method.
     * @return 0 is ok, other value is an issue.
     */
    public int applyConfiguration(String[] args) {
        int initStatus = config.parseConfigFile();
        if (initStatus == 0) {
            extractConfigurationValues();
            logger.log(Level.INFO, "Start {0}", getAppName());
            logger.log(Level.INFO, "- initializing...");
            initStatus = config.parseArgs(args);
            if (initStatus == 0) {
                extractConfigurationValues();
            }
        }
        return initStatus;
    }

    /**
     * from loaded file, extract configuration values ad set corresponding internal
     * App attributes.
     */
    private void extractConfigurationValues() {
        appTitle = (String) config.get(ConfigAttribute.APP_TITLE);
        debugMode = (int) config.get(ConfigAttribute.DEBUG_MODE);
        exitValueTestCounter = (int) config.get(ConfigAttribute.EXIT_TEST_COUNT_FRAME);
        targetFPS = (int) config.get(ConfigAttribute.RENDER_FPS);
        targetUPS = (int) config.get(ConfigAttribute.PHYSIC_UPS);
        updateTestCounter = 0;
    }

    @Override
    public void create() {
        logger.log(Level.INFO, "- create stuff for {0}", getAppName());
        int screenWidth = (int) config.get(ConfigAttribute.SCREEN_WIDTH);
        int screenHeight = (int) config.get(ConfigAttribute.SCREEN_HEIGHT);
        // Create the main player entity.
        var player = (GameObject) new GameObject("player")
                .setType(ObjectType.RECTANGLE)
                .setFillColor(Color.RED)
                .setBorderColor(new Color(0.3f, 0.0f, 0.0f))
                .setSize(16.0, 16.0)
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
                        .setSize(
                                physicEngine.getWorld().getPlayArea().getWidth(),
                                physicEngine.getWorld().getPlayArea().getHeight())
                        .setBorderColor(Color.DARK_GRAY)
                        .setLayer(-1));
    }

    /**
     * Create <code>nbBall</code> Ball GameObject with a <code>ballRadius</code>
     * maximum radius and spread
     * randomly all over a <code>width</code> x <code>height</code> display area,
     * drawn with a <code>fillColor</code> and <code>borderColor</code>.
     *
     * @param nbBall      the number of ball object to be added
     * @param ballRadius  the ball maximum radius
     * @param width       the width of the area where to randomly generate ball
     * @param height      the height of the area where to randomly generate ball
     * @param fillColor   the fill color for the ball rendering
     * @param borderColor the border color for the ball rendering
     */
    private void createBlueBalls(String ballNamePrefix,
            int nbBall,
            double ballRadius,
            int width, int height,
            Color fillColor, Color borderColor) {
        for (int i = 0; i < nbBall; i++) {
            double radius = Math.random() * ballRadius;
            createBall(ballNamePrefix, width, height, fillColor, borderColor, radius);
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
                .setMass(20.0 * Math.random())
                .setAcceleration(0.0, 0.0)
                .setDebug(4)
                .setMaterial(Material.SUPER_BALL)
                .setLayer(2)
                .setPriority(2);
        String ballName = name.replace("#", "" + go.id);
        go.setName(ballName);
        entityMgr.add(go);
    }

    @Override
    public void input(Game g) {
        logger.log(Level.INFO, "- Loop {0}:", updateTestCounter);
        logger.log(Level.INFO, "  - handle input");
        if (inputHandler.isKeyPressed(KeyEvent.VK_ESCAPE)) {
            requestExit(true);
            logger.log(Level.FINEST, "    - key {} has been released",
                    new Object[] { KeyEvent.getKeyText(KeyEvent.VK_ESCAPE) });
        }

        boolean move = false;
        double accelerationStep = 200.0;
        double jumpFactor = 0.5 * accelerationStep;

        Entity<?> player = entityMgr.get("player");

        if (inputHandler.getKey(KeyEvent.VK_UP)) {
            player.addForce(new Point2D.Double(0.0, -jumpFactor));
            move = true;
        }
        if (inputHandler.getKey(KeyEvent.VK_DOWN)) {
            player.addForce(new Point2D.Double(0.0, accelerationStep));
            move = true;
        }
        if (inputHandler.getKey(KeyEvent.VK_LEFT)) {
            player.addForce(new Point2D.Double(-accelerationStep, 0.0));
            move = true;
        }
        if (inputHandler.getKey(KeyEvent.VK_RIGHT)) {
            player.addForce(new Point2D.Double(accelerationStep, 0.0));
            move = true;
        }
        if (!move) {
            if (Optional.ofNullable(player.material).isPresent()) {
                player.dx *= player.material.friction;
                player.dy *= player.material.friction;
            }
        }

        // Managing Balls
        if (inputHandler.getKey(KeyEvent.VK_PAGE_UP)) {
            // maximize number of managed entities.
            if (getEntityManager().getEntities().size() < 2000) {
                addNewBalls("ball_#", 10);
            }
        }
        if (inputHandler.getKey(KeyEvent.VK_PAGE_DOWN)) {
            removeNbObjectByNameFilter("ball_", 10);
        }
        if (inputHandler.getKey(KeyEvent.VK_DELETE)) {
            removeAllObjectByNameFilter("ball_");
        }
    }

    /**
     * Remove a number of {@link Entity} based on a filtering name.
     *
     * @param objectName the name to filter entities on.
     * @param nb         the number of object to be removed.
     */
    private void removeNbObjectByNameFilter(String objectName, int nb) {
        List<Entity<?>> toBeRemoved = new ArrayList<>();
        int count = 0;
        for (Entity<?> e : getEntityManager().getEntities()) {
            if (e.getName().contains(objectName)) {
                toBeRemoved.add(e);
                count++;
                if (count > nb) {
                    break;
                }
            }
        }
        toBeRemoved.forEach(e -> {
            getEntityManager().getEntityMap().remove(e.getName());
        });
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
        createBlueBalls(objectName, nb,
                24.0,
                screenWidth,
                screenHeight,
                Color.CYAN,
                Color.BLUE);
    }

    /**
     * Remove all {@link Entity} based on a filtering name.
     *
     * @param objectName
     */
    private void removeAllObjectByNameFilter(String objectName) {
        List<Entity<?>> toBeRemoved = new ArrayList<>();
        for (Entity<?> e : getEntityManager().getEntities()) {
            if (e.getName().contains(objectName)) {
                toBeRemoved.add(e);
            }
        }
        toBeRemoved.forEach(e -> {
            getEntityManager().getEntityMap().remove(e.getName());
        });
    }

    @Override
    public void update(Game g, Map<String, Object> attributes, double elapsed) {
        int ups = (int) attributes.get("game.ups");
        logger.log(Level.INFO, "  - update thing {0}", elapsed);
        updateTestCounter += 1;
        physicEngine.update(elapsed);
    }

    @Override
    public void render(Game g, Map<String, Object> attributes) {
        int fps = (int) attributes.get("game.fps");
        logger.log(Level.INFO, "  - render thing at {0} FPS", fps);

        renderer.draw(attributes);
        renderer.drawToWindow(window);
    }

    @Override
    public int getTargetUps() {
        return targetUPS;
    }

    @Override
    public int getTargetFps() {
        return targetFPS;
    }

    @Override
    public void dispose() {
        if (debugMode > 0) {
            logger.log(Level.INFO, "debugMode={0}: Main game loop executed {1} times (as required {2}).", new Object[] {
                    debugMode,
                    updateTestCounter,
                    exitValueTestCounter });
        }
        window.close();
        long duration = System.currentTimeMillis() - appStartTime;
        logger.log(Level.INFO, "executed in {0} ms ({1})", new Object[] { duration, Utils.formatDuration(duration) });
        logger.log(Level.INFO, "End of {0}", getAppName());
    }

    @Override
    public boolean isPaused() {
        return pauseFlag;
    }

    @Override
    public boolean isExitRequested() {
        return (debugMode > 0 && updateTestCounter != -1 && updateTestCounter == exitValueTestCounter) || exitFlag;
    }

    @Override
    public Configuration getConfiguration() {
        return config;
    }

    @Override
    public EntityManager getEntityManager() {
        return entityMgr;
    }

    @Override
    public void requestExit(boolean exit) {
        this.exitFlag = exit;
    }

    @Override
    public void setDebugMode(int d) {
        debugMode = d;
    }

    @Override
    public void requestPause(boolean pause) {
        pauseFlag = pause;
    }

    /**
     * retrieve internal loop counter (for debug &gt; test mode only).
     *
     * @return the value of the internal loop counter.
     */
    public long getUpdateTestCounter() {
        return updateTestCounter;
    }

    @Override
    public int getDebugMode() {
        return debugMode;
    }

    @Override
    public double getFPS() {
        return targetFPS;
    }

    @Override
    public PhysicEngine getPhysicEngine() {
        return physicEngine;
    }

    @Override
    public Window getWindow() {
        return window;
    }

    /**
     * return the trigger value to exit from loop ni test mode.
     *
     * @return the value of the test max number of loop.
     */
    public long getExitValueTestCounter() {
        return exitValueTestCounter;
    }

    /**
     * Retrieve the time elapsed since initialization start.
     *
     * @return the time elapsed since initialization start
     */
    public long getInternalTime() {
        return System.currentTimeMillis() - appStartTime;
    }

    /**
     * The main entry for our application.
     *
     * @param args the list of args communicated by CLI
     */
    public static void main(String[] args) {
        App app = new App();
        app.run(args);
    }

    public Renderer getRenderer() {
        return this.renderer;
    }
}
