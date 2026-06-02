# EduSDG - SDG 4 Quality Education Java Application

## Overview
EduSDG is a Java Swing desktop application built around **SDG 4 - Quality Education**.
It has three modules: a Learning Module (12 content screens), a Quiz Module
(22 questions: MCQ + True/False with motivational scoring), and a Gamification
Module (points, badges, timer, leaderboard). Scores are stored in a text file.

## How to Build & Run (command line)
    build.bat        (compiles to out\)
    run.bat          (runs main.Main)

Or manually:

    javac -d out src\main\Main.java src\app\*.java src\interfaces\*.java src\learning\*.java src\quiz\*.java src\gamification\*.java src\data\*.java src\data\exceptions\*.java
    java -cp out main.Main

## Project Structure
    src/main/         Application entry point (Main)
    src/app/          Shared glue: Theme, Screen, AppModule, Navigator, AppContext, HomeScreen
    src/interfaces/   IModule, IQuiz, IGamification, IDataStorage
    src/learning/     LearningModule, ContentPage              (Member 1)
    src/quiz/         QuizModule, Question, MCQQuestion, TrueFalseQuestion (Member 2)
    src/gamification/ GamificationModule, Badge                (Member 3)
    src/data/         DataManager, UserProfile, exceptions/    (Member 4)
    resources/        quiz/questions.txt, learning/content.txt
    data/             scores.txt (created at runtime)

## Team
| Member   | Role                         | Classes                                             | Interface     |
|----------|------------------------------|-----------------------------------------------------|---------------|
| Member 1 | Core Architecture & Learning | LearningModule, ContentPage                         | IModule       |
| Member 2 | Quiz Module                  | QuizModule, Question, MCQQuestion, TrueFalseQuestion | IQuiz        |
| Member 3 | Gamification Module          | GamificationModule, Badge                           | IGamification |
| Member 4 | Data & Exception Handling    | DataManager, UserProfile                            | IDataStorage  |

## Score Scale
    80-100% : Outstanding!
    60-79%  : That's good!
    40-59%  : Good try!
    20-39%  : You can do better!
     0-19%  : Don't give up!

## Requirements
- Java 11 or higher (developed/tested on JDK 25)
- No external libraries required
