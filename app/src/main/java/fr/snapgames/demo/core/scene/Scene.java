package fr.snapgames.demo.core.scene;

import fr.snapgames.demo.core.Game;
import fr.snapgames.demo.core.entity.Camera;
import fr.snapgames.demo.core.gfx.Renderer;

/**
 * Scene interface define a specific game play interface to be implemented as part of an app.
 * RThis {@link Scene} will be managed by the {@link SceneManager} and controlled by the {@link fr.snapgames.demo.gdemoapp.App}.
 *
 * @author Frederic Delorme
 * @since 0.1.1
 */
public interface Scene {
    /**
     * Retrieve the name for this scene.
     *
     * @return
     */
    String getName();

    /**
     * Required by {@link SceneManager} to activate a scene, define operation to be executed before {@link Scene} prepared.
     *
     * @param g the parent Game instance hosting this Scene.
     */
    void initialize(Game g);

    /**
     * the {@link Scene#prepare(Game)} will load all required resources for your scene. This is where image, sounds,
     * and other resources must be loaded, before scene creation.
     *
     * @param g the parent Game instance hosting this Scene.
     */
    void prepare(Game g);


    /**
     * the {@link Scene#create(Game)} method will create all required objects to the scene.
     * This is where the scene is created (described) by adding all the GameObject instances,
     * where default values are initialized, just after the scene is activated.
     *
     * @param g the parent Game instance hosting this Scene.
     */
    void create(Game g);

    /**
     * Here is where the Scene will manage all the device input: mouse and keyboard.
     *
     * @param g the parent Game instance hosting this Scene.
     */
    void input(Game g);

    /**
     * This is where specific update mechanics for a {@link Scene} must be implemented.
     *
     * @param g       the parent Game instance hosting this Scene.
     * @param elapsed the elapsed time since previous scene update call.
     */
    void update(Game g, double elapsed);

    /**
     * THe specific draw operation (after the GameObject rendering,
     * then the specific effects for this scene can be rendered.
     *
     * @param g the parent Game instance hosting this Scene.
     */
    void draw(Game g, Renderer r);

    /**
     * release all the loaded / instantiated resources objects.
     */
    void dispose();

    /**
     * return the existing active Scene {@link Camera}.
     *
     * @return a {@link Camera} instance.
     */
    Camera getCamera();
}