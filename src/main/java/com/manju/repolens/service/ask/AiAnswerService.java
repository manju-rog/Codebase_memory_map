package com.manju.repolens.service.ask;

import com.manju.repolens.config.AiConfig;
import com.manju.repolens.dto.AskQuestionResponse;
import com.manju.repolens.dto.AffectedFileResponse;
import com.manju.repolens.dto.EvidenceResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@ConditionalOnProperty(name = "app.ai.enabled", havingValue = "true")
public class AiAnswerService {
    
    private static final Logger log = LoggerFactory.getLogger(AiAnswerService.class);
    
    private final AiConfig aiConfig;
    
    @Autowired(required = false)
    private ChatClient.Builder chatClientBuilder;
    
    public AiAnswerService(AiConfig aiConfig) {
        this.aiConfig = aiConfig;
    }
    
    public AskQuestionResponse enhanceAnswer(
            String question,
            String graphContext,
            AskQuestionResponse fallbackAnswer) {
        
        if (chatClientBuilder == null) {
            log.warn("ChatClient not available, using fallback answer");
            return fallbackAnswer;
        }
        
        try {
            String prompt = buildPrompt(question, graphContext, fallbackAnswer);
            
            ChatClient chatClient = chatClientBuilder.build();
            
            String enhancedAnswer = chatClient.prompt()
                .user(prompt)
                .call()
                .content();
            
            if (enhancedAnswer != null && !enhancedAnswer.isBlank()) {
                return new AskQuestionResponse(
                    enhancedAnswer.trim(),
                    fallbackAnswer.confidence(),
                    fallbackAnswer.evidence(),
                    fallbackAnswer.affectedFiles(),
                    fallbackAnswer.graphPathMermaid()
                );
            }
            
        } catch (Exception e) {
            log.error("Failed to enhance answer with AI: {}", e.getMessage(), e);
        }
        
        return fallbackAnswer;
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
