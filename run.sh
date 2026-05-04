#!/bin/bash

echo "========================================"
echo "RepoLens AI - Starting Application"
echo "========================================"
echo ""

# Check if Java is installed
if ! command -v java &> /dev/null; then
    echo "ERROR: Java is not installed or not in PATH"
    echo "Please install Java 21 from https://adoptium.net/"
    exit 1
fi

echo "Java found!"
echo ""

# Check if Maven is installed
if ! command -v mvn &> /dev/null; then
    echo "Maven not found, using Maven Wrapper..."
    echo ""
    ./mvnw clean spring-boot:run
else
    echo "Maven found!"
    echo ""
    mvn clean spring-boot:run
fi
