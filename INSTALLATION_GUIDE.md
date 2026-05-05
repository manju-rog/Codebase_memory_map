# 📦 RepoLens AI - Complete Installation Guide

## 🎯 Overview
This guide will help you set up RepoLens AI on **any system** (Windows, Mac, Linux) from scratch.

---

## 📋 Prerequisites

### 1. Java Development Kit (JDK) 17 or higher

#### Windows
```bash
# Download from Oracle or Adoptium
# Option 1: Oracle JDK
https://www.oracle.com/java/technologies/downloads/#java17

# Option 2: Eclipse Temurin (Recommended)
https://adoptium.net/temurin/releases/?version=17

# After installation, verify:
java -version
# Should show: java version "17.x.x"
```

#### Mac
```bash
# Using Homebrew
brew install openjdk@17

# Add to PATH
echo 'export PATH="/opt/homebrew/opt/openjdk@17/bin:$PATH"' >> ~/.zshrc
source ~/.zshrc

# Verify
java -version
```

#### Linux (Ubuntu/Debian)
```bash
# Install OpenJDK 17
sudo apt update
sudo apt install openjdk-17-jdk

# Verify
java -version
```

---

### 2. Git

#### Windows
```bash
# Download from
https://git-scm.com/download/win

# Or use winget
winget install Git.Git
```

#### Mac
```bash
# Using Homebrew
brew install git
```

#### Linux
```bash
sudo apt install git
```

---

### 3. Maven (Optional - Project includes Maven Wrapper)

#### Windows
```bash
# Download from
https://maven.apache.org/download.cgi

# Or use Chocolatey
choco install maven
```

#### Mac
```bash
brew install maven
```

#### Linux
```bash
sudo apt install maven
```

**Note**: Maven is optional because the project includes Maven Wrapper (`mvnw` / `mvnw.cmd`)

---

## 🔑 OCI (Oracle Cloud Infrastructure) Setup

### Step 1: Get OCI Account
1. Sign up at: https://www.oracle.com/cloud/free/
2. Navigate to **Identity & Security** → **Compartments**
3. Copy your **Compartment OCID**

### Step 2: Generate API Key
1. Go to **Profile Icon** → **User Settings**
2. Click **API Keys** → **Add API Key**
3. Download the private key (`.pem` file)
4. Copy the configuration shown

### Step 3: Create OCI Config File
Create directory structure:
```bash
mkdir -p OCI_ApiKey
```

Create `OCI_ApiKey/config` file:
```ini
[DEFAULT]
user=ocid1.user.oc1..your_user_ocid
fingerprint=aa:bb:cc:dd:ee:ff:11:22:33:44:55:66:77:88:99:00
tenancy=ocid1.tenancy.oc1..your_tenancy_ocid
region=us-ashburn-1
key_file=./OCI_ApiKey/your_private_key.pem
```

### Step 4: Place Private Key
Copy your downloaded `.pem` file to `OCI_ApiKey/` directory

### Step 5: Update application.yml
Edit `src/main/resources/application.yml`:
```yaml
spring:
  ai:
    oci:
      genai:
        compartment-id: your_compartment_ocid_here
        endpoint: https://inference.generativeai.us-ashburn-1.oci.oraclecloud.com
        chat:
          options:
            model: openai.gpt-4.1
```

---

## 📥 Installation Steps

### Step 1: Clone Repository
```bash
# Clone the repository
git clone https://github.com/YOUR_USERNAME/repolens-ai.git

# Navigate to project directory
cd repolens-ai
```

### Step 2: Set JAVA_HOME (Windows)
```powershell
# PowerShell
$env:JAVA_HOME = "C:\Program Files\Java\jdk-17"

# Or set permanently in System Environment Variables
# System Properties → Environment Variables → New
# Variable: JAVA_HOME
# Value: C:\Program Files\Java\jdk-17
```

### Step 3: Set JAVA_HOME (Mac/Linux)
```bash
# Add to ~/.bashrc or ~/.zshrc
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64  # Linux
export JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk-17.jdk/Contents/Home  # Mac

# Reload
source ~/.bashrc  # or source ~/.zshrc
```

### Step 4: Verify Setup
```bash
# Check Java
java -version

# Check Maven Wrapper
./mvnw --version  # Linux/Mac
./mvnw.cmd --version  # Windows
```

---

## 🚀 Running the Application

### Option 1: Using Quick Start Scripts

#### Windows
```bash
# Double-click or run:
run.bat

# Or in PowerShell:
./run.bat
```

#### Linux/Mac
```bash
# Make executable
chmod +x run.sh

# Run
./run.sh
```

### Option 2: Manual Start

#### Windows (PowerShell)
```powershell
# Set JAVA_HOME
$env:JAVA_HOME = "C:\Program Files\Java\jdk-17"

# Clean and run
./mvnw.cmd clean spring-boot:run
```

#### Linux/Mac
```bash
# Set JAVA_HOME
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64

# Clean and run
./mvnw clean spring-boot:run
```

### Option 3: Build JAR and Run
```bash
# Build
./mvnw clean package

# Run
java -jar target/repolens-1.0.0.jar
```

---

## 🧪 Testing the Application

### Step 1: Wait for Startup
Look for this message in console:
```
Started RepoLensApplication in X.XXX seconds
```

### Step 2: Test in Browser
Open: http://localhost:8080

### Step 3: Test Demo Endpoint
```bash
# Windows (PowerShell)
Invoke-WebRequest -Uri "http://localhost:8080/api/demo/ask?question=Where+is+the+login+logic+implemented" -UseBasicParsing

# Linux/Mac
curl "http://localhost:8080/api/demo/ask?question=Where+is+the+login+logic+implemented"
```

### Step 4: Run Test Script
```bash
# Windows
./test-demo.bat

# Linux/Mac
chmod +x test-demo.sh
./test-demo.sh
```

---

## 📁 Project Structure

```
repolens-ai/
├── OCI_ApiKey/                 # OCI credentials (DO NOT COMMIT)
│   ├── config                  # OCI config file
│   └── *.pem                   # Private key
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/manju/repolens/
│   │   │       ├── config/     # Configuration classes
│   │   │       ├── controller/ # REST controllers
│   │   │       ├── service/    # Business logic
│   │   │       ├── repository/ # Data access
│   │   │       ├── model/      # Domain models
│   │   │       └── dto/        # Data transfer objects
│   │   └── resources/
│   │       ├── application.yml # Configuration
│   │       └── static/         # Frontend files
│   └── test/                   # Test files
├── test-program/               # Demo test program
│   ├── LoginService.java
│   ├── UserRepository.java
│   └── ...
├── sample-repos/               # Sample repositories
│   └── mini-bank-api/
├── pom.xml                     # Maven configuration
├── run.bat                     # Windows start script
├── run.sh                      # Linux/Mac start script
├── test-demo.bat               # Windows test script
├── test-demo.sh                # Linux/Mac test script
└── README.md                   # Project documentation
```

---

## 🔧 Configuration Files

### application.yml
Located at: `src/main/resources/application.yml`

Key configurations:
```yaml
server:
  port: 8080                    # Change port if needed

spring:
  ai:
    oci:
      genai:
        compartment-id: YOUR_COMPARTMENT_ID
        endpoint: YOUR_ENDPOINT
        chat:
          options:
            model: openai.gpt-4.1
            temperature: 0.3
            max-tokens: 2000

app:
  ai:
    enabled: true               # Enable/disable AI features
  oci:
    config-file: ./OCI_ApiKey/config
```

---

## 🐛 Troubleshooting

### Issue 1: "JAVA_HOME not found"
**Solution**:
```bash
# Windows
$env:JAVA_HOME = "C:\Program Files\Java\jdk-17"

# Linux/Mac
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
```

### Issue 2: "Port 8080 already in use"
**Solution**:
```bash
# Windows - Find and kill process
netstat -ano | findstr :8080
taskkill /PID <PID> /F

# Linux/Mac
lsof -ti:8080 | xargs kill -9

# Or change port in application.yml
server:
  port: 8081
```

### Issue 3: "OCI Authentication Failed"
**Solution**:
1. Verify `OCI_ApiKey/config` file exists
2. Check private key path is correct
3. Verify compartment ID in `application.yml`
4. Ensure API key is added in OCI Console

### Issue 4: "Maven build failed"
**Solution**:
```bash
# Clean Maven cache
./mvnw clean

# Delete target directory
rm -rf target  # Linux/Mac
rmdir /s target  # Windows

# Rebuild
./mvnw clean install
```

### Issue 5: "Cannot connect to H2 database"
**Solution**:
H2 is in-memory, no setup needed. If issues persist:
```yaml
# In application.yml, try:
spring:
  datasource:
    url: jdbc:h2:mem:repolens;DB_CLOSE_DELAY=-1
```

---

## 🔒 Security Notes

### DO NOT COMMIT:
- `OCI_ApiKey/` directory (already in .gitignore)
- Private keys (*.pem files)
- Config files with credentials
- `application.yml` with real credentials

### Before Pushing to GitHub:
1. Check `.gitignore` includes:
   ```
   OCI_ApiKey/
   *.pem
   application-local.yml
   ```

2. Use environment variables for sensitive data:
   ```yaml
   spring:
     ai:
       oci:
         genai:
           compartment-id: ${OCI_COMPARTMENT_ID}
   ```

3. Create `application-example.yml` with placeholders

---

## 📊 System Requirements

### Minimum
- **CPU**: 2 cores
- **RAM**: 4 GB
- **Disk**: 500 MB
- **Java**: 17+
- **OS**: Windows 10+, macOS 10.14+, Linux (Ubuntu 20.04+)

### Recommended
- **CPU**: 4 cores
- **RAM**: 8 GB
- **Disk**: 1 GB
- **Java**: 17+
- **Internet**: Required for OCI API calls

---

## 🎯 Quick Start Checklist

- [ ] Install Java 17+
- [ ] Install Git
- [ ] Clone repository
- [ ] Set up OCI account
- [ ] Generate OCI API key
- [ ] Create OCI_ApiKey/config file
- [ ] Place private key in OCI_ApiKey/
- [ ] Update application.yml with compartment ID
- [ ] Set JAVA_HOME environment variable
- [ ] Run `./run.bat` (Windows) or `./run.sh` (Linux/Mac)
- [ ] Open http://localhost:8080
- [ ] Test with demo question

---

## 📞 Support

### Documentation
- [README.md](README.md) - Project overview
- [SETUP.md](SETUP.md) - Detailed setup guide
- [DEMO_SUCCESS.md](DEMO_SUCCESS.md) - Demo test results
- [WORKING_DEMO_SUMMARY.md](WORKING_DEMO_SUMMARY.md) - Feature summary

### Common Commands
```bash
# Start application
./run.bat  # Windows
./run.sh   # Linux/Mac

# Test demo
./test-demo.bat  # Windows
./test-demo.sh   # Linux/Mac

# Build only
./mvnw clean compile

# Run tests
./mvnw test

# Package JAR
./mvnw clean package
```

---

## 🎉 Success!

If you see this in your browser at http://localhost:8080, you're ready to go! 🚀

Try asking:
- "Where is the login logic implemented?"
- "How does authentication work?"
- "What classes are involved in login?"

---

**Last Updated**: May 5, 2026
**Version**: 1.0.0
**Status**: Production Ready ✅
