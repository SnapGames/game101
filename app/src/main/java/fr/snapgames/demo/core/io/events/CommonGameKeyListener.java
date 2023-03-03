package fr.snapgames.demo.core.io.events;


import fr.snapgames.demo.core.Game;
import fr.snapgames.demo.core.math.Vector2d;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CommonGameKeyListener implements KeyListener {

    private static final Logger logger = Logger.getLogger(CommonGameKeyListener.class.getName());

    private Game game;

    public CommonGameKeyListener(Game g) {
        game = g;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            // request exit from game demo.
            case KeyEvent.VK_ESCAPE -> {
                game.requestExit(true);
                logger.log(Level.FINEST, "    - key {} has been released", new Object[]{KeyEvent.getKeyText(KeyEvent.VK_ESCAPE)});
            }
            // rotate 0-4 debug mode
            case KeyEvent.VK_D -> {
                int debugMode = game.getDebugMode();
                game.setDebugMode(debugMode + 1 < 5 ? debugMode + 1 : 0);
                logger.log(Level.FINEST, "debug level: {}", new Object[]{game.getDebugMode()});
            }
            // set pause mode for game update
            case KeyEvent.VK_PAUSE, KeyEvent.VK_P -> {
                boolean pause = game.isPaused();
                game.requestPause(!pause);
                logger.log(Level.FINEST, "Pause mode: {}", new Object[]{!pause ? "ON" : "OFF"});
            }
            // switch to full Screen
            case KeyEvent.VK_F11 -> {
                boolean fullScreen = game.getWindow().isFullScreen();
                game.getWindow().switchFullScreen(!fullScreen);
                logger.log(Level.FINEST, "Switch FullScreen: {}", new Object[]{!fullScreen ? "ON" : "OFF"});
            }
            // switch gravity effect
            case KeyEvent.VK_G -> {
                Vector2d g = game.getPhysicEngine().getWorld().getGravity();
                game.getPhysicEngine().getWorld().setGravity(g.negate());
                logger.log(Level.FINEST, "Reverse gravity: {}", new Object[]{game.getPhysicEngine().getWorld().getGravity()});
            }
        }
    }
}
