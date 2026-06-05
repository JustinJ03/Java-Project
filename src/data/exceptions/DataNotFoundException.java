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
    // wraps the root IOException so callers know whether the file is missing or unreadable.
    public DataNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
