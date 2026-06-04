package data.exceptions;

/**
 * ContentLoadException.java
 * Thrown when a resource file (questions.txt, content files, etc.) cannot be loaded.
 *
 * Used by:
 *   - QuizModule (Member 2) when questions.txt is missing or malformed
 *   - LearningModule (Member 1) if content files fail to load
 *
 * OOP: custom checked exception; extends Exception for compile-time enforcement.
 *
 * Creator : Member 4
 * Tester  : Adriana Umang Anak Nawang (103088)
 */
public class ContentLoadException extends Exception {

    /** Single-arg: use when the cause is unknown or not available. */
    public ContentLoadException(String message) {
        super(message);
    }

    /** Two-arg: wraps the underlying IOException or other cause. */
    public ContentLoadException(String message, Throwable cause) {
        super(message, cause);
    }
}
