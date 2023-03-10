package fr.snapgames.demo.core.configuration;

import fr.snapgames.demo.core.math.Vector2D;
import fr.snapgames.demo.core.physic.Material;

import java.util.function.Function;

/**
 * Define the configuration attribute interface to implement the right accessor into the enum.
 *
 * @author Frédric Delorme
 * @since 0.0.2
 **/
public interface IConfigAttribute {


    String getAttrName();

    Function<String, Object> getAttrParser();

    Object getDefaultValue();

    String getAttrDescription();

    String getConfigKey();

    /**
     * Convert String "v([double],[double])" to {@link Vector2D}.
     *
     * @param value        the formatted String value to be converted
     * @param defaultValue the default value if no conversion possible.
     * @return a {@link Vector2D} value corresponding to the converted string.
     */
    static Vector2D stringToVector2D(String value, Vector2D defaultValue) {
        if (value == null || value.equals("")) {
            return defaultValue;
        }
        String[] interpretedValue = value
                .substring(
                        "v(".length(),
                        value.length() - ")".length())
                .split(",");
        return new Vector2D(
                Double.parseDouble(interpretedValue[0]),
                Double.parseDouble(interpretedValue[1]));
    }

    /**
     * Convert String "mat([String],[double],[double],[double])" to {@link Material}.
     *
     * @param value        the formatted String value to be converted
     * @param defaultValue the default value if no conversion possible.
     * @return a {@link Material} value corresponding to the converted string.
     */
    static Material stringToMaterial(String value, Material defaultValue) {
        if (value == null || value.equals("")) {
            return defaultValue;
        }
        String[] interpretedValue = value
                .substring(
                        "mat(".length(),
                        value.length() - ")".length())
                .split(",");
        return new Material(
                interpretedValue[0],
                Double.parseDouble(interpretedValue[1]),
                Double.parseDouble(interpretedValue[2]),
                Double.parseDouble(interpretedValue[3]));
    }
}
