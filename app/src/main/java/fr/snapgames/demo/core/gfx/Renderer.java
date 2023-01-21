package fr.snapgames.demo.core.gfx;


import fr.snapgames.demo.core.Game;
import fr.snapgames.demo.core.entity.Entity;
import fr.snapgames.demo.core.gfx.plugins.DrawHelperPlugin;
import fr.snapgames.demo.core.gfx.plugins.GameObjectDrawHelperPlugin;
import fr.snapgames.demo.core.gfx.plugins.GridObjectDrawHelperPlugin;
import fr.snapgames.demo.gdemoapp.ConfigAttribute;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

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

    private Map<Class<? extends Entity<?>>, DrawHelperPlugin<? extends Entity<?>>> plugins = new HashMap<>();
    private String filterWhiteList;
    private String filterBlackList;

    /**
     * Initialize the Renderer service with its parent game.
     *
     * @param g
     */
    public Renderer(Game g) {
        this.game = g;
        // retrieve mandatory configuration
        windowWidth = (int) game.getConfiguration().get(ConfigAttribute.WINDOW_WIDTH);
        windowHeight = (int) game.getConfiguration().get(ConfigAttribute.WINDOW_HEIGHT);
        screenWidth = (int) game.getConfiguration().get(ConfigAttribute.SCREEN_WIDTH);
        screenHeight = (int) game.getConfiguration().get(ConfigAttribute.SCREEN_HEIGHT);

        // Debug information draw Entity's filtering list
        filterWhiteList = (String) game.getConfiguration().get(ConfigAttribute.DEBUG_WHILE_LIST);
        filterBlackList = (String) game.getConfiguration().get(ConfigAttribute.DEBUG_BLACK_LIST);

        // Initialize internal rendering buffer
        buffer = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_ARGB);
        // add default rendering helpers
        addPlugin(new GameObjectDrawHelperPlugin());
        addPlugin(new GridObjectDrawHelperPlugin());

    }


    public void addPlugin(DrawHelperPlugin<? extends Entity<?>> dhp) {
        this.plugins.put(dhp.getEntityType(), dhp);
    }

    /**
     * Draw operation on the internal image buffer.
     *
     * @param attributes a Map of object to be used at rendering time, provisioned by the engine itself (information from the {@link Game#loop()})
     */
    public void draw(Map<String, Object> attributes) {
        Graphics2D g = (Graphics2D) buffer.getGraphics();
        // clear buffer with default color;
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, screenWidth, screenHeight);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        // draw all the things you need.
        game.getEntityManager().getEntities()
                .stream()
                .sorted((o1, o2) -> o1.getLayer() > o2.getLayer() ? 1 : (o1.getPriority() > o1.getPriority() ? 1 : -1))
                .forEach(e -> {
                    // draw objects
                    drawEntity(g, e);

                    if (game.getDebugMode() > 0) {
                        drawDebugInformation(g, e);
                    }
                });
        // draw some debug information.
        drawDisplayDebugLine(g, attributes);

        // release Graphics API
        g.dispose();
    }

    private void drawDisplayDebugLine(Graphics2D g, Map<String, Object> attributes) {
        if (game.getDebugMode() > 0) {
            g.setFont(g.getFont().deriveFont(10.0f));
            g.setColor(new Color(0.3f, 0.0f, 0.0f, 0.5f));
            g.fillRect(0, buffer.getHeight() - 20, buffer.getWidth(), 20);
            g.setColor(Color.ORANGE);
            int ups = (int) (attributes.containsKey("game.ups") ? attributes.get("game.ups") : -1);
            int fps = (int) (attributes.containsKey("game.fps") ? attributes.get("game.fps") : -1);
            double gameTime = (double) (attributes.containsKey("game.time") ? attributes.get("game.time") : -1.0);
            String debugLine = String.format("[ dbg:%d | fps:%02d | ups:%02d |pause:%s | obj:%d | g:%1.3f | gtime: %04.3fs]",
                    game.getDebugMode(),
                    fps,
                    ups,
                    game.isPaused() ? "on" : "off",
                    game.getEntityManager().getEntities().size(),
                    game.getPhysicEngine().getWorld().getGravity().getY(),
                    Math.abs(gameTime / 1000.0));
            g.drawString(debugLine, 8, buffer.getHeight() - 8);
        }
    }

    private void drawDebugInformation(Graphics2D g, Entity<?> e) {

        if (game.getDebugMode() >= e.debug
                && filterWhiteList.contains(e.name)
                && !filterBlackList.contains(e.name)) {
            g.setColor(Color.ORANGE);
            g.draw(e.box);
            if (game.getDebugMode() > 1) {
                int offX = (int) e.x + 4;
                int offY = (int) e.y;
                g.setFont(g.getFont().deriveFont(8.5f));
                long nbLines = e.getDebugInfo().stream().filter(s -> game.getDebugMode() >= Integer.parseInt(s.substring(1, 2))).count();
                int hh = (int) (g.getFontMetrics().getHeight()
                        * (nbLines - 1));
                if (e.y + hh > buffer.getHeight()) {
                    offY = buffer.getHeight() - hh;
                }

                int ww = g.getFontMetrics().stringWidth(e.getDebugInfo().stream().max(Comparator.comparingInt(String::length)).get());

                if (e.x + ww > buffer.getWidth()) {
                    offX = buffer.getWidth() - ww;
                }

                int l = 0;
                for (String s : e.getDebugInfo()) {
                    if (s.startsWith("(")) {
                        if (game.getDebugMode() >= Integer.parseInt(s.substring(1, 2))) {
                            l += 10;
                            g.setColor(new Color(0.0f, 0.0f, 0.4f, 0.5f));
                            g.fillRect((int) (offX + e.width + 1), offY - 10 + l, ww + 2, 10);
                            g.setColor(Color.WHITE);
                            g.drawString(s.substring(3), (int) (offX + e.width + 4), offY + l);
                        }
                    } else {
                        l += 10;
                        g.drawString(s, (int) (offX + e.width + 4), offY + l);
                    }
                }
                g.drawLine((int) (e.x + e.width + 1.0), (int) e.y, (int) (offX + e.width + 3.0), offY);
            }
        }
    }

    private void drawEntity(Graphics2D g, Entity<?> e) {
        if (plugins.containsKey(e.getClass())) {
            DrawHelperPlugin<? extends Entity<?>> dhp = plugins.get(e.getClass());
            dhp.draw(this, g, e);
        }
    }

    /**
     * Copy resulting internal buffer onto the Window.
     *
     * @param w the window where to draw rendering output.
     */
    public void drawToWindow(Window w) {
        Graphics2D g = w.getGraphics2D();
        if (w.isDisplayable()) {
            g.drawImage(buffer,
                    0, 0, w.getFrame().getWidth(), w.getFrame().getHeight(),
                    0, 0, screenWidth, screenHeight,
                    null);
        }
    }

    public BufferedImage getBuffer() {
        return buffer;
    }
}
