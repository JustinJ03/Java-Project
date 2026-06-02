// SUGGESTION (AI-generated reference). Rename to DebugHelper.java and merge into your own work.
// Do not overwrite code you have already written.  Owner: shared
//
// A standalone diagnostic tool. Run it at any point to check your environment:
//   javac -d out src\**\*.java tests\DebugHelper.java
//   java -cp out tests.DebugHelper
//
// Also usable as a utility: import tests.DebugHelper; then call static methods.
package tests;

import app.AppContext;
import app.Navigator;
import interfaces.IDataStorage;
import interfaces.IModule;
import learning.ContentPage;
import learning.LearningModule;
import quiz.Question;
import quiz.QuizModule;
import data.DataManager;
import data.exceptions.DataNotFoundException;

import java.io.*;
import java.util.List;

/**
 * Runtime diagnostics for development. Helps members confirm their environment
 * is set up correctly before debugging logic errors.
 */
public class DebugHelper {

    public static void main(String[] args) {
        System.out.println("==============================================");
        System.out.println("  EduSDG DebugHelper");
        System.out.println("  Run from the project root directory.");
        System.out.println("==============================================\n");

        checkJavaVersion();
        checkWorkingDirectory();
        checkResourceFiles();
        dumpQuestions();
        dumpLearningPages();
        dumpScores("data/scores.txt");
        testNavigation();

        System.out.println("\n==============================================");
        System.out.println("  Diagnostics complete. Review output above.");
        System.out.println("==============================================");
    }

    // ------------------------------------------------------------------
    // 1. Java version
    // ------------------------------------------------------------------
    public static void checkJavaVersion() {
        String version = System.getProperty("java.version");
        System.out.println("[ENV] Java version: " + version);
        int major = parseMajorVersion(version);
        if (major < 11) {
            System.out.println("      WARNING: Java 11+ required. Current major=" + major);
        } else {
            System.out.println("      OK — Java " + major + " meets the >=11 requirement.");
        }
    }

    // ------------------------------------------------------------------
    // 2. Working directory — must be the project root, NOT src/ or out/
    // ------------------------------------------------------------------
    public static void checkWorkingDirectory() {
        String cwd = System.getProperty("user.dir");
        System.out.println("\n[ENV] Working directory: " + cwd);
        boolean hasResources = new File("resources").isDirectory();
        boolean hasSrc       = new File("src").isDirectory();
        if (hasResources && hasSrc) {
            System.out.println("      OK — resources/ and src/ found. CWD looks correct.");
        } else {
            System.out.println("      WARNING: resources/ or src/ not found in CWD.");
            System.out.println("      Run 'java -cp out tests.DebugHelper' from the project root,");
            System.out.println("      not from inside src/ or out/.");
        }
    }

    // ------------------------------------------------------------------
    // 3. Resource files — checks for BOTH the renamed and suggestion names
    // ------------------------------------------------------------------
    public static void checkResourceFiles() {
        System.out.println("\n[FILES] Resource file check:");
        checkFile("resources/quiz/questions.txt",
                  "resources/quiz/questions (suggestion).txt",
                  "Rename 'questions (suggestion).txt' to 'questions.txt'");
        checkFile("resources/learning/content.txt",
                  "resources/learning/content (suggestion).txt",
                  "Rename 'content (suggestion).txt' to 'content.txt'");
        checkFile("data/scores.txt", null,
                  "scores.txt will be created automatically when a quiz is completed.");
    }

    private static void checkFile(String expected, String suggestion, String hint) {
        File f = new File(expected);
        if (f.exists()) {
            System.out.println("      OK   " + expected + " (" + f.length() + " bytes)");
        } else if (suggestion != null && new File(suggestion).exists()) {
            System.out.println("      WARN " + expected + " not found.");
            System.out.println("           Suggestion file exists: " + suggestion);
            System.out.println("           Action: " + hint);
        } else {
            System.out.println("      MISS " + expected + " — " + hint);
        }
    }

    // ------------------------------------------------------------------
    // 4. Dump quiz questions (after questions.txt exists)
    // ------------------------------------------------------------------
    public static void dumpQuestions() {
        System.out.println("\n[QUIZ] Loading questions from resources/quiz/questions.txt ...");
        QuizModule qm = new QuizModule(new AppContext());
        List<Question> questions = qm.loadQuestions();
        if (questions.isEmpty()) {
            System.out.println("      0 questions loaded.");
            System.out.println("      If questions.txt exists, Member 2's loadQuestions() TODO");
            System.out.println("      may not be implemented yet, OR the file isn't renamed.");
        } else {
            System.out.println("      " + questions.size() + " question(s) loaded.");
            int mcq = 0, tf = 0, other = 0;
            for (Question q : questions) {
                String type = q.getClass().getSimpleName();
                if ("MCQQuestion".equals(type))       mcq++;
                else if ("TrueFalseQuestion".equals(type)) tf++;
                else other++;
            }
            System.out.println("      MCQ=" + mcq + "  TrueFalse=" + tf + "  Other=" + other);
            if (questions.size() < 20) {
                System.out.println("      WARNING: <20 questions loaded. Requirement is >=20.");
            }
            // Show first 3 as sample
            int preview = Math.min(3, questions.size());
            System.out.println("      First " + preview + " prompts:");
            for (int i = 0; i < preview; i++) {
                System.out.println("        [" + i + "] " + questions.get(i).getPrompt());
            }
        }
    }

    // ------------------------------------------------------------------
    // 5. Dump learning pages
    // ------------------------------------------------------------------
    public static void dumpLearningPages() {
        System.out.println("\n[LEARN] Loading pages from resources/learning/content.txt ...");
        LearningModule lm = new LearningModule(new AppContext());
        List<ContentPage> pages = lm.loadPages();
        if (pages.isEmpty()) {
            System.out.println("      0 pages loaded.");
            System.out.println("      If content.txt exists, Member 1's loadPages() TODO");
            System.out.println("      may not be implemented yet, OR the file isn't renamed.");
        } else {
            System.out.println("      " + pages.size() + " page(s) loaded.");
            if (pages.size() < 10) {
                System.out.println("      WARNING: <10 pages loaded. Requirement is >=10.");
            }
            int preview = Math.min(3, pages.size());
            System.out.println("      First " + preview + " titles:");
            for (int i = 0; i < preview; i++) {
                System.out.println("        [" + i + "] " + pages.get(i).getTitle());
            }
        }
    }

    // ------------------------------------------------------------------
    // 6. Dump saved scores
    // ------------------------------------------------------------------
    public static void dumpScores(String filePath) {
        System.out.println("\n[DATA] Scores in " + filePath + " ...");
        DataManager dm = new DataManager(filePath);
        try {
            List<String[]> rows = dm.readScores();
            if (rows.isEmpty()) {
                System.out.println("      No scores saved yet (or Member 4's TODO not implemented).");
            } else {
                System.out.println("      " + rows.size() + " row(s):");
                for (String[] row : rows) {
                    System.out.println("        " + String.join(", ", row));
                }
            }
        } catch (DataNotFoundException e) {
            System.out.println("      DataNotFoundException: " + e.getMessage());
            System.out.println("      Member 4: readScores() should return an empty list when");
            System.out.println("      the file is missing, not throw an exception.");
        }
    }

    // ------------------------------------------------------------------
    // 7. Test module navigation
    // ------------------------------------------------------------------
    public static void testNavigation() {
        System.out.println("\n[NAV] Testing module navigation ...");
        try {
            AppContext ctx = new AppContext();
            Navigator nav = ctx.navigator();

            learning.LearningModule lm = new learning.LearningModule(ctx);
            quiz.QuizModule qm         = new quiz.QuizModule(ctx);
            gamification.GamificationModule gm = new gamification.GamificationModule(ctx);

            nav.registerModule(lm);
            nav.registerModule(qm);
            nav.registerModule(gm);

            nav.show("Learning");
            System.out.println("      OK   navigate to Learning");
            nav.show("Quiz");
            System.out.println("      OK   navigate to Quiz");
            nav.show("Gamification");
            System.out.println("      OK   navigate to Gamification");
            nav.show("home");  // back to home (not a module, but should not crash)
            System.out.println("      OK   navigate to home (no crash)");
        } catch (Exception e) {
            System.out.println("      ERROR during navigation: " + e);
            e.printStackTrace();
        }
    }

    // ------------------------------------------------------------------
    // Utility: print any IDataStorage contents (for use in your own code)
    // ------------------------------------------------------------------
    public static void printScores(IDataStorage storage) {
        try {
            List<String[]> rows = storage.readScores();
            System.out.println("Scores (" + rows.size() + " rows):");
            for (String[] row : rows) System.out.println("  " + String.join(", ", row));
        } catch (DataNotFoundException e) {
            System.out.println("No scores found: " + e.getMessage());
        }
    }

    // ------------------------------------------------------------------
    // Utility: print all questions (call from QuizModule while debugging)
    // ------------------------------------------------------------------
    public static void printQuestions(List<Question> questions) {
        System.out.println("Questions (" + questions.size() + "):");
        for (int i = 0; i < questions.size(); i++) {
            Question q = questions.get(i);
            System.out.println("  [" + i + "] " + q.getClass().getSimpleName()
                               + ": " + q.getPrompt());
        }
    }

    // ------------------------------------------------------------------
    // Utility: print all content pages (call from LearningModule while debugging)
    // ------------------------------------------------------------------
    public static void printPages(List<ContentPage> pages) {
        System.out.println("Pages (" + pages.size() + "):");
        for (int i = 0; i < pages.size(); i++) {
            ContentPage p = pages.get(i);
            System.out.println("  [" + i + "] " + p.getTitle());
            System.out.println("      " + p.getBody().substring(0, Math.min(60, p.getBody().length())) + "...");
        }
    }

    // ------------------------------------------------------------------
    // Helpers
    // ------------------------------------------------------------------
    private static int parseMajorVersion(String version) {
        try {
            String v = version.startsWith("1.") ? version.substring(2) : version;
            return Integer.parseInt(v.split("[.\\-]")[0]);
        } catch (Exception e) { return 0; }
    }
}
