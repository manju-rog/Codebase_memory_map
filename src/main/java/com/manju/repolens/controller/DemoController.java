package com.manju.repolens.controller;

import com.oracle.bmc.ConfigFileReader;
import com.oracle.bmc.auth.AuthenticationDetailsProvider;
import com.oracle.bmc.auth.ConfigFileAuthenticationDetailsProvider;
import com.oracle.bmc.generativeaiinference.GenerativeAiInferenceClient;
import com.oracle.bmc.generativeaiinference.model.*;
import com.oracle.bmc.generativeaiinference.requests.ChatRequest;
import com.oracle.bmc.generativeaiinference.responses.ChatResponse;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.nio.file.Files;
import java.util.*;

@RestController
@RequestMapping("/api/demo")
public class DemoController {
    
    @GetMapping("/ask")
    public Map<String, Object> askQuestion(@RequestParam String question) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // Step 1: Scan the test program
            result.put("step1", "Scanning test program...");
            Map<String, String> codeFiles = scanTestProgram();
            result.put("filesScanned", codeFiles.keySet());
            
            // Step 2: Build knowledge graph
            result.put("step2", "Building knowledge graph...");
            String graphDescription = buildGraph(codeFiles);
            result.put("graph", graphDescription);
            
            // Step 3: Generate Mermaid diagram
            String mermaidGraph = generateMermaidGraph();
            result.put("mermaidGraph", mermaidGraph);
            
            // Step 4: Ask AI with context
            result.put("step3", "Asking OCI AI...");
            String aiAnswer = askAI(question, graphDescription, codeFiles);
            result.put("answer", aiAnswer);
            
            result.put("status", "success");
            result.put("question", question);
            
        } catch (Exception e) {
            result.put("status", "error");
            result.put("error", e.getMessage());
            e.printStackTrace();
        }
        
        return result;
    }
    
    private Map<String, String> scanTestProgram() throws Exception {
        Map<String, String> files = new HashMap<>();
        File dir = new File("./test-program");
        
        if (dir.exists() && dir.isDirectory()) {
            for (File file : dir.listFiles()) {
                if (file.getName().endsWith(".java")) {
                    String content = Files.readString(file.toPath());
                    files.put(file.getName(), content);
                }
            }
        }
        
        return files;
    }
    
    private String buildGraph(Map<String, String> codeFiles) {
        StringBuilder graph = new StringBuilder();
        graph.append("Code Structure:\n\n");
        
        for (Map.Entry<String, String> entry : codeFiles.entrySet()) {
            String fileName = entry.getKey();
            String content = entry.getValue();
            
            graph.append("File: ").append(fileName).append("\n");
            
            // Extract class name
            if (content.contains("class ")) {
                int start = content.indexOf("class ") + 6;
                int end = content.indexOf("{", start);
                if (end > start) {
                    String className = content.substring(start, end).trim();
                    graph.append("  Class: ").append(className).append("\n");
                }
            }
            
            // Extract methods
            String[] lines = content.split("\n");
            for (String line : lines) {
                line = line.trim();
                if (line.contains("public ") && line.contains("(") && !line.contains("class")) {
                    graph.append("    Method: ").append(line).append("\n");
                }
            }
            
            // Extract dependencies
            for (String line : lines) {
                line = line.trim();
                if (line.contains("private ") && line.contains(";")) {
                    graph.append("    Uses: ").append(line).append("\n");
                }
            }
            
            graph.append("\n");
        }
        
        return graph.toString();
    }
    
    private String generateMermaidGraph() {
        StringBuilder mermaid = new StringBuilder();
        mermaid.append("graph TD\n");
        mermaid.append("    A[LoginService] --> B[UserRepository]\n");
        mermaid.append("    A --> C[TokenService]\n");
        mermaid.append("    B --> D[User]\n");
        mermaid.append("    C --> D\n");
        mermaid.append("    A --> E[LoginResponse]\n");
        mermaid.append("    \n");
        mermaid.append("    style A fill:#6366f1,stroke:#4f46e5,color:#fff\n");
        mermaid.append("    style B fill:#8b5cf6,stroke:#7c3aed,color:#fff\n");
        mermaid.append("    style C fill:#8b5cf6,stroke:#7c3aed,color:#fff\n");
        
        return mermaid.toString();
    }
    
    private String askAI(String question, String graphDescription, Map<String, String> codeFiles) throws Exception {
        // OCI Configuration
        String configFile = "./OCI_ApiKey/config";
        String compartmentId = "ocid1.compartment.oc1..aaaaaaaacqxr3j2qbhntidg2s32gb67lv7ydpbfkvcazqvnjfsaxj3vdxr7a";
        String endpoint = "https://inference.generativeai.us-ashburn-1.oci.oraclecloud.com";
        String modelId = "openai.gpt-4.1";
        
        // Build context
        StringBuilder context = new StringBuilder();
        context.append("You are analyzing a Java codebase.\n\n");
        context.append("Question: ").append(question).append("\n\n");
        context.append("Code Structure:\n").append(graphDescription).append("\n\n");
        context.append("Source Code:\n\n");
        
        for (Map.Entry<String, String> entry : codeFiles.entrySet()) {
            context.append("=== ").append(entry.getKey()).append(" ===\n");
            context.append(entry.getValue()).append("\n\n");
        }
        
        context.append("\nBased on the code above, answer the question in detail. ");
        context.append("Explain which classes and methods are involved, and how they work together.");
        
        // Load config
        ConfigFileReader.ConfigFile config = ConfigFileReader.parse(configFile, "DEFAULT");
        AuthenticationDetailsProvider provider = new ConfigFileAuthenticationDetailsProvider(config);
        
        // Create client
        GenerativeAiInferenceClient client = GenerativeAiInferenceClient.builder()
            .endpoint(endpoint)
            .build(provider);
        
        // Create message
        List<ChatContent> contentList = new ArrayList<>();
        contentList.add(TextContent.builder().text(context.toString()).build());
        
        List<Message> messages = new ArrayList<>();
        messages.add(UserMessage.builder().content(contentList).build());
        
        // Create request
        ChatDetails chatDetails = ChatDetails.builder()
            .servingMode(OnDemandServingMode.builder().modelId(modelId).build())
            .compartmentId(compartmentId)
            .chatRequest(GenericChatRequest.builder()
                .messages(messages)
                .maxTokens(1000)
                .temperature(0.3)
                .build())
            .build();
        
        ChatRequest request = ChatRequest.builder().chatDetails(chatDetails).build();
        
        // Call API
        ChatResponse response = client.chat(request);
        
        // Extract answer
        if (response.getChatResult() != null && response.getChatResult().getChatResponse() != null) {
            BaseChatResponse chatResponse = response.getChatResult().getChatResponse();
            
            if (chatResponse instanceof GenericChatResponse) {
                GenericChatResponse genericResponse = (GenericChatResponse) chatResponse;
                if (genericResponse.getChoices() != null && !genericResponse.getChoices().isEmpty()) {
                    ChatChoice choice = genericResponse.getChoices().get(0);
                    if (choice.getMessage() != null && choice.getMessage() instanceof AssistantMessage) {
                        List<ChatContent> contents = ((AssistantMessage) choice.getMessage()).getContent();
                        if (contents != null && !contents.isEmpty() && contents.get(0) instanceof TextContent) {
                            return ((TextContent) contents.get(0)).getText();
                        }
                    }
                }
            }
        }
        
        return "Could not get answer from AI";
    }
}
