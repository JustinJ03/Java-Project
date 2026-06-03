# EduSDG – SDG 4 Quality Education Java Application

## Overview
EduSDG is a Java Swing desktop application centred on **SDG 4 – Quality Education**.
It provides three integrated modules: a Learning Module with 10+ educational screens,
a Quiz Module with 20+ questions and motivational scoring, and a Gamification Module
with badges, points, and a leaderboard. Scores are stored in a text file with no hardcoding.

## How to Build and Run

**Compile:**
```
build.bat
```

**Run:**
```
run.bat
```

Or manually from the project root:
```
javac -d out src\main\Main.java src\interfaces\*.java src\learning\*.java src\quiz\*.java src\gamification\*.java src\data\*.java src\data\exceptions\*.java
java -cp out main.Main
```

> Always run from the project root directory (the folder containing `src/` and `resources/`).

## Project Structure
```
src/
  main/         Main.java (entry point — JFrame shell with bottom tab bar)
  interfaces/   IQuiz, IGamification, IDataStorage
  learning/     IModule, LearningModule, ContentPage,
                LearningModulePanel                      (Member 1 — COMPLETE)
  quiz/         QuizModule, Question, MCQQuestion,
                TrueFalseQuestion                        (Member 2 — TODO)
  gamification/ GamificationModule, Badge                (Member 3 — COMPLETE)
  data/         DataManager, UserProfile, exceptions/    (Member 4 — COMPLETE)
resources/
  learning/     Images/ (placeholder images)
  quiz/questions.txt   Quiz question bank (MCQ + True/False)
tests/
  TestSuite.java        Automated structural and logic tests
  DebugHelper.java      Environment diagnostics tool
  ManualTests.java      Basic logic checks
data/
  scores.txt            Created at runtime when a quiz is completed
```

## Team

| Member   | Role                         | Classes                                               | Interface       |
|----------|------------------------------|-------------------------------------------------------|-----------------|
| Member 1 | Learning Module              | LearningModule, ContentPage, LearningModulePanel       | IModule         |
| Member 2 | Quiz Module                  | QuizModule, Question, MCQQuestion, TrueFalseQuestion  | IQuiz           |
| Member 3 | Gamification Module          | GamificationModule, Badge                             | IGamification   |
| Member 4 | Data & Exception Handling    | DataManager, UserProfile                              | IDataStorage    |

## Score Scale
```
80 – 100% : Outstanding!
60 –  79% : That's good!
40 –  59% : Good try!
20 –  39% : You can do better!
 0 –  19% : Don't give up!
```

## Running the Tests
After compiling, run the test suite to check your implementation:
```
javac -cp out -d out tests\TestSuite.java tests\DebugHelper.java tests\ManualTests.java
java -cp out tests.TestSuite
java -cp out tests.DebugHelper
```
`[STRUCTURAL]` tests pass immediately. `[LOGIC]` tests turn green as each member implements their TODOs.

## Requirements
- Java 11 or higher (tested on JDK 25)
- No external libraries required
