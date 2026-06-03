package quiz;

import javax.swing.JComponent;

/**
 * TASK: Member 2 - Abstract Question Base Class
 *
 * PURPOSE:
 * Abstract superclass for all question types (MCQ, True/False).
 * Defines the common contract: every question has a prompt, can check
 * an answer, and can render itself as a Swing component.
 *
 * OOP CONCEPTS TO DEMONSTRATE:
 * - Abstraction   : abstract class with abstract methods
 * - Inheritance   : MCQQuestion and TrueFalseQuestion both extend this
 * - Polymorphism  : QuizModule loops over List<Question>, calling
 * checkAnswer() and render() on each -- the JVM
 * dispatches to the correct subclass at runtime.
 */
public abstract class Question {

    // === FIELD ===
    protected final String prompt; // the question text shown to the player

    // === CONSTRUCTOR ===
    protected Question(String prompt) {
        this.prompt = prompt;
    }

    // === GETTER ===
    public String getPrompt() { 
        return prompt; 
    }

    // =========================================================
    // ABSTRACT METHOD: checkAnswer(String response)
    // =========================================================
    /**
     * Returns true if the player's response is correct.
     *
     * Each subclass decides what "correct" means:
     * - MCQQuestion  : response matches the option text at correctIndex
     * - TrueFalseQuestion : response is "true" or "false" (case-insensitive)
     */
    public abstract boolean checkAnswer(String response);

    // =========================================================
    // ABSTRACT METHOD: render()
    // =========================================================
    /**
     * Builds and returns a Swing panel representing this question's UI.
     *
     * Each subclass renders itself differently:
     * - MCQQuestion      : prompt + radio buttons for each option
     * - TrueFalseQuestion: prompt + True and False buttons
     */
    public abstract JComponent render();
}
