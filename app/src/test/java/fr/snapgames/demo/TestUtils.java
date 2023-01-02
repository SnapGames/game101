package fr.snapgames.demo;

import javax.xml.stream.Location;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/**
 * Provide some Test utilities for comparison purpose.
 *
 * @author Frédéric Delorme
 * @since 0.0.5
 */
public class TestUtils {

    /**
     * Compare 2 {@link BufferedImage} and return a percentage of difference as double.
     * <blockquote><b>NOTE</b><p>If -1 the images have not the same size.</p></blockquote>
     *
     * @param imgA the first image to be compared
     * @param imgB the first image to be compared
     * @return double value as difference percentage between the 2 provided images.
     **/
    public static double compareImage(BufferedImage imgA, BufferedImage imgB) {

        // Assigning dimensions to image
        int width1 = imgA.getWidth();
        int width2 = imgB.getWidth();
        int height1 = imgA.getHeight();
        int height2 = imgB.getHeight();

        System.out.printf("ImageA : %d x %d%n", width1, height1);
        System.out.printf("ImageB : %d x %d%n", width2, height2);

        // Checking whether the images are of same size or
        // not
        if ((width1 != width2) || (height1 != height2))
            return -1.0;
        else {
            // By now, images are of same size
            long difference = 0;
            // treating images likely 2D matrix

            // Outer loop for rows(height)
            for (int y = 0; y < height1; y++) {

                // Inner loop for columns(width)
                for (int x = 0; x < width1; x++) {

                    int rgbA = imgA.getRGB(x, y);
                    int rgbB = imgB.getRGB(x, y);
                    int redA = (rgbA >> 16) & 0xff;
                    int greenA = (rgbA >> 8) & 0xff;
                    int blueA = (rgbA) & 0xff;
                    int redB = (rgbB >> 16) & 0xff;
                    int greenB = (rgbB >> 8) & 0xff;
                    int blueB = (rgbB) & 0xff;

                    difference += Math.abs(redA - redB);
                    difference += Math.abs(greenA - greenB);
                    difference += Math.abs(blueA - blueB);
                }
            }

            // Total number of red pixels = width * height
            // Total number of blue pixels = width * height
            // Total number of green pixels = width * height
            // So total number of pixels = width * height *
            // 3
            double total_pixels = width1 * height1 * 3;

            // Normalizing the value of different pixels
            // for accuracy

            // Note: Average pixels per color component
            double avg_different_pixels
                    = difference / total_pixels;

            // There are 255 values of pixels in total
            double percentage
                    = (avg_different_pixels / 255) * 100;
            return percentage;
        }
    }

    public static BufferedImage extractComponentToImage(Component comp, Point l) {
        BufferedImage bi;
        try {
            Robot r = new Robot();
            bi = r.createScreenCapture(new Rectangle((int) l.getX(), (int) l.getY(), comp.getWidth(), comp.getHeight()));
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }

        return bi;
    }

    public static void waitForDelayInMs(int value) {
        try {
            Thread.sleep(value);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
