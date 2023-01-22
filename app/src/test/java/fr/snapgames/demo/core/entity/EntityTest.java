package fr.snapgames.demo.core.entity;

import fr.snapgames.demo.core.physic.Material;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.List;

/**
 * {@link Entity} is the class under test.
 *
 * @author : Frédéric Delorme
 **/
public class EntityTest {

    @Test
    public void testEntityHasDefaultValue() {
        Entity<String> p = new Entity<>();
        // Entity's level default values
        Assertions.assertTrue(p.getName().startsWith("entity_"));
        Assertions.assertEquals(1.0, p.mass);
        Assertions.assertEquals(p.getLayer(), 1);
        Assertions.assertEquals(p.getPriority(), 1);
        Assertions.assertEquals(p.width, 16);
        Assertions.assertEquals(p.height, 16);
        Assertions.assertEquals(p.fillColor, Color.RED);
        Assertions.assertEquals(p.borderColor, Color.BLACK);
        Assertions.assertEquals(p.material, Material.DEFAULT);

    }

    @Test
    public void testGameObjectHasDebugInfo() {
        Entity<String> p = new Entity<>();
        List<String> debugInfo = p.getDebugInfo();
        Assertions.assertTrue(debugInfo.get(0).startsWith("name:entity_"));
        Assertions.assertEquals("pos:0,00,0,00", debugInfo.get(1).trim());
        Assertions.assertEquals("size:16x16", debugInfo.get(2).trim());
        Assertions.assertEquals("spd:0,00,0,00", debugInfo.get(3).trim());
        Assertions.assertEquals("acc:0,00,0,00", debugInfo.get(4).trim());
        Assertions.assertTrue(debugInfo.get(5).startsWith("map:default[d="));

    }
}
