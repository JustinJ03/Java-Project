// Starter file. Implement the // TODO blocks below.
package interfaces;

import java.util.List;
import quiz.Question;

/** Contract for the quiz logic. */
public interface IQuiz {
    List<Question> loadQuestions();
    int scorePercent();
    String motivationalMessage(int percent);
}
