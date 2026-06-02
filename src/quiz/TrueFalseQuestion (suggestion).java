// SUGGESTION (AI-generated reference). Rename to TrueFalseQuestion.java and merge into your own work.
// Do not overwrite code you have already written.  Owner: Member 2
package quiz;

import javax.swing.*;

/** True/False question. Inherits from Question; overrides checkAnswer + render. */
public class TrueFalseQuestion extends Question {
    private final boolean correct;

    public TrueFalseQuestion(String prompt, boolean correct) {
        super(prompt);
        this.correct = correct;
    }

    @Override
    public boolean checkAnswer(String response) {
        // TODO(Member 2): parse response ("true"/"false", ignore case) and compare with 'correct'.
        return false;
    }

    @Override
    public JComponent render() {
        // TODO(Member 2): build a JPanel with the prompt and True / False buttons; return it.
        return new JPanel();
    }
}
