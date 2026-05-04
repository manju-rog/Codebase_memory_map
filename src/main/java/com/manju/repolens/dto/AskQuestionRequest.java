package com.manju.repolens.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AskQuestionRequest(
    @NotBlank(message = "question is required")
    @Size(min = 3, max = 500, message = "question must be between 3 and 500 characters")
    String question
) {
}
