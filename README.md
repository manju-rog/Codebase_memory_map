# 🔍 RepoLens AI

**AI-powered Codebase Memory Map for Spring Boot repositories**

RepoLens AI acts like a memory layer for a codebase. It scans a Spring Boot repository and builds a living knowledge graph so new developers can ask natural-language questions and understand code flow from API to database.

## 🎯 Problem Statement

When joining a new project, developers spend hours:
- Searching through files to understand code structure
- Tracing API endpoints to database tables
- Understanding dependencies between components
- Finding where specific features are implemented

RepoLens AI solves this by creating an intelligent codebase map that answers questions instantly.

## 🏗️ Architecture

```
┌─────────────┐
│   GitHub    │
│  Repository │
└──────┬──────┘
       │
       ▼
┌─────────────────┐
│  RepoLens AI    │
│  Scanner        │
│  - JavaParser   │
│  - JGit         │
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│  Knowledge      │
│  Graph (H2)     │
│  - Nodes        │
│  - Edges        │
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│  Question       │
│  Answering      │
│  - Graph Search │
│  - OCI GenAI    │
└─────────────────┘
```

## 🛠️ Tech Stack

- **Java 21**
- **Spring Boot 3.5.0**
- **Spring AI 1.1.5** with OCI GenAI integration
- **Maven**
- **H2 Database** (in-memory)
- **JavaParser** for Java source code analysis
- **JGit** for GitHub repository cloning
- **Swagger UI** for API documentation

## 🚀 Quick Start

### Prerequisites

- Java 21
- Maven 3.8+
- Git

### Installation

```bash
# Clone the repository
git clone https://github.com/manju-rog/Codebase_memory_map.git
cd Codebase_memory_map

# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

### Access Points

- **Dashboard**: http://localhost:8080/
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **H2 Console**: http://localhost:8080/h2-console
- **API Docs**: http://localhost:8080/api-docs

## 🔑 Environment Variables

### Required for AI Features (Optional)

```bash
export APP_AI_ENABLED=true
export OCI_COMPARTMENT_ID="ocid1.compartment.oc1..aaaaaaaacqxr3j2qbhntidg2s32gb67lv7ydpbfkvcazqvnjfsaxj3vdxr7a"
export OCI_CONFIG_PROFILE="DEFAULT"
export OCI_ENDPOINT="https://inference.generativeai.us-ashburn-1.oci.oraclecloud.com"
export OCI_LLAMA4_MODEL_ID="ocid1.generativeaimodel.oc1.iad.amaaaaaask7dceyah6tjdejjashngznsylutuhhvufukzb2g2ls54g2flsfq"
export OCI_GPT5_MODEL_ID="openai.gpt-4.1"
```

### Optional for Private GitHub Repos

```bash
export GITHUB_TOKEN="your_github_token_here"
```

**Note**: The application works perfectly without AI credentials using intelligent fallback answers based on graph search.

## 📖 API Examples

### 1. Scan a Local Repository

```bash
curl -X POST http://localhost:8080/api/repos/scan \
  -H "Content-Type: application/json" \
  -d '{
    "sourceType": "LOCAL",
    "localPath": "./sample-repos/mini-bank-api",
    "branch": "main"
  }'
```

**Response:**
```json
{
  "scanId": 1,
  "status": "COMPLETED",
  "repoName": "mini-bank-api",
  "branch": "main",
  "totalFilesScanned": 24,
  "totalNodes": 58,
  "totalEdges": 79,
  "message": "Repository scan initiated"
}
```

### 2. Get Scan Status

```bash
curl http://localhost:8080/api/repos/scans/1
```

### 3. View Knowledge Graph

```bash
curl http://localhost:8080/api/repos/scans/1/graph
```

### 4. Get Mermaid Diagram

```bash
curl http://localhost:8080/api/repos/scans/1/graph/mermaid
```

**Response:**
```
graph TD
  AuthController --> AuthService
  AuthService --> UserRepository
  AuthService --> JwtService
  UserRepository --> User
  User --> users
```

### 5. Ask Questions

```bash
curl -X POST http://localhost:8080/api/repos/scans/1/ask \
  -H "Content-Type: application/json" \
  -d '{
    "question": "Where is login implemented?"
  }'
```

**Response:**
```json
{
  "answer": "Found in controller: AuthController. Exposes endpoint: POST /api/auth/login. Uses service: AuthService. Accesses repository: UserRepository. Works with entity: User. Maps to table: users.",
  "confidence": 0.85,
  "evidence": [
    {
      "filePath": "src/main/java/com/example/auth/AuthController.java",
      "symbolName": "com.example.auth.AuthController",
      "nodeType": "CONTROLLER",
      "lineStart": 5,
      "lineEnd": 18,
      "whyRelevant": "Matched search query"
    }
  ],
  "affectedFiles": [
    {
      "filePath": "src/main/java/com/example/auth/AuthController.java",
      "reason": "CONTROLLER: AuthController"
    },
    {
      "filePath": "src/main/java/com/example/auth/AuthService.java",
      "reason": "SERVICE: AuthService"
    }
  ],
  "graphPathMermaid": "graph TD\n  N0[\"AuthController\"]\n  N0 --> N1\n  N1[\"AuthService\"]"
}
```

## 🎬 Demo Script

### For Team Lead Presentation

**Opening:**
> "Venky, I built RepoLens AI as the first ecosystem POC. It scans a Spring Boot repo and creates a codebase memory graph. Instead of opening files manually, a developer can ask natural language questions and get instant answers with exact file paths and relationships."

**Demo Steps:**

1. **Open Dashboard** (http://localhost:8080/)

2. **Scan the Sample Repo**
   - Click "Scan Repository"
   - Path: `./sample-repos/mini-bank-api`
   - Show scan results: 24 files, 58 nodes, 79 edges

3. **Ask the Killer Question**
   - Question: "Where is login implemented?"
   - Show the answer with:
     - AuthController → POST /api/auth/login
     - AuthService → UserRepository
     - JwtService for token generation
     - User entity → users table
   - Show the Mermaid graph visualization

4. **Ask Impact Analysis Question**
   - Question: "Which files are affected if I change user role logic?"
   - Show affected files:
     - User.java
     - UserRepository.java
     - AuthService.java
     - SecurityConfig.java

5. **Show the Graph**
   - View full Mermaid diagram
   - Explain the relationships

**Closing:**
> "For the first version, I kept AI optional. Even without OCI credentials, the graph-based fallback answer works. With OCI GenAI enabled, Spring AI improves the answer wording. This gives us a base ecosystem for GitHub repo scanning, code understanding, onboarding, and later PR impact analysis and AI-assisted code review."

## 🎯 Example Questions

Try these questions after scanning the sample repository:

1. **Where is login implemented?**
   - Shows: AuthController → AuthService → UserRepository → User → users table

2. **Which files are affected if I change user role logic?**
   - Shows: User.java, UserRepository.java, AuthService.java, SecurityConfig.java

3. **Which APIs use UserRepository?**
   - Shows: AuthController, AuthService

4. **Show controller to database flow for fund transfer**
   - Shows: TransferController → TransferService → AccountRepository → Account → accounts table

5. **Which config controls JWT?**
   - Shows: SecurityConfig, JwtService, application.yml

## 🧪 Running Tests

```bash
mvn test
```

Tests include:
- File discovery and exclusion logic
- Java source parsing
- Endpoint extraction
- Entity and table mapping
- Repository to entity relationships
- Graph search functionality
- Mermaid export
- Question answering fallback

## 🔮 Future Enhancements

- **GitHub PR Analysis**: Automatically analyze pull requests for impact
- **Auto PR Comments**: Bot comments on PRs with affected components
- **Slack/Teams Integration**: Onboarding bot for new developers
- **Neo4j Backend**: Scale to larger codebases with graph database
- **Vector Search**: Semantic code search using embeddings
- **GitHub Webhook Scanner**: Auto-scan on push events
- **Multi-Language Support**: Python, JavaScript, Go support
- **Architecture Visualization**: Interactive graph UI
- **Code Quality Metrics**: Complexity and coupling analysis
- **API Documentation Generation**: Auto-generate API docs from graph

## 🤝 Contributing

This is a POC project. Contributions welcome!

## 📄 License

MIT License

## 👤 Author

Manju

## 🙏 Acknowledgments

- Spring AI team for OCI GenAI integration
- JavaParser for excellent Java parsing library
- JGit for Git operations in Java

---

**Built with ❤️ for better developer onboarding**
