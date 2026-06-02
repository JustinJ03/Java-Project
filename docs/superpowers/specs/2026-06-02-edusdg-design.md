# EduSDG — SDG 4 Quality Education Desktop App — Design Spec

**Course:** TMF2954 Java Programming, Semester 2 S2025/26
**Deliverable deadline:** Friday, 5 June 2026, 5:00 pm (eLEAP)
**Date authored:** 2026-06-02
**Theme:** SDG 4 — Quality Education

---

## 1. Purpose & Scope

Build an Object-Oriented Java **desktop** application about **SDG 4 (Quality Education)** with:
- A **Learning Module** (≥10 content screens, text; images deferred for now),
- A **Quiz Module** (≥20 questions, ≥2 question types, scoring + motivational message),
- **Gamification** (badges, points, timer, leaderboard),
- **Score storage** with no hardcoding,
- A **Swing GUI** sized to a smartphone resolution but run on desktop,
- **Command-line execution**.

**Build mode chosen: "AI suggestion files alongside members' own work."**
Some members have already started coding. To avoid overwriting their source, every
AI-generated file is delivered as a **suggestion reference** with `(suggestion)` in its
filename (e.g. `LearningModule (suggestion).java`), placed in the same package folder
**next to** the member's real file. A member opens the suggestion, then copies/merges the
parts they want into their own properly-named file. The code inside uses the correct class
names and is internally consistent, so dropping the ` (suggestion)` suffix yields a file
that compiles. Each member still codes/finalizes their own class and interface.

**Trade-off accepted:** because a file named `Foo (suggestion).java` cannot hold
`public class Foo`, the delivered set does **not** compile or launch as-is. It is a set of
reference snippets; a running build is produced only after suffixes are stripped and files
are placed/merged by the team (see §6).

### Decisions locked during brainstorming
| Decision | Choice |
|---|---|
| Deliverable scope | Scaffold + per-member stubs (infra fully working, modules stubbed) |
| SDG topic | SDG 4 — Quality Education |
| GUI toolkit | Java Swing (bundled, no setup) |
| Score storage | `scores.txt` text file **now**, behind `IDataStorage` so a DB can be swapped later |
| Media (images/audio/video) | None for now; stored as separate files referenced by name when added |
| Content authoring | I (assistant) author all 20+ quiz questions and 10+ learning screens (text) |
| Non-code deliverables | Plan covers code **+ UML diagram**; PDF/cover/screenshots assembly left to team |
| Java version | Target Java 11+ syntax; JDK 25 is installed and runs it |
| File marking | Every generated file carries `(suggestion)` in its filename; never overwrites members' files |
| AI-artifact handling | `.gitignore` excludes AI traces + sensitive info; AI artifacts kept removable in one move |

### Out of scope (YAGNI)
- Real images, audio, video (architecture leaves the door open; none included now).
- Java DB / Derby setup (interface is DB-ready; not implemented now).
- Networking, login/passwords, multi-language, installers.

---

## 2. Requirements Traceability (project brief → design)

| Brief requirement | Where satisfied |
|---|---|
| ≥4 classes + ≥4 interfaces | 4 interfaces + ≥8 classes (see §4) |
| Each member ≥1 class AND ≥1 interface | Ownership table §4 |
| Learning: ≥10 screens text(+image) | `LearningModule` loads ≥10 entries from `resources/learning/` |
| Quiz: ≥20 questions, ≥2 types | `resources/quiz/questions.txt`: MCQ + True/False, ≥20 total |
| Score + motivational message scale | `QuizModule` maps % → message (0–19 … 80–100) |
| Gamification: badges/points/timer/leaderboard | `GamificationModule`, `Badge`, leaderboard screen |
| Data storage, no hardcoding | `DataManager` reads/writes `scores.txt` |
| Abstraction | abstract `Question`, `AppModule`, `Screen`; 4 interfaces |
| Inheritance | `Question`→subtypes; `AppModule`→modules; `Screen`→panels |
| Polymorphism | modules handled as `IModule`; questions iterated as `Question` |
| Overloading | `saveScore(...)`, `addPoints(...)`, `Badge` constructors |
| Overriding | `checkAnswer()`, `render()`, `getView()` |
| Custom exceptions + try-catch | `DataNotFoundException`, `InvalidScoreException`, `ContentLoadException` |
| GUI at smartphone resolution | `Theme` fixed content size ≈ 360×720, non-resizable |
| Command-line execution | `build.bat` / `run.bat`; `java -cp out main.Main` |

---

## 3. Architecture

Two tiers:

**Tier 1 — Shared glue (fully implemented; not a graded member class):**
- `main.Main` — entry point; builds the `JFrame`, `AppContext`, registers modules, shows home.
- `app.AppContext` — holds shared services (the `IDataStorage` instance) + module registry.
- `app.Navigator` — `CardLayout`-based screen switching (`show(screenName)`).
- `app.Screen` — abstract `JPanel` base with phone-styled layout helpers.
- `app.AppModule` — abstract base implementing `IModule`; gives each module a name + root view.
- `app.Theme` — colors, fonts, the smartphone dimensions (≈360×720), shared constants.
- `app.HomeScreen` — menu with buttons to Learning, Quiz, Gamification/Leaderboard.

**Tier 2 — Member-owned classes + interfaces (compile-ready stubs):** see §4.

### Data flow
1. `Main` creates `AppContext` (which creates a `DataManager` as `IDataStorage`).
2. Modules are constructed with `AppContext` and registered with `Navigator`.
3. User navigates Home → module via `Navigator`.
4. Quiz completion → `QuizModule` computes score → calls `DataManager.saveScore(...)` →
   fires gamification hooks (award badge/points) → leaderboard reads back via `DataManager`.

### Directory / file layout
Every generated file carries ` (suggestion)` before its extension and sits beside any file a
member may already have created (different filename = no overwrite). Class/interface names
**inside** the files are the correct final names (e.g. `public class LearningModule`).
```
Java-Project/
├── src/
│   ├── main/Main (suggestion).java
│   ├── app/AppContext (suggestion).java  Navigator (suggestion).java  Screen (suggestion).java
│   │        AppModule (suggestion).java  Theme (suggestion).java  HomeScreen (suggestion).java
│   ├── interfaces/IModule (suggestion).java  IQuiz (suggestion).java
│   │              IGamification (suggestion).java  IDataStorage (suggestion).java
│   ├── learning/LearningModule (suggestion).java  ContentPage (suggestion).java
│   ├── quiz/QuizModule (suggestion).java  Question (suggestion).java
│   │        MCQQuestion (suggestion).java  TrueFalseQuestion (suggestion).java
│   ├── gamification/GamificationModule (suggestion).java  Badge (suggestion).java
│   └── data/DataManager (suggestion).java  UserProfile (suggestion).java
│       └── exceptions/DataNotFoundException (suggestion).java
│                      InvalidScoreException (suggestion).java  ContentLoadException (suggestion).java
├── resources/
│   ├── learning/content (suggestion).txt   (≥10 screens of SDG 4 text)
│   └── quiz/questions (suggestion).txt      (≥20 questions: MCQ + True/False)
├── docs/uml (suggestion).puml  uml (suggestion).md   (class diagram for the Design PDF)
├── build (suggestion).bat  run (suggestion).bat
├── README (suggestion).md           (existing README.md left untouched)
├── .gitignore                       (excludes AI traces, secrets, build output, data/)
└── data/                            (scores.txt created at runtime; gitignored)
```
> Each generated `.java` file opens with a header comment:
> `// SUGGESTION (AI-generated reference). Rename to <Name>.java and merge into your own work.
>  Do not overwrite code you have already written.`

---

## 4. Member Ownership & Class/Interface Contracts

| Member | Module | Classes owned | Interface owned |
|---|---|---|---|
| 1 | Core / Learning | `LearningModule`, `ContentPage` | `IModule` |
| 2 | Quiz | `QuizModule`, `Question`, `MCQQuestion`, `TrueFalseQuestion` | `IQuiz` |
| 3 | Gamification | `GamificationModule`, `Badge` | `IGamification` |
| 4 | Data / Exceptions | `DataManager`, `UserProfile`, custom exceptions | `IDataStorage` |

> Interfaces are pre-defined as stable contracts so everyone codes against the same API.
> Each member reviews, finalizes, and documents (Javadoc) their own interface — that is their
> interface authorship.

### Interface contracts (signatures the scaffold ships)
- **`IModule`** — `String getName();` `JComponent getView();` `void onShow();`
- **`IQuiz`** — `List<Question> loadQuestions();` `int score();` `String motivationalMessage(int percent);`
- **`IGamification`** — `void addPoints(int p);` `List<Badge> awardedBadges();` `List<String> leaderboard();`
- **`IDataStorage`** — `void saveScore(String user, int score) throws ...;` `List<String[]> readScores() throws ...;`

### Key OOP demonstrations (concrete)
- **Abstract class `Question`** with `abstract boolean checkAnswer(String response);` and
  `abstract JComponent render();`, extended by `MCQQuestion` and `TrueFalseQuestion` (override both).
- **Abstract `AppModule implements IModule`**, extended by the three module classes.
- **Overloading:** `DataManager.saveScore(String,int)` and `saveScore(UserProfile)`;
  `GamificationModule.addPoints(int)` and `addPoints(int,String reason)`; two `Badge` constructors.
- **Polymorphism:** `Navigator` stores `Map<String,IModule>`; quiz loop iterates `List<Question>`.

---

## 5. Module Behaviour (target once stubs are filled)

- **Learning:** loads ≥10 entries from `resources/learning/content.txt`; Prev/Next navigation;
  `ContentPage` holds title + body (optional image path, unused for now).
- **Quiz:** loads ≥20 questions from `resources/quiz/questions.txt`; presents one at a time;
  computes % score; displays the exact motivational message:
  - 80–100% Outstanding! · 60–79% That's good! · 40–59% Good try! · 20–39% You can do better! · 0–19% Don't give up!
  - persists score via `IDataStorage`; triggers gamification.
- **Gamification:** points per correct answer; badges at thresholds; a per-quiz timer;
  leaderboard screen reading `scores.txt` (sorted desc).
- **Data:** `scores.txt` line format `name,score,timestamp`; all I/O wrapped in try-catch;
  throws the custom exceptions on failure; nothing hardcoded.

---

## 6. Build & Run (command-line, as required)

Because the delivered files carry the ` (suggestion)` suffix, they do **not** compile as-is.
To produce a running app the team **merges suggestions into their own files** (drop the
suffix, keep the proper name, e.g. `LearningModule (suggestion).java` → merged into
`LearningModule.java`). Once the proper-named files exist in `src/`, the provided scripts work.

`build (suggestion).bat` (rename to `build.bat` after merging):
```
javac -d out src\main\Main.java src\app\*.java src\interfaces\*.java src\learning\*.java src\quiz\*.java src\gamification\*.java src\data\*.java src\data\exceptions\*.java
```
`run (suggestion).bat` (rename to `run.bat`):
```
java -cp out main.Main
```
Resources are read relative to the working directory (`resources/...`, `data/...`).

---

## 7. UML Deliverable

`docs/uml.puml` (PlantUML) + `docs/uml.md` (Mermaid class diagram) showing all classes, the
4 interfaces, inheritance arrows, associations, and the polymorphic relationships — ready to
drop into the Project Design PDF. PDF/cover-page/screenshots assembly remains the team's task.

---

## 8. Acceptance Criteria

1. Every generated file carries ` (suggestion)` in its filename; no existing member file is overwritten.
2. Each generated `.java` file has the SUGGESTION header comment and uses the correct final class name inside.
3. With suffixes stripped and files placed in `src/`, the set compiles cleanly on JDK 25 and
   `run.bat` launches a phone-sized window showing the Home screen with working navigation to each module.
4. `resources/quiz/questions (suggestion).txt` contains ≥20 questions across MCQ + True/False.
5. `resources/learning/content (suggestion).txt` contains ≥10 SDG 4 content screens.
6. Code organizes into ≥4 interfaces + ≥8 classes matching the ownership table.
7. Every member-owned method body that is intentionally unfinished carries a `// TODO(Member N)`.
8. UML files exist under `docs/` (suffixed).
9. `.gitignore` excludes AI traces, secrets, build output, and `data/`; the existing `README.md` is untouched.

---

## 9. `.gitignore` & AI-Artifact / Sensitive-Info Handling

Goal: when the repo is shared with teammates, no AI-usage trace or sensitive info leaks.

`.gitignore` will include:
```
# --- AI usage traces (do not share) ---
.claude/
docs/superpowers/
CLAUDE.md
AGENTS.md
memory/
*.ai.md

# --- Sensitive info ---
.env
*.env
*.key
*.pem
secrets*
credentials*

# --- Build & runtime output ---
out/
*.class
data/            # runtime scores; not committed
derby.log
*.log

# --- IDE / OS ---
.idea/
.vscode/
nbproject/private/
*.iml
.DS_Store
Thumbs.db
```

**Caveat (documented for the user):** `.gitignore` only affects git tracking. If teammates
receive a **ZIP of the folder** rather than a git clone, ignored files are still physically
present. Mitigation: all AI artifacts live under `docs/superpowers/` (and `.claude/`), so they
can be deleted in a single move before zipping. The `(suggestion)`-suffixed source files are
*intended* to be shared with the team (they are the suggestions), so they are **not** ignored.
