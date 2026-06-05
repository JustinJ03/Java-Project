package interfaces;

import data.exceptions.ContentLoadException;
import java.util.List;
import quiz.Question;

/**
 * Contract for any quiz module. Defines how questions are loaded, how the score is
 * calculated, and how motivational feedback is produced. Implemented by QuizModule.
 *
 * Implemented by: QuizModule (Vince Adhar Anak Sekelie (106653)).
 */
public interface IQuiz {

    /**
     * Parses questions.txt and returns the full question bank (>= 20 questions).
     * Callers must handle ContentLoadException so the app can degrade gracefully
     * if the resource file is missing at runtime.
     *
     * @throws ContentLoadException if the question file is missing or cannot be parsed
     */
    List<Question> loadQuestions() throws ContentLoadException;

    /**
     * Returns the player's final score as an integer percentage (0–100), computed
     * from correctCount and the total number of questions answered.
     */
    int scorePercent();

    /**
     * Maps a percentage score to the five-band motivational message required by the rubric:
     *   80–100 → "Outstanding!"
     *   60–79  → "That's good!"
     *   40–59  → "Good try!"
     *   20–39  → "You can do better!"
     *    0–19  → "Don't give up!"
     *
     * @param percent score in [0, 100]
     * @return the matching motivational string
     */
    String motivationalMessage(int percent);
}
