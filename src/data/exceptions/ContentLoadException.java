// Starter file. Implement the // TODO blocks below.
package data.exceptions;

/** Thrown when a resource file (quiz/learning content) cannot be loaded. */
public class ContentLoadException extends Exception {
    public ContentLoadException(String message, Throwable cause) { super(message, cause); }
}
