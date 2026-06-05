package data.exceptions;

/**
 * Creator : Justin Joel anak Juni (104607)
 * Tester/Reviewer  : Vince Adhar Anak Sekelie (106653)
 * 
 * Thrown by DataManager when a score value is outside the valid 0-100 range.
 *
 */
public class InvalidScoreException extends Exception {
    public InvalidScoreException(String message) {
        super(message);
    }
}
