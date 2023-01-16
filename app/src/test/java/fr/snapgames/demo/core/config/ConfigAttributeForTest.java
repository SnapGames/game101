package fr.snapgames.demo.core.config;

import fr.snapgames.demo.core.configuration.IConfigAttribute;

import java.awt.geom.Point2D;
import java.util.function.Function;

/**
 * List of Configuration attributes than can be set to specific value ot to a default value.
 * <p>
 * Documentation is auto-defined through the Enum values declaration. Don't hesitate to be precise on definition.
 * <p>
 * This would help not only users but also developers to maintain the list of attributes.
 *
 * @author Frédéric Delorme
 * @since 0.0.2
 **/
public enum ConfigAttributeForTest implements IConfigAttribute {
    TEXT_VALUE(
            "boolValue",
            "app.config.string",
            "Set a string value",
            "text_default",
            v -> v),
    INTEGER_VALUE(
            "intValue",
            "app.config.integer",
            "Set an integer value",
            0,
            Integer::valueOf),
    BOOLEAN_VALUE(
            "boolValue",
            "app.config.boolean",
            "set a boolean value",
            false,
            Boolean::valueOf),

    DOUBLE_VALUE(
            "doubleValue",
            "app.config.double",
            "set a double value",
            0.0,
            Double::valueOf),

    VECTOR2D_VALUE(
            "vetor2dValue",
            "app.config.vector2d",
            "set a Vector2D value",
            new Point2D.Double(0.0, 0.0),
            v -> stringToPoint2D(v, new Point2D.Double(0.0, 0.0))
    );

    /**
     * Convert String "v([double],[double])" to Point2D.
     *
     * @param value        the formatted String value to be converted
     * @param defaultValue the default value if no conversion possible.
     * @return Point2D value corresponding to the converted string.
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
        return new Point2D.Double(
                Double.parseDouble(interpretedValue[0]),
                Double.parseDouble(interpretedValue[1]));
    }

    private final String attrName;
    private final String attrDescription;
    private final Object attrDefaultValue;
    private final Function<String, Object> attrParser;
    private final String attrConfigKey;

    ConfigAttributeForTest(String attrName, String attrConfigKey, String attrDescription, Object attrDefaultValue, Function<String, Object> parser) {
        this.attrName = attrName;
        this.attrConfigKey = attrConfigKey;
        this.attrDescription = attrDescription;
        this.attrDefaultValue = attrDefaultValue;
        this.attrParser = parser;
    }

    @Override
    public String getAttrName() {
        return attrName;
    }

    @Override
    public Function<String, Object> getAttrParser() {
        return this.attrParser;
    }

    @Override
    public Object getDefaultValue() {
        return this.attrDefaultValue;
    }

    @Override
    public String getAttrDescription() {
        return this.attrDescription;
    }

    @Override
    public String getConfigKey() {
        return attrConfigKey;
    }
}
