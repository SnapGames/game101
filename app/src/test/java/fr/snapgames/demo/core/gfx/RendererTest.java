package fr.snapgames.demo.core.gfx;

import fr.snapgames.demo.gdemoapp.App;
import fr.snapgames.demo.gdemoapp.ConfigAttribute;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test upon the Renderer service class
 *
 * @author Frédéric Delorme
 * @since 0.0.5
 **/
class RendererTest {
    App game;
    Renderer renderer;

    @BeforeEach
    void setUp() {
        game = new App("/config-renderer.properties");
        game.applyConfiguration(new String[]{});
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testRendererGetItsConfiguration() {
        renderer = new Renderer(game);
        Assertions.assertEquals(renderer.getBuffer().getWidth(),
                game.getConfiguration().get(ConfigAttribute.SCREEN_WIDTH),
                "Internal renderer buffer has not been correctly set.");
        Assertions.assertEquals(renderer.getBuffer().getHeight(),
                game.getConfiguration().get(ConfigAttribute.SCREEN_HEIGHT),
                "Internal renderer buffer has not been correctly set.");
    }
}