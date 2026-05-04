# EduSDG – SDG Quality Education Java Application

## Overview
EduSDG is a Java desktop application built around SDG 4 – Quality Education.
It delivers an interactive learning experience through three integrated modules:
a Learning Module with 10+ educational screens, a Quiz Module with 20+ questions
and motivational scoring, and a Gamification Module with badges, points, and a leaderboard.

## How to Run
Compile:
  javac -d out src/**/*.java

Run:
  java -cp out main.Main

Or via JAR:
  java -jar EduSDG.jar

## Team
| Member   | Role                         | Classes                       | Interface     |
|----------|------------------------------|-------------------------------|---------------|
| Member 1 | Core Architecture & Learning | LearningModule, ContentPage   | IModule       |
| Member 2 | Quiz Module                  | QuizModule, Question          | IQuiz         |
| Member 3 | Gamification Module          | GamificationModule, Badge     | IGamification |
| Member 4 | Data & Exception Handling    | DataManager, UserProfile      | IDataStorage  |

## Score Scale
  80–100% : Outstanding!
  60–79%  : That's good!
  40–59%  : Good try!
  20–39%  : You can do better!
   0–19%  : Don't give up!

## Requirements
- Java 11 or higher
- No external libraries required
- Run from command line as shown above