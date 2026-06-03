# Project Status – EduSDG Java Application

## Course Information
- **Course:** TMF2954 - Java Programming
- **Semester:** Semester 2, S2025/26
- **Deadline:** June 5, 2026 (2 days remaining)
- **SDG Focus:** SDG 4 - Quality Education
- **Repository:** [GitHub](https://github.com/foggy-cliff/Java-Project)

## Current Architecture

The project has transitioned from a shared backbone architecture to a **Member 1 shell** architecture:

- **Main.java** (480×820 JFrame) – Central entry point in `src/main/Main.java`
  - CardLayout with 4 screens: LEARN, QUIZ, BADGES, PROFILE
  - Bottom tab bar with custom TabBarIcon graphics
  - Placeholder panels for incomplete modules
- **Member 1 Implementation** – COMPLETE
  - `LearningModule.java` – Data model with SDG content
  - `LearningModulePanel.java` – JPanel UI with scrolling content cards
  - `ContentPage.java` – Individual content page data structure
  - `IModule.java` – Interface (now in learning/ package)
  - All code migrated from resources/ to src/learning/
- **Member 4 Implementation** – COMPLETE
  - `DataManager.java` – File-based data persistence
  - `UserProfile.java` – User profile model
  - `IDataStorage.java` – Interface in src/interfaces/
  - Exception handling: `ContentLoadException`, `DataNotFoundException`, `InvalidScoreException`

## Member Status

| Member   | Role                         | Status       | Classes Required                                     | Interface     |
|----------|------------------------------|--------------|------------------------------------------------------|---------------|
| Member 1 | Core Architecture & Learning | ✅ COMPLETE  | LearningModule, ContentPage, LearningModulePanel     | IModule       |
| Member 2 | Quiz Module                  | ⏳ TODO      | QuizModule, Question, MCQQuestion, TrueFalseQuestion | IQuiz         |
| Member 3 | Gamification Module          | ⏳ TODO      | GamificationModule, Badge                            | IGamification |
| Member 4 | Data & Exception Handling    | ✅ COMPLETE  | DataManager, UserProfile, exceptions/                | IDataStorage  |

## What Needs to Be Done

### Member 2: Quiz Module (TODO)

**Location:** `src/quiz/`

**Classes to implement:**

1. **QuizModule** (extends JPanel, implements IQuiz)
   - Constructor: `QuizModule(LearningModule learningModule)`
   - Load questions from `resources/quiz/questions.txt`
   - Display questions one at a time with radio buttons / true-false buttons
   - Track user answers and calculate score
   - Implement `IQuiz` methods: `loadQuestions()`, `submitAnswer()`, `calculateScore()`
   - At quiz end, call `DataManager.saveScore()` and display motivational feedback

2. **Question** (abstract base class)
   - Fields: `String questionText`, `String correctAnswer`
   - Abstract method: `boolean checkAnswer(String userAnswer)`

3. **MCQQuestion** (extends Question)
   - Field: `String[] options` (A, B, C, D)
   - Implement `checkAnswer()` to compare selected option

4. **TrueFalseQuestion** (extends Question)
   - No additional fields needed
   - Implement `checkAnswer()` to compare "True" or "False"

**Integration steps:**
- Uncomment lines in `Main.java`:
  ```java
  // quizModule = new QuizModule(learningModule);
  // cardPanel.add(new QuizPanel(quizModule), "QUIZ");
  ```
- Replace the QUIZ placeholder with your panel
- Test with `java -cp out tests.TestSuite`

**Resources:**
- Question bank: `resources/quiz/questions.txt` (20+ questions in format: `MCQ|text|A,B,C,D|correct` or `TF|text|True/False`)

### Member 3: Gamification Module (TODO)

**Location:** `src/gamification/`

**Classes to implement:**

1. **GamificationModule** (extends JPanel, implements IGamification)
   - Constructor: `GamificationModule(UserProfile userProfile)`
   - Display badges, points, and leaderboard
   - Award badges based on quiz scores (e.g., "First Quiz" badge, "80+ Score" badge)
   - Implement `IGamification` methods: `awardBadge()`, `updatePoints()`, `getLeaderboard()`
   - UI should show earned badges with icons and descriptions

2. **Badge** (data class)
   - Fields: `String name`, `String description`, `boolean earned`, `int pointValue`
   - Constructor and getters

**Integration steps:**
- Uncomment lines in `Main.java`:
  ```java
  // gamifModule = new GamificationModule(userProfile);
  // cardPanel.add(gamifModule, "BADGES");
  ```
- Replace the BADGES placeholder with your panel
- Test with `java -cp out tests.TestSuite`

**Badge suggestions:**
- "Beginner" (complete first quiz)
- "Good Start" (score 40%+)
- "Well Done" (score 60%+)
- "Excellence" (score 80%+)
- "Quiz Master" (complete 5 quizzes)

### Both Members: Integration with DataManager (Member 4)

Member 4 has already implemented `DataManager.java` and `UserProfile.java` for you:

**In QuizModule (Member 2):**
```java
// After quiz completion:
dataManager.saveScore(userProfile.getUserId(), score, totalQuestions);
```

**In GamificationModule (Member 3):**
```java
// Load user profile:
UserProfile profile = dataManager.loadProfile("Player1");
int totalPoints = profile.getTotalPoints();
List<String> badges = profile.getBadges();

// Award badge:
dataManager.awardBadge(profile.getUserId(), "Quiz Master");
```

**DataManager API (already implemented):**
- `saveScore(String userId, int score, int totalQuestions)` – Save quiz result to `data/scores.txt`
- `loadProfile(String userId)` – Load user profile from file
- `awardBadge(String userId, String badgeName)` – Add badge to user profile
- Throws: `DataNotFoundException`, `InvalidScoreException`, `ContentLoadException`

## Build and Run

**Compile:**
```batch
build.bat
```

**Run:**
```batch
run.bat
```

**Manual compile (if needed):**
```batch
javac -d out src\main\Main.java src\learning\*.java src\quiz\*.java src\gamification\*.java src\data\*.java src\data\exceptions\*.java src\interfaces\*.java
java -cp out main.Main
```

**Run tests:**
```batch
javac -cp out -d out tests\TestSuite.java tests\DebugHelper.java tests\ManualTests.java
java -cp out tests.TestSuite
java -cp out tests.DebugHelper
```

Tests are in `tests/`:
- `TestSuite.java` – Automated structural and logic tests
- `DebugHelper.java` – Environment diagnostics (check classpath, file access)
- `ManualTests.java` – Basic logic validation

## Submission Checklist

- [ ] All 4 members' code complete and integrated
- [ ] All tests passing (`tests.TestSuite` shows green)
- [ ] Application runs without errors (`run.bat`)
- [ ] Scores saved to `data/scores.txt` after quiz completion
- [ ] Badges displayed correctly in Gamification tab
- [ ] Learning module shows 10+ content pages
- [ ] Quiz module has 20+ questions
- [ ] Motivational score feedback working (5 ranges from 0-19% to 80-100%)
- [ ] README.md updated (if needed)
- [ ] UML diagram reviewed (`docs/uml.puml`, `docs/uml.md`)
- [ ] Project Design PDF prepared (required deliverable)
- [ ] Code committed to GitHub (`git push origin main`)
- [ ] Final submission uploaded to e-learning platform

## Important Notes

- **Always run from project root** (the folder containing `src/` and `resources/`)
- **Do not modify** `Main.java`, `DataManager.java`, `UserProfile.java`, or `LearningModule.java` without team discussion
- **File locations:**
  - Learning content: `resources/learning/content.txt`
  - Quiz questions: `resources/quiz/questions.txt`
  - Score data (runtime): `data/scores.txt` (created automatically)
- **Package structure:**
  - `main` – Main.java entry point
  - `learning` – Member 1 classes
  - `quiz` – Member 2 classes
  - `gamification` – Member 3 classes
  - `data` – Member 4 classes
  - `interfaces` – IQuiz, IGamification, IDataStorage

## Recent Changes (Tasks 1-11)

1. ✅ Migrated Member 1's main.java → `src/main/Main.java`
2. ✅ Migrated Member 1's `IModule.java` → `src/learning/IModule.java`
3. ✅ Migrated Member 1's `ContentPage.java` → `src/learning/ContentPage.java`
4. ✅ Migrated Member 1's `LearningModule.java` → `src/learning/LearningModule.java`
5. ✅ Migrated Member 1's `LearningModulePanel.java` → `src/learning/LearningModulePanel.java`
6. ✅ Removed old backbone (`src/app/*`, `src/interfaces/IModule.java`) – empty now
7. ✅ Updated `QuizModule.java` stub (removed AppModule inheritance)
8. ✅ Updated `GamificationModule.java` stub (removed AppModule inheritance)
9. ✅ Fixed `TestSuite.java` for new API
10. ✅ Fixed `DebugHelper.java` for new API
11. ✅ Fixed `ManualTests.java` for new API
12. ✅ Created this status document

## Questions or Issues?

Contact team members via the project group chat. Good luck with the final push!
