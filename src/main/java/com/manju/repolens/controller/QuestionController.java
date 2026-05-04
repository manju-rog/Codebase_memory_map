package com.manju.repolens.controller;

import com.manju.repolens.dto.AskQuestionRequest;
import com.manju.repolens.dto.AskQuestionResponse;
import com.manju.repolens.service.ask.QuestionAnswerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/repos/scans/{scanId}")
@Tag(name = "Question Answering", description = "APIs for asking questions about the codebase")
public class QuestionController {
    
    private final QuestionAnswerService questionAnswerService;
    
    public QuestionController(QuestionAnswerService questionAnswerService) {
        this.questionAnswerService = questionAnswerService;
    }
    
    @PostMapping("/ask")
    @Operation(summary = "Ask a question", description = "Ask a natural language question about the codebase")
    public ResponseEntity<AskQuestionResponse> askQuestion(
            @PathVariable Long scanId,
            @Valid @RequestBody AskQuestionRequest request) {
        AskQuestionResponse response = questionAnswerService.answerQuestion(scanId, request);
        return ResponseEntity.ok(response);
    }
}
