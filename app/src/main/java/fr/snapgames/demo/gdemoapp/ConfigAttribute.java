package fr.snapgames.demo.gdemoapp;

import fr.snapgames.demo.core.configuration.IConfigAttribute;

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
public enum ConfigAttribute implements IConfigAttribute {
    /**
     * The required name for this application to be displayed on the window.
     */
    APP_TITLE(
            "appTitle",
            "app.main.title",
            "Set the name of the application to be displayed in log and UI",
            "GDemoApp",
            v -> v),
    /*
     * debug mode argument.
     */
    DEBUG_MODE(
            "debugMode",
            "app.debug.mode",
            "Setting the debug mode (0 to 5)",
            0,
            v -> Integer.valueOf(v)),
    /*
     * number of loop cycle before exit in test mode.
     */
    EXIT_TEST_COUNT_FRAME(
            "testCounter",
            "app.test.counter",
            "if debug>0, set a number of frame to execute before exit (test mode)",
            -1,
            v -> Integer.valueOf(v)),
    /**
     * the FPS required for looping and rendering process.
     */
    RENDER_FPS(
            "fps",
            "app.render.fps",
            "set the frame per second for the render engine",
            60,
            v -> Integer.valueOf(v));
    private final String attrName;
    private final String attrDescription;
    private final Object attrDefaultValue;
    private final Function<String, Object> attrParser;
    private String attrConfigKey;

    ConfigAttribute(String attrName, String attrConfigKey, String attrDescription, Object attrDefaultValue, Function<String, Object> parser) {
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

    /**
     * @return
     */
    @Override
    public String getConfigKey() {
        return attrConfigKey;
    }
}
