package fr.snapgames.demo.core;

import fr.snapgames.demo.core.configuration.Configuration;
import fr.snapgames.demo.core.entity.EntityManager;
import fr.snapgames.demo.core.gfx.Window;
import fr.snapgames.demo.core.physic.PhysicEngine;

import java.util.HashMap;
import java.util.Map;

/**
 * THe Game Interface defining the right Game API for our game application framework.
 *
 * @author Frédéric Delorme
 * @since 0.0.1
 */
public interface Game {
    /**
     * Retrieve the name of the application.
     *
     * @return The name for this game implementation.
     */
    String getAppName();


    /**
     * Initialize the application according to CLI parameters.
     *
     * @param args list of arguments from CLI.
     */
    int initialize(String[] args);

    /**
     * Create all the required entity to be managed by the game.
     */
    void create();

    /**
     * Manage all the device input status to update keyboard and mouse, and more..
     *
     * @param g the parent Game instance.
     */
    void input(Game g);

    /**
     * Update all the entities in the game according to some possible input changes and compute next moves for the entities.
     *
     * @param g          the parent Game instance.
     * @param attributes a map of statistical values to be used at Rendering lvel for display purpose (or anything else, computed by the {@link Game#loop()})..
     * @param elapsed    the elapsed time since previous call for an incremental time update computation.
     */
    void update(Game g, Map<String, Object> attributes, double elapsed);

    /**
     * Draw everything on screen about the entities and the game play.
     *
     * @param g          the parent Game instance.
     * @param attributes a map of statistical values to be used at Rendering lvel for display purpose (or anything else, computed by the {@link Game#loop()})..
     */
    void render(Game g, Map<String, Object> attributes);

    /**
     * The loop API providing a default implementation.
     * The Loop supposed to be a constant frame loop processing, based on an FPS reference.
     */
    default void loop() {
        int frames = 0;
        int upsCount = 0;
        int fps = getTargetFps();
        int ups = getTargetUps();
        double internalTime = 0;
        double previousTime = System.currentTimeMillis();
        double currentTime = previousTime;
        double gameTime = 0;
        double elapsed;
        Map<String, Object> renderingAttributes = new HashMap<>();

        renderingAttributes.put("game.time", gameTime);
        renderingAttributes.put("game.fps", fps);
        renderingAttributes.put("game.ups", ups);

        loadResources();
        create();

        while (!isExitRequested()) {
            currentTime = System.currentTimeMillis();
            input(this);
            elapsed = currentTime - previousTime;
            if (!isPaused()) {
                update(this, renderingAttributes, elapsed);
                gameTime += elapsed;
                upsCount += 1;
            }

            renderingAttributes.put("game.time", gameTime);
            renderingAttributes.put("game.fps", fps);
            renderingAttributes.put("game.ups", ups);
            render(this, renderingAttributes);
            frames += 1;

            internalTime += elapsed;
            if (internalTime > 1000.0) {
                fps = frames;
                frames = 0;
                internalTime = 0;
                ups = upsCount;
                upsCount = 0;
            }
            waitUntilNextFrame(elapsed);
            previousTime = currentTime;
        }
    }

    /**
     * Load resources for the scene before {@link Game#create()}
     */
    default void loadResources() {

    }


    /**
     * a waiting operation to match the getTargetFPS() value.
     *
     * @param elapsed the elapsed time since previous call.
     */
    default void waitUntilNextFrame(double elapsed) {
        try {
            double timeFrame = 1000.0 / getTargetUps();
            int wait = (int) (elapsed < timeFrame ? timeFrame - elapsed : 5);
            Thread.sleep(wait);
        } catch (InterruptedException ie) {
            System.err.println("error while trying to wait for sometime");
        }
    }

    /**
     * The API to run the game, here is a default simple implementation, proposing the common operation:
     * <ul>
     *     <li><code>initialize</code> will propose a CLI arguments parser,</li>
     *     <li><code>loop</code> is supposed to call the main GameLoop to start the implemented game play,</li>
     *     <li><code>dispose</code> to finalize exit operation, will implement a proper releasing for all loaded
     *     resources and memory clean.</li>
     * </ul>
     *
     * @param args the list of CLI parameters
     */
    default void run(String[] args) {

        initialize(args);
        loop();
        dispose();
    }

    /**
     * Define the target Update-Per-Second for the update of all entities in the game.
     *
     * @return an int value for the UPS target.
     */
    int getTargetUps();

    /**
     * retrieve te required to be targeted Frame Per Second for loop processing.
     *
     * @return the Frame Per Second as target value.
     */
    int getTargetFps();

    /**
     * release all reserved ald loaded resources before exiting.
     */
    void dispose();

    /**
     * Return the state of pause mode.
     * <ul>
     *     <li><code>true</code> the game is in pause mode: no {@link Game#update(Game, Map, double)} are processing,</li>
     *     <li><code>false</code> the game is in normal mode, {@link Game#update(Game, Map, double)} is processed.</li>
     * </ul>
     *
     * @return boolean value for pause mode.
     */
    boolean isPaused();

    /**
     * Return status ofr exit request:
     * <ul>
     *     <li><code>false</code> the game continues to loop,</li>
     *     <li><code>true</code> the game performs exit process.</li>
     * </ul>
     *
     * @return boolean value for exit required state.
     */
    boolean isExitRequested();

    /**
     * Retrieve configuration required by sub services.
     *
     * @return the Configuration instance required for sub services.
     */
    Configuration getConfiguration();

    /**
     * Retrieve the EntityManager required by sub services.
     *
     * @return the EntityManager instance required for sub services.
     */
    EntityManager getEntityManager();

    /**
     * Retrieve the PhysicEngine service instance.
     *
     * @return a PhysicEngine instance.
     */
    PhysicEngine getPhysicEngine();

    /**
     * retrieve the {@link Window} for this Game implementation.
     *
     * @return the current active Game {@link Window}.
     */
    Window getWindow();

    /**
     * return the current debug mode set at Game level
     *
     * @return an int value between 0 and 4, defining level of display debug information. 0 mean no debug info.
     */
    int getDebugMode();

    /**
     * Return the number of Image per frame drawn.
     *
     * @return a double value computed according to the real number of frame per second processed.
     */
    double getFPS();

    /**
     * Request exit out of the Game.
     *
     * @param exit a boolean value, if true, request exit is effective.
     */
    void requestExit(boolean exit);

    /**
     * Set the debug info display level.
     *
     * @param d 0 to 4, 0 mean no display.
     */
    void setDebugMode(int d);

    /**
     * Request to the Game implementation to pause the update process.
     *
     * @param pause true is to pause the update. else continue updating all entities.
     */
    void requestPause(boolean pause);

}
