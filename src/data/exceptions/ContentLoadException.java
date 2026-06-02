package data.exceptions;

/**
 * Thrown when a resource file (content.txt or questions.txt) cannot be loaded.
 * Used by LearningModule (Member 1) and QuizModule (Member 2).
 *
 * Member 4 -- this file is complete.
 */
public class ContentLoadException extends Exception {
    public ContentLoadException(String message, Throwable cause) {
        super(message, cause);
    }
}
