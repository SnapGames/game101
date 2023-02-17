package fr.snapgames.demo.core.physic;

import fr.snapgames.demo.core.math.Vector2D;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * {@link World} is the class under test, part of the {@link PhysicEngine}
 * system.
 *
 * @author : Frédéric Delorme
 * @since 0.0.9
 **/
public class WorldTest {

    @Test
    public void testWorldHasDefaultValues() {
        World world = new World();
        Assertions.assertEquals(new Vector2D(0, 0.981), world.gravity);
        Assertions.assertEquals(Material.AIR, world.material);
        Assertions.assertEquals(new Rectangle2D.Double(0, 0, 320, 200), world.playArea);
        Assertions.assertEquals(0.001, world.minSpeed);
        Assertions.assertEquals(120.0, world.maxSpeedX);
        Assertions.assertEquals(120.0, world.maxSpeedX);
        Assertions.assertEquals(0.00001, world.minAcc);
        Assertions.assertEquals(600.0, world.maxAccX);
        Assertions.assertEquals(600.0, world.maxAccY);
        Assertions.assertEquals(Material.AIR, world.material);
    }
}
