package fr.snapgames.demo.core.gfx;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Optional;

/**
 * THe {@link Window} object intend to display on screen the content of our application game.
 *
 * @author Frédéric Delorme
 * @since 0.0.4
 */
public class Window extends JPanel {

    public String title;
    public int width;
    public int height;
    private JFrame frame;
    private boolean fullScreenMode;

    /**
     * Create a new Window wit a title, of size (width x height) or if requested in full screen mode.
     *
     * @param title           a string to set as the window title.
     * @param width           the horizontal size of the window to create
     * @param height          the vertical size of the window to create
     * @param bFullScreenMode if true, create this window directly in full screen mode.
     */
    public Window(String title, int width, int height, boolean bFullScreenMode) {
        this.title = title;
        this.height = height;
        this.width = width;
        this.fullScreenMode = bFullScreenMode;

        createPanel(title, width, height, fullScreenMode);
    }

    /**
     * Create a new Window wit a title, of size (width x height).
     *
     * @param title  a string to set as the window title.
     * @param width  the horizontal size of the window to create
     * @param height the vertical size of the window to create
     */

    public Window(String title, int width, int height) {
        this(title, width, height, false);
    }

    /**
     * Switch this current window between full screen and window mode.
     *
     * @param bFullScreen if true set to fullscreen, else switch to window mode.
     */
    public void switchFullScreen(boolean bFullScreen) {
        this.fullScreenMode = bFullScreen;
        if (bFullScreen && frame.isVisible()) {
            frame.setVisible(false);
            frame.dispose();
        }
        createPanel(this.title, this.width, this.height, bFullScreen);
    }

    /**
     * create the JFrame containing our Window object (as it's an extended JPanel).
     *
     * @param title          a string to set as the window title.
     * @param width          the horizontal size of the window to create
     * @param height         the vertical size of the window to create
     * @param fullScreenMode if true, create this window directly in full screen mode.
     */
    private void createPanel(
            String title,
            int width, int height,
            boolean fullScreenMode) {
        Dimension dim = new Dimension(width, height);
        if (Optional.ofNullable(frame).isPresent()) {
            frame.dispose();
            frame = null;
        }
        frame = new JFrame(title);
        frame.setPreferredSize(dim);
        frame.setSize(dim);
        frame.setContentPane(this);
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        frame.setLocationRelativeTo(null);
        if (fullScreenMode) {
            gd.setFullScreenWindow(frame);
        } else {
            gd.setFullScreenWindow(null);
        }
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setFocusTraversalKeysEnabled(true);
        frame.pack();
        frame.setVisible(true);
        frame.requestFocus();
        if (!fullScreenMode) {
            frame.addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent componentEvent) {
                    if (!isFullScreen()) {
                        setHeight(frame.getHeight());
                        setWidth(frame.getWidth());
                    }
                }
            });
        }
    }

    /**
     * request to close the {@link Window} and dispose all its components.
     */
    public void close() {
        frame.setVisible(false);
        frame.dispose();
    }

    /**
     * Set the width of this window.
     *
     * @param width the horizontal size of the window
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * Set the height of this window.
     *
     * @param height the vertical size of the window to create
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * Return true if window is in full screen mode.
     *
     * @return boolean value, true if window is in full screen mode, else false.
     */
    public boolean isFullScreen() {
        return fullScreenMode;
    }

    /**
     * Retrieve the initialized frame for that window.
     *
     * @return a JFrame instance for this window.
     */
    public JFrame getFrame() {
        return this.frame;
    }
}
