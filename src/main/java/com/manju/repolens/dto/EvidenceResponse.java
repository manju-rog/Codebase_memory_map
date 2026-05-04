package com.manju.repolens.dto;

import com.manju.repolens.model.NodeType;

public record EvidenceResponse(
    String filePath,
    String symbolName,
    NodeType nodeType,
    Integer lineStart,
    Integer lineEnd,
    String whyRelevant
) {
}
