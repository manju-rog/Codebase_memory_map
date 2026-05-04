package com.manju.repolens.service.ask;

import com.manju.repolens.config.AiConfig;
import com.manju.repolens.dto.AskQuestionResponse;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(name = "app.ai.enabled", havingValue = "true")
public class AiAnswerService {
    
    private final AiConfig aiConfig;
    
    public AiAnswerService(AiConfig aiConfig) {
        this.aiConfig = aiConfig;
    }
    
    public AskQuestionResponse enhanceAnswer(
            String question,
            String graphContext,
            AskQuestionResponse fallbackAnswer) {
        
        // For now, return fallback answer
        // In production, this would call OCI GenAI to enhance the answer
        // using Spring AI ChatClient with OCI configuration
        
        return fallbackAnswer;
    }
}
