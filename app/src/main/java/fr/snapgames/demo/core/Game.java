package fr.snapgames.demo.core;

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
     * @return
     */
    String getAppName();

    /**
     * Initialize the application according to CLI parameters.
     *
     * @param args list of arguments from CLI.
     * @return 0 if ok, -1 if error.
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
     * @param g       the parent Game instance.
     * @param elapsed the elapsed time since previous call for an incremental time update computation.
     */
    void update(Game g, double elapsed);

    /**
     * Draw everything on screen about the entities and the game play.
     *
     * @param g   the parent Game instance.
     * @param fps the number of frame per seconds really performed (computed by the {@link Game#loop()})..
     */
    void render(Game g, int fps);

    /**
     * The loop API providing a default implementation.
     * The Loop supposed to be a constant frame loop processing, based on an FPS reference.
     */
    default void loop() {
        int frames = 0;
        int fps = getTargetFps();
        double internalTime = 0;
        double previousTime = System.currentTimeMillis();
        double currentTime = System.currentTimeMillis();
        double elapsed = currentTime - previousTime;
        create();
        while (!isExitRequested()) {
            currentTime = System.currentTimeMillis();
            input(this);
            elapsed = currentTime - previousTime;
            if (!isPaused()) {
                update(this, elapsed);
            }
            render(this, fps);
            frames += 1;
            internalTime += elapsed;
            if (internalTime > 1000.0) {
                fps = frames;
                frames = 0;
                internalTime = 0;
            }
            waitUntilNextFrame(elapsed);
            previousTime = currentTime;
        }
    }

    /**
     * a waiting operation to match the getTargetFPS() value.
     *
     * @param elapsed the elapsed time since previous call.
     */
    default void waitUntilNextFrame(double elapsed) {
        try {
            double timeFrame = 1000.0 / getTargetFps();
            int wait = (int) (elapsed < timeFrame ? timeFrame - elapsed : 1);
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
     *     <li><code>true</code> the game is in pause mode: no {@link Game#update(Game, double)} are processing,</li>
     *     <li><code>false</code> the game is in normal mode, {@link Game#update(Game, double)} is processed.</li>
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

}
