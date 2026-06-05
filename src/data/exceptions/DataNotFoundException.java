package data.exceptions;

/**
 * Creator : Justin Joel anak Juni (104607)
 * Tester/Reviewer  : Vince Adhar Anak Sekelie (106653)
 * 
 * Thrown by DataManager when saved data cannot be located or read from disk.
 *
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
