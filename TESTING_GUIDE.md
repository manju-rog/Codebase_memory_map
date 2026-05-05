# 🧪 RepoLens AI - Testing Guide

## ✅ **WHAT'S WORKING:**

### 1. Application Status
- ✅ Spring Boot running on port 8080
- ✅ OCI GenAI client initialized
- ✅ Repository scanned (20 files, 34 nodes, 15 relationships)
- ✅ REST APIs responding
- ✅ UI accessible at http://localhost:8080/

### 2. Confirmed Working APIs
```powershell
# Health Check
Invoke-WebRequest -Uri "http://localhost:8080/actuator/health"
# Result: 200 OK ✓

# Question API (basic test)
$body = @{ scanId = 1; question = "Where is login implemented?" } | ConvertTo-Json
Invoke-RestMethod -Uri "http://localhost:8080/api/question/ask" -Method POST -Body $body -ContentType "application/json"
# Result: Returns answer ✓
```

---

## 🎯 **HOW TO TEST IN TERMINAL:**

### Test 1: Check Application Health
```powershell
Invoke-WebRequest -Uri "http://localhost:8080/actuator/health" -UseBasicParsing
```
**Expected**: Status 200

### Test 2: Get Dashboard Stats
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/dashboard/stats"
```
**Expected**: JSON with totalScans, totalNodes, totalEdges

### Test 3: Ask a Question
```powershell
$body = @{
    scanId = 1
    question = "Where is login implemented?"
} | ConvertTo-Json

$response = Invoke-RestMethod -Uri "http://localhost:8080/api/question/ask" `
    -Method POST `
    -Body $body `
    -ContentType "application/json"

Write-Host "Answer: $($response.answer)"
Write-Host "Confidence: $([math]::Round($response.confidence * 100))%"
```

### Test 4: Get Graph Data
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/graph/1"
```
**Expected**: JSON with nodes and edges

---

## 🌐 **HOW TO TEST IN BROWSER:**

### Option 1: Use the UI
1. Open: **http://localhost:8080/**
2. Type question: "Where is login implemented?"
3. Press Enter
4. View answer and graph

### Option 2: Use Swagger UI
1. Open: **http://localhost:8080/swagger-ui.html**
2. Try the `/api/question/ask` endpoint
3. Use scanId: 1
4. Enter your question

---

## 📊 **SAMPLE QUESTIONS TO TEST:**

```powershell
# Test different types of questions
$questions = @(
    "Where is login implemented?",
    "What REST endpoints are available?",
    "Show me authentication flow",
    "Find UserRepository",
    "How does transfer work?"
)

foreach ($q in $questions) {
    Write-Host "`nQ: $q" -ForegroundColor Yellow
    
    $body = @{ scanId = 1; question = $q } | ConvertTo-Json
    $r = Invoke-RestMethod -Uri "http://localhost:8080/api/question/ask" `
        -Method POST -Body $body -ContentType "application/json"
    
    Write-Host "A: $($r.answer.Substring(0, [Math]::Min(100, $r.answer.Length)))..."
    Write-Host "Confidence: $([math]::Round($r.confidence * 100))%"
}
```

---

## 🔧 **IF SOMETHING ISN'T WORKING:**

### Problem: Application not responding
**Solution**:
```powershell
# Stop current process
# Restart application
$env:JAVA_HOME = "C:\Program Files\Java\jdk-17"
.\mvnw.cmd spring-boot:run
```

### Problem: "No scan found"
**Solution**:
```powershell
# Rescan the repository
$body = @{
    repositoryPath = "./sample-repos/mini-bank-api"
    sourceType = "LOCAL"
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:8080/api/scan" `
    -Method POST -Body $body -ContentType "application/json"
```

### Problem: 500 Internal Server Error
**Check**:
1. Application logs in the terminal
2. Database connection (H2 should be in-memory)
3. OCI credentials are valid

---

## 🎨 **UI FEATURES:**

### Current UI (Minimal Design)
- **Search Bar**: Type your question
- **Answer Section**: Shows AI response with confidence
- **Visual Graph**: Mermaid diagram of code relationships
- **Auto-scan**: Automatically scans on page load

### UI Location
- File: `src/main/resources/static/index.html`
- URL: http://localhost:8080/

---

## 🚀 **PRODUCTION CHECKLIST:**

- [x] Application compiles
- [x] Application starts
- [x] OCI GenAI connected
- [x] Repository scanning works
- [x] Graph building works
- [x] Question answering works
- [x] UI is accessible
- [x] REST APIs functional
- [ ] AI enhancement verified (needs testing)
- [ ] Visual graphs rendering (needs verification)

---

## 📝 **QUICK REFERENCE:**

### URLs
- **UI**: http://localhost:8080/
- **Swagger**: http://localhost:8080/swagger-ui.html
- **H2 Console**: http://localhost:8080/h2-console
- **Health**: http://localhost:8080/actuator/health

### API Endpoints
- `POST /api/scan` - Scan repository
- `POST /api/question/ask` - Ask question
- `GET /api/graph/{scanId}` - Get graph
- `GET /api/dashboard/stats` - Get statistics

### Configuration
- **Config**: `src/main/resources/application.yml`
- **OCI Creds**: `./OCI_ApiKey/config`
- **Port**: 8080
- **Database**: H2 in-memory

---

## 🎯 **NEXT STEPS:**

1. **Test in Browser**: Open http://localhost:8080/ and try asking questions
2. **Verify Graphs**: Check if Mermaid diagrams render
3. **Test AI Enhancement**: Verify OCI Llama 4 responses
4. **Try Different Questions**: Test various query types
5. **Check Performance**: Monitor response times

---

**Status**: Application is running and functional! 🎉
**Last Updated**: May 5, 2026
