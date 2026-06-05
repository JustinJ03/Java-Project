package quiz;

import javax.swing.JComponent;

/**
 * Creator : Vince Adhar Anak Sekelie (106653)
 * Tester/Reviewer  : Justin Joel anak Juni (104607)
 * 
 * Abstract base class for all question types (MCQ, True/False). Declares two abstract
 * methods — checkAnswer and render — that each subclass must implement differently;
 * this is the core of the polymorphism pattern used in QuizModule, which iterates over
 * a {@code List<Question>} and lets the JVM dispatch to the correct subclass at runtime.
 *
 * OOP CONCEPTS:
 * - Abstraction   : abstract class with abstract methods
 * - Inheritance   : MCQQuestion and TrueFalseQuestion both extend this
 * - Polymorphism  : QuizModule calls checkAnswer() and render() on each Question
 *                   without knowing the concrete type
 */
public abstract class Question {

    // === FIELD ===
    protected final String prompt; // the question text shown to the player; set once at construction

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
