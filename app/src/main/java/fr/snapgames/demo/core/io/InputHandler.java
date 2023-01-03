package fr.snapgames.demo.core.io;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * The {@link InputHandler} service is provided a nice implementation to manage Event from mouse and keyboard,
 * and adapt to our own Game framework implementing the {@link KeyListener} and the {@link MouseListener}.
 * <ul>
 *     <li>2 internal buffers are keeping status for keys: {@link InputHandler#keys} and {@link InputHandler#preKeys},</li>
 *     <li>and a simple bunch of boolean holds mouse buttons status
 *     {@link InputHandler#mouseButtons} and {@link InputHandler#preMouseButtons}.</li>
 * </ul>
 * <p>
 * And a set of methods will request status of those buffers to get mouse buttons and keys status:
 *
 * <ul>
 *     <li>{@link InputHandler#getKey(int)} will return the current corresponding key status on its {@link KeyEvent} code,</li>
 *     <li>{@link InputHandler#getMouseButton(int)} returns the status of one of the mouse button.</li>
 * </ul>
 *
 * @author Frédéric Delorme
 * @since 0.0.6
 */
public class InputHandler implements KeyListener, MouseListener {

    /**
     * Internal key state buffer for current status
     */
    private boolean[] keys = new boolean[65535];
    /**
     * Internal key state buffer holding previous status
     */
    private boolean[] preKeys = new boolean[65535];

    /**
     * Current mouse 2D position
     */
    private Point mousePosition;
    /**
     * Current mouse button states
     */
    private boolean[] mouseButtons;
    /**
     * Previous mouse button states
     */
    private boolean[] preMouseButtons;

    /**
     * INputHandler initialization:
     * <ol>
     *     <li>setting key buffers to maximum key code value</li>
     *     <li>sizing the buffer according to the mouse info system</li>
     * </ol>
     */
    public InputHandler() {
        int msButtons = MouseInfo.getNumberOfButtons();
        preMouseButtons = new boolean[msButtons];
        mouseButtons = new boolean[msButtons];
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // nothing done now with that event.
    }

    @Override
    public void keyPressed(KeyEvent e) {
        preKeys[e.getKeyCode()] = keys[e.getKeyCode()];
        keys[e.getKeyCode()] = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        preKeys[e.getKeyCode()] = keys[e.getKeyCode()];
        keys[e.getKeyCode()] = true;
    }

    /**
     * Retrieve the current status for the keyCode from {@link KeyEvent}
     *
     * @param keyCode the key code to retrieve status for.
     * @return true if key is currently active.
     */
    public boolean getKey(int keyCode) {
        return keys[keyCode];
    }

    /**
     * Return if the keyCode from {@link KeyEvent} has just been pressed.
     *
     * @param keyCode the key code to retrieve status for.
     * @return true if key is pressed.
     */
    public boolean isKeyPressed(int keyCode) {
        return !preKeys[keyCode] && keys[keyCode];
    }

    /**
     * Return if the keyCode from {@link KeyEvent} has just been released.
     *
     * @param keyCode the key code to retrieve status for.
     * @return true if key is pushed.
     */
    public boolean isKeyReleased(int keyCode) {
        return preKeys[keyCode] && !keys[keyCode];
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        mousePosition = e.getPoint();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        mousePosition = e.getPoint();
        preMouseButtons[e.getClickCount()] = mouseButtons[e.getClickCount()];
        mouseButtons[e.getClickCount()] = true;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        mousePosition = e.getPoint();
        preMouseButtons[e.getClickCount()] = mouseButtons[e.getClickCount()];
        mouseButtons[e.getClickCount()] = false;
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        mousePosition = e.getPoint();
    }

    @Override
    public void mouseExited(MouseEvent e) {
        mousePosition = e.getPoint();
    }

    /**
     * Return the mouse button <code>mouseButtonCode</code> status
     *
     * @param mouseButtonCode the mouse button number (max value defined at InputHandler initialization).
     * @return true if active.
     */
    public boolean getMouseButton(int mouseButtonCode) {
        return mouseButtons[mouseButtonCode];
    }

    /**
     * Return if the mouseButtonCode has just been pressed.
     *
     * @param mouseButtonCode the mouse button number to retrieve status for.
     * @return true if louse button is pressed.
     */
    public boolean isMouseButtonPressed(int mouseButtonCode) {
        return !preMouseButtons[mouseButtonCode] && mouseButtons[mouseButtonCode];
    }

    /**
     * Return if the mouseButtonCode has just been released.
     *
     * @param mouseButtonCode the mouse button number to retrieve status for.
     * @return true if louse button is pressed.
     */
    public boolean isMouseButtonReleased(int mouseButtonCode) {
        return preMouseButtons[mouseButtonCode] && !mouseButtons[mouseButtonCode];
    }
}
