@echo off
echo Banking System Builder and Runner
echo ===============================

REM Create lib directory if it doesn't exist
if not exist lib mkdir lib
cd lib

REM Download Jackson libraries if they don't exist
if not exist jackson-databind-2.14.0.jar (
    echo Downloading Jackson libraries...
    powershell -Command "Invoke-WebRequest -Uri https://repo1.maven.org/maven2/com/fasterxml/jackson/core/jackson-databind/2.14.0/jackson-databind-2.14.0.jar -OutFile jackson-databind-2.14.0.jar"
    powershell -Command "Invoke-WebRequest -Uri https://repo1.maven.org/maven2/com/fasterxml/jackson/core/jackson-core/2.14.0/jackson-core-2.14.0.jar -OutFile jackson-core-2.14.0.jar"
    powershell -Command "Invoke-WebRequest -Uri https://repo1.maven.org/maven2/com/fasterxml/jackson/core/jackson-annotations/2.14.0/jackson-annotations-2.14.0.jar -OutFile jackson-annotations-2.14.0.jar"
    powershell -Command "Invoke-WebRequest -Uri https://repo1.maven.org/maven2/com/fasterxml/jackson/datatype/jackson-datatype-jsr310/2.14.0/jackson-datatype-jsr310-2.14.0.jar -OutFile jackson-datatype-jsr310-2.14.0.jar"
)

cd ..

REM Compile the code
echo Compiling...
cd src\main\java
javac -cp ".;../../../lib/*" com/bankingsystem/BankingSystem.java com/bankingsystem/model/*.java com/bankingsystem/repository/*.java com/bankingsystem/service/*.java com/bankingsystem/util/*.java

if %errorlevel% neq 0 (
    echo Compilation failed!
    cd ..\..\..
    pause
    exit /b %errorlevel%
)

REM Run the application
echo Running Banking System...
java -cp ".;../../../lib/*" com.bankingsystem.BankingSystem

cd ..\..\..
pause