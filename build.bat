@echo off
setlocal enabledelayedexpansion
if not exist out  mkdir out
if not exist data mkdir data

REM Collect all .java files under src\ for compilation.
set FILES=
for /r src %%f in (*.java) do (
    set FNAME=%%f
    if "!FNAME:(suggestion)=!"=="!FNAME!" set FILES=!FILES! "%%f"
)

javac -d out !FILES!
if %ERRORLEVEL%==0 (
    echo Build successful. Run with:  run.bat
) else (
    echo Build FAILED. Fix the errors above, then re-run build.bat
)
