package data.exceptions;

/**
 * Thrown by DataManager when a score value is outside the valid 0-100 range.
 *
 * Member 4 -- this file is complete.
 */
public class InvalidScoreException extends Exception {
    public InvalidScoreException(String message) {
        super(message);
    }
}
