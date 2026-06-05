# EduSDG – SDG 4 Quality Education

A Java Swing desktop application for **SDG 4 – Quality Education**, built for TMF2954 Java Programming.

The app has four tabs:
- **Learn** – 10 educational pages about SDG 4 with images and Prev/Next navigation
- **Quiz** – 30 questions (multiple-choice + true/false) with a 300-second countdown timer
- **Leaderboard** – points and badges earned from quizzes
- **Profile** – score history read from a text file

---

## Requirements

- **Java 11 or higher** (JDK, not just JRE). Check with:
  ```
  java -version
  ```
- No external libraries needed.

---

## How to Download

1. On the GitHub page, click the green **`Code`** button → **Download ZIP**.
2. Extract the ZIP to any folder.
3. Open a terminal **inside the extracted folder** (the folder that contains `EduSDG.jar`, `src/`, and `resources/`).

> Or clone it: `git clone https://github.com/JustinJ03/Java-Project.git`

---

## How to Run

> ⚠️ **Important:** Always run from the project root folder (the one containing `EduSDG.jar` and the `resources/` folder). The program loads images and saves scores using paths relative to this folder.

### Option A — Run the ready-made JAR (easiest, no compiling)

```
java -jar EduSDG.jar
```

### Option B — Compile from source, then run

**Windows (using the provided script):**
```
build.bat
java -jar EduSDG.jar
```

**Any OS (manual compile):**
```
javac -d out src/main/*.java src/interfaces/*.java src/learning/*.java src/quiz/*.java src/gamification/*.java src/data/*.java src/data/exceptions/*.java
java -cp out main.Main
```

---

## Project Structure

```
EduSDG.jar              Pre-built executable (run with: java -jar EduSDG.jar)
build.bat               Compiles src/ and rebuilds EduSDG.jar (Windows)
src/
  main/                 Main.java — entry point, JFrame with 4-tab bar
  interfaces/           IQuiz, IGamification, IDataStorage
  learning/             IModule, LearningModule, ContentPage, LearningModulePanel
  quiz/                 QuizModule, Question, MCQQuestion, TrueFalseQuestion
  gamification/         GamificationModule, Badge
  data/                 DataManager, UserProfile, ProfilePanel, exceptions/
resources/
  learning/Images/      Lesson images (10 PNG files)
  quiz/questions.txt    Quiz question bank
data/
  scores.txt            Score history (CSV: name,score,timestamp) — written at runtime
```

---

## Team

| Member   | Role                       | Interface       | Main Classes                                          |
|----------|----------------------------|-----------------|-------------------------------------------------------|
| Member 1 | Core & Learning Module     | IModule         | LearningModule, ContentPage, LearningModulePanel       |
| Member 2 | Quiz Module                | IQuiz           | QuizModule, Question, MCQQuestion, TrueFalseQuestion  |
| Member 3 | Gamification Module        | IGamification   | GamificationModule, Badge                             |
| Member 4 | Data & Exception Handling  | IDataStorage    | DataManager, UserProfile, ProfilePanel, 3 exceptions  |

---

## Score Feedback Scale

```
80 – 100% : Outstanding!
60 –  79% : That's good!
40 –  59% : Good try!
20 –  39% : You can do better!
 0 –  19% : Don't give up!
```

---

## OOP Concepts Demonstrated

- **Abstraction** – 4 interfaces + abstract `Question` class
- **Inheritance** – `MCQQuestion`/`TrueFalseQuestion` extend `Question`; custom exceptions extend `Exception`
- **Polymorphism** – `QuizModule` treats every question as a `Question`; modules use `IDataStorage`
- **Method overloading** – `DataManager.saveScore(...)`, `GamificationModule.addPoints(...)`
- **Constructor overloading** – `ContentPage` (3), `Badge` (2)
- **Custom exceptions** – `ContentLoadException`, `InvalidScoreException`, `DataNotFoundException`
