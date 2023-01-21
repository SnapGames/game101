package fr.snapgames.demo.core.gfx;

import fr.snapgames.demo.core.entity.EntityTestWithPlugin;
import fr.snapgames.demo.core.gfx.plugin.EntityTestWithPluginDrawHelperPlugin;
import fr.snapgames.demo.gdemoapp.App;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

/**
 * A {@link fr.snapgames.demo.core.gfx.plugins.DrawHelperPlugin} to test {@link Renderer} Plugin architecture and usage.
 *
 * @author Frédéric Delorme
 * @since 0.0.9
 **/
public class RendererDrawHelperPluginTest {
    App game;
    Renderer renderer;

    @BeforeEach
    void setUp() {
        game = new App("/config-renderer-plugin.properties");
        game.applyConfiguration(new String[]{});
    }

    @Test
    public void testDrawHelperPluginIsActivated() {
        game.initialize(new String[]{""});
        renderer = game.getRenderer();
        renderer.addPlugin(new EntityTestWithPluginDrawHelperPlugin());
        game.getEntityManager().add(new EntityTestWithPlugin("testPlugin"));
        renderer.draw(new HashMap<>());

        EntityTestWithPlugin etwp = (EntityTestWithPlugin) game.getEntityManager().get("testPlugin");
        Assertions.assertTrue(etwp.getDrawnFlag());
    }
}
