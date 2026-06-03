// Test infrastructure -- shared by all members. No renaming needed.
// Run after renaming your (suggestion) source files and implementing your TODOs:
//   build.bat
//   java -cp out tests.TestSuite
//
// TEST LABELS:
//   [STRUCTURAL] -- tests class/interface structure; PASS before any TODO is filled
//   [LOGIC]      -- tests runtime behaviour; RED until you implement the TODO
package tests;

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

    private static int pass = 0, fail = 0;
    private static final String TMP = "data/test_suite_temp.txt";

    public static void main(String[] args) {
        banner("EduSDG Test Suite");

        section("Member 1 -- ContentPage (structural)");
        testContentPage();

        section("Member 2 -- MCQQuestion (logic)");
        testMCQQuestion();

        section("Member 2 -- TrueFalseQuestion (logic)");
        testTrueFalseQuestion();

        section("Member 2 -- QuizModule scoring (logic)");
        testQuizScoring();

        section("Member 3 -- Badge (structural)");
        testBadge();

        section("Member 3 -- GamificationModule (logic)");
        testGamification();

        section("Member 4 -- UserProfile (structural)");
        testUserProfile();

        section("Member 4 -- Custom exceptions (structural)");
        testExceptions();

        section("Member 4 -- DataManager (logic)");
        testDataManager();

        System.out.println("\n================================================");
        System.out.println("  TOTAL  PASS=" + pass + "  FAIL=" + fail);
        System.out.println("================================================");
        if (fail > 0) {
            System.out.println("\n[LOGIC] failures need their TODO(Member N) implemented.");
            System.out.println("Run DebugHelper for environment diagnostics:");
            System.out.println("  java -cp out tests.DebugHelper");
        }
    }

    // -----------------------------------------------------------------------
    // Member 1 -- ContentPage
    // -----------------------------------------------------------------------
    private static void testContentPage() {
        ContentPage p1 = new ContentPage(1, "Title", "Body text");
        check("[STRUCTURAL] ContentPage 3-arg: title stored",    "Title".equals(p1.getTitle()));
        check("[STRUCTURAL] ContentPage 3-arg: body stored",     "Body text".equals(p1.getBodyText()));
        check("[STRUCTURAL] ContentPage 3-arg: imagePath null",  p1.getImagePath() == null);
        check("[STRUCTURAL] ContentPage 3-arg: pageNumber stored", p1.getPageNumber() == 1);
        check("[STRUCTURAL] ContentPage: starts not viewed",     !p1.isViewed());

        p1.markViewed();
        check("[STRUCTURAL] ContentPage: markViewed() sets viewed", p1.isViewed());

        ContentPage p2 = new ContentPage(2, "T2", "B2", "img/photo.png");
        check("[STRUCTURAL] ContentPage 4-arg: title stored",    "T2".equals(p2.getTitle()));
        check("[STRUCTURAL] ContentPage 4-arg: imagePath stored","img/photo.png".equals(p2.getImagePath()));
        check("[STRUCTURAL] ContentPage 4-arg: pageNumber stored", p2.getPageNumber() == 2);
        check("[STRUCTURAL] ContentPage objects are independent", p1 != p2);
    }

    // -----------------------------------------------------------------------
    // Member 2 -- MCQQuestion
    // -----------------------------------------------------------------------
    private static void testMCQQuestion() {
        // correctIndex=2 means "2" is the answer
        MCQQuestion q = new MCQQuestion(
                "What is 1+1?",
                Arrays.asList("1", "3", "2", "4"),
                2);

        check("[LOGIC] MCQ: correct option accepted",        q.checkAnswer("2"));
        check("[LOGIC] MCQ: wrong option rejected (first)",  !q.checkAnswer("1"));
        check("[LOGIC] MCQ: wrong option rejected (last)",   !q.checkAnswer("4"));

        MCQQuestion qCase = new MCQQuestion(
                "Capital?",
                Arrays.asList("london", "paris", "berlin"),
                1);
        check("[LOGIC] MCQ: case-insensitive match",         qCase.checkAnswer("Paris"));
        check("[LOGIC] MCQ: trimmed match (leading space)",  qCase.checkAnswer(" paris"));

        check("[STRUCTURAL] MCQ: getOptions() size",         q.getOptions().size() == 4);
        check("[STRUCTURAL] MCQ: getPrompt() stored",        "What is 1+1?".equals(q.getPrompt()));
    }

    // -----------------------------------------------------------------------
    // Member 2 -- TrueFalseQuestion
    // -----------------------------------------------------------------------
    private static void testTrueFalseQuestion() {
        TrueFalseQuestion qtrue  = new TrueFalseQuestion("Earth is a planet?", true);
        TrueFalseQuestion qfalse = new TrueFalseQuestion("Sun is a planet?",   false);

        check("[LOGIC] TF: 'true'  accepted when answer=true",    qtrue.checkAnswer("true"));
        check("[LOGIC] TF: 'false' rejected when answer=true",   !qtrue.checkAnswer("false"));
        check("[LOGIC] TF: 'false' accepted when answer=false",  qfalse.checkAnswer("false"));
        check("[LOGIC] TF: 'true'  rejected when answer=false",  !qfalse.checkAnswer("true"));
        check("[LOGIC] TF: uppercase 'TRUE' accepted",            qtrue.checkAnswer("TRUE"));
        check("[LOGIC] TF: mixed case 'False' accepted",         qfalse.checkAnswer("False"));

        check("[STRUCTURAL] TF: getPrompt() stored", "Earth is a planet?".equals(qtrue.getPrompt()));
    }

    // -----------------------------------------------------------------------
    // Member 2 -- QuizModule scoring (ALL 10 boundary values)
    // -----------------------------------------------------------------------
    private static void testQuizScoring() {
        QuizModule qm = new QuizModule();

        check("[LOGIC] motivational   0% -> Don't give up!",    "Don't give up!".equals(qm.motivationalMessage(0)));
        check("[LOGIC] motivational  19% -> Don't give up!",    "Don't give up!".equals(qm.motivationalMessage(19)));
        check("[LOGIC] motivational  20% -> You can do better!","You can do better!".equals(qm.motivationalMessage(20)));
        check("[LOGIC] motivational  39% -> You can do better!","You can do better!".equals(qm.motivationalMessage(39)));
        check("[LOGIC] motivational  40% -> Good try!",         "Good try!".equals(qm.motivationalMessage(40)));
        check("[LOGIC] motivational  59% -> Good try!",         "Good try!".equals(qm.motivationalMessage(59)));
        check("[LOGIC] motivational  60% -> That's good!",      "That's good!".equals(qm.motivationalMessage(60)));
        check("[LOGIC] motivational  79% -> That's good!",      "That's good!".equals(qm.motivationalMessage(79)));
        check("[LOGIC] motivational  80% -> Outstanding!",      "Outstanding!".equals(qm.motivationalMessage(80)));
        check("[LOGIC] motivational 100% -> Outstanding!",      "Outstanding!".equals(qm.motivationalMessage(100)));

        check("[LOGIC] scorePercent() with no questions = 0",   qm.scorePercent() == 0);
    }

    // -----------------------------------------------------------------------
    // Member 3 -- Badge
    // -----------------------------------------------------------------------
    private static void testBadge() {
        Badge b1 = new Badge("Rising Star");
        check("[STRUCTURAL] Badge 1-arg: name stored",         "Rising Star".equals(b1.getName()));
        check("[STRUCTURAL] Badge 1-arg: description empty",   "".equals(b1.getDescription()));

        Badge b2 = new Badge("Scholar", "Scored 100%");
        check("[STRUCTURAL] Badge 2-arg: name stored",         "Scholar".equals(b2.getName()));
        check("[STRUCTURAL] Badge 2-arg: description stored",  "Scored 100%".equals(b2.getDescription()));
    }

    // -----------------------------------------------------------------------
    // Member 3 -- GamificationModule
    // -----------------------------------------------------------------------
    private static void testGamification() {
        GamificationModule gm = new GamificationModule();

        check("[STRUCTURAL] GamificationModule: starts at 0 points",  gm.totalPoints() == 0);
        check("[STRUCTURAL] GamificationModule: starts with no badges",gm.awardedBadges().isEmpty());

        gm.addPoints(30);
        check("[LOGIC] addPoints(30): totalPoints = 30",    gm.totalPoints() == 30);

        gm.addPoints(20, "Quiz completed");
        check("[LOGIC] addPoints(20,reason): totalPoints = 50", gm.totalPoints() == 50);
        check("[LOGIC] Badge awarded at 50 points",             !gm.awardedBadges().isEmpty());

        gm.addPoints(50);
        check("[LOGIC] addPoints(50): totalPoints = 100",       gm.totalPoints() == 100);
        check("[LOGIC] Second badge awarded at 100 points",     gm.awardedBadges().size() >= 2);

        check("[STRUCTURAL] leaderboard() returns non-null",    gm.leaderboard() != null);
    }

    // -----------------------------------------------------------------------
    // Member 4 -- UserProfile
    // -----------------------------------------------------------------------
    private static void testUserProfile() {
        UserProfile u = new UserProfile("Alice");
        check("[STRUCTURAL] UserProfile: getName()",       "Alice".equals(u.getName()));
        check("[STRUCTURAL] UserProfile: initial score 0", u.getLastScore() == 0);

        u.setLastScore(85);
        check("[STRUCTURAL] UserProfile: setLastScore()",  u.getLastScore() == 85);
    }

    // -----------------------------------------------------------------------
    // Member 4 -- Custom exceptions (structural only)
    // -----------------------------------------------------------------------
    private static void testExceptions() {
        DataNotFoundException dnf1 = new DataNotFoundException("not found");
        check("[STRUCTURAL] DataNotFoundException(msg)",        "not found".equals(dnf1.getMessage()));

        DataNotFoundException dnf2 = new DataNotFoundException("wrap", new RuntimeException("cause"));
        check("[STRUCTURAL] DataNotFoundException(msg,cause)",  dnf2.getCause() != null);

        InvalidScoreException ise = new InvalidScoreException("bad score");
        check("[STRUCTURAL] InvalidScoreException(msg)",        "bad score".equals(ise.getMessage()));

        data.exceptions.ContentLoadException cle =
                new data.exceptions.ContentLoadException("load fail", new RuntimeException());
        check("[STRUCTURAL] ContentLoadException(msg,cause)",   "load fail".equals(cle.getMessage()));
    }

    // -----------------------------------------------------------------------
    // Member 4 -- DataManager (uses temp file; cleans up after itself)
    // -----------------------------------------------------------------------
    private static void testDataManager() {
        new File("data").mkdirs();
        new File(TMP).delete(); // ensure absent before tests

        DataManager dm = new DataManager(TMP);

        // readScores on non-existent file must return empty list (not throw)
        try {
            List<String[]> empty = dm.readScores();
            check("[LOGIC] readScores on missing file = empty list", empty != null && empty.isEmpty());
        } catch (DataNotFoundException e) {
            check("[LOGIC] readScores on missing file = empty list -- threw instead", false);
        }

        // saveScore with valid boundary values must NOT throw
        try {
            dm.saveScore("alice", 0);
            check("[LOGIC] saveScore(name, 0) no exception", true);
        } catch (Exception e) {
            check("[LOGIC] saveScore(name, 0) no exception -- threw: " + e.getMessage(), false);
        }
        try {
            dm.saveScore("bob", 100);
            check("[LOGIC] saveScore(name, 100) no exception", true);
        } catch (Exception e) {
            check("[LOGIC] saveScore(name, 100) no exception -- threw: " + e.getMessage(), false);
        }

        // After saving, readScores must return rows
        try {
            List<String[]> rows = dm.readScores();
            check("[LOGIC] readScores after save returns non-empty", !rows.isEmpty());
        } catch (DataNotFoundException e) {
            check("[LOGIC] readScores after save -- threw DataNotFoundException", false);
        }

        // saveScore with INVALID scores must throw InvalidScoreException
        boolean threwNeg = false;
        try { dm.saveScore("x", -1); } catch (InvalidScoreException e) { threwNeg = true; }
        check("[LOGIC] saveScore(name, -1) throws InvalidScoreException", threwNeg);

        boolean threwOver = false;
        try { dm.saveScore("x", 101); } catch (InvalidScoreException e) { threwOver = true; }
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

        new File(TMP).delete(); // cleanup
    }

    // -----------------------------------------------------------------------
    // Harness helpers
    // -----------------------------------------------------------------------
    private static void section(String name) {
        System.out.println("\n--- " + name + " ---");
    }

    private static void check(String label, boolean condition) {
        System.out.println((condition ? "PASS" : "FAIL") + " " + label);
        if (condition) pass++; else fail++;
    }

    private static void banner(String title) {
        System.out.println("================================================");
        System.out.println("  " + title);
        System.out.println("  [STRUCTURAL] passes before any TODO is filled");
        System.out.println("  [LOGIC]      needs TODO implementation to pass");
        System.out.println("================================================");
    }
}
