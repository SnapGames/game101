package fr.snapgames.demo.core.scene;

import fr.snapgames.demo.core.Game;
import fr.snapgames.demo.core.configuration.Configuration;
import fr.snapgames.demo.gdemoapp.ConfigAttribute;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The {@link SceneManager} offers to handle multiple {@link Scene} implementation and to manage switching
 * from one to another.
 * <p>
 * A list of {@link SceneManager#scenes} instances can be activated from the corresponding implementation
 * in the {@link SceneManager#availableScenes}.
 * <p>
 * The {@link SceneManager#current} active {@link Scene} is by default set at Service initialization,
 * just after reading the configuration attributes:
 * <ul>
 * <li>{@link ConfigAttribute#SCENE_LIST} containing the list of the available {@link Scene} implementations</li>
 * <li><and {@link ConfigAttribute#SCENE_DEFAULT} pointing the first instance in the provided list to be
 * activated at start.</li>
 * </ul>
 *
 * @author Frédéric Delorme
 * @since 0.1.1
 */
public class SceneManager {

    private static final Logger logger = Logger.getLogger(SceneManager.class.getName());
    /**
     * List of scenes instances
     */
    private Map<String, Scene> scenes = new HashMap<>();
    /**
     * List of available implementations (from {@link ConfigAttribute#SCENE_LIST}.
     */
    private Map<String, Class<? extends Scene>> availableScenes = new HashMap<>();
    /**
     * The parent {@link Game}'s {@link Configuration} instance.
     */
    private Configuration config;
    /**
     * the parent {@link Game} instance.
     */
    private Game game;
    /**
     * The current active Scene (default set to the {@link ConfigAttribute#SCENE_DEFAULT} corresponding instance).
     */
    private Scene current;


    /**
     * Create the {@link SceneManager} from its parent {@link Game} instance.
     *
     * @param g The parent {@link Game} instance.
     */
    public SceneManager(Game g) {
        initialize(g);
    }

    /**
     * Initialize the SceneManager service from the {@link Game} instance.
     *
     * @param g The parent {@link Game} instance.
     */
    public void initialize(Game g) {
        this.game = g;
        config = g.getConfiguration();
        List<String> scenesList = (List<String>) config.get(ConfigAttribute.SCENE_LIST);
        if (Optional.ofNullable(scenesList).isPresent() && !scenesList.isEmpty()) {
            scenesList.forEach(s -> {
                String[] kv = s.split(":");
                try {
                    Class<? extends Scene> sceneToAdd = (Class<? extends Scene>) Class.forName(kv[1]);
                    add(kv[0], sceneToAdd);
                    logger.log(Level.INFO, "Add scene {0}:{1}", new Object[]{kv[0], kv[1]});
                } catch (ClassNotFoundException e) {
                    logger.log(Level.SEVERE, "Unable to load class {0}", kv[1]);
                }
            });
        }
        // initialize default scene.
        activateDefaultScene();
    }

    /**
     * Activate a specific Scene on its internal sceneId.
     *
     * @param sceneId the unique identifier for the scene to be activated (from the {@link ConfigAttribute#SCENE_DEFAULT} list).
     */
    public void activateScene(String sceneId) {
        if (!scenes.containsKey(sceneId) && availableScenes.containsKey(sceneId)) {
            Class<? extends Scene> sceneClass = availableScenes.get(sceneId);
            try {
                Scene scn = sceneClass.getConstructor(Game.class, String.class).newInstance(game, sceneId);
                scenes.put(sceneId, scn);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {
                System.out.printf("Unable to create Scene {0} instance", sceneClass.getName());
            }
            this.current = scenes.get(sceneId);
            this.current.initialize(game);
        } else {
            System.out.printf(
                    "The Scene {0} does not exists in configuration file for key '{1}'.",
                    new Object[]{sceneId, ConfigAttribute.SCENE_LIST.getConfigKey()});
        }
    }

    /**
     * Activate the default scene configured by {@link ConfigAttribute#SCENE_DEFAULT}.
     */
    public void activateDefaultScene() {
        activateScene((String) config.get(ConfigAttribute.SCENE_DEFAULT));
    }

    /**
     * Add a scene to the {@link SceneManager}.
     *
     * @param sceneId    the {@link Scene} unique identifier
     * @param sceneClass the implementation class for this {@link Scene}.
     */
    private void add(String sceneId, Class<? extends Scene> sceneClass) {
        availableScenes.put(sceneId, sceneClass);
    }

    /**
     * retrieve the current active {@link Scene}.
     *
     * @return the instance of the current active {@link Scene}.
     */
    public Scene getCurrent() {
        return current;
    }

    /**
     * Return the list of scenes loaded from configuration into SceneManager.
     *
     * @return the current instantiated Scene list
     */
    public Collection<Scene> getSceneList() {
        return this.scenes.values();
    }

    /**
     * Return a specific scene by its name from tha scenes list.
     *
     * @param sceneName the scene name to be retrieved from the list.
     * @return the Scene instance corresponding to the sceneName.
     */
    public Scene getScene(String sceneName) {
        return scenes.get(sceneName);
    }

    /**
     * Free all resources from the scene instances.
     */
    public void dispose() {
        scenes.values().forEach(Scene::dispose);
    }
}