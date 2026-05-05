@echo off
echo ========================================
echo RepoLens AI - New System Setup
echo ========================================
echo.

echo This script will help you set up RepoLens AI on a new system
echo.

REM Check Java
echo [1/5] Checking Java installation...
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ❌ Java is NOT installed
    echo.
    echo Please install Java 17 or higher from:
    echo https://adoptium.net/temurin/releases/?version=17
    echo.
    pause
    exit /b 1
) else (
    echo ✅ Java is installed
    java -version
)
echo.

REM Check JAVA_HOME
echo [2/5] Checking JAVA_HOME...
if "%JAVA_HOME%"=="" (
    echo ⚠️  JAVA_HOME is not set
    echo.
    echo Please set JAVA_HOME:
    echo 1. Open System Properties ^> Environment Variables
    echo 2. Add new System Variable:
    echo    Variable: JAVA_HOME
    echo    Value: C:\Program Files\Java\jdk-17
    echo.
    echo Or run this in PowerShell:
    echo $env:JAVA_HOME = "C:\Program Files\Java\jdk-17"
    echo.
) else (
    echo ✅ JAVA_HOME is set to: %JAVA_HOME%
)
echo.

REM Check Git
echo [3/5] Checking Git installation...
git --version >nul 2>&1
if %errorlevel% neq 0 (
    echo ❌ Git is NOT installed
    echo.
    echo Please install Git from:
    echo https://git-scm.com/download/win
    echo.
) else (
    echo ✅ Git is installed
    git --version
)
echo.

REM Check OCI Config
echo [4/5] Checking OCI configuration...
if exist "OCI_ApiKey\config" (
    echo ✅ OCI config file found
) else (
    echo ⚠️  OCI config file NOT found
    echo.
    echo Please create OCI_ApiKey\config file:
    echo 1. Copy OCI_ApiKey\config.example to OCI_ApiKey\config
    echo 2. Update with your OCI credentials
    echo 3. Place your private key (.pem) in OCI_ApiKey\ directory
    echo.
    echo See INSTALLATION_GUIDE.md for detailed instructions
    echo.
)
echo.

REM Check application.yml
echo [5/5] Checking application configuration...
if exist "src\main\resources\application.yml" (
    echo ✅ application.yml found
    echo.
    echo ⚠️  Please verify your compartment ID in application.yml
    echo    Location: src\main\resources\application.yml
    echo    Look for: spring.ai.oci.genai.compartment-id
    echo.
) else (
    echo ⚠️  application.yml NOT found
    echo.
    echo Please copy application-example.yml to application.yml:
    echo copy src\main\resources\application-example.yml src\main\resources\application.yml
    echo.
    echo Then update with your OCI compartment ID
    echo.
)
echo.

echo ========================================
echo Setup Check Complete!
echo ========================================
echo.

echo Next Steps:
echo 1. If any checks failed, follow the instructions above
echo 2. Set up OCI credentials (see INSTALLATION_GUIDE.md)
echo 3. Run the application: run.bat
echo 4. Test the demo: test-demo.bat
echo 5. Open browser: http://localhost:8080
echo.

echo Documentation:
echo - INSTALLATION_GUIDE.md - Complete setup instructions
echo - README.md - Project overview
echo - QUICK_START.md - Quick start guide
echo.

pause
