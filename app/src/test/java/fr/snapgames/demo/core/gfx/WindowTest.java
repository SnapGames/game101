package fr.snapgames.demo.core.gfx;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author : M313104
 * @mailto : buy@mail.com
 * @created : 02/01/2023
 **/
class WindowTest {

    @Test
    void createWindowOfSize320x200AndTitleTest() {
        Window window = new Window("Test", 320, 200);
        Assertions.assertEquals("Test", window.title, "Window's title has not been initialize");
        Assertions.assertEquals(320, window.width, "Window's width has not been initialize");
        Assertions.assertEquals(200, window.height, "Window's height has not been initialize");

    }

    @Test
    void switchWindowToFullSCreen() {
        Window window = new Window("Test", 320, 200);
        window.switchFullScreen(true);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertTrue(window.getFrame().getWidth() > 320, "Window's full screen mode has not been initialize");
        Assertions.assertTrue(window.getFrame().getHeight() > 200, "Window's full screen mode has not been initialize");

    }
}