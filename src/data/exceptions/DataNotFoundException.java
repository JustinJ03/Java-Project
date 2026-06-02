package data.exceptions;

/**
 * Thrown by DataManager when saved data cannot be located or read from disk.
 *
 * Member 4 -- this file is complete.
 */
public class DataNotFoundException extends Exception {
    public DataNotFoundException(String message) {
        super(message);
    }
    public DataNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
