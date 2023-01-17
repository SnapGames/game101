package fr.snapgames.demo.core.entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.List;

/**
 * {@link fr.snapgames.demo.core.entity.GameObject} is the class under test in this file.
 *
 * @author Frédéric Delorme
 * @since 0.0.9
 **/
public class GameObjectTest {

    @Test
    public void testGameObjectDefaultValues() {
        GameObject p = new GameObject();

        // GameObject's level default values
        Assertions.assertEquals(ObjectType.RECTANGLE, p.type);
    }

    @Test
    public void testGameObjectHasNewName() {
        GameObject p = new GameObject("myGameObject");
        // Entity's level default values
        Assertions.assertEquals("myGameObject", p.getName());
    }

    @Test
    public void testGameObjectHasRectangleBoxUpdated() {
        GameObject p = new GameObject();
        p.setPosition(20, 20).setSize(32, 32);
        p.updateBox();
        Assertions.assertEquals(
                new Rectangle2D.Double(20, 20, 32, 32),
                p.box);
    }

    @Test
    public void testGameObjectHasEllipseBoxUpdated() {
        GameObject p = new GameObject();
        p.setType(ObjectType.ELLIPSE).setPosition(100, 100).setSize(12, 24);
        p.updateBox();
        Assertions.assertEquals(
                new Ellipse2D.Double(100, 100, 12, 24),
                p.box);
    }

    @Test
    public void testGameObjectHasDebugInfo() {
        GameObject p = new GameObject();
        List<String> debugInfo = p.getDebugInfo();
        Assertions.assertTrue(debugInfo.get(0).startsWith("(1)id:"));
        Assertions.assertTrue(debugInfo.get(1).startsWith("(1)name:entity_"));
        Assertions.assertEquals("(2)type:RECTANGLE", debugInfo.get(2));
        Assertions.assertEquals("(2)pos:0.00,0.00", debugInfo.get(3));
        Assertions.assertEquals("(2)size:16.00,16.00", debugInfo.get(4));
        Assertions.assertEquals("(3)spd:0.00,0.00", debugInfo.get(5));
        Assertions.assertEquals("(3)acc:0.00,0.00", debugInfo.get(6));
        Assertions.assertEquals("(4)l:1 p:1", debugInfo.get(7));
        Assertions.assertTrue(debugInfo.get(8).startsWith("(4)map:default[d="));

    }
}