package fr.snapgames.demo.gdemoapp;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/*
 * Test class for the App main class.
 *
 * @author Frédéric Delorme
 * @since 0.0.1
 */
class AppTest {
    App app;

    @BeforeEach
    void setup() {
        app = new App();
    }

    @Test
    void appCanBeExecutedNTimesInTestMode() {
        app.run(new String[]{"testCounter=20"});
        Assertions.assertEquals(20, app.getUpdateTestCounter(), "testCounter has not been initialized with argument value.");
    }

    @Test
    void appCanHaveItsNameChangedThroughCLI() {
        app.run(new String[]{"appTitle=MyTest", "testCounter=1"});
        Assertions.assertEquals("MyTest", app.getAppName(), "the appTitle has not been initialized with argument value.");
    }

    @Test
    void appCanHaveItsDebugLevelChangedThroughCLI() {
        app.run(new String[]{"debugMode=4", "testCounter=1"});
        Assertions.assertEquals(4, app.getDebugMode(), "the debugMode has not been initialized with argument value.");
    }

    @Test
    void appCanHaveItsFPSChangedThroughCLI() {
        app.run(new String[]{"fps=22", "testCounter=1"});
        Assertions.assertEquals(22, app.getTargetFps(), "the fps has not been initialized with argument value.");
    }

    @Test
    void appIsInitializedInLessThan100ms() {
        app.initialize(new String[]{"testCounter=1"});
        long elapsed = app.getInternalTime();
        Assertions.assertTrue(elapsed < 1500, "App initialization take more than the required 1500ms : " + elapsed + "ms");
    }

    @Test
    void appExitOnBadConfigurationFile() {
        app = new App("/config-test-unknown-prop.properties");
        int status = app.initialize(new String[]{});
        Assertions.assertEquals(-1, status, "wrong configuration file has not been detected");
    }

    @Test
    void appExitOnBadCLIArg() {
        int status = app.initialize(new String[]{"unknownArg=nothing"});
        Assertions.assertEquals(-1, status, "the unknown argument has not been detected.");
    }
}
