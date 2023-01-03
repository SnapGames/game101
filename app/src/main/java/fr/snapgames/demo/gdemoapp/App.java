/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package fr.snapgames.demo.gdemoapp;

import fr.snapgames.demo.core.Game;
import fr.snapgames.demo.core.Utils;
import fr.snapgames.demo.core.configuration.Configuration;
import fr.snapgames.demo.core.entity.Entity;
import fr.snapgames.demo.core.entity.EntityManager;
import fr.snapgames.demo.core.gfx.Renderer;
import fr.snapgames.demo.core.gfx.Window;
import fr.snapgames.demo.core.io.InputHandler;
import fr.snapgames.demo.core.physic.Material;
import fr.snapgames.demo.core.physic.PhysicEngine;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
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
     * Configuration attributes map.
     */
    private Map<ConfigAttribute, Object> configurationValues = new ConcurrentHashMap<>();

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
    private String appTitle = "GDemoApp";

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
        System.out.printf("Start %s%n- initializing...%n", getAppName());

        appStartTime = System.currentTimeMillis();

        int initStatus = applyConfiguration(args);
        // initialize your system and services from here
        window = new Window(
                (String) config.get(ConfigAttribute.APP_TITLE),
                (int) config.get(ConfigAttribute.WINDOW_WIDTH),
                (int) config.get(ConfigAttribute.WINDOW_HEIGHT));
        inputHandler = new InputHandler();
        window.addListener(inputHandler);
        entityMgr = new EntityManager();
        renderer = new Renderer(this);
        physicEngine = new PhysicEngine(this);

        logger.log(Level.INFO, "- initialization done.");
        return initStatus;
    }

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
        updateTestCounter = 0;
    }

    @Override
    public void create() {
        logger.log(Level.INFO, "- create stuff for {0}", getAppName());
        int screenWidth = (int) config.get(ConfigAttribute.SCREEN_WIDTH);
        int screenHeight = (int) config.get(ConfigAttribute.SCREEN_HEIGHT);
        entityMgr.add(
            new Entity("player")
                .setFillColor(Color.RED)
                .setBorderColor(Color.WHITE)
                .setSize(16.0, 16.0)
                .setPosition((screenWidth - 32) * 0.5, (screenHeight - 32) * 0.5)
                .setSpeed(0.0, 0.0)
                .setAcceleration(0.0, 0.0)
                .setDebug(2)
                .setMaterial(Material.RUBBER));

        for (int t = 0; t < 30; t++) {
            entityMgr.add(
                new Entity("ball_" + t)
                    .withFillColor(Color.BLUE)
                    .withBorderColor(Color.CYAN)
                    .withMass(0.01)
                    .setSize(8.0, 8.0)
                    .setPosition(
                        Math.random() * screenWidth,
                        Math.random() * screenHeight)
                    .setSpeed(
                        Math.random() * 20.0,
                        Math.random() * 20.0)
                    .setAcceleration(0.0, 0.0)
                    .setDebug(2)
                    .setMaterial(Material.SUPER_BALL));
        }
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
        double moveStep = 0.00001;

        Entity player = entityMgr.get("player");
        if (inputHandler.getKey(KeyEvent.VK_UP)) {
            player.addForce(new Point2D.Double(0.0, -moveStep * 1000.0));
            move = true;
        }
        if (inputHandler.getKey(KeyEvent.VK_DOWN)) {
            player.addForce(new Point2D.Double(0.0, moveStep));
            move = true;
        }
        if (inputHandler.getKey(KeyEvent.VK_LEFT)) {
            player.addForce(new Point2D.Double(-moveStep, 0.0));
            move = true;
        }
        if (inputHandler.getKey(KeyEvent.VK_RIGHT)) {
            player.addForce(new Point2D.Double(moveStep, 0.0));
            move = true;
        }
        if (!move) {
            if (Optional.ofNullable(player.material).isPresent()) {
                player.dx *= player.material.friction;
                player.dy *= player.material.friction;
                player.ax = 0.0;
                player.ay = 0.0;
            }
        }
    }

    @Override
    public void update(Game g, double elapsed) {
        logger.log(Level.INFO, "  - update thing {0}", elapsed);
        updateTestCounter += 1;
        physicEngine.update(elapsed);
    }

    @Override
    public void render(Game g, int fps) {
        logger.log(Level.INFO, "  - render thing at {0} FPS", fps);

        renderer.draw();
        renderer.drawToWindow(window);
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

    /**
     * Request exit from this Application.
     *
     * @param exit boolean true to exit.
     */
    private void requestExit(boolean exit) {
        this.exitFlag = exit;
    }

    /**
     * retrieve internal loop counter (for debug &gt; test mode only).
     *
     * @return the value of the internal loop counter.
     */
    public long getUpdateTestCounter() {
        return updateTestCounter;
    }

    /**
     * return the level of debug mode.
     *
     * @return the level of debug from 0 to 5.
     */
    public int getDebugMode() {
        return debugMode;
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
     * @return
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

}
