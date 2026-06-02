// SUGGESTION (AI-generated reference). Rename to MCQQuestion.java and merge into your own work.
// Do not overwrite code you have already written.  Owner: Member 2
package quiz;

import javax.swing.*;
import java.util.List;

/** Multiple-choice question. Inherits from Question; overrides checkAnswer + render. */
public class MCQQuestion extends Question {
    private final List<String> options;
    private final int correctIndex;

    public MCQQuestion(String prompt, List<String> options, int correctIndex) {
        super(prompt);
        this.options = options;
        this.correctIndex = correctIndex;
    }

    public List<String> getOptions() { return options; }

    @Override
    public boolean checkAnswer(String response) {
        // TODO(Member 2): return true when response equals options.get(correctIndex).
        //   Trim and compare case-insensitively to be forgiving.
        return false;
    }

    @Override
    public JComponent render() {
        // TODO(Member 2): build a JPanel containing the prompt and a ButtonGroup of
        //   JRadioButtons (one per option) and return it.
        return new JPanel();
    }
}
