package quiz;

import javax.swing.*;
import java.util.List;

/**
 * TASK: Member 2 - Multiple Choice Question
 *
 * PURPOSE:
 *   A question where the player picks one option from a list.
 *   The correct answer is identified by its index in the options list.
 *
 * EXTENDS: Question -- inherit prompt, getPrompt(), and implement
 *          the two abstract methods: checkAnswer() and render().
 *
 * OOP CONCEPTS TO DEMONSTRATE:
 *   - Inheritance : extends Question
 *   - Overriding  : checkAnswer() and render() override the abstract methods
 */
public class MCQQuestion extends Question {

    // === FIELDS ===
    private final List<String> options;     // e.g. ["Paris", "London", "Rome", "Berlin"]
    private final int correctIndex;         // index of the correct option, e.g. 0 for "Paris"

    // === CONSTRUCTOR ===
    public MCQQuestion(String prompt, List<String> options, int correctIndex) {
        super(prompt);
        this.options      = options;
        this.correctIndex = correctIndex;
    }

    // === GETTER ===
    public List<String> getOptions() { return options; }

    // =========================================================
    // METHOD: checkAnswer(String response)  [OVERRIDE]
    // =========================================================
    /**
     * Returns true if the player's response matches the correct option.
     *
     * HOW TO IMPLEMENT:
     *   STEP 1: Get the correct option text:
     *             String correct = options.get(correctIndex);
     *
     *   STEP 2: Compare case-insensitively and ignore leading/trailing spaces:
     *             return correct.trim().equalsIgnoreCase(response.trim());
     *
     * OOP NOTE: @Override because this method is declared abstract in Question.
     *           This is method overriding -- a key OOP concept.
     */
    @Override
    public boolean checkAnswer(String response) {
        // TODO(Member 2): implement using the steps above.
        return false;
    }

    // =========================================================
    // METHOD: render()  [OVERRIDE]
    // =========================================================
    /**
     * Builds the MCQ question UI.
     *
     * HOW TO IMPLEMENT:
     *   STEP 1: Create a JPanel with a vertical BoxLayout or GridLayout.
     *
     *   STEP 2: Add a JLabel at the top showing the prompt text.
     *
     *   STEP 3: Create a ButtonGroup to ensure only one radio button
     *             can be selected at a time.
     *             ButtonGroup group = new ButtonGroup();
     *
     *   STEP 4: Loop through 'options'. For each option:
     *             JRadioButton rb = new JRadioButton(option);
     *             group.add(rb);
     *             panel.add(rb);
     *
     *   STEP 5: Store a reference to the ButtonGroup if QuizModule needs
     *             to read the selected answer later (or use an ActionListener
     *             on each radio button to call checkAnswer() immediately).
     *
     *   STEP 6: Return the panel.
     *
     * OOP NOTE: Overrides Question.render() -- demonstrates overriding.
     */
    @Override
    public JComponent render() {
        // TODO(Member 2): implement using the steps above.
        return new JPanel();
    }
}
