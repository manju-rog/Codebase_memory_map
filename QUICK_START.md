# RepoLens AI - Quick Start Guide

## 🚀 Get Started in 5 Minutes

### Step 1: Install Prerequisites

**Install Java 21:**
- Download from: https://adoptium.net/temurin/releases/?version=21
- Verify installation:
```bash
java -version
# Should show: openjdk version "21.x.x"
```

**Install Maven (Optional - Maven Wrapper included):**
- Download from: https://maven.apache.org/download.cgi
- Or use the included Maven Wrapper (recommended)

### Step 2: Clone and Build

```bash
# Clone the repository
git clone https://github.com/manju-rog/Codebase_memory_map.git
cd Codebase_memory_map

# Build the project (choose one)
mvn clean install              # If Maven is installed
.\mvnw.cmd clean install       # Windows with Maven Wrapper
./mvnw clean install           # Linux/Mac with Maven Wrapper
```

### Step 3: Run the Application

```bash
# Run the application (choose one)
mvn spring-boot:run            # If Maven is installed
.\mvnw.cmd spring-boot:run     # Windows with Maven Wrapper
./mvnw spring-boot:run         # Linux/Mac with Maven Wrapper
```

Wait for the application to start. You should see:
```
Started RepoLensApplication in X.XXX seconds
```

### Step 4: Open the Dashboard

Open your browser and navigate to:
```
http://localhost:8080/
```

You should see the RepoLens AI dashboard.

### Step 5: Run Your First Scan

1. **In the "Scan Repository" section:**
   - Source Type: `LOCAL`
   - Repository Path: `./sample-repos/mini-bank-api`
   - Branch: `main`
   - Click **"Scan Repository"**

2. **Wait for scan to complete** (should take 2-5 seconds)
   - You'll see: `"status": "COMPLETED"`
   - Note the `scanId` (usually `1`)

### Step 6: Ask Your First Question

1. **In the "Ask Questions" section:**
   - Scan ID: `1`
   - Question: `Where is login implemented?`
   - Click **"Ask Question"**

2. **View the Answer:**
   - You'll see the complete flow: AuthController → AuthService → UserRepository → User → users table
   - File paths with line numbers
   - Confidence score
   - Mermaid graph visualization

### Step 7: Try More Questions

Click the example question buttons:
- ✅ "Where is login implemented?"
- ✅ "Which files are affected if I change user role logic?"
- ✅ "Which APIs use UserRepository?"

### Step 8: View the Graph

1. **In the "View Graph" section:**
   - Scan ID: `1`
   - Click **"View Mermaid Graph"**

2. **See the visualization:**
   - Complete codebase graph in Mermaid format
   - Shows all relationships between components

## 🎯 What You Just Did

You successfully:
1. ✅ Scanned a Spring Boot repository
2. ✅ Built a knowledge graph with 50+ nodes and 70+ edges
3. ✅ Asked natural language questions
4. ✅ Got instant answers with file paths and relationships
5. ✅ Visualized the codebase architecture

## 🔧 Alternative Access Points

### Swagger UI (API Documentation)
```
http://localhost:8080/swagger-ui.html
```
- Interactive API documentation
- Try all APIs directly from the browser

### H2 Database Console
```
http://localhost:8080/h2-console
```
- JDBC URL: `jdbc:h2:mem:repolens`
- Username: `sa`
- Password: (leave empty)

### API Endpoints
```
POST   http://localhost:8080/api/repos/scan
GET    http://localhost:8080/api/repos/scans/{scanId}
GET    http://localhost:8080/api/repos/scans/{scanId}/graph
GET    http://localhost:8080/api/repos/scans/{scanId}/graph/mermaid
POST   http://localhost:8080/api/repos/scans/{scanId}/ask
```

## 🧪 Run Tests

```bash
# Run all tests
mvn test                    # If Maven is installed
.\mvnw.cmd test            # Windows with Maven Wrapper
./mvnw test                # Linux/Mac with Maven Wrapper
```

## 🔑 Optional: Enable AI Features

If you want to use OCI GenAI for enhanced answers:

**Windows PowerShell:**
```powershell
$env:APP_AI_ENABLED="true"
$env:OCI_COMPARTMENT_ID="your_compartment_id"
$env:OCI_CONFIG_PROFILE="DEFAULT"
$env:OCI_ENDPOINT="https://inference.generativeai.us-ashburn-1.oci.oraclecloud.com"
$env:OCI_LLAMA4_MODEL_ID="your_model_id"

# Then run
mvn spring-boot:run
```

**Linux/Mac:**
```bash
export APP_AI_ENABLED=true
export OCI_COMPARTMENT_ID="your_compartment_id"
export OCI_CONFIG_PROFILE="DEFAULT"
export OCI_ENDPOINT="https://inference.generativeai.us-ashburn-1.oci.oraclecloud.com"
export OCI_LLAMA4_MODEL_ID="your_model_id"

# Then run
mvn spring-boot:run
```

**Note:** The application works perfectly without AI credentials using intelligent fallback answers.

## 📖 Next Steps

1. **Scan Your Own Repository:**
   - Use `LOCAL` source type for local repos
   - Use `GITHUB` source type for GitHub repos (set GITHUB_TOKEN for private repos)

2. **Explore the APIs:**
   - Check Swagger UI at http://localhost:8080/swagger-ui.html
   - Try different API endpoints

3. **Read the Documentation:**
   - `README.md` - Complete project documentation
   - `SETUP.md` - Detailed setup instructions
   - `PROJECT_SUMMARY.md` - Project overview and architecture

4. **Customize:**
   - Modify `src/main/resources/application.yml` for configuration
   - Add your own extractors in `service/scan/`
   - Extend question answering in `service/ask/`

## 🐛 Troubleshooting

### Port 8080 already in use
```yaml
# Edit src/main/resources/application.yml
server:
  port: 8081  # Change to any available port
```

### Java version mismatch
```bash
java -version
# Must show Java 21
```

### Maven not found
```bash
# Use Maven Wrapper instead
.\mvnw.cmd spring-boot:run    # Windows
./mvnw spring-boot:run        # Linux/Mac
```

### Scan fails
- Check that the path exists: `./sample-repos/mini-bank-api`
- Check that you're in the project root directory
- Check application logs for detailed error messages

## 💡 Tips

1. **First time?** Start with the sample repository (`./sample-repos/mini-bank-api`)
2. **Want to scan GitHub?** Set `GITHUB_TOKEN` environment variable for private repos
3. **Need help?** Check the logs in the console where you ran `mvn spring-boot:run`
4. **Want to see the database?** Open H2 Console at http://localhost:8080/h2-console

## 🎬 Demo Script

Perfect for showing to your team:

1. **Start the app** → Show it starts in seconds
2. **Open dashboard** → Clean, simple UI
3. **Scan sample repo** → Show 24 files, 58 nodes, 79 edges
4. **Ask "Where is login implemented?"** → Show the complete answer with graph
5. **Ask "Which files are affected if I change user role logic?"** → Show impact analysis
6. **View Mermaid graph** → Show visual architecture

**Time needed:** 3-5 minutes

## 📞 Need Help?

- Check `README.md` for detailed documentation
- Check `SETUP.md` for installation help
- Check `PROJECT_SUMMARY.md` for architecture details
- Open an issue on GitHub: https://github.com/manju-rog/Codebase_memory_map

---

**You're all set! Enjoy exploring RepoLens AI! 🚀**
