@echo off
echo === Compiling Test Program ===
echo.

set JAVA_HOME=C:\Program Files\Java\jdk-17
set PATH=%JAVA_HOME%\bin;%PATH%

REM Get all OCI SDK jars from Maven repository
set CLASSPATH=.
for /r "%USERPROFILE%\.m2\repository\com\oracle\oci\sdk" %%f in (*.jar) do set CLASSPATH=!CLASSPATH!;%%f
for /r "%USERPROFILE%\.m2\repository\org\glassfish\jersey" %%f in (*.jar) do set CLASSPATH=!CLASSPATH!;%%f
for /r "%USERPROFILE%\.m2\repository\javax\ws\rs" %%f in (*.jar) do set CLASSPATH=!CLASSPATH!;%%f

javac -cp "%CLASSPATH%" TestOCIAI.java

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo Compilation failed!
    pause
    exit /b 1
)

echo.
echo === Running Test Program ===
echo.

java -cp "%CLASSPATH%;." TestOCIAI

echo.
pause
