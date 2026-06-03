package interfaces;

import data.exceptions.ContentLoadException;
import java.util.List;
import quiz.Question;

/**
 * Contract for the Quiz module's core logic.
 *
 * Implemented by: QuizModule (Member 2).
 */
public interface IQuiz {

    /**
     * Parses questions.txt and returns the full question bank (>= 20 questions).
     * @throws ContentLoadException if the question file is missing or cannot be parsed
     */
    List<Question> loadQuestions() throws ContentLoadException;

    /** Returns the player's current score as a percentage (0-100). */
    int scorePercent();

    /**
     * Maps a percentage score to the required motivational message:
     *   80-100 -> "Outstanding!"
     *   60-79  -> "That's good!"
     *   40-59  -> "Good try!"
     *   20-39  -> "You can do better!"
     *    0-19  -> "Don't give up!"
     */
    String motivationalMessage(int percent);
}
