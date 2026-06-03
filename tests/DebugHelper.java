// Test infrastructure -- shared by all members. No renaming needed.
// Standalone diagnostic tool. Run this FIRST if anything seems broken:
//   javac -d out src\main\Main.java src\app\*.java src\interfaces\*.java ^
//         src\learning\*.java src\quiz\*.java src\gamification\*.java ^
//         src\data\*.java src\data\exceptions\*.java tests\DebugHelper.java
//   java -cp out tests.DebugHelper
//
// Also usable as a utility: import tests.DebugHelper; then call static methods.
package tests;

import interfaces.IDataStorage;
import learning.ContentPage;
import learning.LearningModule;
import quiz.Question;
import quiz.QuizModule;
import data.DataManager;
import data.exceptions.DataNotFoundException;

import java.io.File;
import java.util.List;

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
        int major = parseMajorVersion(version);
        System.out.println("[ENV] Java version: " + version);
        if (major < 11) {
            System.out.println("      WARNING: Java 11+ required. Current major=" + major);
        } else {
            System.out.println("      OK -- Java " + major + " meets the >=11 requirement.");
        }
    }

    // ------------------------------------------------------------------
    // 2. Working directory -- must be the project root, NOT src/ or out/
    // ------------------------------------------------------------------
    public static void checkWorkingDirectory() {
        String cwd = System.getProperty("user.dir");
        System.out.println("\n[ENV] Working directory: " + cwd);
        if (new File("resources").isDirectory() && new File("src").isDirectory()) {
            System.out.println("      OK -- resources/ and src/ found. CWD looks correct.");
        } else {
            System.out.println("      WARNING: resources/ or src/ not found in CWD.");
            System.out.println("      Run from the project root, not from inside src/ or out/.");
        }
    }

    // ------------------------------------------------------------------
    // 3. Resource files -- checks for renamed AND suggestion versions
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
                  "scores.txt is created automatically when a quiz is completed.");
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
            System.out.println("      MISS " + expected + " -- " + hint);
        }
    }

    // ------------------------------------------------------------------
    // 4. Dump quiz questions
    // ------------------------------------------------------------------
    public static void dumpQuestions() {
        System.out.println("\n[QUIZ] Loading questions from resources/quiz/questions.txt ...");
        QuizModule qm = new QuizModule();
        List<Question> questions = qm.loadQuestions();
        if (questions.isEmpty()) {
            System.out.println("      0 questions loaded.");
            System.out.println("      Check: (a) questions.txt is renamed, (b) Member 2's");
            System.out.println("      loadQuestions() TODO is implemented.");
        } else {
            long mcq   = questions.stream().filter(q -> q.getClass().getSimpleName().equals("MCQQuestion")).count();
            long tf    = questions.stream().filter(q -> q.getClass().getSimpleName().equals("TrueFalseQuestion")).count();
            long other = questions.size() - mcq - tf;
            System.out.println("      " + questions.size() + " question(s): MCQ=" + mcq + " TF=" + tf + " Other=" + other);
            if (questions.size() < 20) System.out.println("      WARNING: <20 questions loaded. Requirement is >=20.");
            int preview = Math.min(3, questions.size());
            System.out.println("      First " + preview + " prompts:");
            for (int i = 0; i < preview; i++)
                System.out.println("        [" + i + "] " + questions.get(i).getPrompt());
        }
    }

    // ------------------------------------------------------------------
    // 5. Dump learning pages
    // ------------------------------------------------------------------
    public static void dumpLearningPages() {
        System.out.println("\n[LEARN] Loading pages from LearningModule ...");
        LearningModule lm = new LearningModule("Quality Education", "SDG 4");
        List<ContentPage> pages = lm.getAllPages();
        if (pages.isEmpty()) {
            System.out.println("      0 pages loaded.");
            System.out.println("      Check Member 1's LearningModule implementation.");
        } else {
            System.out.println("      " + pages.size() + " page(s) loaded.");
            if (pages.size() < 10) System.out.println("      WARNING: <10 pages loaded. Requirement is >=10.");
            int preview = Math.min(3, pages.size());
            System.out.println("      First " + preview + " titles:");
            for (int i = 0; i < preview; i++)
                System.out.println("        [" + i + "] " + pages.get(i).getTitle());
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
                System.out.println("      No scores saved yet (complete a quiz first, or Member 4's");
                System.out.println("      DataManager TODO may not be implemented yet).");
            } else {
                System.out.println("      " + rows.size() + " row(s):");
                for (String[] row : rows) System.out.println("        " + String.join(", ", row));
            }
        } catch (DataNotFoundException e) {
            System.out.println("      DataNotFoundException: " + e.getMessage());
            System.out.println("      FIX (Member 4): readScores() must return an empty list when");
            System.out.println("      the file is missing, not throw DataNotFoundException.");
        }
    }

    // ------------------------------------------------------------------
    // 7. Test module instantiation (navigation now in Main's bottom tab bar)
    // ------------------------------------------------------------------
    public static void testNavigation() {
        System.out.println("\n[NAV] Testing module instantiation ...");
        try {
            // Simple instantiation check - navigation is now Main's bottom tab bar
            LearningModule lm = new LearningModule("Quality Education", "SDG 4");
            QuizModule qm = new QuizModule();

            System.out.println("      OK   LearningModule instantiated");
            System.out.println("      OK   QuizModule instantiated");
            System.out.println("      NOTE Navigation is now handled by Main's bottom tab bar");
        } catch (Exception e) {
            System.out.println("      ERROR during instantiation: " + e);
            e.printStackTrace();
        }
    }

    // ------------------------------------------------------------------
    // Utility methods (call from your own code during debugging)
    // ------------------------------------------------------------------

    /** Print all saved scores to stdout. */
    public static void printScores(IDataStorage storage) {
        try {
            List<String[]> rows = storage.readScores();
            System.out.println("Scores (" + rows.size() + " rows):");
            for (String[] row : rows) System.out.println("  " + String.join(", ", row));
        } catch (DataNotFoundException e) {
            System.out.println("No scores found: " + e.getMessage());
        }
    }

    /** Print all loaded questions to stdout. */
    public static void printQuestions(List<Question> questions) {
        System.out.println("Questions (" + questions.size() + "):");
        for (int i = 0; i < questions.size(); i++)
            System.out.println("  [" + i + "] " + questions.get(i).getClass().getSimpleName()
                               + ": " + questions.get(i).getPrompt());
    }

    /** Print all loaded content pages to stdout. */
    public static void printPages(List<ContentPage> pages) {
        System.out.println("Pages (" + pages.size() + "):");
        for (int i = 0; i < pages.size(); i++) {
            ContentPage p = pages.get(i);
            String body = p.getBodyText() != null ? p.getBodyText() : "(null)";
            String preview = body.substring(0, Math.min(60, body.length()));
            System.out.println("  [" + i + "] " + p.getTitle());
            System.out.println("      " + preview + (body.length() > 60 ? "..." : ""));
        }
    }

    // ------------------------------------------------------------------
    private static int parseMajorVersion(String version) {
        try {
            String v = version.startsWith("1.") ? version.substring(2) : version;
            return Integer.parseInt(v.split("[.\\-]")[0]);
        } catch (Exception e) { return 0; }
    }
}
