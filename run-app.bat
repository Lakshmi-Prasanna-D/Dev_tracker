@echo off
echo ==============================================================================
echo DevTrack - Complete Developer Growth Platform
echo Starting local application on port 8085...
echo ==============================================================================

if defined JAVA_HOME (
    set "JAVA_EXE=%JAVA_HOME%\bin\java.exe"
) else (
    set "JAVA_EXE=java"
)

mvn spring-boot:run
pause
