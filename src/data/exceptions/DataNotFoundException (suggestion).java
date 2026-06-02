// SUGGESTION (AI-generated reference). Rename to DataNotFoundException.java and merge into your own work.
// Do not overwrite code you have already written.  Owner: Member 4
package data.exceptions;

/** Thrown when stored data cannot be located or read. */
public class DataNotFoundException extends Exception {
    public DataNotFoundException(String message) { super(message); }
    public DataNotFoundException(String message, Throwable cause) { super(message, cause); }
}
