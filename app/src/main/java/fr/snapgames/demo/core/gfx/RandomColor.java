package fr.snapgames.demo.core.gfx;

import java.awt.*;

/**
 * Random tools to generate random colors.
 *
 * @author Frédéric Delorme
 * @since 0.1.0
 */
public class RandomColor {

    /**
     * Create a random color with red, green, blue and alpha channel.
     *
     * @param red   random max value to be subtracted from 1.0f
     * @param green random max value to be subtracted from 1.0f
     * @param blue  random max value to be subtracted from 1.0f
     * @param alpha random max value to be subtracted from 1.0f
     * @return
     */
    public static Color get(float red, float green, float blue, float alpha) {
        return new Color(
                (float) (1.0 - (Math.random() * red)),
                (float) (1.0 - (Math.random() * green)),
                (float) (1.0 - (Math.random() * blue)),
                (float) (1.0 - (Math.random() * alpha))
        );
    }


    public static Color get(float minRed, float minGreen, float minBlue, float minAlpha,
                            float maxRed, float maxGreen, float maxBlue, float maxAlpha) {
        return new Color(
                (float) (minRed + (Math.random() * (maxRed - minRed))),
                (float) (minGreen + (Math.random() * (maxGreen - minGreen))),
                (float) (minBlue + (Math.random() * (maxBlue - minBlue))),
                (float) (minAlpha + (Math.random() * (maxAlpha - minAlpha)))
        );
    }
}
