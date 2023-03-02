package fr.snapgames.demo.core.io;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The {@link ResourceManager} is the resource store where to load all needed resources.
 * <p>
 * Sample usage :
 *
 * <pre>
 *  // at initialization
 * ResourceManager.load("/images/my_image.png");
 * ResourceManager.load("/font/my_font.ttf");
 *
 * // later to use resource :
 * BufferedImage img = ResourceManager.getImage("images/my_image.png");
 * Font font = ResourceManager.getFont("images/my_font.ttf");
 * </pre>
 *
 * @author Frédéric Delorme
 * @Since 0.1.4
 */
public class ResourceManager {
    private static Map<String, Object> cache = new ConcurrentHashMap<>();

    public static BufferedImage getImage(String path) {
        if (!cache.containsKey(path)) {
            load(path);
        }
        return (BufferedImage) cache.get(path);
    }

    public static Font getFont(String path) {
        if (!cache.containsKey(path)) {
            load(path);
        }
        return (Font) cache.get(path);
    }

    /**
     * Load a resource to the resource the managed ones. Load an image or a font
     * resources according to their file extension.
     * <ul>
     * <li><code>jpg</code>, <code>png</code> are loaded as {@link BufferedImage} resource,</li>
     * <li><code>ttf</code> is loaded as {@link Font} resource.</li>
     * </ul>
     *
     * @param path the file path to the resource to be loaded and managed.
     */
    public static void load(String path) {
        if (!path.contains(".")) {
            return;
        }
        String ext = path.substring(path.lastIndexOf(".") + 1, path.length()).toUpperCase();
        switch (ext) {
            case "PNG", "JPG", "GIF" -> {
                BufferedImage img = null;
                if (!cache.containsKey(path)) {
                    try {
                        img = ImageIO.read(ResourceManager.class.getResourceAsStream(path));
                        cache.put(path, img);
                    } catch (IOException e) {
                        System.err.printf("Game:Unable to read image %s: %s", path, e.getMessage());
                    }
                }

            }
            case "TTF" -> {
                // load a Font resource
                Font font = null;
                if (!cache.containsKey(path)) {
                    try {
                        InputStream stream = ResourceManager.class.getResourceAsStream(path);
                        font = Font.createFont(Font.TRUETYPE_FONT, stream);
                        if (font != null) {
                            cache.put(path, font);
                        }
                    } catch (FontFormatException | IOException e) {
                        System.err.printf("Unable to read font from %s%n", path);
                    }
                }
                if (Optional.ofNullable(font).isPresent()) {
                    cache.put(path, font);
                }
            }
            default -> {
                System.err.printf("File format unknown for %s%n", path);
            }
        }
    }
}