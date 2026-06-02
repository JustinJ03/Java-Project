// SUGGESTION (AI-generated reference). Rename to QuizModule.java and merge into your own work.
// Do not overwrite code you have already written.  Owner: Member 2
package quiz;

import app.AppContext;
import app.AppModule;
import interfaces.IQuiz;
import javax.swing.JComponent;
import java.util.ArrayList;
import java.util.List;

/** Quiz flow: loads questions, presents them, scores, persists, motivates. */
public class QuizModule extends AppModule implements IQuiz {
    private final List<Question> questions = new ArrayList<>();
    private int correctCount = 0;

    public QuizModule(AppContext ctx) { super(ctx, "Quiz"); }

    @Override
    public List<Question> loadQuestions() {
        // TODO(Member 2): read resources/quiz/questions.txt (skip lines starting with '#'),
        //   split each line on '|', build MCQQuestion (TYPE=MCQ; options split on ';';
        //   last field = correct index) or TrueFalseQuestion (TYPE=TF; last field = true/false),
        //   add to 'questions', and return it.
        return questions;
    }

    @Override
    public int scorePercent() {
        // TODO(Member 2): return questions.isEmpty() ? 0 : correctCount * 100 / questions.size().
        return 0;
    }

    @Override
    public String motivationalMessage(int percent) {
        // TODO(Member 2): map percent to the EXACT scale:
        //   80-100 -> "Outstanding!"      60-79 -> "That's good!"
        //   40-59  -> "Good try!"         20-39 -> "You can do better!"
        //   0-19   -> "Don't give up!"
        return "";
    }

    @Override
    public JComponent getView() {
        // TODO(Member 2): build the quiz UI - show one question's render() at a time, advance
        //   on answer, increment correctCount when checkAnswer() is true, then display
        //   scorePercent() + motivationalMessage(scorePercent()) and call
        //   ctx.storage().saveScore("player", scorePercent()) inside try-catch.
        return placeholder("quiz: load 20+ questions, score, motivational message, save");
    }
}
