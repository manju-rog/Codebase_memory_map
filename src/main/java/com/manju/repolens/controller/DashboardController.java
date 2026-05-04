package com.manju.repolens.controller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;

import java.nio.charset.StandardCharsets;

@Controller
public class DashboardController {
    
    @GetMapping("/")
    public ResponseEntity<String> dashboard() {
        try {
            Resource resource = new ClassPathResource("static/index.html");
            String html = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
            return ResponseEntity.ok()
                .contentType(MediaType.TEXT_HTML)
                .body(html);
        } catch (Exception e) {
            return ResponseEntity.ok()
                .contentType(MediaType.TEXT_HTML)
                .body(getDefaultDashboard());
        }
    }
    
    private String getDefaultDashboard() {
        return """
            <!DOCTYPE html>
            <html>
            <head>
                <title>RepoLens AI</title>
                <meta charset="UTF-8">
                <style>
                    body { font-family: Arial, sans-serif; max-width: 1200px; margin: 0 auto; padding: 20px; }
                    h1 { color: #2c3e50; }
                    .section { margin: 20px 0; padding: 20px; border: 1px solid #ddd; border-radius: 5px; }
                    input, select, textarea, button { margin: 5px 0; padding: 8px; width: 100%; box-sizing: border-box; }
                    button { background: #3498db; color: white; border: none; cursor: pointer; width: auto; padding: 10px 20px; }
                    button:hover { background: #2980b9; }
                    .result { background: #ecf0f1; padding: 15px; margin: 10px 0; border-radius: 5px; }
                    .example-btn { background: #27ae60; margin: 5px; }
                    .example-btn:hover { background: #229954; }
                </style>
            </head>
            <body>
                <h1>🔍 RepoLens AI</h1>
                <p><strong>AI-powered Codebase Memory Map for Spring Boot repositories</strong></p>
                
                <div class="section">
                    <h2>Scan Repository</h2>
                    <select id="sourceType">
                        <option value="LOCAL">Local</option>
                        <option value="GITHUB">GitHub</option>
                    </select>
                    <input type="text" id="repoPath" placeholder="Repository path (e.g., ./sample-repos/mini-bank-api)" value="./sample-repos/mini-bank-api">
                    <input type="text" id="branch" placeholder="Branch (default: main)" value="main">
                    <button onclick="scanRepo()">Scan Repository</button>
                    <div id="scanResult" class="result" style="display:none;"></div>
                </div>
                
                <div class="section">
                    <h2>Ask Questions</h2>
                    <input type="number" id="scanId" placeholder="Scan ID" value="1">
                    <textarea id="question" rows="3" placeholder="Ask a question about the codebase..."></textarea>
                    <button onclick="askQuestion()">Ask Question</button>
                    
                    <h3>Example Questions:</h3>
                    <button class="example-btn" onclick="setQuestion('Where is login implemented?')">Where is login implemented?</button>
                    <button class="example-btn" onclick="setQuestion('Which files are affected if I change user role logic?')">Which files are affected if I change user role logic?</button>
                    <button class="example-btn" onclick="setQuestion('Which APIs use UserRepository?')">Which APIs use UserRepository?</button>
                    
                    <div id="answerResult" class="result" style="display:none;"></div>
                </div>
                
                <div class="section">
                    <h2>View Graph</h2>
                    <input type="number" id="graphScanId" placeholder="Scan ID" value="1">
                    <button onclick="viewGraph()">View Mermaid Graph</button>
                    <div id="graphResult" class="result" style="display:none;"></div>
                </div>
                
                <script>
                    async function scanRepo() {
                        const sourceType = document.getElementById('sourceType').value;
                        const path = document.getElementById('repoPath').value;
                        const branch = document.getElementById('branch').value;
                        
                        const body = sourceType === 'LOCAL' 
                            ? { sourceType, localPath: path, branch }
                            : { sourceType, repoUrl: path, branch };
                        
                        try {
                            const response = await fetch('/api/repos/scan', {
                                method: 'POST',
                                headers: { 'Content-Type': 'application/json' },
                                body: JSON.stringify(body)
                            });
                            const data = await response.json();
                            document.getElementById('scanResult').style.display = 'block';
                            document.getElementById('scanResult').innerHTML = '<pre>' + JSON.stringify(data, null, 2) + '</pre>';
                            document.getElementById('scanId').value = data.scanId;
                            document.getElementById('graphScanId').value = data.scanId;
                        } catch (error) {
                            alert('Error: ' + error.message);
                        }
                    }
                    
                    function setQuestion(q) {
                        document.getElementById('question').value = q;
                    }
                    
                    async function askQuestion() {
                        const scanId = document.getElementById('scanId').value;
                        const question = document.getElementById('question').value;
                        
                        try {
                            const response = await fetch(`/api/repos/scans/${scanId}/ask`, {
                                method: 'POST',
                                headers: { 'Content-Type': 'application/json' },
                                body: JSON.stringify({ question })
                            });
                            const data = await response.json();
                            document.getElementById('answerResult').style.display = 'block';
                            document.getElementById('answerResult').innerHTML = 
                                '<h3>Answer:</h3><p>' + data.answer + '</p>' +
                                '<h3>Confidence:</h3><p>' + (data.confidence * 100).toFixed(0) + '%</p>' +
                                '<h3>Evidence:</h3><pre>' + JSON.stringify(data.evidence, null, 2) + '</pre>' +
                                '<h3>Mermaid Graph:</h3><pre>' + data.graphPathMermaid + '</pre>';
                        } catch (error) {
                            alert('Error: ' + error.message);
                        }
                    }
                    
                    async function viewGraph() {
                        const scanId = document.getElementById('graphScanId').value;
                        
                        try {
                            const response = await fetch(`/api/repos/scans/${scanId}/graph/mermaid`);
                            const mermaid = await response.text();
                            document.getElementById('graphResult').style.display = 'block';
                            document.getElementById('graphResult').innerHTML = '<pre>' + mermaid + '</pre>';
                        } catch (error) {
                            alert('Error: ' + error.message);
                        }
                    }
                </script>
            </body>
            </html>
            """;
    }
}
