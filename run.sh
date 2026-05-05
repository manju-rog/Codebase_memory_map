#!/bin/bash

echo "========================================"
echo "RepoLens AI - Starting Application"
echo "========================================"
echo ""

# Check if Java is installed
if ! command -v java &> /dev/null; then
    echo "ERROR: Java is not installed or not in PATH"
    echo "Please install Java 17+ from https://adoptium.net/"
    exit 1
fi

echo "Java found!"
java -version
echo ""

# Check if JAVA_HOME is set
if [ -z "$JAVA_HOME" ]; then
    echo "WARNING: JAVA_HOME is not set"
    echo "Attempting to find Java installation..."
    
    # Try to find Java on common paths
    if [ -d "/usr/lib/jvm/java-17-openjdk-amd64" ]; then
        export JAVA_HOME="/usr/lib/jvm/java-17-openjdk-amd64"
    elif [ -d "/Library/Java/JavaVirtualMachines/jdk-17.jdk/Contents/Home" ]; then
        export JAVA_HOME="/Library/Java/JavaVirtualMachines/jdk-17.jdk/Contents/Home"
    else
        echo "Please set JAVA_HOME manually:"
        echo "export JAVA_HOME=/path/to/jdk-17"
    fi
    
    if [ -n "$JAVA_HOME" ]; then
        echo "JAVA_HOME set to: $JAVA_HOME"
    fi
fi

echo ""
echo "Starting RepoLens AI..."
echo ""

# Check if Maven is installed
if command -v mvn &> /dev/null; then
    echo "Maven found! Using system Maven..."
    mvn clean spring-boot:run
else
    echo "Maven not found, using Maven Wrapper..."
    chmod +x mvnw
    ./mvnw clean spring-boot:run
fi
