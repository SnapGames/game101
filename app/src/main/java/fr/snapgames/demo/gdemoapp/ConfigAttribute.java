package fr.snapgames.demo.gdemoapp;

import java.util.function.Function;

/**
 * List of Configuration attributes than can be set to specific value ot to a default value.
 *
 * @author Frédéric Delorme
 * @since 0.0.2
 **/
public enum ConfigAttribute {
    /*
     * debug mode argument.
     */
    DEBUG_MODE(
            "debugMode",
            "Setting the debug mode (0 to 5)",
            0,
            v -> Integer.valueOf(v)),
    /*
     * number of loop cycle before exit in test mode.
     */
    EXIT_TEST_COUNT_FRAME(
            "testCounter",
            "if debug>0, set a number of frame to execute before exit (test mode)",
            -1,
            v -> Integer.valueOf(v));

    private final String attrName;
    private final String attrDescription;
    private final Object attrDefaultValue;
    private final Function<String, Object> attrParser;

    ConfigAttribute(String attrName, String attrDescription, Object attrDefaultValue, Function<String, Object> parser) {
        this.attrName = attrName;
        this.attrDescription = attrDescription;
        this.attrDefaultValue = attrDefaultValue;
        this.attrParser = parser;
    }

    public String getAttrName() {
        return attrName;
    }

    public Function<String, Object> getAttrParser() {
        return this.attrParser;
    }

    public Object getDefaultValue() {
        return this.attrDefaultValue;
    }

    public String getAttrDescription() {
        return this.attrDescription;
    }
}
