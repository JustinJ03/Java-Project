package quiz;

import data.exceptions.ContentLoadException;
import data.exceptions.InvalidScoreException;
import interfaces.IQuiz;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * TASK: Member 2 - Quiz Module
 *
 * PURPOSE:
 *   Loads 20+ questions from questions.txt, presents them one at a time,
 *   calculates the player's score, shows a motivational message, and
 *   saves the result via DataManager.
 *
 * EXTENDS: JPanel     -- lightweight Swing container for Main's CardLayout
 * IMPLEMENTS: IQuiz   -- loadQuestions(), scorePercent(), motivationalMessage().
 *
 * OOP CONCEPTS TO DEMONSTRATE:
 *   - Inheritance     : extends JPanel
 *   - Overriding      : all IQuiz methods
 *   - Polymorphism    : the questions list holds Question objects; checkAnswer()
 *                       and render() dispatch to MCQQuestion or TrueFalseQuestion
 *                       at runtime without the QuizModule knowing the concrete type
 *   - Exception handling : InvalidScoreException, ContentLoadException
 */
public class QuizModule extends JPanel implements IQuiz {

    // === FIELDS ===
    private final List<Question> questions = new ArrayList<>();
    private int correctCount = 0; // tracks how many answers were correct

    // === CONSTRUCTOR ===
    /**
     * No-arg constructor required for Main's CardLayout management.
     * Main instantiates modules directly and adds them to the CardLayout.
     */
    public QuizModule() {
        super();
    }

    // =========================================================
    // METHOD: loadQuestions()  [IQuiz]
    // =========================================================
    /**
     * Parses questions.txt and populates the questions list.
     *
     * FILE FORMAT (each line is one question):
     *   MCQ|prompt|opt1;opt2;opt3;opt4|correctIndex
     *   TF|prompt||true          (or false)
     *   Lines starting with '#' are comments -- skip them.
     *
     * HOW TO IMPLEMENT:
     *   STEP 1: Open "resources/quiz/questions.txt" with BufferedReader.
     *
     *   STEP 2: For each non-blank, non-comment line:
     *             String[] parts = line.split("\\|");
     *             String type = parts[0].trim();
     *
     *   STEP 3: If type.equals("MCQ"):
     *             String prompt   = parts[1].trim();
     *             List<String> opts = Arrays.asList(parts[2].split(";"));
     *             int correctIdx  = Integer.parseInt(parts[3].trim());
     *             questions.add(new MCQQuestion(prompt, opts, correctIdx));
     *
     *   STEP 4: If type.equals("TF"):
     *             String prompt  = parts[1].trim();
     *             boolean answer = Boolean.parseBoolean(parts[3].trim());
     *             questions.add(new TrueFalseQuestion(prompt, answer));
     *
     *   STEP 5: Use try-with-resources. On IOException throw:
     *             new ContentLoadException("Cannot load questions", e);
     *
     *   EXPECTED: questions.size() >= 20.
     *
     * OOP NOTE: Overrides IQuiz.loadQuestions(). The list is typed as
     *           List<Question> -- polymorphism lets you add both MCQ and TF.
     */
    @Override
    public List<Question> loadQuestions() {
        // TODO(Member 2): implement using the steps above.
        return questions;
    }

    // =========================================================
    // METHOD: scorePercent()  [IQuiz]
    // =========================================================
    /**
     * Returns the player's score as a percentage.
     *
     * HOW TO IMPLEMENT:
     *   return questions.isEmpty() ? 0 : correctCount * 100 / questions.size();
     *
     * BOUNDARY: if questions is empty, return 0 to avoid division by zero.
     */
    @Override
    public int scorePercent() {
        // TODO(Member 2): implement using the formula above.
        return 0;
    }

    // =========================================================
    // METHOD: motivationalMessage(int percent)  [IQuiz]
    // =========================================================
    /**
     * Maps a score percentage to the required motivational message.
     *
     * HOW TO IMPLEMENT (use if-else if chain -- EXACT strings required):
     *
     *   if      (percent >= 80) return "Outstanding!";
     *   else if (percent >= 60) return "That's good!";
     *   else if (percent >= 40) return "Good try!";
     *   else if (percent >= 20) return "You can do better!";
     *   else                    return "Don't give up!";
     *
     * IMPORTANT: The TestSuite checks ALL 10 boundary values (0, 19, 20,
     *            39, 40, 59, 60, 79, 80, 100). Get the >= conditions right.
     */
    @Override
    public String motivationalMessage(int percent) {
        // TODO(Member 2): implement the if-else chain above.
        return "";
    }

    // =========================================================
    // METHOD: getView()  [DEPRECATED -- QuizModule now IS a JPanel]
    // =========================================================
    /**
     * Builds and returns the Quiz Module's Swing panel.
     *
     * HOW TO IMPLEMENT:
     *   STEP 1: Load questions if not done: if (questions.isEmpty()) loadQuestions();
     *
     *   STEP 2: Create a main JPanel to hold the current question's render() output.
     *           Display questions[0].render() initially.
     *
     *   STEP 3: Add a "Submit" or "Next" button.
     *           On click:
     *             a. Read the player's selected answer (from the rendered component).
     *             b. Call questions.get(currentIndex).checkAnswer(answer).
     *             c. If correct: correctCount++;
     *             d. Advance to the next question. If no more questions:
     *                  - Show score: scorePercent() + "%" + motivationalMessage(scorePercent())
     *                  - Save score: try { DataManager.getInstance().saveScore("Player", scorePercent()); }
     *                                catch (InvalidScoreException e) { e.printStackTrace(); }
     *                  - Optionally trigger gamification via DataManager.
     *
     *   STEP 4: Return the main panel.
     *
     * OOP NOTE: Previously overrode AppModule.getView(). Now QuizModule extends JPanel,
     *           so build the UI directly in the constructor or an init() method.
     */
    public JComponent getView() {
        // TODO(Member 2): This method is for backward compatibility. Build UI in constructor instead.
        return new JPanel();
    }

    // =========================================================
    // METHOD: onShow()  [OVERRIDE -- optional]
    // =========================================================
    /**
     * Reset the quiz state when the user navigates back to this module.
     *
     * HOW TO IMPLEMENT:
     *   correctCount = 0;
     *   questions.clear(); // reload fresh next time getView() is called
     *
     * OOP NOTE: Overrides AppModule.onShow().
     */
    public void onShow() {
        // TODO(Member 2): optionally reset correctCount and clear questions for a fresh quiz.
    }
}
