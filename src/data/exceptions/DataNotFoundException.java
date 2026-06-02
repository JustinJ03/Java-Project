// Starter file. Implement the // TODO blocks below.
package data.exceptions;

/** Thrown when stored data cannot be located or read. */
public class DataNotFoundException extends Exception {
    public DataNotFoundException(String message) { super(message); }
    public DataNotFoundException(String message, Throwable cause) { super(message, cause); }
}
