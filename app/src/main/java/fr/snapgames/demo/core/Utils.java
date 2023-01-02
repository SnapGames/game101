package fr.snapgames.demo.core;

/**
 * Add some utilities and helpers to convert values to String.
 *
 * @author Frédéric Delorme
 * @since 0.0.3
 **/
public class Utils {
    /**
     * Convert a long duration value to a formatted String value "D hh:mm:ss.SSS".
     *
     * @param duration in ms
     * @return formatted String "%d d - %02d:%02d:%02d.%03d"
     */
    public static String formatDuration(long duration) {
        int ms, s, m, h, d;
        double dec;
        double time = duration * 1.0;

        time = (time / 1000.0);
        dec = time % 1;
        time = time - dec;
        ms = (int) (dec * 1000.0);

        time = (time / 60.0);
        dec = time % 1;
        time = time - dec;
        s = (int) (dec * 60.0);

        time = (time / 60.0);
        dec = time % 1;
        time = time - dec;
        m = (int) (dec * 60.0);

        time = (time / 24.0);
        dec = time % 1;
        time = time - dec;
        h = (int) (dec * 24.0);

        d = (int) time;

        return (String.format("%d d - %02d:%02d:%02d.%03d", d, h, m, s, ms));
    }
}
