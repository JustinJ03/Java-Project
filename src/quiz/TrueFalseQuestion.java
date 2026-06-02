package quiz;

import javax.swing.*;
import java.awt.*;

/**
 * TASK: Member 2 - True / False Question
 *
 * PURPOSE:
 *   A question with exactly two possible answers: true or false.
 *
 * EXTENDS: Question -- inherit prompt, getPrompt(), and implement
 *          checkAnswer() and render().
 *
 * OOP CONCEPTS TO DEMONSTRATE:
 *   - Inheritance : extends Question
 *   - Overriding  : checkAnswer() and render() override the abstract methods
 */
public class TrueFalseQuestion extends Question {

    // === FIELD ===
    private final boolean correct; // true if the correct answer is "true"

    // === CONSTRUCTOR ===
    public TrueFalseQuestion(String prompt, boolean correct) {
        super(prompt);
        this.correct = correct;
    }

    // =========================================================
    // METHOD: checkAnswer(String response)  [OVERRIDE]
    // =========================================================
    /**
     * Returns true if the player's response matches the correct boolean.
     *
     * HOW TO IMPLEMENT:
     *   STEP 1: Parse the player's string into a boolean:
     *             boolean playerAnswer = Boolean.parseBoolean(response.trim());
     *             (Boolean.parseBoolean("true") == true,  "false" or anything else == false)
     *
     *   STEP 2: Return whether it matches:
     *             return playerAnswer == correct;
     *
     * OOP NOTE: Overrides Question.checkAnswer() -- method overriding.
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
     * Builds the True/False question UI.
     *
     * HOW TO IMPLEMENT:
     *   STEP 1: Create a JPanel (BorderLayout or BoxLayout).
     *
     *   STEP 2: Add a JLabel at the top showing the prompt text.
     *
     *   STEP 3: Create a JPanel for the two answer buttons:
     *             JButton trueBtn  = new JButton("True");
     *             JButton falseBtn = new JButton("False");
     *
     *   STEP 4: Add ActionListeners. When a button is clicked, call
     *             checkAnswer("true") or checkAnswer("false") to check
     *             the answer, then visually indicate correct / incorrect
     *             (e.g. change button background to green/red).
     *
     *   STEP 5: Add the buttons to the panel and return it.
     *
     * OOP NOTE: Overrides Question.render() -- demonstrates overriding.
     */
    @Override
    public JComponent render() {
        // TODO(Member 2): implement using the steps above.
        return new JPanel();
    }
}
