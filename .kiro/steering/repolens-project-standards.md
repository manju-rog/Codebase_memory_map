---
inclusion: auto
---

# RepoLens AI Project Standards

## Project Information
- **Name**: RepoLens AI
- **Subtitle**: AI-powered Codebase Memory Map for Spring Boot repositories
- **Java Version**: 21
- **Spring Boot Version**: 3.5.x
- **Spring AI Version**: 1.1.5
- **Build Tool**: Maven

## Technology Stack
- Spring Web
- Spring Data JPA
- H2 Database
- Spring Validation
- Spring Actuator
- Spring AI 1.1.5 (optional, disabled by default)
- JavaParser for Java source parsing
- JGit for GitHub repository cloning
- springdoc-openapi for Swagger UI
- Jackson for JSON
- JUnit 5 and Spring Boot Test

## Coding Standards

### Architecture
- Use clean layered architecture
- Controllers handle HTTP and validation only
- Services contain business logic
- Repository interfaces handle persistence only
- Split scanner logic into small focused services

### Code Style
- **NO Lombok** - Use explicit constructors
- Use Java records for DTOs where possible
- Clear, descriptive class names
- Beginner-friendly and demo-friendly code
- Every public API must have request/response DTOs
- Consistent error responses
- Use validation annotations for request DTOs

### Security Rules
- **NEVER hardcode secrets, tokens, or API keys**
- Use environment variables for all credentials:
  - `OCI_COMPARTMENT_ID` for OCI compartment
  - `OCI_CONFIG_PROFILE` for OCI config profile
  - `OCI_ENDPOINT` for OCI endpoint
  - `OCI_LLAMA4_MODEL_ID` for OCI Llama model
  - `OCI_GPT5_MODEL_ID` for OCI GPT model
  - `OCI_EMBED_MODEL_ID` for OCI embedding model
  - `GITHUB_TOKEN` for private GitHub repos
  - `APP_AI_ENABLED=true` to enable AI features
- App must start and work without AI credentials using fallback
- Tests must NOT call GitHub or OCI APIs

### Package Structure
Base package: `com.manju.repolens`

```
com.manju.repolens
├── config
├── controller
├── dto
├── exception
├── model
├── repository
├── service
│   ├── scan
│   ├── graph
│   └── ask
└── util
```

### Testing Requirements
- Tests must pass before completion
- No external API calls in tests
- Use realistic test data
- Test core extraction and graph logic

### Documentation
- Comprehensive README with:
  - Setup instructions
  - Architecture overview
  - API examples
  - Demo script
  - Sample questions
- Swagger UI for API documentation
- Clear error messages

### Build Requirements
- Run `mvn clean test` before finishing
- Fix all compile errors
- Fix all test failures
- Ensure app starts without AI credentials
