package fr.snapgames.demo.core.config;

import fr.snapgames.demo.core.configuration.Configuration;
import fr.snapgames.demo.core.math.Vector2D;
import fr.snapgames.demo.core.physic.Material;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.awt.geom.Point2D;

/**
 * The Configuration is the class under test.
 *
 * @author : Frédéric Delorme
 * @since 0.0.9
 **/
public class ConfigurationTest {

    @Test
    public void testConfigurationHasIdentifiedTypeOfValues() {
        Configuration config = new Configuration(ConfigAttributeForTest.values());
        config.setConfigurationFile("/configuration-test.properties");
        config.parseConfigFile();
        Assertions.assertEquals(true, config.get(ConfigAttributeForTest.BOOLEAN_VALUE));
        Assertions.assertEquals(1, config.get(ConfigAttributeForTest.INTEGER_VALUE));
        Assertions.assertEquals(1.0, config.get(ConfigAttributeForTest.DOUBLE_VALUE));
        Assertions.assertEquals("test", config.get(ConfigAttributeForTest.TEXT_VALUE));
        Assertions.assertEquals(new Vector2D(123.45, 456.78), config.get(ConfigAttributeForTest.VECTOR2D_VALUE));
        Assertions.assertEquals(new Material("testMaterial",0.1,0.2,0.3), config.get(ConfigAttributeForTest.MATERIAL_VALUE));
    }
}
