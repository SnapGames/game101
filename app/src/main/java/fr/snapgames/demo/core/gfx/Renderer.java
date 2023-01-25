package fr.snapgames.demo.core.gfx;


import fr.snapgames.demo.core.Game;
import fr.snapgames.demo.core.entity.Camera;
import fr.snapgames.demo.core.entity.Entity;
import fr.snapgames.demo.core.gfx.plugins.DrawHelperPlugin;
import fr.snapgames.demo.core.gfx.plugins.GameObjectDrawHelperPlugin;
import fr.snapgames.demo.core.gfx.plugins.GridObjectDrawHelperPlugin;
import fr.snapgames.demo.gdemoapp.ConfigAttribute;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.*;

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
    private final Rectangle2D playArea;

    /**
     * Screen width
     */
    int screenWidth;
    /**
     * Screen height
     */
    int screenHeight;

    /**
     * The current active camera for this rendering.
     */
    private Camera currentCamera;

    private Map<Class<? extends Entity<?>>, DrawHelperPlugin<? extends Entity<?>>> plugins = new HashMap<>();
    private String filterWhiteList;
    private String filterBlackList;
    private boolean rendering;

    /**
     * Initialize the {@link Renderer} service with its parent {@link Game} instance.
     *
     * @param g the parent Game for the {@link Renderer} service
     */
    public Renderer(Game g) {
        this.game = g;
        // retrieve mandatory configuration

        screenWidth = (int) game.getConfiguration().get(ConfigAttribute.SCREEN_WIDTH);
        screenHeight = (int) game.getConfiguration().get(ConfigAttribute.SCREEN_HEIGHT);
        double playAreaWidth = (double) game.getConfiguration().get(ConfigAttribute.PLAY_AREA_WIDTH);
        double playAreaHeight = (double) game.getConfiguration().get(ConfigAttribute.PLAY_AREA_HEIGHT);
        playArea = new Rectangle2D.Double(0, 0, playAreaWidth, playAreaHeight);

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
        rendering = true;
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
                    // Move view to camera view
                    if (Optional.ofNullable(currentCamera).isPresent() && e.isNotStickToCamera()) {
                        g.translate(-currentCamera.x, -currentCamera.y);
                    }
                    // draw objects
                    drawEntity(g, e);
                    // move back from camera view
                    if (Optional.ofNullable(currentCamera).isPresent() && e.isNotStickToCamera()) {
                        g.translate(currentCamera.x, currentCamera.y);
                    }

                });
        // draw entity's display debug information
        if (game.getDebugMode() > 0) {
            game.getEntityManager().getEntities()
                    .stream()
                    .sorted((o1, o2) -> o1.getLayer() > o2.getLayer() ? 1 : (o1.getPriority() > o1.getPriority() ? 1 : -1))
                    .forEach(e -> {
                        // Move view to camera view
                        if (Optional.ofNullable(currentCamera).isPresent() && e.isNotStickToCamera()) {
                            g.translate(-currentCamera.x, -currentCamera.y);
                        }
                        // draw Entity debug display information.
                        drawDebugInformation(g, e);
                        // move back from camera view
                        if (Optional.ofNullable(currentCamera).isPresent() && e.isNotStickToCamera()) {
                            g.translate(currentCamera.x, currentCamera.y);
                        }
                    });
            // draw some debug information.
            drawDisplayDebugLine(g, attributes);
        }

        // release Graphics API
        g.dispose();
        rendering = false;
    }

    private void drawDisplayDebugLine(Graphics2D g, Map<String, Object> attributes) {
        g.setFont(g.getFont().deriveFont(10.0f));
        g.setColor(new Color(0.3f, 0.0f, 0.0f, 0.5f));
        g.fillRect(0, buffer.getHeight() - 20, buffer.getWidth(), 20);
        g.setColor(Color.ORANGE);
        int ups = (int) (attributes.getOrDefault("game.ups", -1));
        int fps = (int) (attributes.getOrDefault("game.fps", -1));
        double gameTime = (double) (attributes.getOrDefault("game.time", -1.0));
        String debugLine = String.format("[ dbg:%d | f:%02d u:%02d |>%s| scn:%s |o:%d | g:%1.3f | gtime: %04.3fs]",
                game.getDebugMode(),
                fps,
                ups,
                game.isPaused() ? "off" : "on",
                game.getSceneManager().getCurrent().getName(),
                game.getEntityManager().getEntities().size(),
                game.getPhysicEngine().getWorld().getGravity().getY(),
                Math.abs(gameTime / 1000.0));
        g.drawString(debugLine, 8, buffer.getHeight() - 8);
    }

    private void drawDebugInformation(Graphics2D g, Entity<?> e) {

        if (game.getDebugMode() >= e.debug
                && filteredName(filterWhiteList, e.name)
                && !filteredName(filterBlackList, e.name)) {
            g.setColor(Color.ORANGE);
            g.draw(e.box);
            if (game.getDebugMode() > 1) {
                int offX = (int) e.x + 4;
                int offY = (int) e.y;
                g.setFont(g.getFont().deriveFont(8.5f));
                long nbLines = e.getDebugInfo().stream().filter(s -> game.getDebugMode() >= Integer.parseInt(s.substring(1, 2))).count();
                int hh = (int) (g.getFontMetrics().getHeight()
                        * (nbLines - 1));
                if (e.y + hh > playArea.getHeight()) {
                    offY = (int) playArea.getHeight() - hh;
                }

                int ww = g.getFontMetrics().stringWidth(e.getDebugInfo().stream().max(Comparator.comparingInt(String::length)).get());

                if (e.x + ww > playArea.getWidth()) {
                    offX = (int) playArea.getWidth() - ww;
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

    private boolean filteredName(String filter, String entityName) {
        if (!filter.equals("")) {
            return Arrays.stream(filter.split(",")).anyMatch(entityName::contains);
        } else {
            return false;
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

    /**
     * Set the active {@link Camera} for the rendering process.
     *
     * @param currentCamera a {@link Camera} instance.
     */
    public void setCurrentCamera(Camera currentCamera) {
        this.currentCamera = currentCamera;
    }

    /**
     * Return the rendering flag value.
     *
     * @return a boolean value, if true, the rendering is now processing, if false, it is not currently processing.
     */
    public boolean isRendering() {
        return rendering;
    }
}
