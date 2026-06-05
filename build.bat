@echo off
setlocal enabledelayedexpansion
if not exist out  mkdir out
if not exist data mkdir data

REM Collect all .java files under src\ for compilation.
set FILES=
for /r src %%f in (*.java) do set FILES=!FILES! "%%f"

javac -d out !FILES!
if not %ERRORLEVEL%==0 (
    echo Build FAILED. Fix the errors above, then re-run build.bat
    exit /b 1
)

REM Package the compiled classes into an executable JAR.
jar cfe EduSDG.jar main.Main -C out .
if %ERRORLEVEL%==0 (
    echo Build successful.
    echo   Run the JAR:    java -jar EduSDG.jar
    echo   Or run classes: java -cp out main.Main
) else (
    echo JAR packaging failed, but classes compiled. Run with: java -cp out main.Main
)
