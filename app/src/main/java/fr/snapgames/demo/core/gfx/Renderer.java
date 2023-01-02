package fr.snapgames.demo.core.gfx;


import fr.snapgames.demo.core.Game;
import fr.snapgames.demo.gdemoapp.ConfigAttribute;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * {@link Renderer} is the Rendering service for our game.
 * Everything that must be drawn on screen is delegated to this service.
 *
 * @author Frédéric Delorme
 * @since 0.0.5
 */
public class Renderer {
    /**
     * Parent game hosting the service.
     */
    private final Game game;
    /**
     * Internal buffer image to render all things before display on window.
     */
    private BufferedImage buffer;
    /**
     * Window width
     */
    int windowWidth;
    /**
     * Window height
     */
    int windowHeight;
    /**
     * Screen width
     */
    int screenWidth;
    /**
     * Screen height
     */
    int screenHeight;

    /**
     * Initialize the Renderer service with its parent game.
     *
     * @param g
     */
    public Renderer(Game g) {
        this.game = g;
        windowWidth = (int) game.getConfiguration().get(ConfigAttribute.WINDOW_WIDTH);
        windowHeight = (int) game.getConfiguration().get(ConfigAttribute.WINDOW_HEIGHT);
        screenWidth = (int) game.getConfiguration().get(ConfigAttribute.SCREEN_WIDTH);
        screenHeight = (int) game.getConfiguration().get(ConfigAttribute.SCREEN_HEIGHT);
        buffer = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_ARGB);
    }

    /**
     * Draw operation on the internal image buffer.
     */
    public void draw() {
        Graphics2D g = (Graphics2D) buffer.getGraphics();
        // clear buffer with default color;
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, screenWidth, screenHeight);
        // draw all the things you need.

        // release Graphics API
        g.dispose();
    }

    /**
     * Copy resulting internal buffer onto the Window.
     *
     * @param w the window where to draw rendering output.
     */
    public void drawToWindow(Window w) {
        Graphics2D g = w.getGraphics2D();
        g.drawImage(buffer,
                0, 0, w.getFrame().getWidth(), w.getFrame().getHeight(),
                0, 0, screenWidth, screenHeight,
                null);
    }

    public BufferedImage getBuffer() {
        return buffer;
    }
}
