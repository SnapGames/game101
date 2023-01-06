package fr.snapgames.demo.gdemoapp;

import fr.snapgames.demo.core.Utils;
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
            v -> Integer.valueOf(v)),
    /**
     * the width of our game's window
     */
    WINDOW_WIDTH(
            "windowWidth",
            "app.window.width",
            "Set the Display Window width",
            640,
            Integer::valueOf),
    /**
     * the height of our game's window
     */
    WINDOW_HEIGHT(
            "windowHeight",
            "app.window.height",
            "Set the Display Window height",
            400,
            Integer::valueOf),
    /**
     * flag indicates if game's window is on full screen.
     */

    WINDOW_FULL_SCREEN(
            "windowFullscreen",
            "app.window.fullscreen",
            "Switch the Window to full screen",
            false,
            Boolean::valueOf),
    /**
     * Width for the Screen rendering size
     */
    SCREEN_WIDTH(
            "screenWidth",
            "app.screen.width",
            "set the screen width",
            320,
            Integer::valueOf),
    /**
     * Height for the Screen rendering size
     */
    SCREEN_HEIGHT(
            "screenHeight",
            "app.screen.height",
            "set the screen height",
            200,
            Integer::valueOf),
    PLAY_AREA_WIDTH(
            "playAreaWidth",
            "app.physic.world.play.area.width",
            "set the width of the play area",
            320,
            Double::valueOf),
    PLAY_AREA_HEIGHT(
            "playAreaHeight",
            "app.physic.world.play.area.height",
            "set the height of the play area",
            200,
            Double::valueOf),
    PHYSIC_GRAVITY(
            "physicGravity",
            "app.physic.world.gravity",
            "set the 2D vector for gravity applied by physic engine",
            new Point2D.Double(0.0, 0.0),
            v -> {
                return stringToPoint2D(v, new Point2D.Double(0.0, 0.0));
            }
    ),
    PHYSIC_MIN_SPEED(
            "physicSpeedMin",
            "app.physic.world.speed.min",
            "set the minimum speed below considered as zero",
            0.01,
            Double::valueOf
    ),
    PHYSIC_MAX_SPEED_X(
            "physicSpeedXMax",
            "app.physic.world.speed.x.max",
            "set the maximum speed on X axis",
            0.01,
            Double::valueOf
    ),
    PHYSIC_MAX_SPEED_Y(
            "physicSpeedYMax",
            "app.physic.world.speed.y.max",
            "set the maximum speed on Y axis",
            0.01,
            Double::valueOf
    ),
    PHYSIC_MIN_ACCELERATION(
            "physicMinAcceleration",
            "app.physic.world.acceleration.min",
            "Set the minimum acceleration below considered as zero",
            0.00001,
            Double::valueOf),
    PHYSIC_MAX_ACCELERATION_X(
            "physicMaxAccelerationX",
            "app.physic.world.acceleration.x.max",
            "Set the maximum acceleration on X axis",
            0.2,
            Double::valueOf),
    PHYSIC_MAX_ACCELERATION_Y(
            "physicMaxAccelerationY",
            "app.physic.world.acceleration.y.max",
            "Set the maximum acceleration on Y axis",
            0.2,
            Double::valueOf);

    /**
     * Convert String "v([double],[double])" to Point2D.
     *
     * @param value
     * @param defaultValue
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
        Point2D convertedValue = new Point2D.Double(
                Double.parseDouble(interpretedValue[0]),
                Double.parseDouble(interpretedValue[1]));
        return convertedValue;
    }

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
