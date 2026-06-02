@echo off
REM SUGGESTION (AI-generated). Rename to build.bat after merging suggestion files.
REM Compiles all sources to out\
javac -d out ^
  src\main\Main.java ^
  src\app\*.java ^
  src\interfaces\*.java ^
  src\learning\*.java ^
  src\quiz\*.java ^
  src\gamification\*.java ^
  src\data\*.java ^
  src\data\exceptions\*.java
echo Build complete. Run with run.bat
