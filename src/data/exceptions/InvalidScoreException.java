// Starter file. Implement the // TODO blocks below.
package data.exceptions;

/** Thrown when a score is outside the valid 0..100 range. */
public class InvalidScoreException extends Exception {
    public InvalidScoreException(String message) { super(message); }
}
