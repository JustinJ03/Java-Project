// SUGGESTION (AI-generated reference). Rename to ManualTests.java and merge into your own work.
// Do not overwrite code you have already written.  Owner: shared (each member checks their part)
// Run after implementing the TODOs:
//   javac -d out src/**/*.java tests/ManualTests.java   (expand globs per build.bat)
//   java -cp out tests.ManualTests
// Prints PASS/FAIL per check. These are RED until the TODO logic is implemented.
package tests;

import quiz.MCQQuestion;
import quiz.QuizModule;
import quiz.TrueFalseQuestion;
import data.DataManager;
import app.AppContext;
import java.util.Arrays;

public class ManualTests {
    private static int pass = 0, fail = 0;

    public static void main(String[] args) {
        // Question.checkAnswer (Member 2)
        MCQQuestion mcq = new MCQQuestion("Q", Arrays.asList("A", "B", "C", "D"), 2);
        check("MCQ correct option", mcq.checkAnswer("C"));
        check("MCQ wrong option", !mcq.checkAnswer("A"));

        TrueFalseQuestion tf = new TrueFalseQuestion("Q", true);
        check("TF correct", tf.checkAnswer("true"));
        check("TF wrong", !tf.checkAnswer("false"));

        // Motivational message scale (Member 2)
        QuizModule qm = new QuizModule(new AppContext());
        check("90% -> Outstanding!", "Outstanding!".equals(qm.motivationalMessage(90)));
        check("65% -> That's good!", "That's good!".equals(qm.motivationalMessage(65)));
        check("45% -> Good try!", "Good try!".equals(qm.motivationalMessage(45)));
        check("25% -> You can do better!", "You can do better!".equals(qm.motivationalMessage(25)));
        check("10% -> Don't give up!", "Don't give up!".equals(qm.motivationalMessage(10)));

        // DataManager round-trip (Member 4)
        try {
            DataManager dm = new DataManager("data/test_scores.txt");
            dm.saveScore("tester", 80);
            check("readScores returns saved row", !dm.readScores().isEmpty());
        } catch (Exception e) {
            check("DataManager round-trip threw: " + e.getMessage(), false);
        }

        System.out.println("\nPASS=" + pass + "  FAIL=" + fail);
    }

    private static void check(String label, boolean condition) {
        System.out.println((condition ? "PASS " : "FAIL ") + label);
        if (condition) pass++; else fail++;
    }
}
