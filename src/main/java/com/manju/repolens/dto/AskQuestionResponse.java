package com.manju.repolens.dto;

import java.util.List;

public record AskQuestionResponse(
    String answer,
    double confidence,
    List<EvidenceResponse> evidence,
    List<AffectedFileResponse> affectedFiles,
    String graphPathMermaid
) {
}
