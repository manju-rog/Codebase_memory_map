# RepoLens AI - Setup Guide

## Prerequisites

You need to install the following before running the project:

### 1. Install Java 21

**Windows:**
- Download from: https://adoptium.net/temurin/releases/?version=21
- Install and verify:
```bash
java -version
```

### 2. Install Maven

**Windows:**
- Download from: https://maven.apache.org/download.cgi
- Extract to `C:\Program Files\Apache\maven`
- Add to PATH: `C:\Program Files\Apache\maven\bin`
- Verify:
```bash
mvn -version
```

**Alternative - Use Maven Wrapper (Recommended):**

If you don't want to install Maven globally, you can use the Maven Wrapper included in this project:

```bash
# Windows
.\mvnw.cmd clean install

# Linux/Mac
./mvnw clean install
```

## Quick Start

### Step 1: Build the Project

```bash
mvn clean install
```

Or with Maven Wrapper:
```bash
.\mvnw.cmd clean install
```

### Step 2: Run the Application

```bash
mvn spring-boot:run
```

Or with Maven Wrapper:
```bash
.\mvnw.cmd spring-boot:run
```

### Step 3: Access the Application

- Dashboard: http://localhost:8080/
- Swagger UI: http://localhost:8080/swagger-ui.html
- H2 Console: http://localhost:8080/h2-console

## Environment Variables (Optional)

### For AI Features with OCI GenAI

Create a `.env` file or set environment variables:

```bash
# Windows PowerShell
$env:APP_AI_ENABLED="true"
$env:OCI_COMPARTMENT_ID="ocid1.compartment.oc1..aaaaaaaacqxr3j2qbhntidg2s32gb67lv7ydpbfkvcazqvnjfsaxj3vdxr7a"
$env:OCI_CONFIG_PROFILE="DEFAULT"
$env:OCI_ENDPOINT="https://inference.generativeai.us-ashburn-1.oci.oraclecloud.com"
$env:OCI_LLAMA4_MODEL_ID="ocid1.generativeaimodel.oc1.iad.amaaaaaask7dceyah6tjdejjashngznsylutuhhvufukzb2g2ls54g2flsfq"
$env:OCI_GPT5_MODEL_ID="openai.gpt-4.1"

# Linux/Mac
export APP_AI_ENABLED=true
export OCI_COMPARTMENT_ID="ocid1.compartment.oc1..aaaaaaaacqxr3j2qbhntidg2s32gb67lv7ydpbfkvcazqvnjfsaxj3vdxr7a"
export OCI_CONFIG_PROFILE="DEFAULT"
export OCI_ENDPOINT="https://inference.generativeai.us-ashburn-1.oci.oraclecloud.com"
export OCI_LLAMA4_MODEL_ID="ocid1.generativeaimodel.oc1.iad.amaaaaaask7dceyah6tjdejjashngznsylutuhhvufukzb2g2ls54g2flsfq"
export OCI_GPT5_MODEL_ID="openai.gpt-4.1"
```

### For Private GitHub Repositories

```bash
# Windows PowerShell
$env:GITHUB_TOKEN="your_github_token"

# Linux/Mac
export GITHUB_TOKEN="your_github_token"
```

## Running Tests

```bash
mvn test
```

Or with Maven Wrapper:
```bash
.\mvnw.cmd test
```

## Demo Script

### 1. Start the Application

```bash
mvn spring-boot:run
```

### 2. Open Dashboard

Navigate to: http://localhost:8080/

### 3. Scan the Sample Repository

- Source Type: LOCAL
- Path: `./sample-repos/mini-bank-api`
- Branch: main
- Click "Scan Repository"

### 4. Ask Questions

Try these example questions:

1. **Where is login implemented?**
2. **Which files are affected if I change user role logic?**
3. **Which APIs use UserRepository?**

### 5. View the Graph

- Enter Scan ID: 1
- Click "View Mermaid Graph"

## Troubleshooting

### Issue: Port 8080 already in use

Change the port in `src/main/resources/application.yml`:
```yaml
server:
  port: 8081
```

### Issue: Java version mismatch

Ensure Java 21 is installed:
```bash
java -version
```

### Issue: Maven not found

Use the Maven Wrapper:
```bash
.\mvnw.cmd clean install
```

## Project Structure

```
repolens/
├── src/
│   ├── main/
│   │   ├── java/com/manju/repolens/
│   │   │   ├── config/          # Configuration classes
│   │   │   ├── controller/      # REST controllers
│   │   │   ├── dto/             # Data transfer objects
│   │   │   ├── exception/       # Exception handlers
│   │   │   ├── model/           # JPA entities
│   │   │   ├── repository/      # JPA repositories
│   │   │   ├── service/         # Business logic
│   │   │   │   ├── scan/        # Scanning services
│   │   │   │   ├── graph/       # Graph query services
│   │   │   │   └── ask/         # Question answering
│   │   │   └── util/            # Utility classes
│   │   └── resources/
│   │       └── application.yml  # Application config
│   └── test/                    # Unit tests
├── sample-repos/
│   └── mini-bank-api/           # Sample repository for demo
├── pom.xml                      # Maven configuration
└── README.md                    # Project documentation
```

## Next Steps

1. Install Java 21 and Maven
2. Build the project: `mvn clean install`
3. Run the application: `mvn spring-boot:run`
4. Open the dashboard: http://localhost:8080/
5. Scan the sample repository
6. Ask questions and explore!

## Support

For issues or questions, please check the README.md or create an issue on GitHub.
