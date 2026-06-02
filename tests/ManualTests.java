// Test infrastructure -- shared by all members. No renaming needed.
// Run after renaming your (suggestion) source files and implementing your TODOs:
//   javac -d out src\main\Main.java src\app\*.java src\interfaces\*.java ^
//         src\learning\*.java src\quiz\*.java src\gamification\*.java ^
//         src\data\*.java src\data\exceptions\*.java tests\ManualTests.java
//   java -cp out tests.ManualTests
// Prints PASS/FAIL per check. LOGIC tests are RED until TODOs are implemented.
package tests;

import quiz.MCQQuestion;
import quiz.QuizModule;
import quiz.TrueFalseQuestion;
import data.DataManager;
import app.AppContext;
import java.io.File;
import java.util.Arrays;

public class ManualTests {
    private static int pass = 0, fail = 0;

    public static void main(String[] args) {
        // -- Member 2: Question.checkAnswer --
        MCQQuestion mcq = new MCQQuestion("Q", Arrays.asList("A", "B", "C", "D"), 2);
        check("MCQ correct option",  mcq.checkAnswer("C"));
        check("MCQ wrong option",   !mcq.checkAnswer("A"));

        TrueFalseQuestion tf = new TrueFalseQuestion("Q", true);
        check("TF correct",  tf.checkAnswer("true"));
        check("TF wrong",   !tf.checkAnswer("false"));

        // -- Member 2: motivationalMessage scale --
        QuizModule qm = new QuizModule(new AppContext());
        check("90%  -> Outstanding!",      "Outstanding!".equals(qm.motivationalMessage(90)));
        check("65%  -> That's good!",      "That's good!".equals(qm.motivationalMessage(65)));
        check("45%  -> Good try!",         "Good try!".equals(qm.motivationalMessage(45)));
        check("25%  -> You can do better!","You can do better!".equals(qm.motivationalMessage(25)));
        check("10%  -> Don't give up!",    "Don't give up!".equals(qm.motivationalMessage(10)));

        // -- Member 4: DataManager round-trip --
        String tmp = "data/test_scores_manual.txt";
        new File("data").mkdirs();
        try {
            DataManager dm = new DataManager(tmp);
            dm.saveScore("tester", 80);
            check("readScores returns saved row", !dm.readScores().isEmpty());
        } catch (Exception e) {
            check("DataManager round-trip threw: " + e.getMessage(), false);
        } finally {
            new File(tmp).delete(); // cleanup temp file
        }

        System.out.println("\nPASS=" + pass + "  FAIL=" + fail);
    }

    private static void check(String label, boolean condition) {
        System.out.println((condition ? "PASS " : "FAIL ") + label);
        if (condition) pass++; else fail++;
    }
}
