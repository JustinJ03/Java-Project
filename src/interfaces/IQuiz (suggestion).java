// SUGGESTION (AI-generated reference). Rename to IQuiz.java and merge into your own work.
// Do not overwrite code you have already written.  Owner: Member 2
package interfaces;

import java.util.List;
import quiz.Question;

/** Contract for the quiz logic. */
public interface IQuiz {
    List<Question> loadQuestions();
    int scorePercent();
    String motivationalMessage(int percent);
}
