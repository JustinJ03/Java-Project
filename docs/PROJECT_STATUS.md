# Project Status – EduSDG Java Application

## Course Information
- **Course:** TMF2954 - Java Programming
- **Semester:** Semester 2, S2025/26
- **Deadline:** June 5, 2026
- **SDG Focus:** SDG 4 - Quality Education
- **Repository:** https://github.com/JustinJ03/Java-Project

## Current Architecture

The project uses a **Member 1 shell** architecture (a single JFrame with a bottom tab bar):

- **`src/main/Main.java`** (480×820 JFrame) – entry point
  - `CardLayout` with 4 screens: LEARN, QUIZ, BADGES, PROFILE
  - Bottom tab bar with custom `TabBarIcon` graphics
  - At startup, prompts for the player's name and starts a session (Member 4)
- All four modules plug into the CardLayout as `JPanel`s.

## Member Status

| Member   | Role                        | Status        | Classes                                              | Interface     |
|----------|-----------------------------|---------------|------------------------------------------------------|---------------|
| Member 1 | Learning Module             | ✅ COMPLETE   | LearningModule, ContentPage, LearningModulePanel     | IModule       |
| Member 2 | Quiz Module                 | ✅ COMPLETE   | QuizModule, Question, MCQQuestion, TrueFalseQuestion | IQuiz         |
| Member 3 | Gamification Module         | ✅ COMPLETE   | GamificationModule, Badge                            | IGamification |
| Member 4 | Data & Exception Handling   | ✅ COMPLETE   | DataManager, UserProfile, SessionManager, exceptions/| IDataStorage  |

All four modules are implemented, integrated, and compile cleanly. The app runs end-to-end:
Learn → Quiz (saves score) → Badges (reads leaderboard).

## Live Integration Points

- **Quiz → Data:** `QuizModule.finishQuiz()` calls `DataManager.saveScore(player, percent)`
  using the current session player name (`SessionManager`).
- **Gamification → Data:** `GamificationModule.leaderboard()` calls `DataManager.readScores()`
  and ranks them highest-first.
- **Main → Session:** On launch, `Main` prompts for a name and calls
  `SessionManager.getInstance().startSession(name)`.

## Member 4 — Data Layer (Reference for the Team)

**Real `DataManager` API (file-backed, no hardcoded scores):**
```java
DataManager dm = new DataManager("data/scores.txt");
dm.saveScore(String user, int score);     // 0–100, else throws InvalidScoreException
dm.saveScore(UserProfile profile);        // overload — uses profile name + lastScore
List<String[]> rows = dm.readScores();    // each row = [name, score, timestamp]; empty if no file
```

**`UserProfile`** holds per-session player data: name, lastScore, bestScore, quizzesTaken.
**`SessionManager`** (singleton) holds the current player for the whole app session.

**Custom exceptions** (`src/data/exceptions/`):
- `InvalidScoreException` – score outside 0–100
- `DataNotFoundException` – scores file exists but cannot be read
- `ContentLoadException` – a resource file (content.txt / questions.txt) cannot be loaded

**Data format documentation:** see [`docs/DATA_FORMATS.md`](DATA_FORMATS.md).

## Build and Run

**Compile:** `build.bat`  **Run:** `run.bat`

**Manual compile (from project root):**
```batch
javac -d out src\main\Main.java src\interfaces\*.java src\learning\*.java src\quiz\*.java src\gamification\*.java src\data\*.java src\data\exceptions\*.java
java -cp out main.Main
```

**Run tests:**
```batch
javac -cp out -d out tests\TestSuite.java tests\DebugHelper.java tests\ManualTests.java
java -cp out tests.TestSuite
java -cp out tests.DebugHelper
```

## Submission Checklist

- [x] All 4 members' code complete and integrated
- [x] Application compiles and runs (`run.bat`)
- [x] Scores saved to `data/scores.txt` after a quiz
- [x] Badges / leaderboard displayed in Gamification tab
- [x] Learning module shows 10+ content pages
- [x] Quiz module has 20+ questions
- [x] Motivational score feedback (5 ranges, 0-19% … 80-100%)
- [ ] All `tests.TestSuite` checks green (verify after each member's final push)
- [ ] Project Design PDF with UML prepared (see `docs/ProjectDesignDocument.md`)
- [ ] Final submission uploaded to e-learning platform

## Important Notes

- **Always run from project root** (the folder containing `src/` and `resources/`).
- **File locations:**
  - Quiz questions: `resources/quiz/questions.txt`
  - Score data (runtime): `data/scores.txt` (created automatically; git-ignored)
- **Package structure:** `main`, `learning` (M1), `quiz` (M2), `gamification` (M3),
  `data` + `data.exceptions` (M4), `interfaces` (IQuiz, IGamification, IDataStorage).
  Note: `IModule` lives in the `learning` package.
- **Coordinate before editing** shared files: `Main.java`, and any class you don't own.
