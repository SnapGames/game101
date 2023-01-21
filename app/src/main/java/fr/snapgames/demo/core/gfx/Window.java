package fr.snapgames.demo.core.gfx;

import fr.snapgames.demo.core.io.InputHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyListener;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * THe {@link Window} object intend to display on screen the content of our application game.
 *
 * @author Frédéric Delorme
 * @since 0.0.4
 */
public class Window extends JPanel {

    public String title;
    private JFrame frame;
    private boolean fullScreenMode;
    ComponentAdapter windowSizeAdapter;
    private int oldWidth;
    private int oldHeight;
    List<KeyListener> listeners = new CopyOnWriteArrayList<>();

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
        this.setSize(width, height);
        this.fullScreenMode = bFullScreenMode;
        windowSizeAdapter = new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent componentEvent) {
                if (!isFullScreen()) {
                    oldWidth = frame.getWidth();
                    oldHeight = frame.getHeight();
                    setSize(frame.getWidth(), frame.getHeight());
                }
            }
        };
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
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();

        frame.setVisible(false);
        backUpListeners();
        if (bFullScreen) {
            oldWidth = frame.getWidth();
            oldHeight = frame.getHeight();
            frame.dispose();
            frame.setUndecorated(true);
            gd.setFullScreenWindow(frame);
            //frame.removeComponentListener(windowSizeAdapter);
            this.fullScreenMode = true;
        } else {
            frame.dispose();
            frame = new JFrame(title);
            frame.setUndecorated(false);
            gd.setFullScreenWindow(null);
            Dimension dim = new Dimension(oldWidth, oldHeight);
            this.setPreferredSize(dim);
            frame.add(this);
            //frame.addComponentListener(windowSizeAdapter);
            frame.pack();
            this.fullScreenMode = false;
        }
        restoreListeners();
        frame.setVisible(true);
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
        frame = new JFrame(title);
        this.setSize(dim);
        this.setPreferredSize(dim);
        this.setMaximumSize(dim);
        frame.setResizable(true);
        frame.setContentPane(this);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setFocusTraversalKeysEnabled(true);
        frame.pack();
        frame.setVisible(true);
        frame.requestFocus();

    }

    private void restoreListeners() {
        for (KeyListener kl : listeners) {
            frame.addKeyListener(kl);
        }
    }

    private void backUpListeners() {
        listeners.clear();
        for (KeyListener kl : frame.getKeyListeners()) {
            listeners.add(kl);
        }
    }


    /**
     * Add the {@link InputHandler} to manage keys and mouse.
     *
     * @param inputHandler the {@link InputHandler} instance to be connected to this {@link Window}.
     */
    public void addListener(InputHandler inputHandler) {
        frame.addKeyListener(inputHandler);
        frame.addMouseListener(inputHandler);
        backUpListeners();
    }

    /**
     * request to close the {@link Window} and dispose all its components.
     */
    public void close() {
        frame.setVisible(false);
        frame.dispose();
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
     * Retrieve the initialized frame for that {@link Window}.
     *
     * @return a JFrame instance for this {@link Window}.
     */
    public JFrame getFrame() {
        return this.frame;
    }


    /**
     * Retrieve the {@link Graphics2D} API to draw in the {@link Window}.
     *
     * @return the {@link Graphics2D} instance to be used to draw on {@link Window}.
     */
    public Graphics2D getGraphics2D() {
        return (Graphics2D) frame.getGraphics();
    }
}
