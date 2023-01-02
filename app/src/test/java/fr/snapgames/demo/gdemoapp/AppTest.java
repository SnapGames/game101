package fr.snapgames.demo.gdemoapp;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/*
 * Test class for the App main class.
 *
 * @author Frédéric Delorme
 * @since 0.0.1
 */
class AppTest {

    @Test
    void appHaveExitValueCounterArg() {
        App app = new App();
        app.run(new String[]{"debugMode=1", "testCounter=2"});
        Assertions.assertEquals(2, app.getExitValueTestCounter(), "The testCounter argument has not been parse correctly.");
        Assertions.assertEquals(2, app.getUpdateTestCounter(), "The Loop in test mode has not been executed the required number oftimes.");
    }
}
