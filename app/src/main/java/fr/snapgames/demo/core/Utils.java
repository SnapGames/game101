package fr.snapgames.demo.core;

import java.awt.geom.Point2D;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.stream.Collectors;

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

    /**
     * Convert String "v([double],[double])" to Point2D.
     *
     * @param value
     * @param defaultValue
     * @return Point2D value corresponding to the converted string.
     * @since 0.0.8
     */
    private static Point2D stringToPoint2D(String value, Point2D defaultValue) {
        if (value == null || value.equals("")) {
            return defaultValue;
        }
        String[] interpretedValue = value
                .substring(
                        "v(".length(),
                        value.length() - ")".length())
                .split(",");
        Point2D convertedValue = new Point2D.Double(
                Double.parseDouble(interpretedValue[0]),
                Double.parseDouble(interpretedValue[1]));
        return convertedValue;
    }

    public static String prepareStatsString(Map<String, Object> attributes) {
        return "[ " + attributes.entrySet().stream().sorted(Map.Entry.comparingByKey()).map(entry -> {
            String value = "";
            switch (entry.getValue().getClass().getSimpleName()) {
                case "Double", "double", "Float", "float" -> {
                    value = String.format("%04.2f", entry.getValue());
                }
                case "Integer", "int" -> {
                    value = String.format("%5d", entry.getValue());
                }
                default -> {
                    value = entry.getValue().toString();
                }
            }
            return
                    entry.getKey().substring(((String) entry.getKey().toString()).indexOf('_') + 1)
                            + ":"
                            + value;
        }).collect(Collectors.joining(" | ")) + " ]";
    }
}
