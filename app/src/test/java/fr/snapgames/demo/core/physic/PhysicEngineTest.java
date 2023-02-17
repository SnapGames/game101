package fr.snapgames.demo.core.physic;

import fr.snapgames.demo.core.entity.Entity;
import fr.snapgames.demo.core.entity.GameObject;
import fr.snapgames.demo.core.math.Vector2D;
import fr.snapgames.demo.gdemoapp.App;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.geom.Rectangle2D;
import java.util.Locale;

/**
 * {@link PhysicEngine} is the class under test.
 *
 * @author Frédéric Delorme
 */
public class PhysicEngineTest {

    App game;
    PhysicEngine pe;

    @BeforeAll
    public static void beforeAll() {
        System.setProperty("file.encoding", "UTF-8");
        Locale.setDefault(Locale.US);
    }

    @BeforeEach
    public void setup() {
        game = new App("/config-physicengine.properties");
        game.applyConfiguration(new String[] {});
        this.pe = new PhysicEngine(game);
    }

    @Test
    public void testPhysicEngineIsInitialized() {
        Assertions.assertEquals(
                new Vector2D(0.0, 0.000981),
                pe.getWorld().getGravity(),
                "World's gravity has not been correctly setup");
    }

    @Test
    public void testPhysicEngineHasWorld() {
        Assertions.assertNotNull(pe.getWorld(), "World has not been initialized");
    }

    @Test
    public void testPhysicEngineHasPlayArea() {
        Rectangle2D playArea = pe.getWorld().playArea;
        Assertions.assertEquals(
                320.0,
                playArea.getWidth(),
                "Play area width has not been correctly setup");
        Assertions.assertEquals(
                200.0,
                playArea.getHeight(),
                "Play area height has not been correctly setup");
    }

    @Test
    public void testPhysicWorldUpdateEntities() {
        // initialize Game services
        game.initialize(new String[] {});
        World world = pe.getWorld();
        world.setGravity(new Vector2D(0.0, 0.0));
        // create some test entities
        for (int i = 0; i < 10; i++) {
            game.getEntityManager().add(
                    new GameObject("TestEntity_" + i)
                            .setSize(16.0, 16.0)
                            .setPosition(
                                    world.playArea.getWidth() * 0.5,
                                    world.playArea.getHeight() * 0.5)
                            .setMass(1.0)
                            .setMaterial(Material.RUBBER)
                            .addForce(new Vector2D(
                                    (Math.random() * 2.0) - 1.0,
                                    (Math.random() * 2.0) - 1.0)));
        }
        // update the engine
        for (int i = 0; i < 10; i++) {
            pe.update(1600);
        }
        String output = "";
        // check entities position
        for (int i = 0; i < 10; i++) {
            Entity<?> e = game.getEntityManager().get("TestEntity_" + i);
            output += String.format("- Entity[name:%s;pos:%s;spd:%s;acc: %s]%n",
                    e.getName(),
                    e.position,
                    e.velocity,
                    e.acceleration);
            Assertions.assertTrue(e.position.x != 0.0 || e.position.y != 0.0,
                    "Entity " + i + " has not been updated: x="
                            + e.position.x + ",y=" + e.position.y);
        }
        /*try {
            Path contentPath = Paths.get("/content_target/physictests_results.txt");
            String compareContent = new String(Files.readAllBytes(contentPath));

            Assertions.assertEquals(compareContent, output,
                    "Entities has not been correctly updated by the PhysicEngine.");
        } catch (IOException e) {
            System.err.println("Unable to read comparison file 'physictests_results.txt': "+e.getMessage());
            Assertions.fail("Unable to read comparison file 'physictests_results.txt' : "+e.getMessage());
        }*/

    }
}
