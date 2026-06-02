@echo off
setlocal enabledelayedexpansion
if not exist out  mkdir out
if not exist data mkdir data

REM Compile all .java files under src\ that do NOT have "(suggestion)" in the name.
REM The wildcard *.java would also match the reference (suggestion) files, causing
REM duplicate-class errors, so we filter them out with the loop below.
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
