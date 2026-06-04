# Prompt: Generate EduSDG Project Design Document Report Draft

> **Instructions for Claude Chat:**
> Read this entire file before writing anything. It contains:
> 1. The course requirement context
> 2. The original project design (from the PDF template — partially outdated)
> 3. The **actual implemented code** (what was really built — use this as ground truth)
> 4. A delta table of every design change
> 5. Your specific writing task at the bottom
>
> Write the report based on the **actual implementation**, not the original design.
> Where the design document template asks for something that changed, describe what was actually built and note the deviation clearly.

---

## PART 1 — Course & Submission Context

| Field | Detail |
|---|---|
| Course | TMF2954 – Java Programming |
| Semester | Semester 2, S2025/26 |
| Project Title | EduSDG – SDG 4 Quality Education Desktop Application |
| Submission | Friday, 5 June 2026 |
| Language | Java (JDK 11+) |
| Platform | Desktop — Java Swing GUI, 480 × 820 px fixed window |
| Data Storage | Text file `data/scores.txt` — CSV, runtime-generated, no hardcoded scores |
| Execution | `java -jar EduSDG.jar` (JAR built from `build.bat`) |

### Group Members

| No. | Role | Owns |
|---|---|---|
| 1 | Core Architecture & Learning Module | `LearningModule`, `LearningModulePanel`, `ContentPage`, `IModule` |
| 2 | Quiz Module | `QuizModule`, `Question`, `MCQQuestion`, `TrueFalseQuestion`, `IQuiz` |
| 3 | Gamification Module | `GamificationModule`, `Badge`, `IGamification` |
| 4 | Data & Exception Handling | `DataManager`, `UserProfile`, `ProfilePanel`, `IDataStorage`, all 3 exceptions |

*(Fill in actual names and matric numbers before submitting.)*

---

## PART 2 — Original Design Document (PDF Template — May Be Outdated)

The following was the intended architecture from the project design template PDF. It is included here for reference so you can accurately describe what **changed** and **why** in the report.

### Intended Architecture (from PDF)

```
Main
 └── AppContext  (holds Navigator + IDataStorage)
       └── Navigator  (CardLayout screen switcher)
             ├── HomeScreen
             ├── LearningModule  ──► IModule
             ├── QuizModule      ──► IModule, IQuiz
             └── GamificationModule ──► IModule, IGamification

DataManager ──► IDataStorage  (reads/writes data/scores.txt)
```

**Intended packages from PDF:**

| Package | Intended contents |
|---|---|
| `app` | Theme, Screen, AppModule, Navigator, AppContext, HomeScreen |
| `interfaces` | IModule, IQuiz, IGamification, IDataStorage |
| `main` | Main.java |
| `learning` | LearningModule, ContentPage |
| `quiz` | QuizModule, Question, MCQQuestion, TrueFalseQuestion |
| `gamification` | GamificationModule, Badge |
| `data` | DataManager, UserProfile, exceptions/ |

**Intended window size:** 360 × 720 px  
**Intended execution:** `build.bat` → `run.bat`  
**Intended content loading:** `LearningModule` reads `resources/learning/content.txt` at runtime  

---

## PART 3 — Actual Implemented Architecture (Ground Truth)

### 3.1 Package Structure (as built)

```
src/
  interfaces/
    IQuiz.java            Quiz contract (Member 2)
    IGamification.java    Gamification contract (Member 3)
    IDataStorage.java     Storage contract (Member 4)
  learning/
    IModule.java          Module contract — 9 methods (Member 1, in learning/ not interfaces/)
    ContentPage.java      One educational page model (Member 1)
    LearningModule.java   10-page learning flow, hardcoded content (Member 1)
    LearningModulePanel.java  Swing GUI panel for learning (Member 1)
  quiz/
    Question.java         Abstract question base (Member 2)
    MCQQuestion.java      Multiple-choice question (Member 2)
    TrueFalseQuestion.java True/False question (Member 2)
    QuizModule.java       Quiz flow, timer, scoring (Member 2)
  gamification/
    Badge.java            Badge model (Member 3)
    GamificationModule.java  Points, 13 badges, leaderboard (Member 3)
  data/
    DataManager.java      File-based score storage (Member 4)
    UserProfile.java      Player stats model (Member 4)
    ProfilePanel.java     Score History tab GUI (Member 4 — new, not in design)
    exceptions/
      ContentLoadException.java   (Member 4)
      InvalidScoreException.java  (Member 4)
      DataNotFoundException.java  (Member 4)
  main/
    Main.java             Entry point + JFrame + 4-tab CardLayout

resources/
  quiz/questions.txt      37 questions: 27 MCQ + 10 True/False
  learning/Images/        10 .png images (sdg4_intro, barriers, digital, etc.)

data/
  scores.txt              Runtime-generated CSV: name,score,epochMs
```

### 3.2 Actual Main / Navigation Structure

There is **no `app` package**, no `AppModule`, no `Navigator`, no `AppContext`, no `HomeScreen`, and no `Theme` class. Instead:

- `Main` extends `JFrame` directly and uses Java `CardLayout` with **4 bottom tabs**
- Tabs: **Learn**, **Quiz**, **Leaderboard**, **Profile**
- Tab switching is handled by `Main.showTab(int idx)` using `CardLayout.show()`
- Modules are wired together in `Main.initModels()` via setter injection:
  ```java
  quizModule.setGamificationModule(gamifModule);
  quizModule.setGoToLeaderboardCallback(() -> showTab(2));
  ```
- `gamifModule.onShow()` and `profilePanel.onShow()` are called on tab switch to refresh data
- Window size: **480 × 820 px** (not 360 × 720 as originally planned)

### 3.3 IModule Interface (actual — 9 methods, in `learning/` package)

```java
package learning;
public interface IModule {
    void loadContent();
    void displayPage(int pageNumber);
    void nextPage();
    void prevPage();
    int  getTotalPages();
    int  getCurrentPage();
    double getProgress();
    boolean isCompleted();
    void reset();
}
```

> The design doc defined IModule with only 3 methods (`getName`, `getView`, `onShow`).  
> The actual IModule has 9 methods focused on content navigation and progress tracking.  
> IModule lives in `learning/` not `interfaces/`.

### 3.4 LearningModule (actual)

- Implements `IModule` directly (does **not** extend AppModule — AppModule was never created)
- **Hardcodes 10 content pages** in `loadContent()` — does NOT read `content.txt`
- Each page: `pageNumber`, `title`, `bodyText`, `imagePath`, `keyFact`, `viewed` flag
- Progress tracked via `getProgress()` (% of pages viewed)
- Module complete only when user taps **Finish** on last page → `markFinished()`
- Images stored in `resources/learning/Images/` (capital I); loaded via `new File()` path

### 3.5 ContentPage (actual fields)

```java
private int    pageNumber;   // 1-based position
private String title;
private String bodyText;
private String imagePath;    // null if no image
private String keyFact;      // null if no key fact (optional)
private boolean viewed;      // true after user opens this page
```

Three constructors (constructor overloading):
- `ContentPage(pageNumber, title, bodyText, imagePath, keyFact)` — full
- `ContentPage(pageNumber, title, bodyText, imagePath)` — no key fact
- `ContentPage(pageNumber, title, bodyText)` — no image, no key fact

> Design doc showed ContentPage with only 3 fields (title, body, imagePath) and 2 constructors. Actual has 6 fields and 3 constructors including `keyFact` and page tracking.

### 3.6 QuizModule (actual)

- Extends `JPanel`, implements `IQuiz`
- **Does NOT extend AppModule** (AppModule was never created)
- **No `AppContext` constructor argument** — uses setter injection instead
- Loads questions from `resources/quiz/questions.txt` at construction
- **300-second countdown timer** (not in design doc) — turns red under 20s, auto-submits at 0
- **Empty answer guard**: shows `JOptionPane` warning if user submits without selecting
- Player name prompted via `JOptionPane` at quiz start, saved with score
- Results screen shows: score %, breakdown, motivational message, time used
- **"View Leaderboard" button** on results → navigates to tab 2 via callback
- `finishQuiz()` calls `gamifModule.onQuizComplete(playerName, correctCount, total, timeLeft)`

**IQuiz implementation (actual):**
```java
List<Question> loadQuestions() throws ContentLoadException;
int scorePercent();
String motivationalMessage(int percent);
// motivationalMessage bands: ≥80 "Outstanding!", ≥60 "That's good!", ≥40 "Good try!",
//                            ≥20 "You can do better!", else "Don't give up!"
```

### 3.7 GamificationModule (actual)

- Extends `JPanel`, implements `IGamification`
- **Does NOT extend AppModule**
- Tab title: **"🏆 Leaderboard & Badges"**
- `onQuizComplete(String playerName, int score, int total, int timeLeft)` — called by QuizModule
- **13 badge types** (not in original design doc):

| Category | Badge | Condition |
|---|---|---|
| Score | Perfect Score | 100% |
| Score | High Achiever | ≥80% |
| Score | Good Learner | 60–79% |
| Speed | Lightning Fast | Finished in ≤60s |
| Speed | Quick Thinker | Finished in ≤150s |
| Speed | Speed Runner | Any time left remaining |
| Milestone | First Step | Completed first quiz |
| Milestone | Dedicated | 3 quizzes completed |
| Milestone | Quiz Master | 5 quizzes completed |
| Personal | Comeback | Beat personal best score |
| Leaderboard | Top Scorer | Highest all-time score in scores.txt |
| Points | Rising Star / Scholar / Champion | 50 / 100 / 200 points |

- **Method overloading** (as designed): `addPoints(int p)` and `addPoints(int p, String reason)`
- Points awarded by score band + speed bonus (`timeLeft / 6` points)

### 3.8 Badge (actual)

```java
private final String name;
private final String description;
private final String icon;   // emoji (e.g. "💯", "⚡", "🏆")
```

Two constructors (constructor overloading):
- `Badge(String name, String description)` — uses default "🏅" icon
- `Badge(String name, String description, String icon)` — full 3-arg

Also overrides `equals()` and `hashCode()` (case-insensitive name comparison) to prevent duplicate badges.

> Design doc showed Badge with 2 fields (name, description) and 1-arg + 2-arg constructors.  
> Actual has 3 fields (added icon) and 2-arg + 3-arg constructors.

### 3.9 DataManager (actual)

- Implements `IDataStorage` — same as designed
- Saves to `data/scores.txt` as CSV: `name,score,epochMilliseconds`
- **Method overloading**: `saveScore(String, int)` and `saveScore(UserProfile)`
- Throws `InvalidScoreException` if score outside 0–100
- Throws `DataNotFoundException` if file exists but cannot be read
- Returns empty list (not exception) if file does not exist yet

### 3.10 UserProfile (actual)

```java
private final String name;
private int lastScore;
private int bestScore;      // new — not in design doc
private int quizzesTaken;   // new — not in design doc
```

Key method: `recordScore(int score)` — atomically updates lastScore, increments quizzesTaken, updates bestScore if higher. Demonstrates **abstraction** (caller says "record score", internal updates happen automatically).

> Design doc showed UserProfile with only 2 fields (name, lastScore). Actual tracks 4 fields.

### 3.11 ProfilePanel (actual — NOT in design doc)

A completely new class owned by Member 4. It is the **4th tab** ("Profile / Score History").

Features:
- Reads `data/scores.txt` via `DataManager`
- Shows 3 summary stat cards: **Total Quizzes**, **Best Score**, **Average**
- Shows a `JTable` leaderboard sorted highest score first (Rank / Name / Score / Date)
- Shows absolute file path of `scores.txt`
- **Refresh button** to reload from disk on demand
- `onShow()` called by `Main` on tab switch for auto-refresh

### 3.12 Exception Classes (actual)

All three extend `Exception` (checked exceptions, compile-time enforcement):

| Class | Constructors | Thrown by |
|---|---|---|
| `ContentLoadException` | `(String)` and `(String, Throwable)` | `QuizModule.loadQuestions()` |
| `InvalidScoreException` | `(String)` | `DataManager.saveScore()` |
| `DataNotFoundException` | `(String)` and `(String, Throwable)` | `DataManager.readScores()` |

---

## PART 4 — Design vs. Reality Delta Table

| Design Doc Said | What Was Actually Built | Impact |
|---|---|---|
| `app` package with Theme, Screen, AppModule, Navigator, AppContext, HomeScreen | **Not implemented.** These classes do not exist. | All modules extend JPanel directly instead of AppModule. Navigation is handled by Main+CardLayout. |
| Window size 360 × 720 px | **480 × 820 px** | Wider viewport for better content display |
| Execution: `build.bat` → `run.bat` | **`java -jar EduSDG.jar`** | JAR packaging required by brief |
| `IModule` in `interfaces/` with 3 methods | **`IModule` in `learning/` with 9 methods** (navigation + progress tracking) | Richer interface covering full page lifecycle |
| `LearningModule` reads `content.txt` | **Content hardcoded** in `loadContent()`; content.txt removed | Simpler deployment, no file dependency for content |
| `ContentPage(title, body)` — 2 constructors, 3 fields | **3 constructors, 6 fields** (+pageNumber, keyFact, viewed) | Richer data model, tracks reading progress |
| No timer in quiz | **300-second countdown** with colour change at ≤20s | Adds urgency; feeds speed-based badges |
| No HomeScreen tab navigation | **4-tab CardLayout** (Learn / Quiz / Leaderboard / Profile) | Always-visible navigation instead of home menu |
| `GamificationModule` shows badges at point thresholds | **13 badge types** covering score, speed, milestones, leaderboard | Much richer gamification loop |
| `Badge` with 2 fields, 1+2 arg constructors | **3 fields** (+icon emoji), 2+3 arg constructors | Visual badges with emoji icons in UI |
| `UserProfile` with 2 fields (name, lastScore) | **4 fields** (+bestScore, quizzesTaken) + `recordScore()` | Complete session stats for Profile tab |
| No Profile/Score History tab | **ProfilePanel** — 4th tab with JTable leaderboard + stats | New feature entirely |
| Modules communicate via `AppContext.storage()` | **Setter injection** via `setGamificationModule()` + callbacks | Simpler coupling without AppContext |
| QuizModule has no cross-module notification | **`onQuizComplete()`** called at quiz end → awards badges immediately | Tight quiz→gamification integration |

---

## PART 5 — OOP Concepts Demonstrated (Actual Implementation)

| OOP Concept | Where Applied in Actual Code |
|---|---|
| **Abstraction** | `IModule` (9-method contract), `IQuiz`, `IGamification`, `IDataStorage` — interfaces hide implementation details. `Question` abstract class hides how each type checks answers. |
| **Inheritance** | `MCQQuestion extends Question`, `TrueFalseQuestion extends Question`. `LearningModule implements IModule`, `QuizModule implements IQuiz`, `GamificationModule implements IGamification`, `DataManager implements IDataStorage`. |
| **Polymorphism** | `QuizModule` holds `List<Question>` and calls `q.checkAnswer()` / `q.render()` without knowing whether it is MCQ or TrueFalse. `ProfilePanel` and `GamificationModule` call `dataManager.readScores()` through the `IDataStorage` reference. |
| **Method Overriding** | `checkAnswer()` and `render()` overridden in `MCQQuestion` and `TrueFalseQuestion`. `isCompleted()`, `loadContent()`, `nextPage()`, `prevPage()` overridden in `LearningModule`. |
| **Method Overloading** | `DataManager.saveScore(String, int)` and `saveScore(UserProfile)`. `GamificationModule.addPoints(int)` and `addPoints(int, String)`. |
| **Constructor Overloading** | `ContentPage` — 3 constructors. `Badge` — 2 constructors. `DataNotFoundException` — 2 constructors. `ContentLoadException` — 2 constructors. |
| **Encapsulation** | All fields private; accessed through getters. `UserProfile.recordScore()` encapsulates all stat-update logic behind a single method call. |
| **Custom Exception Handling** | `ContentLoadException`, `InvalidScoreException`, `DataNotFoundException` — checked exceptions used in `try-catch` blocks across all 4 members' code. |

---

## PART 6 — Component Interaction Flow (Actual)

### Quiz flow
```
User taps "Quiz" tab
 └── Main.showTab(1) → quizModule.onShow() → shows start screen
 └── User clicks "Start Quiz"
       └── JOptionPane prompts for player name
       └── questions shuffled, 300s timer starts
       └── Each question: question.render() displayed (polymorphism)
       └── User selects answer, clicks Submit
             └── q.checkAnswer(response) called (MCQ or TF polymorphism)
             └── Empty answer → JOptionPane warning, stay on question
       └── After all questions or timer expires:
             └── DataManager.saveScore(playerName, percent) → data/scores.txt
             └── gamifModule.onQuizComplete(playerName, score, total, timeLeft)
                   └── addPoints() → check badge thresholds
                   └── awardBadge() → JOptionPane badge popup
             └── Results screen: score %, breakdown, motivational message
             └── "View Leaderboard" button → Main.showTab(2)
```

### Leaderboard/Profile refresh flow
```
User taps "Leaderboard" tab
 └── Main.showTab(2) → gamifModule.onShow()
       └── refreshLeaderboardList() → dataManager.readScores() → sort by score

User taps "Profile" tab
 └── Main.showTab(3) → profilePanel.onShow()
       └── refreshData() → dataManager.readScores() → sort → populate JTable
```

---

## PART 7 — File Listing (Actual, as of 5 June 2026)

```
src/
  interfaces/
    IQuiz.java
    IGamification.java
    IDataStorage.java
  learning/
    IModule.java
    ContentPage.java
    LearningModule.java
    LearningModulePanel.java
  quiz/
    Question.java
    MCQQuestion.java
    TrueFalseQuestion.java
    QuizModule.java
  gamification/
    Badge.java
    GamificationModule.java
  data/
    DataManager.java
    UserProfile.java
    ProfilePanel.java
    exceptions/
      ContentLoadException.java
      InvalidScoreException.java
      DataNotFoundException.java
  main/
    Main.java

resources/
  quiz/questions.txt        (37 questions: 27 MCQ + 10 T/F)
  learning/Images/          (10 PNG images for lesson pages)

data/
  scores.txt                (runtime-generated CSV)
```

**Total Java source files: 20**

---

## PART 8 — YOUR WRITING TASK

You are writing the **Project Design Document** for the TMF2954 Java Programming group project submission. The document must follow the structure below. Write in clear, formal English suitable for a university assignment report.

**Use the ACTUAL IMPLEMENTATION (Parts 3–7 above) as your source of truth.** Where things changed from the original design (Part 2 and the delta table in Part 4), describe what was actually built and briefly note why the approach changed.

### Required Sections to Write

1. **Project Overview** (~200 words)
   - What EduSDG is, what it does, who it is for
   - Platform, language, execution method, data storage
   - The 4 tabs (Learn, Quiz, Leaderboard, Profile) and their purpose

2. **Application Architecture** (~300 words + code block)
   - Describe the actual architecture (Main + CardLayout + 4 modules)
   - Package summary table
   - Module wiring: how Main connects them via setter injection
   - Note why the `app` package (AppModule/Navigator/AppContext) was simplified away

3. **UML Class Diagram** (description only — note "[INSERT DIAGRAM HERE]")
   - Write a detailed text description of what the UML diagram should contain
   - List every class, interface, and relationship (inheritance, implementation, association) with correct UML notation
   - Use the actual class signatures from Part 3

4. **Class and Interface Distribution** (~600 words total, ~150 words per member)
   - For each of the 4 members, describe:
     - Which interface they own and what it defines
     - Which classes they own
     - What OOP concepts their code demonstrates (with specific method/class examples)
     - How their code connects to the other members' code

5. **Component Interaction Summary** (~200 words + flow diagram)
   - Describe the quiz flow and leaderboard/profile refresh flow
   - Use the flows in Part 6 above

6. **OOP Concepts Demonstrated** (table + 1 paragraph each concept)
   - Use the table from Part 5 as the base
   - Expand each row into a short paragraph with a concrete code example

7. **Appendix – File Listing**
   - Use the actual file listing from Part 7

### Tone and Format

- Formal academic English
- Use present tense ("The application **uses**...", "DataManager **implements**...")
- Use inline code formatting for class/method names (`ClassName`, `methodName()`)
- Use tables where the template uses tables
- Leave `[INSERT UML DIAGRAM IMAGE HERE]` and `[INSERT DISCUSSION SCREENSHOTS HERE]` as placeholders
- Leave `[Full Name]`, `[Matric No.]`, `[Lecture Group]`, `[Program]`, `[Group Number]` as placeholders — do not invent values

### Length Target

Full document should be approximately **2,000–2,800 words** of prose (excluding tables, code blocks, and placeholders).

---

*End of prompt. Begin writing the report draft now.*
