package soft.musala.drone.utils;

import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Pargev A. created on 15.04.2023
 */
public class TestUtils {

    /**
     * Execute method and check assertion of exception and message.
     *
     * @param executable Method via potential exception
     * @param exception  Exception to check
     */
    public static void checkException(Executable executable, Exception exception) {
        var throwableException = assertThrows(exception.getClass(), executable);
        assertEquals(exception.getMessage(), throwableException.getMessage());
    }
}
