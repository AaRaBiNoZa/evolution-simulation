package error_handling;

import static java.lang.System.exit;

/**
 * Signals errors in the program with appropriate messages to stderr.
 * There will be no objects of this class. There should only be one guardian
 * for a program and every method should be static.
 *
 * @author Adam Al-Hosam
 */
public abstract class Guard {
    public static void endProgramWithAMessage(String message) {
        System.err.println("ERROR: " + message);
        exit(1);
    }
}
