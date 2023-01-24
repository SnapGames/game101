package fr.snapgames.demo.core.scene;

import fr.snapgames.demo.core.Game;
import fr.snapgames.demo.core.configuration.Configuration;
import fr.snapgames.demo.core.entity.Camera;
import fr.snapgames.demo.core.entity.Entity;
import fr.snapgames.demo.core.entity.EntityManager;
import fr.snapgames.demo.core.gfx.Renderer;
import fr.snapgames.demo.core.io.InputHandler;
import fr.snapgames.demo.core.physic.PhysicEngine;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The {@link AbstractScene} is the basis for any Scene implementation to provide gameplay.
 *
 * <p>The Lifecycle for a Scene is:</p>
 * <ol>
 * <li>{@link AbstractScene#initialize(Game)} called by the {@link SceneManager} at its instantiation,</li>
 * <li>{@link AbstractScene#prepare(Game)} called just before scene activation, used to load some required resources,</li>
 * <li>{@link AbstractScene#create(Game)} to define the {@link Scene} adding all thr required entity to the scene,</li>
 * <li>{@link AbstractScene#input(Game)} started in le game loop, to manage Input entries and events,</li>
 * <li>{@link AbstractScene#update(Game, double)} to perform any scene specific processing like interaction between {@link Entity},</li>
 * <li>{@link AbstractScene#draw(Game, Renderer)} to draw some un predefined rendering process by {@link fr.snapgames.demo.core.gfx.plugins.DrawHelperPlugin}</li>
 * </ol>
 *
 * @author Frédéric Delorme
 * @see Scene
 * @see Game
 * @see Renderer
 * @see fr.snapgames.demo.core.gfx.plugins.DrawHelperPlugin
 * @see SceneManager
 * @since 0.1.1
 */

public abstract class AbstractScene implements Scene {

    private static final Logger logger = Logger.getLogger(AbstractScene.class.getName());

    protected final Game game;
    protected final String name;

    protected Map<String, Entity<?>> entities = new ConcurrentHashMap<>();

    /**
     * The active scene camera.
     */
    protected Camera camera;
    /**
     * required access to configuration to gather some attributes values
     */
    protected Configuration config;
    /**
     * THe renderer service
     */
    protected Renderer renderer;
    /**
     * The service to manage entities
     */
    protected EntityManager entityMgr;

    /**
     * THe Physic computation center for active {@link Entity}
     */
    protected PhysicEngine physicEngine;
    /**
     * The handler to capture input events
     */
    protected InputHandler inputHandler;

    /**
     * Create the default Scene parameters
     *
     * @param g    the parent Game for that {@link Scene}
     * @param name the name fof this scene (initialize at Scene Manager initialization from configuration.
     */
    public AbstractScene(Game g, String name) {
        logger.log(Level.INFO, "Instantiate scene {}", name);
        this.game = g;
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    /**
     * Scene initialisation starting by a retrieving for default Services
     *
     * @param g the parent Game instance hosting this Scene.
     */

    public void initialize(Game g) {
        config = g.getConfiguration();
        renderer = g.getRenderer();
        entityMgr = g.getEntityManager();
        physicEngine = g.getPhysicEngine();
        inputHandler = g.getInputHandler();
        prepare(g);
    }

    /**
     * Preparation of the scene, this is where resources need to be loaded and reloaded at each scene activation)
     *
     * @param g the parent Game instance hosting this Scene.
     */
    public abstract void prepare(Game g);

    /**
     * CReation of the Scene by defining all the required {@link Entity}'s.
     *
     * @param g the parent Game instance hosting this Scene.
     */
    @Override
    public abstract void create(Game g);

    @Override
    public abstract void input(Game g);

    @Override
    public abstract void update(Game g, double elapsed);

    @Override
    public abstract void draw(Game g, Renderer r);

    @Override
    public void dispose() {
        entities.clear();
    }


    /**
     * Retrieve the default active camera for that Scene.
     *
     * @return the Camera instance
     */
    public Camera getCamera() {
        return camera;
    }

    /**
     * Define the default {@link Camera} for that {@link Scene}.
     *
     * @param camera the camera to be set as the default one for that Scene.
     */
    public void setCamera(Camera camera) {
        this.camera = camera;
        this.renderer.setCurrentCamera(camera);
    }

    /**
     * Add a new Entity to the Scene, and add it to the required services.
     *
     * @param e the Entity to be added to all the required services.
     */
    public void add(Entity<?> e) {
        entityMgr.add(e);
    }
}