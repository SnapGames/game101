package fr.snapgames.demo.core.entity;

import fr.snapgames.demo.core.physic.Material;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.List;
import java.util.Locale;

/**
 * {@link Entity} is the class under test.
 *
 * @author : Frédéric Delorme
 **/
public class EntityTest {
    @BeforeAll
    public static void beforeAll() {
        System.setProperty("file.encoding", "UTF-8");
        Locale.setDefault(Locale.US);
    }

    @Test
    public void testEntityHasDefaultValue() {
        Entity<String> p = new Entity<>();
        // Entity's level default values
        Assertions.assertTrue(p.getName().startsWith("entity_"));
        Assertions.assertEquals(1.0, p.getMass());
        Assertions.assertEquals(1, p.getLayer());
        Assertions.assertEquals(1, p.getPriority());
        Assertions.assertEquals(0, p.size.x);
        Assertions.assertEquals(0, p.size.y);
        Assertions.assertEquals(Color.RED, p.fillColor);
        Assertions.assertEquals(Color.BLACK, p.borderColor);
        Assertions.assertEquals(Material.DEFAULT, p.material);

    }

    @Test
    public void testGameObjectHasDebugInfo() {
        Entity<String> p = new Entity<>();
        List<String> debugInfo = p.getDebugInfo();
        Assertions.assertTrue(debugInfo.get(0).startsWith("name:entity_"));
        Assertions.assertEquals("pos:{x:0.00,y:0.00}", debugInfo.get(1).trim());
        Assertions.assertEquals("size:{x:0.00,y:0.00}", debugInfo.get(2).trim());
        Assertions.assertEquals("spd:{x:0.00,y:0.00}", debugInfo.get(3).trim());
        Assertions.assertEquals("acc:{x:0.00,y:0.00}", debugInfo.get(4).trim());
        Assertions.assertEquals("mat:"+Material.DEFAULT.toString(), debugInfo.get(5));

    }
}
