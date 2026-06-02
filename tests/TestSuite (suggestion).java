// SUGGESTION (AI-generated reference). Rename to TestSuite.java and merge into your own work.
// Do not overwrite code you have already written.  Owner: shared
//
// HOW TO COMPILE AND RUN (from project root, after renaming suggestion files):
//   javac -d out src\main\Main.java src\app\*.java src\interfaces\*.java ^
//         src\learning\*.java src\quiz\*.java src\gamification\*.java ^
//         src\data\*.java src\data\exceptions\*.java tests\TestSuite.java
//   java -cp out tests.TestSuite
//
// TEST LABELS:
//   [STRUCTURAL] -- tests class/interface structure; PASS before any TODO is filled
//   [LOGIC]      -- tests runtime behaviour; RED until you implement the TODO
//
// When all [LOGIC] tests for your module pass, your core logic is correct.
package tests;

import app.AppContext;
import app.Navigator;
import gamification.Badge;
import gamification.GamificationModule;
import learning.ContentPage;
import quiz.MCQQuestion;
import quiz.TrueFalseQuestion;
import quiz.QuizModule;
import data.DataManager;
import data.UserProfile;
import data.exceptions.InvalidScoreException;
import data.exceptions.DataNotFoundException;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class TestSuite {

    // -----------------------------------------------------------------------
    // Test harness
    // -----------------------------------------------------------------------
    private static int pass = 0, fail = 0;
    private static String currentSection = "";

    public static void main(String[] args) {
        banner("EduSDG Test Suite");
        section("Member 1 — ContentPage (structural)");
        testContentPage();
        section("Member 2 — MCQQuestion (logic)");
        testMCQQuestion();
        section("Member 2 — TrueFalseQuestion (logic)");
        testTrueFalseQuestion();
        section("Member 2 — QuizModule scoring (logic)");
        testQuizScoring();
        section("Member 3 — Badge (structural)");
        testBadge();
        section("Member 3 — GamificationModule (logic)");
        testGamification();
        section("Member 4 — UserProfile (structural)");
        testUserProfile();
        section("Member 4 — Custom exceptions (structural)");
        testExceptions();
        section("Member 4 — DataManager (logic)");
        testDataManager();

        System.out.println("\n================================================");
        System.out.println("  TOTAL  PASS=" + pass + "  FAIL=" + fail);
        System.out.println("================================================");
        if (fail > 0) {
            System.out.println("\nFAILING tests need their TODO(Member N) implemented.");
            System.out.println("Run DebugHelper for more detail: java -cp out tests.DebugHelper");
        }
    }

    // -----------------------------------------------------------------------
    // Member 1 — ContentPage
    // -----------------------------------------------------------------------
    private static void testContentPage() {
        // Two-arg constructor
        ContentPage p1 = new ContentPage("Title", "Body text");
        check("[STRUCTURAL] ContentPage 2-arg: title stored",   "Title".equals(p1.getTitle()));
        check("[STRUCTURAL] ContentPage 2-arg: body stored",    "Body text".equals(p1.getBody()));
        check("[STRUCTURAL] ContentPage 2-arg: imagePath null", p1.getImagePath() == null);

        // Three-arg constructor (overloaded)
        ContentPage p2 = new ContentPage("T2", "B2", "img/photo.png");
        check("[STRUCTURAL] ContentPage 3-arg: title stored",     "T2".equals(p2.getTitle()));
        check("[STRUCTURAL] ContentPage 3-arg: imagePath stored", "img/photo.png".equals(p2.getImagePath()));

        // Distinct objects
        check("[STRUCTURAL] ContentPage objects are independent", p1 != p2);
    }

    // -----------------------------------------------------------------------
    // Member 2 — MCQQuestion
    // -----------------------------------------------------------------------
    private static void testMCQQuestion() {
        // correctIndex=2 means "C" is the answer
        MCQQuestion q = new MCQQuestion(
            "What is 1+1?",
            Arrays.asList("1", "3", "2", "4"),
            2);

        check("[LOGIC] MCQ: correct option accepted",       q.checkAnswer("2"));
        check("[LOGIC] MCQ: wrong option rejected (first)", !q.checkAnswer("1"));
        check("[LOGIC] MCQ: wrong option rejected (last)",  !q.checkAnswer("4"));

        // Case-insensitivity (per TODO hint "Trim and compare case-insensitively")
        MCQQuestion qCase = new MCQQuestion(
            "Capital?",
            Arrays.asList("london", "paris", "berlin"),
            1);
        check("[LOGIC] MCQ: case-insensitive match",        qCase.checkAnswer("Paris"));
        check("[LOGIC] MCQ: trimmed match (leading space)", qCase.checkAnswer(" paris"));

        // getOptions() available
        check("[STRUCTURAL] MCQ: getOptions() size",        q.getOptions().size() == 4);
        check("[STRUCTURAL] MCQ: getPrompt() stored",       "What is 1+1?".equals(q.getPrompt()));
    }

    // -----------------------------------------------------------------------
    // Member 2 — TrueFalseQuestion
    // -----------------------------------------------------------------------
    private static void testTrueFalseQuestion() {
        TrueFalseQuestion qtrue  = new TrueFalseQuestion("Earth is a planet?", true);
        TrueFalseQuestion qfalse = new TrueFalseQuestion("Sun is a planet?",   false);

        check("[LOGIC] TF: 'true' accepted when answer=true",    qtrue.checkAnswer("true"));
        check("[LOGIC] TF: 'false' rejected when answer=true",   !qtrue.checkAnswer("false"));
        check("[LOGIC] TF: 'false' accepted when answer=false",  qfalse.checkAnswer("false"));
        check("[LOGIC] TF: 'true' rejected when answer=false",   !qfalse.checkAnswer("true"));

        // Case-insensitive per TODO hint
        check("[LOGIC] TF: uppercase 'TRUE' accepted",           qtrue.checkAnswer("TRUE"));
        check("[LOGIC] TF: mixed case 'False' accepted",         qfalse.checkAnswer("False"));

        check("[STRUCTURAL] TF: getPrompt() stored", "Earth is a planet?".equals(qtrue.getPrompt()));
    }

    // -----------------------------------------------------------------------
    // Member 2 — QuizModule scoring
    // ALL 10 boundary values for motivationalMessage (spec requirement)
    // -----------------------------------------------------------------------
    private static void testQuizScoring() {
        QuizModule qm = new QuizModule(new AppContext());

        // Boundary values — lower edges
        check("[LOGIC] motivational 0%   -> Don't give up!",    "Don't give up!".equals(qm.motivationalMessage(0)));
        check("[LOGIC] motivational 19%  -> Don't give up!",    "Don't give up!".equals(qm.motivationalMessage(19)));
        check("[LOGIC] motivational 20%  -> You can do better!","You can do better!".equals(qm.motivationalMessage(20)));
        check("[LOGIC] motivational 39%  -> You can do better!","You can do better!".equals(qm.motivationalMessage(39)));
        check("[LOGIC] motivational 40%  -> Good try!",         "Good try!".equals(qm.motivationalMessage(40)));
        check("[LOGIC] motivational 59%  -> Good try!",         "Good try!".equals(qm.motivationalMessage(59)));
        check("[LOGIC] motivational 60%  -> That's good!",      "That's good!".equals(qm.motivationalMessage(60)));
        check("[LOGIC] motivational 79%  -> That's good!",      "That's good!".equals(qm.motivationalMessage(79)));
        check("[LOGIC] motivational 80%  -> Outstanding!",      "Outstanding!".equals(qm.motivationalMessage(80)));
        check("[LOGIC] motivational 100% -> Outstanding!",      "Outstanding!".equals(qm.motivationalMessage(100)));

        // scorePercent() with no questions loaded returns 0 (safe default)
        check("[LOGIC] scorePercent() with no questions = 0",   qm.scorePercent() == 0);
    }

    // -----------------------------------------------------------------------
    // Member 3 — Badge
    // -----------------------------------------------------------------------
    private static void testBadge() {
        // Single-arg constructor (overloaded)
        Badge b1 = new Badge("Rising Star");
        check("[STRUCTURAL] Badge 1-arg: name stored",        "Rising Star".equals(b1.getName()));
        check("[STRUCTURAL] Badge 1-arg: description empty",  "".equals(b1.getDescription()));

        // Two-arg constructor (overloaded)
        Badge b2 = new Badge("Scholar", "Scored 100%");
        check("[STRUCTURAL] Badge 2-arg: name stored",        "Scholar".equals(b2.getName()));
        check("[STRUCTURAL] Badge 2-arg: description stored", "Scored 100%".equals(b2.getDescription()));
    }

    // -----------------------------------------------------------------------
    // Member 3 — GamificationModule
    // -----------------------------------------------------------------------
    private static void testGamification() {
        GamificationModule gm = new GamificationModule(new AppContext());

        // Initial state
        check("[STRUCTURAL] GamificationModule: starts at 0 points", gm.totalPoints() == 0);
        check("[STRUCTURAL] GamificationModule: starts with no badges", gm.awardedBadges().isEmpty());

        // addPoints(int) — basic overload
        gm.addPoints(30);
        check("[LOGIC] addPoints(30): totalPoints = 30", gm.totalPoints() == 30);

        // addPoints(int, String) — second overload must also accumulate
        gm.addPoints(20, "Quiz completed");
        check("[LOGIC] addPoints(20, reason): totalPoints = 50", gm.totalPoints() == 50);

        // Badge awarded at threshold 50 ("Rising Star")
        check("[LOGIC] Badge awarded at 50 points", !gm.awardedBadges().isEmpty());

        // Keep adding to reach 100 threshold
        gm.addPoints(50);
        check("[LOGIC] addPoints(50): totalPoints = 100", gm.totalPoints() == 100);
        // At 100 a second badge ("Scholar") should be awarded
        check("[LOGIC] Second badge awarded at 100 points", gm.awardedBadges().size() >= 2);

        // leaderboard() returns a List (may be empty if no scores saved yet)
        check("[STRUCTURAL] leaderboard() returns non-null", gm.leaderboard() != null);
    }

    // -----------------------------------------------------------------------
    // Member 4 — UserProfile
    // -----------------------------------------------------------------------
    private static void testUserProfile() {
        UserProfile u = new UserProfile("Alice");
        check("[STRUCTURAL] UserProfile: getName()",      "Alice".equals(u.getName()));
        check("[STRUCTURAL] UserProfile: initial score",  u.getLastScore() == 0);

        u.setLastScore(85);
        check("[STRUCTURAL] UserProfile: setLastScore()", u.getLastScore() == 85);
    }

    // -----------------------------------------------------------------------
    // Member 4 — Custom exceptions (structural only — constructors must work)
    // -----------------------------------------------------------------------
    private static void testExceptions() {
        // DataNotFoundException — two constructors
        DataNotFoundException dnf1 = new DataNotFoundException("not found");
        check("[STRUCTURAL] DataNotFoundException(msg)",       "not found".equals(dnf1.getMessage()));

        DataNotFoundException dnf2 = new DataNotFoundException("wrap", new RuntimeException("cause"));
        check("[STRUCTURAL] DataNotFoundException(msg,cause)", dnf2.getCause() != null);

        // InvalidScoreException
        InvalidScoreException ise = new InvalidScoreException("bad score");
        check("[STRUCTURAL] InvalidScoreException(msg)",       "bad score".equals(ise.getMessage()));

        // ContentLoadException
        data.exceptions.ContentLoadException cle =
            new data.exceptions.ContentLoadException("load fail", new RuntimeException());
        check("[STRUCTURAL] ContentLoadException(msg,cause)",  "load fail".equals(cle.getMessage()));
    }

    // -----------------------------------------------------------------------
    // Member 4 — DataManager
    // Uses a temp file so it does NOT pollute data/scores.txt
    // -----------------------------------------------------------------------
    private static void testDataManager() {
        String tmpPath = "data/test_suite_temp.txt";
        new File("data").mkdirs(); // ensure data/ exists before writing

        DataManager dm = new DataManager(tmpPath);

        // readScores() on non-existent file must return empty list (not throw)
        try {
            new File(tmpPath).delete(); // ensure absent
            List<String[]> empty = dm.readScores();
            check("[LOGIC] readScores on missing file = empty list (no exception)", empty != null && empty.isEmpty());
        } catch (DataNotFoundException e) {
            check("[LOGIC] readScores on missing file = empty list (no exception) — threw instead", false);
        }

        // saveScore with valid boundary scores must NOT throw
        try {
            dm.saveScore("alice", 0);
            check("[LOGIC] saveScore(name, 0) no exception", true);
        } catch (Exception e) {
            check("[LOGIC] saveScore(name, 0) no exception — threw: " + e.getMessage(), false);
        }
        try {
            dm.saveScore("bob", 100);
            check("[LOGIC] saveScore(name, 100) no exception", true);
        } catch (Exception e) {
            check("[LOGIC] saveScore(name, 100) no exception — threw: " + e.getMessage(), false);
        }

        // After saving, readScores must return rows
        try {
            List<String[]> rows = dm.readScores();
            check("[LOGIC] readScores after save returns non-empty", !rows.isEmpty());
        } catch (DataNotFoundException e) {
            check("[LOGIC] readScores after save — threw DataNotFoundException", false);
        }

        // saveScore with INVALID scores must throw InvalidScoreException
        boolean threwNeg = false;
        try { dm.saveScore("x", -1); }
        catch (InvalidScoreException e) { threwNeg = true; }
        check("[LOGIC] saveScore(name, -1) throws InvalidScoreException", threwNeg);

        boolean threwOver = false;
        try { dm.saveScore("x", 101); }
        catch (InvalidScoreException e) { threwOver = true; }
        check("[LOGIC] saveScore(name, 101) throws InvalidScoreException", threwOver);

        // Overloaded saveScore(UserProfile)
        UserProfile u = new UserProfile("carol");
        u.setLastScore(72);
        try {
            dm.saveScore(u);
            check("[LOGIC] saveScore(UserProfile) no exception", true);
            List<String[]> rows = dm.readScores();
            boolean hasCarol = rows.stream().anyMatch(r -> r.length > 0 && "carol".equals(r[0]));
            check("[LOGIC] saveScore(UserProfile) stores name", hasCarol);
        } catch (Exception e) {
            check("[LOGIC] saveScore(UserProfile) threw: " + e.getMessage(), false);
        }

        // Cleanup temp file
        new File(tmpPath).delete();
    }

    // -----------------------------------------------------------------------
    // Harness helpers
    // -----------------------------------------------------------------------
    private static void section(String name) {
        currentSection = name;
        System.out.println("\n--- " + name + " ---");
    }

    private static void check(String label, boolean condition) {
        String marker = condition ? "PASS" : "FAIL";
        System.out.println(marker + " " + label);
        if (condition) pass++; else fail++;
    }

    private static void banner(String title) {
        System.out.println("================================================");
        System.out.println("  " + title);
        System.out.println("  [STRUCTURAL] = passes before any TODO is filled");
        System.out.println("  [LOGIC]      = needs TODO implementation to pass");
        System.out.println("================================================");
    }
}
