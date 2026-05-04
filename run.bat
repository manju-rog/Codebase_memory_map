@echo off
echo ========================================
echo RepoLens AI - Starting Application
echo ========================================
echo.

REM Check if Java is installed
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ERROR: Java is not installed or not in PATH
    echo Please install Java 21 from https://adoptium.net/
    pause
    exit /b 1
)

echo Java found!
echo.

REM Check if Maven is installed
where mvn >nul 2>&1
if %errorlevel% neq 0 (
    echo Maven not found, using Maven Wrapper...
    echo.
    call mvnw.cmd clean spring-boot:run
) else (
    echo Maven found!
    echo.
    call mvn clean spring-boot:run
)

pause
