package com.manju.repolens.service.ask;

import com.manju.repolens.config.AiConfig;
import com.manju.repolens.dto.AskQuestionResponse;
import com.manju.repolens.dto.AffectedFileResponse;
import com.manju.repolens.dto.EvidenceResponse;
import com.oracle.bmc.ConfigFileReader;
import com.oracle.bmc.auth.AuthenticationDetailsProvider;
import com.oracle.bmc.auth.ConfigFileAuthenticationDetailsProvider;
import com.oracle.bmc.generativeaiinference.GenerativeAiInferenceClient;
import com.oracle.bmc.generativeaiinference.model.*;
import com.oracle.bmc.generativeaiinference.requests.ChatRequest;
import com.oracle.bmc.generativeaiinference.responses.ChatResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@ConditionalOnProperty(name = "app.ai.enabled", havingValue = "true")
public class AiAnswerService {
    
    private static final Logger log = LoggerFactory.getLogger(AiAnswerService.class);
    
    private final AiConfig aiConfig;
    private final GenerativeAiInferenceClient aiClient;
    private final String compartmentId;
    private final String modelId;
    
    public AiAnswerService(
            AiConfig aiConfig,
            @Value("${spring.ai.oci.genai.compartment-id}") String compartmentId,
            @Value("${spring.ai.oci.genai.endpoint}") String endpoint,
            @Value("${spring.ai.oci.genai.chat.options.model}") String modelId,
            @Value("${app.oci.config-file}") String configFile) {
        this.aiConfig = aiConfig;
        this.compartmentId = compartmentId;
        this.modelId = modelId;
        
        try {
            ConfigFileReader.ConfigFile config = ConfigFileReader.parse(configFile, "DEFAULT");
            AuthenticationDetailsProvider provider = new ConfigFileAuthenticationDetailsProvider(config);
            
            this.aiClient = GenerativeAiInferenceClient.builder()
                .endpoint(endpoint)
                .build(provider);
            
            log.info("OCI GenAI client initialized successfully");
        } catch (Exception e) {
            log.error("Failed to initialize OCI GenAI client: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to initialize OCI GenAI", e);
        }
    }
    
    public AskQuestionResponse enhanceAnswer(
            String question,
            String graphContext,
            AskQuestionResponse fallbackAnswer) {
        
        if (aiClient == null) {
            log.warn("AI client not available, using fallback answer");
            return fallbackAnswer;
        }
        
        try {
            String prompt = buildPrompt(question, graphContext, fallbackAnswer);
            
            // Create chat request
            List<ChatContent> contentList = new ArrayList<>();
            contentList.add(TextContent.builder().text(prompt).build());
            
            List<Message> messages = new ArrayList<>();
            messages.add(UserMessage.builder()
                .content(contentList)
                .build());
            
            ChatDetails chatDetails = ChatDetails.builder()
                .servingMode(OnDemandServingMode.builder()
                    .modelId(modelId)
                    .build())
                .compartmentId(compartmentId)
                .chatRequest(GenericChatRequest.builder()
                    .messages(messages)
                    .maxTokens(2000)
                    .temperature(0.3)
                    .build())
                .build();
            
            ChatRequest request = ChatRequest.builder()
                .chatDetails(chatDetails)
                .build();
            
            ChatResponse response = aiClient.chat(request);
            
            if (response.getChatResult() != null && 
                response.getChatResult().getChatResponse() != null) {
                
                String enhancedAnswer = extractAnswer(response.getChatResult().getChatResponse());
                
                if (enhancedAnswer != null && !enhancedAnswer.isBlank()) {
                    log.info("Successfully enhanced answer with OCI GenAI");
                    return new AskQuestionResponse(
                        enhancedAnswer.trim(),
                        fallbackAnswer.confidence(),
                        fallbackAnswer.evidence(),
                        fallbackAnswer.affectedFiles(),
                        fallbackAnswer.graphPathMermaid()
                    );
                }
            }
            
        } catch (Exception e) {
            log.error("Failed to enhance answer with AI: {}", e.getMessage(), e);
        }
        
        return fallbackAnswer;
    }
    
    private String extractAnswer(BaseChatResponse chatResponse) {
        if (chatResponse instanceof CohereChatResponse) {
            CohereChatResponse cohereResponse = (CohereChatResponse) chatResponse;
            return cohereResponse.getText();
        } else if (chatResponse instanceof GenericChatResponse) {
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
        return null;
    }
    
    private String buildPrompt(String question, String graphContext, AskQuestionResponse fallbackAnswer) {
        StringBuilder prompt = new StringBuilder();
        
        prompt.append("You are a code analysis assistant helping developers understand a Spring Boot codebase.\n\n");
        prompt.append("Question: ").append(question).append("\n\n");
        prompt.append("Codebase Knowledge Graph:\n").append(graphContext).append("\n\n");
        prompt.append("Base Analysis:\n").append(fallbackAnswer.answer()).append("\n\n");
        
        if (!fallbackAnswer.evidence().isEmpty()) {
            prompt.append("Evidence Files:\n");
            for (EvidenceResponse evidence : fallbackAnswer.evidence()) {
                prompt.append("- ").append(evidence.filePath())
                      .append(" (").append(evidence.nodeType()).append("): ")
                      .append(evidence.symbolName()).append("\n");
            }
            prompt.append("\n");
        }
        
        if (!fallbackAnswer.affectedFiles().isEmpty()) {
            prompt.append("Affected Files:\n");
            for (AffectedFileResponse file : fallbackAnswer.affectedFiles()) {
                prompt.append("- ").append(file.filePath())
                      .append(": ").append(file.reason()).append("\n");
            }
            prompt.append("\n");
        }
        
        prompt.append("Instructions:\n");
        prompt.append("1. Provide a clear, concise answer to the question\n");
        prompt.append("2. Explain the code flow from controller to database if applicable\n");
        prompt.append("3. Mention specific class names, method names, and file paths\n");
        prompt.append("4. Keep the answer under 200 words\n");
        prompt.append("5. Be specific and technical\n");
        prompt.append("6. Do not make up information - only use the provided context\n\n");
        prompt.append("Enhanced Answer:");
        
        return prompt.toString();
    }
}
