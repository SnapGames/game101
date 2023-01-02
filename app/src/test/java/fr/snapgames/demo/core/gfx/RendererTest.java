package fr.snapgames.demo.core.gfx;

import fr.snapgames.demo.TestUtils;
import fr.snapgames.demo.gdemoapp.App;
import fr.snapgames.demo.gdemoapp.ConfigAttribute;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.awt.image.BufferedImage;

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
        Assertions.assertEquals(renderer.getBuffer().getWidth(), game.getConfiguration().get(ConfigAttribute.SCREEN_WIDTH), "Internal renderer buffer has not been correctly set.");
        Assertions.assertEquals(renderer.getBuffer().getHeight(), game.getConfiguration().get(ConfigAttribute.SCREEN_HEIGHT), "Internal renderer buffer has not been correctly set.");
    }

    @Test
    void testRendererDrawToWindowOperation() {
        renderer = new Renderer(game);
        Window window = new Window("test", 320, 200);
        renderer.draw();
        renderer.drawToWindow(window);
        int cx = window.getFrame().getInsets().left + window.getFrame().getLocation().x;
        int cy = window.getFrame().getInsets().top + window.getFrame().getLocation().y;
        BufferedImage fromWindow = TestUtils.extractComponentToImage(window, new Point(cx, cy));
        double perc = TestUtils.compareImage(renderer.getBuffer(), fromWindow);
        System.out.printf("Percentage difference between images from Renderer Buffer and Window content: %f%n", perc);
        Assertions.assertNotEquals(-1.0, perc, "Image does not correspond in size.");
        Assertions.assertEquals(0.0, perc, "Image has not been correctly rendered on Window");
    }
}