package fr.snapgames.demo.core.physic;

import fr.snapgames.demo.core.entity.Entity;
import fr.snapgames.demo.gdemoapp.App;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.geom.Point2D;

public class PhysicEngineTest {

    App game;
    PhysicEngine pe;

    @BeforeEach
    public void setup() {
        game = new App("/config-physicengine.properties");
        game.applyConfiguration(new String[]{});
        this.pe = new PhysicEngine(game);
    }

    @Test
    public void testPhysicEngineIsInitialized() {
        Assertions.assertEquals(
                new Point2D.Double(0.0, 0.000981),
                pe.getWorld().getGravity(),
                "World's gravity has not been correctly setup");
    }

    @Test
    public void testPhysicEngineHasWorld() {
        Assertions.assertNotNull(pe.getWorld(), "World has not been initialized");
    }

    @Test
    public void testPhysicEngineHasPlayArea() {
        Assertions.assertEquals(
                320,
                pe.getWorld().paWidth,
                "Play area width has not been correctly setup");
        Assertions.assertEquals(
                200,
                pe.getWorld().paHeight,
                "Play area height has not been correctly setup");
    }

    @Test
    public void testPhysicWorldUpdateEntities() {
        // initialize Game services
        game.initialize(new String[]{});
        pe.getWorld().setGravity(new Point2D.Double(0.0, 0.0));
        // create some test entities
        for (int i = 0; i < 10; i++) {
            game.getEntityManager().add(
                    new Entity("TestEntity_" + i)
                            .setSize(16.0, 16.0)
                            .setPosition(0.0, 0.0)
                            .setSpeed(20.0, 20.0)
                            .setMass(1.0)
                            .setMaterial(Material.RUBBER)
                            .addForce(new Point2D.Double(
                                    (Math.random() * 2.0) - 1.0,
                                    (Math.random() * 2.0) - 1.0)));
        }
        // update the engine
        for (int i = 0; i < 10; i++) {
            pe.update(16);
        }
        // check entities position
        for (int i = 0; i < 10; i++) {
            Entity e = game.getEntityManager().get("TestEntity_" + i);
            System.out.printf("- entity %s pos:%3.0f,%3.0f spd:%3.2f,%3.3f, acc: %3.2f,%3.3f%n",
                    e.getName(),
                    e.x, e.y,
                    e.dx, e.dy,
                    e.ax, e.ay);
            Assertions.assertTrue(e.y != 0.0 && e.y != 0.0, "Entity " + i + " has not been updated: x=" + e.x + ",y=" + e.y);
        }
    }
}
